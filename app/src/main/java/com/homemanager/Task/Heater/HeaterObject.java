package com.homemanager.Task.Heater;


import java.util.ArrayList;
import java.util.List;

public class HeaterObject {
    private int modeChartDay;
    private int modeChartNight;
    private int modeChartOff;
    private int modeChartDayPerDay;
    private int modeChartNightPerDay;
    private int modeChartOffPerDay;

    private double tempDay;
    private double tempNight;
    private double threshold;

    private boolean mainHeater;
    private boolean additionHeater;

    private int[] tempModeDay = new int[7];
    private int[] tempModeDayAddition = new int[7];

    private List<TempData> temperatureData;

    public boolean isAdditionHeater() {
        return additionHeater;
    }

    public void setAdditionHeater(boolean additionHeater) {
        this.additionHeater = additionHeater;
    }

    public boolean isMainHeater() {
        return mainHeater;
    }

    public void setMainHeater(boolean mainHeater) {
        this.mainHeater = mainHeater;
    }

    public int getModeChartDay() {
        return modeChartDay;
    }

    public void setModeChartDay(int modeChartDay) {
        this.modeChartDay = modeChartDay;
    }

    public int getModeChartNight() {
        return modeChartNight;
    }

    public void setModeChartNight(int modeChartNight) {
        this.modeChartNight = modeChartNight;
    }

    public int getModeChartOff() {
        return modeChartOff;
    }

    public void setModeChartOff(int modeChartOff) {
        this.modeChartOff = modeChartOff;
    }

    public List<TempData> getTemperatureData() {
        return temperatureData;
    }

    public void addTemperatureEntry(TempData tempData){
        temperatureData.add(tempData);
    }

    public HeaterObject(){
        temperatureData = new ArrayList<TempData>();
    }

    public int getModeChartDayPerDay() {
        return modeChartDayPerDay;
    }

    public void setModeChartDayPerDay(int modeChartDayPerDay) {
        this.modeChartDayPerDay = modeChartDayPerDay;
    }

    public int getModeChartNightPerDay() {
        return modeChartNightPerDay;
    }

    public void setModeChartNightPerDay(int modeChartNightPerDay) {
        this.modeChartNightPerDay = modeChartNightPerDay;
    }

    public int getModeChartOffPerDay() {
        return modeChartOffPerDay;
    }

    public void setModeChartOffPerDay(int modeChartOffPerDay) {
        this.modeChartOffPerDay = modeChartOffPerDay;
    }

    public double getTempDay() {
        return tempDay;
    }

    public void setTempDay(double tempDay) {
        this.tempDay = tempDay;
    }

    public double getTempNight() {
        return tempNight;
    }

    public void setTempNight(double tempNight) {
        this.tempNight = tempNight;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getTempModeDay(int index) {
        return tempModeDay[index];
    }

    public void setTempModeDay(int index, int tempModeDay) {
        this.tempModeDay[index] = tempModeDay;
    }

    public int getTempAdditionModeDay(int index) {
        return tempModeDayAddition[index];
    }

    public void setTempAdditionModeDay(int index, int tempModeDay) {
        this.tempModeDayAddition[index] = tempModeDay;
    }

}
