package com.homemanager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.homemanager.R;
import com.homemanager.Task.Action.EventsTask;
import com.homemanager.Task.Action.StatusMessage;
import com.homemanager.Task.Action.TaskDescription;
import com.homemanager.Task.Task;
import com.homemanager.Task.Temperature.TemperatureMessage;
import com.homemanager.Task.Temperature.TemperatureObject;
import com.homemanager.Task.Temperature.TemperatureTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class EventsProvider implements Runnable, TemperatureMessage, StatusMessage {
    private Object lock = new Object();
    private Context context;

    private String insideTemp = "-";
    private String outsideTemp = "-";
    private List<TaskDescription> events;

    public EventsProvider(Context context) {
        this.context = context;
    }

    public String getTemperatureInside(){
        return insideTemp;
    }

    public String getTemperatureOutside(){
        return outsideTemp;
    }

    public List<TaskDescription> getEvents() { return events; }


    @Override
    public void run() {

        Task task;
        long timeout = 25000;

        RestApi restApi = new RestApi(context);
        SharedPreferences prefs = context.getSharedPreferences(
                "com.homemanager", context.MODE_PRIVATE);

        String localUrl = "http://" + prefs.getString("com.homemanager.localUrl", "") + ":8090/restApi";
        String remoteUrl = context.getString(R.string.RemoteUrl);
        TemperatureTask tempTask = new TemperatureTask(this);
        EventsTask eventsTask = new EventsTask(this);

        while (true) {
            synchronized (lock) {
                try {
                    restApi.writeDataToServer(localUrl, tempTask.getRequestData());
                    if (restApi.getResponseCode() == 200){
                        tempTask.parseContent(new JSONObject(restApi.getJsonResponse()));
                        tempTask.inProgressStateNotification();
                    }

                    restApi.writeDataToServer(localUrl, eventsTask.getRequestData());
                    if (restApi.getResponseCode() == 200){
                        eventsTask.parseContent(new JSONObject(restApi.getJsonResponse()));
                        eventsTask.inProgressStateNotification();
                    }

                } catch (JSONException e) {}

                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    //should not happen - just log this critical issue
                }
            }
        }
    }

    @Override
    public void displayTemperature(TemperatureObject temperature) {
        if (temperature.isValidInside()){
            insideTemp = temperature.getInsideTemperature();
        }
        if (temperature.isValidOutside()){
            outsideTemp = temperature.getOutsideTemperature();
        }
    }

    @Override
    public void displayData(List<TaskDescription> taskDesc) {
        events = taskDesc;
    }

    @Override
    public void doneActionNotification() {

    }

    @Override
    public void displayErrorMessage() {

    }

    @Override
    public void displayHint(int hint) {

    }

    @Override
    public void displayCustomHint(String hint) {

    }
}
/**
 * Implementation of App Widget functionality.
 */
public class HomeManagerWidget extends AppWidgetProvider{

    private EventsProvider eventsProvider = null;
    private  final int maxNumberOfMessages = 6;

    void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                         final int appWidgetId) {


        if(eventsProvider == null) {
            eventsProvider = new EventsProvider(context);
            new Thread(eventsProvider).start();
        }

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String resName;
                int resId = 1;

                CharSequence tempText = eventsProvider.getTemperatureInside() + "ºC";

                tempText = tempText + " \\ " + eventsProvider.getTemperatureOutside() + "ºC";
                
                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_manager_widget);
                views.setTextViewText(R.id.appwidget_text, tempText);

                try {
                    for (TaskDescription event : eventsProvider.getEvents()) {
                        resName = "eventText" + resId;
                        views.setTextViewText(context.getResources().getIdentifier(resName, "id", context.getPackageName()), event.getDescription());

                        resName = "eventIco" + resId;
                        views.setImageViewResource(context.getResources().getIdentifier(resName, "id", context.getPackageName()), event.getIcon());

                        resName = "eventDate" + resId;
                        views.setTextViewText(context.getResources().getIdentifier(resName, "id", context.getPackageName()), event.getDate());

                        resId++;

                        if (resId > maxNumberOfMessages) break;
                    }
                }
                catch (Exception e) {}

                while (resId <= maxNumberOfMessages){
                    resName = "eventIco" + resId;
                    views.setImageViewResource(context.getResources().getIdentifier(resName, "id", context.getPackageName()), 0);

                    resName = "eventText" + resId;
                    views.setTextViewText(context.getResources().getIdentifier(resName, "id", context.getPackageName()), "");

                    resName = "eventDate" + resId;
                    views.setTextViewText(context.getResources().getIdentifier(resName, "id", context.getPackageName()), "");

                    resId++;
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        },5000,  30000);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

