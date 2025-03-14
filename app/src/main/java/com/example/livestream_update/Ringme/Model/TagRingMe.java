package com.example.livestream_update.Ringme.Model;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by thanhnt72 on 3/5/2016.
 */
public class TagRingMe implements Serializable {

    @SerializedName("tag_name")
    private String tagName = "";

    @SerializedName("name")
    private String name = "";

    @SerializedName("msisdn")
    private String msisdn = "";

    private transient String contactName = "";

    private transient int offset;

    public TagRingMe(String tagName, String name, String msisdn) {
        this.tagName = tagName;
        this.name = name;
        this.msisdn = msisdn;
    }

    public TagRingMe() {
    }

    public String getTagName() {
        return tagName;
    }

    public String getName() {
        return name;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public interface OnClickTag {
        void OnClickUser(String msisdn, String name);
    }

    @Override
    public String toString() {
        return "TagKakoak{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", contactName='" + contactName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TagRingMe) {
            TagRingMe c = (TagRingMe) o;
            return this.name.equals(c.name);
        }
        return false;
    }
}

