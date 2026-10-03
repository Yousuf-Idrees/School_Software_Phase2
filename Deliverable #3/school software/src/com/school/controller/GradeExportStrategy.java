package com.school.controller;

import java.util.List;

// Strategy Interface
public interface GradeExportStrategy {
    void exportGrades(String studentId, List<String> gradeLines);
}
