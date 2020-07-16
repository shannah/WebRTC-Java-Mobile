/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author shannah
 */
public class RTCList<T> implements Iterable<T> {
    private ArrayList<T> list = new ArrayList<T>();

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
    
    public int size() {
        return list.size();
    }
    
    public T get(int index) {
        return list.get(index);
    }
    
    public void add(T item) {
        list.add(item);
    }
    
    public void clear() {
        list.clear();
    }
    
    public boolean remove(T item) {
        return list.remove(item);
    }
    
}
