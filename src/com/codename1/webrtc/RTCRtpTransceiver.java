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
public interface RTCRtpTransceiver extends RefCounted {
    public RTCRtpTransceiverDirection getCurrentDirection();
    public RTCRtpTransceiverDirection getDirection();
    public void setDirection(RTCRtpTransceiverDirection dir);
    public String getMid();
    public RTCRtpReceiver getReceiver();
    public RTCRtpSender getSender();
    public boolean isStopped();
    
    public void setCodecPreferences(RTCRtpCodecCapability... codecs);
    public void stop();
}
