package com.homemanager.Task.Energy;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class EnergyTask extends Task {
    private long duration = 0;
    private EnergyMessage energyMessage;
    private EnergyObject energy;

    public EnergyTask(EnergyMessage energyMessage){
        super();
        this.energyMessage = energyMessage;
        energy = new EnergyObject();
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "energy");
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public long getDuration()
    {
        return duration;
    }

    @Override
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor()
    {
        return 263;
    }

    @Override
    public void parseContent(JSONObject content){

        try {
            JSONArray array = content.getJSONArray("energy");

            for (int i=0; i< array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("type").equals("today")) energy.setTodayPower(obj.getString("power"));
                if (obj.getString("type").equals("total")) energy.setTotalPower(obj.getString("power"));
                if (obj.getString("type").equals("current")) energy.setCurrentPower(obj.getString("power"));
            }
            energy.setValid(true);
        }
        catch(JSONException e){
            energy.setValid(false);
        }
    }

    @Override
    public void inProgressStateNotification(){
        energyMessage.displayEnergy(energy);
    }

    @Override
    public void inErrorStateNotification(){
        energyMessage.displayErrorMessage();
    }
}