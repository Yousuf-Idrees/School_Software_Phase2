package com.school.model;

// Concrete Mediator
public class ExamRoomMediator implements ExamMediator {

    private ExamTimer timer;
    private StudentPanel studentPanel;
    private ProctorPanel proctorPanel;

    public void setTimer(ExamTimer timer) { 
        this.timer = timer; 
    }

    public void setStudentPanel(StudentPanel studentPanel) { 
        this.studentPanel = studentPanel; 
    }

    public void setProctorPanel(ProctorPanel proctorPanel) { 
        this.proctorPanel = proctorPanel; 
    }

    @Override
    public void notify(ExamComponent sender, String event) {
        switch (event) {
            case "EXAM_STARTED":
                studentPanel.unlock();
                proctorPanel.receiveAlert("Exam has started. Monitoring active.");
                break;
            case "TIME_UP":
                studentPanel.lock();
                proctorPanel.receiveAlert("Time is up. All submissions closed.");
                break;
            case "ANSWER_SUBMITTED":
                proctorPanel.receiveAlert("A student submitted their answers.");
                break;
        }
    }
}
