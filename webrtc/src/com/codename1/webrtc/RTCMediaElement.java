/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCMediaElement interface adds to HTMLElement the properties and methods needed to support basic media-related capabilities that are common to audio and video. The HTMLVideoElement and HTMLAudioElement elements both inherit this interface.
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement
 */
public interface RTCMediaElement extends RTCElement, EventTarget {
    public double getCurrentTime();
    public double getDuration();
    public boolean isEnded();
    public RTCMediaError getError();
    public boolean isLoop();
    public boolean isMuted();
    public void setMuted(boolean muted);
    public NetworkState getNetworkState();
    public boolean isPaused();
    public double getPlaybackRate();
    public TimeRanges getPlayed();
    public Preload getPreload();
    public ReadyState getReadyState();
    public TimeRanges getSeekable();
    public boolean isSeeking();
    public String getSinkId();
    public String getSrc();
    public TextTracks getTextTracks();
    public VideoTracks getVideoTracks();
    public AudioTracks getAudioTracks();
    public boolean isAutoplay();
    public void setAutoplay(boolean autoplay);
    public double getVolume();
    public MediaStream getSrcObject();
    public void setSrcObject(MediaStream stream);
    
    public void addTextTrack(TextTrack track);
    public MediaStream captureStream();
    public CanPlay canPlayType(String type);
    public void fastSeek(double time);
    public void load();
    public void pause();
    public Promise play();
    public Promise setSinkId(String sinkId);
    
    public static interface RTCMediaElementListener extends EventListener<Event>{}
    /**
     * Fired when the resource was not fully loaded, but not as the result of an error.
     */
    public static final String EVENT_ABORT = "abort";
    

    
    /**
     * Add listener to receive abort event.  Wrapper for calling {@link #addEventListener(java.lang.String, com.codename1.webrtc.EventListener) } with 
     * {@link #EVENT_ABORT} type.
     * @param l Listener to be notified of abort event.
     * @return Self for chaining.
     */
    public RTCMediaElement addAbortListener(RTCMediaElementListener l);
    
    /**
     * Remove listener from receiving abort events.  Wrapper for calling {@link #removeEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with {@link #EVENT_ABORT} type.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeAbortListener(RTCMediaElementListener l);
    
    /**
     * Alias of {@link #addAbortListener(com.codename1.webrtc.RTCMediaElement.AbortListener) }
     * @param l
     * @return Self for chaining
     */
    public RTCMediaElement onabort(RTCMediaElementListener l);
    /**
     * Fired when the user agent can play the media, but estimates that not enough data has been loaded to play the media up to its end without having to stop for further buffering of content
     */
    public static final String EVENT_CANPLAY = "canplay";
    
    /**
     * Adds listener to receive canplay events.  Wrapper for calling {@link #addEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with {@link #EVENT_CANPLAY} type.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addCanPlayListener(RTCMediaElementListener l);
    
    /**
     * Removes "canplay" listener.  Wrapper for {@link #removeEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with type {@link #EVENT_CANPLAY}
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeCanPlayListener(RTCMediaElementListener l);
    
    /**
     * Alias of {@link #addCanPlayListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement oncanplay(RTCMediaElementListener l);
    
    /**
     * Fired when the user agent can play the media, and estimates that enough data has been loaded to play the media up to its end without having to stop for further buffering of content.
     */
    public static final String EVENT_CANPLAYTHROUGH = "canplaythrough";
    
    /**
     * Adds "canplaythrough" listener.  Wrapper for {@link #addEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with type {@link #EVENT_CANPLAYTHROUGH}.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addCanPlayThroughListener(RTCMediaElementListener l);
    
    /**
     * Removes "canplaythrough" listener.  Wrapper for {@link #removeEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with type {@link #EVENT_CANPLAYTHROUGH}.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeCanPlayThroughListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addCanPlayThroughListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement oncanplaythrough(RTCMediaElementListener l);
    
    /**
     * Fired when the duration attribute has been updated.
     */
    public static final String EVENT_DURATIONCHANGE = "durationchange";
    
    /**
     * Adds "durationchange" event listener.  Wrapper for {@link #addEventListener(java.lang.String, com.codename1.webrtc.EventListener) } with
     * {@link #EVENT_DURATIONCHANGE} type.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addDurationChangeListener(RTCMediaElementListener l);
    
    /**
     * Removes "durationchange" event listener.  Wrapper for {@link #removeEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with {@link #EVENT_DURATIONCHANGE} type.
     * @param l 
     * @return Self for chaining.
     */
    public RTCMediaElement removeDurationChangeListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addDurationChangeListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement ondurationchange(RTCMediaElementListener l);
    
    
    /**
     * Fired when the media has become empty; for example, when the media has already been loaded (or partially loaded), and the HTMLMediaElement.load() method is called to reload it.
     */
    public static final String EVENT_EMPTIED = "emptied";
   
