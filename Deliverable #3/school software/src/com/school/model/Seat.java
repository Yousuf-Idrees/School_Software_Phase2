package com.school.model;

/**
 * Classroom Seat class
 * Represents an individual seat in a classroom
 */
public class Seat {
    private int seatNumber;
    private String studentAssigned;
    private SeatType seatType;

    public Seat(int seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.studentAssigned = null;
    }

    public void assignStudent(String studentName) {
        this.studentAssigned = studentName;
    }

    public void displaySeatInfo() {
        seatType.display(seatNumber);
        if (studentAssigned != null) {
            System.out.println("  Assigned to: " + studentAssigned);
        } else {
            System.out.println("  Not assigned");
        }
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getStudentAssigned() {
        return studentAssigned;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}
