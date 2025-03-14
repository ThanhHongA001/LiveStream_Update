package com.example.livestream_update.Ringme.Model;


import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.utils.Utilities;

import java.io.Serializable;


public class UserInfo implements Serializable {
    private static final long serialVersionUID = -8930895246615031169L;

    public static final int NOT_RINGME_USER = 0;
    public static final int IS_RINGME_USER = 1;

    public static final int USER_ONMEDIA_NORMAL = 0;
    public static final int USER_ONMEDIA_OFFICAL = 2;
    public static final int USER_ONMEDIA_VTF = 3;
    public static final int USER_ONMEDIA_VERIFY = 4;

    @SerializedName("id")
    private String id;

    @SerializedName("msisdn")
    private String msisdn = "";

    @SerializedName("facebook")
    private String facebook = "";

    @SerializedName("birthday")
    private String birthday = "";

    @SerializedName("address")
    private String address = "";

    @SerializedName("email")
    private String email = "";


    @SerializedName("user_type")
    private int user_type;


    @SerializedName("name")
    private String name = "";

    @SerializedName("contact_name")
    private String contact_name;

    @SerializedName("avatar")
    private String avatar = "";

    @SerializedName("image")
    private final String image = "";

    private boolean isChecked = false;

    @SerializedName("share_feed_privacy")
    private int share_feed_privacy;

    @SerializedName("is_vip")
    private int is_vip;

    @SerializedName("image310")
    private String image310;

    @SerializedName("status")
    private String status;

    @SerializedName("session_token")
    private String sessionToken;

    @SerializedName("isMochaUser")
    private int stateRingMe;

    @SerializedName("stamp")
    private long time;

    private String rowIdSuggestFriend;

    private boolean addFriend;

    private String prize;

    public UserInfo() {
    }

    public UserInfo(String msisdn, String name) {
        this.msisdn = msisdn;
        this.name = name;
    }

    public UserInfo(String id, String msisdn, String status) {
        this.id = id;
        this.msisdn = msisdn;
        this.status = status;
    }

    public static final int USER_TYPE_NORMAL = 0;
    public static final int USER_TYPE_KEENG = 1;
    public static final int USER_TYPE_SINGER = 2;
    public static final int USER_TYPE_OTHER = 3;

    public static final int USER_DEACTIVE = 0;
    public static final int USER_OFFLINE = 1;
    public static final int USER_ONLINE = 2;
    public static final int USER_INVISIBLE = 3;

    public boolean isVip() {
        return is_vip != 0;
    }

    public int getVIP() {
        return is_vip;
    }

    public boolean isOffical() {
        return user_type != USER_TYPE_NORMAL;
    }


    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }


    public String getNameUser() {

        return isNameIsPhoneNumber(name);
    }

    public String isNameIsPhoneNumber(String name) {
        name = "".equals(name) ? msisdn : name;
        name = "null".equals(name) ? msisdn : name;
        if ("".equals(msisdn)) {
            return name;
        }
        String sdt = Utilities.fixPhoneNumb(msisdn);
        String name2 = Utilities.fixPhoneNumb(name);
        if (sdt.equals(name2)) {

            return hidenPhoneNumber();
        }

        return name;
    }

    public boolean nameIsPhone() {
        String sdt = Utilities.fixPhoneNumb(msisdn);
        String name2 = Utilities.fixPhoneNumb(name);
        return sdt.equals(name2);


    }

    public String hidenPhoneNumber() {
        String sdt = Utilities.fixPhoneNumb(msisdn);
        if (TextUtils.isEmpty(sdt)) {
            return "";
        }
        int leng = sdt.length();

        if (leng == 10) {
            sdt = sdt.substring(0, 4) + "***" + sdt.substring(7, 10);
        } else {
            sdt = sdt.substring(0, 3) + "***" + sdt.substring(6, 9);
        }
        return "0" + sdt;
    }

    public String getMsisdn() {
        return msisdn;
    }   //dung cho profile

    //dung cho feed Onmedia
    public String getFix84MSISDN() {
        return Utilities.fixPhoneNumbTo84(msisdn);
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", facebook='" + facebook + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", user_type=" + user_type +
                ", name='" + name + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", image='" + image + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isChecked=" + isChecked +
                ", share_feed_privacy=" + share_feed_privacy +
                ", is_vip=" + is_vip +
                ", image310='" + image310 + '\'' +
                '}';
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getShare_feed_privacy() {
        return share_feed_privacy;
    }

    public void setShare_feed_privacy(int share_feed_privacy) {
        this.share_feed_privacy = share_feed_privacy;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getImage310() {
        return image310;
    }

    public void setImage310(String image310) {
        this.image310 = image310;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public boolean isUserRingMe() {
        return stateRingMe == 1;
    }

    public void setStateRingMe(int stateRingMe) {
        this.stateRingMe = stateRingMe;
    }

    public long getTime() {
        return time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAddFriend() {
        return addFriend;
    }

    public void setAddFriend(boolean addFriend) {
        this.addFriend = addFriend;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
