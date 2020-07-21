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
public interface RTCPeerConnection extends EventTarget {
    public boolean canTrickleIceCandidates();
    public RTCPeerConnectionState getConnectionState();
    public RTCSessionDescription getCurrentLocalDescription();
    public RTCSessionDescription getCurrentRemoteDescription();
    
    public RTCIceConnectionState getIceConnectionState();
    public RTCIceGatheringState getIceGatheringState();
    public RTCSessionDescription getLocalDescription();
    public RTCIdentityAssertion getPeerIdentity();
    public RTCSessionDescription getPendingLocalDescription();
    public RTCSessionDescription getPendingRemoteDescription();
    public RTCSessionDescription getRemoteDescription();
    public RTCSctpTransport getSctp();
    public RTCSignalingState getSignalingState();
    
    public RTCPromise addIceCandidate(RTCIceCandidate candidate);
    public RTCRtpSender addTrack(MediaStreamTrack track, MediaStream... stream);
    public void close();
    public RTCPromise<RTCSessionDescription> createAnswer();
    public RTCPromise<RTCSessionDescription> createAnswer(RTCAnswerOptions options);
    public RTCDataChannel createDataChannel(String label, RTCDataChannelInit options);
    public RTCPromise<RTCSessionDescription> createOffer(RTCOfferOptions options);
    
    public RTCRtpReceivers getReceivers();
    public RTCRtpSenders getSenders();
    public RTCPromise<RTCStatsReport> getStats(MediaStreamTrack selector);
    public MediaStream getStreamById(String id);
    public RTCRtpTransceivers getTransceivers();
    public void removeTrack(RTCRtpSender sender);
    public void restartIce();
    public void setIdentityProvider(String domainName);
    public void setIdentityProvider(String domainName, String protocol);
    public void setIdentityProvider(String domainName, String protocol, String username);
    public RTCPromise setLocalDescription(RTCSessionDescription sessionDescription);
    public RTCPromise setRemoteDescription(RTCSessionDescription sessionDescription);
    
    
    
    public static class RTCDataChannelInit implements JSObject {

        /**
         * @return the ordered
         */
        public boolean isOrdered() {
            return ordered;
        }

        /**
         * @param ordered the ordered to set
         */
        public void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        /**
         * @return the maxPacketLifeTime
         */
        public Integer getMaxPacketLifeTime() {
            return maxPacketLifeTime;
        }

        /**
         * @param maxPacketLifeTime the maxPacketLifeTime to set
         */
        public void setMaxPacketLifeTime(Integer maxPacketLifeTime) {
            this.maxPacketLifeTime = maxPacketLifeTime;
        }

        /**
         * @return the maxRetransmits
         */
        public Integer getMaxRetransmits() {
            return maxRetransmits;
        }

        /**
         * @param maxRetransmits the maxRetransmits to set
         */
        public void setMaxRetransmits(Integer maxRetransmits) {
            this.maxRetransmits = maxRetransmits;
        }

        /**
         * @return the protocol
         */
        public String getProtocol() {
            return protocol;
        }

        /**
         * @param protocol the protocol to set
         */
        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        /**
         * @return the negotiated
         */
        public boolean isNegotiated() {
            return negotiated;
        }

