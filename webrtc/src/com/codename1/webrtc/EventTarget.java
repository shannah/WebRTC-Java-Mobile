/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * EventTarget is a DOM interface implemented by objects that can receive events and may have listeners for them.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/EventTarget
 */
public interface EventTarget {
    
    /**
     * Adds an event listener to respond to events of the given type.
     * @param eventName The type of event.
     * @param listener Listener to register to receive events.
     * @see https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener
     */
    public void addEventListener(String eventName, EventListener listener);
    
    /**
     * Removes event listener.
     * @param eventName THe type of event.
     * @param listener The listener
     * @see https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/removeEventListener
     */
    public void removeEventListener(String eventName, EventListener listener);
    
    /**
     * Dispatches the given event to registered event listenerse.
     * @param evt
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/dispatchEvent
     * 
     */
    public boolean dispatchEvent(Event evt);
}
