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
public interface RTCSessionDescription extends JSObject {
    public RTCSdpType getType();
    public String getSdp();
    
    public static enum RTCSdpType {
        Answer("answer"),
        Offer("offer"),
        Pranswer("pranswer"),
        Rollback("rollback");
        private String string;
        
        RTCSdpType(String str) {
            string = str;
        }
        
        public boolean matches(String str) {
            return string.equals(str);
        }
        
        public String stringValue() {
            return string;
        }
    }
    
    public String toJSON();
}
