/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
 */
public interface EventTarget {
    public void addEventListener(String eventName, EventListener listener);
    public void removeEventListener(String eventName, EventListener listener);
    public boolean dispatchEvent(Event evt);
}
