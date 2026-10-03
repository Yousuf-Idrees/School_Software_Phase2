package com.school.notifications;

/**
 * Refined Abstraction for Bridge Pattern
 * Handles midterm exam notifications
 */
public class MidtermNotification extends ExamNotification {
    public MidtermNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void notifyStudent() {
        sender.sendMessage("Midterm exam scheduled");
    }
}
