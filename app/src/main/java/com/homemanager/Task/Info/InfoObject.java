package com.homemanager.Task.Info;

import java.util.ArrayList;
import java.util.List;

public class InfoObject {
    private String alarmTime;
    private String alarmState;
    private String todayHeaterStats;
    private String todayRainStats;
    private int    alarmStateValue;
    private String alarmStop;
    private String alarmVolume;
    private int alarmChannel;
    private String energyToday;
    private String energyTotal;

    private List<Integer> channelsID = new ArrayList<Integer>();
    private List<String>  channelsName = new ArrayList<String>();
    private List<Integer>  energyValues = new ArrayList<Integer>();
    private List<Integer>  energyPrevValues = new ArrayList<Integer>();

    public final int ALARM_ALWAYES   = 3;
    public final int ALARM_WEEK_DAYS = 2;
    public final int ALARM_NEVER     = 1;

    public int getAlarmStateValue() {
        return alarmStateValue;
    }

    public void setAlarmStateValue(int alarmStateValue) {
        this.alarmStateValue = alarmStateValue;
    }

    public String getAlarmStop() {
        return alarmStop;
    }

    public String getEnergyToday() {
        return energyToday;
    }

    public void setEnergyToday(String energy) { this.energyToday = energy; }

    public void setEnergyTotal(String energy) { this.energyTotal = energy; }

    public String getEnergyTotal() {
        return energyTotal;
    }

    public int getAlarmChannel() {
        return alarmChannel;
    }

    public void setAlarmChannel(int alarmChannel) {
        this.alarmChannel = alarmChannel;
    }

    public void setAlarmStop(String alarmStop) {
        this.alarmStop = alarmStop;
    }

    public String getAlarmVolume() {
        return alarmVolume;
    }

    public void setAlarmVolume(String alarmVolume) {
        this.alarmVolume = alarmVolume;
    }

    public List<Integer> getChannelsID() {
        return channelsID;
    }

    public List<Integer> getEnergyValues() {
        return energyValues;
    }

    public List<Integer> getEnergyPrevValues() {
        return energyPrevValues;
    }

    public void addEnergyValue(Integer energyValue) { this.energyValues.add(energyValue); }

    public void addEnergyPrevValue(Integer energyValue) { this.energyPrevValues.add(energyValue); }

    public void addChannelsID(int channelID) {
        this.channelsID.add(channelID);
    }

    public List<String> getChannelsName() {
        return channelsName;
    }

    public void addChannelsName(String channelName) {
        this.channelsName.add(channelName);
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getTodayHeaterStats() {
        return todayHeaterStats;
    }

    public void setTodayHeaterStats(String todayHeaterStats) {
        this.todayHeaterStats = todayHeaterStats;
    }

    public String getTodayRainStats() {
        return todayRainStats;
    }

    public void setTodayRainStats(String todayRainStats) {
        this.todayRainStats = todayRainStats;
    }

}
