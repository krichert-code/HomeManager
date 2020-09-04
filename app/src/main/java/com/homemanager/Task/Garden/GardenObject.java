package com.homemanager.Task.Garden;

public class GardenObject {
    private boolean globalEnable;
    private int duration;
    private String startTime;
    private boolean[] enablePerDay = new boolean[7];

    public boolean isGlobalEnable() {
        return globalEnable;
    }

    public void setGlobalEnable(boolean globalEnable) {
        this.globalEnable = globalEnable;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isEnablePerDay(int dayIdx) {
        return enablePerDay[dayIdx];
    }

    public void setEnablePerDay(int dayIdx, boolean enablePerDay) {
        this.enablePerDay[dayIdx] = enablePerDay;
    }
}