    /**
     * Adds listener for "emptied" event.  Wrapper for {@link #addEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with {@link #EVENT_EMPTIED} type.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addEmptiedListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for "emptied" event.  Wrapper for {@link #removeEventListener(java.lang.String, com.codename1.webrtc.EventListener) }
     * with {@link #EVENT_EMPTIED}.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeEmptiedListener(RTCMediaElementListener l);
    
    /**
     * Alias or {@link #addEmptiedListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onemptied(RTCMediaElementListener l);
    
    /**
     * Fired when playback stops when end of the media (<audio> or <video>) is reached or because no further data is available.
     */
    public static final String EVENT_ENDED = "ended";
    
    /**
     * Adds listener for {@link #EVENT_ENDED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addEndedListener(RTCMediaElementListener l);
    
    /**
     * Removes listzener for {@link #EVENT_ENDED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeEndedListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addEndedListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onended(RTCMediaElementListener l);
    
    /**
     * Fired when the resource could not be loaded due to an error.
     */
    public static final String EVENT_ERROR = "error";
    
    /**
     * Adds listener for {@link #EVENT_ERROR} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addErrorListener(ErrorListener l);
    
    /**
     * Removes listener for {@link #EVENT_ERROR} event.
     * @param l 
     * @return Self for chaining.
     */
    public RTCMediaElement removeErrorListener(ErrorListener l);
    
    /**
     * Alias for {@link #addErrorListener(com.codename1.webrtc.ErrorListener) }.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onerror(ErrorListener l);
    
    /**
     * Fired when the first frame of the media has finished loading.
     */
    public static final String EVENT_LOADEDDATA = "loadeddata";
    
    /**
     * Adds listener for {@link #EVENT_LOADEDDATA} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addLoadedDataListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_LOADEDDATA} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeLoadedDataListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addLoadedDataListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onloadeddata(RTCMediaElementListener l);
    
    /**
     * Fired when the metadata has been loaded
     */
    public static final String EVENT_LOADEDMETADATA = "loadedmetadata";
    
    /**
     * Adds listener for {@link #EVENT_LOADEDMETADATA} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addLoadedMetadataListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_LOADEDMETADATA} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeLoadedMetadataListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addLoadedMetadataListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onloadedmetadata(RTCMediaElementListener l);
    
    /**
     * Fired when the browser has started to load a resource.
     */
    public static final String EVENT_LOADSTART = "loadstart";
    
    /**
     * Adds listener for {@link #EVENT_LOADSTART} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addLoadStartListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_LOADSTART} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeLoadStartListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addLoadStartListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onloadstart(RTCMediaElementListener l);
    
    /**
     * Fired when a request to pause play is handled and the activity has entered its paused state, most commonly occurring when the media's pause() method is called.
     */
    public static final String EVENT_PAUSE = "pause";
    
    /**
     * Adds listener for {@link #EVENT_PAUSE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addPauseListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_PAUSE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removePauseListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addPauseListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onpause(RTCMediaElementListener l);
    
    /**
     * Fired when the paused property is changed from true to false, as a result of the play() method, or the autoplay attribute
     */
    public static final String EVENT_PLAY = "play";
    
    /**
     * Adds listener for {@link #EVENT_PLAY} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addPlayListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_PLAY} event.
     * @param l
     * @return Self for chaining. 
     */
    public RTCMediaElement removePlayListener(RTCMediaElementListener l);
    
    /**
     * Alias of {@link #addPlayListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return 
     */
    public RTCMediaElement onplay(RTCMediaElementListener l);
    
    /**
     * Fired when playback is ready to start after having been paused or delayed due to lack of data
     */
    public static final String EVENT_PLAYING = "playing";
    
    /**
     * Adds listener for {@link #EVENT_PLAYING} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addPlayingListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_PLAYING} event.
     * @param l 
     * @return Self for chaining.
     */
    public RTCMediaElement removePlayingListener(RTCMediaElementListener l);
    
    /**
     * Alias of {@link #addPlayingListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onplaying(RTCMediaElementListener l);
    
    /**
     * Fired periodically as the browser loads a resource.
     */
    public static final String EVENT_PROGRESS = "progress";
    
