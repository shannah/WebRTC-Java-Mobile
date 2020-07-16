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
public interface MediaStreamTrack extends RefCounted, EventTarget {
    public String getContentHint();
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
    public String getId();
    public TrackKind getKind();
    public String getLabel();
    public boolean isMuted();
    public boolean isReadOnly();
    public ReadyState getReadyState();
    public boolean isRemote();
    
    public RTCPromise applyConstraints(MediaTrackConstraints constraints);
    public MediaStreamTrack clone();
    public MediaTrackConstraints getConstraints();
    public MediaTrackSettings getSettings();
    public void stop();
    
    public static enum ReadyState {
        Live("live"),
        Ended("ended");
        private String string;
        ReadyState(String str) {
            string = str;
        }
        public boolean matches(String str) {
            return string.equals(str);
        }
    }
    
   
}
