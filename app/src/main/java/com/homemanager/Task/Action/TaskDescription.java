package com.homemanager.Task.Action;

import com.example.homemanager.R;

public class TaskDescription {
    private String description;
    private int icon;
    private String date;

    public TaskDescription(String desc, int icon, String date)
    {
        this.description = desc;
        this.icon = icon;
        this.date = date;
    }

    public String getDescription()
    {
        return this.description;
    }

    public int getIcon() { return this.icon; }

    public String getDate()
    {
        return this.date;
    }
}
