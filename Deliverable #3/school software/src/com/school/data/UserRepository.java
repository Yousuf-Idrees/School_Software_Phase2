package com.school.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public String verifyLogin(String username, String password) {
        String query = "SELECT role FROM users WHERE (LOWER(username) = LOWER(?) OR LOWER(name) = LOWER(?)) AND LOWER(password) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);
            
            // Debug: Show all users in database
            try (Statement debugStmt = conn.createStatement();
                 ResultSet debugRs = debugStmt.executeQuery("SELECT username, name, password FROM users")) {
                System.out.println("[DB DEBUG] All users in database:");
                while (debugRs.next()) {
                    System.out.println("[DB DEBUG]   - '" + debugRs.getString("username") + "' / '" + debugRs.getString("name") + "' / '" + debugRs.getString("password") + "'");
                }
            }
            
            System.out.println("[DB DEBUG] Attempting to match: '" + username + "' / '" + password + "'");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("[DB DEBUG] Match found, role: " + role);
                return role;
            }
            System.out.println("[DB DEBUG] No match found");
            return null;
        } catch (SQLException e) {
            System.out.println("Error verifying login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getStudentDetails(String id) {
        String query = "SELECT name FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                return "Name: " + name + "\nMajor: Computer Science\nLevel: 3\nGPA: 3.8";
            }
        } catch (SQLException e) {
            System.out.println("Error getting student details: " + e.getMessage());
        }
        return "Details not found.";
    }

    public List<String> getGrades(String id) {
        List<String> gradesList = new ArrayList<>();
        String query = "SELECT subject, grade FROM grades WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder gradesStr = new StringBuilder();
            boolean hasGrades = false;
            while (rs.next()) {
                hasGrades = true;
                if (gradesStr.length() > 0) {
                    gradesStr.append("\n");
                }
                gradesStr.append(rs.getString("subject")).append(": ").append(rs.getString("grade"));
            }
            
            if (hasGrades) {
                gradesList.add(gradesStr.toString());
            } else {
                gradesList.add("No grades found for ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error getting grades: " + e.getMessage());
            gradesList.add("No grades found for ID: " + id);
        }
        return gradesList;
    }

    public List<String> getAttendance(String id) {
        List<String> attendanceList = new ArrayList<>();
        String query = "SELECT attendance_date, status FROM attendance WHERE student_id = ? ORDER BY attendance_date";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder attendanceStr = new StringBuilder();
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                if (attendanceStr.length() > 0) {
                    attendanceStr.append(", ");
                }
                attendanceStr.append(rs.getString("attendance_date")).append(": ").append(rs.getString("status"));
            }
            
            if (hasRecords) {
                attendanceList.add(attendanceStr.toString());
            } else {
                attendanceList.add("No attendance records found for ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error getting attendance: " + e.getMessage());
            attendanceList.add("No attendance records found for ID: " + id);
        }
        return attendanceList;
    }

    public void saveAttendance(String id, String date, String status) {
        String query = "INSERT INTO attendance (student_id, attendance_date, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, date);
            stmt.setString(3, status);
            stmt.executeUpdate();
            System.out.println("Saved to DB: ID " + id + " marked " + status + " on " + date);
        } catch (SQLException e) {
            System.out.println("Error saving attendance: " + e.getMessage());
        }
    }

    /**
     * Insert user into database
     */
    public boolean insertUserToDatabase(String id, String username, String password, String role, String name) {
        String insertQuery = "INSERT INTO users (id, username, password, role, name) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
        return false;
    }
}