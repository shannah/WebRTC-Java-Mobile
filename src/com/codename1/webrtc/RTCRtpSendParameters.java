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
public class RTCRtpSendParameters {
    private RTCDegradationPreference degradationPreference;
    private RTCRtpEncodingParameters[] encodings;
    private RTCPriorityType priority;
    String transactionId;
    
    public static enum RTCPriorityType {
        Low("low");
        
        private String string;
        
        RTCPriorityType(String str) {
            this.string = str;
        }
    }
    
    public static enum RTCDegradationPreference {
        Balanced("balanced");
        
        private String string;
        
        RTCDegradationPreference(String str) {
            this.string = str;
        }
    }
    
}
