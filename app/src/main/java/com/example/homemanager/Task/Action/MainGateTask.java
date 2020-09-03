package com.example.homemanager.Task.Action;

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
    public String getUrl()
    {
        if (false == perm) {
            return "/Gate1";
        }
        return "/Gate1Perm";
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
