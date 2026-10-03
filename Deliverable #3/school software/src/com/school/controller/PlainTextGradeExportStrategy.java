package com.school.controller;

import java.util.List;

// Concrete Strategy 3: Plain Text Export
public class PlainTextGradeExportStrategy implements GradeExportStrategy {
    @Override
    public void exportGrades(String studentId, List<String> gradeLines) {
        System.out.println("=== Plain Text Grades for Student: " + studentId + " ===");
        for (String line : gradeLines) {
            System.out.println("  " + line);
        }
        System.out.println("Plain text export complete.");
    }
}
