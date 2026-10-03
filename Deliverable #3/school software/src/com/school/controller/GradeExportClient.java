package com.school.controller;

import java.util.Arrays;
import java.util.List;

// Client
public class GradeExportClient {
    public static void main(String[] args) {
        List<String> grades = Arrays.asList(
            "Mathematics: A",
            "Physics: B+",
            "Software Engineering: A+",
            "Data Structures: A",
            "Databases: B"
        );
        String studentId = "STU2024001";

        // Teacher exports as CSV
        GradeExporterContext exporter = new GradeExporterContext(new CSVGradeExportStrategy());
        exporter.export(studentId, grades);

        System.out.println();

        // Teacher switches to PDF at runtime
        exporter.setStrategy(new PDFGradeExportStrategy());
        exporter.export(studentId, grades);

        System.out.println();

        // Teacher switches to Plain Text
        exporter.setStrategy(new PlainTextGradeExportStrategy());
        exporter.export(studentId, grades);
    }
}
