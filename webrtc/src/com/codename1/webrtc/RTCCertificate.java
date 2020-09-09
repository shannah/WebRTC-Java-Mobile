/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Date;
import java.util.Map;

/**
 * The interface of the the WebRTC API provides an object represents a certificate that an RTCPeerConnection uses to authenticate.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCCertificate
 */
public interface RTCCertificate extends JSONStruct {
    
   
    
    /**
     * Returns the expiration date of the certificate.
     * @return 
     */
    public Date getExpires();
}
