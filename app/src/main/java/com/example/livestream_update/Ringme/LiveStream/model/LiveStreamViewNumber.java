package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class LiveStreamViewNumber implements Serializable {
    private String type;
    private String topicId;
    private int number;

    public LiveStreamViewNumber() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getNumber() {
        if(number < 1) return 1;
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
