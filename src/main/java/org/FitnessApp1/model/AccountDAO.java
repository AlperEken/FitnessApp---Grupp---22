package org.FitnessApp1.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt; // Import för BCrypt

import java.sql.*;

public class AccountDAO {
    private static int id = 0;

    // Hämta konto baserat på kontoID
    public Account getAccountByID(int kontoID) {
        String sql = "SELECT * FROM konto WHERE kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kontoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Hämta ALLA fält inklusive kontoID
                int id = rs.getInt("kontoID");
                String namn = rs.getString("namn");
                String efternamn = rs.getString("efternamn");
                String epost = rs.getString("epost");
                String lösenord = rs.getString("lösenord");
                int ålder = rs.getInt("ålder");
                double vikt = rs.getDouble("vikt");
                String kön = rs.getString("kön");
                int dagligtMal = rs.getInt("dagligtMål");

                return new Account(id, namn, efternamn, epost, lösenord, ålder, vikt, kön, dagligtMal);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av konto: " + e.getMessage());
        }

        return null;
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
    public boolean validateLogIn(String epost, String losenord) {
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



    public boolean registerAccount(Account account) {
        String sql = "INSERT INTO konto (namn, efternamn, epost, lösenord, ålder, vikt, kön, dagligtMål) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING kontoID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());

            stmt.setString(1, account.getName());
            stmt.setString(2, account.getLastname());
            stmt.setString(3, account.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.setInt(5, account.getAge());
            stmt.setDouble(6, account.getWeight());
            stmt.setString(7, account.getGender());
            stmt.setInt(8, account.getDaliyGoals());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int kontoID = rs.getInt("kontoID");

                // Skapa kalender och koppla till kontoID
                int kalenderID = createNewCalender(kontoID);
                if (kalenderID == -1) {
                    System.err.println("Misslyckades skapa kalender");
                    return false;
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid registrering: " + e.getMessage());
        }

        return false;
    }



    // Uppdatera konto (exempel: användare ändrar sina uppgifter)
    public boolean updateAccount(Account account) {
        String sql = "UPDATE konto SET namn = ?, efternamn = ?, epost = ?, lösenord = ?, ålder = ?, vikt = ?, kön = ?, dagligtMål = ? WHERE kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String lösenord = account.getPassword();
            // Hasha endast om lösenordet INTE är hashat redan
            if (!lösenord.startsWith("$2a$") && !lösenord.startsWith("$2b$")) {
                lösenord = BCrypt.hashpw(lösenord, BCrypt.gensalt());
            }

            stmt.setString(1, account.getName());
            stmt.setString(2, account.getLastname());
            stmt.setString(3, account.getEmail());
            stmt.setString(4, lösenord);
            stmt.setInt(5, account.getAge());
            stmt.setDouble(6, account.getWeight());
            stmt.setString(7, account.getGender());
            stmt.setInt(8, account.getDaliyGoals());
            stmt.setInt(9, account.getAccountID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Fel vid uppdatering av konto: " + e.getMessage());
        }

        return false;
    }

    public boolean eraseAccount(int kontoID) {
        String sql = "DELETE FROM konto WHERE kontoID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kontoID);
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Fel vid radering av konto: " + e.getMessage());
            return false;
        }
    }


    public int createNewCalender(int kontoID) {
        String sql = "INSERT INTO kalender (kontoID) VALUES (?) RETURNING kalenderid";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kontoID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("kalenderid");
            }
        } catch (SQLException e) {
            System.err.println("Fel vid skapande av kalender: " + e.getMessage());
        }
        return -1;  // Fel indikerat
    }
    public ObservableList<Account> getAllAccounts() {
        ObservableList<Account> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM konto";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("kontoID");
                String namn = rs.getString("namn");
                String efternamn = rs.getString("efternamn");
                String epost = rs.getString("epost");
                String lösenord = rs.getString("lösenord");
                int ålder = rs.getInt("ålder");
                double vikt = rs.getDouble("vikt");
                String kön = rs.getString("kön");
                int dagligtMal = rs.getInt("dagligtMål");

                lista.add(new Account(id, namn, efternamn, epost, lösenord, ålder, vikt, kön, dagligtMal));
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av alla konton: " + e.getMessage());
        }

        return lista;
    }



}