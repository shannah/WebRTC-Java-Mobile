/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.JSONParser;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A list of RTCIceServer objects
 * @author shannah
 */
public class RTCIceServers extends RTCList<RTCIceServer> implements JSONStruct {
    
    public RTCIceServers() {
        
    }
    
    /**
     * Creates a RTCIceServers from a list of servers.  
     * @param servers List of servers.  Each server can be either a Map or a RTCIceServer.
     */
    public RTCIceServers(Iterable servers) {
        for (Object o : servers) {
            if (o instanceof RTCIceServer) {
                add((RTCIceServer)o);
            } else if (o instanceof Map) {
                add(new RTCIceServer((Map)o));
            }
        }
    }
    

    @Override
    public Object toJSONStruct() {
        ArrayList out = new ArrayList();
        for (RTCIceServer server : this) {
            out.add(server.toJSONStruct());
        }
        return out;
    }
    
    
}
