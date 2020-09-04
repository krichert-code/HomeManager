package com.homemanager.Task.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaObject {
    private List<Integer> radioChannelIds = new ArrayList<Integer>();
    private List<String> radioChannelnames = new ArrayList<String>();

    private List<Integer> tvChannelIds = new ArrayList<Integer>();
    private List<String> tvChannelnames = new ArrayList<String>();

    private int volume;

    public List<Integer> getRadioChannelIds() {
        return radioChannelIds;
    }

    public void addRadioChannelId(int radioChannelId) {
        this.radioChannelIds.add(radioChannelId);
    }

    public List<String> getRadioChannelnames() {
        return radioChannelnames;
    }

    public void addRadioChannelname(String radioChannelname) {
        this.radioChannelnames.add(radioChannelname);
    }

    public List<Integer> getTvChannelIds() {
        return tvChannelIds;
    }

    public void addTvChannelId(int tvChannelId) {
        this.tvChannelIds.add(tvChannelId);
    }

    public List<String> getTvChannelnames() {
        return tvChannelnames;
    }

    public void addTvChannelname(String tvChannelname) {
        this.tvChannelnames.add(tvChannelname);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
