package com.example.livestream_update.Ringme.Model;


import android.text.TextUtils;

import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.ReengMessageConstant;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by thaodv on 6/30/2014.
 * Bây giờ thì như 1 đống rác (09/13/2016)
 * Bây giờ thì là 1 bãi rác rồi =)) (11/21/2018 - thanhnt)
 */
public class ReengMessage implements Serializable, Cloneable {
    private static final String TAG = ReengMessage.class.getSimpleName();
    public int size;        //Voi msg la music, size =1 thi dung holder moi. msg la Sticker, size =1 la da play o
    // QuickReply se ko play o Thread nua
    private int id;
    private String content = "";
    private int threadId;
    private int readState;
    private String sender = "";
    private String receiver = "";
    private long time;
    private boolean isSent;
    private String fileType = ""; // Type of message: voicemail, image or doc
    private String packetId = "";
    private int status = 0; // 0: don't send, 1: send to server, 2: delivery
    private boolean isChecked = false;
    private ReengMessageConstant.MessageType messageType = ReengMessageConstant.MessageType.notification;// default
    private int duration;
    // name of voicemail file (voicemail) or thumbnail file (image), collectionID
    private String fileName;        // luu them event room name
    private long songId;            //stickerID, song id
    private String fileId;          // file id, luu message text co phai large emo ko
    private boolean isPlaying = false;
    private String videoContentUri;
    private String directLinkMedia;
    // path of file, luu them xml array sticker list,voice url voi sticker, latitude,time transfer_money
    private String filePath;        // luu them event room id
    private int chatMode = 0;
    private int loadigProgress = 0; // bien tam luu % download hoac upload ko
    // luu vao database
    private int playingProgressPercentage = 0; // luu % play cua voicemail
    private String date = null;
    private String hour = null;
    private ReengMessageConstant.Direction direction;
    private int musicState;
    //
    //private CountDownInviteMusic mCountDown;
    private boolean counting = false;
    private boolean forwardingMessage = false;
    private String imageUrl;                   //luu them longitude // luu them session id cung nghe, unit_money,
    // event room avatar,image_link_url,json action trong advertise
    //greeting sticker
    private List<StickerItem> listStickerItem;
    private boolean isPlayedGif = true;
    private long timeDelivered = 0L;
    private long timeSeen = 0L;
    private boolean isForceShow = false;
    private boolean isNewMessage = false;
    private Video video;
    private String textTranslated;
    private boolean isShowTranslate;
    private String languageTarget;
    private int sticky;
    private String senderName;
    private String senderAvatar;
    // add by toanvk2 (MENU delete)
    private boolean isMenuDelete = false;
    // reply
    private String replyDetail;

    private long expired = -1;
    private boolean runFirstAnimation = false;
    private int cState = -1;
    private boolean initDeeplink = false;
    //private ArrayList<String> listImage = new ArrayList<>();
    private String tagContent;
    private ArrayList<TagRingMe> listTagContent;
    private String filePathCompress;
    private String dataResponseUpload;
    private boolean isUploading;

    private String tOpr, fOpr;

    private ArrayList<Integer> listReaction;
    private boolean isReactionType = false;

    private String kqiContent;
    private String targetPacketIdE2E;
    private String preKeyTmp;
    private String messageEncrpyt = "";

    private int stateMyReaction = -1;
    private boolean isDateInMonth; // todo kiem tra tin nhan trong 1 thang
    private boolean allowToConvertVideo = true;

    public boolean isAllowToConvertVideo() {
        return allowToConvertVideo;
    }

    public void setAllowToConvertVideo(boolean allowToConvertVideo) {
        this.allowToConvertVideo = allowToConvertVideo;
    }