        /**
         * @param negotiated the negotiated to set
         */
        public void setNegotiated(boolean negotiated) {
            this.negotiated = negotiated;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }
        private boolean ordered;
        private Integer maxPacketLifeTime;
        private Integer maxRetransmits;
        private String protocol;
        private boolean negotiated;
        private int id;

        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            out.put("ordered", ordered);
            if (maxPacketLifeTime != null) {
                out.put("maxPacketLifeTime", maxPacketLifeTime);
            }
            if (maxRetransmits != null) {
                out.put("maxRetransmits", maxRetransmits);
            }
            if (protocol != null) {
                out.put("protocol", protocol);
            }
            out.put("negotiated", negotiated);
            if (id != 0 ) out.put("id", id);
            return out;
        }
    }
    
    public static enum RTCBundlePolicy {
        Balanced("balanced"),
        MaxCompat("max-compat"),
        MaxBundle("max-bundle");
        
        String string;
        
        RTCBundlePolicy(String str) {
            this.string = str;
        }
        
    }
    
    
    public static enum RTCIceConnectionState {
        New("new"),
        Checking("checking"),
        Connected("connected"),
        Completed("completed"),
        Failed("failed"),
        Disconnected("disconnected"),
        Closed("closed");
        
        String string;
        
        RTCIceConnectionState(String str) {
            this.string = str;
        }
        
        public boolean matches(String str) {
            return string.equals(str);
        }
        
    }
    
    public static enum RTCIceGatheringState {
        New("new"),
        Gathering("gathering"),
        Complete("complete");
        
        String string;
        
        RTCIceGatheringState(String str) {
            this.string = str;
        }
        
        public boolean matches(String str) {
            return string.equals(str);
        }
        
    }
    
    public static enum RTCIceTransportPolicy {
        All("all"),
        Public("public"),
        Relay("relay");
        
        String string;
        
        RTCIceTransportPolicy(String str) {
            this.string = str;
        }
        
    }
    
    public static enum RTCPeerConnectionState {
        New("new"),
        Connecting("connecting"),
        Connected("connected"),
        Disconnected("disconnected"),
        Failed("failed"),
        Closed("closed");
        
        String string;
        
        RTCPeerConnectionState(String str) {
            this.string = str;
        }

        public boolean matches(String stateStr) {
            return string.equals(stateStr);
        }
        
    }
    
    public static enum RTCRtcpMuxPolicy {
        Negotiate("negotiate"),
        Require("require");
        
        String string;
        
        RTCRtcpMuxPolicy(String str) {
            this.string = str;
        }
        
    }
    
    public static enum RTCSignalingState {
        Stable("stable"),
        HaveLocalOffer("have-local-offer"),
        HaveRemoteOffer("have-remote-offer"),
        HaveLocalPranswer("have-local-pranswer"),
        HaveRemotePranswer("have-remote-pranswer"),
        Closed("closed");
        
        String string;
        
        RTCSignalingState(String str) {
            this.string = str;
        }
        
        
        boolean matches(String str) {
            return string.equals(str);
        }
        
    }
    
    public static class RTCOfferOptions implements JSObject {

        /**
         * @return the iceRestart
         */
        public Boolean isIceRestart() {
            return iceRestart;
        }

        /**
         * @param iceRestart the iceRestart to set
         */
        public void setIceRestart(Boolean iceRestart) {
            this.iceRestart = iceRestart;
        }
        
        public RTCOfferOptions iceRestart(Boolean iceRestart) {
            setIceRestart(iceRestart);
            return this;
        }

        /**
         * @return the offerToReceiveAudio
         */
        public Boolean isOfferToReceiveAudio() {
            return offerToReceiveAudio;
        }

        /**
         * @param offerToReceiveAudio the offerToReceiveAudio to set
         */
        public void setOfferToReceiveAudio(Boolean offerToReceiveAudio) {
            this.offerToReceiveAudio = offerToReceiveAudio;
        }
        
        public RTCOfferOptions offerToReceiveAudio(Boolean offerToReceiveAudio) {
            setOfferToReceiveAudio(offerToReceiveAudio);
            return this;
        }

        /**
         * @return the offerToReceiveVideo
         */
        public Boolean isOfferToReceiveVideo() {
            return offerToReceiveVideo;
        }

        /**
         * @param offerToReceiveVideo the offerToReceiveVideo to set
         */
        public void setOfferToReceiveVideo(Boolean offerToReceiveVideo) {
            this.offerToReceiveVideo = offerToReceiveVideo;
        }
        
        public RTCOfferOptions offerToReceiveVideo(Boolean offerToReceiveVideo) {
            setOfferToReceiveVideo(offerToReceiveVideo);
            return this;
        }

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
        public RTCOfferOptions voiceActivityDetection(Boolean voiceActivityDetection) {
            setVoiceActivityDetection(voiceActivityDetection);
            return this;
        }
        
        private Boolean iceRestart;
        private Boolean offerToReceiveAudio;
        private Boolean offerToReceiveVideo;
        private Boolean voiceActivityDetection;

        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            if (iceRestart != null) out.put("iceRestart", iceRestart);
            if (offerToReceiveAudio != null) out.put("offerToReceiveAudio", offerToReceiveAudio);
            if (offerToReceiveVideo != null) out.put("offerToReceiveVideo", offerToReceiveVideo);
            if (voiceActivityDetection != null) out.put("voiceActivityDetection", voiceActivityDetection);
            return out;
        }
    }
    
}
