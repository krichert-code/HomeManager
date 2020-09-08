package com.homemanager.Task.Action;

import org.json.JSONObject;

public class GateTask extends EventsTask {
    private long duration = 20*1000;
    private StatusMessage statusMessages;

    public GateTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "Gate0");
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
        return 3;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.doneActionNotification();
    }
}
