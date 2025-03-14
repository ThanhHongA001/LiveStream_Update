package com.example.livestream_update.Ringme.Model.tab_video;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vtm.R;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.model.ChannelOnMedia;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuanha00 on 3/8/2018.
 */

public class Video implements Serializable{
    private static final int DONT_SHOW_ADS = 0;
    private static final int SHOW_ADS_WITH_INDEX = 1;
    private static final int MUST_SHOW_ADS = 2;
    private static final long serialVersionUID = -3L;

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("videoTitle")
    @Expose
    private String title = "";
    @SerializedName("videoDesc")
    @Expose
    private String description = "";
    @SerializedName("videoImage")
    @Expose
    private String imagePath = "";
    @SerializedName("videoMedia")
    @Expose
    private String originalPath = "";
    @SerializedName("imageSmall")
    @Expose
    private String image_path_small = "";
    @SerializedName("imageThumb")
    @Expose
    private String image_path_thump = "";
    @SerializedName("videoTime")
    @Expose
    private String duration;
    @SerializedName("actived")
    @Expose
    private int actived;
    @SerializedName("totalLikes")
    @Expose
    private long totalLike = 0;
    @SerializedName("totalShares")
    @Expose
    private long totalShare = 0;
    @SerializedName("totalComments")
    @Expose
    private long totalComment = 0;
    @SerializedName("totalViews")
    @Expose
    private long totalView = 0;
    @SerializedName("publishTime")
    @Expose
    private long publishTime = 0;

    @SerializedName("aspecRatio")
    @Expose
    private double aspectRatio = 0;
    @SerializedName("isLike")
    @Expose
    private int isLiked;

    @SerializedName("url")
    @Expose
    private String link = "";
    @SerializedName("isShort")
    @Expose
    private int isShort = 0;
    @SerializedName("is_paid")
    @Expose
    private final int isPaid = 0;

    public int getIsShort() {
        return isShort;
    }

    public void setIsShort(int isShort) {
        this.isShort = isShort;
    }

    private long timeCreate = 0;
    private String sourceName = "";
    private int originalPathId = 0;
    private String posterPath = "";
    private String videoType = "";
    private String chapter = "";
    private String url_ADS;
    private String url_ADS_End;
    private String campaign = "";
    private String sourceType = "";
    private String fromSource = "";
    private String categoryText = "";
    private String categoryId = "";
    private String recommendType = "";
    private String subTitleUrl = "";
    private int isPrivate;

    private boolean share = false;
    private boolean playing = false;
    private boolean isSave = false;
    private boolean isWatchLater = false;
    private boolean isCallLogEnd = false;
    private boolean collapse = true;
    private boolean isShowDescription = false;

    private boolean log5 = false;
    private boolean log10 = false;
    private boolean log15 = false;
    private boolean log30 = false;
    private boolean isSeek = false;
    private boolean codecNeedsSetOutputSurfaceWorkaround = false;

    private String timeLog = "";
    private String lagArr = "";
    private String playArr = "";
    private String bandwidthArr = "";
    private String networkArr = "";
    private String errorDes = "";
    private String stateLog = "";
    private String surfaceName = "";
    private String adsInfo = "";

    private long totalTimePlay = 0;
    private long timeCurrent = 0;
    private long timeDuration = 0;
    private int resumeWindow = 0;

    private float volume = 0;

    private int itemStatus = Status.APPROVED.VALUE;

    private int thumbnail = R.drawable.rm_error;

    private int offset = 0;
    private int limit = 0;
    private String lastId = "";
    private String imdb = "";

    private boolean fromOnMedia = false;
    private boolean isPause = false;// trạng thái của player với item này

    private boolean isMovie = false;
    private boolean isLive = false;
    private int indexQuality = 0;
    private int showAds = 0;
    /*
    Với showAds =2, thì luôn hiện QC
    Với showAds =1, hiện QC theo luật
    Với showAds =0, Không hiển thị QC
    */
    private String logo;
    private int logoPosition; //1: top - left; 2: top - right; 3: bottom - right; 4: bottom - left
    private String startMediaUrl = "";

