package com.homemanager.Task;

import org.json.JSONObject;

import java.sql.Timestamp;

abstract public class Task implements TaskInterface {

    private int progressState;
    private Timestamp initTimestamp;

    private final int BEGIN_STATE       = 0;
    private final int READY_STATE       = 1;
    private final int DONE_STATE        = 2;
    private final int ERROR_STATE       = 3;

    public Task()
    {
        this.progressState = BEGIN_STATE;
        this.initTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public void inReadyStateNotification() {}
    public void inDoneStateNotification() {}
    public void inErrorStateNotification() {}

    public void setInReadyState() {
        this.progressState = READY_STATE;
        inReadyStateNotification();
    }

    public void setDoneState() {
        this.progressState = DONE_STATE;
        inDoneStateNotification();
    }

    public Timestamp getInitTimestamp()
    {
        return this.initTimestamp;
    }

    public void setErrorState() {
        this.progressState = ERROR_STATE;
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

    public boolean isReadyState()
    {
        if (this.progressState == READY_STATE){
            return true;
        }
        else return false;
    }
}
