package com.homemanager.Task.Action;

import org.json.JSONObject;

public class DoorTask extends EventsTask {
    private long duration = 5*1000;
    private StatusMessage statusMessages;

    public DoorTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "Door");
        }
        catch(Exception e){
        }

        return jsonParams;
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
        return 2;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.doneActionNotification();
    }

}
