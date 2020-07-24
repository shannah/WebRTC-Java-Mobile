/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCDataChannelEvent() constructor returns a new RTCDataChannelEvent
 * object, which represents a datachannel event. These events sent to an
 * RTCPeerConnection when its remote peer is asking to open an RTCDataChannel
 * between the two peers.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannelEvent
 */
public interface RTCDataChannelEvent {

    public RTCDataChannel getChannel();

}
