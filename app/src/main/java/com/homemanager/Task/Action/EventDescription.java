package com.homemanager.Task.Action;

public class EventDescription {
    private String description;
    private int icon;
    private String date;

    public EventDescription(String desc, int icon, String date) {
        this.description = desc;
        this.icon = icon;
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public int getIcon() {
        return this.icon;
    }

    public String getDate() {
        return this.date;
    }
}
