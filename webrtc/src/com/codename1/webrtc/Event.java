/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The Event interface represents an event that takes place in the RTC instance.  Any class that implements
 * {@link EventTarget} may have {@link EventListener} objects registered that listen for events of a given type.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/Event
 */
public interface Event {
    /**
     * The event type.  E.g. "icecandidate", "track", etc..
     * @return 
     */
    public String getType();
    
    /**
     * Prevents the event from propagating further.
     */
    public void preventDefault();
    
    
}
