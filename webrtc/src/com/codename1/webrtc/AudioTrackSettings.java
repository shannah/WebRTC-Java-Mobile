/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 * The MediaTrackSettings dictionary is used to return the current values configured for each of a MediaStreamTrack's settings. These values will adhere as closely as possible to any constraints previously described using a MediaTrackConstraints object and set using applyConstraints(), and will adhere to the default constraints for any properties whose constraints haven't been changed, or whose customized constraints couldn't be matched.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings
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
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/autoGainControl
     */
    public boolean isAutoGainControl() {
        return autoGainControl;
    }

    /**
     * @param autoGainControl the autoGainControl to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/autoGainControl
     */
    public void setAutoGainControl(boolean autoGainControl) {
        this.autoGainControl = autoGainControl;
    }

    /**
     * @return the channelCount
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/channelCount
     */
    public int getChannelCount() {
        return channelCount;
    }

    /**
     * @param channelCount the channelCount to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/channelCount
     */
    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    /**
     * @return the echoCancellation
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/echoCancellation
     */
    public boolean isEchoCancellation() {
        return echoCancellation;
    }

    /**
     * @param echoCancellation the echoCancellation to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/echoCancellation
     */
    public void setEchoCancellation(boolean echoCancellation) {
        this.echoCancellation = echoCancellation;
    }

    /**
     * @return the latency
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/latency
     */
    public double getLatency() {
        return latency;
    }

    /**
     * @param latency the latency to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/latency
     */
    public void setLatency(double latency) {
        this.latency = latency;
    }

    /**
     * @return the noiseSuppression
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/noiseSuppression
     */
    public boolean isNoiseSuppression() {
        return noiseSuppression;
    }

    /**
     * @param noiseSuppression the noiseSuppression to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/noiseSuppression
     * 
     */
    public void setNoiseSuppression(boolean noiseSuppression) {
        this.noiseSuppression = noiseSuppression;
    }

    /**
     * @return the sampleRate
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/sampleRate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * @param sampleRate the sampleRate to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/sampleRate
     */
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * @return the sampleSize
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/sampleSize
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * @param sampleSize the sampleSize to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/sampleSize
     */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     * @return the volume
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/volume
     * 
     */
    public double getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackSettings/volume
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    
    
    
    
           
}
