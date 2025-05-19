package org.FitnessApp1.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarDAO {

    public CalendarDAO() {
        // Check if the table exists when the DAO is created
        checkTableExists();
    }

    // Check if the kalender table exists, and create it if it doesn't
    private void checkTableExists() {
        System.out.println("Checking if kalender table exists...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "kalender", null);

            if (!tables.next()) {
                System.out.println("kalender table does not exist, creating it...");
                createTable(conn);
            } else {
                System.out.println("kalender table exists, checking columns...");
                checkColumns(conn);
            }

        } catch (SQLException e) {
            System.err.println("Error checking table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create the kalender table
    private void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE kalender (" +
                     "kalenderid SERIAL PRIMARY KEY, " +
                     "datum DATE NOT NULL, " +
                     "anteckningar TEXT, " +
                     "kontoid INTEGER NOT NULL, " +
                     "FOREIGN KEY (kontoid) REFERENCES konto(kontoid))";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("kalender table created successfully");
        }
    }

    // Check if the kalender table has the required columns
    private void checkColumns(Connection conn) throws SQLException {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = 'kalender'";
        ArrayList<String> columns = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                columns.add(rs.getString("column_name").toLowerCase());
            }

            System.out.println("kalender table columns: " + columns);

            // Check if all required columns exist
            boolean hasKalenderID = columns.contains("kalenderid");
            boolean hasDatum = columns.contains("datum");
            boolean hasAnteckningar = columns.contains("anteckningar");
            boolean hasKontoID = columns.contains("kontoid");

            System.out.println("Has kalenderID: " + hasKalenderID);
            System.out.println("Has datum: " + hasDatum);
            System.out.println("Has anteckningar: " + hasAnteckningar);
            System.out.println("Has kontoID: " + hasKontoID);

            if (!hasKalenderID || !hasDatum || !hasAnteckningar || !hasKontoID) {
                System.err.println("kalender table is missing required columns!");
            }
        }
    }

    // Hämta anteckning för ett specifikt datum
    public String getNoteForDate(LocalDate datum) {
        // Kontrollera om användaren är inloggad
        int kontoID = SessionManager.getAktivtKontoID();
        System.out.println("getNoteForDate: kontoID = " + kontoID);

        if (kontoID == -1) {
            System.err.println("Kunde inte hämta anteckning: Ingen användare är inloggad");
            return null;
        }

        String sql = "SELECT anteckningar FROM kalender WHERE datum = ? AND kontoid = ?";
        System.out.println("getNoteForDate: SQL = " + sql);
        System.out.println("getNoteForDate: datum = " + datum + ", kontoID = " + kontoID);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            stmt.setInt(2, kontoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String note = rs.getString("anteckningar");
                System.out.println("getNoteForDate: found note = " + note);
                return note;
            } else {
                System.out.println("getNoteForDate: no note found");
            }

        } catch (SQLException e) {
            System.err.println("Kunde inte hämta anteckning: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Lägg till ny anteckning
    public boolean insertNote(LocalDate datum, String anteckning) {
        // Kontrollera om användaren är inloggad
        int kontoID = SessionManager.getAktivtKontoID();
        System.out.println("insertNote: kontoID = " + kontoID);

        if (kontoID == -1) {
            System.err.println("Kunde inte lägga till anteckning: Ingen användare är inloggad");
            return false;
        }

        // First check if a note already exists for this date and user
        String checkSql = "SELECT COUNT(*) FROM kalender WHERE datum = ? AND kontoid = ?";
        System.out.println("insertNote: checkSql = " + checkSql);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if entry already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setDate(1, Date.valueOf(datum));
                checkStmt.setInt(2, kontoID);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("insertNote: Note already exists, updating instead");
                    // Note already exists, update it instead
                    return updateNote(datum, anteckning);
                }
            }

            // If we get here, no note exists, so insert a new one
            String sql = "INSERT INTO kalender (datum, anteckningar, kontoid) VALUES (?, ?, ?)";
            System.out.println("insertNote: SQL = " + sql);
            System.out.println("insertNote: datum = " + datum + ", anteckning = " + anteckning + ", kontoID = " + kontoID);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(datum));
                stmt.setString(2, anteckning);
                stmt.setInt(3, kontoID);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("insertNote: rowsAffected = " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Kunde inte lägga till anteckning: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Uppdatera befintlig anteckning
    public boolean updateNote(LocalDate datum, String anteckning) {
        // Kontrollera om användaren är inloggad
        int kontoID = SessionManager.getAktivtKontoID();
        System.out.println("updateNote: kontoID = " + kontoID);

        if (kontoID == -1) {
            System.err.println("Kunde inte uppdatera anteckning: Ingen användare är inloggad");
            return false;
        }

        String sql = "UPDATE kalender SET anteckningar = ? WHERE datum = ? AND kontoid = ?";
        System.out.println("updateNote: SQL = " + sql);
        System.out.println("updateNote: datum = " + datum + ", anteckning = " + anteckning + ", kontoID = " + kontoID);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anteckning);
            stmt.setDate(2, Date.valueOf(datum));
            stmt.setInt(3, kontoID);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("updateNote: rowsAffected = " + rowsAffected);

            if (rowsAffected == 0) {
                // No rows were updated, which means the note doesn't exist
                // Try to insert it instead
                System.out.println("updateNote: No rows updated, trying to insert instead");
                return insertNote(datum, anteckning);
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte uppdatera anteckning: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Spara eller uppdatera (kombinerad metod)
    public boolean saveOrUpdate(LocalDate datum, String anteckning) {
        // Kontrollera om användaren är inloggad
        int kontoID = SessionManager.getAktivtKontoID();
        System.out.println("saveOrUpdate: kontoID = " + kontoID);

        if (kontoID == -1) {
            System.err.println("Kunde inte spara anteckning: Ingen användare är inloggad");
            return false;
        }

        // Use a more direct approach to check if a note exists and update/insert accordingly
        String checkSql = "SELECT COUNT(*) FROM kalender WHERE datum = ? AND kontoid = ?";
        System.out.println("saveOrUpdate: checkSql = " + checkSql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setDate(1, Date.valueOf(datum));
            checkStmt.setInt(2, kontoID);
            ResultSet rs = checkStmt.executeQuery();

            boolean noteExists = rs.next() && rs.getInt(1) > 0;
            System.out.println("saveOrUpdate: noteExists = " + noteExists);

            boolean result;
            if (noteExists) {
                // Note exists, update it
                result = updateNote(datum, anteckning);
                System.out.println("saveOrUpdate: updateNote result = " + result);
            } else {
                // Note doesn't exist, insert it
                result = insertNote(datum, anteckning);
                System.out.println("saveOrUpdate: insertNote result = " + result);
            }
            return result;

        } catch (SQLException e) {
            System.err.println("Kunde inte kontrollera om anteckning finns: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Ta bort anteckning
    public boolean deleteNote(LocalDate datum) {
        // Kontrollera om användaren är inloggad
        int kontoID = SessionManager.getAktivtKontoID();
        System.out.println("deleteNote: kontoID = " + kontoID);

        if (kontoID == -1) {
            System.err.println("Kunde inte ta bort anteckning: Ingen användare är inloggad");
            return false;
        }

        // First check if a note exists for this date and user
        String checkSql = "SELECT COUNT(*) FROM kalender WHERE datum = ? AND kontoid = ?";
        System.out.println("deleteNote: checkSql = " + checkSql);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if entry exists
            boolean noteExists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setDate(1, Date.valueOf(datum));
                checkStmt.setInt(2, kontoID);
                ResultSet rs = checkStmt.executeQuery();

                noteExists = rs.next() && rs.getInt(1) > 0;
                System.out.println("deleteNote: noteExists = " + noteExists);

                if (!noteExists) {
                    // Note doesn't exist, nothing to delete
                    System.out.println("deleteNote: Note doesn't exist, nothing to delete");
                    return true; // Return true since the end state is as requested (no note)
                }
            }

            // If we get here, the note exists, so delete it
            String sql = "DELETE FROM kalender WHERE datum = ? AND kontoid = ?";
            System.out.println("deleteNote: SQL = " + sql);
            System.out.println("deleteNote: datum = " + datum + ", kontoID = " + kontoID);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(datum));
                stmt.setInt(2, kontoID);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("deleteNote: rowsAffected = " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Kunde inte ta bort anteckning: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
