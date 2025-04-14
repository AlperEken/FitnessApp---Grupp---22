package org.FitnessApp1.model;

import org.mindrot.jbcrypt.BCrypt; // Import för BCrypt

import java.sql.*;

public class KontoDAO {

    // Hämta konto baserat på kontoID
    public Konto getAccountByID(int kontoID) {
        String sql = "SELECT * FROM konto WHERE kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kontoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String namn = rs.getString("namn");
                String efternamn = rs.getString("efternamn");
                String epost = rs.getString("epost");
                String lösenord = rs.getString("lösenord");
                int ålder = rs.getInt("ålder");
                double vikt = rs.getDouble("vikt");
                String kön = rs.getString("kön");
                int dagligtMal = rs.getInt("dagligtMål");

                // Skapa och returnera Konto-objekt
                return new Konto(namn, efternamn, epost, lösenord, ålder, vikt, kön, dagligtMal);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av konto: " + e.getMessage());
        }

        return null; // Returnera null om inget konto hittas
    }

    // Hämta namn baserat på e-post
    public String getNameByEmail(String email) {
        String sql = "SELECT namn FROM konto WHERE epost = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("namn");
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av namn: " + e.getMessage());
        }

        return null; // Returnera null om inget namn hittas
    }

    // Hämta kontoID baserat på e-post
    public int getAcoountIDByEmail(String email) {
        String sql = "SELECT kontoID FROM konto WHERE epost = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("kontoID");
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av kontoID: " + e.getMessage());
        }

        return -1; // Returnera -1 om inget konto hittas
    }

    // Validera inloggning genom att jämföra e-post och lösenord
    public boolean valideraInloggning(String epost, String losenord) {
        String sql = "SELECT lösenord FROM konto WHERE epost = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, epost);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String sparatHash = rs.getString("lösenord");
                return BCrypt.checkpw(losenord, sparatHash); // Använd BCrypt för att verifiera lösenordet
            }

        } catch (SQLException e) {
            System.err.println(" Fel vid inloggning: " + e.getMessage());
        }

        return false; // Om inget konto hittas eller lösenordet inte stämmer
    }

    // Registrera ett nytt konto
    public boolean registeraccount(Konto konto) {
        String sql = "INSERT INTO konto (namn, efternamn, epost, lösenord, ålder, vikt, kön, dagligtMål) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hasha lösenordet innan det sparas
            String hashedPassword = BCrypt.hashpw(konto.getLösenord(), BCrypt.gensalt());

            // Sätt parametrar
            stmt.setString(1, konto.getNamn());
            stmt.setString(2, konto.getEfternamn());
            stmt.setString(3, konto.getEpost());
            stmt.setString(4, hashedPassword);
            stmt.setInt(5, konto.getÅlder());
            stmt.setDouble(6, konto.getVikt());
            stmt.setString(7, konto.getKön());
            stmt.setInt(8, konto.getDagligtMal());

            // Utför insättningen
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Fel vid registrering: " + e.getMessage());
        }

        return false;
    }

    // Uppdatera konto (exempel: användare ändrar sina uppgifter)
    public boolean uppdateraKonto(Konto konto) {
        String sql = "UPDATE konto SET namn = ?, efternamn = ?, epost = ?, lösenord = ?, ålder = ?, vikt = ?, kön = ?, dagligtMål = ? WHERE kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Hasha lösenordet innan det sparas
            String hashedPassword = BCrypt.hashpw(konto.getLösenord(), BCrypt.gensalt());

            // Sätt parametrar
            stmt.setString(1, konto.getNamn());
            stmt.setString(2, konto.getEfternamn());
            stmt.setString(3, konto.getEpost());
            stmt.setString(4, hashedPassword);
            stmt.setInt(5, konto.getÅlder());
            stmt.setDouble(6, konto.getVikt());
            stmt.setString(7, konto.getKön());
            stmt.setInt(8, konto.getDagligtMal());
            stmt.setInt(9, konto.getKontoID());

            // Utför uppdateringen
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Fel vid uppdatering av konto: " + e.getMessage());
        }

        return false; // Om något gick fel
    }
}
