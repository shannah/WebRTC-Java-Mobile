/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The RTCIceServer dictionary defines how to connect to a single ICE server (such as a STUN or TURN server). It includes both the URL and the necessary credentials, if any, to connect to the server.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer
 */
public class RTCIceServer implements JSObject {

    /**
     * The credential to use when logging into the server. This is only used if the RTCIceServer represents a TURN server.
     * @return the credential
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/credential
     */
    public String getCredential() {
        return credential;
    }

    /**
     * The credential to use when logging into the server. This is only used if the RTCIceServer represents a TURN server.
     * @param credential the credential to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/credential
     */
    public void setCredential(String credential) {
        this.credential = credential;
    }

    /**
     * If the RTCIceServer represents a TURN server, this attribute specifies what kind of credential is to be used when connecting. This must be one of the values defined by the RTCIceCredentialType enum. The default is password.
     * @return the credentialType
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/credentialType
     */
    public RTCIceCredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * If the RTCIceServer represents a TURN server, this attribute specifies what kind of credential is to be used when connecting. This must be one of the values defined by the RTCIceCredentialType enum. The default is password.
     * @param credentialType the credentialType to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/credentialType
     */
    public void setCredentialType(RTCIceCredentialType credentialType) {
        this.credentialType = credentialType;
    }

    /**
     * This required property is either a single DOMString or an array of DOMStrings, each specifying a URL which can be used to connect to the server.
     * @return the urls
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/urls
     */
    public URLs getUrls() {
        return urls;
    }

    /**
     * This required property is either a single DOMString or an array of DOMStrings, each specifying a URL which can be used to connect to the server.
     * @param urls the urls to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/urls
     */
    public void setUrls(URLs urls) {
        this.urls = urls;
    }

    /**
     * If the RTCIceServer is a TURN server, then this is the username to use during the authentication process.
     * @return the username
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/username
     */
    public String getUsername() {
        return username;
    }

    /**
     * If the RTCIceServer is a TURN server, then this is the username to use during the authentication process.
     * @param username the username to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceServer/username
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
