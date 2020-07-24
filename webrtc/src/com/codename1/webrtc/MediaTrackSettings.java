/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 *
 * @author shannah
 */
public class MediaTrackSettings {
    private String deviceId, groupId;

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
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
