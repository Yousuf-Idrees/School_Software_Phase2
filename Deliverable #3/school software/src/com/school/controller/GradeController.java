package com.school.controller;

import com.school.data.UserRepository;
import java.util.List;

public class GradeController {
    private UserRepository userRepository = new UserRepository();

    public List<String> viewStudentGrades(String studentID) {
        // Validation logic can go here (e.g., checking if ID is empty)
        if (studentID == null || studentID.isEmpty()) {
            return null;
        }
        return userRepository.getGrades(studentID);
    }

    public void viewGrades(String studentId) { 
        System.out.println("Displaying grades for student: " + studentId);
    }
}