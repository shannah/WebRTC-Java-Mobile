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
public interface RTCDtlsTransport {
    public RTCIceTransport getIceTransport();
    public RTCDtlsTransportState getState();
    
    
    public static enum RTCDtlsTransportState {
        New("new"),
        Connecting("connecting"),
        Connected("connected"),
        Closed("closed"),
        Failed("failed");
        
        private String string;
        
        RTCDtlsTransportState(String str) {
            this.string = str;
        }
        
    }
            
}
