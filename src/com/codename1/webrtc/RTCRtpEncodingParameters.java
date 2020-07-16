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
public class RTCRtpEncodingParameters {

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the codecPayloadType
     */
    public int getCodecPayloadType() {
        return codecPayloadType;
    }

    /**
     * @param codecPayloadType the codecPayloadType to set
     */
    public void setCodecPayloadType(int codecPayloadType) {
        this.codecPayloadType = codecPayloadType;
    }

    /**
     * @return the dtx
     */
    public boolean isDtx() {
        return dtx;
    }

    /**
     * @param dtx the dtx to set
     */
    public void setDtx(boolean dtx) {
        this.dtx = dtx;
    }

    /**
     * @return the maxBitrate
     */
    public long getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * @param maxBitrate the maxBitrate to set
     */
    public void setMaxBitrate(long maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    /**
     * @return the maxFramerate
     */
    public double getMaxFramerate() {
        return maxFramerate;
    }

    /**
     * @param maxFramerate the maxFramerate to set
     */
    public void setMaxFramerate(double maxFramerate) {
        this.maxFramerate = maxFramerate;
    }

    /**
     * @return the ptime
     */
    public long getPtime() {
        return ptime;
    }

    /**
     * @param ptime the ptime to set
     */
    public void setPtime(long ptime) {
        this.ptime = ptime;
    }

    /**
     * @return the rid
     */
    public String getRid() {
        return rid;
    }

    /**
     * @param rid the rid to set
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * @return the scaleResolutionDownBy
     */
    public double getScaleResolutionDownBy() {
        return scaleResolutionDownBy;
    }

    /**
     * @param scaleResolutionDownBy the scaleResolutionDownBy to set
     */
    public void setScaleResolutionDownBy(double scaleResolutionDownBy) {
        this.scaleResolutionDownBy = scaleResolutionDownBy;
    }
    private boolean active;
    private int codecPayloadType;
    private boolean dtx;
    private long maxBitrate;
    private double maxFramerate;
    private long ptime;
    private String rid;
    private double scaleResolutionDownBy;
}
