/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCRtpSender interface provides the ability to control and obtain details
 * about how a particular MediaStreamTrack is encoded and sent to a remote peer.
 * With it, you can configure the encoding used for the corresponding track, get
 * information about the device's media capabilities, and so forth. You can also
 * obtain access to an RTCDTMFSender which can be used to send DTMF codes (to
 * simulate the user pressing buttons on a telephone's dial pad) to the remote
 * peer.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender
 */
public interface RTCRtpSender extends RefCounted {

    /**
     * An RTCDTMFSender which can be used to send DTMF tones using
     * telephone-event payloads on the RTP session represented by the
     * RTCRtpSender object. If null, the track and/or the connection doesn't
     * support DTMF. Only audio tracks can support DTMF.
     *
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/dtmf
     */
    public RTCDTMFSender getDtmf();

    /**
     * The MediaStreamTrack which is being handled by the RTCRtpSender. If track
     * is null, the RTCRtpSender doesn't transmit anything.
     *
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/track
     */
    public MediaStreamTrack getTrack();

    /**
     * Returns a RTCRtpParameters object describing the current configuration
     * for the encoding and transmission of media on the track.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/getParameters
     */
    public RTCRtpSendParameters getParameters();

    /**
     * Returns a Promise which is fulfilled with a RTCStatsReport which provides
     * statistics data for all outbound streams being sent using this
     * RTCRtpSender.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/getStats
     */
    public RTCStatsReport getStats();

    /**
     * Applies changes to parameters which configure how the track is encoded
     * and transmitted to the remote peer.
     *
     * @param parameters
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/setParameters
     */
    public RTCPromise setParameters(RTCRtpSendParameters parameters);

    /**
     * Attempts to replace the track currently being sent by the RTCRtpSender
     * with another track, without performing renegotiation. This method can be
     * used, for example, to toggle between the front- and rear-facing cameras
     * on a device.
     *
     * @param newTrack
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/replaceTrack
     */
    public RTCPromise replaceTrack(MediaStreamTrack newTrack);
}
