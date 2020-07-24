/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Defines audio constraints for a call to {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
 * 
 * See https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints for information about WebRTC media track constraints.
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
    
    /**
     * Creates a new AudioTrackConstraints objec twith the given {@link MediaStreamConstraints} as its parent.
     * @param parent 
     */
    public AudioTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }

    /**
     * Converts the object to a JSON struct; in this case {@link Map}.
     * @return 
     */
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
     * A ConstrainBoolean object which specifies whether automatic gain control is preferred and/or required.
     * @return the autoGainControl
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/autoGainControl
     */
    public ConstrainBoolean getAutoGainControl() {
        return autoGainControl;
    }

    /**
     * A ConstrainLong specifying the channel count or range of channel counts which are acceptable and/or required.
     * @param autoGainControl the autoGainControl to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/autoGainControl
     */
    public void setAutoGainControl(ConstrainBoolean autoGainControl) {
        this.autoGainControl = autoGainControl;
    }
    
    /**
     * Sets autoGainControl property and returns self for chaining.
     * @param autoGainControl
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/autoGainControl
     */
    public AudioTrackConstraints autoGainControl(ConstrainBoolean autoGainControl) {
        setAutoGainControl(autoGainControl);
        return this;
    }
    
    /**
     * Sets autoGainControl and returns self for chaining.
     * @param autoGainControl
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/autoGainControl
     */
    public AudioTrackConstraints autoGainControl(boolean autoGainControl) {
        setAutoGainControl(new ConstrainBoolean(autoGainControl));
        return this;
    }

    /**
     * A ConstrainLong specifying the channel count or range of channel counts which are acceptable and/or required.
     * 
     * @return the channelCount
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/channelCount
     */
    public ConstrainNumber<Integer> getChannelCount() {
        return channelCount;
    }

    /**
     * A ConstrainLong specifying the channel count or range of channel counts which are acceptable and/or required.
     * @param channelCount the channelCount to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/channelCount
     */
    public void setChannelCount(ConstrainNumber<Integer> channelCount) {
        this.channelCount = channelCount;
    }
    
    /**
     * 
     * @param channelCount
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/channelCount
     */
    public AudioTrackConstraints channelCount(ConstrainNumber<Integer> channelCount) {
        setChannelCount(channelCount);
        return this;
    }
    
    /**
     * 
     * @param channelCount
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/channelCount
     */
    public AudioTrackConstraints channelCount(int channelCount) {
        setChannelCount(ConstrainNumber.wrap(channelCount));
        return this;
    }
    
    

    /**
     * @return the echoCancellation
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/echoCancellation
     */
    public ConstrainBoolean getEchoCancellation() {
        return echoCancellation;
    }

    /**
     * @param echoCancellation the echoCancellation to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/echoCancellation
     */
    public void setEchoCancellation(ConstrainBoolean echoCancellation) {
        this.echoCancellation = echoCancellation;
    }
    
    /**
     * 
     * @param echoCancellation
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/echoCancellation
     */
    public AudioTrackConstraints echoCancellation(ConstrainBoolean echoCancellation) {
        setEchoCancellation(echoCancellation);
        return this;
    }
    
    /**
     * 
     * @param echoCancellation
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/echoCancellation
     */
    public AudioTrackConstraints echoCancellation(boolean echoCancellation) {
        return echoCancellation(new ConstrainBoolean(echoCancellation));
    }

    /**
     * @return the latency
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/latency
     */
    public ConstrainNumber<Double> getLatency() {
        return latency;
    }

    /**
     * @param latency the latency to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/latency
     */
    public void setLatency(ConstrainNumber<Double> latency) {
        this.latency = latency;
    }
    
    /**
     * 
     * @param latency
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/latency
     */
    public AudioTrackConstraints latency(ConstrainNumber<Double> latency) {
        setLatency(latency);
        return this;
    }
    
    /**
     * 
     * @param latency
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/latency
     */
    public AudioTrackConstraints latency(double latency) {
        return latency(ConstrainNumber.wrap(latency));
    }

    /**
     * @return the noiseSuppression
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/noiseSuppression
     */
    public ConstrainBoolean getNoiseSuppression() {
        return noiseSuppression;
    }

    /**
     * @param noiseSuppression the noiseSuppression to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/noiseSuppression
     */
    public void setNoiseSuppression(ConstrainBoolean noiseSuppression) {
        this.noiseSuppression = noiseSuppression;
    }
    
    /**
     * 
     * @param noiseSuppression
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/noiseSuppression
     */
    public AudioTrackConstraints noiseSuppression(ConstrainBoolean noiseSuppression) {
        setNoiseSuppression(noiseSuppression);
        return this;
    }
    
    /**
     * 
     * @param noiseSuppression
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/noiseSuppression
     */
    public AudioTrackConstraints noiseSuppression(boolean noiseSuppression) {
        return noiseSuppression(new ConstrainBoolean(noiseSuppression));
    }

    /**
     * @return the sampleRate
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleRate
     */
    public ConstrainNumber<Integer> getSampleRate() {
        return sampleRate;
    }

    /**
     * @param sampleRate the sampleRate to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleRate
     */
    public void setSampleRate(ConstrainNumber<Integer> sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    /**
     * 
     * @param sampleRate
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleRate
     */
    public AudioTrackConstraints sampleRate(ConstrainNumber<Integer> sampleRate) {
        setSampleRate(sampleRate);
        return this;
    }
    
    /**
     * 
     * @param sampleRate
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleRate
     */
    public AudioTrackConstraints sampleRate(int sampleRate) {
        return sampleRate(ConstrainNumber.wrap(sampleRate));
    }

    /**
     * @return the sampleSize
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleSize
     */
    public ConstrainNumber<Integer> getSampleSize() {
        return sampleSize;
    }

    /**
     * @param sampleSize the sampleSize to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleSize
     */
    public void setSampleSize(ConstrainNumber<Integer> sampleSize) {
        this.sampleSize = sampleSize;
    }
    
    /**
     * 
     * @param sampleSize
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleSize
     */
    public AudioTrackConstraints sampleSize(ConstrainNumber<Integer> sampleSize) {
        setSampleSize(sampleSize);
        return this;
    }
    
    /**
     * 
     * @param sampleSize
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/sampleSize
     */
    public AudioTrackConstraints sampleSize(int sampleSize) {
        return sampleSize(ConstrainNumber.wrap(sampleSize));
    }

    /**
     * @return the volume
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/volume
     */
    public ConstrainNumber<Double> getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/volume
     */
    public void setVolume(ConstrainNumber<Double> volume) {
        this.volume = volume;
    }
    
    /**
     * 
     * @param volume
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/volume
     */
    public AudioTrackConstraints volume(ConstrainNumber<Double> volume) {
        setVolume(volume);
        return this;
    }
    
    /**
     * 
     * @param volume
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints/volume
     */
    public AudioTrackConstraints volume(double volume) {
        return volume(ConstrainNumber.wrap(volume));
    }
    
    /**
     * Sets the parent {@link MediaStreamConstraints} object video property to `true`.
     * @param video Whether to include video in the constraints.
     * @return Self for chaining.
     */
    public AudioTrackConstraints video(boolean video) {
        getParent().video(video);
        return this;
    }
    
    
}
