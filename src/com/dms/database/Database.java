package com.dms.database;

import java.sql.*;
import com.dms.model.User;
import com.dms.util.Utils;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/dealership_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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
