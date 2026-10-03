package com.school.grades;

public class GradeExporterAdapter implements GradeExporter {

    private LegacyReportPrinter adaptee;

    public GradeExporterAdapter(LegacyReportPrinter adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void exportGrades(String studentId, String reportData) {
        String formattedData = "Student ID: " + studentId + " | " + reportData;
        adaptee.printReport(formattedData);
    }
}
