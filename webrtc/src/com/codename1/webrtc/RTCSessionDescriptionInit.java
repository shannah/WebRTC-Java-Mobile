/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.processing.Result;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTCSessionDescriptionInit implements RTCSessionDescription {
    
    private RTCSdpType type;
    private String sdp;
    
    public RTCSessionDescriptionInit type(RTCSdpType type) {
        this.type = type;
        return this;
    }
    
    public RTCSessionDescriptionInit sdp(String sdp) {
        this.sdp = sdp;
        return this;
    }
    

    @Override
    public RTCSdpType getType() {
        return type;
    }

    @Override
    public String getSdp() {
        return sdp;
    }

    @Override
    public String toJSON() {
        return Result.fromContent((Map)toJSONStruct()).toString();
    }

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (type != null) {
            out.put("type", type.stringValue());
        }
        if (sdp != null) {
            out.put("sdp", sdp);
        }
        return out;
    }

    @Override
    public String toString() {
        return toJSON();
    }
    
    
    
}
