/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;

import com.codename1.io.JSONParser;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.Callback;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chen
 */
public class CordovaCallbackManager {
    
    private static Map callbacks = new HashMap();

    public static void setMethodCallback(String callbackId, CordovaCallback cb){
        
        callbacks.put(callbackId, cb);
    }
    
    /**
     * Utility wrapper for the {@link JSONParser#parseJSON(java.io.Reader) } that configures it
     * with booleans instead of strings, just how we like.
     * @param json
     * @return
     * @throws IOException 
     */
    private static Map parseJSON(String json) throws IOException {
        JSONParser p = new JSONParser();
        p.setUseBooleanInstance(true);
        p.setIncludeNullsInstance(true);
        
        return p.parseJSON(new StringReader(json));
        
    }

   
    public static void sendResult(String callbackId, boolean isSuccess, int status, String args, boolean keepCallback){
        try {
            
            Map json = parseJSON(args);
            CordovaCallback cb = (CordovaCallback)callbacks.get(callbackId);
            if (!keepCallback) {
                callbacks.remove(callbackId);
            }
            Map result = new HashMap();
            result.put("message", json);
            result.put("status", status);
            result.put("keepCallback", keepCallback);
            result.put("isSuccess", isSuccess);
            cb.sendResult(result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
