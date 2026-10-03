package com.school.services;

public class GradeViewerService {
    private static volatile GradeViewerService instance;
    private GradeViewerService() {}

    public static GradeViewerService getInstance() {
        if (instance == null) {
            synchronized (GradeViewerService.class) {
                if (instance == null) {
                    instance = new GradeViewerService();
                }
            }
        }
        return instance;
    }

    public void viewGrades(String studentId) { 
        System.out.println("Displaying grades for student: " + studentId);
    }
}