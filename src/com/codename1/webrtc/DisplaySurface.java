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
public enum DisplaySurface {
    Application("application"),
    Browser("browser"),
    Monitor("monitor"),
    Window("window");
    private String string;
    DisplaySurface(String str) {
        string = str;
    }
}