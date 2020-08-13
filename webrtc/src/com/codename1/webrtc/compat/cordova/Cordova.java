/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;


import com.codename1.io.Log;
import com.codename1.system.NativeLookup;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.CN;
import com.codename1.ui.PeerComponent;
import com.codename1.ui.WebRTCAccessor;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Chen
 */
public class Cordova {

    private CordovaNative cordova;
    private BrowserComponent webview;
    private boolean initialized;
    private List<Runnable> onReady = new LinkedList<Runnable>();
    private static final int TIMEOUT = 10000;
    private long initializeStart = System.currentTimeMillis();

    public Cordova(BrowserComponent webview) {
        cordova = (CordovaNative) NativeLookup.create(CordovaNative.class);
        this.webview = webview;
    }
    
    public void pluginInitialize() {
        if (!CN.isEdt()) {
            CN.callSerially(()->pluginInitialize());
            return;
        }
        if (initialized) {
            return;
        }
        PeerComponent internal = WebRTCAccessor.getInternal(webview);
        if (internal == null) {
            if (System.currentTimeMillis() - initializeStart > TIMEOUT) {
                throw new RuntimeException("Failed to initialize Cordova after timeout of "+TIMEOUT+"ms");
            }
            CN.setTimeout(1, ()->{
                pluginInitialize();
            });
            return;
        }
        
        cordova.pluginInitialize(internal);
        initialized = true;
        while (!onReady.isEmpty()) {
            try {
                onReady.remove(0).run();
            } catch (Throwable t) {
                Log.p("Exception in ready callback after initializing Cordova");
                Log.e(t);
            }
        }
        
        
        
        
    }
    
    public boolean execute(String callbackId, String action, String jsonArgs, CordovaCallback callback) throws IOException{
        if(cordova == null || !cordova.isSupported()){
            throw new RuntimeException("Not supported on this platform");
        }
        if (!initialized) {
            
            onReady.add(()-> {
                try {
                    execute(callbackId, action, jsonArgs, callback);
                } catch (Throwable t) {
                    Log.p("Exception occurred in Cordova.execute() using deferred execution.  Action="+action+", jsonArgs="+jsonArgs);
                    Log.e(t);
                }
            });
            
            return true;
        }
        if (callbackId != null) {
            
        }
        CordovaCallbackManager.setMethodCallback(callbackId, callback);
        System.out.println("Cordova#execute#"+action);
        boolean executed = cordova.execute(callbackId, action, jsonArgs);        
        if(executed){
            if(callback.isError()){
                throw new IOException((String)callback.getResponse().get("message"));
                
            }
        }
        return executed;
    }
    
    public void dispose() {
        if (!initialized) {
            onReady.add(()->dispose());
            return;
        }
        cordova.dispose();
        cordova = null;
    }

    
}
