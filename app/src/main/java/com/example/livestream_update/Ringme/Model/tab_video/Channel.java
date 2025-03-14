package com.example.livestream_update.Ringme.Model.tab_video;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vtm.R;
import com.vtm.ringme.model.VideoMainHomeChannel;
import com.vtm.ringme.tabvideo.BaseAdapter;
import com.vtm.ringme.utils.Utilities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tuanha00 on 3/8/2018.
 */

public class Channel implements Serializable, BaseAdapter.Clone {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("channelName")
    @Expose
    private String name = "";
    @SerializedName("channelAvatar")
    @Expose
    private String imageUrl = "";
    @SerializedName("description")
    @Expose
    private String description = "";
    @SerializedName("headerBanner")
    @Expose
    private String imageCoverUrl = "";
    @SerializedName("numFollows")
    @Expose
    private long numfollow = 0;
    @SerializedName("numVideos")
    @Expose
    private int numVideo = 0;
    @SerializedName("isOfficial")
    @Expose
    private int isOfficial;
    @SerializedName("isFollow")
    @Expose
    private int isFollow;
    @SerializedName("createdFrom")
    @Expose
    private long createdDate;

    @Expose
    @SerializedName("isOwner")
    private int isMyChannel;
    @SerializedName("url")
    private String url="";
    @SerializedName("state")
    private String state;
    private String textFollow = "";
    private String packageAndroid = "";
//    private String url = "";
    private long lastPublishVideo = System.currentTimeMillis();
    private boolean haveNewVideo = false;
    private boolean isInstall = false;
    private int hasFilmGroup = 0;

    private int thumbnail = R.drawable.rm_error;

    private int typeChannel = TypeChanel.TYPE_DEFAULT.VALUE;
    private String textTotalPoint = "";
    private ArrayList<Video> videos;

    public static Channel convertFromChannelOnMedia(VideoMainHomeChannel nChannel) {
        if (nChannel == null) return null;
        Channel channel = new Channel();
        channel.setId(String.valueOf(nChannel.getId()));
        channel.setName(nChannel.getChannelName());
        channel.setUrlImage(nChannel.getChannelAvatar());
        channel.setDescription(nChannel.getDescription());
        channel.setUrlImageCover(nChannel.getChannelAvatar());
        channel.setNumFollow(nChannel.getNumFollows());
        channel.setNumVideo(nChannel.getNumVideos());
        channel.setIsOfficial(nChannel.getIsOfficial());
        if (!TextUtils.isEmpty(nChannel.getIsFollow()))
            channel.setFollow(nChannel.getIsFollow().equals("1"));
        channel.setCreatedDate(nChannel.getCreatedFrom());
        channel.setMyChannel(nChannel.getIsOfficial());
        channel.setUrl(nChannel.getUrl());
        nChannel.setState(nChannel.getState());
        channel.setTypeChannel(TypeChanel.TYPE_DEFAULT.VALUE);
        return channel;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return imageUrl;
    }

    public void setUrlImage(String url_images) {
        this.imageUrl = url_images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImageCover() {
        return imageCoverUrl;
    }

    public void setUrlImageCover(String url_images_cover) {
        this.imageCoverUrl = url_images_cover;
    }

    public long getNumfollow() {
        return numfollow;
    }

    public long getNumFollow() {
        return numfollow;
    }

    public void setNumFollow(long numfollow) {
        this.numfollow = numfollow;
        textFollow = Utilities.shortenLongNumber(numfollow);
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public boolean isFollow() {
        return isFollow == 1;
    }

    public void setFollow(boolean follow) {
        this.isFollow = follow ? 1 : 0;
    }

    public String getTextFollow() {
        return textFollow;
    }

    public int getIsOfficial() {
        return isOfficial;
//        return 0;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public int getTypeChannel() {
        return typeChannel;
    }

    public void setTypeChannel(int typeChannel) {
        this.typeChannel = typeChannel;
    }

    public boolean isMyChannel() {
        return isMyChannel == 1;
    }

    public void setMyChannel(int myChannel) {
        isMyChannel = myChannel;
    }

    public String getPackageAndroid() {
        return packageAndroid;
    }

    public void setPackageAndroid(String packageAndroid) {
        this.packageAndroid = packageAndroid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLastPublishVideo() {
        return lastPublishVideo;
    }

    public void setLastPublishVideo(long lastPublishVideo) {
        this.lastPublishVideo = lastPublishVideo;
    }

    public boolean isHaveNewVideo() {
        return haveNewVideo;
    }

    public void setHaveNewVideo(boolean haveNewVideo) {
        this.haveNewVideo = haveNewVideo;
    }

    public boolean isInstall() {
        return isInstall;
    }

    public void setInstall(boolean install) {
        isInstall = install;
    }

    public int getHasFilmGroup() {
        return hasFilmGroup;
    }

    public void setHasFilmGroup(int hasFilmGroup) {
        this.hasFilmGroup = hasFilmGroup;
    }

    public int getNumVideo() {
        return numVideo;
    }

    public void setNumVideo(int numVideo) {
        this.numVideo = numVideo;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getTextTotalPoint() {
        return textTotalPoint;
    }

    public void setTextTotalPoint(String textTotalPoint) {
        this.textTotalPoint = textTotalPoint;
    }

    public boolean isHasFilmGroup() {
        return hasFilmGroup == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Utilities.equals(id, channel.id);
    }

    public Channel clone() {
        try {
            Channel channel = new Channel();
            channel.id = id;
            channel.name = name;
            channel.imageUrl = imageUrl;
            channel.description = description;
            channel.imageCoverUrl = imageCoverUrl;
            channel.textFollow = textFollow;
            channel.packageAndroid = packageAndroid;
            channel.numfollow = numfollow;
            channel.isFollow = isFollow;
            channel.isMyChannel = isMyChannel;
            channel.thumbnail = thumbnail;
            channel.typeChannel = typeChannel;
            channel.lastPublishVideo = lastPublishVideo;
            channel.haveNewVideo = haveNewVideo;
            channel.numVideo = numVideo;
            channel.videos = videos;
            channel.textTotalPoint = textTotalPoint;
            return channel;
        } catch (Exception e) {
            return this;
        }
    }

    public enum TypeChanel {
        TYPE_DEFAULT(0),
        RINGME_VIDEO(1),
        UNABLE_SUB(2),
        OPEN_APP(3);
        public int VALUE;

        TypeChanel(int VALUE) {
            this.VALUE = VALUE;
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", imageCoverUrl='" + imageCoverUrl + '\'' +
                ", numfollow=" + numfollow +
                ", numVideo=" + numVideo +
                ", isOfficial=" + isOfficial +
                ", isFollow=" + isFollow +
                ", createdDate=" + createdDate +
                ", isMyChannel=" + isMyChannel +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                ", textFollow='" + textFollow + '\'' +
                ", packageAndroid='" + packageAndroid + '\'' +
                ", lastPublishVideo=" + lastPublishVideo +
                ", haveNewVideo=" + haveNewVideo +
                ", isInstall=" + isInstall +
                ", hasFilmGroup=" + hasFilmGroup +
                ", thumbnail=" + thumbnail +
                ", typeChannel=" + typeChannel +
                ", textTotalPoint='" + textTotalPoint + '\'' +
                ", videos=" + videos +
                '}';
    }
}