    /**
     * tao message default. chua co packet id
     */
    public ReengMessage() {
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public ReengMessageConstant.MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ReengMessageConstant.MessageType messageType) {
        this.messageType = messageType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStateEnableE2E(int state) {
        if (state == 1) fileName = "1";
        else fileName = "0";
    }

    public int getStateEnableE2E() {
        return "1".equals(fileName) ? 1 : 0;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public String getSender() {
        if (sender == null) {
            return "";
        }
        return sender;
    }

    /**
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        // TimeHelper.setTimeOfMessage(this);
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean isSent) {
        this.isSent = isSent;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public int getStatus() {
        return status;
    }

    public int getMusicState() {
        return musicState;
    }

    public void setMusicState(int musicState) {
        this.musicState = musicState;
    }

    public void setStatusFromDb(int status) {
        this.status = status;
    }

    public void setStatus(int newStatus) {
        //fail, seen
        if (newStatus == ReengMessageConstant.STATUS_FAIL || newStatus == ReengMessageConstant.STATUS_SEEN) {
            this.status = newStatus;
            return;
        }
        //delivered,sent,loading
        if (status == ReengMessageConstant.STATUS_SEEN ||
                status == ReengMessageConstant.STATUS_DELIVERED ||
                status == ReengMessageConstant.STATUS_CHANGE_SONG) {
            return;
        }
        //neu trang thai hien tai la sent thi ko the chuyen ve trang thai dang gui
        if (status == ReengMessageConstant.STATUS_SENT &&
                (newStatus == ReengMessageConstant.STATUS_NOT_SEND || newStatus == ReengMessageConstant
                        .STATUS_LOADING)) {
            return;
        }
        if (newStatus == ReengMessageConstant.STATUS_DELIVERED && status >= ReengMessageConstant.STATUS_RECEIVED)
            return;
        this.status = newStatus;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * set name of voicemail or image
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(id).
                append(" sender = ").append(sender).
                append(" receiver = ").append(receiver).
                append(" content = ").append(content).
                append(" readState = ").append(readState).
                append(" threadId = ").append(threadId).
                append(" time = ").append(time).
                append(" status = ").append(status).
                append(" packetId = ").append(packetId).
                append(" filePath = ").append(filePath).
                append(" songId = ").append(songId).
                append(" fileName = ").append(fileName).
                append(" fileId = ").append(fileId).
                append(" imageUrl = ").append(imageUrl).
                append(" filesize = ").append(size);
        return sb.toString();
    }

    public boolean getMenuDelete() {
        return isMenuDelete;
    }

    public void setMenuDelete(boolean isMenuDelete) {
        this.isMenuDelete = isMenuDelete;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getChatMode() {
        return chatMode;
    }

    public void setChatMode(int chatMode) {
        this.chatMode = chatMode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getProgress() {
        return loadigProgress;
    }

    public void setProgress(int progress) {
        this.loadigProgress = progress;
    }

    public int getPlayingProgressPercentage() {
        return playingProgressPercentage;
    }

    public void setPlayingProgressPercentage(int playingProgressPercentage) {
        this.playingProgressPercentage = playingProgressPercentage;
    }

    public void setForwardingMessage(boolean forwardingMessage) {
        this.forwardingMessage = forwardingMessage;
    }

    public boolean isForwardingMessage() {
        return forwardingMessage;
    }

    public String getDate() {
        if (TextUtils.isEmpty(date))
            date = TimeHelper.getDateOfMessage(time);
        return date;
    }

    public String getHour() {
        if (TextUtils.isEmpty(hour))
            hour = TimeHelper.getHourOfMessage(time);
        return hour;
    }

    public String getVideoContentUri() {
        return videoContentUri;
    }

    public void setVideoContentUri(String videoContentUri) {
        this.videoContentUri = videoContentUri;
    }

    public ArrayList<String> getListDeliveredMemsers() {
        if (TextUtils.isEmpty(videoContentUri)) {
            return new ArrayList<>();
        }
        String[] numbersArray = videoContentUri.split(";");
        HashSet<String> numbersList = new HashSet<>();
        Collections.addAll(numbersList, numbersArray);
        return new ArrayList<>(numbersList);
    }

    public ReengMessageConstant.Direction getDirection() {
        return direction;
    }

    public void setDirection(ReengMessageConstant.Direction direction) {
        this.direction = direction;
    }

    /*public String getContentVideoURI() {
        return contentVideoURI;
    }

    public void setContentVideoURI(String contentVideoURI) {
        this.contentVideoURI = contentVideoURI;
    }*/

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<StickerItem> getListStickerItem() {
        return listStickerItem;
    }

    public void setListStickerItem(List<StickerItem> listStickerItem) {
        this.listStickerItem = listStickerItem;
    }

    public boolean isPlayedGif() {
        return isPlayedGif;
    }

    public void setPlayedGif(boolean isPlayGif) {
        this.isPlayedGif = isPlayGif;
    }

    public boolean isForceShow() {
        return isForceShow;
    }

    public void setIsForceShow(boolean isForceShow) {
        this.isForceShow = isForceShow;
    }

    public long getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(long timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public long getTimeSeen() {
        return timeSeen;
    }

    public void setTimeSeen(long timeSeen) {
        this.timeSeen = timeSeen;
    }

    public boolean isNewMessage() {
        return isNewMessage;
    }

    public void setNewMessage(boolean isNewMessage) {
        this.isNewMessage = isNewMessage;
    }

    public boolean isCounting() {
        return counting;
    }

    public void setCounting(boolean counting) {
        this.counting = counting;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getRoomInfo() {
        if (!TextUtils.isEmpty(senderName) || !TextUtils.isEmpty(senderAvatar)) {
            try {
                JSONObject object = new JSONObject();
                if (!TextUtils.isEmpty(senderName))
                    object.put("name", senderName);
                if (!TextUtils.isEmpty(senderAvatar))
                    object.put("avatar", senderAvatar);
                return object.toString();
            } catch (Exception e) {
                Log.e(TAG, "JSONException", e);
                return null;
            }
        } else {
            return null;
        }
    }

    public void setRoomInfo(String roomInfo) {
        if (!TextUtils.isEmpty(roomInfo)) {
            try {
                JSONObject object = new JSONObject(roomInfo);
                if (object.has("name"))
                    senderName = object.getString("name");
                if (object.has("avatar"))
                    senderAvatar = object.getString("avatar");
            } catch (Exception e) {
                Log.e(TAG, "JSONException", e);
            }
        }
    }

    public String getTextTranslated() {
        return textTranslated;
    }

    public void setTextTranslated(String textTranslated) {
        this.textTranslated = textTranslated;
    }

    public boolean isShowTranslate() {
        return isShowTranslate;
    }

    public void setShowTranslate(boolean isShowTranslate) {
        this.isShowTranslate = isShowTranslate;
    }

    public String getLanguageTarget() {
        return languageTarget;
    }

    public void setLanguageTarget(String languageTarget) {
        this.languageTarget = languageTarget;
    }

    public void setSticky(int sticky) {
        this.sticky = sticky;
    }

    public boolean isSticky() {
        return (sticky == 1);
    }

    public String getDirectLinkMedia() {
        return directLinkMedia;
    }

    public void setDirectLinkMedia(String directLinkMedia) {
        this.directLinkMedia = directLinkMedia;
    }

    public boolean isTypeSendSeenMessage() {
        if (messageType == ReengMessageConstant.MessageType.shareContact ||
                messageType == ReengMessageConstant.MessageType.voicemail ||
                messageType == ReengMessageConstant.MessageType.text ||
                messageType == ReengMessageConstant.MessageType.voiceSticker ||
                messageType == ReengMessageConstant.MessageType.image ||
                messageType == ReengMessageConstant.MessageType.inviteShareMusic ||
                messageType == ReengMessageConstant.MessageType.shareVideo ||
                messageType == ReengMessageConstant.MessageType.shareLocation ||
                messageType == ReengMessageConstant.MessageType.transferMoney ||
                messageType == ReengMessageConstant.MessageType.crbt_gift ||
                messageType == ReengMessageConstant.MessageType.deep_link ||
                messageType == ReengMessageConstant.MessageType.gift ||
                messageType == ReengMessageConstant.MessageType.fake_mo ||
                messageType == ReengMessageConstant.MessageType.notification_fake_mo ||
                messageType == ReengMessageConstant.MessageType.image_link ||
                messageType == ReengMessageConstant.MessageType.luckywheel_help ||
                messageType == ReengMessageConstant.MessageType.watch_video ||
                messageType == ReengMessageConstant.MessageType.bank_plus) {
            return true;
        }
        return false;
    }

    /**
     * CRBT music
     * lưu thông tin quà tặng crbt vao cac truong co san
     */
    // luu crbt session truong image url
    public void setCrbtGiftSession(String crbtGiftSession) {
        this.imageUrl = crbtGiftSession;
    }

    public String getCrbtGiftSession() {
        return imageUrl;
    }

    /**
     * DeepLink
     * lưu tam thong tin tren mem, sau do convert set vao db truong co san sau
     */
    private String deepLinkLeftLabel, deepLinkLeftAction;
    private String deepLinkRightLabel, deepLinkRightAction;

    public String getDeepLinkLeftLabel() {
        return deepLinkLeftLabel;
    }

    public void setDeepLinkLeftLabel(String deepLinkLeftLabel) {
        this.deepLinkLeftLabel = deepLinkLeftLabel;
    }

    public String getDeepLinkLeftAction() {
        return deepLinkLeftAction;
    }

    public void setDeepLinkLeftAction(String deepLinkLeftAction) {
        this.deepLinkLeftAction = deepLinkLeftAction;
    }

    public String getDeepLinkRightLabel() {
        return deepLinkRightLabel;
    }

    public void setDeepLinkRightLabel(String deepLinkRightLabel) {
        this.deepLinkRightLabel = deepLinkRightLabel;
    }

    public String getDeepLinkRightAction() {
        return deepLinkRightAction;
    }

    public void setDeepLinkRightAction(String deepLinkRightAction) {
        this.deepLinkRightAction = deepLinkRightAction;
    }

    public void setDeepLinkInfo(boolean isNotification) {
        String jsonString = null;
        if (!TextUtils.isEmpty(deepLinkLeftAction)) {// deeplink bắt buộc phải có left action
            try {
                JSONObject object = new JSONObject();
                if (!TextUtils.isEmpty(deepLinkLeftLabel) && !TextUtils.isEmpty(deepLinkLeftAction)) {
                    object.put("leftLabel", deepLinkLeftLabel);
                    object.put("leftAction", deepLinkLeftAction);
                }
                if (!TextUtils.isEmpty(deepLinkRightLabel) && !TextUtils.isEmpty(deepLinkRightAction)) {
                    object.put("rightLabel", deepLinkRightLabel);
                    object.put("rightAction", deepLinkRightAction);
                }
                jsonString = object.toString();
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
                jsonString = null;
            }
        }
        initDeeplink = true;
        if (isNotification) {// message notification deeplink lưu vào trường directLinkMedia, trường imageURL đã dùng
            // cho avatar change group ròi :(
            this.directLinkMedia = jsonString;
        } else {
            this.imageUrl = jsonString;
        }
    }

    public void initDeepLinkInfo(boolean isNotification) {
        // message notification deeplink lưu vào trường directLinkMedia, trường imageURL đã dùng cho avatar change
        // group ròi :(
        String json = isNotification ? directLinkMedia : imageUrl;
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject object = new JSONObject(json);
                if (object.has("leftLabel") && object.has("leftAction")) {
                    deepLinkLeftLabel = object.getString("leftLabel");
                    deepLinkLeftAction = object.getString("leftAction");
                }
                if (object.has("rightLabel") && object.has("rightAction")) {
                    deepLinkRightLabel = object.getString("rightLabel");
                    deepLinkRightAction = object.getString("rightAction");
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
        initDeeplink = true;
    }

    // gift
    public void setGifThumbId(String gifThumb) {
        this.videoContentUri = gifThumb;
    }

    public String getGifThumbId() {
        return videoContentUri;
    }

    public void setGifThumbPath(String gifThumbPath) {
        this.filePath = gifThumbPath;
    }

    public String getGifThumbPath() {
        return filePath;
    }

    public void setGifImgId(String gifImgId) {
        this.fileId = gifImgId;
    }

    public String getGifImgId() {
        return fileId;
    }

    public void setGifImgPath(String gifImgPath) {
        this.imageUrl = gifImgPath;
    }

    public String getGifImgPath() {
        return imageUrl;
    }

    /**
     * song info save json (directLinkMedia)
     */
    public static final int SONG_ID_DEFAULT_NEW = -2;




}