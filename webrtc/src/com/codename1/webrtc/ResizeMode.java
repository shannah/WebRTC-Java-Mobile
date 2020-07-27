/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * Values for {@link VideoTrackSettings#getResizeMode() } and {@link VideoTrackSettings#setResizeMode(com.codename1.webrtc.ResizeMode)
 * }.
 *
 * @author shannah
 */
public enum ResizeMode implements JSONStruct {

    /**
     * The track has the resolution offered by the camera, its driver or the OS.
     */
    None("none"),
    /**
     * The track's resolution might be the result of the user agent using
     * cropping or downscaling from a higher camera resolution.
     */
    CropAndScale("crop-and-scale");
    private String string;

    ResizeMode(String str) {
        string = str;
    }

    @Override
    public Object toJSONStruct() {
        return string;
    }

    public boolean matches(String str) {
        return string.equals(str);
    }
}
