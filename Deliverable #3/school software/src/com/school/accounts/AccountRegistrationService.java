package com.school.accounts;

import java.sql.*;
import com.school.model.UserManager;
import com.school.model.StudentAccountManager;
import com.school.model.TeacherAccountManager;

/**
 * Account Registration Service
 * Handles creation of student and teacher accounts
 * Uses the UserManager to manage account types
 */
public class AccountRegistrationService {
    private static final String DB_URL = "jdbc:sqlite:school.db";
    
    /**
     * Create a new student account
     */
    public static boolean createStudentAccount(String studentId, String username, String password, String fullName) {
        UserManager factory = new StudentAccountManager();
        
        // Setup profile and permissions
        UserProfile profile = factory.createProfile();
        UserPermissions permissions = factory.createPermissions();
        profile.setupProfile();
        permissions.assignPermissions();
        
        // Persist to database
        return insertUserToDatabase(studentId, username, password, "STUDENT", fullName);
    }
    
    /**
     * Create a new teacher account
     */
    public static boolean createTeacherAccount(String teacherId, String username, String password, String fullName) {
        UserManager factory = new TeacherAccountManager();
        
        // Setup profile and permissions
        UserProfile profile = factory.createProfile();
        UserPermissions permissions = factory.createPermissions();
        profile.setupProfile();
        permissions.assignPermissions();
        
        // Persist to database
        return insertUserToDatabase(teacherId, username, password, "TEACHER", fullName);
    }
    
    /**
     * Insert user into database
     */
    private static boolean insertUserToDatabase(String id, String username, String password, String role, String name) {
        String insertQuery = "INSERT INTO users (id, username, password, role, name) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            
            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, name);
            
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("[SUCCESS] " + role + " account created: " + username);
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("[ERROR] Username already exists: " + username);
            } else {
                System.out.println("[ERROR] Failed to create account: " + e.getMessage());
            }
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if username already exists
     */
    public static boolean usernameExists(String username) {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to check username: " + e.getMessage());
        }
        return false;
    }
}
