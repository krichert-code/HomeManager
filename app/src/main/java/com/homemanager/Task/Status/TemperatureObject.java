package com.homemanager.Task.Status;

import com.example.homemanager.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TemperatureObject {
    private String temperatureInside;
    private String temperatureOutside;
    private String time;
    private int mode;
    private boolean validInside;
    private boolean validOutside;


    public TemperatureObject(){
        validInside = false;
        validOutside = false;
    }

    public void setInsideTemperature(String temperature){
        this.temperatureInside = temperature;
    }

    public void setOutsideTemperature(String temperature){
        this.temperatureOutside = temperature;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public String getInsideTemperature(){
        return this.temperatureInside;
    }

    public String getOutsideTemperature() { return this.temperatureOutside; }

    public String getTime(){
        return this.time;
    }

    public int getMode(){
        return this.mode;
    }

    public boolean isValidInside() {return this.validInside; }

    public void setValidInside(boolean valid) { this.validInside = valid; }

    public boolean isValidOutside() {return this.validOutside; }

    public void setValidOutside(boolean valid) { this.validOutside = valid; }

    public void parseTemperature(JSONObject content){
        // parse temperature
        try {
            JSONObject tempContent = content.getJSONObject("temperature");
            if (tempContent.getString("statusInside").contains("OK")) {
                setInsideTemperature(tempContent.getString("tempInside"));
                setValidInside(true);
            } else {
                setValidInside(false);
                setInsideTemperature("-");
            }

            if (tempContent.getString("statusOutside").contains("OK")) {
                setOutsideTemperature(tempContent.getString("tempOutside"));
                setValidOutside(true);
            } else {
                setValidOutside(false);
                setOutsideTemperature("-");
            }

            if (tempContent.getString("mode").contains("night")) {
                setMode(R.drawable.night);
            } else {
                setMode(R.drawable.day);
            }
            setTime(tempContent.getString("time"));
        }
        catch(JSONException e){
            setValidInside(false);
            setValidOutside(false);
        }
    }
}
