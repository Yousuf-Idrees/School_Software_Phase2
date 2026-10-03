package com.school.model;

/**
 * Flyweight class for Classroom Seat Management
 * Represents shared seat type objects to reduce memory usage
 */
public class SeatType {
    private String type;

    public SeatType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void display(int seatNumber) {
        System.out.println("Seat " + seatNumber + " is " + type);
    }
}
