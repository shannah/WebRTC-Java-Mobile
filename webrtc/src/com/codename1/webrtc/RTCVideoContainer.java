/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.Log;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.util.AsyncResource;

/**
 * A Codename One UI component capable of displaying a WebRTC video.
 * @author shannah
 */
public class RTCVideoContainer extends Container implements RTCVideoElement, AutoCloseable  {
    private RTCVideoElement internal;
    private RTC rtc;
    
    private void debug(String message) {
        Log.p("[RTCVideoContainer] "+message, Log.DEBUG);
    }
   
    public RTCVideoContainer() {
        this(null);
        debug("constructor()");
    }
    
    public RTCVideoContainer(MediaStream stream) {
        super(new BorderLayout());
        debug("constructor("+stream+")");
        _setStream(stream);
        
    }
    
    private void _setStream(MediaStream stream) {
        debug("_setStream("+stream+")");
        if (this.rtc != null) {
            Component videoComp = this.rtc.getVideoComponent();
            if (contains(videoComp)) {
                videoComp.remove();
            }
            
        }
        if (stream != null) {
            this.rtc = RTC.getRTC(stream);
            if (this.rtc == null) {
                throw new IllegalStateException("Cannot find RTC for stream "+stream);
            }
            RTCMediaElement el = rtc.findElementForStream(stream);
            
            if (el == null) {
                el = rtc.createVideo();
                el.setAutoplay(true);
                rtc.append(el);
                el.applyStyle("width:100%;height:100%;");
            } 

            this.internal = (RTCVideoElement)el;
            add(BorderLayout.CENTER, rtc.getVideoComponent());
            debug("_setStream(): setting srcObject in video element: "+stream);
            this.internal.setSrcObject(stream);
            revalidateLater();
        }
        
    }
    
    
    /**
     * Creates a new video container for displaying video from the device camera.
     * @param constraints
     * @return 
     *
    public static Promise<RTCVideoContainer> createFromCamera(MediaStreamConstraints constraints) {
        RTCVideoElement video[] = new RTCVideoElement[1];
        RTC rtc[] = new RTC[1];
        return RTC.createRTC().then(_rtc->{
            rtc[0] = _rtc;
            video[0] = _rtc.createVideo();
            video[0].setAutoplay(true);
            _rtc.append(video[0]);
            video[0].applyStyle("width:100%;height:100%;");
            
            return _rtc.getUserMedia(constraints);
        }).then(stream->{
            video[0].setSrcObject((MediaStream)stream);
            return new RTCVideoContainer(rtc[0], video[0]);
            
        });
    }
    */
    /**
     * Creates a new video container for displaying an existing MediaStream.
     * @param stream The stream to display.
     * @return 
     *
    public static Promise<RTCVideoContainer> createFromStream(MediaStream stream) {
        RTCVideoElement video[] = new RTCVideoElement[1];
        RTC rtc[] = new RTC[1];
        RTC streamRTC = RTC.getRTC(stream);
        Component cmp = streamRTC.getVideoComponent();
        RTCMediaElement videoEl = streamRTC.findElementForStream(stream);
        if (videoEl == null) {
            
        }
        return (Promise<RTCVideoContainer>)RTC.createRTC().then( _rtc -> {
            rtc[0] = _rtc;
            video[0] = _rtc.createVideo();
            video[0].setAutoplay(true);
            _rtc.append(video[0]);
            video[0].applyStyle("width:100%;height:100%;");
            return _rtc.importStream(stream);
        }).then(importedStream -> {
            video[0].setSrcObject((MediaStream)importedStream);
            return new RTCVideoContainer(rtc[0], video[0]);

        });

    }
    */
    /**
     * Creates an empty container which isn't displaying any video yet.  Video can be set using 
     * {@link #setSrcObject(com.codename1.webrtc.MediaStream) }.
     * @return 
     *
    public static Promise<RTCVideoContainer> create() {
        
        return RTC.createRTC().then(rtc->{
            RTCVideoElement video = rtc.createVideo();
            video.setAutoplay(true);
            rtc.append(video);
            video.applyStyle("width:100%;height:100%;");
            return new RTCVideoContainer(rtc, video);
        });

    }*/
    
    
    /**
     * Gets a reference to the internal RTC instance.  Each RTCVideoContainer uses its own
     * embedded RTC instance.
     * @return 
     */
    public RTC getRTC() {
        return rtc;
    }
    
