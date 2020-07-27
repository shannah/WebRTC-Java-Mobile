/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCIceCandidate interface - part of the WebRTC API - represents a candidate
 * Internet Connectivity Establishment (ICE) configuration which may be used to
 * establish an RTCPeerConnection.
 *
 * An ICE candidate describes the protocols and routing needed for WebRTC to be
 * able to communicate with a remote device. When starting a WebRTC peer
 * connection, typically a number of candidates are proposed by each end of the
 * connection, until they mutually agree upon one which describes the connection
 * they decide will be best. WebRTC then uses that candidate's details to
 * initiate the connection.
 *
 * @author shannah
 * 
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate
 */
public interface RTCIceCandidate extends JSONStruct {

    /**
     * A DOMString representing the transport address for the candidate that can be used for connectivity checks. The format of this address is a candidate-attribute as defined in RFC 5245. This string is empty ("") if the RTCIceCandidate is an "end of candidates" indicator.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/candidate
     */
    public String getCandidate();

    /**
     * A DOMString which indicates whether the candidate is an RTP or an RTCP candidate; its value is either "rtp" or "rtcp", and is derived from the  "component-id" field in the candidate a-line string. The permitted values are listed in the RTCIceComponent enumerated type.
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/component
     */
    public String getComponent();

    /**
     * Returns a DOMString containing a unique identifier that is the same for any candidates of the same type, share the same base (the address from which the ICE agent sent the candidate), and come from the same STUN server. This is used to help optimize ICE performance while prioritizing and correlating candidates that appear on multiple RTCIceTransport objects.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/foundation
     */
    public String getFoundation();

    /**
     * A DOMString containing the IP address of the candidate.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/ip
     */
    public String getIp();

    /**
     * An integer value indicating the candidate's port number.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/port
     */
    public int getPort();

    
    /**
     * A long integer value indicating the candidate's priority.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/priority
     */
    public long getPriority();

    /**
     * If the candidate is derived from another candidate, relatedAddress is a DOMString containing that host candidate's IP address. For host candidates, this value is null.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/relatedAddress
     */
    public String getRelatedAddress();

    /**
     * For a candidate that is derived from another, such as a relay or reflexive candidate, the relatedPort is a number indicating the port number of the candidate from which this candidate is derived. For host candidates, the relatedPort property is null.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/relatedPort
     */
    public int getRelatedPort();

    /**
     * A DOMString specifying the candidate's media stream identification tag which uniquely identifies the media stream within the component with which the candidate is associated, or null if no such association exists.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/sdpMid
     */
    public String getSdpMid();

    /**
     * If not null, sdpMLineIndex indicates the zero-based index number of the media description (as defined in RFC 4566) in the SDP with which the candidate is associated.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/sdpMLineIndex
     */
    public Integer getSdpMLineIndex();

    /**
     * If protocol is "tcp", tcpType represents the type of TCP candidate. Otherwise, tcpType is null.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/tcpType
     */
    public String getTcpType();

    /**
     * A DOMString indicating the type of candidate as one of the strings from the RTCIceCandidateType enumerated type.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/type
     */
    public String getType();

    /**
     * A DOMString containing a randomly-generated username fragment ("ice-ufrag") which ICE uses for message integrity along with a randomly-generated password ("ice-pwd"). You can use this string to verify generations of ICE generation; each generation of the same ICE process will use the same usernameFragment, even across ICE restarts.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/usernameFragment
     */
    public String getUsernameFragment();

    /**
     * Given the RTCIceCandidate's current configuration, toJSON() returns a DOMString containing a JSON representation of that configuration in the form of a RTCIceCandidateInit object.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidate/toJSON
     */
    public String toJSON();

}
