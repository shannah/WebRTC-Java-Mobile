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
public interface RTCDTMFSender extends EventTarget {
    public String getToneBuffer();
    
    public void insertDTMF(String tones, int duration, int interToneGap);
    public void insertDTMF(String tones, int duration);
    public void insertDTMF(String tones);
}
