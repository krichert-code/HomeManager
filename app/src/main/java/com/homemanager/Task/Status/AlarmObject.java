package com.homemanager.Task.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AlarmObject {
    private int state;
    private boolean valid;

    private void setAlarmState(int state){
        this.state = state;
    }

    private void setValid(boolean valid) { this.valid = valid; }

    public boolean isAlarmArmed() {
        return state == 1;
    }

    public boolean isValid() {return this.valid; }

    public void parseAlarmState(JSONObject content) {
        try {
            setAlarmState(content.getInt("alarm"));
            setValid(true);
        }
        catch(JSONException e){
            setValid(false);
        }
    }
}
