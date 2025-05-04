package org.FitnessApp1.model;

import java.sql.*;
import java.time.LocalDate;

public class CalendarDAO {

    // Hämta anteckning för ett specifikt datum
    public String getNoteForDate(LocalDate datum) {
        String sql = "SELECT anteckningar FROM kalender WHERE datum = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("anteckningar");
            }

        } catch (SQLException e) {
            System.err.println("Kunde inte hämta anteckning: " + e.getMessage());
        }

        return null;
    }

    // Lägg till ny anteckning
    public boolean insertNote(LocalDate datum, String anteckning) {
        String sql = "INSERT INTO kalender (datum, anteckningar) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            stmt.setString(2, anteckning);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte lägga till anteckning: " + e.getMessage());
            return false;
        }
    }

    // Uppdatera befintlig anteckning
    public boolean updateNote(LocalDate datum, String anteckning) {
        String sql = "UPDATE kalender SET anteckningar = ? WHERE datum = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anteckning);
            stmt.setDate(2, Date.valueOf(datum));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte uppdatera anteckning: " + e.getMessage());
            return false;
        }
    }

    // Spara eller uppdatera (kombinerad metod)
    public boolean saveOrUpdate(LocalDate datum, String anteckning) {
        if (getNoteForDate(datum) != null) {
            return updateNote(datum, anteckning);
        } else {
            return insertNote(datum, anteckning);
        }
    }

    // Ta bort anteckning
    public boolean deleteNote(LocalDate datum) {
        String sql = "DELETE FROM kalender WHERE datum = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte ta bort anteckning: " + e.getMessage());
            return false;
        }
    }
}
