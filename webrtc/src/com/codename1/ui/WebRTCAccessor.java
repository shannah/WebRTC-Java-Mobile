/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.ui;

/**
 *
 * @author shannah
 */
public class WebRTCAccessor {
    public static PeerComponent getInternal(BrowserComponent cmp) {
        return cmp.getInternal();
    }
}
