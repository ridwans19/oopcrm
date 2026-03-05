package com.dms.dao;

import com.dms.model.Service;
import com.dms.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    
    public List<Service> getServicesByCustomer(int customerId) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE customer_id=? ORDER BY service_date DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Service s = new Service();
                s.setId(rs.getInt("id"));
                s.setVehicleId(rs.getInt("vehicle_id"));
                s.setCustomerId(rs.getInt("customer_id"));
                s.setDescription(rs.getString("description"));
                s.setCost(rs.getDouble("cost"));
                s.setServiceDate(rs.getDate("service_date"));
                services.add(s);
            }
        }
        return services;
    }
}
