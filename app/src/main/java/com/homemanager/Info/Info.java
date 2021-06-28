package com.homemanager.Info;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Heater.TempData;
import com.homemanager.Task.Info.AlarmSettingsTask;
import com.homemanager.TaskConnector;
import com.example.homemanager.R;
import com.homemanager.Task.Info.InfoMessage;
import com.homemanager.Task.Info.InfoObject;
import com.google.android.material.tabs.TabLayout;

import android.app.Activity;
import android.content.Context;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Info implements InfoMessage {

    private View promptView;
    private InfoObject infoObj;
    private TaskConnector tasks;
    private StatusMessage statusMessages;
    private Info infoClass;

    public Info(TaskConnector taskConnector, StatusMessage statusMessages){
        this.tasks = taskConnector;
        this.statusMessages = statusMessages;
        this.infoClass = this;
    }

    private void createLineChart(final View view){
        BarChart chart = (BarChart) promptView.findViewById(R.id.batteryChart);

        ArrayList<BarEntry> values1 = new ArrayList<>();
        final ArrayList<String> xLabel = new ArrayList<>();
        int index = 0;

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                statusMessages.displayCustomHint(Double.toString(e.getY())+" KWh" );
            }

            @Override
            public void onNothingSelected()
            {

            }
        });
        for (int value : infoObj.getEnergyValues()){
            values1.add(new BarEntry(index + 1, (float)value));
            index++;
        }

        BarDataSet set1 = new BarDataSet(values1,view.getResources().getString(R.string.insideTemp));
        set1.setColor(Color.RED);
        set1.setValueTextSize(9f);
        set1.setValueTextSize(9f);
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        chart.setData(data);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.getDescription().setEnabled(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.animateXY(1500, 1500);
        chart.invalidate();
    }

    private void createButtonHandler(final int button, final int text, final boolean isDirectionUp){
        Button btnAdd = (Button) promptView.findViewById(button);
        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event){
                final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                try {
                    String time = ((TextView) promptView.findViewById(text)).getText().toString();
                    Date d = dateFormat.parse(time);
                    if (!isDirectionUp)
                        d.setTime(d.getTime() - ONE_MINUTE_IN_MILLIS);
                    else
                        d.setTime(d.getTime() + ONE_MINUTE_IN_MILLIS);
                    ((TextView) promptView.findViewById(text)).setText(dateFormat.format(d));
                }
                catch(Exception e){
                }
                return true;
            }
        });
    }

    private void createButtonDurationHandler(final int button, final int text, final boolean isDirectionUp){
        Button btnAdd = (Button) promptView.findViewById(button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    int value = Integer.parseInt(((TextView) promptView.findViewById(text)).getText().toString());
                    if (!isDirectionUp && value > 1)
                        value = value - 1;
                    else if (isDirectionUp)
                        value = value + 1;
                    ((TextView) promptView.findViewById(text)).setText(Integer.toString(value));
                }
                catch(Exception e){
                }
            }
        });
    }

    public View createScreen(View view, final AlertDialog dialog, InfoObject data){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.info, null);
        infoObj = data;
        int index = 0;

        createLineChart(view);
        Button btnAdd = (Button) promptView.findViewById(R.id.button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });


        btnAdd = (Button) promptView.findViewById(R.id.saveAlarmSettings);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int state;

                infoObj.setAlarmStop(
                        ((TextView)promptView.findViewById(R.id.editStopAlarmTime)).getText().toString() );

                infoObj.setAlarmTime(
                        ((TextView)promptView.findViewById(R.id.editStartAlarmTime)).getText().toString() );

                infoObj.setAlarmChannel(
                        infoObj.getChannelsID().get(((Spinner)promptView.findViewById(R.id.spinner)).getSelectedItemPosition()));

                if ( ((RadioButton)promptView.findViewById(R.id.radioAlarmMode1)).isChecked() )
                    state = infoObj.ALARM_NEVER;
                else if ( ((RadioButton)promptView.findViewById(R.id.radioAlarmMode2)).isChecked() )
                    state = infoObj.ALARM_WEEK_DAYS;
                else
                    state = infoObj.ALARM_ALWAYES;

                infoObj.setAlarmStateValue(state);

                infoObj.setAlarmVolume(Integer.toString( ((SeekBar)promptView.findViewById(R.id.volumeBar)).getProgress()));

                tasks.putNewTask(new AlarmSettingsTask(infoObj, infoClass));
            }
        });

        createButtonHandler(R.id.leftTime, R.id.editStartAlarmTime, false);
        createButtonDurationHandler(R.id.leftDuration,R.id.editStopAlarmTime, false);
        createButtonHandler(R.id.rightTime, R.id.editStartAlarmTime,true);
        createButtonDurationHandler(R.id.rightDuration, R.id.editStopAlarmTime, true);

        ((SeekBar)promptView.findViewById(R.id.volumeBar)).setProgress(Integer.parseInt(infoObj.getAlarmVolume()));

        TextView textView = (TextView)promptView.findViewById(R.id.infoText1);
        String stringValue;
        if (data.getAlarmStateValue() == data.ALARM_ALWAYES)
            textView.setText(promptView.getResources().getString(R.string.InfoAlarmMode2) + " " + data.getAlarmTime());
        if (data.getAlarmStateValue() == data.ALARM_WEEK_DAYS)
            textView.setText(promptView.getResources().getString(R.string.InfoAlarmMode1) + " " + data.getAlarmTime());
        if (data.getAlarmStateValue() == data.ALARM_NEVER)
            textView.setText(promptView.getResources().getString(R.string.InfoAlarmMode0) );

        textView = (TextView)promptView.findViewById(R.id.infoText2);
        textView.setText(data.getTodayRainStats());

        textView = (TextView)promptView.findViewById(R.id.versionInfoText);
        textView.setText(promptView.getResources().getString(R.string.InfoVersion));

        textView = (TextView)promptView.findViewById(R.id.infoText3);
        textView.setText(promptView.getResources().getString(R.string.InfoHeater) +" " + data.getTodayHeaterStats());

        textView = (TextView)promptView.findViewById(R.id.batteryInfoText);
        textView.setText(promptView.getResources().getString(R.string.InfoEnergyTotal) +" " + data.getEnergyTotal() + "KWh");

        Spinner spinner = (Spinner) promptView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(promptView.getContext(),
                android.R.layout.simple_spinner_item, data.getChannelsName());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int channel : data.getChannelsID()){
            if (channel == data.getAlarmChannel()){
                spinner.setSelection(index);
                break;
            }
            index++;
        }

        textView = (TextView)promptView.findViewById(R.id.editStartAlarmTime);
        textView.setText(data.getAlarmTime());
        textView = (TextView)promptView.findViewById(R.id.editStopAlarmTime);
        textView.setText(data.getAlarmStop());


        if(data.getAlarmStateValue() == data.ALARM_NEVER){
            RadioButton radioMode = (RadioButton)promptView.findViewById(R.id.radioAlarmMode1);
            radioMode.setChecked(true);
        }
        else if (data.getAlarmStateValue() == data.ALARM_WEEK_DAYS){
            RadioButton radioMode = (RadioButton)promptView.findViewById(R.id.radioAlarmMode2);
            radioMode.setChecked(true);
        }
        else{
            RadioButton radioMode = (RadioButton)promptView.findViewById(R.id.radioAlarmMode3);
            radioMode.setChecked(true);
        }

        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    TableLayout info = (TableLayout) promptView.findViewById(R.id.infoPanel);
                    info.setVisibility(View.VISIBLE);

                    info = (TableLayout) promptView.findViewById(R.id.settingInfoPanel);
                    info.setVisibility(View.GONE);
                }
                else if (tabItems.getTabAt(1) == tab){
                    TableLayout info = (TableLayout) promptView.findViewById(R.id.settingInfoPanel);
                    info.setVisibility(View.VISIBLE);

                    info = (TableLayout) promptView.findViewById(R.id.infoPanel);
                    info.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return promptView;
    }

    @Override
    public void displayInfo(InfoObject info) {
        statusMessages.displayHint(R.string.HintSettingsSaveDone);
    }
}


