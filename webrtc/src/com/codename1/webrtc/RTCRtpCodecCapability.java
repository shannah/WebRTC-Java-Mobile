/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 * Used with {@link RTCRtpTransceiver#setCodecPreferences(com.codename1.webrtc.RTCRtpCodecCapability...) }
 * @author shannah
 */
public class RTCRtpCodecCapability {

    RTCRtpCodecCapability(Map map) {
        MapWrap w = new MapWrap(map);
        channels = w.getInt("channels", 0);
        mimeType = w.getString("mimeType", null);
        clockRate = w.getLong("clockRate", 0l);
        sdpFmtpLine = w.getString("sdpFmtpLine", null);
    }

    /**
     * @return the channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * @param channels the channels to set
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    /**
     * @return the clockRate
     */
    public long getClockRate() {
        return clockRate;
    }

    /**
     * @param clockRate the clockRate to set
     */
    public void setClockRate(long clockRate) {
        this.clockRate = clockRate;
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return the sdpFmtpLine
     */
    public String getSdpFmtpLine() {
        return sdpFmtpLine;
    }

    /**
     * @param sdpFmtpLine the sdpFmtpLine to set
     */
    public void setSdpFmtpLine(String sdpFmtpLine) {
        this.sdpFmtpLine = sdpFmtpLine;
    }
    private int channels;
    private long clockRate;
    private String mimeType;
    private String sdpFmtpLine;
}
