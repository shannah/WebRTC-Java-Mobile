/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * A list of {@link RTCRtpReceiver} objects.
 * @author shannah
 */
public class RTCRtpReceivers extends RTCList<RTCRtpReceiver> {
    public RTCRtpReceiver removeById(String id) {
        RTCRtpReceiver found = null;
        for (RTCRtpReceiver r : this) {
            if (id.equals(r.getTrack().getId())) {
                found = r;
                break;
            }
        }
        if (found != null) {
            remove(found);
        }
        return found;
    }
}
