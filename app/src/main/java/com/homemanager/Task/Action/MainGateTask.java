package com.homemanager.Task.Action;

import org.json.JSONObject;

public class MainGateTask extends EventsTask {
    private long duration = 30*1000;
    private StatusMessage statusMessages;
    private boolean perm;

    public MainGateTask(StatusMessage statusMessages, boolean perm){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.perm = perm;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            if (false == perm) {
                jsonParams.put("action", "Gate1");
            }
            else{
                jsonParams.put("action", "Gate1Perm");
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
        return 1;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.doneActionNotification();
    }

}
