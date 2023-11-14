package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class GateTask extends EventsTask {
    private long duration = 30*1000;
    private StatusMessage statusMessages;
    private boolean perm;
    private int gateId;

    public GateTask(StatusMessage statusMessages, int gateId, long duration, boolean perm){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.perm = perm;
        this.gateId = gateId;
        this.duration = duration;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            if (false == perm) {
                jsonParams.put("action", "Gate");
                jsonParams.put("id", Integer.toString(gateId));
            }
            else{
                jsonParams.put("action", "GatePerm");
                jsonParams.put("id", Integer.toString(gateId));
            }
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
        return 1 + gateId;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.getStatusEventsDataOnly();
    }

}
