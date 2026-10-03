package com.school.model;

// Template Method Pattern - Abstract base class
public abstract class CourseCreation {
    public final void createCourse() {
        addTitle();
        addContent();
        setSchedule();
        publish();
    }

    abstract void addTitle();
    abstract void addContent();
    abstract void setSchedule();
    abstract void publish();
}
