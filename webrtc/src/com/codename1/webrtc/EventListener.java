/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The EventListener interface represents an object that can handle an event dispatched by an {@link EventTarget} object.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/EventListener
 */
public interface EventListener<T extends Event> {
    /**
     * Triggered to handle an event.
     * @param evt 
     */
    public void handleEvent(T evt);
}
