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

/**
 * Encapsulates a set of CSS style properties that can be applied to {@link RTCElement} objects via its {@link RTCElement#applyStyle(com.codename1.webrtc.RTCStyle) }
 * method.
 * 
 * @author shannah
 */
public class RTCStyle implements Iterable<CSSProperty> {

    /**
     * Encapsulates a single CSS property.
     */
    public static class CSSProperty {
        private String property, value;
        
        /**
         * Creates a new CSS property with name {@literal property} and value {@literal value}.
         * 
         * @param property The CSS property name
         * @param value The CSS property value.
         */
        CSSProperty(String property, String value) {
            this.property = property;
            this.value = value;
        }
        
        /**
         * Gets the CSS property name.
         * @return 
         */
        public String getProperty() {
            return property;
        }
        
        /**
         * Gets the CSS property value.
         * @return 
         */
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
    
    /**
     * Creates a new style given a CSS string.
     * @param css A css string.  May include one or more properties.  E.g. "color:black; font-size:14pt;"
     * @return An RTCStyle encapsulating the provided string.
     */
    public static RTCStyle createStyle(String css) {
        return new RTCStyle().add(css);
    }
}
