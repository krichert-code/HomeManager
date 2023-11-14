package com.homemanager.Task.Alarm;

import com.homemanager.Task.Task;

import org.json.JSONObject;

public class AlarmTask extends Task {

    private AlarmMessage alarmMessage;
    private AlarmObject alarmObject;
    private long duration = 2 * 1000;

    public AlarmTask(AlarmMessage alarmMessage){
        alarmObject = new AlarmObject();
        this.alarmMessage = alarmMessage;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void setDuration(long duration) {

    }

    @Override
    public int getTaskDescriptor() {
        return 9786;
    }

    @Override
    public void parseContent(JSONObject content) {
        alarmObject.createAlarmObject(content);
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "GetRooms");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void inDoneStateNotification(){
        if (alarmObject.isDataValid())
            alarmMessage.displayAlarm(alarmObject);
        else
            alarmMessage.errorAlarmOccur();
    }

    @Override
    public void inErrorStateNotification(){
        alarmMessage.errorAlarmOccur();
    }
}
