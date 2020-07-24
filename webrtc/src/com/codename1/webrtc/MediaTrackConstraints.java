/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class MediaTrackConstraints implements JSObject {

    public MediaTrackConstraints(MediaStreamConstraints parent) {
        this.parent = parent;
    }
    
    /**
     * @return the deviceId
     */
    public ConstrainString getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(ConstrainString deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the groupId
     */
    public ConstrainString getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(ConstrainString groupId) {
        this.groupId = groupId;
    }
    private MediaStreamConstraints parent;
    private ConstrainString deviceId;
    private ConstrainString groupId;

    @Override
    public Object toJSONStruct() {
        Map out = new HashMap();
        if (deviceId != null) {
            out.put("deviceId", toJSONStruct(deviceId));
        }
        if (groupId != null) {
            out.put("groupId", toJSONStruct(groupId));
        }
        return out;
    }
    
    private static Object toJSONStruct(Object o) {
        if (o instanceof JSObject) {
            return ((JSObject)o).toJSONStruct();
            
        }
        return null;
    }
    
    public MediaStreamConstraints getParent() {
        return parent;
    }
    
    public AudioTrackConstraints audio() {
        AudioTrackConstraints audio = parent.getAudioConstraints();
        if (audio == null) {
            audio = new AudioTrackConstraints(parent);
            parent.setAudioConstraints(audio);
        }
        return audio;
    }
    
    public VideoTrackConstraints video() {
        VideoTrackConstraints video = parent.getVideoConstraints();
        if (video == null) {
            video = new VideoTrackConstraints(parent);
            parent.setVideoConstraints(video);
        }
        return video;
    }
    
    public MediaStreamConstraints stream() {
        return getParent();
    }
    
}
