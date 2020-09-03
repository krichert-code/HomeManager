package com.example.homemanager.Heater;

import android.app.AlertDialog;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.homemanager.TaskConnector;
import com.example.homemanager.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.example.homemanager.Task.Action.StatusMessage;
import com.example.homemanager.Task.Heater.HeaterMessage;
import com.example.homemanager.Task.Heater.HeaterObject;
import com.example.homemanager.Task.Heater.HeaterSettingsTask;
import com.example.homemanager.Task.Heater.TempData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.tabs.TabLayout;

public class Heater implements HeaterMessage {
    private View promptView;
    private int pieChartIdDraw;
    private HeaterObject heaterObject;
    private TaskConnector tasks;
    private StatusMessage statusMessages;
    private final Heater heaterClass;
    private boolean firstUpdate = true;

    public Heater(TaskConnector taskConnector, StatusMessage statusMessages){
        this.tasks = taskConnector;
        this.statusMessages = statusMessages;
        this.heaterClass = this;
    }

    private void createLineChart(final View view){
        LineChart chart = (LineChart) promptView.findViewById(R.id.chartLine);

        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        final ArrayList<String> xLabel = new ArrayList<>();
        int index = 0;

        for(TempData temp :heaterObject.getTemperatureData()){
            String date = temp.getDate();
            date = date.substring(date.indexOf("/") + 1);
            //date = date.substring(0, date.indexOf("/"));
            xLabel.add(date);
            values1.add(new Entry(index, (float)temp.getTempInside()));
            values2.add(new Entry(index, (float)temp.getTempOutside()));
            index++;
        }

        LineDataSet set1 = new LineDataSet(values1,view.getResources().getString(R.string.insideTemp));
        set1.setLineWidth(1f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setValueTextSize(9f);
        set1.setCircleRadius(0);
        set1.setCircleColor(Color.RED);
        set1.setValueTextSize(9f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);


        LineDataSet set2 = new LineDataSet(values2,view.getResources().getString(R.string.outsideTemp));
        set2.setLineWidth(1f);
        set2.enableDashedHighlightLine(10f, 5f, 0f);
        set2.setColor(Color.BLUE);
        set2.setValueTextSize(9f);
        set2.setCircleColor(Color.BLUE);
        set2.setValueTextSize(9f);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.getDescription().setEnabled(false);
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setEnabled(false);
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });
//        xAxis.setEnabled(false);

