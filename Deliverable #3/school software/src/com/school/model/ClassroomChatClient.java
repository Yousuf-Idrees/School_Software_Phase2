package com.school.model;

// Client
public class ClassroomChatClient {
    public static void main(String[] args) {
        ChatMediator mediator = new ClassroomChatMediator();

        ChatUser teacher = new TeacherUser(mediator, "Dr. Hassan");
        ChatUser student1 = new StudentUser(mediator, "Ahmed");
        ChatUser student2 = new StudentUser(mediator, "Sara");
        ChatUser student3 = new StudentUser(mediator, "Omar");

        mediator.addUser(teacher);
        mediator.addUser(student1);
        mediator.addUser(student2);
        mediator.addUser(student3);

        teacher.send("Good morning everyone, class starts in 5 minutes.");
        System.out.println();
        student1.send("Good morning, Dr. Hassan!");
        System.out.println();
        student2.send("Is the assignment deadline extended?");
    }
}
