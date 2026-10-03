package com.school.model;

// Client
public class ExamRoomClient {
    public static void main(String[] args) {
        ExamRoomMediator mediator = new ExamRoomMediator();

        ExamTimer timer = new ExamTimer(mediator);
        StudentPanel studentPanel = new StudentPanel(mediator);
        ProctorPanel proctorPanel = new ProctorPanel(mediator);

        mediator.setTimer(timer);
        mediator.setStudentPanel(studentPanel);
        mediator.setProctorPanel(proctorPanel);

        // Exam starts
        timer.start();
        System.out.println();

        // Student submits during exam
        studentPanel.submitAnswer();
        System.out.println();

        // Time runs out
        timer.timeUp();
        System.out.println();

        // Student tries to submit after time is up
        studentPanel.submitAnswer();
    }
}
