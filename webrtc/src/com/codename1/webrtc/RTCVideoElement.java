/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCVideoElement interface provides special properties and methods for
 * manipulating video objects. It also inherits properties and methods of
 * RTCMediaElement and RTCElement.
 *
 * The list of supported media formats varies from one browser to the other. You
 * should either provide your video in a single format that all the relevant
 * browsers supports, or provide multiple video sources in enough different
 * formats that all the browsers you need to support are covered.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/HTMLVideoElement
 */
public interface RTCVideoElement extends RTCMediaElement {

    /**
     * Returns an unsigned integer value indicating the intrinsic width of the
     * resource in CSS pixels, or 0 if no media is available yet.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/HTMLVideoElement/videoHeight
     */
    public int getVideoWidth();

    /**
     * Returns an unsigned integer value indicating the intrinsic height of the
     * resource in CSS pixels, or 0 if no media is available yet.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/HTMLVideoElement/videoHeight
     */
    public int getVideoHeight();

    public boolean playsInline();

    public void setPlaysInline(boolean playsInline);

}
