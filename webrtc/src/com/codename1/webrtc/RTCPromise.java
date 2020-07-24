/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.util.SuccessCallback;

/**
 * The Promise object represents the eventual completion (or failure) of an
 * asynchronous operation, and its resulting value.
 *
 * @author shannah
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise
 */
public interface RTCPromise<T> {

    /**
     * Appends fulfillment and rejection handlers to the promise, and returns a
     * new promise resolving to the return value of the called handler, or to
     * its original settled value if the promise was not handled (i.e. if the
     * relevant handler onFulfilled or onRejected is not a function).
     *
     * @param onFulfilled
     * @return
     */
    public RTCPromise<T> then(SuccessCallback<T> onFulfilled);

    /**
     * Appends fulfillment and rejection handlers to the promise, and returns a
     * new promise resolving to the return value of the called handler, or to
     * its original settled value if the promise was not handled (i.e. if the
     * relevant handler onFulfilled or onRejected is not a function).
     *
     * @param onFulfilled
     * @param onRejected
     * @return
     */
    public RTCPromise<T> then(SuccessCallback<T> onFulfilled, SuccessCallback<Throwable> onRejected);

    /**
     * Appends a rejection handler callback to the promise, and returns a new
     * promise resolving to the return value of the callback if it is called, or
     * to its original fulfillment value if the promise is instead fulfilled.
     *
     * @param onRejected
     * @return
     */
    public RTCPromise<T> onCatch(SuccessCallback<Throwable> onRejected);

    /**
     * Appends a handler to the promise, and returns a new promise that is
     * resolved when the original promise is resolved. The handler is called
     * when the promise is settled, whether fulfilled or rejected.
     *
     * @param onFinally
     * @return
     */
    public RTCPromise<T> onFinally(SuccessCallback onFinally);

    /**
     * Waits for the promise to resolve and retrieves the value, or throws an
     * exception if the promise failed to resolve.
     *
     * NOTE: This is uses {@link com.codename1.ui.Display#invokeAndBlock(java.lang.Runnable)
     * } under the hood.
     *
     * @return
     */
    public T get();
}
