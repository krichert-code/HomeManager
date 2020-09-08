package com.homemanager.Task.Action;

import org.json.JSONObject;

public class StopMediaTask extends EventsTask {
    private long duration = 2 * 1000;

    private StatusMessage statusMessages;


    public StopMediaTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "Stop");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 1922;
    }
}