package com.example.livestream_update.Ringme.Model;

import java.text.SimpleDateFormat;

public class LogDebugModel {
    private final long timeStamp;
    private final String content;

    private static final String SDF_FULL = "yyyy/MM/dd HH:mm:ss.SSS";
    SimpleDateFormat spf = new SimpleDateFormat(SDF_FULL);

    public LogDebugModel(long timeStamp, String content) {
        this.timeStamp = timeStamp;
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getContent() {
        return content;
    }


    @Override
    public String toString() {
        return spf.format(timeStamp) + " | " + content;
    }


}