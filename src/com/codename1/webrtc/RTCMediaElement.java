/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
 */
public interface RTCMediaElement extends RTCElement {
    public double getCurrentTime();
    public double getDuration();
    public boolean isEnded();
    public RTCMediaError getError();
    public boolean isLoop();
    public boolean isMuted();
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
    public RTCPromise play();
    public void setSinkId(String sinkId);
    
    
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
