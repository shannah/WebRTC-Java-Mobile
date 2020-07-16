/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
 */
public interface RTCSessionDescription {
    public RTCSdpType getType();
    public String getSdp();
    
    public static enum RTCSdpType {
        Answer,
        Offer,
        Pranswer,
        Rollback
    }
    
    public String toJSON();
}
