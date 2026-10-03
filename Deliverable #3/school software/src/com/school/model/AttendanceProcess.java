package com.school.model;

// Template Method Pattern - Attendance Process
public abstract class AttendanceProcess {
    public final void markAttendance() {
        loadStudents();
        mark();
        validate();
        save();
    }

    abstract void loadStudents();
    abstract void mark();
    abstract void validate();
    abstract void save();
}
