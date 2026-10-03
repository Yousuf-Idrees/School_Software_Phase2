package com.school.assignments;

/**
 * Refined Abstraction for Bridge Pattern
 * Represents project assignments
 */
public class Project extends Assignment {
    public Project(Storage storage) {
        super(storage);
    }

    @Override
    public void submit(String fileName) {
        System.out.println("Submitting project:");
        storage.store(fileName);
    }
}
