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
public interface RTCRtpReceiver {
    public MediaStreamTrack getTrack();
    public RTCDtlsTransport getRtcpTransport();
    public RTCDtlsTransport getTransport();
    
    public RTCRtpContributingSources getContributingSources();
    public RTCRtpParameters getParameters();
    public RTCPromise<RTCStatsReport> getStats();
    public RTCRtpSynchronizationSources getSynchronizationSources();
    
}
