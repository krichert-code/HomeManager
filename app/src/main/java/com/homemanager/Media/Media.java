package com.homemanager.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.homemanager.Task.Action.VideoShareTask;
import com.homemanager.Task.Youtube.YoutubeInterface;
import com.homemanager.Task.Youtube.YoutubeObject;
import com.homemanager.Task.Youtube.YoutubeSearchTask;
import com.homemanager.TaskConnector;
import com.example.homemanager.R;

import com.homemanager.Task.Action.PlayTask;
import com.homemanager.Task.Action.SoundVolumeTask;
import com.homemanager.Task.Status.StatusMessage;
import com.homemanager.Task.Action.StopMediaTask;
import com.homemanager.Task.Media.MediaObject;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;



public class Media extends Activity implements YoutubeInterface {

    private View promptView;
    private Media media;
    private TaskConnector taskConnector;
    private StatusMessage statusMessages;
    private String currentSporifyDirectory;
    private List<String> sporifyDirectoriesList;
    private ArrayList<YoutubeObject> ytObjects = null;
    private Timer timer;
    private boolean sourceIsLocal;

    public Media(TaskConnector tasksConnector, StatusMessage statusMessages){
        this.taskConnector = tasksConnector;
        this.statusMessages = statusMessages;
        this.currentSporifyDirectory = "";
        sporifyDirectoriesList = new ArrayList<String>();
        this.sourceIsLocal = false;
        this.media = this;
    }

    public View createScreen(View view, final AlertDialog dialog, final MediaObject mediaObject){
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.media, null);

        DisplayMetrics displayMetrics = promptView.getResources().getDisplayMetrics();
        FrameLayout layout1 = (FrameLayout) promptView.findViewById(R.id.frameMediaLayout);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) layout1.getLayoutParams();
        params.height = (int)(displayMetrics.heightPixels * 0.6);
        layout1.setLayoutParams( params);

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

        ImageButton imgBtnAdd = (ImageButton) promptView.findViewById(R.id.button);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });

        imgBtnAdd = (ImageButton) promptView.findViewById(R.id.stopMediaButton);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((TextView) promptView.findViewById(R.id.mediaText)).setText("");
                taskConnector.putNewTask(new StopMediaTask(statusMessages));
            }
        });


        imgBtnAdd= (ImageButton) promptView.findViewById(R.id.playAll);
        imgBtnAdd.setEnabled(false);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> ytLinks = new ArrayList<String>();
                sourceIsLocal = false;
                if (ytObjects != null && !ytObjects.isEmpty()) {
                    for (final YoutubeObject obj : ytObjects)
                        ytLinks.add(obj.getVideoLink());
                    taskConnector.putNewTask(new VideoShareTask(statusMessages, ytLinks));
                    statusMessages.displayHint(R.string.HintWaitForData);
                }
            }
        });

        imgBtnAdd= (ImageButton) promptView.findViewById(R.id.next);
        imgBtnAdd.setEnabled(false);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                taskConnector.putNewTask(new StopMediaTask(statusMessages, true,
                        sourceIsLocal) );
            }
        });


        imgBtnAdd = (ImageButton) promptView.findViewById(R.id.searchYTButton);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                taskConnector.putNewTask(new YoutubeSearchTask( ((EditText)promptView.findViewById(R.id.searchYoutubeObject)).getText().toString(), media ));
                statusMessages.displayHint(R.string.HintWaitForData);
                ((ProgressBar)(promptView.findViewById(R.id.loadingYTBar))).setVisibility(View.VISIBLE);
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

        layout = (LinearLayout) promptView.findViewById(R.id.localLayout);
        layout.removeAllViews();
        int folderIdx = 0;
        for (final String name : mediaObject.getLocalFolderNames() ) {
            final Button button = new Button(promptView.getContext());
            button.setText(name);
            int finalFolderIdx = folderIdx;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sourceIsLocal = true;
                    ((TextView) promptView.findViewById(R.id.mediaText)).setText(button.getText());
                    taskConnector.putNewTask(new PlayTask(finalFolderIdx, name,
                            statusMessages));
                }
            });

            layout.addView(button, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT , TableRow.LayoutParams.WRAP_CONTENT));
            folderIdx++;
        }

        final TabLayout tabItems = (TabLayout) promptView.findViewById(R.id.tabMediaLayout);
        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabItems.getTabAt(0) == tab){
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.VISIBLE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.localLayout);
                    tvLayout.setVisibility(View.GONE);

                    LinearLayout youtubeLayout = (LinearLayout) promptView.findViewById(R.id.youtubeLayout);
                    youtubeLayout.setVisibility(View.GONE);

                    ImageButton btn = (ImageButton) promptView.findViewById(R.id.playAll);
                    btn.setEnabled(false);
                    btn = (ImageButton) promptView.findViewById(R.id.next);
                    btn.setEnabled(false);
                }
                else if (tabItems.getTabAt(1) == tab) {
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.GONE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.localLayout);
                    tvLayout.setVisibility(View.VISIBLE);

                    LinearLayout youtubeLayout = (LinearLayout) promptView.findViewById(R.id.youtubeLayout);
                    youtubeLayout.setVisibility(View.GONE);

                    ImageButton btn = (ImageButton) promptView.findViewById(R.id.playAll);
                    btn.setEnabled(false);
                    btn = (ImageButton) promptView.findViewById(R.id.next);
                    btn.setEnabled(true);
                }
                else if (tabItems.getTabAt(2) == tab) {
                    LinearLayout radioLayout = (LinearLayout) promptView.findViewById(R.id.radioLayout);
                    radioLayout.setVisibility(View.GONE);

                    LinearLayout tvLayout = (LinearLayout) promptView.findViewById(R.id.localLayout);
                    tvLayout.setVisibility(View.GONE);

                    LinearLayout youtubeLayout = (LinearLayout) promptView.findViewById(R.id.youtubeLayout);
                    youtubeLayout.setVisibility(View.VISIBLE);
                    ImageButton btn = (ImageButton) promptView.findViewById(R.id.playAll);
                    btn.setEnabled(true);
                    btn = (ImageButton) promptView.findViewById(R.id.next);
                    btn.setEnabled(true);
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
    public void youtubeObjectRecived(final ArrayList<YoutubeObject> youtubeSearchResult) {
        //prepare list of items on the screen
        ytObjects = youtubeSearchResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Youtube ytList = new Youtube(youtubeSearchResult);
                ytList.YoutubeUpdateList(promptView, statusMessages, taskConnector);
                ((ProgressBar)(promptView.findViewById(R.id.loadingYTBar))).setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void youtubeObjectRecivedError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ProgressBar)(promptView.findViewById(R.id.loadingYTBar))).setVisibility(View.INVISIBLE);
                statusMessages.displayHint(R.string.HintErrorMessage);
            }
        });
    }
}

