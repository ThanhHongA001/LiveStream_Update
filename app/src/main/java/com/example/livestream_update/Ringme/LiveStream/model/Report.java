package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class Report implements Serializable {
    private int id;
    private String title;
    private String description;
    private boolean isChosen;

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