    @SerializedName("channel")
    @Expose
    private Channel channel = new Channel();
    private Category category = new Category();


    @SerializedName("list_resolution")
    @Expose
    private ArrayList<Resolution> list_resolution = new ArrayList<>();


    private Map<String, String> info = new HashMap<>();

    private ArrayList<AdSense> listAds;
    private String country = "";
    private String queryRecommendation = "";
    private long startLiveTime;
    private long endLiveTime;

    private String isSubtitle;
    private String isNarrative;
    private int totalEpisodes = 0;
    private int currentEpisode = 0;
    private boolean decryptVideoPath5dMax = false;
    private boolean isCheckTriangle= false;
    private boolean isConvertFromMovieWatched = false;
    private String timeWatched = ""; // thời lượng đã xem
    private boolean isPlayMyChannel;



    private LivestreamModel livestream;

    public static Video convertLivestreamToVideo(LivestreamModel live) {
        Video video = new Video();
        video.setId(live.getVideoId());
        video.setTitle(live.getTitle());
        video.setOriginalPath(live.getHlsPlaylink());
        video.setLink("https://kakoak.tls.tl/video/" + live.getVideoId());
        video.setImagePath(live.getLinkAvatar());
        video.setDescription(live.getDescription());
        video.setTotalView(live.getTotalView());
        video.setTotalLike(live.getTotalLike());
        video.setTotalComment(live.getTotalComment());
        video.setTimeCreate(live.getTimeStart());
        video.setLike(live.isLike());
        live.getAdaptiveLinks().setLinkSrc(live.getHlsPlaylink());
        video.setListResolution(live.getAdaptiveLinks().setListResolution());
        video.setIndexQuality(live.getIndexQuality());
        video.setChannel(live.getChannel());
        video.setLive(live.getStatus() == 1);
        video.setLivestream(live);
        video.setAspectRatio(16/9d);
        return video;
    }

    public static Channel getChannel(ChannelOnMedia channelOnMedia) {
        if (channelOnMedia == null) return null;
        Channel channel = new Channel();
        channel.setId(channelOnMedia.getId());
        channel.setName(channelOnMedia.getName());
        channel.setUrlImage(channelOnMedia.getAvatarUrl());
        channel.setUrlImageCover(channelOnMedia.getCoverUrl());
        channel.setFollow(channelOnMedia.isFollow());
        channel.setMyChannel(channelOnMedia.isMyChannel() ? 1 : 0);
        channel.setNumFollow(channelOnMedia.getNumFollow());
        channel.setHasFilmGroup(channelOnMedia.getHasFilmGroup());
        channel.setCreatedDate(channelOnMedia.getCreatedDate());
        channel.setTypeChannel(channelOnMedia.getType());
        channel.setTypeChannel(Channel.TypeChanel.TYPE_DEFAULT.VALUE);
        return channel;
    }



    public boolean isDecryptVideoPath5dMax() {
        return decryptVideoPath5dMax;
    }

    public void setDecryptVideoPath5dMax(boolean decryptVideoPath5dMax) {
        this.decryptVideoPath5dMax = decryptVideoPath5dMax;
    }

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

    public String getDuration() {
        try {
            return Utilities.milliSecondsToTimer(Long.parseLong(duration) * 1000);
        } catch (Exception ex) {
            return duration;
        }
    }

    public boolean isCheckTriangle() {
        return isCheckTriangle;
    }

    public void setCheckTriangle(boolean checkTriangle) {
        isCheckTriangle = checkTriangle;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String original_path) {
        this.originalPath = original_path;
    }

    public int getOriginalPathId() {
        return originalPathId;
    }

