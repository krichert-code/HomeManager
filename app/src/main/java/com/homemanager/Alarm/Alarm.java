package com.homemanager.Alarm;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.homemanager.R;

import com.google.android.material.tabs.TabLayout;
import com.homemanager.Task.Action.LightTask;
import com.homemanager.TaskConnector;


public class Alarm {
    private View promptView;
    private TaskConnector tasks;
    private AlarmArea[] rooms = new AlarmArea[11];

    public Alarm(TaskConnector taskConnector){
        this.tasks = taskConnector;
        rooms[0] = new AlarmArea("Kuchnia", "21", true, false);
        rooms[1] = new AlarmArea("Ogród", "4", false, true);
        rooms[2] = new AlarmArea("Salon", "21", false, false);
        rooms[3] = new AlarmArea("Gabinet", "21", false, false);
        rooms[4] = new AlarmArea("Sypialnia Nati", "20", false, false);
        rooms[5] = new AlarmArea("Sypialnia Pati", "21", false, false);
        rooms[6] = new AlarmArea("Sypialnia", "20", false, false);
        rooms[7] = new AlarmArea("Garderoba", "20", false, false);
        rooms[8] = new AlarmArea("Pralnia", "19", false, false);
        rooms[9] = new AlarmArea("Garaż", "17", false, false);
        rooms[10] = new AlarmArea("Kotłownia", "18", false, false);
        //this.statusMessages = statusMessages;
        //this.gardenClass = this;
    }


    private void createInfoPanel(){
        int index = 0;
        TableLayout infoPanel = (TableLayout) promptView.findViewById(R.id.alarmInfoPanel);
        // Stuff that updates the UI
        infoPanel.removeAllViews();

        for (final AlarmArea room:rooms) {
            TableRow row = new TableRow(promptView.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            TextView textview = new TextView(promptView.getContext());
            textview.setText(room.getName());

            LinearLayout heaterLayout = new LinearLayout((promptView.getContext()));
            heaterLayout.setOrientation(LinearLayout.VERTICAL);
            TextView alarmText = new TextView(promptView.getContext());
            ImageView imageAlarm = new ImageView(promptView.getContext());
            imageAlarm.setImageDrawable(promptView.getResources().getDrawable(R.drawable.alarm));
            alarmText.setTextSize(TypedValue.COMPLEX_UNIT_SP,8);
            if (room.isAlarmActive()) {
                imageAlarm.setColorFilter(new LightingColorFilter(0x99999999, 0x550000));
                alarmText.setText("Aktywny");
            }
            else{
                alarmText.setText("Nieaktywny");
            }
            heaterLayout.addView(imageAlarm);
            heaterLayout.addView(alarmText);


            LinearLayout tempLayout = new LinearLayout((promptView.getContext()));
            tempLayout.setOrientation(LinearLayout.VERTICAL);
            ImageView imageHeater = new ImageView(promptView.getContext());
            imageHeater.setImageDrawable(promptView.getResources().getDrawable(R.drawable.piec));
            TextView textTemp = new TextView(promptView.getContext());
            textTemp.setText(room.getTemperature() + " \u2103");
            tempLayout.addView(imageHeater);
            tempLayout.addView(textTemp);

            final ImageButton light = new ImageButton((promptView.getContext()));
            light.setImageDrawable(promptView.getResources().getDrawable(R.drawable.lamp_off));
            TypedValue outValue = new TypedValue();
            promptView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
            light.setBackgroundResource(outValue.resourceId);
            light.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (room.isLightOn()) {
                //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
            }
            if (room.isLightConnected()) {
                light.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (tasks.putNewTask(new LightTask()) == 0) {
                            room.toggleLight();
                            if (room.isLightOn()) {
                                //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                                light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
                            } else {
                                light.setColorFilter(null);
                            }
                        }
                    }
                });
            }


            row.addView(textview);
            row.addView(heaterLayout);
            row.addView(tempLayout);
            row.addView(light);
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

            light.getLayoutParams().height = 80;
            light.getLayoutParams().width = 80;
            ((LinearLayout.LayoutParams)(light.getLayoutParams())).rightMargin = 15;


            infoPanel.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            index++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View createScreen(View view, final AlertDialog dialog){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.alarm, null);


        Button btnAdd = (Button) promptView.findViewById(R.id.alarmCloseButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });

        createInfoPanel();


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


        return promptView;
    }


}