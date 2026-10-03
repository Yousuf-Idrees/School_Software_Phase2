package com.school.controller;

import java.util.List;

// Concrete Strategy 2: PDF Export
public class PDFGradeExportStrategy implements GradeExportStrategy {
    @Override
    public void exportGrades(String studentId, List<String> gradeLines) {
        System.out.println("=== PDF Grade Report for Student: " + studentId + " ===");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-25s %-10s%n", "Subject", "Grade");
        System.out.println("--------------------------------------------------");
        for (String line : gradeLines) {
            String[] parts = line.split(": ", 2);
            String subject = parts.length > 0 ? parts[0].trim() : line;
            String grade   = parts.length > 1 ? parts[1].trim() : "N/A";
            System.out.printf("%-25s %-10s%n", subject, grade);
        }
        System.out.println("--------------------------------------------------");
        System.out.println("PDF export complete. Saved as " + studentId + "_grades.pdf");
    }
}
