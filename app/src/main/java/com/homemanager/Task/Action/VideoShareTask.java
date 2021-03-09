package com.homemanager.Task.Action;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoShareTask extends Task {
    private long duration = 0;
    private StatusMessage statusMessages;
    private String link = "";
    private int resultInfo = R.string.HintYTError;

    public VideoShareTask(StatusMessage statusMessages, String link){
        super();
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
    public void parseContent(JSONObject content) {
        try {
            resultInfo = R.string.HintYTSuccess;
            if (content.getInt("status") != 0) {
                resultInfo = R.string.HintYTError;
            }
        }
        catch(JSONException e){
            resultInfo = R.string.HintYTError;
        }
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
        statusMessages.displayHint(resultInfo);
    }
}