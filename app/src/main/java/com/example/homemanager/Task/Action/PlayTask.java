package com.example.homemanager.Task.Action;

public class PlayTask extends EventsTask {
    private long duration = 5 * 1000;

    private StatusMessage statusMessages;
    private int channelId;

    public PlayTask(int channelId, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.channelId = channelId;
    }

    @Override
    public String getUrl() {
        return "/PlayPVR/" + channelId;
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
        return 1921;
    }
}