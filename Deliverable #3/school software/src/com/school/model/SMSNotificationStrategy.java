package com.school.model;

// Concrete Strategy 1: SMS
public class SMSNotificationStrategy implements NotificationDeliveryStrategy {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS] Sending to " + recipient + ": " + message);
        // In production: integrate with SMS gateway API (e.g. Twilio)
    }
}
