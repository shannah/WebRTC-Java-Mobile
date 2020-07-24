/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCIceParameters dictionary specifies the username fragment and password assigned to an ICE session. During ICE negotiation, each peer's username fragment and password are recorded in an RTCIceParameters object, which can be obtained from the RTCIceTransport by calling its getLocalParameters() or getRemoteParameters() method, depending on which end interests you.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceParameters
 */
public class RTCIceParameters {

    /**
     * A DOMString specifying the value of the ICE session's username fragment field, ufrag.
     * @return the usernameFragment
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceParameters/usernameFragment
     */
    public String getUsernameFragment() {
        return usernameFragment;
    }

    /**
     * A DOMString specifying the value of the ICE session's username fragment field, ufrag.
     * @param usernameFragment the usernameFragment to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceParameters/usernameFragment
     */
    public void setUsernameFragment(String usernameFragment) {
        this.usernameFragment = usernameFragment;
    }

    /**
     * A DOMString specifying the session's password string.
     * @return the password
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceParameters/password
     */
    public String getPassword() {
        return password;
    }

    /**
     * A DOMString specifying the session's password string.
     * @param password the password to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCIceParameters/password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    private String usernameFragment;
    private String password;
}