    /**
     * 
     * {@inheritDoc }
     */
    @Override
    public int getVideoWidth() {

        return internal.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return internal.getVideoHeight();
    }

    @Override
    public boolean playsInline() {

        return internal.playsInline();
    }

    @Override
    public void setPlaysInline(boolean playsInline) {
        internal.setPlaysInline(playsInline);
    }

    @Override
    public double getCurrentTime() {

        return internal.getCurrentTime();
    }

    @Override
    public double getDuration() {

        return internal.getDuration();
    }

    @Override
    public boolean isEnded() {

        return internal.isEnded();
    }

    @Override
    public RTCMediaError getError() {

        return internal.getError();
    }

    @Override
    public boolean isLoop() {
        return internal.isLoop();
    }

    @Override
    public boolean isMuted() {
        return internal.isMuted();
    }

    @Override
    public void setMuted(boolean muted) {
        
        internal.setMuted(muted);
        
    }

    @Override
    public NetworkState getNetworkState() {

        return internal.getNetworkState();
    }

    @Override
    public boolean isPaused() {

        return internal.isPaused();
    }

    @Override
    public double getPlaybackRate() {

        return internal.getPlaybackRate();
    }

    @Override
    public TimeRanges getPlayed() {

        return internal.getPlayed();
    }

    @Override
    public Preload getPreload() {
        return internal.getPreload();
                
    }

    @Override
    public ReadyState getReadyState() {
        return internal.getReadyState();
    }

    @Override
    public TimeRanges getSeekable() {

        return internal.getSeekable();
    }

    @Override
    public boolean isSeeking() {

        return internal.isSeeking();
    }

    @Override
    public String getSinkId() {
        return internal.getSinkId();
    }

    @Override
    public String getSrc() {
        return internal.getSrc();
        
    }

    @Override
    public TextTracks getTextTracks() {
        
        return internal.getTextTracks();
    }

    @Override
    public VideoTracks getVideoTracks() {
        return internal.getVideoTracks();
    }

    @Override
    public AudioTracks getAudioTracks() {
        return internal.getAudioTracks();
    }

    @Override
    public boolean isAutoplay() {
        return internal.isAutoplay();
    }

    @Override
    public void setAutoplay(boolean autoplay) {
        internal.setAutoplay(autoplay);
    }

    @Override
    public double getVolume() {
        return internal.getVolume();
    }

    @Override
    public MediaStream getSrcObject() {
        return internal.getSrcObject();
    }

    @Override
    public void setSrcObject(MediaStream stream) {
        debug("setSrcObject("+stream+")");
        _setStream(stream);
        
    }

    @Override
    public void addTextTrack(TextTrack track) {
        internal.addTextTrack(track);
    }

    @Override
    public MediaStream captureStream() {
        return internal.captureStream();
    }

    @Override
    public CanPlay canPlayType(String type) {
        return internal.canPlayType(type);
    }

    @Override
    public void fastSeek(double time) {
        internal.fastSeek(time);
    }

    @Override
    public void load() {
        internal.load();
    }

    @Override
    public void pause() {
        internal.pause();
    }

    @Override
    public Promise play() {
        return internal.play();
    }

    @Override
    public Promise setSinkId(String sinkId) {
        return internal.setSinkId(sinkId);
    }

    @Override
    public RTCMediaElement addAbortListener(RTCMediaElementListener l) {
        return internal.addAbortListener(l);
    }

    @Override
    public RTCMediaElement removeAbortListener(RTCMediaElementListener l) {
        return internal.removeAbortListener(l);
    }

    @Override
    public RTCMediaElement onabort(RTCMediaElementListener l) {
        return internal.onabort(l);
    }

    @Override
    public RTCMediaElement addCanPlayListener(RTCMediaElementListener l) {
        return internal.addCanPlayListener(l);
    }

    @Override
    public RTCMediaElement removeCanPlayListener(RTCMediaElementListener l) {
        return internal.removeCanPlayListener(l);
    }

    @Override
    public RTCMediaElement oncanplay(RTCMediaElementListener l) {
        return internal.oncanplay(l);
    }

    @Override
    public RTCMediaElement addCanPlayThroughListener(RTCMediaElementListener l) {
        return internal.addCanPlayThroughListener(l);
    }

    @Override
    public RTCMediaElement removeCanPlayThroughListener(RTCMediaElementListener l) {
        return removeCanPlayThroughListener(l);
    }

