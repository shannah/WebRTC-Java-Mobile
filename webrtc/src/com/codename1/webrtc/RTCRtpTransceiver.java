/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The WebRTC interface RTCRtpTransceiver describes a permanent pairing of an
 * RTCRtpSender and an RTCRtpReceiver, along with some shared state.
 *
 * Each SDP media section describes one bidirectional SRTP ("Secure Real Time
 * Protocol") stream (excepting the media section for RTCDataChannel, if
 * present). This pairing of send and receive SRTP streams is significant for
 * some applications, so RTCRtpTransceiver is used to represent this pairing,
 * along with other important state from the media section. Each non-disabled
 * SRTP media section is always represented by exactly one transceiver.
 *
 * A transceiver is uniquely identified using its mid property, which is the
 * same as the media ID (mid) of its corresponding m-line. An RTCRtpTransceiver
 * is associated with an m-line if its mid is non-null; otherwise it's
 * considered disassociated.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver
 */
public interface RTCRtpTransceiver extends RefCounted {

    /**
     * A string from the enum RTCRtpTransceiverDirection which indicates the
     * transceiver's current directionality, or null if the transceiver is
     * stopped or has never participated in an exchange of offers and answers.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/currentDirection
     */
    public RTCRtpTransceiverDirection getCurrentDirection();

    /**
     * A string from the enum RTCRtpTransceiverDirection which is used to set
     * the transceiver's desired direction.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/direction
     */
    public RTCRtpTransceiverDirection getDirection();

    /**
     * A string from the enum RTCRtpTransceiverDirection which is used to set
     * the transceiver's desired direction.
     *
     * @param dir
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/direction
     */
    public void setDirection(RTCRtpTransceiverDirection dir);

    /**
     * The media ID of the m-line associated with this transceiver. This
     * association is established, when possible, whenever either a local or
     * remote description is applied. This field is null if neither a local or
     * remote description has been applied, or if its associated m-line is
     * rejected by either a remote offer or any answer.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/mid
     */
    public String getMid();

    /**
     * The RTCRtpReceiver object that handles receiving and decoding incoming
     * media.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/receiver
     */
    public RTCRtpReceiver getReceiver();

    /**
     * The RTCRtpSender object responsible for encoding and sending data to the
     * remote peer.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/sender
     */
    public RTCRtpSender getSender();

    /**
     * Indicates whether or not sending and receiving using the paired
     * RTCRtpSender and RTCRtpReceiver has been permanently disabled, either due
     * to SDP offer/answer, or due to a call to stop().
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/stopped
     */
    public boolean isStopped();

    /**
     * A list of RTCRtpCodecParameters objects which override the default
     * preferences used by the user agent for the transceiver's codecs.
     *
     * @param codecs
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/setCodecPreferences
     */
    public void setCodecPreferences(RTCRtpCodecCapability... codecs);

    /**
     * Permanently stops the RTCRtpTransceiver. The associated sender stops
     * sending data, and the associated receiver likewise stops receiving and
     * decoding incoming data.
     *
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpTransceiver/stop
     */
    public void stop();
}
