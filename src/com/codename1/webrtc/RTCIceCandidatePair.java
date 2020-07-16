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
public class RTCIceCandidatePair {

    /**
     * @return the local
     */
    public RTCIceCandidate getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(RTCIceCandidate local) {
        this.local = local;
    }

    /**
     * @return the remote
     */
    public RTCIceCandidate getRemote() {
        return remote;
    }

    /**
     * @param remote the remote to set
     */
    public void setRemote(RTCIceCandidate remote) {
        this.remote = remote;
    }
    private RTCIceCandidate local;
    private RTCIceCandidate remote;
}
