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
public class RTCOfferAnswerOptions implements JSObject {

    /**
     * @return the voiceActivityDetection
     */
    public Boolean isVoiceActivityDetection() {
        return voiceActivityDetection;
    }

    /**
     * @param voiceActivityDetection the voiceActivityDetection to set
     */
    public void setVoiceActivityDetection(Boolean voiceActivityDetection) {
        this.voiceActivityDetection = voiceActivityDetection;
    }
    private Boolean voiceActivityDetection;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (voiceActivityDetection != null) {
            out.put("voiceActivityDetection", voiceActivityDetection);
        }
        
        return out;
    }
}
