package com.homemanager.Task.CtrlDevice;
import androidx.annotation.NonNull;

import com.homemanager.Task.Alarm.Room;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CtrlDeviceObject implements Iterable<CtrlRoom> {
    private List<CtrlRoom> ctrlRooms = new ArrayList<CtrlRoom>();
    private boolean dataValid = true;

    public boolean isDataValid(){
        return dataValid;
    }

    public void createCtrlDeviceObject(JSONObject content) {
        try {
            JSONArray array = content.getJSONArray("rooms");
            if (content.getInt("error") != 0){
                dataValid = false;
            }

            for (int idx = 0; idx < array.length(); idx++) {
                CtrlRoom room = new CtrlRoom();
                JSONObject item = array.getJSONObject(idx);

                room.name = item.getString("name");
                room.lightIp = item.getJSONObject("light").getString("light_ip");

                if (item.getJSONObject("light").getInt("light_present") != 0){
                    room.lightExist = true;
                }else{
                    room.lightExist = false;
                }
                //TODO: Remove below line when HCC implement GetCtrlRooms method
                if (room.lightExist  == false) continue;

                if (item.getJSONObject("light").getString("light_state").equals("on")){
                    room.isLightOn = true;
                }else{
                    room.isLightOn = false;
                }


                if (item.getJSONObject("temperature").getInt("present") != 0){
                    room.tempSensorExist = true;
                }else{
                    room.tempSensorExist = false;
                }

                room.tempSensorValue = item.getJSONObject("temperature").getString("temperature");

                ctrlRooms.add(room);
            }
        } catch (Exception e) {
            dataValid = false;
        }
    }

    @NonNull
    @Override
    public Iterator<CtrlRoom> iterator() {
        return this.ctrlRooms.iterator();
    }
}
