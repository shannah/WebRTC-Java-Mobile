/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.util.SuccessCallback;

/**
 *
 * @author shannah
 */
public interface RTCPromise<T> {
    public RTCPromise<T> then(SuccessCallback<T> onFulfilled);
    public RTCPromise<T> then(SuccessCallback<T> onFulfilled, SuccessCallback<Throwable> onRejected);
    public RTCPromise<T> onCatch(SuccessCallback<Throwable> onRejected);
    public RTCPromise<T> onFinally(SuccessCallback onFinally);
}
