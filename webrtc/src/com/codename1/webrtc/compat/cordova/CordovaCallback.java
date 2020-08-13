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
    
    private Map response;
    
    private ActionListener listener;

    public CordovaCallback() {
    }

    public CordovaCallback(ActionListener listener) {
        this.listener = listener;
    }
    
    
    public void sendResult(Map json){
        this.response = json;
        if(listener != null){
            callSerially(()-> {
                listener.actionPerformed(new ActionEvent(this)); 
            });
        }
    }

    public Map getResponse() {
        return response;
    }
    
    public boolean isError(){
        return response != null && response.get("error") != null;
    }
    
    
    
}
