package com.example.homemanager.Task.Action;

import com.example.homemanager.Task.Task;

import org.json.JSONObject;

import java.util.List;

public class EventsTask extends Task {
    private long duration = 0;
    private StatusMessage statusMessages;
    private List<TaskDescription> taskDesc;

    public EventsTask(StatusMessage statusMessages){
        this.statusMessages = statusMessages;
    }

    @Override
    public String getUrl() {
        return "/events";
    }

    @Override
    public boolean isWriteRequest() {
        return false;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 122;
    }

    public void parseContent(JSONObject content){
        TaskDescriptionParser statusParser = new TaskDescriptionParser();
        taskDesc = statusParser.getDescription(content);
    }

    @Override
    public void inProgressStateNotification(){
        statusMessages.displayData(taskDesc);
    }
}


