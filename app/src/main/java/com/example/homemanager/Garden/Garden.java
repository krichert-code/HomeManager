package com.example.homemanager.Garden;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

import com.example.homemanager.GuiConnector;
import com.example.homemanager.R;
import com.example.homemanager.Schedule.Schedule;
import com.example.homemanager.Task.Action.MainGateTask;
import com.example.homemanager.Task.Action.StatusMessage;
import com.example.homemanager.Task.Action.SprinklerTask;
import com.example.homemanager.Task.Garden.GardenMessage;
import com.example.homemanager.Task.Garden.GardenObject;
import com.example.homemanager.Task.Garden.GardenSettingsTask;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Garden implements GardenMessage {
    private GuiConnector tasks;
    private StatusMessage statusMessages;
    private View promptView;
    private GardenObject gardenObject;
    private final Garden gardenClass;

    public Garden(GuiConnector taskConnector, StatusMessage statusMessages){
        this.tasks = taskConnector;
        this.statusMessages = statusMessages;
        this.gardenClass = this;
    }

    private void updateSettingsWindow(){
        EditText editText = (EditText) promptView.findViewById(R.id.startTimeText);
        editText.setText(gardenObject.getStartTime());
        editText = (EditText) promptView.findViewById(R.id.durationText);
        editText.setText(Integer.toString(gardenObject.getDuration()));

        String[] daysArray = promptView.getResources().getStringArray(R.array.days_array);
        for(int i=0; i<daysArray.length; i++) {
            String name = "switch" + i;
            try {
                Switch swch = (Switch) promptView.findViewById(R.id.class.getField(name).getInt(null));
                swch.setText(daysArray[i]);
                swch.setChecked(gardenObject.isEnablePerDay(i));
            }
            catch(Exception e){
            }
        }

        ToggleButton stateButton = (ToggleButton) promptView.findViewById(R.id.globalStateButton);
        if (gardenObject.isGlobalEnable()) {
            stateButton.setChecked(true);
        }
        else {
            stateButton.setChecked(false);
        }
    }

    public View createScreen(final View view, final AlertDialog dialog, final GardenObject gardenObject){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.garden, null);
        this.gardenObject = gardenObject;

        updateSettingsWindow();

        Button btnAdd = (Button) promptView.findViewById(R.id.sprinkler1);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd has been clicked
                tasks.putNewTask(new SprinklerTask("/SprinklerOn/1", statusMessages));
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.sprinkler2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd has been clicked
                tasks.putNewTask(new SprinklerTask("/SprinklerOn/2", statusMessages));
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.sprinkler3);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd has been clicked
                tasks.putNewTask(new SprinklerTask("/SprinklerOn/3", statusMessages));
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.autoWaterButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd has been clicked
                tasks.putNewTask(new SprinklerTask("/SprinklerForceAuto", statusMessages));
                Toast toast = Toast.makeText(v.getContext(), R.string.HintAutoWaterScheduled, Toast.LENGTH_LONG);
                toast.show();
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.stopBtn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd has been clicked
                tasks.putNewTask(new SprinklerTask("/SprinklerOff", statusMessages));
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.saveGardenSettings);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                gardenObject.setGlobalEnable(
                        ((ToggleButton)promptView.findViewById(R.id.globalStateButton)).isChecked());

                gardenObject.setDuration(
                        Integer.parseInt( ((TextView)promptView.findViewById(R.id.durationText)).getText().toString()));

                gardenObject.setStartTime(
                         ((TextView)promptView.findViewById(R.id.startTimeText)).getText().toString());

                gardenObject.setEnablePerDay(0,
                        ((Switch)promptView.findViewById(R.id.switch0)).isChecked());
                gardenObject.setEnablePerDay(1,
                        ((Switch)promptView.findViewById(R.id.switch1)).isChecked());
                gardenObject.setEnablePerDay(2,
                        ((Switch)promptView.findViewById(R.id.switch2)).isChecked());
                gardenObject.setEnablePerDay(3,
                        ((Switch)promptView.findViewById(R.id.switch3)).isChecked());
                gardenObject.setEnablePerDay(4,
                        ((Switch)promptView.findViewById(R.id.switch4)).isChecked());
                gardenObject.setEnablePerDay(5,
                        ((Switch)promptView.findViewById(R.id.switch5)).isChecked());
                gardenObject.setEnablePerDay(6,
                        ((Switch)promptView.findViewById(R.id.switch6)).isChecked());

                tasks.putNewTask(new GardenSettingsTask(gardenObject, gardenClass));
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.closeBtn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd has been clicked
                dialog.dismiss();
            }
        });

        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    LinearLayout ctrlLayout = (LinearLayout) promptView.findViewById(R.id.ctrlGarden);
                    ctrlLayout.setVisibility(View.VISIBLE);

                    TableLayout settingLayout = (TableLayout) promptView.findViewById(R.id.settingGarden);
                    settingLayout.setVisibility(View.GONE);
                }
                else {
                    LinearLayout ctrlLayout = (LinearLayout) promptView.findViewById(R.id.ctrlGarden);
                    ctrlLayout.setVisibility(View.GONE);

                    TableLayout settingLayout = (TableLayout) promptView.findViewById(R.id.settingGarden);
                    settingLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        EditText editText = (EditText) promptView.findViewById(R.id.startTimeText);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                gardenObject.setStartTime(textView.getText().toString());
                return false;
            }
        });

        editText = (EditText) promptView.findViewById(R.id.durationText);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                gardenObject.setDuration(Integer.parseInt(textView.getText().toString()));
                return false;
            }
        });


        return promptView;
    }

    @Override
    public void displayGardenData(GardenObject gardenObject) {
        statusMessages.displayHint(R.string.HintSettingsSaveDone);
    }
}