        chart.setAutoScaleMinMaxEnabled(true);
        chart.animateXY(1500, 1500);
        chart.invalidate();
    }

    private void createPieChart(final View view, int chartId){
        PieChart chart = (PieChart) promptView.findViewById(R.id.chart);

        ArrayList<PieEntry> values1 = new ArrayList<>();

        if (chartId==0) {
            chart.setCenterText(view.getResources().getString(R.string.pieChartLabel1));
            values1.add(new PieEntry(heaterObject.getModeChartOff(), view.getResources().getString(R.string.OffMode)));
            values1.add(new PieEntry(heaterObject.getModeChartDay(), view.getResources().getString(R.string.DayMode)));
            values1.add(new PieEntry(heaterObject.getModeChartNight(), view.getResources().getString(R.string.NightMode)));
        }
        else{
            chart.setCenterText(view.getResources().getString(R.string.pieChartLabel2));
            values1.add(new PieEntry(heaterObject.getModeChartOffPerDay(), view.getResources().getString(R.string.OffMode)));
            values1.add(new PieEntry(heaterObject.getModeChartDayPerDay(), view.getResources().getString(R.string.DayMode)));
            values1.add(new PieEntry(heaterObject.getModeChartNightPerDay(), view.getResources().getString(R.string.NightMode)));
        }

        PieDataSet set1 = new PieDataSet(values1,"");
        set1.setColors(Color.LTGRAY, Color.RED, Color.BLUE);
        set1.setSliceSpace(5f);
        set1.setIconsOffset(new MPPointF(0, 40));
        set1.setSelectionShift(5f);

        PieData data = new PieData(set1);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(40f);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(15f);

        data.setValueFormatter(new PercentFormatter(new DecimalFormat()));
        chart.setUsePercentValues(true);

        chart.setData(data);
        //chart.setDrawHoleEnabled(false);
        chart.setDrawCenterText(true);

        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);

        chart.animateXY(1500, 1500);
        chart.invalidate();
    }

    private void updateSettingsWindow(){

        if(firstUpdate) {
            EditText editText = (EditText) promptView.findViewById(R.id.dayTempText);
            editText.setText(Double.toString(heaterObject.getTempDay()));
            editText = (EditText) promptView.findViewById(R.id.nightTempText);
            editText.setText(Double.toString(heaterObject.getTempNight()));
            firstUpdate = false;
        }

        int v = heaterObject.getTempModeDay(
                ((Spinner) promptView.findViewById(R.id.spinnerHeater)).getSelectedItemPosition());
        int idx = 0;

        for (int bitIdx = 0; bitIdx < 24; bitIdx++) {
            String name = "h" + idx;
            try {
                CheckBox cb = (CheckBox) promptView.findViewById(R.id.class.getField(name).getInt(null));
                if ((v & 1) == 1) cb.setChecked(true);
                else cb.setChecked(false);
            } catch (Exception e) { }
            idx++;
            v = v >> 1;
        }
    }

    private void updateTempModeSettings(int hourIdx, boolean state){
        Spinner spinner = (Spinner) promptView.findViewById(R.id.spinnerHeater);
        int v = heaterObject.getTempModeDay(spinner.getSelectedItemPosition());
        if (state){
            v = (v | (1<<hourIdx));
        }
        else
        {
            v = v & (~ (1 << hourIdx));
        }
        heaterObject.setTempModeDay(spinner.getSelectedItemPosition(), v);
    }

    public View createScreen(final View view, final AlertDialog dialog, final HeaterObject heaterObject){
        pieChartIdDraw = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.heater, null);

        this.heaterObject = heaterObject;
        createPieChart(view, pieChartIdDraw);
        createLineChart(view);

        Button btnAdd = (Button) promptView.findViewById(R.id.closeHeater);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.saveHeaterSettings);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                heaterObject.setTempDay(Double.parseDouble(
                        ((TextView) promptView.findViewById(R.id.dayTempText)).getText().toString() ));
                heaterObject.setTempNight(Double.parseDouble(
                        ((TextView) promptView.findViewById(R.id.nightTempText)).getText().toString() ));
                tasks.putNewTask(new HeaterSettingsTask(heaterObject, heaterClass));
            }
        });

        for (int i=0; i<24; i++) {
            String name = "h" + i;
            try{
                CheckBox cBox = (CheckBox) promptView.findViewById(R.id.class.getField(name).getInt(null));
                    final int finalI = i;
                    cBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        updateTempModeSettings(finalI, b);
                    }
                });
            }
            catch(Exception e){}
        }

        Spinner daysOfWeek = (Spinner) promptView.findViewById(R.id.spinnerHeater);
        daysOfWeek.setOnItemSelectedListener( new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSettingsWindow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    PieChart chart = (PieChart) promptView.findViewById(R.id.chart);
                    //ViewGroup.LayoutParams lp = chart.getLayoutParams();
                    //lp.height = 1000;
                    //chart.setLayoutParams(lp);
                    createPieChart(view, pieChartIdDraw);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(1500, 1500);

                    LineChart chartLine = (LineChart) promptView.findViewById(R.id.chartLine);
                    chartLine.setVisibility(View.GONE);

                    TableLayout heaterSettings = (TableLayout) promptView.findViewById(R.id.heaterSettings);
                    heaterSettings.setVisibility(View.GONE);
                }
                else if (tabItems.getTabAt(1) == tab){
                    PieChart chart = (PieChart) promptView.findViewById(R.id.chart);
                    chart.setVisibility(View.GONE);

                    TableLayout heaterSettings = (TableLayout) promptView.findViewById(R.id.heaterSettings);
                    heaterSettings.setVisibility(View.GONE);

                    LineChart chartLine = (LineChart) promptView.findViewById(R.id.chartLine);
                    chartLine.setVisibility(View.VISIBLE);
                    chartLine.animateXY(1500, 1500);
                }
                else {
                    PieChart chart = (PieChart) promptView.findViewById(R.id.chart);
                    chart.setVisibility(View.GONE);

                    LineChart chartLine = (LineChart) promptView.findViewById(R.id.chartLine);
                    chartLine.setVisibility(View.GONE);

                    TableLayout heaterSettings = (TableLayout) promptView.findViewById(R.id.heaterSettings);
                    heaterSettings.setVisibility(View.VISIBLE);
                    updateSettingsWindow();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                pieChartIdDraw = 0;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    PieChart chart = (PieChart) promptView.findViewById(R.id.chart);
                    //ViewGroup.LayoutParams lp = chart.getLayoutParams();
                    //lp.height = 1000;
                    //chart.setLayoutParams(lp);
                    pieChartIdDraw = (pieChartIdDraw +1) %2;
                    createPieChart(view, pieChartIdDraw);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(1500, 1500);

                    LineChart chartLine = (LineChart) promptView.findViewById(R.id.chartLine);
                    chartLine.setVisibility(View.GONE);

                    TableLayout heaterSettings = (TableLayout) promptView.findViewById(R.id.heaterSettings);
                    heaterSettings.setVisibility(View.GONE);
                }
            }
        });


        return promptView;
    }

    @Override
    public void displayHeaterData(HeaterObject heaterObject) {
        statusMessages.displayHint(R.string.HintSettingsSaveDone);
    }
}


