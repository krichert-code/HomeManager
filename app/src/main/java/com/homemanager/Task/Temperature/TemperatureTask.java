package com.homemanager.Task.Temperature;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class TemperatureTask extends Task {
    private long duration = 0;
    private TemperatureMessage temperatureMessage;
    private TemperatureObject currentTemp;

    public TemperatureTask(TemperatureMessage temperatureMessage){
        super();
        this.temperatureMessage = temperatureMessage;
        currentTemp = new TemperatureObject();
    }

    @Override
    public String getUrl()
    {
        return "/temperature";
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
        return 121;
    }

    @Override
    public void parseContent(JSONObject content){

        try {
            if (content.getString("mode").contains("night")) {
                currentTemp.setMode(R.drawable.night);
            } else {

                currentTemp.setMode(R.drawable.day);
            }
            currentTemp.setTime(content.getString("time"));
            currentTemp.setTemperature(content.getString("temp"));
        }
        catch(JSONException e){

        }
    }

    @Override
    public void inProgressStateNotification(){
        temperatureMessage.displayTemperature(currentTemp);
    }

    @Override
    public void inErrorStateNotification(){
        temperatureMessage.displayErrorMessage();
    }
}
