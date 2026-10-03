package com.school.assignments;

/**
 * Abstraction for Bridge Pattern
 * Defines the interface for different types of assignments
 */
public abstract class Assignment {
    protected Storage storage;

    public Assignment(Storage storage) {
        this.storage = storage;
    }

    abstract void submit(String fileName);
}
