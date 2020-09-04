package com.homemanager.Task.Schedule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleObjectParser {

    public List<ScheduleObject> getScheduleData(JSONObject content){
        ScheduleObject element;
        List<ScheduleObject> directionsElements = new ArrayList<ScheduleObject>();

        directionsElements.clear();
        try {
            JSONArray arr = content.getJSONObject("DirectionA").getJSONArray("connections");
            for (int i = 0; i < arr.length(); i++) {
                element = new ScheduleObject();
                String arrival = arr.getJSONObject(i).getString("arrival");
                element.setArrival(arrival.substring(arrival.indexOf("T")+1, arrival.indexOf(".")));
                element.setDeparture(arr.getJSONObject(i).getString("departure"));
                element.setDistance(arr.getJSONObject(i).getInt("distance"));
                element.setTravelTime(arr.getJSONObject(i).getInt("travel_time"));
                element.setDirectionId(arr.getJSONObject(i).getInt("end_station_id"));
                directionsElements.add(element);
            }

            arr = content.getJSONObject("DirectionB").getJSONArray("connections");
            for (int i = 0; i < arr.length(); i++) {
                element = new ScheduleObject();
                String arrival = arr.getJSONObject(i).getString("arrival");
                element.setArrival(arrival.substring(arrival.indexOf("T")+1, arrival.indexOf(".")));
                element.setDeparture(arr.getJSONObject(i).getString("departure"));
                element.setDistance(arr.getJSONObject(i).getInt("distance"));
                element.setTravelTime(arr.getJSONObject(i).getInt("travel_time"));
                element.setDirectionId(arr.getJSONObject(i).getInt("end_station_id"));
                directionsElements.add(element);
            }
        }
        catch(Exception e)
        {

        }
        return directionsElements;
    }
}
