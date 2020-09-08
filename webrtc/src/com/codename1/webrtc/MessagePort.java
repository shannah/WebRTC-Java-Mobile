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
public interface MessagePort extends EventTarget {
    public void postMessage(String message);
    public void postMessage(int message);
    public void postMessage(double message);
    public void postMessage(byte[] message);
    public void postMessage(String[] message);
    public void postMessage(int[] message);
    public void postMessage(double[] message);
    public void start();
    public void close();
}
