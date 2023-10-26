package com.homemanager.Task.Spotify;

import com.homemanager.Task.Action.EventsTask;
import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class SpotifyPlayTask extends EventsTask {
    private String link;
    private long duration = 5 * 1000;
    private String actionName;

    public SpotifyPlayTask(StatusMessage statusMessages, String link, boolean isDirectory) {
        super(statusMessages);
        this.link = link;
        if (isDirectory) actionName = "PlaySpotifyDirectory";
        else actionName = "PlaySpotifyObject";
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", this.actionName);
            jsonParams.put("link", link);
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
        return 19276;
    }
}
