package com.homemanager.Task.Youtube;

public class YoutubeObject {
    private String iconLink;
    private String videoLink;
    private String title;
    private String duration;

    public YoutubeObject(String title, String iconLink, String videoLink, String duration) {
        this.iconLink = iconLink;
        this.videoLink = videoLink;
        this.title = title;
        this.duration = duration;
    }

    public String getIconLink() {
        return iconLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }
}
