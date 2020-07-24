/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class ConstrainBoolean implements JSObject {

   
    
    public ConstrainBoolean(Boolean fallback) {
        this.fallback = fallback;
    }
    
    public static ConstrainBoolean fallback(boolean fallback) {
        return new ConstrainBoolean(fallback);
    }
    
    public static ConstrainBoolean exact(boolean exact) {
        ConstrainBoolean out = new ConstrainBoolean(exact);
        out.setExact(exact);
        return out;
    }
    
    public static ConstrainBoolean ideal(boolean ideal) {
        ConstrainBoolean out = new ConstrainBoolean(ideal);
        out.setIdeal(ideal);
        return out;
    }
    
    
    
    
    /**
     * @return the exact
     */
    public Boolean getExact() {
        return exact;
    }

    /**
     * @param exact the exact to set
     */
    public void setExact(Boolean exact) {
        this.exact = exact;
    }

    

    /**
     * @param ideal the ideal to set
     */
    public void setIdeal(Boolean ideal) {
        this.ideal = ideal;
    }
    
    public Boolean getIdeal() {
        return ideal;
    }

    
    private Boolean exact, ideal, fallback;
    
    @Override
    public Object toJSONStruct() {
        if (exact != null) {
            Map out = new HashMap();
            out.put("exact", exact);
            return out;
        } else if (ideal != null) {
            Map out = new HashMap();
            out.put("ideal", ideal);
            return out;
        } else {
            return fallback;
        }
    }
    
    
}
