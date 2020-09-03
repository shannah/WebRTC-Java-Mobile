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
 * A constraint used by {@link MediaTrackConstraints}, {@link AudioTrackConstraints}, and {@link VideoTrackConstraints}
 * to constrain the allowed values of certain constraints.
 * @author shannah
 */
public class ConstrainObject<T> implements JSONStruct {

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
    
    public ConstrainObject<T> exact(T exact) {
        setExact(exact);
        return this;
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
    
    public ConstrainObject<T> ideal(T ideal) {
        setIdeal(ideal);
        return this;
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
            
            out.put("exact", toJSONStruct(exact));
            return out;
        } else if (ideal != null) {
            
            out.put("ideal", toJSONStruct(ideal));
            return out;
        } else if (values != null) {
           return toJSONStruct(values);
        } else if (fallback != null) {
            return fallback;
        }
        return out;
        
    }
    
    private static Object toJSONStruct(Object o) {
        if (o instanceof JSONStruct) {
            return ((JSONStruct)o).toJSONStruct();
            
        }
        if (o == null) {
            return null;
        }
        return String.valueOf(o);
    }
    
    /**
     * A list of constraint objects.
     * @param <T> 
     */
    public static class ConstrainValues<T> extends RTCList<T> implements JSONStruct {

        @Override
        public Object toJSONStruct() {
            if (size() == 1) {
                for (T item : this) {
                    if (item instanceof JSONStruct) {
                        return ((JSONStruct)item).toJSONStruct();
                    }
                }
            } else if (size() == 0) {
                return null;
            }
            ArrayList out = new ArrayList();
            for (T item : this) {
                if (item instanceof JSONStruct) {
                    out.add(((JSONStruct)item).toJSONStruct());
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
