package com.homemanager.CtrlDevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
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

import androidx.annotation.DrawableRes;

import com.example.homemanager.R;
import com.homemanager.Task.Action.LightTask;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Alarm.AlarmObject;
import com.homemanager.Task.Alarm.Room;
import com.homemanager.Task.CtrlDevice.CtrlDeviceMessage;
import com.homemanager.Task.CtrlDevice.CtrlDeviceObject;
import com.homemanager.Task.CtrlDevice.CtrlRoom;
import com.homemanager.TaskConnector;

public class CtrlDevice  implements CtrlDeviceMessage {
    private View promptView;
    private TaskConnector tasks;
    private StatusMessage statusMessages;
    private final CtrlDevice ctrlDeviceClass;

    public CtrlDevice(TaskConnector taskConnector, StatusMessage statusMessage){
        this.tasks = taskConnector;
        this.statusMessages = statusMessages;
        this.ctrlDeviceClass = this;
    }

    private synchronized void createInfoPanel(CtrlDeviceObject ctrlRooms){
        TableLayout infoPanel = (TableLayout) promptView.findViewById(R.id.ctrlRoomInfoPanel);

        infoPanel.removeAllViews();

        for (final CtrlRoom ctrlRoom:ctrlRooms) {
            TableRow row = new TableRow(promptView.getContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            LinearLayout wndLayout = new LinearLayout((promptView.getContext()));
            wndLayout.setOrientation(LinearLayout.HORIZONTAL);
            wndLayout.setBackgroundResource(R.drawable.round_corner);

            LinearLayout dataLayout = new LinearLayout((promptView.getContext()));
            dataLayout.setOrientation(LinearLayout.VERTICAL);

            TextView ctrlRoomName = new TextView(promptView.getContext());
            ctrlRoomName.setText(ctrlRoom.name);
            ctrlRoomName.setTypeface(null, Typeface.BOLD);

            LinearLayout tempLayout = new LinearLayout((promptView.getContext()));
            tempLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageView imageHeater = new ImageView(promptView.getContext());
            imageHeater.setImageDrawable(promptView.getResources().getDrawable(R.drawable.piec));
            TextView textTemp = new TextView(promptView.getContext());
            if (ctrlRoom.tempSensorExist) {
                textTemp.setText(ctrlRoom.tempSensorValue + " \u2103");
            }
            tempLayout.addView(imageHeater);
            tempLayout.addView(textTemp);

            dataLayout.addView(ctrlRoomName);
            dataLayout.addView(tempLayout);


            final ImageButton light = new ImageButton((promptView.getContext()));
            if (ctrlRoom.lightExist) {
                light.setImageDrawable(promptView.getResources().getDrawable(R.drawable.lamp_off));
                TypedValue outValue = new TypedValue();

                light.setBackgroundResource(outValue.resourceId);
                light.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (ctrlRoom.isLightOn) {
                    //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                    light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
                }

                light.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (tasks.putNewTask(new LightTask(ctrlRoom.lightIp)) == 0) {
                            ctrlRoom.isLightOn = (!ctrlRoom.isLightOn);
//                            if (querySent == true) {
//                                ignoreResponse = true;
//                            }

                            if (ctrlRoom.isLightOn) {
                                //light.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP));
                                light.setColorFilter(new LightingColorFilter(0x77777777, 0x00777733));
                            } else {
                                light.setColorFilter(null);
                            }
                        }
                    }
                });
            }

            wndLayout.addView(light);
            wndLayout.addView(dataLayout);

            row.addView(wndLayout);
            row.setPadding(0,20,0,20);

            LinearLayout.LayoutParams p  = (LinearLayout.LayoutParams) imageHeater.getLayoutParams();
            p.rightMargin=10;
            p.leftMargin = 0;
            p.gravity= Gravity.LEFT ;

            p = (LinearLayout.LayoutParams)ctrlRoomName.getLayoutParams();
            p.rightMargin=10;
            p.leftMargin = 0;
            p.gravity= Gravity.CENTER | Gravity.LEFT;
            p.topMargin = 40;
            p.bottomMargin = 20;

            imageHeater.getLayoutParams().height = 40;
            imageHeater.getLayoutParams().width = 130;

            Configuration configuration = promptView.getResources().getConfiguration();

            if (configuration.screenHeightDp < 800) {
                light.getLayoutParams().height = 200;
                light.getLayoutParams().width = 200;
            }
            else {
                light.getLayoutParams().height = 120;
                light.getLayoutParams().width = 120;
            }


            ((LinearLayout.LayoutParams) (light.getLayoutParams())).rightMargin = 15;

            infoPanel.addView(row, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
        }
    }

    public View createScreen(final View view, final AlertDialog dialog, final CtrlDeviceObject ctrlRoomsObject){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.ctrlroom, null);

        createInfoPanel(ctrlRoomsObject);

        Button btnAdd = (Button) promptView.findViewById(R.id.ctrlRoomCloseButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //timer.cancel();
                dialog.dismiss();
            }
        });

        return promptView;
    }

    @Override
    public void displayCtrlDevice(CtrlDeviceObject ctrlDeviceObject) {

    }
}
