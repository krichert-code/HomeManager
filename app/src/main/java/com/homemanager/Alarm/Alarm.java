package com.homemanager.Alarm;

import android.app.AlertDialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.example.homemanager.R;

import com.homemanager.Task.Action.LightTask;
import com.homemanager.TaskConnector;


public class Alarm {
    private View promptView;
    private TaskConnector tasks;

    public Alarm(TaskConnector taskConnector){
        this.tasks = taskConnector;
        //this.statusMessages = statusMessages;
        //this.gardenClass = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View createScreen(View view, final AlertDialog dialog){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.alarm, null);

        Button btnAdd = (Button) promptView.findViewById(R.id.button7);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd has been clicked
                tasks.putNewTask(new LightTask());
            }
        });

        return promptView;
    }


}