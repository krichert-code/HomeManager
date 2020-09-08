package com.homemanager.Task;


import org.json.JSONObject;

public interface TaskInterface {
    public long getDuration();
    public void setDuration(long duration);
    public int getTaskDescriptor();
    public void parseContent(JSONObject content);
    public JSONObject getRequestData();

}
