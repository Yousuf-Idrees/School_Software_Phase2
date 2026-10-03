package com.school.model;

// Concrete Strategy 2: Email
public class EmailNotificationStrategy implements NotificationDeliveryStrategy {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[EMAIL] Sending to " + recipient + "@school.edu: " + message);
        // In production: use JavaMail API
    }
}
