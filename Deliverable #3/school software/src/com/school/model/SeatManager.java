package com.school.model;

import java.util.HashMap;

/**
 * Seat Type Manager
 * Manages and reuses seat type objects
 */
public class SeatManager {
    private static HashMap<String, SeatType> seats = new HashMap<>();

    /**
     * Gets or creates a seat type
     * If the type already exists, returns the cached instance
     * Otherwise, creates a new instance and caches it
     */
    public static SeatType getSeatType(String type) {
        if (!seats.containsKey(type)) {
            seats.put(type, new SeatType(type));
            System.out.println("Creating new seat type: " + type);
        } else {
            System.out.println("Reusing existing seat type: " + type);
        }
        return seats.get(type);
    }

    /**
     * Returns the number of cached seat types
     */
    public static int getCacheSize() {
        return seats.size();
    }

    /**
     * Clears all cached seat types
     */
    public static void clearCache() {
        seats.clear();
    }
}
