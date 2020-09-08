package com.homemanager;

import org.json.JSONObject;

public class ConnectionChecker {
    private final int CONNECTION_TYPE_INIT   = 0;
    private final int CONNECTION_TYPE_LOCAL  = 1;
    private final int CONNECTION_TYPE_REMOTE = 2;
    private final int CONNECTION_TYPE_NONE   = 3;

    private int connectionType = CONNECTION_TYPE_INIT;
    private RestApi restApi = new RestApi();
    private String localUrl;
    private String remoteUrl;
    private NetworkService networkService;
    private Object lock = new Object();

    public ConnectionChecker(String localUrl, String remoteUrl, NetworkService networkService)
    {
        this.localUrl = localUrl;
        this.remoteUrl = remoteUrl;
        this.networkService = networkService;
    }

    public void checkConnection()
    {
        synchronized (lock) {
            if (networkService.isError())
            {
                connectionErrorAppear();
            }
            JSONObject jsonParams = new JSONObject();

            try {
                jsonParams.put("action", "version");

                if ((connectionType == CONNECTION_TYPE_NONE) || (connectionType == CONNECTION_TYPE_INIT)) {
                    restApi.writeDataToServer(localUrl, jsonParams);
                    if (restApi.getResponseCode() == 200) {
                        connectionType = CONNECTION_TYPE_LOCAL;
                        networkService.clearError();
                    } else {
                        restApi.writeDataToServer(remoteUrl, jsonParams);
                        if (restApi.getResponseCode() == 200) {
                            connectionType = CONNECTION_TYPE_REMOTE;
                            networkService.clearError();
                        }
                    }
                }
            }
            catch (Exception e) {}
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

    private void connectionErrorAppear()
    {
        connectionType = CONNECTION_TYPE_NONE;
    }

    public boolean isConnectionErrorAppear()
    {
        boolean result = false;
        synchronized (lock) {
            if (connectionType == CONNECTION_TYPE_NONE) result = true;
            return result;
        }
    }

    public boolean isConnectionEstablishInProgress()
    {
        boolean result = false;
        synchronized (lock) {
            if (connectionType == CONNECTION_TYPE_INIT) result = true;
            return result;
        }
    }

}
