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
public class RTCOfferAnswerOptions {

    /**
     * @return the voiceActivityDetection
     */
    public boolean isVoiceActivityDetection() {
        return voiceActivityDetection;
    }

    /**
     * @param voiceActivityDetection the voiceActivityDetection to set
     */
    public void setVoiceActivityDetection(boolean voiceActivityDetection) {
        this.voiceActivityDetection = voiceActivityDetection;
    }
    private boolean voiceActivityDetection;
}
