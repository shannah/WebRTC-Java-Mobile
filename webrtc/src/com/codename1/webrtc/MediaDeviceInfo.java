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
public interface MediaDeviceInfo {
    String getDeviceId();
    String getGroupId();
    Kind getKind();
    String getLabel();
    
    public static enum Kind {
        VideoInput("videoinput"),
        AudioInput("audioinput"),
        AudioOutput("audiooutput");
        
        private String id;
        Kind(String id) {
            this.id = id;
        }
        
        public static Kind fromString(String str) {
            if ("videoinput".equals(str)) {
                return VideoInput;
            }
            if ("audioinput".equals(str)) {
                return AudioInput;
            }
            if ("audiooutput".equals(str)) {
                return AudioOutput;
            }
            throw new IllegalArgumentException("Unrecognized kind string: "+str);
        }

        @Override
        public String toString() {
            return id;
        }
        
        
    }
}
