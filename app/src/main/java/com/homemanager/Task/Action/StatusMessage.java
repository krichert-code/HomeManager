package com.homemanager.Task.Action;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public interface StatusMessage {
    public void displayData(final List<TaskDescription> taskDesc);
    public void doneActionNotification();
    public void displayHint(int hint);
    public void displayCustomHint(String hint);
}
