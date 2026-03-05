package com.dms.dao;

import com.dms.model.Sale;
import com.dms.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
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
                sales.add(s);
            }
        }
        return sales;
    }
}
