package com.homemanager.Task.Youtube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.homemanager.Task.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class YoutubeSearchTask extends Task {

    private long duration = 0;
    private String searchText;
    private JSONArray searchResult;
    private YoutubeInterface youtubeInterface;

    public YoutubeSearchTask(String searchText, YoutubeInterface youtubeInterface){
        super();
        this.searchText = searchText;
        this.youtubeInterface = youtubeInterface;
    }

    @Override
    public long getDuration()
    {
        return duration;
    }

    @Override
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public int getTaskDescriptor()
    {
        return 260682;
    }

    @Override
    public JSONObject getRequestData() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("action", "getYTSearchResult");
            jsonParams.put("search", searchText);
        }
        catch(Exception e){
        }

        return jsonParams;
    }

    @Override
    public void parseContent(JSONObject content){
        try {
            searchResult = content.getJSONArray("videos");
        }
        catch(JSONException e){
        }
    }


    @Override
    public void inDoneStateNotification(){
        ArrayList<YoutubeObject> youtubeSearchResult = new ArrayList<YoutubeObject>();

        if (searchResult == null) {
            youtubeInterface.youtubeObjectRecivedError();
            return;
        }

        for (int i = 0; i < searchResult.length(); i++) {
            try {
                JSONObject obj = searchResult.getJSONObject(i);
                youtubeSearchResult.add(new YoutubeObject(obj.getString("title"),
                        obj.getString("icon"),obj.getString("link"),
                        obj.getString("duration"))  );
            }
            catch (JSONException e){
            }
        }

        youtubeInterface.youtubeObjectRecived(youtubeSearchResult);
    }

    @Override
    public void inErrorStateNotification(){
        youtubeInterface.youtubeObjectRecivedError();
    }

}
