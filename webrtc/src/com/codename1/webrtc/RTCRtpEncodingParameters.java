/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An instance of the WebRTC API's RTCRtpEncodingParameters dictionary describes
 * a single configuration of a codec for an {@link RTCRtpSender}. It's used in
 * the {@link RTCRtpSendParameters} describing the configuration of an RTP
 * sender's encodings; {@link RTCRtpDecodingParameters} is used to describe the
 * configuration of an RTP receiver's encodings.
 *
 * @author shannah
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpEncodingParameters
 */
public class RTCRtpEncodingParameters implements JSONStruct {

    /**
     * If true, the described encoding is currently actively being used. That
     * is, for RTP senders, the encoding is currently being used to send data,
     * while for receivers, the encoding is being used to decode received data.
     * The default value is true.
     *
     * @return the active
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * If true, the described encoding is currently actively being used. That
     * is, for RTP senders, the encoding is currently being used to send data,
     * while for receivers, the encoding is being used to decode received data.
     * The default value is true.
     *
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * When describing a codec for an RTCRtpSender, codecPayloadType is a single
     * 8-bit byte (or octet) specifying the codec to use for sending the stream;
     * the value matches one from the owning RTCRtpParameters object's codecs
     * parameter. This value can only be set when creating the transceiver;
     * after that, this value is read only.
     *
     * @return the codecPayloadType
     */
    public Integer getCodecPayloadType() {
        return codecPayloadType;
    }

    /**
     * When describing a codec for an RTCRtpSender, codecPayloadType is a single
     * 8-bit byte (or octet) specifying the codec to use for sending the stream;
     * the value matches one from the owning RTCRtpParameters object's codecs
     * parameter. This value can only be set when creating the transceiver;
     * after that, this value is read only.
     *
     * @param codecPayloadType the codecPayloadType to set
     */
    public void setCodecPayloadType(Integer codecPayloadType) {
        this.codecPayloadType = codecPayloadType;
    }

    /**
     * Only used for an RTCRtpSender whose kind is audio, this property
     * indicates whether or not to use discontinuous transmission (a feature by
     * which a phone is turned off or the microphone muted automatically in the
     * absence of voice activity). The value is taken from the enumerated string
     * type RTCDtxStatus.
     *
     * @return the dtx
     */
    public Boolean isDtx() {
        return dtx;
    }

    /**
     * Only used for an RTCRtpSender whose kind is audio, this property
     * indicates whether or not to use discontinuous transmission (a feature by
     * which a phone is turned off or the microphone muted automatically in the
     * absence of voice activity). The value is taken from the enumerated string
     * type RTCDtxStatus.
     *
     * @param dtx the dtx to set
     */
    public void setDtx(Boolean dtx) {
        this.dtx = dtx;
    }

    /**
     * An unsigned long integer indicating the maximum number of bits per second
     * to allow for this encoding. Other parameters may further constrain the
     * bit rate, such as the value of maxFramerate or transport or physical
     * network limitations.
     *
     * @return the maxBitrate
     */
    public Integer getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * An unsigned long integer indicating the maximum number of bits per second
     * to allow for this encoding. Other parameters may further constrain the
     * bit rate, such as the value of maxFramerate or transport or physical
     * network limitations.
     *
     * @param maxBitrate the maxBitrate to set
     */
    public void setMaxBitrate(Integer maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    /**
     * A double-precision floating-point value specifying the maximum number of
     * frames per second to allow for this encoding.
     *
     * @return the maxFramerate
     */
    public Double getMaxFramerate() {
        return maxFramerate;
    }

    /**
     * A double-precision floating-point value specifying the maximum number of
     * frames per second to allow for this encoding.
     *
     * @param maxFramerate the maxFramerate to set
     */
    public void setMaxFramerate(Double maxFramerate) {
        this.maxFramerate = maxFramerate;
    }

    /**
     * An unsigned long integer value indicating the preferred duration of a
     * media packet in milliseconds. This is typically only relevant for audio
     * encodings. The user agent will try to match this as well as it can, but
     * there is no guarantee.
     *
     * @return the ptime
     */
    public Integer getPtime() {
        return ptime;
    }

    /**
     * An unsigned long integer value indicating the preferred duration of a
     * media packet in milliseconds. This is typically only relevant for audio
     * encodings. The user agent will try to match this as well as it can, but
     * there is no guarantee.
     *
     * @param ptime the ptime to set
     */
    public void setPtime(Integer ptime) {
        this.ptime = ptime;
    }

    /**
     * A DOMString which, if set, specifies an RTP stream ID (RID) to be sent
     * using the RID header extension. This parameter cannot be modified using
     * setParameters(). Its value can only be set when the transceiver is first
     * created.
     *
     * @return the rid
     */
    public String getRid() {
        return rid;
    }

    /**
     * A DOMString which, if set, specifies an RTP stream ID (RID) to be sent
     * using the RID header extension. This parameter cannot be modified using
     * setParameters(). Its value can only be set when the transceiver is first
     * created.
     *
     * @param rid the rid to set
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * Only used for senders whose track's kind is video, this is a
     * double-precision floating-point value specifying a factor by which to
     * scale down the video during encoding. The default value, 1.0, means that
     * the sent video's size will be the same as the original. A value of 2.0
     * scales the video frames down by a factor of 2 in each dimension,
     * resulting in a video 1/4 the size of the original. The value must not be
     * less than 1.0 (you can't use this to scale the video up).
     *
     * @return the scaleResolutionDownBy
     */
    public Double getScaleResolutionDownBy() {
        return scaleResolutionDownBy;
    }

    /**
     * Only used for senders whose track's kind is video, this is a
     * double-precision floating-point value specifying a factor by which to
     * scale down the video during encoding. The default value, 1.0, means that
     * the sent video's size will be the same as the original. A value of 2.0
     * scales the video frames down by a factor of 2 in each dimension,
     * resulting in a video 1/4 the size of the original. The value must not be
     * less than 1.0 (you can't use this to scale the video up).
     *
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
        if (active != null) {
            out.put("active", active);
        }
        if (codecPayloadType != null) {
            out.put("codecPayloadType", codecPayloadType);
        }
        if (dtx != null) {
            out.put("dtx", dtx);
        }
        if (maxBitrate != null) {
            out.put("maxBitrate", maxBitrate);
        }
        if (maxFramerate != null) {
            out.put("maxFramerate", maxFramerate);
        }
        if (ptime != null) {
            out.put("ptime", ptime);
        }
        if (rid != null) {
            out.put("rid", rid);
        }
        if (scaleResolutionDownBy != null) {
            out.put("scaleResolutionDownBy", scaleResolutionDownBy);
        }
        return out;
    }
}
