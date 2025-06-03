package org.FitnessApp1.model;

import java.sql.*;
import java.time.LocalDate;

public class CalendarDAO {

    public String getNoteForDate(LocalDate date, int kontoid) {
        String sql = "SELECT anteckningar FROM kalender WHERE datum = ? AND kontoid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            stmt.setInt(2, kontoid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("anteckningar");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrUpdate(LocalDate date, int kontoid, String note) {
        String checkSql = "SELECT kalenderid FROM kalender WHERE datum = ? AND kontoid = ?";
        String insertSql = "INSERT INTO kalender (datum, anteckningar, kontoid) VALUES (?, ?, ?)";
        String updateSql = "UPDATE kalender SET anteckningar = ? WHERE kalenderid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setDate(1, Date.valueOf(date));
            checkStmt.setInt(2, kontoid);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int kalenderID = rs.getInt("kalenderid");
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, note);
                    updateStmt.setInt(2, kalenderID);
                    int updatedRows = updateStmt.executeUpdate();
                    return updatedRows > 0;
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setDate(1, Date.valueOf(date));
                    insertStmt.setString(2, note);
                    insertStmt.setInt(3, kontoid);
                    int insertedRows = insertStmt.executeUpdate();
                    return insertedRows > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNote(LocalDate date, int kontoid) {
        String sql = "DELETE FROM kalender WHERE datum = ? AND kontoid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            stmt.setInt(2, kontoid);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
