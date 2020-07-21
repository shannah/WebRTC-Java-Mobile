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
public class RTCRtpEncodingParameters implements JSObject {

    /**
     * @return the active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the codecPayloadType
     */
    public Integer getCodecPayloadType() {
        return codecPayloadType;
    }

    /**
     * @param codecPayloadType the codecPayloadType to set
     */
    public void setCodecPayloadType(Integer codecPayloadType) {
        this.codecPayloadType = codecPayloadType;
    }

    /**
     * @return the dtx
     */
    public Boolean isDtx() {
        return dtx;
    }

    /**
     * @param dtx the dtx to set
     */
    public void setDtx(Boolean dtx) {
        this.dtx = dtx;
    }

    /**
     * @return the maxBitrate
     */
    public Integer getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * @param maxBitrate the maxBitrate to set
     */
    public void setMaxBitrate(Integer maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    /**
     * @return the maxFramerate
     */
    public Double getMaxFramerate() {
        return maxFramerate;
    }

    /**
     * @param maxFramerate the maxFramerate to set
     */
    public void setMaxFramerate(Double maxFramerate) {
        this.maxFramerate = maxFramerate;
    }

    /**
     * @return the ptime
     */
    public Integer getPtime() {
        return ptime;
    }

    /**
     * @param ptime the ptime to set
     */
    public void setPtime(Integer ptime) {
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
    public Double getScaleResolutionDownBy() {
        return scaleResolutionDownBy;
    }

    /**
     * @param scaleResolutionDownBy the scaleResolutionDownBy to set
     */
    public void setScaleResolutionDownBy(Double scaleResolutionDownBy) {
        this.scaleResolutionDownBy = scaleResolutionDownBy;
    }
    private Boolean active;
    private Integer codecPayloadType;
    private Boolean dtx;
    private Integer maxBitrate;
    private Double maxFramerate;
    private Integer ptime;
    private String rid;
    private Double scaleResolutionDownBy;

    @Override
    public Object toJSONStruct() {
        Map out = new LinkedHashMap();
        if (active != null) out.put("active", active);
        if (codecPayloadType != null) out.put("codecPayloadType", codecPayloadType);
        if (dtx != null) out.put("dtx", dtx);
        if (maxBitrate != null) out.put("maxBitrate", maxBitrate);
        if (maxFramerate != null) out.put("maxFramerate", maxFramerate);
        if (ptime != null) out.put("ptime", ptime);
        if (rid != null) out.put("rid", rid);
        if (scaleResolutionDownBy != null) out.put("scaleResolutionDownBy", scaleResolutionDownBy);
        return out;
    }
}
