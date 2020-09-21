/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;


import com.codename1.ui.CN;
import static com.codename1.ui.CN.callSerially;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.util.Map;

/**
 *
 * @author Chen
 */
public class CordovaCallback {
    
    public class ResponseAction extends ActionEvent {
        private Map response;
        public ResponseAction(CordovaCallback callback, Map response) {
            super(callback);
            this.response = response;
        }
        
        public Map getResponse() {
            return response;
        }
        
        public CordovaCallback getCallback() {
            return (CordovaCallback)getSource();
        }
        
        public boolean isError() {
            return response != null && response.get("error") != null;
        }
    }
    
    
    
    private ActionListener listener;

    public CordovaCallback() {
    }

    public CordovaCallback(ActionListener listener) {
        this.listener = listener;
    }
    
    
    public void sendResult(Map json){
        if(listener != null){
            callSerially(()-> {
                listener.actionPerformed(new ResponseAction(this, json)); 
            });
        }
    }

    
    
    
}
