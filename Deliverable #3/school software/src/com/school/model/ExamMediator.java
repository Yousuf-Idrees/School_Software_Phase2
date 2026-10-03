package com.school.model;

// Mediator Interface
public interface ExamMediator {
    void notify(ExamComponent sender, String event);
}
