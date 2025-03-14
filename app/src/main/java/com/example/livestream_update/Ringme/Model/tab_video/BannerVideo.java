package com.example.livestream_update.Ringme.Model.tab_video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerVideo {

    @SerializedName("intervalItem")
    @Expose
    private long intervalItem;
    @SerializedName("displayTime")
    @Expose
    private long displayTime;
    @SerializedName("actSMS")
    @Expose
    private ActSMS actSMS;
    @SerializedName("actFakeMOInline")
    @Expose
    private ActFakeMOInline actFakeMOInline;
    @SerializedName("display")
    @Expose
    private Display display;
    @SerializedName("filter")
    @Expose
    private Filter filter;
    @SerializedName("lStartTime")
    @Expose
    private long lStartTime;
    @SerializedName("lEndTime")
    @Expose
    private long lEndTime;

    public long getIntervalItem() {
        return intervalItem;
    }

    public void setIntervalItem(long intervalItem) {
        this.intervalItem = intervalItem;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }

    public ActSMS getActSMS() {
        return actSMS;
    }

    public void setActSMS(ActSMS actSMS) {
        this.actSMS = actSMS;
    }

    public void setActFakeMOInline(ActFakeMOInline actFakeMOInline) {
        this.actFakeMOInline = actFakeMOInline;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public long getlStartTime() {
        return lStartTime;
    }

    public void setlStartTime(long lStartTime) {
        this.lStartTime = lStartTime;
    }

    public long getlEndTime() {
        return lEndTime;
    }

    public void setlEndTime(long lEndTime) {
        this.lEndTime = lEndTime;
    }

    public class ActSMS {

        @SerializedName("smsCommand")
        @Expose
        private String smsCommand;

        @SerializedName("smsCodes")
        @Expose
        private String smsCodes;

        public String getSmsCommand() {
            return smsCommand;
        }

        public void setSmsCommand(String smsCommand) {
            this.smsCommand = smsCommand;
        }

        public String getSmsCodes() {
            return smsCodes;
        }

        public void setSmsCodes(String smsCodes) {
            this.smsCodes = smsCodes;
        }
    }

    public class ActFakeMOInline {

        @SerializedName("contentConfirm")
        @Expose
        private String contentConfirm;
        @SerializedName("iconConfirm")
        @Expose
        private String iconConfirm;
        @SerializedName("labelConfirm")
        @Expose
        private String labelConfirm;
        @SerializedName("backgroundConfirm")
        @Expose
        private String backgroundConfirm;
        @SerializedName("command")
        @Expose
        private String command;

        public String getContentConfirm() {
            return contentConfirm;
        }

        public void setContentConfirm(String contentConfirm) {
            this.contentConfirm = contentConfirm;
        }

        public String getIconConfirm() {
            return iconConfirm;
        }

        public void setIconConfirm(String iconConfirm) {
            this.iconConfirm = iconConfirm;
        }

        public String getLabelConfirm() {
            return labelConfirm;
        }

        public void setLabelConfirm(String labelConfirm) {
            this.labelConfirm = labelConfirm;
        }

        public String getBackgroundConfirm() {
            return backgroundConfirm;
        }

        public void setBackgroundConfirm(String backgroundConfirm) {
            this.backgroundConfirm = backgroundConfirm;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    public class Display {

        @SerializedName("background")
        @Expose
        private String background;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("labelBtn")
        @Expose
        private String labelBtn;

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLabelBtn() {
            return labelBtn;
        }

        public void setLabelBtn(String labelBtn) {
            this.labelBtn = labelBtn;
        }

    }

    public class Filter {

        @SerializedName("isVip")
        @Expose
        private List<String> isVip = null;
        @SerializedName("networkType")
        @Expose
        private List<String> networkType = null;
        @SerializedName("channelId")
        @Expose
        private List<String> channelId = null;

        public List<String> getIsVip() {
            return isVip;
        }

        public void setIsVip(List<String> isVip) {
            this.isVip = isVip;
        }

        public List<String> getNetworkType() {
            return networkType;
        }

        public void setNetworkType(List<String> networkType) {
            this.networkType = networkType;
        }

        public List<String> getChannelId() {
            return channelId;
        }

        public void setChannelId(List<String> channelId) {
            this.channelId = channelId;
        }

    }
}
