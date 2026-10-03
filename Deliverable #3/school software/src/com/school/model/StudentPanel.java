package com.school.model;

// Concrete Colleague 2: Student Panel
public class StudentPanel extends ExamComponent {

    private boolean locked = false;

    public StudentPanel(ExamMediator mediator) {
        super(mediator);
    }

    @Override
    public String getName() { 
        return "StudentPanel"; 
    }

    public void submitAnswer() {
        if (!locked) {
            System.out.println("[StudentPanel] Answer submitted.");
            mediator.notify(this, "ANSWER_SUBMITTED");
        } else {
            System.out.println("[StudentPanel] Panel is locked. Cannot submit.");
        }
    }

    public void lock() {
        locked = true;
        System.out.println("[StudentPanel] Panel locked — no more submissions.");
    }

    public void unlock() {
        locked = false;
        System.out.println("[StudentPanel] Panel unlocked — exam in progress.");
    }
}
