/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCRtpTransceiverDirection type is an enumerated set of strings which are
 * used to describe the directionality of a RTCRtpTransceiver instance. Both the
 * preferred direction and the currentDirection properties are of this type.
 *
 * == Values
 *
 * The RTCRtpTransceiverDirection type is an enumeration of string values. Each
 * describes how the transceiver's associated RTCRtpSender and RTCRtpReceiver
 * behave as shown in the table below.
 *
 * @author shannah
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiverDirection
 */
public enum RTCRtpTransceiverDirection {

    /**
     * === RTCRtpSender Behaviour
     *
     * Offers to send RTP data, and will do so if the other peer accepts the
     * connection and at least one of the sender's encodings is active.
     *
     * === RTCRtpReceiver Behaviour
     *
     * Offers to receive RTP data, and does so if the other peer accepts.
     *
     *
     */
    Sendrecv("sendrecv"),
    /**
     * === RTCRtpSender Behaviour
     *
     * Offers to send RTP data, and will do so if the other peer accepts the
     * connection and at least one of the sender's encodings is active.
     *
     * === RTCRtpReceiver Behaviour
     *
     * Does not offer to receive RTP data and will not do so.
     *
     *
     */
    Sendonly("sendonly"),
    /**
     * === RTCRtpSender Behaviour
     *
     * Does not offer to send RTP data, and will not do so.
     *
     * === RTCRtpReceiver Behaviour
     *
     * Offers to receive RTP data, and will do so if the other peer offers.
     *
     *
     */
    Recvonly("recvonly"),
    /**
     * === RTCRtpSender Behaviour
     *
     * Does not offer to send RTP data, and will not do so.
     *
     * === RTCRtpReceiver Behaviour
     *
     * Does not offer to receive RTP data and will not do so.
     *
     *
     */
    Inactive("inactive"),
    Stopped("stopped");

    private String string;

    RTCRtpTransceiverDirection(String str) {
        this.string = str;
    }

    /**
     * Gets underlying string value.
     * @return 
     */
    public String stringValue() {
        return string;
    }

    /**
     * Checks if matches string.
     * @param str String in format of underlying string value.
     * @return 
     */
    public boolean matches(String str) {
        return string.equals(str);
    }
}
