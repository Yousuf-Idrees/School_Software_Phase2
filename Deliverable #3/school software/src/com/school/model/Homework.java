package com.school.assignments;

/**
 * Refined Abstraction for Bridge Pattern
 * Represents homework assignments
 */
public class Homework extends Assignment {
    public Homework(Storage storage) {
        super(storage);
    }

    @Override
    public void submit(String fileName) {
        System.out.println("Submitting homework:");
        storage.store(fileName);
    }
}
