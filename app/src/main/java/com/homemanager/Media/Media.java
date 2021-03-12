package com.homemanager.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.homemanager.Task.Spotify.SpotifyGetObject;
import com.homemanager.Task.Spotify.SpotifyGetObjectTask;
import com.homemanager.Task.Spotify.SpotifyInterface;
import com.homemanager.Task.Spotify.SpotifyPlayTask;
import com.homemanager.TaskConnector;
import com.example.homemanager.R;
import com.homemanager.Task.Action.CecTask;
import com.homemanager.Task.Action.PlayTask;
import com.homemanager.Task.Action.SoundVolumeTask;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Action.StopMediaTask;
import com.homemanager.Task.Media.MediaObject;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Media extends Activity implements SpotifyInterface {

    private View promptView;
    private Media media;
    private TaskConnector taskConnector;
    private StatusMessage statusMessages;
    private String currentSporifyDirectory;
    private List<String> sporifyDirectoriesList;
    private Timer timer;

    public Media(TaskConnector tasksConnector, StatusMessage statusMessages){
        this.taskConnector = tasksConnector;
        this.statusMessages = statusMessages;
        this.currentSporifyDirectory = "";
        sporifyDirectoriesList = new ArrayList<String>();

        this.media = this;
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

        imgBtnAdd = (ImageButton) promptView.findViewById(R.id.buttonUp);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sporifyDirectoriesList.size() >=2){
                    int lastIndex = sporifyDirectoriesList.size()-1;
                    //remove current directory
                    sporifyDirectoriesList.remove(lastIndex);
                    lastIndex--;
                    //get upper directory
                    String directory = sporifyDirectoriesList.get(lastIndex);
                    sporifyDirectoriesList.remove(lastIndex);
                    //go to upper directory
                    taskConnector.putNewTask(new SpotifyGetObjectTask(media, directory));
                    statusMessages.displayHint(R.string.HintWaitForData);
                    ((ProgressBar)(promptView.findViewById(R.id.loadingBar))).setVisibility(View.VISIBLE);
                }

            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ProgressBar bar = (ProgressBar) promptView.findViewById(R.id.loadingBar);
                bar.setProgress(bar.getProgress()+1);

            }
        }, 100, 10);




        imgBtnAdd = (ImageButton) promptView.findViewById(R.id.buttonPlaySpotify);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentSporifyDirectory.length() != 0 ){
                    taskConnector.putNewTask(new SpotifyPlayTask(statusMessages, currentSporifyDirectory));
                    statusMessages.displayHint(R.string.HintWaitForData);
                }

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

        layout = (LinearLayout) promptView.findViewById(R.id.spotifyLayout);
        layout.setVisibility(View.GONE);


        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabMediaLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.VISIBLE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
                    tvLayout.setVisibility(View.GONE);

                    LinearLayout spotifyLayout = (LinearLayout) promptView.findViewById(R.id.spotifyLayout);
                    spotifyLayout.setVisibility(View.GONE);

                }
                else if (tabItems.getTabAt(1) == tab) {
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.GONE);

                    LinearLayout spotifyLayout = (LinearLayout) promptView.findViewById(R.id.spotifyLayout);
                    spotifyLayout.setVisibility(View.GONE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
                    tvLayout.setVisibility(View.VISIBLE);
                }
                else {
                    if (currentSporifyDirectory.length() ==0) {
                        taskConnector.putNewTask(new SpotifyGetObjectTask(media));
                        statusMessages.displayHint(R.string.HintWaitForData);
                        ((ProgressBar)(promptView.findViewById(R.id.loadingBar))).setVisibility(View.VISIBLE);
                    }
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.GONE);

                    LinearLayout spotifyLayout = (LinearLayout) promptView.findViewById(R.id.spotifyLayout);
                    spotifyLayout.setVisibility(View.VISIBLE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.tvLayout);
                    tvLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return promptView;
    }

    @Override
    public synchronized void spotifyObjectRecived(SpotifyGetObject spotifyCurrentObject){
        final JSONArray array = spotifyCurrentObject.getCurrentSpotifyContent();

        this.currentSporifyDirectory = spotifyCurrentObject.getCurrentDirectory();
        sporifyDirectoriesList.add(this.currentSporifyDirectory);
        //prepare list of items on the screen

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createSpotifyContent(array);
            }
        });
     }

    @Override
    public void spotifyObjectRecivedError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)(promptView.findViewById(R.id.loadingBar))).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void createSpotifyContent(final JSONArray items) {
        LinearLayout layout = (LinearLayout) promptView.findViewById(R.id.spotifyDataLayout);
        layout.removeAllViews();

        ((ProgressBar)(promptView.findViewById(R.id.loadingBar))).setVisibility(View.INVISIBLE);

        for (int idx = 0; idx < items.length(); idx++) {
            try {
                JSONObject item = items.getJSONObject(idx);

                final Button button = new Button(promptView.getContext());
                button.setText(item.getString("label"));
                final String file = item.getString("file");

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        taskConnector.putNewTask(new SpotifyGetObjectTask(media, file));
                        statusMessages.displayHint(R.string.HintWaitForData);
                        ((ProgressBar)(promptView.findViewById(R.id.loadingBar))).setVisibility(View.VISIBLE);
                    }
                });

                layout.addView(button, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            } catch (Exception e) {

            }
        }
    }
}