    @Override
    public RTCMediaElement oncanplaythrough(RTCMediaElementListener l) {
        return internal.oncanplaythrough(l);
    }

    @Override
    public RTCMediaElement addDurationChangeListener(RTCMediaElementListener l) {
        return internal.addDurationChangeListener(l);
    }

    @Override
    public RTCMediaElement removeDurationChangeListener(RTCMediaElementListener l) {
        return internal.removeDurationChangeListener(l);
    }

    @Override
    public RTCMediaElement ondurationchange(RTCMediaElementListener l) {
        return internal.ondurationchange(l);
    }

    @Override
    public RTCMediaElement addEmptiedListener(RTCMediaElementListener l) {
        return internal.addEmptiedListener(l);
    }

    @Override
    public RTCMediaElement removeEmptiedListener(RTCMediaElementListener l) {
        return internal.removeEmptiedListener(l);
    }

    @Override
    public RTCMediaElement onemptied(RTCMediaElementListener l) {
        return internal.onemptied(l);
    }

    @Override
    public RTCMediaElement addEndedListener(RTCMediaElementListener l) {
        return internal.addEmptiedListener(l);
    }

    @Override
    public RTCMediaElement removeEndedListener(RTCMediaElementListener l) {
        return internal.removeEndedListener(l);
                
    }

    @Override
    public RTCMediaElement onended(RTCMediaElementListener l) {
        return internal.onended(l);
    }

    @Override
    public RTCMediaElement addErrorListener(ErrorListener l) {
        return internal.addErrorListener(l);
    }

    @Override
    public RTCMediaElement removeErrorListener(ErrorListener l) {
        return internal.removeErrorListener(l);
    }

    @Override
    public RTCMediaElement onerror(ErrorListener l) {
        return internal.onerror(l);
    }

    @Override
    public RTCMediaElement addLoadedDataListener(RTCMediaElementListener l) {
        return internal.addLoadedDataListener(l);
    }

    @Override
    public RTCMediaElement removeLoadedDataListener(RTCMediaElementListener l) {
        return internal.removeLoadedDataListener(l);
    }

    @Override
    public RTCMediaElement onloadeddata(RTCMediaElementListener l) {
        return internal.onloadeddata(l);
    }

    @Override
    public RTCMediaElement addLoadedMetadataListener(RTCMediaElementListener l) {
        return internal.addLoadedMetadataListener(l);
    }

    @Override
    public RTCMediaElement removeLoadedMetadataListener(RTCMediaElementListener l) {
        return internal.removeLoadedMetadataListener(l);
    }

    @Override
    public RTCMediaElement onloadedmetadata(RTCMediaElementListener l) {
        return internal.onloadeddata(l);
    }

    @Override
    public RTCMediaElement addLoadStartListener(RTCMediaElementListener l) {
        return internal.addLoadStartListener(l);
    }

    @Override
    public RTCMediaElement removeLoadStartListener(RTCMediaElementListener l) {
        return internal.removeLoadStartListener(l);
    }

    @Override
    public RTCMediaElement onloadstart(RTCMediaElementListener l) {
        return internal.onloadstart(l);
    }

    @Override
    public RTCMediaElement addPauseListener(RTCMediaElementListener l) {
        return internal.addPauseListener(l);
    }

    @Override
    public RTCMediaElement removePauseListener(RTCMediaElementListener l) {
        return internal.removePauseListener(l);
    }

    @Override
    public RTCMediaElement onpause(RTCMediaElementListener l) {
        return internal.onpause(l);
    }

    @Override
    public RTCMediaElement addPlayListener(RTCMediaElementListener l) {
        return internal.addPlayListener(l);
    }

    @Override
    public RTCMediaElement removePlayListener(RTCMediaElementListener l) {
        return internal.removePlayListener(l);
    }

    @Override
    public RTCMediaElement onplay(RTCMediaElementListener l) {
        return internal.onplay(l);
    }

    @Override
    public RTCMediaElement addPlayingListener(RTCMediaElementListener l) {
        return internal.addPlayingListener(l);
    }

    @Override
    public RTCMediaElement removePlayingListener(RTCMediaElementListener l) {
        return internal.removePlayingListener(l);
    }

    @Override
    public RTCMediaElement onplaying(RTCMediaElementListener l) {
        return internal.onplaying(l);
    }

    @Override
    public RTCMediaElement addProgressListener(RTCMediaElementListener l) {
        return internal.addProgressListener(l);
    }

