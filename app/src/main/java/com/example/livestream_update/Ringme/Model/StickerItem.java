package com.example.livestream_update.Ringme.Model;




import com.vtm.ringme.values.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by ThanhNT on 12/30/2014.
 */
public class StickerItem implements Serializable{
    private long id;
    private int itemId;
    private int collectionId;
    private String imagePath;
    private String voicePath;
    private long recentTime = -1;
    private String type;
    private boolean isDownloadImg;
    private boolean isDownloadVoice;
    private String urlImg;
    private String urlVoice;
    private boolean isNotActive;
//    private boolean isDownloading;      //dung cho sticker greeting

    public StickerItem() {
        //TODO insert your code
    }

    public StickerItem(int collectionId, int itemId) {
        this.collectionId = collectionId;
        this.itemId = itemId;
    }

    public StickerItem(int collectionId, int itemId, String imagePath, String voicePath) {
        this.collectionId = collectionId;
        this.itemId = itemId;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public long getRecentTime() {
        return recentTime;
    }

    public void setRecentTime(long recentTime) {
        this.recentTime = recentTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDownloadImg() {
        return isDownloadImg;
    }

    public void setDownloadImg(boolean isDownloadImg) {
        this.isDownloadImg = isDownloadImg;
    }

    public boolean isDownloadVoice() {
        return isDownloadVoice;
    }

    public void setDownloadVoice(boolean isDownloadVoice) {
        this.isDownloadVoice = isDownloadVoice;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUrlVoice() {
        return urlVoice;
    }

    public void setUrlVoice(String urlVoice) {
        this.urlVoice = urlVoice;
    }

    /*    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }*/

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + itemId + collectionId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (StickerItem.class != obj.getClass()) {
            return false;
        }
        StickerItem otherItem = (StickerItem) obj;
        return otherItem.getCollectionId() == collectionId &&
                otherItem.getItemId() == itemId;
    }

    public void setDataFromJsonObject(JSONObject object, int stickerCollectionId,
                                      String stickerCollectionPath) throws JSONException {
        collectionId = stickerCollectionId;
        itemId = object.getInt(Constants.HTTP.STICKER.STICKER_ITEM_ID);
        type = object.getString(Constants.HTTP.STICKER.STICKER_ITEM_TYPE);
        imagePath = stickerCollectionPath + object.getString(Constants.HTTP.STICKER.STICKER_ITEM_IMAGE);
        voicePath = stickerCollectionPath + object.getString(Constants.HTTP.STICKER.STICKER_ITEM_VOICE);
    }

    public String toString() {
        return "StickerItem: " + "\n localId: " + id + "\n stickerId: " + itemId + " \n type: " +
                type + "\n imagePath: " + imagePath + "\n voicePath: " + voicePath +
                "\n urlImg: " + urlImg + "\n urlVoice: " + urlVoice ;
    }

    //dung cho parce greeting sticker
    public void setDataFromJsonObject(JSONObject object) throws JSONException{
        if(object.has("collectionid")){
            collectionId = object.getInt("collectionid");
        }
        if(object.has("itemid")){
            itemId = object.getInt("itemid");
        }
        if(object.has("type")){
            type = object.getString("type");
        }
        if(object.has("urlvoice")){
            urlVoice = object.getString("urlvoice");
        }
        if(object.has("urlimg")){
            urlImg = object.getString("urlimg");
        }
    }
}
