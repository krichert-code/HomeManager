package com.homemanager.Task.Media;

import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaTask extends Task {
    private long duration = 0;
    private MediaMessage mediaMessage;
    private MediaObject mediaObject;


    public MediaTask(MediaMessage mediaMessage){
        super();
        this.mediaMessage = mediaMessage;
        this.mediaObject = new MediaObject();
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
        return 711;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "getMediaChannels");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void parseContent(JSONObject content){

        try {
            JSONArray array = content.getJSONArray("radio");
            for(int idx = 0; idx < array.length() ;idx++ ){
                JSONObject item = array.getJSONObject(idx);
                mediaObject.addRadioChannelId(item.getInt("channelid"));
                mediaObject.addRadioChannelname(item.getString("label"));
            }

            array = content.getJSONArray("tv");
            for(int idx = 0; idx < array.length() ;idx++ ){
                JSONObject item = array.getJSONObject(idx);
                mediaObject.addTvChannelId(item.getInt("channelid"));
                mediaObject.addTvChannelname(item.getString("label"));
            }

            mediaObject.setVolume(content.getInt("volume"));
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inDoneStateNotification(){
        mediaMessage.displayMedia(mediaObject);
    }
}
