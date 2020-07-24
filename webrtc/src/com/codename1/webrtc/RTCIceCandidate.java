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
public interface RTCIceCandidate extends JSObject {
    public String getCandidate();
    public String getComponent();
    public String getFoundation();
    public String getIp();
    public int getPort();
    public long getPriority();
    public String getRelatedAddress();
    public int getRelatedPort();
    public String getSdpMid();
    public Integer getSdpMLineIndex();
    public String getTcpType();
    public String getType();
    public String getUsernameFragment();
    public String toJSON();
    
}
