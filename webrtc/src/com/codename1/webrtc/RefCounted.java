/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * An interface implemented by certain classes in the
 * {@link com.codename1.webrtc} package which have an associated Javascript peer
 * object, so that they can be reference counted. The rules for reference
 * counting are similar to Objective-C's reference counting rules.
 *
 * 1. **You own any object you create** - If you create an object using
 * {@literal rtc.newXXX()}, then you own it. You don't need to call {@link #retain()
 * }, as the retain count has already been incremented to {@link 1} on object
 * creation. 
 * 2. **You can take ownership of an object using {@link #retain() }**
 * 3. **When you no longer need it , you must relinquish ownership or an object
 * you own using {@link #release() }**. 
 * 4. **You must not relinquish ownership
 * of an object you do not own**
 *
 * NOTE: Memory leaks of RefCounted objects only exist within the confines of an
 * {@link RTC} object. If you call {@link RTC#close() }
 * it will dispose of its underlying WebView, and clean up all resources
 * anyways.
 *
 * IMPORTANT: {@link RTCList} will automatically retain and release RefCounted
 * objects when they are added/removed, so you don't need to claim ownership of
 * objects when you add them to an {@link RTCList}.
 *
 * @author shannah
 */
public interface RefCounted {

    /**
     * Claim ownership of an object.
     * 
     */
    public void retain();

    /**
     * Relinquish ownership of an object.
     */
    public void release();
}
