/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCSessionDescription interface describes one end of a connection - or
 * potential connection - and how it's configured. Each RTCSessionDescription
 * consists of a description type indicating which part of the offer/answer
 * negotiation process it describes and of the SDP descriptor of the session.
 *
 * The process of negotiating a connection between two peers involves exchanging
 * RTCSessionDescription objects back and forth, with each description
 * suggesting one combination of connection configuration options that the
 * sender of the description supports. Once the two peers agree upon a
 * configuration for the connection, negotiation is complete.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription
 */
public interface RTCSessionDescription extends JSONStruct {

    /**
     * An enum of type RTCSdpType describing the session description's type.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription/type
     */
    public RTCSdpType getType();

    /**
     * A DOMString containing the SDP describing the session.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription/sdp
     */
    public String getSdp();

    /**
     * This enum defines strings that describe the current state of the session
     * description, as used in the {@link #getType() } property. The session
     * description's type will be specified using one of these values.
     */
    public static enum RTCSdpType {
        /**
         * The SDP contained in the sdp property is the definitive choice in the
         * exchange. In other words, this session description describes the
         * agreed-upon configuration, and is being sent to finalize negotiation.
         */
        Answer("answer"),
        /**
         * The session description object describes the initial proposal in an
         * offer/answer exchange. The session negotiation process begins with an
         * offer being sent from the caller to the callee.
         */
        Offer("offer"),
        /**
         * The session description object describes a provisional answer; that
         * is, a response to a previous offer that is not the final answer. It
         * is usually employed by legacy hardware.
         */
        Pranswer("pranswer"),
        /**
         * This special type with an empty session description is used to roll
         * back to the previous stable state.
         */
        Rollback("rollback");
        private String string;

        RTCSdpType(String str) {
            string = str;
        }

        public boolean matches(String str) {
            return string.equals(str);
        }

        public String stringValue() {
            return string;
        }
    }

    /**
     * Returns a JSON description of the object. The values of both properties,
     * type and sdp, are contained in the generated JSON.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCSessionDescription/toJSON
     */
    public String toJSON();
}
