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
public class RTCConfiguration implements JSObject{

    /**
     * @return the bundlePolicy
     */
    public RTCBundlePolicy getBundlePolicy() {
        return bundlePolicy;
    }

    /**
     * @param bundlePolicy the bundlePolicy to set
     */
    public void setBundlePolicy(RTCBundlePolicy bundlePolicy) {
        this.bundlePolicy = bundlePolicy;
    }

    /**
     * @return the certificates
     */
    public RTCCertificates getCertificates() {
        return certificates;
    }

    /**
     * @param certificates the certificates to set
     */
    public void setCertificates(RTCCertificates certificates) {
        this.certificates = certificates;
    }

    /**
     * @return the iceCandidatePoolSize
     */
    public int getIceCandidatePoolSize() {
        return iceCandidatePoolSize;
    }

    /**
     * @param iceCandidatePoolSize the iceCandidatePoolSize to set
     */
    public void setIceCandidatePoolSize(int iceCandidatePoolSize) {
        this.iceCandidatePoolSize = iceCandidatePoolSize;
    }

    /**
     * @return the iceServers
     */
    public RTCIceServers getIceServers() {
        return iceServers;
    }

    /**
     * @param iceServers the iceServers to set
     */
    public void setIceServers(RTCIceServers iceServers) {
        this.iceServers = iceServers;
    }

    /**
     * @return the iceTransportPolicy
     */
    public RTCIceTransportPolicy getIceTransportPolicy() {
        return iceTransportPolicy;
    }

    /**
     * @param iceTransportPolicy the iceTransportPolicy to set
     */
    public void setIceTransportPolicy(RTCIceTransportPolicy iceTransportPolicy) {
        this.iceTransportPolicy = iceTransportPolicy;
    }

    /**
     * @return the peerIdentity
     */
    public String getPeerIdentity() {
        return peerIdentity;
    }

    /**
     * @param peerIdentity the peerIdentity to set
     */
    public void setPeerIdentity(String peerIdentity) {
        this.peerIdentity = peerIdentity;
    }

    /**
     * @return the rtcpMuxPolicy
     */
    public RTCRtcpMuxPolicy getRtcpMuxPolicy() {
        return rtcpMuxPolicy;
    }

    /**
     * @param rtcpMuxPolicy the rtcpMuxPolicy to set
     */
    public void setRtcpMuxPolicy(RTCRtcpMuxPolicy rtcpMuxPolicy) {
        this.rtcpMuxPolicy = rtcpMuxPolicy;
    }
    private RTCBundlePolicy bundlePolicy;
    private RTCCertificates certificates;
    private int iceCandidatePoolSize;
    private RTCIceServers iceServers;
    private RTCIceTransportPolicy iceTransportPolicy;
    private String peerIdentity;
    private RTCRtcpMuxPolicy rtcpMuxPolicy;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (bundlePolicy != null) {
            out.put("bundlePolicy", bundlePolicy.string);
        }
        if (certificates != null) {
            out.put("certificates", certificates.toJSONStruct());
        }
        if (iceCandidatePoolSize != 0) {
            out.put("iceCandidatePoolSize", iceCandidatePoolSize);
        }
        if (iceServers != null) {
            out.put("iceServers", iceServers.toJSONStruct());
        }
        if (iceTransportPolicy != null) {
            out.put("iceTransportPolicy", iceTransportPolicy.getStringValue());
        }
        if (peerIdentity != null) {
            out.put("peerIdentity", peerIdentity);
        }
        if (rtcpMuxPolicy != null) {
            out.put("rtcpMuxPolicy", rtcpMuxPolicy.string);
        }
        
        return out;
    }
    
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
        
        public String getStringValue() {
            return string;
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
