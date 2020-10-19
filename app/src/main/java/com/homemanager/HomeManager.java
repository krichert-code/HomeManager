package com.homemanager;

import android.annotation.SuppressLint;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.homemanager.Alarm.Alarm;
import com.homemanager.Network.Network;
import com.homemanager.Task.Action.DoorTask;
import com.homemanager.Task.Action.GateTask;
import com.homemanager.Task.Garden.GardenTask;
import com.homemanager.Heater.Heater;
import com.homemanager.Info.Info;
import com.homemanager.Media.Media;
import com.example.homemanager.R;
import com.homemanager.Schedule.Schedule;
import com.homemanager.Task.Action.EventsTask;
import com.homemanager.Task.Action.MainGateTask;
import com.homemanager.Task.Action.SoundVolumeTask;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Action.TaskDescription;
import com.homemanager.Garden.Garden;
import com.homemanager.Task.Garden.GardenMessage;
import com.homemanager.Task.Garden.GardenObject;
import com.homemanager.Task.Heater.HeaterMessage;
import com.homemanager.Task.Heater.HeaterObject;
import com.homemanager.Task.Heater.HeaterTask;
import com.homemanager.Task.Info.InfoMessage;
import com.homemanager.Task.Info.InfoObject;
import com.homemanager.Task.Info.InfoTask;
import com.homemanager.Task.Media.MediaMessage;
import com.homemanager.Task.Media.MediaObject;
import com.homemanager.Task.Media.MediaTask;
import com.homemanager.Task.Schedule.ScheduleMessage;
import com.homemanager.Task.Schedule.ScheduleObject;
import com.homemanager.Task.Schedule.ScheduleTask;
import com.homemanager.Task.Task;
import com.homemanager.Task.Temperature.TemperatureMessage;
import com.homemanager.Task.Temperature.TemperatureObject;
import com.homemanager.Task.Temperature.TemperatureTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeManager extends AppCompatActivity implements StatusMessage, TemperatureMessage,
        InfoMessage, TaskConnector, ScheduleMessage, HeaterMessage, GardenMessage, MediaMessage,
        ConnectionMessage {

    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();

            statusTimerReschedule(1000);
        }
    };

    private final HomeManager appContext = this;

    private TaskInvoker taskDispatcher;

    private Timer timer;
    private TableLayout tl;
    private ConnectionChecker connectionChecker;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();


    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;

    private void statusTimerReschedule(int delay){
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkGeneralStatus();
            }
        }, delay);
    }

    private void checkGeneralStatus(){
        if(!connectionChecker.isConnectionErrorAppear() && !connectionChecker.isConnectionEstablishInProgress()) {
            putNewTask(new TemperatureTask(this));
            putNewTask(new EventsTask(this));
            statusTimerReschedule(30000);
        }
        else {
            statusTimerReschedule(1000);
        }
    }


    @Override
    public int putNewTask(Task task){
        return taskDispatcher.putNewTask(task);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        new Thread(taskDispatcher = new TaskInvoker(getApplicationContext())).start();

        //reasd localhost from the resource
        SharedPreferences prefs = getSharedPreferences(
                "com.homemanager", getApplicationContext().MODE_PRIVATE);

        String localUrl = "http://" + prefs.getString("com.homemanager.localUrl", "") + ":8090/restApi";

        connectionChecker = new ConnectionChecker(localUrl,
                getString(R.string.RemoteUrl), taskDispatcher, this);



        findViewById(R.id.imageGate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putNewTask(new GateTask(appContext));
            }
        });
        findViewById(R.id.imageGate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.GateOpenText)

                        .setPositiveButton(R.string.YesButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                putNewTask(new MainGateTask(appContext, true));
                            }
                        })


                        .setNegativeButton(R.string.NoButton, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                putNewTask(new MainGateTask(appContext, false));
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                hide();
            }
        });

        findViewById(R.id.imageDoor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putNewTask(new DoorTask(appContext));
            }
        });

        findViewById(R.id.imageSprinkler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                putNewTask(new GardenTask(appContext));
            }
        });

        findViewById(R.id.imageInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                putNewTask(new InfoTask(appContext));
            }
        });

        findViewById(R.id.imageSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                putNewTask(new ScheduleTask(appContext));
                Toast toast = Toast.makeText(view.getContext(), R.string.HintWaitForData, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        findViewById(R.id.imageHeater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                putNewTask(new HeaterTask(appContext));
            }
        });

        findViewById(R.id.imageMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                putNewTask(new MediaTask(appContext));
            }
        });

        findViewById(R.id.imageAlarm).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                Alarm alarm = new Alarm(appContext);
                AlertDialog alarmDialog = new AlertDialog.Builder(appContext).create();
                alarmDialog.setView(alarm.createScreen(mContentView, alarmDialog));
                alarmDialog.show();
                hide();
            }
        });

                /*
                HOW to switch to another window (activity2 class must extend Activity and must
                exist in manifest xml:
                ---------------------------------------------------------------------------------
                Intent myIntent = new Intent(view.getContext(), Activity2.class);
                startActivityForResult(myIntent, 0);

                HOW to CREATE standard AlertDialog:
                ----------------------------------
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("Write your message here.");
                builder1.setCancelable(true);

                builder1.setNeutralButton(
                        "Sprinkler 1",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                //builder1.
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        */

        //fixed task to get events in progress
        timer = new Timer();
        statusTimerReschedule(1000);

    }


    @Override
    public void doneActionNotification(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                putNewTask(new EventsTask(appContext));
            }
        });
    }

    @Override
    public void displaySchedule(final List<ScheduleObject> elements){
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Schedule scheduleData = new Schedule();
                if (false == connectionChecker.isConnectionErrorAppear() &&
                        false == connectionChecker.isConnectionEstablishInProgress()) {
                    AlertDialog scheduleDialog = new AlertDialog.Builder(appContext).create();
                    scheduleDialog.setView(scheduleData.createScreen(mContentView, scheduleDialog, elements));
                    scheduleDialog.show();
                    hide();
                }
            }
        });

    }

    @Override
    public void displayHeaterData(final HeaterObject heaterObject){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Heater heaterData = new Heater(appContext,appContext);
                if (false == connectionChecker.isConnectionErrorAppear() &&
                        false == connectionChecker.isConnectionEstablishInProgress()) {
                    AlertDialog heaterDialog = new AlertDialog.Builder(appContext).create();
                    heaterDialog.setView(heaterData.createScreen(mContentView, heaterDialog, heaterObject));
                    heaterDialog.show();
                    hide();
                }
            }
        });

    }

    @Override
    public void displayMedia(final MediaObject mediaObject){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (false == connectionChecker.isConnectionErrorAppear() &&
                        false == connectionChecker.isConnectionEstablishInProgress()) {
                    Media mediaData = new Media(appContext, appContext);
                    AlertDialog mediaDialog = new AlertDialog.Builder(appContext).create();
                    mediaDialog.setView(mediaData.createScreen(mContentView, mediaDialog, mediaObject));
                    mediaDialog.show();
                    hide();
                }
            }
        });

    }

    @Override
    public void displayGardenData(final GardenObject gardenObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Garden garden = new Garden(appContext, appContext);
                if (false == connectionChecker.isConnectionErrorAppear() &&
                        false == connectionChecker.isConnectionEstablishInProgress()) {
                    AlertDialog gardenDialog = new AlertDialog.Builder(appContext).create();
                    gardenDialog.setView(garden.createScreen(mContentView, gardenDialog, gardenObject));
                    gardenDialog.show();
                    hide();
                }
            }
        });
    }

    @Override
    public void displayInfo(final InfoObject info){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Info infoData = new Info(appContext, appContext);
                              if (false == connectionChecker.isConnectionErrorAppear() &&
                                  false == connectionChecker.isConnectionEstablishInProgress()) {
                                  AlertDialog infoDialog = new AlertDialog.Builder(appContext).create();
                                  infoDialog.setView(infoData.createScreen(mContentView, infoDialog, info));
                                  infoDialog.show();
                                  hide();
                              }
                          }
                      });
    }

    @Override
    public void displayHint(final int hint){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast toast = Toast.makeText(appContext, hint, Toast.LENGTH_SHORT);
                              toast.show();
                          }
                      });
    }

    @Override
    public void displayData(final List<TaskDescription> taskDesc) {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if (connectionChecker.isConnectionErrorAppear() ||
                        connectionChecker.isConnectionEstablishInProgress()){
                    return;
                }

                tl = (TableLayout) findViewById(R.id.News);
                // Stuff that updates the UI
                tl.removeAllViews();

                for (TaskDescription description : taskDesc) {
                    TableRow tr1 = new TableRow(getApplicationContext());

                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView textview = new TextView(getApplicationContext());
                    textview.setText(description.getDescription() + " " + description.getDate());
                    textview.setTextColor(Color.BLACK);
                    textview.setElegantTextHeight(true);
                    textview.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    textview.setHorizontallyScrolling(false);

                    textview.setSingleLine(false);
                    textview.setMaxLines(3);
                    textview.setMinLines(1);
                    textview.setMaxWidth(tl.getWidth() - 100);

                    ImageView image = new ImageView(getApplicationContext());
                    image.setImageDrawable(getResources().getDrawable(description.getIcon()));

                    tr1.addView(image);
                    tr1.addView(textview);

                    LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)textview.getLayoutParams();
                    p.rightMargin=10;
                    p.leftMargin = 20;
                    p.gravity=Gravity.CENTER | Gravity.LEFT;

                    image.getLayoutParams().height = 60;
                    image.getLayoutParams().width = 60;

                    tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT ,
                            TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
        });
    }

    @Override
    public void displayErrorMessage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView temp = (TextView) findViewById(R.id.temperature);
                temp.setText(getString(R.string.connectionError));
            }
        });
    }

    @Override
    public void displayTemperature(final TemperatureObject temperature){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    TextView temp = (TextView) findViewById(R.id.temperature);
                    ImageView  modeImage = (ImageView) findViewById(R.id.tempModeImage);

                    temp.setText(temperature.getTemperature() + " \u2103");
                    modeImage.setImageDrawable(getResources().getDrawable(temperature.getMode()));
                }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        hide();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            putNewTask(new SoundVolumeTask(SoundVolumeTask.VOLUME_DOWN,0, appContext));
        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            putNewTask(new SoundVolumeTask(SoundVolumeTask.VOLUME_UP,0, appContext));
        }

        return true;
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void displayConnectionError() {
        //display configuration panel
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Network netIssue = new Network(appContext);
                AlertDialog alarmDialog = new AlertDialog.Builder(appContext).create();
                alarmDialog.setView(netIssue.createScreen(mContentView, alarmDialog, appContext));
                alarmDialog.show();
                hide();
            }
        });
    }

    @Override
    public void connectionParametersChanged() {
        SharedPreferences prefs = getSharedPreferences(
                "com.homemanager", getApplicationContext().MODE_PRIVATE);

        String localUrl = "http://" + prefs.getString("com.homemanager.localUrl", "") + ":8090/restApi";

        connectionChecker.updateCheckerParameters(localUrl);
        connectionChecker.restartConnectionTimer();
        statusTimerReschedule(2000);
    }
}