package com.homemanager.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.homemanager.R;

import com.google.android.material.tabs.TabLayout;
import com.homemanager.Task.Action.LightTask;
import com.homemanager.Task.Alarm.AlarmMessage;
import com.homemanager.Task.Alarm.AlarmObject;
import com.homemanager.Task.Alarm.AlarmTask;
import com.homemanager.Task.Alarm.Room;
import com.homemanager.TaskConnector;

import java.util.Timer;
import java.util.TimerTask;


public class Alarm extends Activity implements AlarmMessage {
    private View promptView;
    private TaskConnector tasks;
    private final Alarm alarmInfo = this;

    public Alarm(TaskConnector taskConnector){
        this.tasks = taskConnector;
        //this.statusMessages = statusMessages;
        //this.gardenClass = this;
    }


    private void createInfoPanel(AlarmObject rooms){
        int index = 0;
        TableLayout infoPanel = (TableLayout) promptView.findViewById(R.id.alarmInfoPanel);
        // Stuff that updates the UI
        infoPanel.removeAllViews();

        for (final Room room:rooms) {
            TableRow row = new TableRow(promptView.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            TextView textview = new TextView(promptView.getContext());
            textview.setText(room.name);

            LinearLayout heaterLayout = new LinearLayout((promptView.getContext()));
            heaterLayout.setOrientation(LinearLayout.VERTICAL);
            TextView alarmText = new TextView(promptView.getContext());
            ImageView imageAlarm = new ImageView(promptView.getContext());
            imageAlarm.setImageDrawable(promptView.getResources().getDrawable(R.drawable.alarm));
            alarmText.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
            if ((room.isAlarmActivate || room.isPresenceOccur) && (room.alarmSensorExist)) {
                imageAlarm.setColorFilter(new LightingColorFilter(0x99999999, 0x550000));
                alarmText.setText("Aktywny");
            }
            else if (room.alarmSensorExist) {
                alarmText.setText("Nieaktywny");
            }
            heaterLayout.addView(imageAlarm);
            heaterLayout.addView(alarmText);


            LinearLayout tempLayout = new LinearLayout((promptView.getContext()));
            tempLayout.setOrientation(LinearLayout.VERTICAL);
            ImageView imageHeater = new ImageView(promptView.getContext());
            imageHeater.setImageDrawable(promptView.getResources().getDrawable(R.drawable.piec));
            TextView textTemp = new TextView(promptView.getContext());
            if (room.tempSensorExist) {
                textTemp.setText(room.tempSensorValue + " \u2103");
            }

            tempLayout.addView(imageHeater);
            tempLayout.addView(textTemp);

            final ImageButton light = new ImageButton((promptView.getContext()));
            if (room.lightExist) {
                light.setImageDrawable(promptView.getResources().getDrawable(R.drawable.lamp_off));
                TypedValue outValue = new TypedValue();
                promptView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
                light.setBackgroundResource(outValue.resourceId);
                light.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (room.isLightOn) {
                    //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                    light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
                }

                light.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (tasks.putNewTask(new LightTask(room.lightIp)) == 0) {
                            room.isLightOn = (!room.isLightOn);
                            /*if (room.isLightOn) {
                                //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                                light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
                            } else {
                                //light.setColorFilter(null);
                            }*/
                        }
                    }
                });
            }


            row.addView(textview);
            row.addView(heaterLayout);
            row.addView(tempLayout);
            if (room.lightExist) {
                row.addView(light);
            }
            if (index%2 == 0)
                row.setBackgroundColor(Color.LTGRAY);
            else
                row.setBackgroundColor(Color.GRAY);
            row.setPadding(0,10,0,10);

            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)textview.getLayoutParams();
            p.rightMargin=10;
            p.leftMargin = 20;
            p.gravity= Gravity.CENTER | Gravity.LEFT;

            p  = (LinearLayout.LayoutParams) textTemp.getLayoutParams();
            p.rightMargin=10;
            p.leftMargin = 40;
            p.gravity= Gravity.CENTER_VERTICAL | Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

            imageAlarm.getLayoutParams().height = 40;
            imageAlarm.getLayoutParams().width = 80;

            imageHeater.getLayoutParams().height = 40;
            imageHeater.getLayoutParams().width = 130;

            if (room.lightExist) {
                light.getLayoutParams().height = 80;
                light.getLayoutParams().width = 80;
                ((LinearLayout.LayoutParams) (light.getLayoutParams())).rightMargin = 15;
            }


            infoPanel.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            index++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View createScreen(View view, final AlertDialog dialog, final AlarmObject alarmObject){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.alarm, null);


        Button btnAdd = (Button) promptView.findViewById(R.id.alarmCloseButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });

        createInfoPanel(alarmObject);


        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabAlarmLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    promptView.findViewById(R.id.alarmInfoPanel).setVisibility(View.GONE);
                    promptView.findViewById(R.id.alarmEnablePanel).setVisibility(View.VISIBLE);
                }
                else if (tabItems.getTabAt(1) == tab){
                    promptView.findViewById(R.id.alarmInfoPanel).setVisibility(View.VISIBLE);
                    promptView.findViewById(R.id.alarmEnablePanel).setVisibility(View.GONE);
                }
                else {
                    promptView.findViewById(R.id.alarmInfoPanel).setVisibility(View.GONE);
                    promptView.findViewById(R.id.alarmEnablePanel).setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnAdd = (Button) promptView.findViewById(R.id.alarmActivateButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd1 has been clicked
                createInfoPanel(alarmObject);
            }
        });


        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask(){
            @Override
            public void run() {
                tasks.putNewTask(new AlarmTask(alarmInfo));
            }
        }, 5000, 5000);

        return promptView;
    }


    @Override
    public void displayAlarm(final AlarmObject alarmObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createInfoPanel(alarmObject);
            }
        });
    }
}