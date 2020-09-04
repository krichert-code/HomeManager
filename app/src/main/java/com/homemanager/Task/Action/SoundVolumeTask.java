package com.homemanager.Task.Action;

public class SoundVolumeTask extends EventsTask {
    private long duration = 0;

    private String url;

    private StatusMessage statusMessages;

    private int volume;

    public static final int VOLUME_UP   = 0;
    public static final int VOLUME_DOWN = 1;
    public static final int VOLUME_SET  = 2;


    public SoundVolumeTask(int mode, int volume, StatusMessage statusMessages){
        super(statusMessages);
        if (volume < 0) volume = 0;
        if (volume>100) volume = 100;
        if (mode == VOLUME_UP) this.url = "/VolumeUp";
        if (mode == VOLUME_DOWN) this.url = "/VolumeDown";
        if (mode == VOLUME_SET) this.url = "/VolumeSet/" + volume;

        this.volume = volume;
        this.statusMessages = statusMessages;
        this.url = url;
    }

    public SoundVolumeTask(String url, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
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
