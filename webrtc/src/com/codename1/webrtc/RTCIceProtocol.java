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
public enum RTCIceProtocol {
    TCP("tcp"),
    UDP("udp");
    
    private String string;
    RTCIceProtocol(String str) {
        this.string = str;
    }
    
    public String stringValue() {
        return string;
    }
    
    public static RTCIceProtocol protocolFromString(String protocol) {
        if ("tcp".equals(protocol)) {
            return TCP;
        }
        if ("udp".equals(protocol)) {
            return UDP;
        }
        throw new IllegalArgumentException("Unsupported protocol: " + protocol);
    }
}
