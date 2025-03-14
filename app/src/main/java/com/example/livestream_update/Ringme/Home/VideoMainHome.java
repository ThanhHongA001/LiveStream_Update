package com.example.livestream_update.Ringme.Home;


import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.model.VideoMainHomeChannel;
import com.vtm.ringme.model.livestream.ListResolution;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;

import java.io.Serializable;
import java.util.List;

public class VideoMainHome implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("cateId")
    private int cateId;
    @SerializedName("channelId")
    private int channelId;
    @SerializedName("videoTitle")
    private String videoTitle;
    @SerializedName("videoDesc")
    private String videoDesc;
    @SerializedName("videoMedia")
    private String videoMedia;
    @SerializedName("videoImage")
    private String videoImage;
    @SerializedName("imageThumb")
    private String imageThumb;
    @SerializedName("imageSmall")
    private String imageSmall;
    @SerializedName("videoTime")
    private String videoTime;
    @SerializedName("publishTime")
    private long publishTime;
    @SerializedName("totalViews")
    private int totalViews;
    @SerializedName("totalLikes")
    private int totalLikes;
    @SerializedName("totalShares")
    private int totalShares;
    @SerializedName("totalComments")
    private int totalComments;
    @SerializedName("resolution")
    private int resolution;
    @SerializedName("aspecRatio")
    private String aspecRatio;
    @SerializedName("isAdaptive")
    private int isAdaptive;
    @SerializedName("isLike")
    private int isLike;
    @SerializedName("status")
    private int status;
    @SerializedName("channel")
    private VideoMainHomeChannel channel;
    @SerializedName("url")
    private String url;
    @SerializedName("hashId")
    private String hashId;
    @SerializedName("list_resolution")
    private List<ListResolution> listResolution;
    private boolean isLive;
    private LivestreamModel livestreamModel;

    public VideoMainHome() {
    }

    public VideoMainHome(int id, String imageThumb, int totalViews) {
        this.id = id;
        this.imageThumb = imageThumb;
        this.totalViews = totalViews;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getVideoMedia() {
        return videoMedia;
    }

    public void setVideoMedia(String videoMedia) {
        this.videoMedia = videoMedia;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getAspecRatio() {
        return aspecRatio;
    }

    public void setAspecRatio(String aspecRatio) {
        this.aspecRatio = aspecRatio;
    }

    public int getIsAdaptive() {
        return isAdaptive;
    }

    public void setIsAdaptive(int isAdaptive) {
        this.isAdaptive = isAdaptive;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public VideoMainHomeChannel getChannel() {
        return channel;
    }

    public void setChannel(VideoMainHomeChannel channel) {
        this.channel = channel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public List<ListResolution> getListResolution() {
        return listResolution;
    }

    public void setListResolution(List<ListResolution> listResolution) {
        this.listResolution = listResolution;
    }

    public static VideoMainHome convertLivestreamToVideo(LivestreamModel live) {
        VideoMainHome video = new VideoMainHome();
        video.setId(Integer.parseInt(live.getVideoId()));
        video.setVideoTitle(live.getTitle());
        video.setVideoMedia(live.getHlsPlaylink());
        video.setVideoImage(live.getLinkAvatar());
        video.setUrl("https://kakoak.tls.tl/video/" + live.getVideoId());
        live.getAdaptiveLinks().setLinkSrc(live.getHlsPlaylink());
        video.setLive(live.getStatus() == 1);
        video.setLiveStreamModel(live);
        video.setChannel(new VideoMainHomeChannel(Integer.parseInt(live.getChannel().getId()),
                live.getChannel().getName(), live.getChannel().getUrlImage(), (int) live.getChannel().getNumFollow()));
        return video;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public LivestreamModel getLiveStreamModel() {
        return livestreamModel;
    }

    public void setLiveStreamModel(LivestreamModel live) {
        this.livestreamModel = live;
    }

    public static Video convertToVideo(VideoMainHome nVideo) {
        Video video = new Video();
        video.setId(String.valueOf(nVideo.getId()));
        video.setTitle(nVideo.getVideoTitle());
        video.setDescription(nVideo.getVideoDesc());
        video.setImagePath(nVideo.getVideoImage());
        video.setOriginalPath(nVideo.getVideoMedia());
        video.setTotalComment(nVideo.getTotalComments());
        video.setImage_path_small(nVideo.getImageSmall());
        video.setImage_path_thump(nVideo.getImageThumb());
        video.setDuration(nVideo.getVideoTime());
        video.setTotalLike(nVideo.getTotalLikes());
        video.setTotalShare(nVideo.getTotalShares());
        video.setTotalComment(nVideo.getTotalComments());
        video.setTotalView(nVideo.getTotalViews());
        video.setPublishTime(nVideo.getPublishTime());
        video.setLike(nVideo.getIsLike() == 1);
        video.setLink(nVideo.getUrl());
        video.setChannel(Channel.convertFromChannelOnMedia(nVideo.getChannel()));
        return video;
    }

    public static Video convertToVideoLog(VideoMainHome nVideo, String timeLog) {
        Video video = new Video();
        video.setId(String.valueOf(nVideo.getId()));
        video.setTitle(nVideo.getVideoTitle());
        video.setDescription(nVideo.getVideoDesc());
        video.setImagePath(nVideo.getVideoImage());
        video.setOriginalPath(nVideo.getVideoMedia());
        video.setTotalComment(nVideo.getTotalComments());
        video.setImage_path_small(nVideo.getImageSmall());
        video.setImage_path_thump(nVideo.getImageThumb());
        video.setDuration(nVideo.getVideoTime());
        video.setTotalLike(nVideo.getTotalLikes());
        video.setTotalShare(nVideo.getTotalShares());
        video.setTotalComment(nVideo.getTotalComments());
        video.setTotalView(nVideo.getTotalViews());
        video.setPublishTime(nVideo.getPublishTime());
        video.setLike(nVideo.getIsLike() == 1);
        video.setLink(nVideo.getUrl());
        video.setChannel(Channel.convertFromChannelOnMedia(nVideo.getChannel()));
        video.setStateLog("END");
        video.setTimeLog(timeLog);
        return video;
    }
}
