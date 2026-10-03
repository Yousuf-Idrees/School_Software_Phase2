package com.school.model;

// Concrete Template Method Implementation - QR Attendance
public class QRAttendance extends AttendanceProcess {

    @Override
    void loadStudents() {
        System.out.println("Loading registered students");
    }

    @Override
    void mark() {
        System.out.println("Students scan QR code");
    }

    @Override
    void validate() {
        System.out.println("Validate scan timestamps");
    }

    @Override
    void save() {
        System.out.println("Store QR attendance logs");
    }
}
