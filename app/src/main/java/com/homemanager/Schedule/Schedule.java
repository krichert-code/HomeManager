package com.homemanager.Schedule;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.homemanager.R;
import com.homemanager.Task.Schedule.ScheduleObject;

import java.util.ArrayList;
import java.util.List;



public class Schedule {
    private View promptView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View createScreen(View view, final AlertDialog dialog, final List<ScheduleObject> elements){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.schedule, null);
        TableLayout tl;
        List<ScheduleRow> rows = new ArrayList<ScheduleRow>();
        int direction = elements.get(0).getDirectionId();
        int index = 0;

        //prepare rows
        for (ScheduleObject element : elements) {
            if (direction == element.getDirectionId()) {
                rows.add(new ScheduleRow(element.getArrival() + " (" + element.getTravelTime() + "min)", ""));
            }
            else{
                if (index < rows.size()) {
                    ScheduleRow row = rows.get(index);
                    row.setElement_2(element.getArrival()+ " (" + element.getTravelTime() + "min)");
                    index++;
                }
                else{
                    rows.add(new ScheduleRow("", element.getArrival()+ " (" + element.getTravelTime() + "min)"));
                }
            }
        }

        Button btnAdd = (Button) promptView.findViewById(R.id.scheduleClose);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btn has been clicked
                dialog.dismiss();
            }
        });


        tl = (TableLayout) promptView.findViewById(R.id.scheduleLayout);

        //tl.removeAllViews();

        //display rows
        for (ScheduleRow element : rows) {
            TableRow tr1 = new TableRow(view.getContext());

            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView textview = new TextView(view.getContext());
            textview.setText(element.getElement_1());
            textview.setTextColor(Color.BLACK);
            //textview.setElegantTextHeight(true);
            textview.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            textview.setHorizontallyScrolling(false);

            textview.setSingleLine(false);
            textview.setMaxLines(3);
            textview.setMinLines(2);

            TextView textview1 = new TextView(view.getContext());
            textview1.setText(element.getElement_2());
            textview1.setTextColor(Color.BLACK);
            //textview1.setElegantTextHeight(true);
            textview1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            textview1.setHorizontallyScrolling(false);

            textview1.setSingleLine(false);
            textview1.setMaxLines(3);
            textview1.setMinLines(2);

            tr1.addView(textview);
            tr1.addView(textview1);

            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)textview.getLayoutParams();
            p.rightMargin=10;
            p.leftMargin = 20;
            p.gravity= Gravity.CENTER;

            tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT , TableRow.LayoutParams.WRAP_CONTENT));
        }
        return promptView;
    }
}

