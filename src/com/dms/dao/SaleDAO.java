package com.dms.dao;

import com.dms.model.Sale;
import com.dms.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
    public void createSale(Sale sale) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            String saleSql = "INSERT INTO sales (vehicle_id, customer_id, employee_id, sale_date, sale_price, payment_method) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(saleSql)) {
                stmt.setInt(1, sale.getVehicleId());
                stmt.setInt(2, sale.getCustomerId());
                stmt.setInt(3, sale.getEmployeeId());
                stmt.setDate(4, sale.getSaleDate());
                stmt.setDouble(5, sale.getSalePrice());
                stmt.setString(6, sale.getPaymentMethod());
                stmt.executeUpdate();
            }

            String updateVehicle = "UPDATE vehicles SET status='Sold' WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(updateVehicle)) {
                stmt.setInt(1, sale.getVehicleId());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getInt("id"));
                s.setVehicleId(rs.getInt("vehicle_id"));
                s.setCustomerId(rs.getInt("customer_id"));
                s.setEmployeeId(rs.getInt("employee_id"));
                s.setSaleDate(rs.getDate("sale_date"));
                s.setSalePrice(rs.getDouble("sale_price"));
                s.setPaymentMethod(rs.getString("payment_method"));
                sales.add(s);
            }
        }
        return sales;
    }

    public Sale getSaleById(int id) throws SQLException {
        String sql = "SELECT * FROM sales WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getInt("id"));
                s.setVehicleId(rs.getInt("vehicle_id"));
                s.setCustomerId(rs.getInt("customer_id"));
                s.setEmployeeId(rs.getInt("employee_id"));
                s.setSaleDate(rs.getDate("sale_date"));
                s.setSalePrice(rs.getDouble("sale_price"));
                s.setPaymentMethod(rs.getString("payment_method"));
                return s;
            }
        }
        return null;
    }
    
    public List<Sale> getSalesByCustomer(int customerId) throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE customer_id=? ORDER BY sale_date DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getInt("id"));
                s.setVehicleId(rs.getInt("vehicle_id"));
                s.setCustomerId(rs.getInt("customer_id"));
                s.setEmployeeId(rs.getInt("employee_id"));
                s.setSaleDate(rs.getDate("sale_date"));
                s.setSalePrice(rs.getDouble("sale_price"));
                s.setPaymentMethod(rs.getString("payment_method"));
                sales.add(s);
            }
        }
        return sales;
    }
}
