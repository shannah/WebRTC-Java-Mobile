/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.HashMap;
import java.util.Map;

/**
 * The ConstrainNumber type is used to specify a constraint for a property whose
 * value is a number. It extends the {@link NumberRante} dictionary (which
 * provides the ability to specify a permitted range of property values) to also
 * support an exact value and/or an ideal value the property should take on. In
 * addition, you can specify the value as a simple long integer value, in which
 * case the user agent does its best to match the value once all other more
 * stringent constraints are met.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/ConstrainULong
 * @see https://developer.mozilla.org/en-US/docs/Web/API/ULongRange
 */
public class ConstrainNumber<T extends Number> extends NumberRange<T> implements JSObject {

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

    public ConstrainNumber(T num) {
        setFallback(num);
    }

    public ConstrainNumber(T min, T max) {
        setMin(min);
        setMax(max);
    }

    public T getValue() {
        return getMin();
    }

    public static ConstrainNumber<Integer> wrap(int val) {
        return new ConstrainNumber<Integer>(val);
    }

    public static ConstrainNumber<Double> wrap(double val) {
        return new ConstrainNumber<Double>(val);
    }

    public static ConstrainNumber<Integer> range(int min, int max) {
        return new ConstrainNumber(min, max);
    }

    public static ConstrainNumber<Double> range(double min, double max) {
        return new ConstrainNumber(min, max);
    }

    public static ConstrainNumber<Integer> exact(int val) {
        ConstrainNumber num = new ConstrainNumber(val);
        num.setExact(val);
        return num;
    }

    public static ConstrainNumber<Double> exact(double val) {
        ConstrainNumber num = new ConstrainNumber(val);
        num.setExact(val);
        return num;
    }

    public static ConstrainNumber<Integer> ideal(int val) {
        ConstrainNumber num = new ConstrainNumber(val);
        num.setIdeal(val);
        return num;
    }

    public static ConstrainNumber<Double> ideal(double val) {
        ConstrainNumber num = new ConstrainNumber(val);
        num.setIdeal(val);
        return num;
    }

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
    private T exact, ideal, fallback;

    @Override
    public Object toJSONStruct() {
        Map out = new HashMap();
        if (exact != null) {

            out.put("exact", getExact());
            return out;
        }
        if (ideal != null) {

            out.put("ideal", getIdeal());

        }
        if (getMin() != null && getMax() != null) {
            out.put("min", getMin());
            out.put("max", getMax());

        }
        if (getFallback() != null && out.isEmpty()) {
            getFallback();
        }
        return out;
    }
}
