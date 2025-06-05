package org.FitnessApp1.model;

import java.sql.*;

public class AdminDAO {

    public boolean validateAdmin(String epost, String lösenord) {
        String sql = """
        SELECT k.lösenord
        FROM admin a
        JOIN konto k ON a.epost = k.epost
        WHERE a.epost = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, epost);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String sparatHash = rs.getString("lösenord");
                return org.mindrot.jbcrypt.BCrypt.checkpw(lösenord, sparatHash);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid admininloggning: " + e.getMessage());
        }

        return false;
    }

    public boolean createAdmin(String epost) {
        String sql = "INSERT INTO admin (epost) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, epost);
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Fel vid skapande av admin: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAdmin(String epost) {
        String sql = "DELETE FROM admin WHERE epost = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, epost);
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Fel vid radering av admin: " + e.getMessage());
            return false;
        }
    }
}
