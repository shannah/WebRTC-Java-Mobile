/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The RTCConfiguration dictionary is used to provide configuration options for
 * an {@link RTCPeerConnection}. 
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCConfiguration
 */
public class RTCConfiguration implements JSObject {

    /**
     * Specifies how to handle negotiation of candidates when the remote peer is not compatible with the SDP BUNDLE standard. This must be one of the values from the enum RTCBundlePolicy. If this value isn't included in the dictionary, "balanced" is assumed.
     * 
     * @return the bundlePolicy
     */
    public RTCBundlePolicy getBundlePolicy() {
        return bundlePolicy;
    }

    /**
     * Specifies how to handle negotiation of candidates when the remote peer is not compatible with the SDP BUNDLE standard. This must be one of the values from the enum RTCBundlePolicy. If this value isn't included in the dictionary, "balanced" is assumed.
     * @param bundlePolicy the bundlePolicy to set
     */
    public void setBundlePolicy(RTCBundlePolicy bundlePolicy) {
        this.bundlePolicy = bundlePolicy;
    }

    /**
     * An Array of objects of type RTCCertificate which are used by the connection for authentication. If this property isn't specified, a set of certificates is generated automatically for each RTCPeerConnection instance. Although only one certificate is used by a given connection, providing certificates for multiple algorithms may improve the odds of successfully connecting in some circumstances. See Using certificates below for additional information.
     * @return the certificates
     */
    public RTCCertificates getCertificates() {
        return certificates;
    }

    /**
     * An Array of objects of type RTCCertificate which are used by the connection for authentication. If this property isn't specified, a set of certificates is generated automatically for each RTCPeerConnection instance. Although only one certificate is used by a given connection, providing certificates for multiple algorithms may improve the odds of successfully connecting in some circumstances. See Using certificates below for additional information.
     * @param certificates the certificates to set
     */
    public void setCertificates(RTCCertificates certificates) {
        this.certificates = certificates;
    }

    /**
     * An unsigned 16-bit integer value which specifies the size of the prefetched ICE candidate pool. The default value is 0 (meaning no candidate prefetching will occur). You may find in some cases that connections can be established more quickly by allowing the ICE agent to start fetching ICE candidates before you start trying to connect, so that they're already available for inspection when RTCPeerConnection.setLocalDescription() is called.
     * @return the iceCandidatePoolSize
     */
    public int getIceCandidatePoolSize() {
        return iceCandidatePoolSize;
    }

    /**
     * An unsigned 16-bit integer value which specifies the size of the prefetched ICE candidate pool. The default value is 0 (meaning no candidate prefetching will occur). You may find in some cases that connections can be established more quickly by allowing the ICE agent to start fetching ICE candidates before you start trying to connect, so that they're already available for inspection when RTCPeerConnection.setLocalDescription() is called.
     * @param iceCandidatePoolSize the iceCandidatePoolSize to set
     */
    public void setIceCandidatePoolSize(int iceCandidatePoolSize) {
        this.iceCandidatePoolSize = iceCandidatePoolSize;
    }

    /**
     * An array of RTCIceServer objects, each describing one server which may be used by the ICE agent; these are typically STUN and/or TURN servers. If this isn't specified, the connection attempt will be made with no STUN or TURN server available, which limits the connection to local peers.
     * @return the iceServers
     */
    public RTCIceServers getIceServers() {
        return iceServers;
    }

    /**
     * An array of RTCIceServer objects, each describing one server which may be used by the ICE agent; these are typically STUN and/or TURN servers. If this isn't specified, the connection attempt will be made with no STUN or TURN server available, which limits the connection to local peers.
     * @param iceServers the iceServers to set
     */
    public void setIceServers(RTCIceServers iceServers) {
        this.iceServers = iceServers;
    }

    /**
     * The current ICE transport policy; this must be one of the values from the RTCIceTransportPolicy enum. If this isn't specified, "all" is assumed.
     * @return the iceTransportPolicy
     */
    public RTCIceTransportPolicy getIceTransportPolicy() {
        return iceTransportPolicy;
    }

