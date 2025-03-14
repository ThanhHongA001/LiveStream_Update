package com.example.livestream_update.Ringme.Home;



import com.vtm.ringme.model.tab_video.Channel;

import java.util.List;

public class VideoSearchModel {
    private List<VideoMainHome> videos;
    private List<Channel> channels;
    private int dataNum;
    private List<VideoMainHome> shortVideo;

    public VideoSearchModel() {
        dataNum = 3;
    }
    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public List<VideoMainHome> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoMainHome> videos) {
        this.videos = videos;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<VideoMainHome> getShortVideo() {
        return shortVideo;
    }

    public void setShortVideo(List<VideoMainHome> shortVideo) {
        this.shortVideo = shortVideo;
    }
}

