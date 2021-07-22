package com.homemanager.Task.Action;

import com.example.homemanager.R;

import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class TaskDescriptionParser {
    public List<TaskDescription> getDescription(JSONObject obj)
    {
        List<TaskDescription> taskDesc = new ArrayList<TaskDescription>();

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

                taskDesc.add(new TaskDescription(desc, iconId, date));
            }
        }
        catch (Exception e){
            taskDesc.clear();
        }
        return taskDesc;
    }
}
