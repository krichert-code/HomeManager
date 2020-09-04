package com.homemanager.Task.Info;

import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoTask extends Task {
    private long duration = 0;
    private InfoMessage infoMessage;
    private InfoObject currentInfo = new InfoObject();

    public InfoTask(InfoMessage infoMessage){
        super();
        this.infoMessage = infoMessage;
    }

    @Override
    public String getUrl()
    {
        return "/info";
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
        return 129;
    }

    @Override
    public void parseContent(JSONObject content){

        try {
            currentInfo.setAlarmTime(content.getString("alarm_start"));
            currentInfo.setAlarmState(content.getString("alarm_state"));
            currentInfo.setTodayHeaterStats(content.getString("heater_time"));
            currentInfo.setTodayRainStats(content.getString("rain"));
            currentInfo.setAlarmChannel(Integer.parseInt(content.getString("alarm_channel")));

            currentInfo.setAlarmStateValue(content.getInt("alarm_state_value"));
            currentInfo.setAlarmStop(content.getString("alarm_stop"));
            currentInfo.setAlarmVolume(content.getString("alarm_volume"));
            JSONArray array = content.getJSONArray("alarm_channels");

            for(int idx = 0; idx < array.length() ;idx++ ){
                JSONObject item = array.getJSONObject(idx);
                currentInfo.addChannelsID(item.getInt("channelid"));
                currentInfo.addChannelsName(item.getString("label"));
            }
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inDoneStateNotification(){
        infoMessage.displayInfo(currentInfo);
    }
}
