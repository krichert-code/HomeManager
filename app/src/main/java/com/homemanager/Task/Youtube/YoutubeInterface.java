package com.homemanager.Task.Youtube;

import com.homemanager.Task.Spotify.SpotifyGetObject;

import java.util.ArrayList;
import java.util.List;

public interface YoutubeInterface {
    void youtubeObjectRecived(ArrayList<YoutubeObject> youtubeSearchResult);
    void youtubeObjectRecivedError();
}
