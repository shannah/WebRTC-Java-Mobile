/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The WebRTC API interface RTCTrackEvent represents the track event, which is
 * sent when a new {@link MediaStreamTrack} is added to an
 * {@link RTCRtpReceiver} which is part of the {@link RTCPeerConnection}. The
 * target is the {@link RTCPeerConnection} object to which the track is being
 * added.
 *
 * This event is sent by the WebRTC layer to the web site or application, so you
 * will not typically need to instantiate an RTCTrackEvent yourself.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent
 */
public interface RTCTrackEvent extends Event {

    /**
     * The RTCRtpReceiver used by the track that's been added to the
     * RTCPeerConnection.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent/receiver
     */
    public RTCRtpReceiver getReceiver();

    /**
     * An array of MediaStream objects, each representing one of the media
     * streams to which the added track belongs. By default, the array is empty,
     * indicating a streamless track.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent/streams
     */
    public MediaStreams getStreams();

    /**
     * The MediaStreamTrack which has been added to the connection.
     *
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent/track
     */
    public MediaStreamTrack getTrack();

    /**
     * The RTCRtpTransceiver being used by the new track.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent/transceiver
     */
    public RTCRtpTransceiver getTransceiver();

}
