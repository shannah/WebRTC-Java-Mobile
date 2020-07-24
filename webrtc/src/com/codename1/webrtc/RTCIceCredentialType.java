/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The WebRTC API's RTCIceCredentialType enumerated string type defines the
 * authentication method used to gain access to an ICE server identified by an
 * RTCIceServer object.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCredentialType
 */
public enum RTCIceCredentialType {
    /**
     * The RTCIceServer requires the use of OAuth 2.0 to authenticate in order
     * to use the ICE server described. This process is detailed in RFC 7635.
     * This property was formerly called token.
     */
    Oauth("oauth"),
    /**
     * The RTCIceServer requires a username and password to authenticate prior
     * to using the described ICE server.
     */
    Password("password");

    private String string;

    RTCIceCredentialType(String str) {
        this.string = str;
    }

    public String getStringValue() {
        return string;
    }

}
