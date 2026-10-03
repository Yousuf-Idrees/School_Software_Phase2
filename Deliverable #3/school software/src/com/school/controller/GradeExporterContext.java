package com.school.controller;

import java.util.List;

// Context - GradeExporter with Strategy Pattern
public class GradeExporterContext {
    private GradeExportStrategy strategy;

    public GradeExporterContext(GradeExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(GradeExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void export(String studentId, List<String> gradeLines) {
        strategy.exportGrades(studentId, gradeLines);
    }
}
