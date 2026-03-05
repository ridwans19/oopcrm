package com.dms.database;

import java.sql.*;
import com.dms.model.User;
import com.dms.util.Utils;
import com.dms.util.ConfigLoader;

public class Database {
    private static final String URL = ConfigLoader.get("db.url");
    private static final String USER = ConfigLoader.get("db.user");
    private static final String PASSWORD = ConfigLoader.get("db.password");

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw new SQLException("Unable to connect to database. Please check if PostgreSQL is running.", e);
        }
    }

    // Temporary hardcoded authentication for testing
    public static User authenticate(String username, String password) {
        String hashedPassword = Utils.hashPassword(password);
        
        // Hardcoded users for now (replace with DB query later)
        if (username.equals("admin") && password.equals("admin123")) {
            return new User(1, "admin", hashedPassword, "ADMIN", true);
        } else if (username.equals("sales1") && password.equals("admin123")) {
            return new User(2, "sales1", hashedPassword, "SALES", true);
        } else if (username.equals("service1") && password.equals("admin123")) {
            return new User(3, "service1", hashedPassword, "SERVICE", true);
        }
        return null;
    }
}
