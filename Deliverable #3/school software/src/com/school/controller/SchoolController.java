package com.school.controller;

import com.school.data.UserRepository;
import com.school.controller.GradeController;
import java.util.List;

public class SchoolController {
    private UserRepository dataLayer = new UserRepository();
    private GradeController gradeController = new GradeController();

    // Use Case 1: View Grades
    public String processGradeRequest(String id) {
        if (id.isEmpty()) return "Please enter an ID.";
        gradeController.viewGrades(id);
        List<String> grades = dataLayer.getGrades(id);
        return String.join("\n", grades);
    }
    public String getStudentInfo(String id) {
        return dataLayer.getStudentDetails(id);
    }
    // Use Case 2: View Attendance (Student)
    public String processAttendanceViewRequest(String id) {
        if (id.isEmpty()) return "Please enter an ID.";
        List<String> attendance = dataLayer.getAttendance(id);
        return String.join("\n", attendance);
    }
    public String login(String username, String password) {
        System.out.println("[CONTROLLER DEBUG] login called with: " + username);
        String role = dataLayer.verifyLogin(username, password);
        if (role != null) {
            System.out.println("[CONTROLLER DEBUG] Returning " + role);
            return role;
        }
        System.out.println("[CONTROLLER DEBUG] Returning INVALID");
        return "INVALID";
    }
    // Use Case 3: Record Attendance (Teacher) - THIS WAS MISSING
    public String processAttendanceRecord(String id, String status) {
        if (id.isEmpty()) return "Please enter a Student ID first.";

        // Get current date
        String date = java.time.LocalDate.now().toString();

        // Call DAO to save to database
        dataLayer.saveAttendance(id, date, status);

        return "Success: Recorded " + status + " for " + id + " on " + date;
    }

    /**
     * Create a new student account
     */
    public boolean createStudentAccount(String studentId, String username, String password, String fullName) {
        return dataLayer.insertUserToDatabase(studentId, username, password, "STUDENT", fullName);
    }

    /**
     * Create a new teacher account
     */
    public boolean createTeacherAccount(String teacherId, String username, String password, String fullName) {
        return dataLayer.insertUserToDatabase(teacherId, username, password, "TEACHER", fullName);
    }

    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        return dataLayer.usernameExists(username);
    }
}