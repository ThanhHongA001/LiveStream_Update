package com.example.livestream_update.Ringme.Holder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by toanvk2 on 6/28/14.
 */
public abstract class AbsContentHolder {
    private View convertView;
    private boolean isDifferSenderWithAfter = false;
    private boolean isDifferSenderWithPrevious = false;
    private boolean isSameDateWithPrevious = false;
    private boolean isFirstNewMessage = false;
    private boolean isLastMessageOfOthers = false;
    protected Context mContext;
    private boolean isDeleting = false;

    public View getConvertView() {
        return convertView;
    }

    public void setDifferSenderWithAfter(boolean isSameWithPrevious) {
        this.isDifferSenderWithAfter = isSameWithPrevious;
    }

    public void setDifferSenderWithPrevious(boolean isDifferSenderWithPrevious) {
        this.isDifferSenderWithPrevious = isDifferSenderWithPrevious;
    }

    public void setSameDateWithPrevious(boolean isSameDateWithPrevious) {
        this.isSameDateWithPrevious = isSameDateWithPrevious;
    }

    public void setFirstNewMessage(boolean isFirstNewMessage) {
        this.isFirstNewMessage = isFirstNewMessage;
    }

    protected void setConvertView(View convertView) {
        convertView.setFocusable(false);
        this.convertView = convertView;
    }

    protected Context getContext() {
        return mContext;
    }

    public void setDeleting(boolean isDeleting) {
        this.isDeleting = isDeleting;
    }

    public void setLastMessageOfOthers(boolean lastMessageOfOthers) {
        isLastMessageOfOthers = lastMessageOfOthers;
    }

    /**
     * @param parent
     * @param rowView
     * @param position
     * @param layoutInflater
     */
    public abstract void initHolder(ViewGroup parent, View rowView, int position, LayoutInflater layoutInflater);


}
