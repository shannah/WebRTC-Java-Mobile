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
public class RTCConfiguration {
    private RTCBundlePolicy bundlePolicy;
    private RTCCertificates certificates;
    private int iceCandidatePoolSize;
    private RTCIceServers iceServers;
    private RTCIceTransportPolicy iceTransportPolicy;
    private String peerIdentity;
    private RTCRtcpMuxPolicy rtcpMuxPolicy;
    
    public static enum RTCBundlePolicy {
        Balanced("balanced"),
        MaxCompat("max-compat"),
        MaxBundle("max-bundle");
        
        private String string;
        
        RTCBundlePolicy(String str) {
            string = str;
        }
    }
    
    public static enum RTCIceTransportPolicy {
        All("all"),
        Public("public"),
        Relay("relay");
        
        private String string;
        
        RTCIceTransportPolicy(String str) {
            string = str;
        }
    }
    
    public static enum RTCRtcpMuxPolicy {
        Negotiate("negotiate"),
        Require("require");
        
        private String string;
        
        RTCRtcpMuxPolicy(String str) {
            string = str;
        }
    }
}
