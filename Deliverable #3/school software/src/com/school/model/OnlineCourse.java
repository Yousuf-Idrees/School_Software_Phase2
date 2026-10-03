package com.school.model;

// Concrete Template Method Implementation - Online Course
public class OnlineCourse extends CourseCreation {
    @Override
    void addTitle() {
        System.out.println("Online course title added");
    }

    @Override
    void addContent() {
        System.out.println("Upload videos & materials");
    }

    @Override
    void setSchedule() {
        System.out.println("Flexible schedule");
    }

    @Override
    void publish() {
        System.out.println("Publish online course");
    }
}
