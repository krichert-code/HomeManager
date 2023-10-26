package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class PlayTask extends EventsTask {
    private long duration = 5 * 1000;

    private StatusMessage statusMessages;
    private int channelId;

    public PlayTask(int channelId, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.channelId = channelId;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "PlayPVR");
            jsonParams.put("channel", channelId);
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
        return 1921;
    }
}