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
public class RTCSessionDescriptionInit {

    /**
     * @return the type
     */
    public RTCSessionDescription.RTCSdpType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(RTCSessionDescription.RTCSdpType type) {
        this.type = type;
    }

    /**
     * @return the sdp
     */
    public String getSdp() {
        return sdp;
    }

    /**
     * @param sdp the sdp to set
     */
    public void setSdp(String sdp) {
        this.sdp = sdp;
    }
    private RTCSessionDescription.RTCSdpType type;
    private String sdp;
}
