package com.school.model;

// Concrete Template Method Implementation - Offline Course
public class OfflineCourse extends CourseCreation {
    @Override
    void addTitle() {
        System.out.println("Offline course title added");
    }

    @Override
    void addContent() {
        System.out.println("Prepare classroom material");
    }

    @Override
    void setSchedule() {
        System.out.println("Set classroom timings");
    }

    @Override
    void publish() {
        System.out.println("Publish offline course");
    }
}
