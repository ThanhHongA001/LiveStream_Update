package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model;

public class MainActivity_List_LiveStream_Fragment01_Model {
    private int imageResource;
    private String streamName;
    private int viewCount;

    public MainActivity_List_LiveStream_Fragment01_Model(int imageResource, String streamName, int viewCount) {
        this.imageResource = imageResource;
        this.streamName = streamName;
        this.viewCount = viewCount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getStreamName() {
        return streamName;
    }

    public int getViewCount() {
        return viewCount;
    }
}
