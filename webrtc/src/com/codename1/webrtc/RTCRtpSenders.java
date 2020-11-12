/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * A list of {@link RTCRtpSender} objects.
 * @author shannah
 */
public class RTCRtpSenders extends RTCList<RTCRtpSender> {
    public RTCRtpSender removeById(String id) {
        RTCRtpSender found = null;
        for (RTCRtpSender r : this) {
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
