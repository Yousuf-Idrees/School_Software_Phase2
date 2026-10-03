package com.school.controller;

import java.util.List;

// Concrete Strategy 1: CSV Export
public class CSVGradeExportStrategy implements GradeExportStrategy {
    @Override
    public void exportGrades(String studentId, List<String> gradeLines) {
        System.out.println("=== CSV Grade Export for Student: " + studentId + " ===");
        System.out.println("StudentID,Subject,Grade");
        for (String line : gradeLines) {
            String[] parts = line.split(": ", 2);
            String subject = parts.length > 0 ? parts[0].trim() : line;
            String grade   = parts.length > 1 ? parts[1].trim() : "N/A";
            System.out.println(studentId + "," + subject + "," + grade);
        }
        System.out.println("CSV export complete.");
    }
}
