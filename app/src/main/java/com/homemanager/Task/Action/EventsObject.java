package com.homemanager.Task.Action;

import com.example.homemanager.R;

import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class EventsObject {
    private List<EventDescription> events = new ArrayList<EventDescription>();

    public void parseEvents(JSONObject obj)
    {
        events.clear();
        try {
            JSONArray arr = obj.getJSONArray("events");
            for (int i = 0; i < arr.length(); i++)
            {
                String desc = arr.getJSONObject(i).getString("eventDesc");
                String eventType = arr.getJSONObject(i).getString("eventType");
                String date = arr.getJSONObject(i).getString("eventDate");
                int iconId = -1;

                if (eventType.contains("calendar")){
                    iconId = R.drawable.calendar;
                }
                if (eventType.contains("gate")){
                    iconId = R.drawable.gate;
                }
                if (eventType.contains("radio")){
                    iconId = R.drawable.media;
                }
                if (eventType.contains("sprinkler")){
                    iconId = R.drawable.garden;
                }
                if (eventType.contains("status")){
                    iconId = R.drawable.information;
                }
                if (eventType.contains("heater")){
                    iconId = R.drawable.piec;
                }

                events.add(new EventDescription(desc, iconId, date));
            }
        }
        catch (Exception e){
            events.clear();
        }
    }

    public List<EventDescription> getEventsList(){
        return events;
    }

}
