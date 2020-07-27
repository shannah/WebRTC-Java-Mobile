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
 * The WebRTC API's RTCRtpSendParameters dictionary is used to specify the parameters for an {@link RTCRtpSender} when calling its {@link RTCRtpSender#setParameters(com.codename1.webrtc.RTCRtpSendParameters) } method.
 * @author shannah
 * @saee https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSendParameters
 */
public class RTCRtpSendParameters implements JSONStruct {

    /**
     * Specifies the preferred way the WebRTC layer should handle optimizing bandwidth against quality in constrained-bandwidth situations; the value comes from the RTCDegradationPreference enumerated string type, and the default is balanced.
     * @return the degradationPreference
     */
    public RTCDegradationPreference getDegradationPreference() {
        return degradationPreference;
    }

    /**
     * Specifies the preferred way the WebRTC layer should handle optimizing bandwidth against quality in constrained-bandwidth situations; the value comes from the RTCDegradationPreference enumerated string type, and the default is balanced.
     * @param degradationPreference the degradationPreference to set
     */
    public void setDegradationPreference(RTCDegradationPreference degradationPreference) {
        this.degradationPreference = degradationPreference;
    }

    /**
     * An array of RTCRtpEncodingParameters objects, each specifying the parameters for a single codec that could be used to encode the track's media.
     * @return the encodings
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSendParameters/encodings
     */
    public RTCRtpEncodingParameters[] getEncodings() {
        return encodings;
    }

    /**
     * An array of RTCRtpEncodingParameters objects, each specifying the parameters for a single codec that could be used to encode the track's media.
     * @param encodings the encodings to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSendParameters/encodings
     */
    public void setEncodings(RTCRtpEncodingParameters[] encodings) {
        this.encodings = encodings;
    }

    /**
     * A string from the RTCPriorityType enumerated type which indicates the encoding's priority. The default value is low.
     * @return the priority
     */
    public RTCPriorityType getPriority() {
        return priority;
    }

    /**
     * A string from the RTCPriorityType enumerated type which indicates the encoding's priority. The default value is low.
     * @param priority the priority to set
     */
    public void setPriority(RTCPriorityType priority) {
        this.priority = priority;
    }

    /**
     * A string containing a unique ID for the last set of parameters applied; this value is used to ensure that setParameters() can only be called to alter changes made by a specific previous call to getParameters().
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * A string containing a unique ID for the last set of parameters applied; this value is used to ensure that setParameters() can only be called to alter changes made by a specific previous call to getParameters().
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    private RTCDegradationPreference degradationPreference;
    private RTCRtpEncodingParameters[] encodings;
    private RTCPriorityType priority;
    private String transactionId;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (getDegradationPreference() != null) {
            out.put("degradationPreference", getDegradationPreference().string);
        }
        if (getEncodings() != null && getEncodings().length > 0) {
            ArrayList<Map> encodingsList = new ArrayList<Map>();
            for (RTCRtpEncodingParameters enc : getEncodings()) {
                encodingsList.add((Map)enc.toJSONStruct());
            }
            out.put("encodings", encodingsList);
        }
        if (getPriority() != null) {
            out.put("priority", getPriority().string);
        }
        if (getTransactionId() != null) {
            out.put("transactionId", getTransactionId());
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
