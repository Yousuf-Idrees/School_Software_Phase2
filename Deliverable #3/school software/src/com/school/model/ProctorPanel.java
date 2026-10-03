package com.school.model;

// Concrete Colleague 3: Proctor Panel
public class ProctorPanel extends ExamComponent {

    public ProctorPanel(ExamMediator mediator) {
        super(mediator);
    }

    @Override
    public String getName() { 
        return "ProctorPanel"; 
    }

    public void receiveAlert(String alert) {
        System.out.println("[ProctorPanel] Alert received: " + alert);
    }
}
