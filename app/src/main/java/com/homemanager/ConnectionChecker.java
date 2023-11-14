package com.homemanager;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionChecker {
    private final int CONNECTION_TYPE_INIT   = 0;
    private final int CONNECTION_TYPE_LOCAL  = 1;
    private final int CONNECTION_TYPE_REMOTE = 2;
    private final int CONNECTION_TYPE_NONE   = 3;

    private int connectionType;
    private RestApi restApi;
    private String localUrl;
    private String remoteUrl;
    private String localRtsp;
    private String remoteRtsp;
    private NetworkService networkService;
    private Timer connectionCheckTimer;
    private boolean waitForNewConnectionSettings;
    private Object lock = new Object();
    private ConnectionMessage connectionMessage;

    public ConnectionChecker(String localUrl, String remoteUrl, NetworkService networkService, final ConnectionMessage connectionMessage) {
        this.localRtsp = "rtsp://" + localUrl + ":8554/mystream";
        this.remoteRtsp = "rtsp://" + remoteUrl + ":8554/mystream";

        this.localUrl = "http://" + localUrl + "/restApi";
        this.remoteUrl = "http://" + remoteUrl + "/restApi";

        this.networkService = networkService;
        this.connectionMessage = connectionMessage;
        connectionType = CONNECTION_TYPE_INIT;
        restApi = new RestApi(networkService.getContext());

        connectionTimerReschedule(1);
    }

    public String getCurrentRtspUrl(){
        if (connectionType == CONNECTION_TYPE_LOCAL)
            return localRtsp;
        else
            return remoteRtsp;
    }

    public void updateCheckerParameters(String localUrl){
        this.localRtsp = "rtsp://" + localUrl + ":8554/mystream";
        this.localUrl = "http://" + localUrl + "/restApi";
    }

    private void connectionTimerReschedule(int delay){
        if (connectionCheckTimer != null) connectionCheckTimer.cancel();
        connectionCheckTimer = new Timer();
        connectionCheckTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectionCheckerTimer();
            }
        }, delay);
    }
    private void connectionCheckerTimer() {
        int delay = 30000;
        synchronized (lock) {
            checkConnection();
            if (isConnectionErrorAppear()) {
                delay = 100;
            }
            connectionTimerReschedule(delay);
        }
    }

    public void restartConnectionTimer()
    {
        synchronized (lock) {
            waitForNewConnectionSettings = false;
        }
    }

    private void checkConnection()
    {
        JSONObject jsonParams = new JSONObject();

        if (networkService.isError())
        {
            connectionType = CONNECTION_TYPE_NONE;
        }

        try {
            jsonParams.put("action", "version");

            if ( ((connectionType == CONNECTION_TYPE_NONE) || (connectionType == CONNECTION_TYPE_INIT)) &&
                    (waitForNewConnectionSettings == false)) {
                restApi.writeDataToServer(localUrl, jsonParams);
                if (restApi.getResponseCode() == 200) {
                    connectionType = CONNECTION_TYPE_LOCAL;
                    networkService.clearError();
                    networkService.setUrl(localUrl);
                }
                else {
                    restApi.writeDataToServer(remoteUrl, jsonParams);
                    if (restApi.getResponseCode() == 200) {
                        connectionType = CONNECTION_TYPE_REMOTE;
                        networkService.clearError();
                        networkService.setUrl(remoteUrl);
                    }
                }
                if ((connectionType != CONNECTION_TYPE_LOCAL) &&
                        (connectionType != CONNECTION_TYPE_REMOTE) &&
                        (waitForNewConnectionSettings == false)) {
                    connectionMessage.displayConnectionError();
                    waitForNewConnectionSettings = true;
                }
            }
        }
        catch (Exception e) {
            connectionType = CONNECTION_TYPE_NONE;
        }
    }

    public boolean isLocalConnection(){
        boolean result = false;
        synchronized (lock) {
            if (connectionType == CONNECTION_TYPE_LOCAL) result = true;
            return result;
        }
    }

    public boolean isRemoteConnection(){
        boolean result = false;
        synchronized (lock) {
            if (connectionType == CONNECTION_TYPE_REMOTE) result = true;
            return result;
        }
    }

    protected void connectionErrorAppear()
    {
        synchronized (lock) {
            connectionType = CONNECTION_TYPE_NONE;
            connectionTimerReschedule(1);        }
    }

//    public boolean isConnectionErrorAppear()
//    {
//        boolean result = false;
//        synchronized (lock) {
//            if (connectionType == CONNECTION_TYPE_NONE) result = true;
//            return result;
//        }
//    }
//
//    public boolean isConnectionEstablishInProgress()
//    {
//        boolean result = false;
//        synchronized (lock) {
//            if (connectionType == CONNECTION_TYPE_INIT) result = true;
//            return result;
//        }
//    }

    public boolean isConnectionErrorAppear(){
        boolean result = false;
        synchronized (lock) {
            if ((connectionType == CONNECTION_TYPE_NONE) ||
               (connectionType == CONNECTION_TYPE_INIT)) {
                result = true;
            }
            return result;
        }
    }

}
