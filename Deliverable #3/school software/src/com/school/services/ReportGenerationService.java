package com.school.services;

import com.school.data.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Report Generation Service - Demonstrates DocumentManager integration
 * This service shows how reports would be generated and stored
 */
public class ReportGenerationService {

    /**
     * Generate a report and save it to database and optionally to file
     */
    public String generateAndSaveReport(String reportType, String title, String description,
                                      String generatedBy, String format, boolean saveToFile) {
        try {
            // Generate report content (simulating DocumentManager + Decorator patterns)
            StringBuilder content = new StringBuilder();
            content.append("=== ").append(format.toUpperCase()).append(" DOCUMENT ===\n");
            content.append("Title: ").append(title).append("\n");
            content.append("Type: ").append(reportType).append("\n\n");

            // Simulate report content with patterns
            content.append("REPORT HEADER:\n");
            content.append("OFFICIAL STUDENT TRANSCRIPT\n");
            content.append("School: Global Academy\n");
            content.append("[SIGNED BY PRINCIPAL: Verified ✔]\n\n");

            content.append("REPORT CONTENT:\n");
            content.append("Student: Yassin Sameh\n");
            content.append("GPA: 3.8\n");
            content.append("Status: Passed\n");
            content.append("[WATERMARK: CONFIDENTIAL DOCUMENT]\n\n");

            content.append("DOCUMENT STRUCTURE:\n");
            content.append("Header: School Report\n");
            content.append("Body: Student grades and attendance\n");
            content.append("Footer: Generated on ").append(LocalDate.now()).append("\n");

            String finalContent = content.toString();
            String filePath = null;

            // Save to file if requested
            if (saveToFile) {
                filePath = saveReportToFile(title, format, finalContent);
            }

            // Save to database
            saveReportToDatabase(reportType, title, description, generatedBy, format, filePath, finalContent);

            return "Report '" + title + "' generated successfully in " + format + " format!";

        } catch (Exception e) {
            return "Error generating report: " + e.getMessage();
        }
    }

    private String saveReportToFile(String title, String format, String content) throws IOException {
        java.io.File reportsDir = new java.io.File("school_reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_" +
                          LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        String fileName = title.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + "." + format.toLowerCase();
        String filePath = "school_reports/" + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }

        return filePath;
    }

    private void saveReportToDatabase(String reportType, String title, String description,
                                    String generatedBy, String format, String filePath, String content) throws SQLException {
        String sql = "INSERT INTO reports (report_type, title, description, generated_by, " +
                    "generated_date, generated_time, file_format, file_path, content) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, reportType);
            pstmt.setString(2, title);
            pstmt.setString(3, description != null ? description : "");
            pstmt.setString(4, generatedBy);
            pstmt.setString(5, LocalDate.now().toString());
            pstmt.setString(6, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            pstmt.setString(7, format);
            pstmt.setString(8, filePath);
            pstmt.setString(9, content);

            pstmt.executeUpdate();
        }
    }

    public ResultSet getUserReports(String username) throws SQLException {
        String sql = "SELECT * FROM reports WHERE generated_by = ? ORDER BY generated_date DESC, generated_time DESC";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }
}