package com.school.model;

// Client
public class NotificationDeliveryClient {
    public static void main(String[] args) {
        String student  = "STU2024001";
        String examAlert    = "Reminder: Software Engineering exam on Thursday at 10:00 AM, Room 204.";
        String announcement = "School will be closed on Friday due to a national holiday.";
        String gradeAlert   = "Your grades for Semester 2 have been published.";

        // Teacher sends urgent exam alert via SMS
        NotificationSender sender = new NotificationSender(new SMSNotificationStrategy());
        sender.send(student, examAlert);

        System.out.println();

        // Teacher switches to Email for a formal announcement
        sender.setStrategy(new EmailNotificationStrategy());
        sender.send(student, announcement);

        System.out.println();

        // System uses In-App for a grade update
        sender.setStrategy(new InAppNotificationStrategy());
        sender.send(student, gradeAlert);

        System.out.println();

        // Broadcast exam alert to multiple students via SMS
        NotificationSender smsSender = new NotificationSender(new SMSNotificationStrategy());
        String[] students = {"STU2024001", "STU2024002", "STU2024003"};
        System.out.println("--- Broadcasting exam alert to all students ---");
        for (String s : students) {
            smsSender.send(s, examAlert);
        }
    }
}
