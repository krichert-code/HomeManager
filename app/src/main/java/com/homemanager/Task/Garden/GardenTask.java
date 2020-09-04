package com.homemanager.Task.Garden;

import com.homemanager.Task.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class GardenTask extends Task {

    private final GardenMessage gardenMessage;
    private GardenObject  gardenObject;

    public GardenTask(GardenMessage gardenMessage){
        super();
        this.gardenMessage = gardenMessage;
        this.gardenObject = new GardenObject();
    }

    @Override
    public String getUrl() {
        return "/getGardenSettings";
    }

    @Override
    public boolean isWriteRequest() {
        return false;
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
        return 989;
    }

    @Override
    public void parseContent(JSONObject content){
        try {
            gardenObject.setDuration(content.getInt("duration"));
            gardenObject.setGlobalEnable(content.getInt("globalEnable") != 0 ? true : false);
            gardenObject.setStartTime(content.getString("startTime"));
            gardenObject.setEnablePerDay(0, content.getInt("day1") != 0 ? true : false);
            gardenObject.setEnablePerDay(1, content.getInt("day2") != 0 ? true : false);
            gardenObject.setEnablePerDay(2, content.getInt("day3") != 0 ? true : false);
            gardenObject.setEnablePerDay(3, content.getInt("day4") != 0 ? true : false);
            gardenObject.setEnablePerDay(4, content.getInt("day5") != 0 ? true : false);
            gardenObject.setEnablePerDay(5, content.getInt("day6") != 0 ? true : false);
            gardenObject.setEnablePerDay(6, content.getInt("day7") != 0 ? true : false);
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inDoneStateNotification(){
        gardenMessage.displayGardenData(gardenObject);
    }
}
