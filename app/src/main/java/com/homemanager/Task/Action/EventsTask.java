package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;
import com.homemanager.Task.Status.StatusObject;
import com.homemanager.Task.Task;

import org.json.JSONObject;

public class EventsTask extends Task {
    private long duration = 0;
    private StatusMessage statusMessages;
    StatusObject statusData = new StatusObject();

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
    final public void parseContent(JSONObject content){
        EventsObject eventsObject = new EventsObject();
        eventsObject.parseEvents(content);
        statusData.setEventsObject(eventsObject);
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.displayStatusData(statusData);
    }
}


