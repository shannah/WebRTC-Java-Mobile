/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The MediaStream interface represents a stream of media content. A stream
 * consists of several tracks such as video or audio tracks. Each track is
 * specified as an instance of {@link MediaStreamTrack} .You can obtain a MediaStream
 * object either by using the constructor or by calling
 * {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.
 * 
 * == Events
 * 
 * . {@link #EVENT_ADD_TRACK}
 * . {@link #EVENT_REMOVE_TRACK}
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream
 */
public interface MediaStream extends RefCounted, EventTarget {
    
    /**
     * Fired when a new MediaStreamTrack object is added.
     */
    public static final String EVENT_ADD_TRACK = "addtrack";
    
    /**
     * Fired when a MediaStreamTrack object has been removed.
     */
    public static final String EVENT_REMOVE_TRACK = "removetrack";

    /**
     * A DOMString containing 36 characters denoting a universally unique identifier (UUID) for the object.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/id
     */
    public String getId();

    /**
     * A Boolean value that returns true if the MediaStream is active, or false otherwise.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/active
     */
    public boolean isActive();

    /**
     * A Boolean value set to true if the end of the stream has been reached. This has been removed from the specification; you should instead check the value of MediaStreamTrack.readyState to see if its value is ended for the track or tracks you want to ensure have finished playing.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/ended
     */
    public boolean isEnded();

    public ReadyState getReadyState();

    /**
     * Returns a list of the MediaStreamTrack objects stored in the MediaStream object that have their kind attribute set to audio. The order is not defined, and may not only vary from one browser to another, but also from one call to another.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/getAudioTracks
     */
    public MediaStreamTracks getAudioTracks();

    /**
     * Returns a list of the MediaStreamTrack objects stored in the MediaStream object that have their kind attribute set to "video". The order is not defined, and may not only vary from one browser to another, but also from one call to another.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/getVideoTracks
     */
    public MediaStreamTracks getVideoTracks();

    /**
     * Returns a list of all MediaStreamTrack objects stored in the MediaStream object, regardless of the value of the kind attribute. The order is not defined, and may not only vary from one browser to another, but also from one call to another.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/getTracks
     */
    public MediaStreamTracks getTracks();

    /**
     * Stores a copy of the MediaStreamTrack given as argument. If the track has already been added to the MediaStream object, nothing happens.
     * @param track 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/addTrack
     */
    public void addTrack(MediaStreamTrack track);

    /**
     * Removes the MediaStreamTrack given as argument. If the track is not part of the MediaStream object, nothing happens.
     * @param track 
     */
    public void removeTrack(MediaStreamTrack track);

    /**
     * Returns a clone of the MediaStream object. The clone will, however, have a unique value for id.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/clone
     */
    public MediaStream clone();

    /**
     * Returns the track whose ID corresponds to the one given in parameters, trackid. If no parameter is given, or if no track with that ID does exist, it returns null. If several tracks have the same ID, it returns the first one.
     * @param id
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream/getTrackById
     */
    public MediaStreamTrack getTrackById(String id);

    public static enum ReadyState {
        Live("live"),
        Ended("ended");

        private String string;

        ReadyState(String str) {
            string = str;
        }
    }

}