    public void setOriginalPathId(int originalPathId) {
        this.originalPathId = originalPathId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String image_path) {
        this.imagePath = image_path;
    }

    public String getImage_path_small() {
        return image_path_small;
    }

    public void setImage_path_small(String image_path_small) {
        this.image_path_small = image_path_small;
    }

    public String getImage_path_thump() {
        return image_path_thump;
    }

    public void setImage_path_thump(String image_path_thump) {
        this.image_path_thump = image_path_thump;
    }

    public String getPosterPath() {
        if (!TextUtils.isEmpty(posterPath))
            return posterPath;
        return "";
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public long getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(long total_like) {
        this.totalLike = total_like;
    }

    public long getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(long total_share) {
        this.totalShare = total_share;
    }

    public long getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(long total_comment) {
        this.totalComment = total_comment;
    }

    public long getTotalView() {
        return totalView;
    }

    public void setTotalView(long totalView) {
        this.totalView = totalView;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isLike() {
        return isLiked == 1;
    }

    public void setLike(boolean like) {
        isLiked = like ? 1 : 0;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public long getTimeCurrent() {
        return timeCurrent;
    }

    public void setTimeCurrent(long timeCurrent) {
        this.timeCurrent = timeCurrent;
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getResumeWindow() {
        return resumeWindow;
    }

    public void setResumeWindow(int resumeWindow) {
        this.resumeWindow = resumeWindow;
    }

    public boolean isFromOnMedia() {
        return fromOnMedia;
    }

    public void setFromOnMedia(boolean fromOnMedia) {
        this.fromOnMedia = fromOnMedia;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isWatchLater() {
        return isWatchLater;
    }

    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }

    public boolean isCallLogEnd() {
        return isCallLogEnd;
    }

    public void setCallLogEnd(boolean callLogEnd) {
        isCallLogEnd = callLogEnd;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isShowDescription() {
        return isShowDescription;
    }

    public void setShowDescription(boolean showDescription) {
        isShowDescription = showDescription;
    }

    public ArrayList<Resolution> getListResolution() {
        if (list_resolution == null) list_resolution = new ArrayList<>();
        return list_resolution;
    }

    public void setListResolution(ArrayList<Resolution> list_resolution) {
        this.list_resolution = list_resolution;
    }

    public boolean isHasListResolution() {
        return list_resolution != null && list_resolution.size() > 1;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public boolean isLog5() {
        return log5;
    }

    public void setLog5(boolean log5) {
        this.log5 = log5;
    }

    public boolean isLog10() {
        return log10;
    }

    public void setLog10(boolean log10) {
        this.log10 = log10;
    }

    public boolean isLog15() {
        return log15;
    }

    public void setLog15(boolean log15) {
        this.log15 = log15;
    }

    public boolean isLog30() {
        return log30;
    }

    public void setLog30(boolean log30) {
        this.log30 = log30;
    }

    public String getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(String timeLog) {
        this.timeLog = timeLog;
    }

    public String getLagArr() {
        return lagArr;
    }

    public void setLagArr(String lagArr) {
        this.lagArr = lagArr;
    }

    public String getPlayArr() {
        return playArr;
    }

    public void setPlayArr(String playArr) {
        this.playArr = playArr;
    }

    public String getBandwidthArr() {
        return bandwidthArr;
    }

    public void setBandwidthArr(String bandwidthArr) {
        this.bandwidthArr = bandwidthArr;
    }

    public String getNetworkArr() {
        return networkArr;
    }

    public void setNetworkArr(String networkArr) {
        this.networkArr = networkArr;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }

    public String getStateLog() {
        return stateLog;
    }

    public void setStateLog(String stateLog) {
        this.stateLog = stateLog;
    }

    public boolean isSeek() {
        return isSeek;
    }

    public void setSeek(boolean seek) {
        isSeek = seek;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        if (itemStatus <= 0) return;
        this.itemStatus = itemStatus;
    }

    public String getUrlAds() {
        if (TextUtils.isEmpty(url_ADS)) return "";
        return url_ADS;
    }

    public void setUrlAds(String url_ADS) {
        this.url_ADS = url_ADS;
    }

    public String getUrlAdsEnd() {
        return url_ADS_End;
    }

    public void setUrlAdsEnd(String url_ADS_End) {
        this.url_ADS_End = url_ADS_End;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getAdsInfo() {
        return adsInfo;
    }

    public void setAdsInfo(String adsInfo) {
        this.adsInfo = adsInfo;
    }

    public int getIndexQuality() {
        return indexQuality;
    }

    public void setIndexQuality(int indexQuality) {
        this.indexQuality = indexQuality;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getSurfaceName() {
        return surfaceName;
    }

    public void setSurfaceName(String surfaceName) {
        this.surfaceName = surfaceName;
    }

    public String getSourceType() {
        if (TextUtils.isEmpty(sourceType)) return "";
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getFromSource() {
        if (TextUtils.isEmpty(fromSource)) return Constants.LOG_SOURCE_TYPE.TYPE_KAKOAK_VIDEO;
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public boolean isCodecNeedsSetOutputSurfaceWorkaround() {
        return codecNeedsSetOutputSurfaceWorkaround;
    }

    public void setCodecNeedsSetOutputSurfaceWorkaround(boolean codecNeedsSetOutputSurfaceWorkaround) {
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public void resetParam() {
        log5 = false;
        log15 = false;
        log10 = false;
        log30 = false;
        isSeek = false;
        volume = 0;
        surfaceName = "";
        codecNeedsSetOutputSurfaceWorkaround = false;
    }

    public String getChannelName() {
        if (channel != null && channel.getName() != null) return channel.getName();
        return "";
    }
    public String getChannelId() {
        if (channel != null && channel.getId() != null) return channel.getId();
        return "";
    }
    public String getChannelAvatar() {
        if (channel != null && channel.getId() != null) return channel.getId();
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Utilities.equals(id, video.id);
    }

    public Video clone() {
        try {
            Video video = new Video();
            video.id = id;
            video.title = title;
            if (!TextUtils.isEmpty(description) && !TextUtils.isEmpty(sourceName)) {
                video.description = description + "\n" + sourceName;
            } else {
                video.description = description;
            }
            video.duration = duration;
            video.originalPath = originalPath;
            video.imagePath = imagePath;
            video.link = link;
            video.aspectRatio = aspectRatio;
            video.totalLike = totalLike;
            video.totalShare = totalShare;
            video.totalComment = totalComment;
            video.totalView = totalView;
            video.isLiked = isLiked;
            video.share = share;
            video.playing = playing;
            video.isSave = isSave;
            video.isWatchLater = isWatchLater;
            video.log5 = log5;
            video.log10 = log10;
            video.log15 = log15;
            video.log30 = log30;
            video.isSeek = isSeek;
            video.timeLog = timeLog;
            video.lagArr = lagArr;
            video.playArr = playArr;
            video.bandwidthArr = bandwidthArr;
            video.errorDes = errorDes;
            video.stateLog = stateLog;
            video.timeCurrent = timeCurrent;
            video.timeDuration = timeDuration;
            video.resumeWindow = resumeWindow;
            video.volume = volume;
            video.itemStatus = itemStatus;
            video.thumbnail = thumbnail;
            video.offset = offset;
            video.limit = limit;
            video.timeCreate = timeCreate;
            video.lastId = lastId;
            video.fromOnMedia = fromOnMedia;
            video.categoryId = categoryId;
            video.recommendType = recommendType;
            video.list_resolution = list_resolution;
            video.indexQuality = indexQuality;
            video.surfaceName = surfaceName;
            video.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround;
            video.chapter = chapter;
            video.url_ADS = url_ADS;
            video.sourceType = sourceType;
            video.url_ADS_End = url_ADS_End;
            video.campaign = campaign;
            video.isPrivate = isPrivate;
            video.isMovie = isMovie;
            video.imdb = imdb;
            video.isPause = isPause;
            video.isLive = isLive;

            video.originalPathId = originalPathId;
            video.posterPath = posterPath;
            video.image_path_small = image_path_small;
            video.image_path_thump = image_path_thump;
            video.videoType = videoType;
            video.fromSource = fromSource;
            video.category = category;
            video.isCallLogEnd = isCallLogEnd;
            video.collapse = collapse;
            video.isShowDescription = isShowDescription;
            video.networkArr = networkArr;
            video.adsInfo = adsInfo;
            video.listAds = listAds;
            video.showAds = showAds;
            video.logo = logo;
            video.logoPosition = logoPosition;
            video.publishTime = publishTime;

            if (channel != null)
                video.channel = channel.clone();
            else
                video.channel = new Channel();
            video.startLiveTime = startLiveTime;
            video.endLiveTime = endLiveTime;
            return video;
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", originalPath='" + originalPath + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", link='" + link + '\'' +
                ", videoType='" + videoType + '\'' +
                ", aspectRatio=" + aspectRatio +
                ", totalLike=" + totalLike +
                ", totalShare=" + totalShare +
                ", totalComment=" + totalComment +
                ", totalView=" + totalView +
                ", share=" + share +
                ", playing=" + playing +
                ", isSave=" + isSave +
                ", isWatchLater=" + isWatchLater +
                ", log5=" + log5 +
                ", log10=" + log10 +
                ", log15=" + log15 +
                ", log30=" + log30 +
                ", isSeek=" + isSeek +
                ", timeLog='" + timeLog + '\'' +
                ", lagArr='" + lagArr + '\'' +
                ", playArr='" + playArr + '\'' +
                ", errorDes='" + errorDes + '\'' +
                ", stateLog='" + stateLog + '\'' +
                ", timeCurrent=" + timeCurrent +
                ", timeDuration=" + timeDuration +
                ", resumeWindow=" + resumeWindow +
                ", volume=" + volume +
                ", itemStatus=" + itemStatus +
                ", thumbnail=" + thumbnail +
                ", offset=" + offset +
                ", limit=" + limit +
                ", timeCreate='" + timeCreate + '\'' +
                ", lastId='" + lastId + '\'' +
                ", fromOnMedia=" + fromOnMedia +
                ", isPause=" + isPause +
                ", channel=" + channel +
                ", surfaceName=" + surfaceName +
                ", codecNeedsSetOutputSurfaceWorkaround=" + codecNeedsSetOutputSurfaceWorkaround +
                ", chapter=" + chapter +
                ", campaign=" + campaign +
                ", isPrivate=" + isPrivate +
                ", isLive=" + isLive +
                ", listAds=" + listAds +
                '}';
    }

    public String getMediaLink() {
        String url = originalPath;
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("?")) {
                url += "&v=" + BuildConfig.VERSION_CODE;
                if (!url.contains("&rt=")) {
                    url += "&rt=CP";
                } else if (url.contains("&rt=WP")) {
                    url = url.replace("&rt=WP", "&rt=CP");
                } else if (url.contains("&rt=P")) {
                    url = url.replace("&rt=P", "&rt=CP");
                }
            } else {
                url += "?rt=CP";
                url += "&v=" + BuildConfig.VERSION_CODE;
            }
        }
        return url;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean movie) {
        isMovie = movie;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        if (category != null) return category.getName();
        return categoryText;
    }

    public String getCategoryId() {
        if (category != null) return category.getId();
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public ArrayList<AdSense> getListAds() {
        if (listAds == null) listAds = new ArrayList<>();
        return listAds;
    }

    public void setListAds(ArrayList<AdSense> listAds) {
        this.listAds = listAds;
    }

    public int getShowAds() {
        return showAds;
    }

    public void setShowAds(int showAds) {
        this.showAds = showAds;
    }

    public boolean isMustShowAds() {
        return showAds == MUST_SHOW_ADS;
    }

    public boolean isShowAdsWithIndex() {
        return showAds == SHOW_ADS_WITH_INDEX;
    }

    public boolean isDontShowAds() {
        return showAds == DONT_SHOW_ADS;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLogoPosition() {
        return logoPosition;
    }

    public void setLogoPosition(int logoPosition) {
        this.logoPosition = logoPosition;
    }

    public boolean isLiveEnd() {
        return isLive && TextUtils.isEmpty(originalPath);
    }

    public boolean canShowLogo() {
        return (logoPosition == 1 || logoPosition == 2 || logoPosition == 3 || logoPosition == 4) && Utilities.notEmpty(logo);
    }

    public boolean isVideoLandscape() {
        return aspectRatio >= 1 || aspectRatio == 0;
    }


    public String getStartMediaUrl() {
        if (TextUtils.isEmpty(startMediaUrl)) startMediaUrl = originalPath;
        return startMediaUrl;
    }

    public void setStartMediaUrl(String startMediaUrl) {
        this.startMediaUrl = startMediaUrl;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getQueryRecommendation() {
        return queryRecommendation;
    }

    public void setQueryRecommendation(String queryRecommendation) {
        this.queryRecommendation = queryRecommendation;
    }

    public boolean isSubtitleFilm() {
        return false;
        //return "1".equals(isSubtitle);
    }

    public void setSubtitle(String isSubtitle) {
        this.isSubtitle = isSubtitle;
    }

    public boolean isNarrativeFilm() {
        return "1".equals(isNarrative);
    }

    public void setNarrative(String isNarrative) {
        this.isNarrative = isNarrative;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getDurationMinutes() {
        try {
            int tmp = Integer.parseInt(duration);
            return tmp / 60;
        } catch (Exception e) {
        }
        return 0;
    }

    public long getStartLiveTime() {
        return startLiveTime;
    }

    public void setStartLiveTime(long startLiveTime) {
        this.startLiveTime = startLiveTime;
    }

    public long getEndLiveTime() {
        return endLiveTime;
    }

    public void setEndLiveTime(long endLiveTime) {
        this.endLiveTime = endLiveTime;
    }


    public String getSubTitleUrl() {
        return subTitleUrl;
    }

    public void setSubTitleUrl(String subTitleUrl) {
        this.subTitleUrl = subTitleUrl;
    }

    public boolean isConvertFromMovieWatched() {
        return isConvertFromMovieWatched;
    }

    public void setConvertFromMovieWatched(boolean convertFromMovieWatched) {
        isConvertFromMovieWatched = convertFromMovieWatched;
    }

    public String getTimeWatched() {
        return timeWatched;
    }

    public void setTimeWatched(String timeWatched) {
        this.timeWatched = timeWatched;
    }

    public long getTotalTimePlay() {
        return totalTimePlay;
    }

    public void setTotalTimePlay(long totalTimePlay) {
        this.totalTimePlay = totalTimePlay;
    }

    public LivestreamModel getLivestream() {
        return livestream;
    }

    public void setLivestream(LivestreamModel livestream) {
        this.livestream = livestream;
    }

    public boolean isPaid() {
        return isPaid == 1;
    }

    public boolean isPlayMyChannel() {
        return isPlayMyChannel;
    }

    public void setPlayMyChannel(boolean playMyChannel) {
        isPlayMyChannel = playMyChannel;
    }

    /**
     * trả vể trạng thái của video, phê duyệt, hay chưa phê duyệt
     */
    public enum Status {
        NOT_APPROVED(1),// chưa phê duyệt
        APPROVED(2);// đã phê duyệt

        public int VALUE;

        Status(int value) {
            this.VALUE = value;
        }
    }

}
