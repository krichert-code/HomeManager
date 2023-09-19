package com.homemanager.Task.Action;

import com.homemanager.Task.Task;

import org.json.JSONObject;

public class LightTask extends Task {
    private long duration = 1 * 500;
    private String ip;

    public LightTask(String ip){
        this.ip = ip;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "toggleLight");
            jsonParams.put("ip", ip);
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

