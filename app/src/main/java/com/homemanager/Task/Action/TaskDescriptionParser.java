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
                String icon = arr.getJSONObject(i).getString("eventIcon");
                String date = arr.getJSONObject(i).getString("eventDate");
                int iconId = -1;

                if (icon.contains("calendar")){
                    iconId = R.drawable.calendar;
                }
                if (icon.contains("gate")){
                    iconId = R.drawable.gate;
                }
                if (icon.contains("radio")){
                    iconId = R.drawable.media;
                }
                if (icon.contains("garden")){
                    iconId = R.drawable.garden;
                }
                if (icon.contains("cesspit")){
                    iconId = R.drawable.cesspit;
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
