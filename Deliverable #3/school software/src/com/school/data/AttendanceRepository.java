package com.school.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    public List<String> getAttendanceFromDB(String studentID) {
        List<String> records = new ArrayList<>();
        String query = "SELECT attendance_date, status FROM attendance WHERE student_id = ? ORDER BY attendance_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(rs.getString("attendance_date") + ": " + rs.getString("status"));
            }
            
            if (records.isEmpty()) {
                records.add("No attendance records for this ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving attendance: " + e.getMessage());
            records.add("No attendance records for this ID.");
        }
        
        return records;
    }
}