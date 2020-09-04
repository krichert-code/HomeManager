package com.homemanager.Task.Action;

public class DoorTask extends EventsTask {
    private long duration = 5*1000;
    private StatusMessage statusMessages;

    public DoorTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public String getUrl() {
        return "/Door";
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
        return 2;
    }

    @Override
    public void inDoneStateNotification(){
        statusMessages.doneActionNotification();
    }

}
