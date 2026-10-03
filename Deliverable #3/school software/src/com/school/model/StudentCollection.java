package com.school.model;

import java.util.ArrayList;
import java.util.List;

// Collection
public class StudentCollection {
    private List<String> students = new ArrayList<>();

    public void addStudent(String name) {
        students.add(name);
    }

    public StudentIterator createIterator() {
        return new StudentListIterator();
    }

    private class StudentListIterator implements StudentIterator {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < students.size();
        }

        @Override
        public String next() {
            return students.get(index++);
        }
    }
}
