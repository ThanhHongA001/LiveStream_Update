package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class ReactionCommentModel implements Serializable, Comparable {
    private String type;
    private int number;
    private String sidMessage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSidMessage() {
        return sidMessage;
    }

    public void setSidMessage(String sidMessage) {
        this.sidMessage = sidMessage;
    }

    @Override
    public int compareTo(Object o) {
        int compareNumber = ((ReactionCommentModel) o).getNumber();
        return compareNumber - this.getNumber();
    }

    public ReactionCommentModel(String type, int number) {
        this.type = type;
        this.number = number;
    }
}
