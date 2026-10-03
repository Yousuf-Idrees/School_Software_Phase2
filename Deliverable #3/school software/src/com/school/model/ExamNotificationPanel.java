package com.school.model;

// Concrete Observer 2: Exam Notification Panel
public class ExamNotificationPanel implements EventListener {
    @Override
    public void update(String eventType, String data) {
        if ("EXAM_NOTIFICATION".equals(eventType)) {
            System.out.println("[ExamNotificationPanel] New exam alert → " + data);
        }
    }
}
