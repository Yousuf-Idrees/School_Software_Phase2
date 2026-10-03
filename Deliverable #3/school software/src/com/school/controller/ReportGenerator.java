package com.school.controller;

// 1. Component Interface
interface Report {
    String getHeader();
    String getContent();
}

// 2. Concrete Component
class StudentTranscript implements Report {
    @Override
    public String getHeader() {
        return "OFFICIAL STUDENT TRANSCRIPT\nSchool: Global Academy\n";
    }

    @Override
    public String getContent() {
        return "Student: Yassin Sameh\nGPA: 3.8\nStatus: Passed";
    }
}

// 3. Base Decorator
abstract class ReportDecorator implements Report {
    protected Report tempReport;

    public ReportDecorator(Report report) {
        this.tempReport = report;
    }

    @Override
    public String getHeader() {
        return tempReport.getHeader();
    }

    @Override
    public String getContent() {
        return tempReport.getContent();
    }
}

// 4. Concrete Decorator A (Watermark)
class WatermarkDecorator extends ReportDecorator {
    public WatermarkDecorator(Report report) {
        super(report);
    }

    @Override
    public String getContent() {
        return super.getContent() + "\n[WATERMARK: CONFIDENTIAL DOCUMENT]";
    }
}

// 5. Concrete Decorator B (Digital Signature)
class DigitalSignatureDecorator extends ReportDecorator {
    public DigitalSignatureDecorator(Report report) {
        super(report);
    }

    @Override
    public String getHeader() {
        return super.getHeader() + "[SIGNED BY PRINCIPAL: Verified ✔]\n";
    }
}

// 6. Client Code - Demo
public class ReportGenerator {
    public static void main(String[] args) {
        System.out.println("=== BASIC TRANSCRIPT ===");
        Report basicTranscript = new StudentTranscript();
        System.out.println(basicTranscript.getHeader());
        System.out.println(basicTranscript.getContent());

        System.out.println("\n=== OFFICIAL TRANSCRIPT (Watermark + Digital Signature) ===");
        Report officialTranscript = new DigitalSignatureDecorator(
                                        new WatermarkDecorator(
                                            new StudentTranscript()));
        System.out.println(officialTranscript.getHeader());
        System.out.println(officialTranscript.getContent());
    }
}