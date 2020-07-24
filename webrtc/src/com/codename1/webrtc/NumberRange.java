/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * A range of allowed numbers used in a constraint.
 * 
 * @author shannah
 * @see MediaTrackConstraints
 * @see MediaStreamConstraints
 * @see AudioTrackConstraints
 * @see VideoTrackConstraints
 */
public class NumberRange<T extends Number> {

    /**
     * @return the max
     */
    public T getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(T max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public T getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(T min) {
        this.min = min;
    }
    private T max, min;
}
