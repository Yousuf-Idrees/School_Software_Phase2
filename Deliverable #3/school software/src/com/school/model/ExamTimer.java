package com.school.model;

// Concrete Colleague 1: Exam Timer
public class ExamTimer extends ExamComponent {

    public ExamTimer(ExamMediator mediator) {
        super(mediator);
    }

    @Override
    public String getName() { 
        return "ExamTimer"; 
    }

    public void start() {
        System.out.println("[ExamTimer] Exam started. Timer running.");
        mediator.notify(this, "EXAM_STARTED");
    }

    public void timeUp() {
        System.out.println("[ExamTimer] Time is up!");
        mediator.notify(this, "TIME_UP");
    }
}
