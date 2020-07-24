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
public interface RTCElement {
    public void setId(String id);
    public String getId();
    
    // Sets element dimensions to fill the RTC component.
    public void fill();
    
    
    public void applyStyle(RTCStyle style);
    public void applyStyle(String css);
    
}
