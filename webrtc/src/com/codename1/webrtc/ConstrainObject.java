/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class ConstrainObject<T> implements JSObject {

    /**
     * @return the exact
     */
    public T getExact() {
        return exact;
    }

    /**
     * @param exact the exact to set
     */
    public void setExact(T exact) {
        this.exact = exact;
    }

    /**
     * @return the ideal
     */
    public T getIdeal() {
        return ideal;
    }

    /**
     * @param ideal the ideal to set
     */
    public void setIdeal(T ideal) {
        this.ideal = ideal;
    }

    /**
     * @return the values
     */
    public ConstrainValues<T> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(ConstrainValues<T> values) {
        this.values = values;
    }
    private T exact, ideal, fallback;
    private ConstrainValues<T> values;

    @Override
    public Object toJSONStruct() {
        Map out = new HashMap();
        if (exact != null) {
            
            out.put("exact", toJSONStruct(values));
            return out;
        } else if (ideal != null) {
            
            out.put("ideal", toJSONStruct(values));
            return out;
        } else if (values != null) {
           return toJSONStruct(values);
        } else if (fallback != null) {
            return fallback;
        }
        return out;
        
    }
    
    private static Object toJSONStruct(Object o) {
        if (o instanceof JSObject) {
            return ((JSObject)o).toJSONStruct();
            
        }
        return null;
    }
    
    public static class ConstrainValues<T> extends RTCList<T> implements JSObject {

        @Override
        public Object toJSONStruct() {
            if (size() == 1) {
                for (T item : this) {
                    if (item instanceof JSObject) {
                        return ((JSObject)item).toJSONStruct();
                    }
                }
            } else if (size() == 0) {
                return null;
            }
            ArrayList out = new ArrayList();
            for (T item : this) {
                if (item instanceof JSObject) {
                    out.add(((JSObject)item).toJSONStruct());
                }
            }
            return out;
        }
        
    }

    /**
     * @return the fallback
     */
    public T getFallback() {
        return fallback;
    }

    /**
     * @param fallback the fallback to set
     */
    public void setFallback(T fallback) {
        this.fallback = fallback;
    }
}
