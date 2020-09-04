package com.homemanager.Task.Schedule;

import com.homemanager.Task.Task;

import org.json.JSONObject;

import java.util.List;

public class ScheduleTask extends Task {
    private long duration = 0;
    private ScheduleMessage scheduleMessage;
    private List<ScheduleObject> directionsElements;

    public ScheduleTask(ScheduleMessage scheduleMessage){
        super();
        this.scheduleMessage = scheduleMessage;
    }

    @Override
    public String getUrl()
    {
        return "/schedule";
    }

    @Override
    public boolean isWriteRequest() {
        return false;
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
        return 159;
    }

    @Override
    public void parseContent(JSONObject content){
        directionsElements = new ScheduleObjectParser().getScheduleData(content);
    }

    @Override
    public void inDoneStateNotification(){
        scheduleMessage.displaySchedule(directionsElements);
    }
}
