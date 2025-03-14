package com.example.livestream_update.Ringme.Model;


import java.io.Serializable;

/**
 * Created by toanvk2 on 7/9/14.
 */
public class ItemContextMenu implements Serializable {
    private CharSequence mText;

    private int mImageRes;
    private int mActionTag;
    private Object mObj;
    private boolean isChecked = false;

    // new obj Item
    public ItemContextMenu(CharSequence text, int imageResourceId, Object obj, int actionTag) {
        this.mText = text;
        mImageRes = imageResourceId;
       /* if (imageResourceId != -1) {
            mImage = ContextCompat.getDrawable(context, imageResourceId);
        } else {
            mImage = null;
        }*/
        this.mActionTag = actionTag;
        this.mObj = obj;
    }

    public ItemContextMenu() {
    }

    public ItemContextMenu(int action) {
        this.mActionTag = action;
    }

    public void setText(CharSequence text) {
        this.mText = text;
    }

    public CharSequence getText() {
        return mText;
    }

    public int getImageRes() {
        return mImageRes;
    }

    public void setActionTag(int tag) {
        this.mActionTag = tag;
    }

    public int getActionTag() {
        return mActionTag;
    }

    public Object getObj() {
        return mObj;
    }

    public void setObj(Object obj) {
        this.mObj = obj;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
