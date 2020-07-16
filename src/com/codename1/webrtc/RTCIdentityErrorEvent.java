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
public interface RTCIdentityErrorEvent extends Event  {
    public String getIdp();
    public String getLoginUrl();
    public String getProtocol();
}
