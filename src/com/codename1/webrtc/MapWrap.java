/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.Map;

/**
 *
 * @author shannah
 */
class MapWrap {

    private final Map m;

    MapWrap(Map m) {
        this.m = m;
    }

    public Object get(String key, Object defaultVal) {
        if (has(key)) {
            return m.get(key);
        }
        return defaultVal;
    }

    Object get(String key) {
        return m.get(key);
    }

    boolean has(String key) {
        return m.containsKey(key);
    }

}
