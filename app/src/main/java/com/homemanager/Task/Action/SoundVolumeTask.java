package com.homemanager.Task.Action;

import org.json.JSONObject;

public class SoundVolumeTask extends EventsTask {
    private long duration = 0;

    private String volumeMode;

    private StatusMessage statusMessages;

    private int volume;

    public static final int VOLUME_UP   = 0;
    public static final int VOLUME_DOWN = 1;
    public static final int VOLUME_SET  = 2;


    public SoundVolumeTask(int volumeMode, int volume, StatusMessage statusMessages){
        super(statusMessages);
        if (volume < 0) volume = 0;
        if (volume>100) volume = 100;
        if (volumeMode == VOLUME_UP) this.volumeMode = "VolumeUp";
        if (volumeMode == VOLUME_DOWN) this.volumeMode = "VolumeDown";
        if (volumeMode == VOLUME_SET) this.volumeMode = "VolumeSet";

        this.volume = volume;
        this.statusMessages = statusMessages;
    }

    public SoundVolumeTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", volumeMode);
            jsonParams.put("volume", volume);
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
        return 30;
    }
}
