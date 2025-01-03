package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(Constants.DB_DRIVER);
                connection = DriverManager.getConnection(
                    Constants.DB_URL, 
                    Constants.DB_USERNAME, 
                    Constants.DB_PASSWORD
                );
                if (connection != null) {
                    System.out.println("Database connection successful!");
                } else {
                    throw new SQLException(Constants.ERROR_DB_CONNECTION);
                }
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        }
    }
}
