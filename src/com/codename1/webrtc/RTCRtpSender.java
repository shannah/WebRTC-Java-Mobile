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
public interface RTCRtpSender {
    public RTCDTMFSender getDtmf();
    public RTCDtlsTransport getRtcpTransport();
    public MediaStreamTrack getTrack();
    public RTCDtlsTransport getTransport();
    
    public RTCRtpParameters getParameters();
    public RTCStatsReport getStats();
    public RTCPromise setParameters(RTCRtpSendParameters parameters);
    public RTCPromise replaceTrack(MediaStreamTrack newTrack);
}
