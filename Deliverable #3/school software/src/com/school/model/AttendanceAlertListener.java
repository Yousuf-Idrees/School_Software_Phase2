package com.school.model;

// Concrete Observer 4: Attendance Alert Listener
public class AttendanceAlertListener implements EventListener {
    @Override
    public void update(String eventType, String data) {
        if ("ATTENDANCE_MARKED".equals(eventType)) {
            if (data.toUpperCase().contains("ABSENT")) {
                System.out.println("[AttendanceAlertListener] Student ABSENT — "
                        + "notifying parent for: " + data);
            } else {
                System.out.println("[AttendanceAlertListener] Student PRESENT — no action.");
            }
        }
    }
}
