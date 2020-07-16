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
public enum TrackKind {
    Audio("audio"),
    Video("video");
    private String string;
    TrackKind(String str) {
        string = str;
    }
    
    public boolean matches(String str) {
        return string.equals(str);
    }

    @Override
    public String toString() {
        return string;
    }
    
    
}
