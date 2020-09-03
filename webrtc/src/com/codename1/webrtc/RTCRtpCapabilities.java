/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTCRtpCapabilities implements Iterable<RTCRtpCodecCapability> {

    /**
     * @return the codecs
     */
    public RTCRtpCodecCapability[] getCodecs() {
        return codecs;
    }

    /**
     * @return the headerExtensions
     */
    public RTCRtpHeaderExtensionCapability[] getHeaderExtensions() {
        return headerExtensions;
    }
    private RTCRtpCodecCapability[] codecs;
    private RTCRtpHeaderExtensionCapability[] headerExtensions;
    
    
    RTCRtpCapabilities(Map data) {
        List codecs = (List)data.get("codecs");
        if (codecs != null) {
            int len = codecs.size();
            this.codecs = new RTCRtpCodecCapability[len];
            for (int i=0; i<len; i++) {
                this.codecs[i] = new RTCRtpCodecCapability((Map)codecs.get(i));
            }
            
        }
        List headerExtensions = (List)data.get("headerExtensions");
        if (headerExtensions != null) {
            int len = headerExtensions.size();
            this.headerExtensions = new RTCRtpHeaderExtensionCapability[len];
            for (int i=0; i<len; i++) {
                this.headerExtensions[i] = new RTCRtpHeaderExtensionCapability((Map)headerExtensions.get(i));
            }
        }
       
    }

    @Override
    public Iterator<RTCRtpCodecCapability> iterator() {
        return Arrays.asList(getCodecs()).iterator();
    }
    
    
}


