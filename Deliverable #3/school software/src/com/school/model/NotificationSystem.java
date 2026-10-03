package com.school.model;

// 1. Component Interface
interface Notification {
    void send(String message);
}

// 2. Concrete Component (The "Real" Object)
class BasicNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("LOG: Announcement posted to School Dashboard: " + message);
    }
}

// 3. Base Decorator (The Wrapper)
abstract class NotificationDecorator implements Notification {
    protected Notification tempNotification;

    public NotificationDecorator(Notification notification) {
        this.tempNotification = notification;
    }

    @Override
    public void send(String message) {
        tempNotification.send(message);
    }
}

// 4. Concrete Decorator A (Email)
class EmailDecorator extends NotificationDecorator {
    public EmailDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("ACTION: Email alert sent to all subscribed Students/Teachers.");
    }
}

// 5. Concrete Decorator B (SMS)
class SMSDecorator extends NotificationDecorator {
    public SMSDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("ACTION: SMS emergency alert sent to Parent contacts.");
    }
}

// 6. Client Code - Demo
public class NotificationSystem {
    public static void main(String[] args) {
        System.out.println("--- Scenario 1: Basic Announcement ---");
        Notification simpleNote = new BasicNotification();
        simpleNote.send("The library will close at 5 PM today.");

        System.out.println("\n--- Scenario 2: Important Announcement (Dashboard + Email) ---");
        Notification importantNote = new EmailDecorator(new BasicNotification());
        importantNote.send("Exam schedules have been published.");

        System.out.println("\n--- Scenario 3: URGENT Emergency (Dashboard + Email + SMS) ---");
        Notification urgentNote = new SMSDecorator(
                                    new EmailDecorator(
                                        new BasicNotification()));
        urgentNote.send("School is closed tomorrow due to heavy rain.");
    }
}