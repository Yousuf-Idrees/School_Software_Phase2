package com.school.notifications;

/**
 * Abstraction for Bridge Pattern
 * Defines the interface for different types of exam notifications
 */
public abstract class ExamNotification {
    protected NotificationSender sender;

    public ExamNotification(NotificationSender sender) {
        this.sender = sender;
    }

    abstract void notifyStudent();
}
