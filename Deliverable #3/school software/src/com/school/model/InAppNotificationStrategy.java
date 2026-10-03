package com.school.model;

// Concrete Strategy 3: In-App
public class InAppNotificationStrategy implements NotificationDeliveryStrategy {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[IN-APP] Pop-up for " + recipient + ": " + message);
        // In production: trigger JOptionPane or update a notification panel in GUI
    }
}
