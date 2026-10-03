package com.school.model;

import java.util.*;

// Singleton + Subject
public class EventManager {

    // --- Singleton ---
    private static volatile EventManager instance;

    private EventManager() {
        listeners = new HashMap<>();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    // --- Observer (Subject) ---
    private final Map<String, List<EventListener>> listeners;

    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        System.out.println("[EventManager] " + listener.getClass().getSimpleName()
                + " subscribed to: " + eventType);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> group = listeners.get(eventType);
        if (group != null) {
            group.remove(listener);
            System.out.println("[EventManager] " + listener.getClass().getSimpleName()
                    + " unsubscribed from: " + eventType);
        }
    }

    public void notify(String eventType, String data) {
        List<EventListener> group = listeners.get(eventType);
        if (group == null || group.isEmpty()) {
            System.out.println("[EventManager] No listeners for: " + eventType);
            return;
        }
        System.out.println("[EventManager] Broadcasting '" + eventType
                + "' to " + group.size() + " listener(s).");
        for (EventListener listener : group) {
            listener.update(eventType, data);
        }
    }
}
