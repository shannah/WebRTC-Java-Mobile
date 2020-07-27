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
public class VideoTrackConstraints extends MediaTrackConstraints {

    
    public VideoTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }
    
    /**
     * @return the aspectRatio
     */
    public ConstrainNumber<Double> getAspectRatio() {
        return aspectRatio;
    }

    /**
     * @param aspectRatio the aspectRatio to set
     */
    public void setAspectRatio(ConstrainNumber<Double> aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
    
    public VideoTrackConstraints aspectRatio(ConstrainNumber<Double> aspectRatio) {
        setAspectRatio(aspectRatio);
        return this;
    }

    /**
     * @return the frameRate
     */
    public ConstrainNumber<Double> getFrameRate() {
        return frameRate;
    }

    /**
     * @param frameRate the frameRate to set
     */
    public void setFrameRate(ConstrainNumber<Double> frameRate) {
        this.frameRate = frameRate;
    }
    
    public VideoTrackConstraints frameRate(ConstrainNumber<Double> frameRate) {
        setFrameRate(frameRate);
        return this;
    }

    /**
     * @return the height
     */
    public ConstrainNumber<Integer> getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(ConstrainNumber<Integer> height) {
        this.height = height;
    }
    
    public VideoTrackConstraints height(ConstrainNumber<Integer> height) {
        setHeight(height);
        return this;
    }

    /**
     * @return the width
     */
    public ConstrainNumber<Integer> getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(ConstrainNumber<Integer> width) {
        this.width = width;
    }
    
    public VideoTrackConstraints width(ConstrainNumber<Integer> width) {
        setWidth(width);
        return this;
    }

    /**
     * @return the facingMode
     */
    public ConstrainFacingMode getFacingMode() {
        return facingMode;
    }

    /**
     * @param facingMode the facingMode to set
     */
    public void setFacingMode(ConstrainFacingMode facingMode) {
        this.facingMode = facingMode;
    }
    
    public VideoTrackConstraints facingMode(ConstrainFacingMode facingMode) {
        setFacingMode(facingMode);
        return this;
    }

    /**
     * @return the resizeMode
     */
    public ConstrainResizeMode getResizeMode() {
        return resizeMode;
    }

    /**
     * @param resizeMode the resizeMode to set
     */
    public void setResizeMode(ConstrainResizeMode resizeMode) {
        this.resizeMode = resizeMode;
    }
    
    public VideoTrackConstraints resizeMode(ConstrainResizeMode resizeMode) {
        setResizeMode(resizeMode);
        return this;
    }
    
    private ConstrainNumber<Double> aspectRatio, frameRate;
    private ConstrainNumber<Integer> height, width;
    
    private ConstrainFacingMode facingMode;
    private ConstrainResizeMode resizeMode;
    
    
    private static Object toJSONStruct(Object o) {
        if (o instanceof JSONStruct) {
            return ((JSONStruct)o).toJSONStruct();
            
        }
        return null;
    }

    @Override
    public Object toJSONStruct() {
        Map out = (Map)super.toJSONStruct();
        if (aspectRatio != null) {
            out.put("aspectRatio", toJSONStruct(aspectRatio));
        }
        if (frameRate != null) {
            out.put("frameRate", toJSONStruct(frameRate));
        }
        if (height != null) {
            out.put("height", toJSONStruct(height));
        }
        if (width != null) {
            out.put("width", toJSONStruct(width));
        }
        if (facingMode != null) {
            out.put("facingMode", toJSONStruct(facingMode));
        }
        if (resizeMode != null) {
            out.put("resizeMode", toJSONStruct(resizeMode));
        }
        return out;
    }
    
    
    
    
    
    
    public static class ConstrainFacingMode extends ConstrainObject<FacingMode> {
        
    }
    
   
    
    public static class ConstrainResizeMode extends ConstrainObject<ResizeMode> {
        
    }
    
    public VideoTrackConstraints audio(boolean audio) {
        getParent().audio(audio);
        return this;
    }
    
}
