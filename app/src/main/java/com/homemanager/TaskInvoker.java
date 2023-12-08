package com.homemanager;

import android.content.Context;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class TaskInvoker implements Runnable, NetworkService {

    //task list
    private final List<Task> taskList = new ArrayList<Task>();

    private final Object lock = new Object();

    private String url;

    private boolean isPaused = false;

    private final Context context;

    private boolean isError = true;

    public void Pause()
    {
        synchronized (lock) {
            isPaused = true;
        }
    }

    public void Resume()
    {
        synchronized (lock) {
            isPaused = false;
        }
    }

    public TaskInvoker(Context context){
        this.context = context;
    }

    private Task getFirstTaskFromQueue()
    {
        Task task = null;
        synchronized (lock) {
            for (Task t: taskList) {
                if (t.isBeginState()){
                    task = t;
                    break;
                }
            }
        }
        return task;
    }

    private boolean finishTask()
    {
        boolean isAnyTaskDone = false;
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        synchronized (lock) {
            for (Task t: taskList) {
                if (t.isReadyState()){
                    t.setDoneState();
                    isAnyTaskDone = true;
                }
            }

            for (Task t: taskList)
                if (t.isDoneState() || t.isErrorState()) taskList.remove(t);
        }
        return isAnyTaskDone;
    }

    private long getMinTimeout()
    {
        long timeout = Long.MAX_VALUE;
        long taskTimeout = 0;
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        synchronized (lock) {
            for (Task t: taskList) {
                if (t.isBeginState()) {
                    taskTimeout = t.getDuration() - (currentTimestamp.getTime() - t.getInitTimestamp().getTime());
                    // we are out of time - set to 1 because 0 means infinity timeout
                    if (taskTimeout <= 0) taskTimeout = 1;

                    if (timeout > taskTimeout) {
                        timeout = taskTimeout;
                    }
                }
            }
        }

        if (timeout == Long.MAX_VALUE) timeout = 0;
        return timeout;
    }

    public int putNewTask(Task task)
    {
        int result = 0;
        synchronized (lock) {
            if (isPaused == false) {
                for (Task t : taskList) {
                    if ((t.getTaskDescriptor() == task.getTaskDescriptor()) &&
                            (t.isBeginState() || t.isReadyState())) {
                        //task is already in the queue
                        return -1;
                    }
                }
                taskList.add(task); //add only if no one the list
                try {
                    lock.notify();
                } catch (Exception e) {
                }
            }
            else {
                result = -1;
            }
        }
        return result;
    }

    @Override
    public void setUrl(String serverUrl){
        this.url = serverUrl;
    }

    @Override
    public boolean isError() {
        return isError;
    }

    @Override
    public void clearError(){
            isError = false;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    public void run() {

        Task task;
        long timeout = Long.MAX_VALUE;


        while(true) {
            RestApi restApi = new RestApi(context);

            synchronized (lock) {
                if (isError == false) {
                    //check if new task in the queue
                    while ((task = getFirstTaskFromQueue()) != null) {
                        restApi.writeDataToServer(url, task.getRequestData());

                        if (restApi.getResponseCode() == 200) {
                            try {
                                task.parseContent(new JSONObject(restApi.getJsonResponse()));
                                task.setInReadyState();
                            } catch (Exception e) {
                                task.setErrorState();
                            }

                        } else {
                            isError = true;
                            task.setErrorState();
                            break;
                        }

                    }
                }

                //get minimal timeout to wait
                timeout = getMinTimeout();

                //update state to DONE for in ready state task
                finishTask();

                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    //should not happen - just log this critical issue
                }
            }
        }
    }
}

