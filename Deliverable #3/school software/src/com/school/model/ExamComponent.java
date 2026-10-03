package com.school.model;

// Abstract Colleague
public abstract class ExamComponent {
    protected ExamMediator mediator;

    public ExamComponent(ExamMediator mediator) {
        this.mediator = mediator;
    }

    public abstract String getName();
}
