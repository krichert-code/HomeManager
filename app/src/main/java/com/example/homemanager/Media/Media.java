package com.example.homemanager.Media;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.homemanager.TaskConnector;
import com.example.homemanager.R;
import com.example.homemanager.Task.Action.CecTask;
import com.example.homemanager.Task.Action.PlayTask;
import com.example.homemanager.Task.Action.SoundVolumeTask;
import com.example.homemanager.Task.Action.StatusMessage;
import com.example.homemanager.Task.Action.StopMediaTask;
import com.example.homemanager.Task.Media.MediaObject;
import com.google.android.material.tabs.TabLayout;

public class Media {

    private View promptView;
    private TaskConnector taskConnector;
    private StatusMessage statusMessages;

    public Media(TaskConnector tasksConnector, StatusMessage statusMessages){
        this.taskConnector = tasksConnector;
        this.statusMessages = statusMessages;
    }

    public View createScreen(View view, final AlertDialog dialog, final MediaObject mediaObject){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.media, null);

        SeekBar volumeBar = (SeekBar) promptView.findViewById(R.id.volumeBar);
        volumeBar.setProgress(mediaObject.getVolume());
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                taskConnector.putNewTask(new SoundVolumeTask(SoundVolumeTask.VOLUME_SET,i,
                        statusMessages));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button btnAdd = (Button) promptView.findViewById(R.id.button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.stopMediaButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((TextView) promptView.findViewById(R.id.mediaText)).setText("");
                taskConnector.putNewTask(new StopMediaTask(statusMessages));
            }
        });


        ImageButton imgBtnAdd = (ImageButton) promptView.findViewById(R.id.powerMediaButton);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                taskConnector.putNewTask(new CecTask());
            }
        });

        LinearLayout layout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
        layout.removeAllViews();
        int channelIdx = 0;
        for (final int channelId : mediaObject.getRadioChannelIds()) {

            final Button button = new Button(promptView.getContext());
            button.setText(mediaObject.getRadioChannelnames().get(channelIdx));

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((TextView) promptView.findViewById(R.id.mediaText)).setText(button.getText());
                    taskConnector.putNewTask(new PlayTask(channelId,
                            statusMessages));
                }
            });

            layout.addView(button, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT , TableRow.LayoutParams.WRAP_CONTENT));
            channelIdx++;
        }

        layout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
        layout.removeAllViews();
        channelIdx = 0;
        for (final int channelId : mediaObject.getTvChannelIds()) {

            final Button button = new Button(promptView.getContext());
            button.setText(mediaObject.getTvChannelnames().get(channelIdx));

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((TextView) promptView.findViewById(R.id.mediaText)).setText(button.getText());
                    taskConnector.putNewTask(new PlayTask(channelId,
                            statusMessages));
                }
            });

            layout.addView(button, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT , TableRow.LayoutParams.WRAP_CONTENT));
            channelIdx++;
        }

        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabMediaLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.VISIBLE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
                    tvLayout.setVisibility(View.GONE);
                }
                else {
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.GONE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
                    tvLayout.setVisibility(View.VISIBLE);
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


