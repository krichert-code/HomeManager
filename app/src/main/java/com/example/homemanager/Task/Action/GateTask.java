package com.example.homemanager.Task.Action;

public class GateTask extends EventsTask {
    private long duration = 20*1000;
    private StatusMessage statusMessages;

    public GateTask(StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
    }

    @Override
    public String getUrl()
    {
        return "/Gate0";
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
