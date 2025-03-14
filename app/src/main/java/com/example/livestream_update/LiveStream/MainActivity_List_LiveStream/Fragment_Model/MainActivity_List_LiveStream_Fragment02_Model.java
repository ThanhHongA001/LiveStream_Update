package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model;

public class MainActivity_List_LiveStream_Fragment02_Model {
    private int imageResId;
    private String streamName;
    private String viewCount;

    public MainActivity_List_LiveStream_Fragment02_Model(int imageResId, String streamName, String viewCount) {
        this.imageResId = imageResId;
        this.streamName = streamName;
        this.viewCount = viewCount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getViewCount() {
        return viewCount;
    }
}
