/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCPeerConnectionIceEvent interface represents events that occurs in
 * relation to ICE candidates with the target, usually an RTCPeerConnection.
 * Only one event is of this type: icecandidate.
 *
 * @author shannah
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnectionIceEvent
 */
public interface RTCPeerConnectionIceEvent extends Event {

    /**
     * Contains the RTCIceCandidate containing the candidate associated with the
     * event, or null if this event indicates that there are no further
     * candidates to come.
     *
     * @return
     */
    public RTCIceCandidate getCandidate();
}
