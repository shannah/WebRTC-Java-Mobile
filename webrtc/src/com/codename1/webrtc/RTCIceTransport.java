/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCIceTransport interface provides access to information about the ICE
 * transport layer over which the data is being sent and received. This is
 * particularly useful if you need to access state information about the
 * connection.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceTransport
 */
public interface RTCIceTransport extends EventTarget {

    /**
     * The ICE component being used by the transport. The value is one of the
     * strings from the RTCIceTransport enumerated type: "RTP" or "RTSP".
     *
     * @return
     */
    public String getComponent();

    /**
     * A DOMString indicating which gathering state the ICE agent is currently
     * in. The value is one of those included in the RTCIceGathererState
     * enumerated type: "new", "gathering", or "complete".
     *
     * @return
     */
    public String getGatheringState();

    /**
     * Returns a DOMString whose value is one of the members of the RTCIceRole
     * enumerated type: "controlling" or "controlled"; this indicates whether
     * the ICE agent is the one that makes the final decision as to the
     * candidate pair to use or not.
     *
     * @return
     */
    public String getRole();

    /**
     * A DOMString indicating what the current state of the ICE agent is. The
     * value of state can be used to determine whether the ICE agent has made an
     * initial connection using a viable candidate pair ("connected"), made its
     * final selection of candidate pairs ("completed"), or in an error state
     * ("failed"), among other states. See the RTCIceTransportState enumerated
     * type for a complete list of states.
     *
     * @return
     */
    public String getState();

    /**
     * Returns an array of RTCIceCandidate objects, each describing one of the
     * ICE candidates that have been gathered so far for the local device. These
     * are the same candidates which have already been sent to the remote peer
     * by sending an icecandidate event to the RTCPeerConnection for
     * transmission.
     *
     * @return
     */
    public RTCIceCandidates getLocalCandidates();

    /**
     * Returns a RTCIceParameters object describing the ICE parameters
     * established by a call to the RTCPeerConnection.setLocalDescription()
     * method. Returns null if parameters have not yet been received.
     *
     * @return
     */
    public RTCIceParameters getLocalParameters();

    /**
     * Returns an array of RTCIceCandidate objects, one for each of the remote
     * device's ICE candidates that have been received by the local end of the
     * RTCPeerConnection and delivered to ICE by calling addIceCandidate().
     *
     * @return
     */
    public RTCIceCandidates getRemoteCandidates();

    /**
     * Returns a RTCIceParameters object containing the ICE parameters for the
     * remote device, as set by a call to
     * RTCPeerConnection.setRemoteDescription(). If setRemoteDescription()
     * hasn't been called yet, the return value is null.
     *
     * @return
     */
    public RTCIceParameters getRemoveParameters();

    /**
     * Returns a RTCIceCandidatePair object that identifies the two
     * candidates - one for each end of the connection - that have been selected so
     * far. It's possible that a better pair will be found and selected later;
     * if you need to keep up with this, watch for the
     * selectedcandidatepairchange event. If no candidate pair has been selected
     * yet, the return value is null.
     *
     * @return
     */
    public RTCIceCandidatePair getSelectedCandidatePair();

}
