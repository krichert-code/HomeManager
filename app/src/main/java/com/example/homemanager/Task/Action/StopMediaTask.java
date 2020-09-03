package com.example.homemanager.Task.Action;

public class StopMediaTask extends EventsTask {
    private long duration = 2 * 1000;

    private StatusMessage statusMessages;


    public StopMediaTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public String getUrl() {
        return "/Stop";
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
        return 1922;
    }
}