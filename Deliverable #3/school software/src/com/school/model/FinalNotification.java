package com.school.notifications;

/**
 * Refined Abstraction for Bridge Pattern
 * Handles final exam notifications
 */
public class FinalNotification extends ExamNotification {
    public FinalNotification(NotificationSender sender) {
        super(sender);
    }

    @Override
    public void notifyStudent() {
        sender.sendMessage("Final exam scheduled");
    }
}
