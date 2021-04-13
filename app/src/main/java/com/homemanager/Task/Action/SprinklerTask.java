package com.homemanager.Task.Action;

import org.json.JSONObject;

public class SprinklerTask extends EventsTask {
    public final static int MANUAL_DEVICE_1   = 1;
    public final static int MANUAL_DEVICE_2   = 2;
    public final static int MANUAL_DEVICE_3   = 3;
    public final static int MANUAL_STOP       = 4;
    public final static int AUTO_FORCE        = 5;

    private long duration = 0;
    private int mode;
    private StatusMessage statusMessages;

    public SprinklerTask(int mode, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.mode = mode;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            if (mode<=MANUAL_DEVICE_3) {
                jsonParams.put("action", "SprinklerOn");
                jsonParams.put("id", Integer.toString(this.mode));
            }
            else if (mode == MANUAL_STOP){
                jsonParams.put("action", "SprinklerOff");
            }
            else if (mode == AUTO_FORCE){
                jsonParams.put("action", "SprinklerForceAuto");
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

    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 20;
    }
}
