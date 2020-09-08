package com.homemanager.Task.Garden;

import com.homemanager.Task.Task;

import org.json.JSONObject;

public class GardenSettingsTask extends Task{

    private GardenMessage gardenMessage;
    private GardenObject  gardenObject;

    public GardenSettingsTask(GardenObject gardenObject, GardenMessage gardenMessage){
        super();
        this.gardenObject = gardenObject;
        this.gardenMessage = gardenMessage;
    }

    @Override
    public void parseContent(JSONObject content) {
    }
    
    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "setGardenSettings");
            jsonParams.put("state", gardenObject.isGlobalEnable() ? "enable" : "disable" );
            jsonParams.put("start_time", gardenObject.getStartTime());
            jsonParams.put("duration", Integer.toString(gardenObject.getDuration()));
            jsonParams.put("day1", gardenObject.isEnablePerDay(0) ? "True" : "False");
            jsonParams.put("day2", gardenObject.isEnablePerDay(1) ? "True" : "False");
            jsonParams.put("day3", gardenObject.isEnablePerDay(2) ? "True" : "False");
            jsonParams.put("day4", gardenObject.isEnablePerDay(3) ? "True" : "False");
            jsonParams.put("day5", gardenObject.isEnablePerDay(4) ? "True" : "False");
            jsonParams.put("day6", gardenObject.isEnablePerDay(5) ? "True" : "False");
            jsonParams.put("day7", gardenObject.isEnablePerDay(6) ? "True" : "False");
        }
        catch(Exception e){
        }

        return jsonParams;
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
        return 5989;
    }

    @Override
    public void inDoneStateNotification(){
        gardenMessage.displayGardenData(gardenObject);
    }

}
