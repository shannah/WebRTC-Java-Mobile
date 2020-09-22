/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.Log;
import java.util.Map;

/**
 * Utility class to wrap a Map and make it easier to get values from it.
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
    
    public String getString(String key, String defaultVal) {
        Object o = get(key, defaultVal);
        if (o == defaultVal) {
            return (String)o;
        }
        if (o instanceof String) {
            return (String)o;
        }
        return String.valueOf(o);
        
    }
    
    public Integer getInt(String key, Integer defaultVal) {
        if (has(key)) {
            Object val = m.get(key);
            if (val == null) {
                return defaultVal;
            }
            if (val instanceof String) {
                try {
                    val = Integer.parseInt((String)val);
                } catch (Throwable t) {
                    throw new IllegalArgumentException("Expected integer but found string "+val+", which could not be parsed as an int.");
                }
            }
            if (!(val instanceof Number)) {
                throw new IllegalArgumentException("Expected integer but found "+val);
            }
            return ((Number)val).intValue();
        }
        return defaultVal;
    }
    
    public Boolean getBoolean(String key, Boolean defaultVal) {
        if (has(key)) {
            Object o = m.get(key);
            if (o instanceof Boolean) {
                return (Boolean)o;
            } else if (o instanceof String) {
                return "true".equals(o);
            }
            return defaultVal;
        }
        return defaultVal;
    }
    
    public Long getLong(String key, Long defaultVal) {
        if (has(key)) {
            return ((Number)m.get(key)).longValue();
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
