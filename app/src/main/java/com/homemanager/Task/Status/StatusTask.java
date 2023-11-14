package com.homemanager.Task.Status;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONObject;

public class StatusTask extends Task {
    private long duration = 0;
    private StatusMessage statusMessages;
    private StatusObject statusData = new StatusObject();

    public StatusTask(StatusMessage statusMessages){
        this.statusMessages = statusMessages;
    }

    @Override
    public long getDuration(){
        return duration;
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 791;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "status");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    final public void parseContent(JSONObject content){
        statusData.parseStatus(content);
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.displayStatusData(statusData);
    }
}
