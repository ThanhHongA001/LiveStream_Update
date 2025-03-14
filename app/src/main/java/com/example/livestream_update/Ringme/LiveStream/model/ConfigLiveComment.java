/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2020/2/25
 *
 */

package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class ConfigLiveComment implements Serializable {
    String domainAPI;
    String domainWS;
    String publicKey;

    public ConfigLiveComment() {
    }

    public ConfigLiveComment(String domainAPI, String domainWS, String publicKey) {
        this.domainAPI = domainAPI;
        this.domainWS = domainWS;
        this.publicKey = publicKey;
    }

    public String getDomainAPI() {
        if (domainAPI == null) domainAPI = "";
        return domainAPI;
    }

    public void setDomainAPI(String domainAPI) {
        this.domainAPI = domainAPI;
    }

    public String getDomainWS() {
        if (domainWS == null) domainWS = "";
        return domainWS;
    }

    public void setDomainWS(String domainWS) {
        this.domainWS = domainWS;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "ConfigLiveComment{" +
                "domainAPI='" + domainAPI + '\'' +
                ", domainWS='" + domainWS + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
