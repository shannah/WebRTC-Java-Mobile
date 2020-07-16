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
public class ImageTrackConstraints extends MediaTrackConstraints {

    public ImageTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }
    
    /**
     * @return the whiteBalanceMode
     */
    public WhiteBalanceMode getWhiteBalanceMode() {
        return whiteBalanceMode;
    }

    /**
     * @param whiteBalanceMode the whiteBalanceMode to set
     */
    public void setWhiteBalanceMode(WhiteBalanceMode whiteBalanceMode) {
        this.whiteBalanceMode = whiteBalanceMode;
    }

    /**
     * @return the exposureMode
     */
    public ExposureMode getExposureMode() {
        return exposureMode;
    }

    /**
     * @param exposureMode the exposureMode to set
     */
    public void setExposureMode(ExposureMode exposureMode) {
        this.exposureMode = exposureMode;
    }

    /**
     * @return the focusMode
     */
    public FocusMode getFocusMode() {
        return focusMode;
    }

    /**
     * @param focusMode the focusMode to set
     */
    public void setFocusMode(FocusMode focusMode) {
        this.focusMode = focusMode;
    }

    /**
     * @return the pointsOfInterest
     */
    public PointsOfInterest getPointsOfInterest() {
        return pointsOfInterest;
    }

    /**
     * @param pointsOfInterest the pointsOfInterest to set
     */
    public void setPointsOfInterest(PointsOfInterest pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    /**
     * @return the exposureCompensation
     */
    public ConstrainNumber<Double> getExposureCompensation() {
        return exposureCompensation;
    }

    /**
     * @param exposureCompensation the exposureCompensation to set
     */
    public void setExposureCompensation(ConstrainNumber<Double> exposureCompensation) {
        this.exposureCompensation = exposureCompensation;
    }

    /**
     * @return the colorTemperature
     */
    public ConstrainNumber<Double> getColorTemperature() {
        return colorTemperature;
    }

    /**
     * @param colorTemperature the colorTemperature to set
     */
    public void setColorTemperature(ConstrainNumber<Double> colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    /**
     * @return the iso
     */
    public ConstrainNumber<Double> getIso() {
        return iso;
    }

    /**
     * @param iso the iso to set
     */
    public void setIso(ConstrainNumber<Double> iso) {
        this.iso = iso;
    }

    /**
     * @return the brightness
     */
    public ConstrainNumber<Double> getBrightness() {
        return brightness;
    }

    /**
     * @param brightness the brightness to set
     */
    public void setBrightness(ConstrainNumber<Double> brightness) {
        this.brightness = brightness;
    }

    /**
     * @return the contrast
     */
    public ConstrainNumber<Double> getContrast() {
        return contrast;
    }

    /**
     * @param contrast the contrast to set
     */
    public void setContrast(ConstrainNumber<Double> contrast) {
        this.contrast = contrast;
    }

    /**
     * @return the saturation
     */
    public ConstrainNumber<Double> getSaturation() {
        return saturation;
    }

    /**
     * @param saturation the saturation to set
     */
    public void setSaturation(ConstrainNumber<Double> saturation) {
        this.saturation = saturation;
    }

    /**
     * @return the sharpness
     */
    public ConstrainNumber<Double> getSharpness() {
        return sharpness;
    }

    /**
     * @param sharpness the sharpness to set
     */
    public void setSharpness(ConstrainNumber<Double> sharpness) {
        this.sharpness = sharpness;
    }

    /**
     * @return the focusDistance
     */
    public ConstrainNumber<Double> getFocusDistance() {
        return focusDistance;
    }

    /**
     * @param focusDistance the focusDistance to set
     */
    public void setFocusDistance(ConstrainNumber<Double> focusDistance) {
        this.focusDistance = focusDistance;
    }

    /**
     * @return the zoom
     */
    public ConstrainNumber<Double> getZoom() {
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(ConstrainNumber<Double> zoom) {
        this.zoom = zoom;
    }

    /**
     * @return the torch
     */
    public Boolean getTorch() {
        return torch;
    }

    /**
     * @param torch the torch to set
     */
    public void setTorch(Boolean torch) {
        this.torch = torch;
    }
    private WhiteBalanceMode whiteBalanceMode;
    private ExposureMode exposureMode;
    private FocusMode focusMode;
    private PointsOfInterest pointsOfInterest;
    private ConstrainNumber<Double> exposureCompensation;
    private ConstrainNumber<Double> colorTemperature;
    private ConstrainNumber<Double> iso, brightness, contrast, saturation, sharpness, focusDistance, zoom;
    private Boolean torch;
    
    
    
    
    public static enum WhiteBalanceMode {
        None("none"),
        Manual("manual"),
        SingleShot("single-shot"),
        Continuous("continuous");
        private String string;
        WhiteBalanceMode(String str) {
            string = str;
        }
                
        
    }
    public static enum ExposureMode {
        None("none"),
        Manual("manual"),
        SingleShot("single-shot"),
        Continuous("continuous");
        private String string;
        ExposureMode(String str) {
            string = str;
        }
                
        
    }
    public static enum FocusMode {
        None("none"),
        Manual("manual"),
        SingleShot("single-shot"),
        Continuous("continuous");
        private String string;
        FocusMode(String str) {
            string = str;
        }
                
        
    }
    
    public static class PointOfInterest {
        private double x;
        private double y;

        /**
         * @return the x
         */
        public double getX() {
            return x;
        }

        /**
         * @param x the x to set
         */
        public void setX(double x) {
            this.x = x;
        }

        /**
         * @return the y
         */
        public double getY() {
            return y;
        }

        /**
         * @param y the y to set
         */
        public void setY(double y) {
            this.y = y;
        }
    }
    
    public static class PointsOfInterest extends RTCList<PointOfInterest> {
    
    }
}
