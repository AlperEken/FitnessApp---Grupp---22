package org.FitnessApp1.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.FitnessApp1.model.Activity;


public class ActivityDAO {

    public boolean saveActivity(String note, LocalDate date, LocalTime startTime, LocalTime endTime, int kalenderID) {
        String sql = "INSERT INTO aktivitet (anteckningar, startat, endat, kalenderid, datum) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, note);
            stmt.setTime(2, Time.valueOf(startTime));
            stmt.setTime(3, Time.valueOf(endTime));
            stmt.setInt(4, kalenderID);
            stmt.setDate(5, Date.valueOf(date));

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to save activity: " + e.getMessage());
            return false;
        }
    }

    public List<Activity> getActivitiesForDay(int kalenderID, LocalDate date) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM aktivitet WHERE kalenderid = ? AND datum = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kalenderID);
            stmt.setDate(2, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String note = rs.getString("anteckningar");
                LocalTime start = rs.getTime("startat").toLocalTime();
                LocalTime end = rs.getTime("endat").toLocalTime();

                list.add(new Activity(note, date, start, end));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching activities: " + e.getMessage());
        }

        return list;
    }
}
