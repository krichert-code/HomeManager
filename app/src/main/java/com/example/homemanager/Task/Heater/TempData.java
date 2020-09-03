package com.example.homemanager.Task.Heater;

public class TempData {
    private String date;
    private double tempInside;
    private double tempOutside;

    public String getDate() {
        return date;
    }

    public void setData(String date) {
        this.date = date;
    }

    public double getTempInside() {
        return tempInside;
    }

    public void setTempInside(double tempInside) {
        this.tempInside = tempInside;
    }

    public double getTempOutside() {
        return tempOutside;
    }

    public void setTempOutside(double tempOutside) {
        this.tempOutside = tempOutside;
    }

}
