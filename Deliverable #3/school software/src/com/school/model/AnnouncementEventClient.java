package com.school.model;

// Client
public class AnnouncementEventClient {
    public static void main(String[] args) {

        // Get the single EventManager instance (Singleton)
        EventManager eventManager = EventManager.getInstance();

        // Prove it is a Singleton
        EventManager anotherRef = EventManager.getInstance();
        System.out.println("Same instance: " + (eventManager == anotherRef)); // true

        System.out.println();

        // Create observers (UI panels)
        AnnouncementPanel     announcementPanel  = new AnnouncementPanel();
        ExamNotificationPanel examPanel          = new ExamNotificationPanel();
        SMSAlertPanel         smsPanel           = new SMSAlertPanel();
        AttendanceAlertListener attendanceListener = new AttendanceAlertListener();

        // Subscribe each observer to the events it cares about
        eventManager.subscribe("ANNOUNCEMENT",      announcementPanel);
        eventManager.subscribe("ANNOUNCEMENT",      smsPanel);
        eventManager.subscribe("EXAM_NOTIFICATION", examPanel);
        eventManager.subscribe("EXAM_NOTIFICATION", smsPanel);
        eventManager.subscribe("GRADE_PUBLISHED",   announcementPanel);
        eventManager.subscribe("ATTENDANCE_MARKED", attendanceListener);

        System.out.println();

        // Teacher posts a new announcement → AnnouncementPanel + SMSAlertPanel react
        System.out.println("=== Teacher posts announcement ===");
        eventManager.notify("ANNOUNCEMENT",
                "School trip to the Science Museum on April 25th.");

        System.out.println();

        // Teacher schedules exam → ExamNotificationPanel + SMSAlertPanel react
        System.out.println("=== Teacher schedules exam notification ===");
        eventManager.notify("EXAM_NOTIFICATION",
                "Software Engineering midterm — Thursday April 24th, 10:00 AM, Room 204.");

        System.out.println();

        // System publishes grades → AnnouncementPanel reacts
        System.out.println("=== System publishes grades ===");
        eventManager.notify("GRADE_PUBLISHED",
                "Grades for Databases (CS301) are now available.");

        System.out.println();

        // Teacher records attendance → AttendanceAlertListener reacts
        System.out.println("=== Teacher records attendance ===");
        eventManager.notify("ATTENDANCE_MARKED", "Student STU2024002 — ABSENT — 2026-04-19");
        eventManager.notify("ATTENDANCE_MARKED", "Student STU2024003 — PRESENT — 2026-04-19");

        System.out.println();

        // Unsubscribe SMS panel from announcements
        eventManager.unsubscribe("ANNOUNCEMENT", smsPanel);

        System.out.println();

        // Fire another announcement — SMS panel should NOT react this time
        System.out.println("=== Another announcement (SMS unsubscribed) ===");
        eventManager.notify("ANNOUNCEMENT", "Library closed for maintenance on Monday.");
    }
}
