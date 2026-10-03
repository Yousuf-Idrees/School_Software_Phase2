package com.school.model;

import java.util.HashMap;

/**
 * Classroom class representing a physical classroom with seats
 * Uses the Flyweight pattern to manage seat types efficiently
 */
public class Classroom {
    private String classroomName;
    private HashMap<Integer, Seat> seats;

    public Classroom(String classroomName, int numberOfSeats) {
        this.classroomName = classroomName;
        this.seats = new HashMap<>();
        initializeSeats(numberOfSeats);
    }

    /**
     * Initialize seats in the classroom
     * Creates standard seat types using the SeatManager
     */
    private void initializeSeats(int numberOfSeats) {
        SeatType standardSeat = SeatManager.getSeatType("Standard");
        for (int i = 1; i <= numberOfSeats; i++) {
            seats.put(i, new Seat(i, standardSeat));
        }
    }

    /**
     * Assign a student to a specific seat
     */
    public void assignStudentToSeat(int seatNumber, String studentName) {
        if (seats.containsKey(seatNumber)) {
            seats.get(seatNumber).assignStudent(studentName);
            System.out.println("Student " + studentName + " assigned to seat " + seatNumber);
        } else {
            System.out.println("Seat " + seatNumber + " does not exist");
        }
    }

    /**
     * Display all seats and their assignments
     */
    public void displayClassroomLayout() {
        System.out.println("\n=== Classroom: " + classroomName + " ===");
        for (Seat seat : seats.values()) {
            seat.displaySeatInfo();
        }
        System.out.println("Total seat types in cache: " + SeatManager.getCacheSize());
    }

    public String getClassroomName() {
        return classroomName;
    }

    public int getTotalSeats() {
        return seats.size();
    }
}
