/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 *
 * @author shannah
 */
public class VideoTrackSettings extends MediaTrackSettings {

    /**
     * @return the aspectRatio
     */
    public double getAspectRatio() {
        return aspectRatio;
    }

    /**
     * @param aspectRatio the aspectRatio to set
     */
    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * @return the facingMode
     */
    public FacingMode getFacingMode() {
        return facingMode;
    }

    /**
     * @param facingMode the facingMode to set
     */
    public void setFacingMode(FacingMode facingMode) {
        this.facingMode = facingMode;
    }

    /**
     * @return the frameRate
     */
    public double getFrameRate() {
        return frameRate;
    }

    /**
     * @param frameRate the frameRate to set
     */
    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the resizeMode
     */
    public ResizeMode getResizeMode() {
        return resizeMode;
    }

    /**
     * @param resizeMode the resizeMode to set
     */
    public void setResizeMode(ResizeMode resizeMode) {
        this.resizeMode = resizeMode;
    }

    @Override
    public void fromJSONStruct(Object struct) {
        super.fromJSONStruct(struct);
        if (struct instanceof Map) {
            Map m = (Map)struct;
            aspectRatio = ((Number)m.get("aspectRatio")).doubleValue();
            for (FacingMode mode : FacingMode.values()) {
                if (mode.matches((String)m.get("facingMode"))) {
                    facingMode = mode;
                    break;
                }
            }
            frameRate = ((Number)m.get("frameRate")).doubleValue();
            width = ((Number)m.get("width")).intValue();
            height = ((Number)m.get("height")).intValue();
            for (ResizeMode mode : ResizeMode.values()) {
                if (mode.matches((String)m.get("resizeMode"))) {
                    resizeMode = mode;
                    break;
                }
            }
        }
    }
    
    
    
    private double aspectRatio;
    private FacingMode facingMode;
    private double frameRate;
    private int width;
    private int height;
    private ResizeMode resizeMode;
   
}
