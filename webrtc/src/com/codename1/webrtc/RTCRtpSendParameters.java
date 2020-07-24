/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTCRtpSendParameters implements JSObject {
    private RTCDegradationPreference degradationPreference;
    private RTCRtpEncodingParameters[] encodings;
    private RTCPriorityType priority;
    String transactionId;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (degradationPreference != null) {
            out.put("degradationPreference", degradationPreference.string);
        }
        if (encodings != null && encodings.length > 0) {
            ArrayList<Map> encodingsList = new ArrayList<Map>();
            for (RTCRtpEncodingParameters enc : encodings) {
                encodingsList.add((Map)enc.toJSONStruct());
            }
            out.put("encodings", encodingsList);
        }
        if (priority != null) {
            out.put("priority", priority.string);
        }
        if (transactionId != null) {
            out.put("transactionId", transactionId);
        }
        return out;
    }
    
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
