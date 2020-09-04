package com.homemanager.Alarm;

import android.app.AlertDialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.homemanager.R;
import com.homemanager.Schedule.ScheduleRow;
import com.homemanager.Task.Schedule.ScheduleObject;

import java.util.ArrayList;
import java.util.List;

public class Alarm {
    private View promptView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View createScreen(View view, final AlertDialog dialog){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.alarm, null);

        return promptView;
    }
}