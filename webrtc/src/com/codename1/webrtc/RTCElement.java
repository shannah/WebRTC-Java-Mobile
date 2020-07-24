/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCElement interface represents any HTML element in the DOM of the RTC instance.. Some elements directly implement this interface, while others implement it via an interface that inherits it.
 * @author shannah
 */
public interface RTCElement {
    
    
    /**
     * Sets the ID of the element.
     * @param id 
     */
    public void setId(String id);
    
    /**
     * Gets the ID of the element
     * @return 
     */
    public String getId();
    
    // Sets element dimensions to fill the RTC component.
    /**
     * Sets the styles on the element to fill the bounds of the RTC visual component.
     */
    public void fill();
    
    
    /**
     * Applies the given CSS styles to the element
     * @param style 
     */
    public void applyStyle(RTCStyle style);
    
    /**
     * Applies the given CSS styles to the element.
     * @param css 
     */
    public void applyStyle(String css);
    
}
