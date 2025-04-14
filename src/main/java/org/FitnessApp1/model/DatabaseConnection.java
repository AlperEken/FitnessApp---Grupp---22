package org.FitnessApp1.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://pgserver.mau.se/aq0647";
    private static final String USER = "aq0647";  //
    private static final String PASSWORD = "1pbrlr94";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}