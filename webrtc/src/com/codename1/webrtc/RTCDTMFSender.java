/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCDTMFSender interface provides a mechanism for transmitting DTMF codes on a WebRTC RTCPeerConnection. You gain access to the connection's RTCDTMFSender through the RTCRtpSender.dtmf property on the audio track you wish to send DTMF with.

The primary purpose for WebRTC's DTMF support is to allow WebRTC-based communication clients to be connected to a public-switched telephone network (PSTN) or other legacy telephone service, including extant voice over IP (VoIP) services. For that reason, DTMF can't be used between two WebRTC-based devices, because there is no mechanism provided by WebRTC for receiving DTMF codes.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFSender
 */
public interface RTCDTMFSender extends EventTarget {
    /**
     * A DOMString which contains the list of DTMF tones currently in the queue to be transmitted (tones which have already been played are no longer included in the string). See toneBuffer for details on the format of the tone buffer.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFSender/toneBuffer
     */
    public String getToneBuffer();
    
    /**
     * Given a string describing a set of DTMF codes and, optionally, the duration of and inter-tone gap between the tones, insertDTMF() starts sending the specified tones. Calling insertDTMF() replaces any already-pending tones from the toneBuffer. You can abort sending queued tones by specifying an empty string ("") as the set of tones to play.
     * @param tones
     * @param duration
     * @param interToneGap 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFSender/insertDTMF
     */
    public void insertDTMF(String tones, int duration, int interToneGap);
    
    /**
     * Given a string describing a set of DTMF codes and, optionally, the duration of and inter-tone gap between the tones, insertDTMF() starts sending the specified tones. Calling insertDTMF() replaces any already-pending tones from the toneBuffer. You can abort sending queued tones by specifying an empty string ("") as the set of tones to play.
     * @param tones
     * @param duration 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFSender/insertDTMF
     */
    public void insertDTMF(String tones, int duration);
    
    /**
     * Given a string describing a set of DTMF codes and, optionally, the duration of and inter-tone gap between the tones, insertDTMF() starts sending the specified tones. Calling insertDTMF() replaces any already-pending tones from the toneBuffer. You can abort sending queued tones by specifying an empty string ("") as the set of tones to play.
     * @param tones 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDTMFSender/insertDTMF
     */
    public void insertDTMF(String tones);
}
