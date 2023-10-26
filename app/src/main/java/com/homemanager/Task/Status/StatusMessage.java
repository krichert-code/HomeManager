package com.homemanager.Task.Status;

import android.content.Context;
import android.content.SharedPreferences;

import com.homemanager.Task.Status.StatusObject;

import java.util.List;

public interface StatusMessage {
    public void displayStatusData(final StatusObject statusData);
    public void actionDoneNotification();

    public void displayHint(int hint);
    public void displayCustomHint(String hint);
}
