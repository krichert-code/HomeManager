package com.homemanager.Task.Action;

import com.homemanager.Task.Task;

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

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "events");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void parseContent(JSONObject content){
        TaskDescriptionParser statusParser = new TaskDescriptionParser();
        taskDesc = statusParser.getDescription(content);
    }

    @Override
    public void inProgressStateNotification(){
        statusMessages.displayData(taskDesc);
    }
}


