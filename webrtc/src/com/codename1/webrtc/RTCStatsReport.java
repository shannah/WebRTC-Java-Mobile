/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCStatsReport interface provides a statistics report obtained by calling
 * one of the RTCPeerConnection.getStats(), RTCRtpReceiver.getStats(), and
 * RTCRtpSender.getStats() methods. This statistics report contains a mapping of
 * statistic category string names to objects containing the corresponding
 * statistics data.
 *
 * Calling getStats() on an RTCPeerConnection lets you specify whether you wish
 * to obtain statistics for outbound, inbound, or all streams on the connection.
 * The RTCRtpReceiver and RTCRtpSender versions of getStats() specifically only
 * return statistics available to the incoming or outgoing stream on which you
 * call them.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCStatsReport
 */
public interface RTCStatsReport {

    /**
     * A DOMString which uniquely identifies the object which was inspected to
     * produce this object based on RTCStats.
     *
     * @return
     */
    public String getId();

    /**
     * A DOMHighResTimeStamp object indicating the time at which the sample was
     * taken for this statistics object.
     *
     * @return
     */
    public long getTimestamp();

    /**
     * A DOMString indicating the type of statistics the object contains, taken
     * from the enum type RTCStatsType.
     *
     * @return
     */
    public String getType();
}
