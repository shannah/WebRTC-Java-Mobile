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
public class RTCIceCandidateInit {
    private String candidate;
    private String sdpMid;
    private Integer sdpMLineIndex;
    private String usernameFragment; 

    /**
     * @return the usernameFragment
     */
    public String getUsernameFragment() {
        return usernameFragment;
    }

    /**
     * @param usernameFragment the usernameFragment to set
     */
    public void setUsernameFragment(String usernameFragment) {
        this.usernameFragment = usernameFragment;
    }

    /**
     * @return the candidate
     */
    public String getCandidate() {
        return candidate;
    }

    /**
     * @param candidate the candidate to set
     */
    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    /**
     * @return the sdpMid
     */
    public String getSdpMid() {
        return sdpMid;
    }

    /**
     * @param sdpMid the sdpMid to set
     */
    public void setSdpMid(String sdpMid) {
        this.sdpMid = sdpMid;
    }

    /**
     * @return the sdpMLineIndex
     */
    public Integer getSdpMLineIndex() {
        return sdpMLineIndex;
    }

    /**
     * @param sdpMLineIndex the sdpMLineIndex to set
     */
    public void setSdpMLineIndex(Integer sdpMLineIndex) {
        this.sdpMLineIndex = sdpMLineIndex;
    }
}
