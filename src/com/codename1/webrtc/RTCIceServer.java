/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTCIceServer implements JSObject {

    /**
     * @return the credential
     */
    public String getCredential() {
        return credential;
    }

    /**
     * @param credential the credential to set
     */
    public void setCredential(String credential) {
        this.credential = credential;
    }

    /**
     * @return the credentialType
     */
    public RTCIceCredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * @param credentialType the credentialType to set
     */
    public void setCredentialType(RTCIceCredentialType credentialType) {
        this.credentialType = credentialType;
    }

    /**
     * @return the urls
     */
    public URLs getUrls() {
        return urls;
    }

    /**
     * @param urls the urls to set
     */
    public void setUrls(URLs urls) {
        this.urls = urls;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    private String credential;
    private RTCIceCredentialType credentialType;
    private URLs urls;
    private String username;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (credential != null) {
            out.put("credential", credential);
        }
        if (credentialType != null) {
            out.put("credentialType", credentialType.getStringValue());
        }
        if (urls != null) {
            out.put("urls", urls.toJSONStruct());
        }
        if (username != null) {
            out.put("username", username);
        }
        
        
        return out;
    }
}
