/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.system.NativeInterface;

/**
 * Native interface used internally.
 * @author shannah
 */
public interface WebRTCNative extends NativeInterface {
    
    /**
     * Request native permissions for microphone and/or camera.  The native implementation *must*
     * call the specified callback ID via {@link RTC#permissionCallback(java.lang.String, boolean, boolean) } when
     * it has completed the permissions request.
     * 
     * @param callbackId The callback ID.  The native implementation *must* use {@link RTC#permissionCallback(java.lang.String, boolean, boolean) } to
     * call this callback.
     * @param video Whether to request camera permissions.
     * @param audio Whether to request microphone permissions.
     */
    public void requestPermissions(String callbackId, boolean video, boolean audio);
}
