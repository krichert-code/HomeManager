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
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "info");
        }
        catch(Exception e){
        }

        return jsonParams;
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

            currentInfo.setEnergyTotal((content.getString("total_energy")));

            JSONArray array = content.getJSONArray("alarm_channels");

            for(int idx = 0; idx < array.length() ;idx++ ){
                JSONObject item = array.getJSONObject(idx);
                currentInfo.addChannelsID(item.getInt("channelid"));
                currentInfo.addChannelsName(item.getString("label"));
            }

            array = content.getJSONArray("total_per_month");
            for(int idx = 0; idx < array.length() ;idx++ ){
                currentInfo.addEnergyValue(array.getInt(idx));
            }

            array = content.getJSONArray("total_per_month_prev_value");
            for(int idx = 0; idx < array.length() ;idx++ ){
                currentInfo.addEnergyPrevValue(array.getInt(idx));
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
