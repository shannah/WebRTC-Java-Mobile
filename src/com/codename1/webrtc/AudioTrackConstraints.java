/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class AudioTrackConstraints extends MediaTrackConstraints {
    
    
    
    private ConstrainBoolean autoGainControl;
    private ConstrainNumber<Integer> channelCount;
    private ConstrainBoolean echoCancellation;
    private ConstrainNumber<Double> latency;
    private ConstrainBoolean noiseSuppression;
    private ConstrainNumber<Integer> sampleRate;
    private ConstrainNumber<Integer> sampleSize;
    private ConstrainNumber<Double> volume;
    
    public AudioTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }

    @Override
    public Object toJSONStruct() {
        Map out = (Map)super.toJSONStruct();
        if (autoGainControl != null) {
            out.put("autoGainControl", toJSONStruct(autoGainControl));
        }
        if (channelCount != null) {
            out.put("channelCount", toJSONStruct(channelCount));
        }
        if (echoCancellation != null) {
            out.put("echoCancellation", toJSONStruct(echoCancellation));
        }
        if (latency != null) {
            out.put("latency", toJSONStruct(latency));
        }
        if (noiseSuppression != null) {
            out.put("noiseSuppression", toJSONStruct(noiseSuppression));
        }
        if (sampleSize != null) {
            out.put("sampleSize", toJSONStruct(sampleSize));
        }
        if (volume != null) {
            out.put("volume", toJSONStruct(volume));
        }
        return out;
    }
    
    private static Object toJSONStruct(Object o) {
        if (o instanceof JSObject) {
            return ((JSObject)o).toJSONStruct();
            
        }
        return null;
    }
    /**
     * @return the autoGainControl
     */
    public ConstrainBoolean getAutoGainControl() {
        return autoGainControl;
    }

    /**
     * @param autoGainControl the autoGainControl to set
     */
    public void setAutoGainControl(ConstrainBoolean autoGainControl) {
        this.autoGainControl = autoGainControl;
    }
    
    public AudioTrackConstraints autoGainControl(ConstrainBoolean autoGainControl) {
        setAutoGainControl(autoGainControl);
        return this;
    }
    
    public AudioTrackConstraints autoGainControl(boolean autoGainControl) {
        setAutoGainControl(new ConstrainBoolean(autoGainControl));
        return this;
    }

    /**
     * @return the channelCount
     */
    public ConstrainNumber<Integer> getChannelCount() {
        return channelCount;
    }

    /**
     * @param channelCount the channelCount to set
     */
    public void setChannelCount(ConstrainNumber<Integer> channelCount) {
        this.channelCount = channelCount;
    }
    
    public AudioTrackConstraints channelCount(ConstrainNumber<Integer> channelCount) {
        setChannelCount(channelCount);
        return this;
    }
    
    public AudioTrackConstraints channelCount(int channelCount) {
        setChannelCount(ConstrainNumber.wrap(channelCount));
        return this;
    }
    
    

    /**
     * @return the echoCancellation
     */
    public ConstrainBoolean getEchoCancellation() {
        return echoCancellation;
    }

    /**
     * @param echoCancellation the echoCancellation to set
     */
    public void setEchoCancellation(ConstrainBoolean echoCancellation) {
        this.echoCancellation = echoCancellation;
    }
    
    public AudioTrackConstraints echoCancellation(ConstrainBoolean echoCancellation) {
        setEchoCancellation(echoCancellation);
        return this;
    }
    
    public AudioTrackConstraints echoCancellation(boolean echoCancellation) {
        return echoCancellation(new ConstrainBoolean(echoCancellation));
    }

    /**
     * @return the latency
     */
    public ConstrainNumber<Double> getLatency() {
        return latency;
    }

    /**
     * @param latency the latency to set
     */
    public void setLatency(ConstrainNumber<Double> latency) {
        this.latency = latency;
    }
    
    public AudioTrackConstraints latency(ConstrainNumber<Double> latency) {
        setLatency(latency);
        return this;
    }
    
    public AudioTrackConstraints latency(double latency) {
        return latency(ConstrainNumber.wrap(latency));
    }

    /**
     * @return the noiseSuppression
     */
    public ConstrainBoolean getNoiseSuppression() {
        return noiseSuppression;
    }

    /**
     * @param noiseSuppression the noiseSuppression to set
     */
    public void setNoiseSuppression(ConstrainBoolean noiseSuppression) {
        this.noiseSuppression = noiseSuppression;
    }
    
    public AudioTrackConstraints noiseSuppression(ConstrainBoolean noiseSuppression) {
        setNoiseSuppression(noiseSuppression);
        return this;
    }
    
    public AudioTrackConstraints noiseSuppression(boolean noiseSuppression) {
        return noiseSuppression(new ConstrainBoolean(noiseSuppression));
    }

    /**
     * @return the sampleRate
     */
    public ConstrainNumber<Integer> getSampleRate() {
        return sampleRate;
    }

    /**
     * @param sampleRate the sampleRate to set
     */
    public void setSampleRate(ConstrainNumber<Integer> sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    public AudioTrackConstraints sampleRate(ConstrainNumber<Integer> sampleRate) {
        setSampleRate(sampleRate);
        return this;
    }
    
    public AudioTrackConstraints sampleRate(int sampleRate) {
        return sampleRate(ConstrainNumber.wrap(sampleRate));
    }

    /**
     * @return the sampleSize
     */
    public ConstrainNumber<Integer> getSampleSize() {
        return sampleSize;
    }

    /**
     * @param sampleSize the sampleSize to set
     */
    public void setSampleSize(ConstrainNumber<Integer> sampleSize) {
        this.sampleSize = sampleSize;
    }
    
    public AudioTrackConstraints sampleSize(ConstrainNumber<Integer> sampleSize) {
        setSampleSize(sampleSize);
        return this;
    }
    
    public AudioTrackConstraints sampleSize(int sampleSize) {
        return sampleSize(ConstrainNumber.wrap(sampleSize));
    }

    /**
     * @return the volume
     */
    public ConstrainNumber<Double> getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(ConstrainNumber<Double> volume) {
        this.volume = volume;
    }
    
    public AudioTrackConstraints volume(ConstrainNumber<Double> volume) {
        setVolume(volume);
        return this;
    }
    
    public AudioTrackConstraints volume(double volume) {
        return volume(ConstrainNumber.wrap(volume));
    }
    
    
    public AudioTrackConstraints video(boolean video) {
        getParent().video(video);
        return this;
    }
    
    
}
