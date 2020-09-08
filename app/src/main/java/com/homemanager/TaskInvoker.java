package com.homemanager;

import com.example.homemanager.R;
import com.homemanager.Task.Task;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class TaskInvoker implements Runnable, NetworkService {

    //task list
    private List<Task> taskList = new ArrayList<Task>();

    private Object lock = new Object();

    private String url;

    private boolean isError = false;


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
                if((currentTimestamp.getTime() - t.getInitTimestamp().getTime() >= t.getDuration()) &&
                        (t.isInProgressState())){
                    t.setDoneState();
                    isAnyTaskDone = true;
                }
            }
            //for (Task t: taskList) {
                //if (t.isDoneState()) taskList.remove(t);
            //}
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
                if (t.isInProgressState()) {
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

    public void putNewTask(Task task)
    {
        synchronized (lock) {
            for (Task t: taskList) {
                if ((t.getTaskDescriptor() == task.getTaskDescriptor() ) &&
                        (t.isBeginState() || t.isInProgressState())){
                    //task is already in the queue
                    return;
                }
            }
            taskList.add(task); //add only if no one the list
            try {
                lock.notify();
            }
            catch (Exception e) {}
        }
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

    public void run() {

        Task task;
        long timeout = Long.MAX_VALUE;
        RestApi restApi = new RestApi();

        while(true) {
            //check if new task in the queue
            while ((task = getFirstTaskFromQueue()) != null) {
                restApi.writeDataToServer(url , task.getRequestData());

                synchronized (lock) {
                    if (restApi.getResponseCode() == 200) {
                        try {
                            task.parseContent(new JSONObject(restApi.getJsonResponse()));
                            task.setInProgressState();
                        }
                        catch(Exception e){
                        }

                    } else {
                        isError = true;
                        task.setErrorState(R.string.connectionError);
                    }
                }

            }

            synchronized (lock) {
                //get minimal timeout to wait
                timeout = getMinTimeout();

                //update state to DONE for in progress task
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

