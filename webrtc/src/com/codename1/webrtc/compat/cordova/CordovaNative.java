/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;

import com.codename1.system.NativeInterface;
import com.codename1.ui.PeerComponent;
import com.codename1.util.Callback;

/**
 *
 * @author Chen
 */
public interface CordovaNative extends NativeInterface{
    public void pluginInitialize(PeerComponent browserComponent);
    public void dispose();
    public boolean execute(String callbackId, String action, String jsonArgs);
}
