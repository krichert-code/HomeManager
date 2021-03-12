package com.homemanager.Task.Spotify;

import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SpotifyGetObjectTask extends Task implements SpotifyGetObject{
    private long duration = 0;
    private SpotifyInterface spotifyMessage;
    private String currentDirectory;
    private JSONArray currentDirectoryContent;


    public SpotifyGetObjectTask(SpotifyInterface spotifyMessage){
        super();
        this.spotifyMessage = spotifyMessage;
        this.currentDirectory = "";
    }

    public SpotifyGetObjectTask(SpotifyInterface spotifyMessage, String directory){
        super();
        this.spotifyMessage = spotifyMessage;
        this.currentDirectory = directory;
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
        return 3945;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "getSpotifyObject");
            if (currentDirectory.length() > 0)
                jsonParams.put("directory", this.currentDirectory);
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void parseContent(JSONObject content){
        try {
            currentDirectoryContent = content.getJSONArray("result");
            currentDirectory = content.getString("directory");
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inDoneStateNotification(){
        spotifyMessage.spotifyObjectRecived(this);
    }

    @Override
    public void inErrorStateNotification(){
        spotifyMessage.spotifyObjectRecivedError();
    }

    @Override
    public String getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public JSONArray getCurrentSpotifyContent() {
        return currentDirectoryContent;
    }
}
