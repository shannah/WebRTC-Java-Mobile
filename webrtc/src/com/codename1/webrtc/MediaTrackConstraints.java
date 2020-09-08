/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The MediaTrackConstraints dictionary is used to describe a set of
 * capabilities and the value or values each can take on. A constraints
 * dictionary is passed into applyConstraints() to allow a script to establish a
 * set of exact (required) values or ranges and/or preferred values or ranges of
 * values for the track, and the most recently-requested set of custom
 * constraints can be retrieved by calling getConstraints().
 *
 * For each constraint, you can typically specify an exact value you need, an
 * ideal value you want, a range of acceptable values, and/or a value which
 * you'd like to be as close to as possible. The specifics vary somewhat
 * depending on the type of the constrainable property.
 *
 * @author shannah
 */
public class MediaTrackConstraints implements JSONStruct {

    private MediaStreamConstraints parent;
    private ConstrainString deviceId;
    private ConstrainString groupId;
    private Map optional;
    
    public MediaTrackConstraints(MediaStreamConstraints parent) {
        this.parent = parent;
    }

    /**
     * A ConstrainDOMString object specifying a device ID or an array of device IDs which are acceptable and/or required.
     * @return the deviceId
     */
    public ConstrainString getDeviceId() {
        return deviceId;
    }

    /**
     * A ConstrainDOMString object specifying a group ID or an array of group IDs which are acceptable and/or required.
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(ConstrainString deviceId) {
        this.deviceId = deviceId;
    }
    
    public MediaTrackConstraints deviceId(ConstrainString deviceId) {
        setDeviceId(deviceId);
        return this;
    }

    /**
     * @return the groupId
     */
    public ConstrainString getGroupId() {
        return groupId;
    }

    /**
     * A ConstrainDOMString object specifying a group ID or an array of group IDs which are acceptable and/or required.
     * @param groupId the groupId to set
     */
    public void setGroupId(ConstrainString groupId) {
        this.groupId = groupId;
    }
    
    
    public MediaTrackConstraints optional(String key, Object val) {
        if (optional == null) optional = new HashMap();
        optional.put(key, val);
        return this;
    }
    

    @Override
    public Object toJSONStruct() {
        Map out = new HashMap();
        if (deviceId != null) {
            out.put("deviceId", toJSONStruct(deviceId));
        }
        if (groupId != null) {
            out.put("groupId", toJSONStruct(groupId));
        }
        if (optional != null) {
            out.put("optional", new ArrayList(Arrays.asList(optional)));
        }
        return out;
    }

    private static Object toJSONStruct(Object o) {
        if (o instanceof JSONStruct) {
            return ((JSONStruct) o).toJSONStruct();

        }
        return null;
    }

    /**
     * Gets the {@link MediaStreamConstraints} object that this track constraints is associated with.
     * @return 
     */
    public MediaStreamConstraints getParent() {
        return parent;
    }

    /**
     * Gets the {@link AudioTrackConstraints} that this constraints object is associated with.
     * @return 
     */
    public AudioTrackConstraints audio() {
        AudioTrackConstraints audio = parent.getAudioConstraints();
        if (audio == null) {
            audio = new AudioTrackConstraints(parent);
            parent.setAudioConstraints(audio);
        }
        return audio;
    }

    /**
     * Gets the {@link VideoTrackConstraints} that this constraints object is associated with.
     * @return 
     */
    public VideoTrackConstraints video() {
        VideoTrackConstraints video = parent.getVideoConstraints();
        if (video == null) {
            video = new VideoTrackConstraints(parent);
            parent.setVideoConstraints(video);
        }
        return video;
    }

    /**
     * Gets the MediaStreamConstraints object that this constraints object is associated with.
     * @return 
     */
    public MediaStreamConstraints stream() {
        return getParent();
    }

}
