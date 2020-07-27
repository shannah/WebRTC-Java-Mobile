/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The WebRTC API's RTCOfferAnswerOptions dictionary is used to specify options
 * that configure and control the process of creating WebRTC offers or answers.
 * It's used as the base type for the options parameter when calling
 * createOffer() or createAnswer() on an RTCPeerConnection.
 *
 * Each of createOffer() and createAnswer() use RTCOfferAnswerOptions as the
 * base type for their options parameter's dictionary. createOffer() uses
 * RTCOfferOptions and createAnswer() uses RTCAnswerOptions.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCOfferAnswerOptions
 */
public class RTCOfferAnswerOptions implements JSONStruct {

    /**
     * For configurations of systems and codecs that are able to detect when the
     * user is speaking and toggle muting on and off automatically, this option
     * enables and disables that behavior. The default value is true, enabling
     * this functionality
     *
     * @return the voiceActivityDetection
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCOfferAnswerOptions/voiceActivityDetection
     */
    public Boolean isVoiceActivityDetection() {
        return voiceActivityDetection;
    }

    /**
     * For configurations of systems and codecs that are able to detect when the
     * user is speaking and toggle muting on and off automatically, this option
     * enables and disables that behavior. The default value is true, enabling
     * this functionality
     *
     * @param voiceActivityDetection the voiceActivityDetection to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCOfferAnswerOptions/voiceActivityDetection
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
