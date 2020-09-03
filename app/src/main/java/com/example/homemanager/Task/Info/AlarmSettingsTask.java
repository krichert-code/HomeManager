package com.example.homemanager.Task.Info;

import com.example.homemanager.Task.Task;

import org.json.JSONObject;

public class AlarmSettingsTask extends Task {

    private InfoMessage infoMessage;
    private InfoObject infoObject;

    public AlarmSettingsTask(InfoObject infoObject, InfoMessage infoMessage){
        super();
        this.infoObject = infoObject;
        this.infoMessage = infoMessage;
    }

    @Override
    public String getUrl() {
        return "/setAlarmSettings";
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();
        String day_policy= "disable";

        try {
            jsonParams.put("start_time",  infoObject.getAlarmTime() );
            jsonParams.put("stop_time",  infoObject.getAlarmStop() );
            jsonParams.put("channel",  Integer.toString(infoObject.getAlarmChannel()) );
            jsonParams.put("volume", infoObject.getAlarmVolume());
            if (infoObject.getAlarmStateValue() == infoObject.ALARM_ALWAYES)
                day_policy = "week";
            else if (infoObject.getAlarmStateValue() == infoObject.ALARM_WEEK_DAYS)
                day_policy = "week_day";
            jsonParams.put("day_policy",  day_policy );
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public boolean isWriteRequest() {
        return true;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void setDuration(long duration) {

    }

    @Override
    public int getTaskDescriptor() {
        return 11989;
    }

    @Override
    public void inDoneStateNotification(){
        infoMessage.displayInfo(infoObject);
    }

}