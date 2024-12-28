package com.homemanager.Task.Action;

import com.homemanager.Task.Status.StatusMessage;

import org.json.JSONObject;

public class PlayTask extends EventsTask {
    private long duration = 5 * 1000;

    private StatusMessage statusMessages;
    private int channelId;
    private int folderId;
    private String folder;
    private boolean local;

    public PlayTask(int channelId, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.channelId = channelId;
        this.local = false;
    }

    public PlayTask(int folderId, String folder, StatusMessage statusMessages){
        super(statusMessages);
        this.statusMessages = statusMessages;
        this.folderId = folderId;
        this.folder = folder;
        this.local = true;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            if (!local) {
                jsonParams.put("action", "PlayPVR");
                jsonParams.put("channel", channelId);
            }
            else {
                jsonParams.put("action", "PlayMp3");
                jsonParams.put("folder", folder);
                jsonParams.put("folderId", folderId);
            }
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor(){
        return 1921;
    }
}