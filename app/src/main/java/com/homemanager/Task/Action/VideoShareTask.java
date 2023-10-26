package com.homemanager.Task.Action;

import com.example.homemanager.R;
import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class VideoShareTask extends EventsTask {
    private long duration = 5 * 1000;
    private StatusMessage statusMessages;
    private String link = "";

    public VideoShareTask(StatusMessage statusMessages, String link){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.link = link;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "VideoShare");
            jsonParams.put("link", link);
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public long getDuration()
    {
        return duration;
    }

    @Override
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor()
    {
        return 47823;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.displayHint(R.string.HintYTSent);
    }
}