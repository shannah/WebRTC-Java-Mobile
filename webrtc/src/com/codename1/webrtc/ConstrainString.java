/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * A constraint used by {@link MediaTrackConstraints}, {@link AudioTrackConstraints}, and {@link VideoTrackConstraints}
 * to constrain the allowed values of certain constraints.
 * 
 * <p>There are some static convenience methods in the {@link RTC} class for creating exact and ideal constraints.
 * @author shannah
 * @see RTC#exact(java.lang.String) 
 * @see RTC#ideal(java.lang.String) 
 */
public class ConstrainString extends ConstrainObject<String> {
    
   
    public ConstrainString exact(String str) {
        return (ConstrainString)super.exact(str);
    }

}
