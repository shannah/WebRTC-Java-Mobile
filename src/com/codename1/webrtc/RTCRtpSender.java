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
public interface RTCRtpSender extends RefCounted {
    public RTCDTMFSender getDtmf();
    
    public MediaStreamTrack getTrack();
    
    
    public RTCRtpSendParameters getParameters();
    public RTCStatsReport getStats();
    public RTCPromise setParameters(RTCRtpSendParameters parameters);
    public RTCPromise replaceTrack(MediaStreamTrack newTrack);
}
