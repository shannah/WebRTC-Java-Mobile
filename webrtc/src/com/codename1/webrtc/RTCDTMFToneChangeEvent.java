/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCDTMFToneChangeEvent interface represents events sent to indicate that DTMF tones have started or finished playing. This interface is used by the tonechange event.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFToneChangeEvent
 */
public interface RTCDTMFToneChangeEvent extends Event {
    
    /**
     * A DOMString specifying the tone which has begun playing, or an empty string ("") if the previous tone has finished playing.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFToneChangeEvent/tone
     */
    public String getTone();
}
