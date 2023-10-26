package com.homemanager.Task.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EnergyObject {
    private String current;
    private String today;
    private String total;
    private boolean valid;

    public void setCurrentPower(String power){
        this.current = power;
    }

    public void setTodayPower(String power){
        this.today = power;
    }

    public void setTotalPower(String power){
        this.total = power;
    }


    public String getCurrentPower() { return this.current; }

    public String getTodayPower() { return this.today; }

    public String getTotalPower(){
        return this.total;
    }


    public boolean isValid() {return this.valid; }

    public void setValid(boolean valid) { this.valid = valid; }

    public void parseEnergy(JSONObject content) {
        try {
            JSONArray array = content.getJSONObject("energy").getJSONArray("energy");

            for (int i=0; i< array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("type").equals("today"))
                        setTodayPower(obj.getString("power"));
                if (obj.getString("type").equals("total"))
                        setTotalPower(obj.getString("power"));
                if (obj.getString("type").equals("current"))
                        setCurrentPower(obj.getString("power"));
            }
            setValid(true);
        }
        catch(JSONException e){
            setValid(false);
        }
    }
}



