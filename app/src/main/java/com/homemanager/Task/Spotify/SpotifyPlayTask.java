package com.homemanager.Task.Spotify;

import com.homemanager.Task.Action.EventsTask;
import com.homemanager.Task.Action.StatusMessage;

import org.json.JSONObject;

public class SpotifyPlayTask extends EventsTask {
    private String link;
    private long duration = 5 * 1000;

    public SpotifyPlayTask(StatusMessage statusMessages, String link) {
        super(statusMessages);
        this.link = link;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "PlaySpotifyObject");
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
