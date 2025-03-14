package com.example.livestream_update.Ringme.Model.livestream;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.LivestreamVoteModel;
import com.vtm.ringme.model.tab_video.Channel;

import java.io.Serializable;
import java.util.ArrayList;

public class LivestreamModel implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("status")
    private int status;
    @SerializedName("privacy")
    private int privacy;
    @SerializedName("hlsPlayLink")
    private String hlsPlaylink;
    @SerializedName("linkAvatar")
    private String linkAvatar;
    @SerializedName("totalView")
    private int totalView;
    @SerializedName("totalLike")
    private int totalLike;
    @SerializedName("totalComment")
    private int totalComment;
    @SerializedName("totalShare")
    private int totalShare;
    @SerializedName("timeStart")
    private long timeStart;
    @SerializedName("ageLimit")
    private int ageLimit;
    @SerializedName("enableChat")
    private int enableChat;
    @SerializedName("chatSetting")
    private int chatSetting;
    @SerializedName("channelId")
    private String channelId;
    @SerializedName("channelName")
    private String channelName;
    @SerializedName("totalViewStr")
    private String totalViewStr;
    @SerializedName("follow")
    private boolean follow;
    @SerializedName("enableDvr")
    private int enableDvr;
    @SerializedName("like")
    private boolean like;
    @SerializedName("blockComment")
    private boolean blockComment;
    @SerializedName("adaptiveLinks")
    private AdaptiveLinkModel adaptiveLinks;
    @SerializedName("bannerLink")
    private String bannerLnk;
    @SerializedName("totalFollow")
    private int totalFollow;
    @SerializedName("channel")
    private Channel channel;
    @SerializedName("videoId")
    private String videoId;
    @SerializedName("typeScreen")
    private int screenType = 0;
    @SerializedName("type")
    private int type;
    @SerializedName("timeEventStart")
    private long timeEventStart;
    @SerializedName("notify")
    private boolean isNotified;
    @SerializedName("surveyDto")
    private ArrayList<LivestreamVoteModel> listVote;

    @SerializedName("enableDonate")
    private boolean enableDonate;

    public boolean isEnableDonate() {
        return enableDonate;
    }

    public void setEnableDonate(boolean enableDonate) {
        this.enableDonate = enableDonate;
    }

    private int indexQuality = 0;

    private boolean isPause;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String getHlsPlaylink() {
        return hlsPlaylink;
    }

    public void setHlsPlaylink(String hlsPlaylink) {
        this.hlsPlaylink = hlsPlaylink;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public int getTotalView() {
        return totalView;
    }

    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public int getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(int totalShare) {
        this.totalShare = totalShare;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getEnableChat() {
        return enableChat;
    }

    public void setEnableChat(int enableChat) {
        this.enableChat = enableChat;
    }

    public int getChatSetting() {
        return chatSetting;
    }

    public void setChatSetting(int chatSetting) {
        this.chatSetting = chatSetting;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTotalViewStr() {
        return totalViewStr;
    }

    public void setTotalViewStr(String totalViewStr) {
        this.totalViewStr = totalViewStr;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isBlockComment() {
        return blockComment;
    }

    public void setBlockComment(boolean blockComment) {
        this.blockComment = blockComment;
    }

    public int getEnableDvr() {
        return enableDvr;
    }

    public void setEnableDvr(int enableDvr) {
        this.enableDvr = enableDvr;
    }

    public AdaptiveLinkModel getAdaptiveLinks() {
        return adaptiveLinks;
    }

    public void setAdaptiveLinks(AdaptiveLinkModel adaptiveLinks) {
        this.adaptiveLinks = adaptiveLinks;
    }

    public int getIndexQuality() {
        return indexQuality;
    }

    public void setIndexQuality(int indexQuality) {
        this.indexQuality = indexQuality;
    }

    public String getBannerLnk() {
        return bannerLnk;
    }

    public void setBannerLnk(String bannerLnk) {
        this.bannerLnk = bannerLnk;
    }

    public int getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(int totalFollow) {
        this.totalFollow = totalFollow;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public int getScreenType() {
        return this.screenType;
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;
    }

    public long getTimeEventStart() {
        return timeEventStart;
    }

    public void setTimeEventStart(long timeEventStart) {
        this.timeEventStart = timeEventStart;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<LivestreamVoteModel> getListVote() {
        return listVote;
    }

    public void setListVote(ArrayList<LivestreamVoteModel> listVote) {
        this.listVote = listVote;
    }
}
