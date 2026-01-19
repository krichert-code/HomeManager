package com.homemanager.Task.Alarm;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlarmObject implements Iterable<Room> {
    private List<Room> rooms = new ArrayList<Room>();
    private boolean dataValid = true;

    public boolean isDataValid(){
        return dataValid;
    }

    public void createAlarmObject(JSONObject content) {
        try {
            JSONArray array = content.getJSONArray("rooms");
            if (content.getInt("error") != 0){
                dataValid = false;
            }

            for (int idx = 0; idx < array.length(); idx++) {
                Room room;
                JSONObject item = array.getJSONObject(idx);
                if (item.getString("name").isEmpty()) continue;

                room = new Room();
                room.name = item.getString("name");
                room.lightIp = item.getJSONObject("light").getString("light_ip");

                if (item.getJSONObject("light").getInt("light_present") != 0){
                    room.lightExist = true;
                }else{
                    room.lightExist = false;
                }

                if (item.getJSONObject("light").getString("light_state").equals("on")){
                    room.isLightOn = true;
                }else{
                    room.isLightOn = false;
                }

                if (item.getJSONObject("alarm").getInt("present") != 0){
                    room.alarmSensorExist = true;
                }else{
                    room.alarmSensorExist = false;
                }

                if (item.getJSONObject("alarm").getString("alert").equals("on")){
                    room.isAlarmActivate = true;
                }else{
                    room.isAlarmActivate = false;
                }

                if (item.getJSONObject("alarm").getString("presence").equals("on")){
                    room.isPresenceOccur = true;
                }else{
                    room.isPresenceOccur = false;
                }

                if (item.getJSONObject("temperature").getInt("present") != 0){
                    room.tempSensorExist = true;
                }else{
                    room.tempSensorExist = false;
                }

                room.tempSensorValue = item.getJSONObject("temperature").getString("temperature");

                rooms.add(room);
            }
        } catch (Exception e) {
            dataValid = false;
        }
    }

    @NonNull
    @Override
    public Iterator<Room> iterator() {
        return this.rooms.iterator();
    }
}
