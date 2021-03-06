/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.Map;

/**
 * A list of RTCCertificate objects.
 * @author shannah
 */
public class RTCCertificates extends RTCList<RTCCertificate> implements JSONStruct {

    
    
    @Override
    public Object toJSONStruct() {
        ArrayList out = new ArrayList();
        for (RTCCertificate cert : this) {
            out.add(cert.toJSONStruct());
        }
        return out;
    }
    
}
