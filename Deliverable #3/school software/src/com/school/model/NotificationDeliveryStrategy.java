package com.school.model;

// Strategy Interface
public interface NotificationDeliveryStrategy {
    void sendNotification(String recipient, String message);
}
