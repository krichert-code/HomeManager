package com.homemanager.Task.Temperature;

public class TemperatureObject {
    private String temperatureInside;
    private String temperatureOutside;
    private String time;
    private int mode;
    private boolean validInside;
    private boolean validOutside;


    TemperatureObject(){
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

}