    /**
     * The current ICE transport policy; this must be one of the values from the RTCIceTransportPolicy enum. If this isn't specified, "all" is assumed.
     * @param iceTransportPolicy the iceTransportPolicy to set
     */
    public void setIceTransportPolicy(RTCIceTransportPolicy iceTransportPolicy) {
        this.iceTransportPolicy = iceTransportPolicy;
    }

    /**
     * A DOMString which specifies the target peer identity for the RTCPeerConnection. If this value is set (it defaults to null), the RTCPeerConnection will not connect to a remote peer unless it can successfully authenticate with the given name.
     * @return the peerIdentity
     */
    public String getPeerIdentity() {
        return peerIdentity;
    }

    /**
     * A DOMString which specifies the target peer identity for the RTCPeerConnection. If this value is set (it defaults to null), the RTCPeerConnection will not connect to a remote peer unless it can successfully authenticate with the given name.
     * @param peerIdentity the peerIdentity to set
     */
    public void setPeerIdentity(String peerIdentity) {
        this.peerIdentity = peerIdentity;
    }

    /**
     * The RTCP mux policy to use when gathering ICE candidates, in order to support non-multiplexed RTCP. The value must be one of those from the RTCRtcpMuxPolicy enum. The default is "require".
     * @return the rtcpMuxPolicy
     */
    public RTCRtcpMuxPolicy getRtcpMuxPolicy() {
        return rtcpMuxPolicy;
    }

    /**
     * The RTCP mux policy to use when gathering ICE candidates, in order to support non-multiplexed RTCP. The value must be one of those from the RTCRtcpMuxPolicy enum. The default is "require".
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

    /**
     * The RTCBundlePolicy enum defines string constants which are used to request a specific policy for gathering ICE candidates if the remote peer isn't compatible with the SDP BUNDLE standard for bundling multiple media streams on a single transport link.
     */
    public static enum RTCBundlePolicy {
        /**
         * On BUNDLE-aware connections, the ICE agent should gather candidates for all of the media types in use (audio, video, and data). Otherwise, the ICE agent should only negotiate one audio and video track on separate transports.
         */
        Balanced("balanced"),
        
        /**
         * The ICE agent should gather candidates for each track, using separate transports to negotiate all media tracks for connections which aren't BUNDLE-compatible.
         */
        MaxCompat("max-compat"),
        /**
         * The ICE agent should gather candidates for just one track. If the connection isn't BUNDLE-compatible, then the ICE agent should negotiate just one media track.
         */
        MaxBundle("max-bundle");

        private String string;

        RTCBundlePolicy(String str) {
            string = str;
        }
    }

    /**
     * The RTCIceTransportPolicy enum defines string constants which can be used to limit the transport policies of the ICE candidates to be considered during the connection process.
     */
    public static enum RTCIceTransportPolicy {
        /**
         * All ICE candidates will be considered.
         */
        All("all"),
        /**
         * Only ICE candidates with public IP addresses will be considered. Removed from the specification's May 13, 2016 working draft.
         */
        Public("public"),
        /**
         * Only ICE candidates whose IP addresses are being relayed, such as those being passed through a TURN server, will be considered.
         */
        Relay("relay");

        private String string;

        RTCIceTransportPolicy(String str) {
            string = str;
        }

        public String getStringValue() {
            return string;
        }
    }

    /**
     * The RTCRtcpMuxPolicy enum defines string constants which specify what ICE candidates are gathered to support non-multiplexed RTCP. <<<add a link to info about multiplexed RTCP.
     */
    public static enum RTCRtcpMuxPolicy {
        /**
         * Instructs the ICE agent to gather both RTP and RTCP candidates. If the remote peer can multiplex RTCP, then RTCP candidates are multiplexed atop the corresponding RTP candidates. Otherwise, both the RTP and RTCP candidates are returned, separately.
         */
        Negotiate("negotiate"),
        /**
         * Tells the ICE agent to gather ICE candidates for only RTP, and to multiplex RTCP atop them. If the remote peer doesn't support RTCP multiplexing, then session negotiation fails.
         */
        Require("require");

        private String string;

        RTCRtcpMuxPolicy(String str) {
            string = str;
        }
    }
}
