/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;

/**
 *
 * @author shannah
 */
public class RTCIceServers extends RTCList<RTCIceServer> implements JSObject {

    @Override
    public Object toJSONStruct() {
        ArrayList out = new ArrayList();
        for (RTCIceServer server : this) {
            out.add(server.toJSONStruct());
        }
        return out;
    }
    
}
