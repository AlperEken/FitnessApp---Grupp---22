package org.FitnessApp1.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KaloriLoggDAO {

    // Lägg till ny logg
    public boolean addLog(KaloriLogg logg) {
        String sql = "INSERT INTO kalorier (datum, calorier, kontoID, beskrivning) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(logg.getDatum()));
            stmt.setInt(2, logg.getKalorier());
            stmt.setInt(3, logg.getKontoID());
            stmt.setString(4, logg.getBeskrivning());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println(" Kunde inte lägga till logg: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Hämta alla loggar för en specifik dag
    public List<KaloriLogg> getLogsForDate(LocalDate datum, int kontoID) {
        List<KaloriLogg> loggar = new ArrayList<>();
        String sql = "SELECT loggID, datum, calorier, beskrivning FROM kalorier WHERE datum = ? AND kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            stmt.setInt(2, kontoID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int loggID = rs.getInt("loggID");
                int kalorier = rs.getInt("calorier");
                String beskrivning = rs.getString("beskrivning");

                KaloriLogg logg = new KaloriLogg(loggID, datum, beskrivning, kalorier, kontoID);
                loggar.add(logg);
            }

        } catch (SQLException e) {
            System.err.println(" Kunde inte hämta loggar: " + e.getMessage());
        }

        return loggar;
    }

    // Räkna totala kalorier för en dag
    public int countTotalCalories(LocalDate datum, int kontoID) {
        String sql = "SELECT SUM(calorier) FROM kalorier WHERE datum = ? AND kontoID = ?";
        int total = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(datum));
            stmt.setInt(2, kontoID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(" Kunde inte räkna kalorier: " + e.getMessage());
        }

        return total;
    }

    // Uppdatera befintlig logg
    public boolean updateLogs(KaloriLogg logg) {
        String sql = "UPDATE kalorier SET datum = ?, calorier = ?, beskrivning = ? WHERE loggID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(logg.getDatum()));
            stmt.setInt(2, logg.getKalorier());
            stmt.setString(3, logg.getBeskrivning());
            stmt.setInt(4, logg.getLoggID());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte uppdatera logg: " + e.getMessage());
            return false;
        }
    }

    // Ta bort logg
    public boolean deleteLog(int loggID) {
        String sql = "DELETE FROM kalorier WHERE loggID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loggID);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Kunde inte ta bort logg: " + e.getMessage());
            return false;
        }
    }
}
