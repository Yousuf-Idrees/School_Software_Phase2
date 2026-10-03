package com.school.model;

// Concrete Observer 3: SMS Alert Panel
public class SMSAlertPanel implements EventListener {
    @Override
    public void update(String eventType, String data) {
        if ("EXAM_NOTIFICATION".equals(eventType) || "ANNOUNCEMENT".equals(eventType)) {
            System.out.println("[SMSAlertPanel] SMS dispatched to all students → " + data);
        }
    }
}
