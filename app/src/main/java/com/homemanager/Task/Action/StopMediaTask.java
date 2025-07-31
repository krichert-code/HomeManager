package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class StopMediaTask extends EventsTask {
    private long duration = 2 * 1000;
    private boolean stopAndNext = false;
    private boolean sourceMp3 = false;

    private StatusMessage statusMessages;


    public StopMediaTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    public StopMediaTask(StatusMessage statusMessages, boolean stopAndNext, boolean sourceIsLocal){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.stopAndNext = stopAndNext;
        this.sourceMp3 = sourceIsLocal;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "Stop");
            if (this.stopAndNext) {
                jsonParams.put("next", 1);
                jsonParams.put("sourceIsLocal", sourceMp3);
            }

        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 1922;
    }
}