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
