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
public class RTCIceParameters {

    /**
     * @return the usernameFragment
     */
    public String getUsernameFragment() {
        return usernameFragment;
    }

    /**
     * @param usernameFragment the usernameFragment to set
     */
    public void setUsernameFragment(String usernameFragment) {
        this.usernameFragment = usernameFragment;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    private String usernameFragment;
    private String password;
}
