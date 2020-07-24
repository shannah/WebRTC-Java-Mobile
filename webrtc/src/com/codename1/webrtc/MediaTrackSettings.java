/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 * The MediaTrackSettings dictionary is used to return the current values configured for each of a MediaStreamTrack's settings. These values will adhere as closely as possible to any constraints previously described using a MediaTrackConstraints object and set using applyConstraints(), and will adhere to the default constraints for any properties whose constraints haven't been changed, or whose customized constraints couldn't be matched.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings
 */
public class MediaTrackSettings {
    private String deviceId, groupId;

    /**
     * A DOMString indicating the current value of the deviceId property. The device ID is a origin-unique string identifying the source of the track; this is usually a GUID. This value is specific to the source of the track's data and is not usable for setting constraints; it can, however, be used for initially selecting media when calling {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
     * @return the deviceId
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * A DOMString indicating the current value of the deviceId property. The device ID is a origin-unique string identifying the source of the track; this is usually a GUID. This value is specific to the source of the track's data and is not usable for setting constraints; it can, however, be used for initially selecting media when calling {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
     * @param deviceId the deviceId to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * A DOMString indicating the current value of the groupId property. The group ID is a browsing session-unique string identifying the source group of the track. Two devices (as identified by the deviceId) are considered part of the same group if they are from the same physical device. For instance, the audio input and output devices for the speaker and microphone built into a phone would share the same group ID, since they're part of the same physical device. The microphone on a headset would have a different ID, though. This value is specific to the source of the track's data and is not usable for setting constraints; it can, however, be used for initially selecting media when calling {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
     * @return the groupId
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * A DOMString indicating the current value of the groupId property. The group ID is a browsing session-unique string identifying the source group of the track. Two devices (as identified by the deviceId) are considered part of the same group if they are from the same physical device. For instance, the audio input and output devices for the speaker and microphone built into a phone would share the same group ID, since they're part of the same physical device. The microphone on a headset would have a different ID, though. This value is specific to the source of the track's data and is not usable for setting constraints; it can, however, be used for initially selecting media when calling {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
     * @param groupId the groupId to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public void fromJSONStruct(Object struct) {
        if (struct instanceof Map) {
            Map m = (Map)struct;
            groupId = (String)m.get("groupId");
            deviceId = (String)m.get("deviceId");
            
        }
    }
}
