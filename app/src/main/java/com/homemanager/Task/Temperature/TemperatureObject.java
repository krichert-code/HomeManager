package com.homemanager.Task.Temperature;

public class TemperatureObject {
    private String temperature;
    private String time;
    private int mode;


    public void setTemperature(String temperature){
        this.temperature = temperature;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public String getTemperature(){
        return this.temperature;
    }

    public String getTime(){
        return this.time;
    }

    public int getMode(){
        return this.mode;
    }

}
