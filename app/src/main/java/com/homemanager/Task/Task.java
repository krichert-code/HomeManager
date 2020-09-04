package com.homemanager.Task;

import org.json.JSONObject;

import java.sql.Timestamp;

abstract public class Task implements TaskInterface {

    private int progressState;
    private int errorDesc;
    private Timestamp initTimestamp;
    private String response;

    private final int BEGIN_STATE       = 0;
    private final int IN_PROGRESS_STATE = 1;
    private final int DONE_STATE        = 2;
    private final int ERROR_STATE       = 3;

    public Task()
    {
        this.progressState = BEGIN_STATE;
        this.initTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public void parseContent(JSONObject content){}
    public void inProgressStateNotification() {}
    public void inDoneStateNotification() {}
    public void inErrorStateNotification() {}
    public JSONObject getRequestData() { return null; }

    public void setInProgressState()
    {
        this.progressState = IN_PROGRESS_STATE;
        inProgressStateNotification();
    }

    public void setDoneState() {
        this.progressState = DONE_STATE;
        inDoneStateNotification();
    }

    public Timestamp getInitTimestamp()
    {
        return this.initTimestamp;
    }

    public void setErrorState(int error) {
        this.progressState = ERROR_STATE;
        this.errorDesc = error;
        inErrorStateNotification();
    }

    public boolean isErrorState()
    {
        if (this.progressState == ERROR_STATE){
            return true;
        }
        else return false;
    }

    public boolean isDoneState()
    {
        if (this.progressState == DONE_STATE){
            return true;
        }
        else return false;
    }

    public boolean isBeginState()
    {
        if (this.progressState == BEGIN_STATE){
            return true;
        }
        else return false;
    }

    public boolean isInProgressState()
    {
        if (this.progressState == IN_PROGRESS_STATE){
            return true;
        }
        else return false;
    }

    public int getErrorDescription()
    {
        return this.errorDesc;
    }

}
