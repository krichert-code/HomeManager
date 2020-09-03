package com.example.homemanager.Task.Action;

import com.example.homemanager.Task.Task;

public class CecTask extends Task {
    private long duration = 2 * 1000;


    @Override
    public String getUrl() {
        return "/toggleCec";
    }

    @Override
    public boolean isWriteRequest() {
        return false;
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
        return 1945;
    }
}
