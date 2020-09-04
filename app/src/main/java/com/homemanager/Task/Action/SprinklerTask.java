package com.homemanager.Task.Action;

public class SprinklerTask extends EventsTask {
    private long duration = 0;

    private String url;

    private StatusMessage statusMessages;

    public SprinklerTask(String url, StatusMessage statusMessages){
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
        return 20;
    }
}
