package com.school.model;

// Concrete Observer 1: Announcements Panel
public class AnnouncementPanel implements EventListener {
    private String latestAnnouncement = "No announcements yet.";

    @Override
    public void update(String eventType, String data) {
        if ("ANNOUNCEMENT".equals(eventType) || "GRADE_PUBLISHED".equals(eventType)) {
            latestAnnouncement = data;
            System.out.println("[AnnouncementPanel] UI refreshed → " + data);
        }
    }
}
