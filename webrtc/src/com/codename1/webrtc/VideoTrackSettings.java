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
    public Double getAspectRatio() {
        return aspectRatio;
    }

    /**
     * @param aspectRatio the aspectRatio to set
     */
    public void setAspectRatio(Double aspectRatio) {
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
    public Double getFrameRate() {
        return frameRate;
    }

    /**
     * @param frameRate the frameRate to set
     */
    public void setFrameRate(Double frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Integer height) {
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
            MapWrap w = new MapWrap(m);
            if (w.has("aspectRatio")) {
                aspectRatio = ((Number)m.get("aspectRatio")).doubleValue();
            } else {
                aspectRatio = null;
            }
            for (FacingMode mode : FacingMode.values()) {
                if (mode.matches((String)m.get("facingMode"))) {
                    facingMode = mode;
                    break;
                }
            }
            if (w.has("frameRate")) {
                frameRate = ((Number)m.get("frameRate")).doubleValue();
            } else {
                frameRate = null;
            }
            if (w.has("width")) {
                width = ((Number)m.get("width")).intValue();
            } else {
                width = null;
            }
            if (w.has("height")) {
                height = ((Number)m.get("height")).intValue();
            } else {
                height = null;
            }
            for (ResizeMode mode : ResizeMode.values()) {
                if (mode.matches((String)m.get("resizeMode"))) {
                    resizeMode = mode;
                    break;
                }
            }
        }
    }
    
    
    
    private Double aspectRatio;
    private FacingMode facingMode;
    private Double frameRate;
    private Integer width;
    private Integer height;
    private ResizeMode resizeMode;
   
}
