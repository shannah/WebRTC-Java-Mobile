/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
 */
public enum RTCRtpTransceiverDirection {
    Sendrecv("sendrecv"),
    Sendonly("sendonly"),
    Recvonly("recvonly"),
    Inactive("inactive"),
    Stopped("stopped");
    
    private String string;
    
    RTCRtpTransceiverDirection(String str) {
        this.string = str;
    }
    
    public String stringValue() {
        return string;
    }
    
    public boolean matches(String str) {
        return string.equals(str);
    }
}
