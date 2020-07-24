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
public class RTCRtpContributingSource {

    /**
     * @return the audioLevel
     */
    public double getAudioLevel() {
        return audioLevel;
    }

    /**
     * @param audioLevel the audioLevel to set
     */
    public void setAudioLevel(double audioLevel) {
        this.audioLevel = audioLevel;
    }

    /**
     * @return the rtmpTimestamp
     */
    public double getRtmpTimestamp() {
        return rtmpTimestamp;
    }

    /**
     * @param rtmpTimestamp the rtmpTimestamp to set
     */
    public void setRtmpTimestamp(double rtmpTimestamp) {
        this.rtmpTimestamp = rtmpTimestamp;
    }

    /**
     * @return the source
     */
    public int getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     * @return the timestamp
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
    private double audioLevel;
    private double rtmpTimestamp; 
    private int source;
    private double timestamp;
}
