package com.example.homemanager.Task.Action;

import com.example.homemanager.Task.Action.TaskDescription;

import java.util.List;

public interface StatusMessage {
    public void displayData(final List<TaskDescription> taskDesc);
    public void doneActionNotification();
    public void displayHint(int hint);
}