    @Override
    public RTCMediaElement removeProgressListener(RTCMediaElementListener l) {
        return internal.removeProgressListener(l);
    }

    @Override
    public RTCMediaElement onprogress(RTCMediaElementListener l) {
        return internal.onprogress(l);
    }

    @Override
    public RTCMediaElement addRateChangeListener(RTCMediaElementListener l) {
        return internal.addRateChangeListener(l);
    }

    @Override
    public RTCMediaElement removeRateChangeListener(RTCMediaElementListener l) {
        return internal.removeRateChangeListener(l);
    }

    @Override
    public RTCMediaElement onratechange(RTCMediaElementListener l) {
        return internal.onratechange(l);
    }

    @Override
    public RTCMediaElement addSeekedListener(RTCMediaElementListener l) {
        return internal.addSeekedListener(l);
    }

    @Override
    public RTCMediaElement removeSeekedListener(RTCMediaElementListener l) {
        return internal.removeSeekedListener(l);
                
                
    }

    @Override
    public RTCMediaElement onseeked(RTCMediaElementListener l) {
        return internal.onseeked(l);
    }

    @Override
    public RTCMediaElement addSeekingListener(RTCMediaElementListener l) {
        return internal.addSeekingListener(l);
    }

    @Override
    public RTCMediaElement removeSeekingListener(RTCMediaElementListener l) {
        return internal.removeSeekingListener(l);
    }

    @Override
    public RTCMediaElement onseeking(RTCMediaElementListener l) {
        return internal.onseeking(l);
    }

    @Override
    public RTCMediaElement addStalledtListener(RTCMediaElementListener l) {
        return internal.addStalledtListener(l);
    }

    @Override
    public RTCMediaElement removeStalledListener(RTCMediaElementListener l) {
        return internal.removeStalledListener(l);
    }

    @Override
    public RTCMediaElement onstalled(RTCMediaElementListener l) {
        return internal.onstalled(l);
    }

    @Override
    public RTCMediaElement addSuspendListener(RTCMediaElementListener l) {
        return internal.addSuspendListener(l);
    }

    @Override
    public RTCMediaElement removeSuspendListener(RTCMediaElementListener l) {
        return internal.removeSuspendListener(l);
    }

    @Override
    public RTCMediaElement onsuspend(RTCMediaElementListener l) {
        return internal.onsuspend(l);
    }

    @Override
    public RTCMediaElement addTimeUpdateListener(RTCMediaElementListener l) {
        return internal.addTimeUpdateListener(l);
    }

    @Override
    public RTCMediaElement removeTimeUpdateListener(RTCMediaElementListener l) {
        return internal.removeTimeUpdateListener(l);
    }

    @Override
    public RTCMediaElement ontimeupdate(RTCMediaElementListener l) {
        return internal.ontimeupdate(l);
    }

    @Override
    public RTCMediaElement addVolumeChangeListener(RTCMediaElementListener l) {
        return internal.addVolumeChangeListener(l);
    }

    @Override
    public RTCMediaElement removeVolumeChangeListener(RTCMediaElementListener l) {
        return internal.removeVolumeChangeListener(l);
    }

    @Override
    public RTCMediaElement onvolumechange(RTCMediaElementListener l) {
        return internal.onvolumechange(l);
    }

    @Override
    public RTCMediaElement addWaitingListener(RTCMediaElementListener l) {
        return internal.addWaitingListener(l);
    }

    @Override
    public RTCMediaElement removeWaitingListener(RTCMediaElementListener l) {
        return internal.removeWaitingListener(l);
    }

    @Override
    public RTCMediaElement onwaiting(RTCMediaElementListener l) {
        return internal.onwaiting(l);
    }

    @Override
    public void setId(String id) {
        internal.setId(id);
    }

    @Override
    public String getId() {
        return internal.getId();
    }

    @Override
    public void fill() {
        internal.fill();
    }

    @Override
    public void applyStyle(RTCStyle style) {
        internal.applyStyle(style);
    }

    @Override
    public void applyStyle(String css) {
        internal.applyStyle(css);
    }

    @Override
    public void addEventListener(String eventName, EventListener listener) {
        internal.addEventListener(eventName, listener);
    }

    @Override
    public void removeEventListener(String eventName, EventListener listener) {
        internal.removeEventListener(eventName, listener);
    }

    @Override
    public boolean dispatchEvent(Event evt) {
        return internal.dispatchEvent(evt);
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
    
}
