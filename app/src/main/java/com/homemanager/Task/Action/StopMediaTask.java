package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class StopMediaTask extends EventsTask {
    private long duration = 2 * 1000;
    private boolean stopAndnext = false;

    private StatusMessage statusMessages;


    public StopMediaTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    public StopMediaTask(StatusMessage statusMessages, boolean next){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.stopAndnext = next;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "Stop");
            if (this.stopAndnext)
                jsonParams.put("next", 1);
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