/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.Util;
import com.codename1.webrtc.RTCStyle.CSSProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTCStyle implements Iterable<CSSProperty> {

    
    public static class CSSProperty {
        private String property, value;
        
        CSSProperty(String property, String value) {
            this.property = property;
            this.value = value;
        }
        
        public String getProperty() {
            return property;
        }
        
        public String getValue() {
            return value;
        }
    }
    private ArrayList<CSSProperty> properties = new ArrayList<CSSProperty>();
    
    /**
     * Add a CSS property to the style.
     * @param cssKey The property name.
     * @param cssValue The property value.
     * @return Self for chaining.
     */
    public RTCStyle add(String cssKey, String cssValue) {
        properties.add(new CSSProperty(cssKey, cssValue));
        return this;
    }
    
    /**
     * Add a one or more CSS key/value pairs to the style.  
     * @param cssKeyValue The keyvalue pairs to add.  E.g. "color:blue"  or "color:blue;font-size:12px"
     * @return Self for chaining.
     */
    public RTCStyle add(String cssKeyValue) {
        
        int pos = cssKeyValue.indexOf(";");
        if (pos > 0) {
            String[] parts = Util.split(cssKeyValue, ";");
            for (String part : parts) {
                part = part.trim();
                if (part.isEmpty()) {
                    continue;
                }
                add(part);
            }
            return this;
        }
                
        pos = cssKeyValue.indexOf(":");
        if (pos > 0) {
            return add(cssKeyValue.substring(0, pos).trim(), cssKeyValue.substring(pos+1).trim());
        }
        throw new IllegalArgumentException("RTCStyle.add() expects format key:value.  Didn't find colon delimiter");
    }
    
    
    @Override
    public Iterator<CSSProperty> iterator() {
        return properties.iterator();
    }
    
    
    public static RTCStyle createStyle(String css) {
        return new RTCStyle().add(css);
    }
}
