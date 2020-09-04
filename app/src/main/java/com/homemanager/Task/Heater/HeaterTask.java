package com.homemanager.Task.Heater;

import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class HeaterTask extends Task {
    private long duration = 0;
    private HeaterMessage heaterMessage;
    private HeaterObject heaterObject;

    public HeaterTask(HeaterMessage heaterMessage){
        super();
        this.heaterMessage = heaterMessage;
        this.heaterObject = new HeaterObject();
    }

    @Override
    public String getUrl()
    {
        return "/heaterCharts";
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
        return 189;
    }

    @Override
    public void parseContent(JSONObject content){

        try {
            heaterObject.setModeChartDay(content.getJSONObject("percentage").getInt("day"));
            heaterObject.setModeChartNight(content.getJSONObject("percentage").getInt("night"));
            heaterObject.setModeChartOff(content.getJSONObject("percentage").getInt("off"));
            heaterObject.setModeChartDayPerDay(content.getJSONObject("percentagePerDay").getInt("day"));
            heaterObject.setModeChartNightPerDay(content.getJSONObject("percentagePerDay").getInt("night"));
            heaterObject.setModeChartOffPerDay(content.getJSONObject("percentagePerDay").getInt("off"));

            JSONArray tempArray = content.getJSONArray("temp");
            for (int i = 0; i < tempArray.length(); i++) {
                TempData element  = new TempData();

                element.setData(tempArray.getJSONObject(i).getString("date"));
                element.setTempInside(tempArray.getJSONObject(i).getDouble("inside"));
                //todo: both data (inside and outside) must be stored in xml as double value
                element.setTempOutside( Double.valueOf(tempArray.getJSONObject(i).getString("outside")));
                heaterObject.addTemperatureEntry(element);
            }

            heaterObject.setTempDay(content.getJSONObject("settings").getDouble("dayTemp"));
            heaterObject.setTempNight(content.getJSONObject("settings").getDouble("nightTemp"));
            heaterObject.setThreshold(content.getJSONObject("settings").getDouble("threshold"));
            heaterObject.setTempModeDay(0, content.getJSONObject("settings").getInt("day1"));
            heaterObject.setTempModeDay(1, content.getJSONObject("settings").getInt("day2"));
            heaterObject.setTempModeDay(2, content.getJSONObject("settings").getInt("day3"));
            heaterObject.setTempModeDay(3, content.getJSONObject("settings").getInt("day4"));
            heaterObject.setTempModeDay(4, content.getJSONObject("settings").getInt("day5"));
            heaterObject.setTempModeDay(5, content.getJSONObject("settings").getInt("day6"));
            heaterObject.setTempModeDay(6, content.getJSONObject("settings").getInt("day7"));
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inDoneStateNotification(){
        heaterMessage.displayHeaterData(heaterObject);
    }
}
