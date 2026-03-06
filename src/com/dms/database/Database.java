package com.dms.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.dms.model.User;
import com.dms.model.Vehicle;
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

    // Database authentication with PreparedStatement
    public static User authenticate(String username, String password) {
        String hashedPassword = Utils.hashPassword(password);
        String sql = "SELECT id, username, password, role, active FROM users WHERE username = ? AND active = true";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(hashedPassword)) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        dbPassword,
                        rs.getString("role"),
                        rs.getBoolean("active")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            // Fallback to hardcoded users if database is not available
            if (username.equals("admin") && password.equals("admin123")) {
                return new User(1, "admin", hashedPassword, "ADMIN", true);
            } else if (username.equals("sales1") && password.equals("admin123")) {
                return new User(2, "sales1", hashedPassword, "SALES", true);
            } else if (username.equals("service1") && password.equals("admin123")) {
                return new User(3, "service1", hashedPassword, "SERVICE", true);
            }
        }
        return null;
    }

    // Vehicle CRUD operations
    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                vehicles.add(new Vehicle(
                    rs.getInt("id"),
                    rs.getString("vin"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("price"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    public static boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vin, make, model, year, price, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicle.getVin());
            stmt.setString(2, vehicle.getMake());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getYear());
            stmt.setDouble(5, vehicle.getPrice());
            stmt.setString(6, vehicle.getStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET vin=?, make=?, model=?, year=?, price=?, status=? WHERE id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehicle.getVin());
            stmt.setString(2, vehicle.getMake());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getYear());
            stmt.setDouble(5, vehicle.getPrice());
            stmt.setString(6, vehicle.getStatus());
            stmt.setInt(7, vehicle.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            return false;
        }
    }

    public static List<Vehicle> searchVehicles(String keyword, String status) {
        List<Vehicle> vehicles = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicles WHERE 1=1");
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(vin) LIKE ? OR LOWER(make) LIKE ? OR LOWER(model) LIKE ?)");
        }
        if (status != null && !status.equals("All")) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY id");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            if (status != null && !status.equals("All")) {
                stmt.setString(paramIndex, status);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicles.add(new Vehicle(
                    rs.getInt("id"),
                    rs.getString("vin"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("price"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching vehicles: " + e.getMessage());
        }
        return vehicles;
    }
}
