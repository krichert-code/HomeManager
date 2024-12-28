package com.homemanager.Task.Youtube;

import java.util.ArrayList;
import java.util.List;

public interface YoutubeInterface {
    void youtubeObjectRecived(ArrayList<YoutubeObject> youtubeSearchResult);
    void youtubeObjectRecivedError();
}
