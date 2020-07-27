/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCRtpReceiver interface of the WebRTC API manages the reception and
 * decoding of data for a {@link MediaStreamTrack} on an
 * {@link RTCPeerConnection}.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver
 */
public interface RTCRtpReceiver extends RefCounted {

    /**
     * Returns the MediaStreamTrack associated with the current RTCRtpReceiver
     * instance.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver/track
     */
    public MediaStreamTrack getTrack();

    /**
     * Returns an array of RTCRtpContributingSource instances for each unique
     * CSRC (contributing source) identifier received by the current
     * RTCRtpReceiver in the last ten seconds.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver/getContributingSources
     */
    public RTCRtpContributingSources getContributingSources();

    /**
     * Returns an RTCRtpParameters object which contains information about how
     * the RTC data is to be decoded.
     *
     * @return
     */
    public RTCRtpParameters getParameters();

    /**
     * Returns a Promise whose fulfillment handler receives a RTCStatsReport
     * which contains statistics about the incoming streams and their
     * dependencies.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver/getStats
     */
    public RTCPromise<RTCStatsReport> getStats();

    /**
     * Returns an array including one RTCRtpSynchronizationSource instance for
     * each unique SSRC (synchronization source) identifier received by the
     * current RTCRtpReceiver in the last ten seconds.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver/getSynchronizationSources
     */
    public RTCRtpSynchronizationSources getSynchronizationSources();

}
