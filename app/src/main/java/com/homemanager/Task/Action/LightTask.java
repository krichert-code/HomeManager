package com.homemanager.Task.Action;

import com.homemanager.Task.Task;

import org.json.JSONObject;

public class LightTask extends Task {
    private long duration = 2 * 1000;


    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "toggleLight");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void parseContent(JSONObject content) {

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
        return 1910;
    }
}

