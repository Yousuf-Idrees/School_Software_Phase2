package com.school.model;

// Concrete Template Method Implementation - Manual Attendance
public class ManualAttendance extends AttendanceProcess {

    @Override
    void loadStudents() {
        System.out.println("Loading students list for class");
    }

    @Override
    void mark() {
        System.out.println("Teacher manually marks attendance");
    }

    @Override
    void validate() {
        System.out.println("Check missing entries");
    }

    @Override
    void save() {
        System.out.println("Save attendance to database");
    }
}
