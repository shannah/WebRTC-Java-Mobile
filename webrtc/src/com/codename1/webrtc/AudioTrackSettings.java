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
public class AudioTrackSettings extends MediaTrackSettings {
    private boolean autoGainControl;
    private int channelCount;
    private boolean echoCancellation;
    private double latency;
    private boolean noiseSuppression;
    private int sampleRate;
    private int sampleSize;
    private double volume;

    @Override
    public void fromJSONStruct(Object struct) {
        super.fromJSONStruct(struct);
        if (struct instanceof Map) {
            MapWrap m = new MapWrap((Map)struct);
            autoGainControl = (Boolean)m.get("autoGainControl", false);
            channelCount = ((Number)m.get("channelCount", 0)).intValue();
            echoCancellation = (Boolean)m.get("echoCancellation", false);
            latency = ((Number)m.get("latency", 0.0)).doubleValue();
            noiseSuppression = (Boolean)m.get("noiseSuppression", false);
            sampleRate = ((Number)m.get("sampleRate", 0)).intValue();
            sampleSize = ((Number)m.get("sampleSize", 0)).intValue();
            volume = ((Number)m.get("volume", 1.0)).doubleValue();
        }
    }
    
    
    
    /**
     * @return the autoGainControl
     */
    public boolean isAutoGainControl() {
        return autoGainControl;
    }

    /**
     * @param autoGainControl the autoGainControl to set
     */
    public void setAutoGainControl(boolean autoGainControl) {
        this.autoGainControl = autoGainControl;
    }

    /**
     * @return the channelCount
     */
    public int getChannelCount() {
        return channelCount;
    }

    /**
     * @param channelCount the channelCount to set
     */
    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    /**
     * @return the echoCancellation
     */
    public boolean isEchoCancellation() {
        return echoCancellation;
    }

    /**
     * @param echoCancellation the echoCancellation to set
     */
    public void setEchoCancellation(boolean echoCancellation) {
        this.echoCancellation = echoCancellation;
    }

    /**
     * @return the latency
     */
    public double getLatency() {
        return latency;
    }

    /**
     * @param latency the latency to set
     */
    public void setLatency(double latency) {
        this.latency = latency;
    }

    /**
     * @return the noiseSuppression
     */
    public boolean isNoiseSuppression() {
        return noiseSuppression;
    }

    /**
     * @param noiseSuppression the noiseSuppression to set
     */
    public void setNoiseSuppression(boolean noiseSuppression) {
        this.noiseSuppression = noiseSuppression;
    }

    /**
     * @return the sampleRate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * @param sampleRate the sampleRate to set
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * @return the sampleSize
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * @param sampleSize the sampleSize to set
     */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    
    
    
    
           
}
