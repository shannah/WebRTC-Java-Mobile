/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * A range of times.
 * @author shannah
 */
public interface TimeRanges {
    public int getLength();
    public double getStart(int index);
    public double getEnd(int index);
}
