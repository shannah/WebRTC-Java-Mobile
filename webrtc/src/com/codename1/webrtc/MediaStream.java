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
public interface MediaStream extends RefCounted, EventTarget {
    public String getId();
    public boolean isActive();
    public boolean isEnded();
    public ReadyState getReadyState();
    public MediaStreamTracks getAudioTracks();
    public MediaStreamTracks getVideoTracks();
    public MediaStreamTracks getTracks();
    public void addTrack(MediaStreamTrack track);
    public void removeTrack(MediaStreamTrack track);
    public MediaStream clone();
    public MediaStreamTrack getTrackById(String id);
    
    
    
    
    
    public static enum ReadyState {
        Live("live"),
        Ended("ended");
        
        private String string;
        
        ReadyState(String str) {
            string = str;
        }
    }
    
    
    
}
