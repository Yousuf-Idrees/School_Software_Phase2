package com.school.controller;

import com.school.data.AttendanceDAO;

import java.util.List;

public class AttendanceController {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    // Ensure this method name is exactly what you call in the View
    public List<String> handleViewAttendance(String studentID) {
        if (studentID == null || studentID.trim().isEmpty()) {
            return null;
        }
        return attendanceDAO.getAttendanceFromDB(studentID);
    }

    public String processAttendance(String date, String id, String status) {
        return null ;
    }
}