package com.example.homemanager.Task.Heater;

import com.example.homemanager.Task.Task;

import org.json.JSONObject;

public class HeaterSettingsTask extends Task {

    private HeaterMessage heaterMessage;
    private HeaterObject heaterObject;

    public HeaterSettingsTask(HeaterObject heaterObject, HeaterMessage heaterMessage){
        super();
        this.heaterObject = heaterObject;
        this.heaterMessage = heaterMessage;
    }

    @Override
    public String getUrl() {
        return "/setHeaterSettings";
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("day_temperature", Double.toString(heaterObject.getTempDay()));
            jsonParams.put("night_temperature", Double.toString(heaterObject.getTempNight()));
            jsonParams.put("day1", Integer.toString(heaterObject.getTempModeDay(0)));
            jsonParams.put("day2", Integer.toString(heaterObject.getTempModeDay(1)));
            jsonParams.put("day3", Integer.toString(heaterObject.getTempModeDay(2)));
            jsonParams.put("day4", Integer.toString(heaterObject.getTempModeDay(3)));
            jsonParams.put("day5", Integer.toString(heaterObject.getTempModeDay(4)));
            jsonParams.put("day6", Integer.toString(heaterObject.getTempModeDay(5)));
            jsonParams.put("day7", Integer.toString(heaterObject.getTempModeDay(6)));
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
        return 6989;
    }

    @Override
    public void inDoneStateNotification(){
        heaterMessage.displayHeaterData(heaterObject);
    }

}