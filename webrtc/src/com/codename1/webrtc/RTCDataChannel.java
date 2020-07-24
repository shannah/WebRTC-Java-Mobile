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
public interface RTCDataChannel extends EventTarget, RefCounted {
    public String getBinaryType();
    public int getBufferedAmount();
    public int getBufferedAmountLowThreshold();
    public int getId();
    public String getLabel();
    public Integer getMaxPacketLifeTime();
    public Integer getMaxRetransmits();
    public boolean isNegotiated();
    public String getProtocol();
    public RTCDataChannelState getReadyState();
    public void close();
    public void send(byte[] bytes);
    public void send(Blob blob);
    public void send(String string);
    
    
    public static enum RTCDataChannelState {
        Connecting("connecting"),
        Open("open"),
        Closing("closing"),
        Closed("closed");
        
        private String string;
        
        RTCDataChannelState(String str) {
            this.string = str;
        }

        public boolean matches(String readyStateStr) {
            return string.equals(readyStateStr);
        }
    }
    
}