    /**
     * Adds listener for {@link #EVENT_PROGRESS} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addProgressListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_PROGRESS} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeProgressListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addProgressListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return 
     */
    public RTCMediaElement onprogress(RTCMediaElementListener l);
    
    /**
     * Fired when the playback rate has changed.
     */
    public static final String EVENT_RATECHANGE = "ratechange";
    
    /**
     * Adds listener for {@link #EVENT_RATECHANGE} event.
     * @param l 
     * @return Self for chaining.
     */
    public RTCMediaElement addRateChangeListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_RATECHANGE} event.
     * @param l 
     * @return Self for chaining.
     */
    public RTCMediaElement removeRateChangeListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addRateChangeListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onratechange(RTCMediaElementListener l);
    
    /**
     * Fired when a seek operation completes
     */
    public static final String EVENT_SEEKED = "seeked";
    
    /**
     * Adds listener for {@link #EVENT_SEEKED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addSeekedListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_SEEKED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeSeekedListener(RTCMediaElementListener l);
    
    /**
     * Alias of {@link #addSeekedListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onseeked(RTCMediaElementListener l);
    
    /**
     * Fired when a seek operation begins
     */
    public static final String EVENT_SEEKING = "seeking";
    
    /**
     * Adds listener for {@link #EVENT_SEEKING} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addSeekingListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_SEEKING} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeSeekingListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addSeekingListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onseeking(RTCMediaElementListener l);
    
    /**
     * Fired when the user agent is trying to fetch media data, but data is unexpectedly not forthcoming.
     */
    public static final String EVENT_STALLED = "stalled";
    
    /**
     * Adds listener for {@link #EVENT_STALLED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addStalledtListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_STALLED} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeStalledListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addStalledtListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onstalled(RTCMediaElementListener l);
    
    /**
     * Fired when the media data loading has been suspended.
     */
    public static final String EVENT_SUSPEND = "suspend";
    
    /**
     * Adds listener for {@link #EVENT_SUSPEND} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addSuspendListener(RTCMediaElementListener l);
    /**
     * Removes listener for {@link #EVENT_SUSPEND} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeSuspendListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addSuspendListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return 
     */
    public RTCMediaElement onsuspend(RTCMediaElementListener l);
    
    /**
     * Fired when the time indicated by the currentTime attribute has been updated.
     */
    public static final String EVENT_TIMEUPDATE = "timeupdate";
    
    /**
     * Adds listener for {@link #EVENT_TIMEUPDATE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addTimeUpdateListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_TIMEUPDATE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeTimeUpdateListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addTimeUpdateListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return 
     */
    public RTCMediaElement ontimeupdate(RTCMediaElementListener l);
    
    /**
     * Fired when the volume has changed.
     */
    public static final String EVENT_VOLUMECHANGE = "volumechange";
    
    /**
     * Adds listener for {@link #EVENT_VOLUMECHANGE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addVolumeChangeListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_VOLUMECHANGE} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeVolumeChangeListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addVolumeChangeListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onvolumechange(RTCMediaElementListener l);
    
    /**
     * Fired when playback has stopped because of a temporary lack of data.
     */
    public static final String EVENT_WAITING = "waiting";
    
    /**
     * Adds listener for {@link #EVENT_WAITING} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement addWaitingListener(RTCMediaElementListener l);
    
    /**
     * Removes listener for {@link #EVENT_WAITING} event.
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement removeWaitingListener(RTCMediaElementListener l);
    
    /**
     * Alias for {@link #addWaitingListener(com.codename1.webrtc.RTCMediaElement.RTCMediaElementListener) }
     * @param l
     * @return Self for chaining.
     */
    public RTCMediaElement onwaiting(RTCMediaElementListener l);
    
    
    
    public static enum NetworkState {
        Empty(0),
        Idle(1),
        Loading(2),
        NoSource(3);
        
        private int code;
        
        NetworkState(int code) {
            this.code = code;
        }
    }
    
    public static enum Preload {
        None("none"),
        Metadata("metadata"),
        Auto("auto");
        private String string;
        Preload(String str) {
            string = str;
        }
    }
    
    public static enum ReadyState {
        HaveNothing(0),
        HaveMetadata(1),
        HaveCurrentData(2),
        HaveFutureData(3),
        HaveEnoughData(4);
        
        private int code;
        ReadyState(int code) {
            this.code = code;
        }
        
    }
    
    public static enum CanPlay {
        Probably("probably"),
        Maybe("maybe"),
        Unknown("");
        private String string;
        
        CanPlay(String str) {
            this.string = str;
        }
    }
    
}
