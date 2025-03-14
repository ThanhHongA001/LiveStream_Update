package com.example.livestream_update.Ringme.LiveStream.model;

import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.TagRingMe;
import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.ApplicationController;

import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveStreamMessage implements Serializable {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SAY_HI = 1;
    public static final int TYPE_FRIEND_WATCH = 2;
    public static final int TYPE_LIKE_COMMENT = 3;
    public static final int TYPE_LIKE_VIDEO = 3;
    public static final int TYPE_FOLLOW_CHANNEL = 4;
    public static final int TYPE_COUNT_LIVE = 5;
    public static final int TYPE_UNLIKE_COMMENT = 6;
    public static final int TYPE_UNSUBSCRIBE = 7;
    public static final int LEVEL_MESSAGE_1 = 1;
    public static final int LEVEL_MESSAGE_2 = 2;
    private static final String TAG = LiveStreamMessage.class.getSimpleName();
    @SerializedName("idcmt")
    private String id;

    @SerializedName("rowId")
    private String rowId;

    @SerializedName("idRep")
    private String idRep;

    @SerializedName("type")
    private int type;

    @SerializedName("timestamp")
    private long timeStamp;

    @SerializedName("timeServer")
    private long timeServer;

    @SerializedName("isLike")
    private int isLike;

    @SerializedName("countLike")
    private long countLike;

    @SerializedName("levelMessage")
    private int levelMessage;

    @SerializedName("msisdn")
    private String msisdn;

    @SerializedName("from")
    private String nameSender;

    @SerializedName("avatar")
    private String lastAvatar;

    @SerializedName("quotedMessage")
    private LiveStreamMessage quotedMessage;

    @SerializedName("message")
    private String content;

    @SerializedName("roomId")
    private String roomId;

    @SerializedName("tags")
    private String tags;

    private ArrayList<TagRingMe> listTag = new ArrayList<>();

    private boolean getContactPhoneDone;
    private PhoneNumber phoneNumber;
    private Video currentVideo;
    private boolean enableHi = true;
    private boolean enableHello = true;
    private boolean enableHiName = true;

    public LiveStreamMessage() {
    }

    public static LiveStreamMessage createMyLiveStreamMessage(int type, String content, String roomId, LiveStreamMessage quotedMessage, ApplicationController app) {
        LiveStreamMessage liveStreamMessage = new LiveStreamMessage();
        liveStreamMessage.setType(type);
        content = TextHelper.getInstant().filterSensitiveWords(content);
        liveStreamMessage.setContent(content);
        liveStreamMessage.setRoomId(roomId);
        liveStreamMessage.setMsisdn(app.getJidNumber());
        liveStreamMessage.setLastAvatar(app.getLastChangeAvatar());
        liveStreamMessage.setQuotedMessage(quotedMessage);
        liveStreamMessage.setNameSender(app.getUserName());
        liveStreamMessage.setTimeStamp(System.currentTimeMillis());

        return liveStreamMessage;
    }

    private static ArrayList<TagRingMe> parseListTag(String textTag) {
        ArrayList<TagRingMe> tagList = new ArrayList<>();
        if (Utilities.notEmpty(textTag)) {
            try {
                JSONObject jsonObject = new JSONObject(textTag);
                JSONArray jsonArray = jsonObject.optJSONArray("tags");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonTmp = jsonArray.optJSONObject(i);
                    if (jsonTmp != null) {
                        String tagName = jsonTmp.optString("tag_name");
                        String name = jsonTmp.optString("name");
                        String msisdn = jsonTmp.optString("msisdn");
                        TagRingMe tagRingMe = new TagRingMe(tagName, name, msisdn);
                        tagList.add(tagRingMe);
                    }
                }
            } catch (Exception e) {
            }
        }
        return tagList;
    }

    public static LiveStreamMessage clone(LiveStreamMessage message) {
        LiveStreamMessage liveStreamMessage = new LiveStreamMessage();
        liveStreamMessage.setId(message.getId());
        liveStreamMessage.setType(message.getType());
        liveStreamMessage.setContent(message.getContent());
        liveStreamMessage.setRoomId(message.getRoomId());
        liveStreamMessage.setMsisdn(message.getMsisdn());
        liveStreamMessage.setLastAvatar(message.getLastAvatar());
        liveStreamMessage.setQuotedMessage(message.getQuotedMessage());
        liveStreamMessage.setNameSender(message.getNameSender());
        liveStreamMessage.setTimeStamp(message.getTimeStamp());
        liveStreamMessage.setTimeServer(message.getTimeServer());
        liveStreamMessage.setLike(message.isLike);
        liveStreamMessage.setCountLike(message.getCountLike());
        liveStreamMessage.setTags(message.getTags());
        liveStreamMessage.setListTag(message.getListTag());
        liveStreamMessage.setLevelMessage(message.getLevelMessage());
        liveStreamMessage.setRowId(message.getRowId());

        return liveStreamMessage;
    }

    public Video getCurrentVideo() {
        return currentVideo;
    }

    public void setCurrentVideo(Video currentVideo) {
        this.currentVideo = currentVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getIdRep() {
        return idRep;
    }

    public void setIdRep(String idRep) {
        this.idRep = idRep;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimeStamp() {
        if (timeStamp == 0) timeStamp = System.currentTimeMillis();
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeServer() {
        return timeServer;
    }

    public void setTimeServer(long timeServer) {
        this.timeServer = timeServer;
    }

    public boolean isLike() {
        return isLike == 1;
    }

    public void setLike(int like) {
        isLike = like;
    }

    public long getCountLike() {
        return countLike;
    }

    public void setCountLike(long countLike) {
        this.countLike = countLike;
    }

    public int getLevelMessage() {
        if (levelMessage > 1) return LEVEL_MESSAGE_2;
        return LEVEL_MESSAGE_1;
    }

    public void setLevelMessage(int levelMessage) {
        this.levelMessage = levelMessage;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String name) {
        this.nameSender = name;
    }

    public String getLastAvatar() {
        return lastAvatar;
    }

    public void setLastAvatar(String lastAvatar) {
        this.lastAvatar = lastAvatar;
    }

    public LiveStreamMessage getQuotedMessage() {
        return quotedMessage;
    }

    public void setQuotedMessage(LiveStreamMessage quotedMessage) {
        this.quotedMessage = quotedMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isGetContactPhoneDone() {
        return getContactPhoneDone;
    }

    public void setGetContactPhoneDone(boolean getContactPhoneDone) {
        this.getContactPhoneDone = getContactPhoneDone;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<TagRingMe> getListTag() {
        return parseListTag(tags);
    }

    public void setListTag(ArrayList<TagRingMe> listTag) {
        this.listTag = listTag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String textTag) {
        this.tags = textTag;
    }

    public boolean isEnableHi() {
        return enableHi;
    }

    public void setEnableHi(boolean enableHi) {
        this.enableHi = enableHi;
    }

    public boolean isEnableHello() {
        return enableHello;
    }

    public void setEnableHello(boolean enableHello) {
        this.enableHello = enableHello;
    }

    public boolean isEnableHiName() {
        return enableHiName;
    }

    public void setEnableHiName(boolean enableHiName) {
        this.enableHiName = enableHiName;
    }
}
