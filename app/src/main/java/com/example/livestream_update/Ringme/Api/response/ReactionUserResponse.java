package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.livestream.model.ReactionCommentModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ReactionUserResponse implements Serializable {
    private int code;
    private String message;
    ArrayList<ReactionCommentModel> data;

    public ArrayList<ReactionCommentModel> getData() {
        return data;
    }
}

