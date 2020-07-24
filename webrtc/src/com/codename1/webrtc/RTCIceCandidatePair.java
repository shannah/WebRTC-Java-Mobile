/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCIceCandidatePair dictionary describes a pair of ICE candidates which
 * together comprise a description of a viable connection between two WebRTC
 * endpoints. It is used as the return value from
 * RTCIceTransport.getSelectedCandidatePair() to identify the currently-selected
 * candidate pair identified by the ICE agent.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidatePair
 */
public class RTCIceCandidatePair {

    /**
     * An RTCIceCandidate describing the configuration of the local end of the
     * connection.
     *
     * @return the local
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidatePair/local
     */
    public RTCIceCandidate getLocal() {
        return local;
    }

    /**
     * An RTCIceCandidate describing the configuration of the local end of the
     * connection.
     *
     * @param local the local to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidatePair/local
     */
    public void setLocal(RTCIceCandidate local) {
        this.local = local;
    }

    /**
     * The RTCIceCandidate describing the configuration of the remote end of the
     * connection.
     *
     * @return the remote
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidatePair/remote
     */
    public RTCIceCandidate getRemote() {
        return remote;
    }

    /**
     * The RTCIceCandidate describing the configuration of the remote end of the
     * connection.
     *
     * @param remote the remote to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCIceCandidatePair/remote
     */
    public void setRemote(RTCIceCandidate remote) {
        this.remote = remote;
    }
    private RTCIceCandidate local;
    private RTCIceCandidate remote;
}
