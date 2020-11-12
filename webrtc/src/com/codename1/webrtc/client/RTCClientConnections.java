/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shannah
 */
public class RTCClientConnections implements Iterable<RTCClientConnection> {
    private List<RTCClientConnection> list = new ArrayList<>();
    @Override
    public Iterator<RTCClientConnection> iterator() {
        return list.iterator();
    }
    
    public void add(RTCClientConnection conn) {
        list.add(conn);
    }
    
    public boolean remove(RTCClientConnection conn) {
        return list.remove(conn);
    }
    
    public int size() {
        return list.size();
    }
    
    public RTCClientConnection get(int index) {
        return list.get(index);
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public RTCClientConnections getConnectionsToUser(String username) {
        RTCClientConnections out = new RTCClientConnections();
        for (RTCClientConnection c : this) {
            if (username.equals(c.getRemoteUsername())) {
                out.add(c);
            }
        }
        return out;
    }
    
    
    public boolean contains(RTCClientConnection conn) {
        return list.contains(conn);
    }
}
