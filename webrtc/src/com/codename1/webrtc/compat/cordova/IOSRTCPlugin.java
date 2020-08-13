/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;

import ca.weblite.codename1.json.JSONArray;
import com.codename1.ui.BrowserComponent;
import com.codename1.util.Callback;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author shannah
 */
public class IOSRTCPlugin implements CordovaPlugin {
    private Cordova cordova;
    private boolean initialized;
    
    public IOSRTCPlugin(BrowserComponent webview) {
        cordova = new Cordova(webview);
    }

    @Override
    public boolean execute(String callbackId, String action, List args, Callback callback) {
        String jsonArgs = new JSONArray(args).toString();
        
        return execute(callbackId, action, jsonArgs, callback);
        
    }

    @Override
    public boolean execute(String callbackId, String action, String jsonArgs, Callback callback) {
        System.out.println("IOSRTCPlugin#"+action);
        if (cordova == null) {
            throw new IllegalStateException("IosrtcPlugin already disposed");
        }
        try {
            return cordova.execute(callbackId, action, jsonArgs, new CordovaCallback(evt->{
                System.out.println("EmbeddedCordovaApplication#"+action+" callback "+jsonArgs);
                CordovaCallback cdvCallback = (CordovaCallback)evt.getSource();
                if (cdvCallback.isError()) {
                    callback.onError(this, new IOException((String)cdvCallback.getResponse().get("error")), 0, (String)cdvCallback.getResponse().get("error"));
                } else {
                    callback.onSucess(cdvCallback.getResponse());
                }
            }));
        } catch (IOException ex) {
            callback.onError(this, ex, 0, ex.getMessage());
            return false;
        }
    }
    
    @Override
    public void dispose() {
        if (cordova != null) {
            cordova.dispose();
            cordova = null;
        }
    }

    @Override
    public void pluginInitialize() {
        if (!initialized) {
            initialized = true;
            cordova.pluginInitialize();
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    
    
}
