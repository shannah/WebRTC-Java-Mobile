/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCRtpContributingSource dictionary of the the WebRTC API is used by {@link RTCRtpReceiver#getContributingSources()
 * } to provide information about a given contributing source (CSRC), including
 * the most recent time a packet that the source contributed was played out.
 *
 * The information provided is based on the last ten seconds of media received.
 *
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource
 * @author shannah
 */
public class RTCRtpContributingSource {

    /**
     * A double-precision floating-point value between 0 and 1 specifying the
     * audio level contained in the last RTP packet played from this source.
     *
     * @return the audioLevel
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/audioLevel
     */
    public double getAudioLevel() {
        return audioLevel;
    }

    /**
     * A double-precision floating-point value between 0 and 1 specifying the
     * audio level contained in the last RTP packet played from this source.
     *
     * @param audioLevel the audioLevel to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/audioLevel
     */
    public void setAudioLevel(double audioLevel) {
        this.audioLevel = audioLevel;
    }

    /**
     * The RTP timestamp of the media played out at the time indicated by
     * timestamp. This value is a source-generated time value which can be used
     * to help with sequencing and synchronization.
     *
     * @return the rtmpTimestamp
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/rtpTimestamp
     */
    public double getRtmpTimestamp() {
        return rtmpTimestamp;
    }

    /**
     * The RTP timestamp of the media played out at the time indicated by
     * timestamp. This value is a source-generated time value which can be used
     * to help with sequencing and synchronization.
     *
     * @param rtmpTimestamp the rtmpTimestamp to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/rtpTimestamp
     */
    public void setRtmpTimestamp(double rtmpTimestamp) {
        this.rtmpTimestamp = rtmpTimestamp;
    }

    /**
     * A 32-bit unsigned integer value specifying the CSRC identifier of the
     * contributing source.
     *
     * @return the source
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/source
     */
    public int getSource() {
        return source;
    }

    /**
     * A 32-bit unsigned integer value specifying the CSRC identifier of the
     * contributing source.
     *
     * @param source the source to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/source
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     * A DOMHighResTimeStamp indicating the most recent time at which a frame
     * originating from this source was delivered to the receiver's
     * MediaStreamTrack
     *
     * @return the timestamp
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/timestamp
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * A DOMHighResTimeStamp indicating the most recent time at which a frame
     * originating from this source was delivered to the receiver's
     * MediaStreamTrack
     *
     * @param timestamp the timestamp to set
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpContributingSource/timestamp
     */
    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
    private double audioLevel;
    private double rtmpTimestamp;
    private int source;
    private double timestamp;
}
