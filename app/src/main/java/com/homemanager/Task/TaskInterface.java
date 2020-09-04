package com.homemanager.Task;


public interface TaskInterface {
    public String getUrl();
    public boolean isWriteRequest();
    public long getDuration();
    public void setDuration(long duration);
    public int getTaskDescriptor();
}
