/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.Util;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.CN;
import com.codename1.ui.Component;

import com.codename1.util.AsyncResource;
import com.codename1.util.Callback;
import com.codename1.util.SuccessCallback;
import com.codename1.webrtc.MediaStream.ReadyState;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RTC {
    private BrowserComponent web;
    private Map<String,RefCounted> registry = new HashMap<>();
    private final AsyncResource<RTC> async = new AsyncResource<RTC>();
    
    private RTC() {
        init();
    }
    
    public static AsyncResource<RTC> createRTC() {
        return new RTC().async;
    }
    
    private void init() {
        web = new BrowserComponent();
        web.addWebEventListener(BrowserComponent.onLoad, e->{
            System.out.println("In onLoad handler");
            web.addJSCallback("window.cn1Callback = function(data) {callback.onSuccess(JSON.stringify(data))}", (value) -> {
                try {
                    Map data = parseJSON(value.getValue());
                    String target = (String)data.get("target");
                    String eventType = (String)data.get("type");
                    RefCounted targetObject = (RefCounted)registry.get(target);
                    if (targetObject == null) {
                        return;
                    }
                    if (targetObject instanceof EventTarget) {
                        Event evt = new EventImpl(eventType, data);
                        EventTarget eventTarget = (EventTarget)targetObject;
                        eventTarget.dispatchEvent(evt);
                    }
                } catch (IOException ex) {
                    Log.e(ex);
                }
            });
            async.complete(RTC.this);
        });
        try {
            web.setPage(Util.readToString(CN.getResourceAsStream("/com_codename1_rtc_RTCBootstrap.html")), "http://localhost/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    private class EventSupport implements EventTarget {
        private final EventTarget target;
        private Map<String,ArrayList<EventListener>> listeners = new HashMap<>();
        
        public EventSupport(EventTarget target) {
            this.target = target;
        }

        @Override
        public void addEventListener(String eventName, EventListener listener) {
            if (!listeners.containsKey(eventName)) {
                ArrayList<EventListener> l = new ArrayList<>();
                listeners.put(eventName, l);
            }
            listeners.get(eventName).add(listener);
        }

        @Override
        public void removeEventListener(String eventName, EventListener listener) {
            if (listeners.containsKey(eventName)) {
                listeners.get(eventName).remove(listener);
                if (listeners.get(eventName).isEmpty()) {
                    listeners.remove(eventName);
                }
            }
        }

        @Override
        public boolean dispatchEvent(Event evt) {
            List<EventListener> targets = listeners.get(evt.getType());
            if (targets != null) {
                targets = new ArrayList<EventListener>(targets);
                for (EventListener l : targets) {
                    l.handleEvent(evt);
                    if (((EventImpl)evt).canceled) {
                        return false;
                    }
                }
            }
            return !((EventImpl)evt).canceled;
        }
        
    }
    
    private class EventImpl implements Event {
        private String type;
        private boolean canceled;
        private Map data;
        public EventImpl(String type) {
            this.type = type;
        }
        
        public EventImpl(String type, Map data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public void preventDefault() {
            canceled = true;
        }
        
        public Map getData() {
            return data;
        }
    }
    
    private static Map parseJSON(String json) throws IOException {
        JSONParser p = new JSONParser();
        p.setUseBoolean(true);
        p.setIncludeNullsInstance(true);
        
        return p.parseJSON(new StringReader(json));
        
    }
    
    private static class RTCPromiseImpl<T> extends AsyncResource<T> implements RTCPromise<T> {

        @Override
        public RTCPromise<T> then(SuccessCallback<T> onFulfilled, SuccessCallback<Throwable> onRejected) {
            if (onFulfilled != null) this.ready(onFulfilled);
            
            if (onRejected != null) this.except(onRejected);
            return this;
        }
        
        

        @Override
        public RTCPromise<T> onCatch(SuccessCallback<Throwable> onRejected) {
            this.except(onRejected);
            return this;
        }

        @Override
        public RTCPromise<T> onFinally(SuccessCallback onFinally) {
            this.onResult((res, err) ->{
                onFinally.onSucess(null);
            });
            return this;
        }

        @Override
        public RTCPromise<T> then(SuccessCallback<T> onFulfilled) {
            return then(onFulfilled, null);
        }
        
    }
    
   
    
    public <T> RTCPromise<T> newPromise(Class<T> type) {
        return new RTCPromiseImpl<T>();
    }
    
    
    
    
    private class RefCountedImpl implements RefCounted {
        
        private String refId;
        private int count;
        
        public void init(String refId, String javascript) {
            this.refId = refId;
            web.execute("var o = ("+javascript+");registry.retain(o, ${0})", new Object[]{refId});
        }

        @Override
        public void retain() {
            if (registry.containsKey(refId)) {
                count++;
                web.execute("registry.retain(${0})", new Object[]{refId});
            } else {
                // If this object is not yet in the java registry, then we don't
                // need to call retain on the javascript side because it will have
                // already been retained once.
                count++;
                registry.put(refId, this);
            }
            
        }

        @Override
        public void release() {
            count--;
            if (count == 0) {
                dealloc();
                registry.remove(refId);
            }
            web.execute("registry.release(${0})", new Object[]{refId});
            
        }
        
       
        public void setRefId(String id) {
            refId = id;
            
        }
        
        public String getRefId() {
            return refId;
        }
        
        public void dealloc() {
            
        }
        
    }
    
    private class MediaStreamImpl extends RefCountedImpl implements MediaStream {
        private String id;
        private boolean active, ended;
        private ReadyState readyState;
        private EventSupport es = new EventSupport(this);
        private MediaStreamTracks tracks=new MediaStreamTracks();
                
                
        
        
        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public boolean isEnded() {
            return ended;
        }

        @Override
        public ReadyState getReadyState() {
            return readyState;
        }

        @Override
        public void addEventListener(String eventName, EventListener listener) {
            es.addEventListener(eventName, listener);
        }

        @Override
        public void removeEventListener(String eventName, EventListener listener) {
            es.removeEventListener(eventName, listener);
        }

        @Override
        public boolean dispatchEvent(Event evt) {
            return es.dispatchEvent(evt);
        }

        
        
        @Override
        public MediaStreamTracks getAudioTracks() {
            MediaStreamTracks out = new MediaStreamTracks();
            for (MediaStreamTrack track : tracks) {
                if (track.getKind() == TrackKind.Audio) {
                    out.add(track);
                }
            }
            return out;
        }

        @Override
        public MediaStreamTracks getVideoTracks() {
            MediaStreamTracks out = new MediaStreamTracks();
            for (MediaStreamTrack track : tracks) {
                if (track.getKind() == TrackKind.Video) {
                    out.add(track);
                }
            }
            return out;
        }
        
        @Override
        public MediaStreamTracks getTracks() {
            MediaStreamTracks out = new MediaStreamTracks();
            for (MediaStreamTrack track : tracks) {
                out.add(track);
                
            }
            return out;
        }

        @Override
        public void dealloc() {
            for (MediaStreamTrack track : tracks) {
                track.release();
            }
            tracks.clear();
            super.dealloc();
        }

        @Override
        public void addTrack(MediaStreamTrack track) {
            MediaStreamTrack existing = getTrackById(track.getId());
            if (existing == null) {
                tracks.add(track);
                track.retain();
                web.execute("var stream = registry.get(${0});"
                        + "var track = registry.get(${1}); "
                        + "stream.addTrack(track);", 
                        new Object[]{getRefId(), ((MediaStreamTrackImpl)track).getRefId()});
            }
        }

        @Override
        public void removeTrack(MediaStreamTrack track) {
            MediaStreamTrack existing = getTrackById(track.getId());
            if (existing != null) {
                tracks.remove(existing);
                web.execute("registry.get(${0}).removeTrack(${1})",
                        new Object[]{getRefId(), ((MediaStreamTrackImpl)track).getRefId()});
                existing.release();
                
            }
        }

        @Override
        public MediaStream clone() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public MediaStreamTrack getTrackById(String id) {
            for (MediaStreamTrack track : tracks) {
                if (id.equals(track.getId())) {
                    return track;
                }
            }
            return null;
        }
        
        
        
    }
    
    
    
    
    private MediaTrackSettings newMediaTrackSettings(String kind, Map m) {
        
        if ("audio".equals(kind)) {
            AudioTrackSettings out = new AudioTrackSettings();
            out.fromJSONStruct(m);
            return out;
        }
        if ("video".equals(kind)) {
            VideoTrackSettings out = new VideoTrackSettings();
            out.fromJSONStruct(m);
            return out;
        }
        
        MediaTrackSettings out = new MediaTrackSettings();
        out.fromJSONStruct(m);
        return out;
    }
    
    private MediaStreamTrack newMediaStreamTrack(Map data) {
        String trackId = (String)data.get("id");
        return  new MediaStreamTrackImpl(
                        (String)data.get("refId"),
                        trackId,
                        (String)data.get("contentHint"),
                        (boolean)data.get("enabled"),
                        (String)data.get("kind"),
                        (String)data.get("label"),
                        (boolean)data.get("muted"),
                        (boolean)data.get("readOnly"),
                        (String)data.get("readyState"),
                        (boolean)data.get("remote"),
                        newMediaTrackSettings((String)data.get("kind"), (Map)data.get("settings"))
                                
                );
    }
    
    private MediaStream newMediaStream(String refId, String id, boolean active, boolean ended, List<Map> tracks) {
        MediaStreamImpl out = new MediaStreamImpl();
        out.id = id;
        out.active = active;
        out.ended = ended;
        out.setRefId(refId);
        out.retain();
        
        for (Map track : tracks) {
            MediaStreamTrack mst = newMediaStreamTrack(track);
            out.tracks.add(mst);
        }
        
        out.addEventListener("ended", evt->{
            out.ended = true;
            out.active = false;
            out.readyState = ReadyState.Ended;
        });
        out.addEventListener("addtrack", evt->{
            EventImpl e = (EventImpl)evt;
            Map data = e.getData();
            String trackId = (String)data.get("id");
            MediaStreamTrack existingTrack = out.getTrackById(trackId);
            
            if (existingTrack == null) {
                MediaStreamTrack newTrack = newMediaStreamTrack(data);
                out.tracks.add(newTrack);
                
            }
        });
        
        out.addEventListener("removetrack", evt->{
            EventImpl e = (EventImpl)evt;
            Map data = e.getData();
            String trackId = (String)data.get("id");
            MediaStreamTrack track = out.getTrackById(trackId);
            if (track != null) {
                if (out.tracks.remove(track)) {
                    track.release();
                }
            }
        });
        return out;
    }
    
    private class MediaStreamTrackImpl extends RefCountedImpl implements MediaStreamTrack {
        private String contentHint;
        private boolean enabled;
        private String id;
        private TrackKind kind;
        private String label;
        private boolean muted;
        private boolean readOnly;
        private ReadyState readyState;
        private boolean remote;
        private MediaTrackSettings settings;
        private MediaTrackConstraints constraints;
        private EventSupport es = new EventSupport(this);
        
        private MediaStreamTrackImpl(
            String refId, 
            String id, 
            String contentHint, 
            boolean enabled,
            String kind,
            String label,
            boolean muted,
            boolean readonly,
            String readyState,
            boolean remote,
            MediaTrackSettings settings
            ) {
            setRefId(refId);
            this.id = id;
            this.contentHint = contentHint;
            this.enabled = enabled;
            for (TrackKind trackKind: TrackKind.values()){
                if (trackKind.matches(kind)) {
                    this.kind = trackKind;
                    break;
                }
            }
            for (ReadyState rs : ReadyState.values()) {
                if (rs.matches(readyState)) {
                    this.readyState = rs;
                    break;
                }
            }
            this.label = label;
            this.muted = muted;
            this.readOnly = readonly;
            this.remote = remote;
            this.settings = settings;
            retain();
            
            addEventListener("ended", evt->{
                
                this.readyState = ReadyState.Ended;
                release();
            });
            addEventListener("mute", evt-> {
                this.muted = true;
            });
            addEventListener("unmute", evt->{
                this.muted = false;
            });
        }
                

        @Override
        public String getContentHint() {
            return contentHint;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public TrackKind getKind() {
            return kind;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public boolean isMuted() {
            return muted;
        }

        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public ReadyState getReadyState() {
            return readyState;
        }

        @Override
        public boolean isRemote() {
            return remote;
        }

        @Override
        public RTCPromise applyConstraints(MediaTrackConstraints constraints) {
            if (stopping || readyState == ReadyState.Ended) {
                throw new IllegalStateException("Track is already ended");
            }
            RTCPromiseImpl out = new RTCPromiseImpl();
            web.execute("var track = registry.get(${0});track.applyConstraints("+constraints.toJSONStruct()+")"
                    + ".then(function(res){"
                    + "  callback.onSuccess(JSONStringify({settings:track.getSettings()}));"
                    + "})"
                    + ".catch(function(err) {"
                    + "  callback.onError(err);"
                    + "});", new Object[]{getRefId()}, new Callback<BrowserComponent.JSRef>() {
                @Override
                public void onSucess(BrowserComponent.JSRef value) {
                    MediaStreamTrackImpl.this.constraints = constraints;
                    Map data;
                    try {
                        data = parseJSON(value.getValue());
                    } catch (Exception ex) {
                        Log.e(ex);
                        onError(this, ex, 0, ex.getMessage());
                        return;
                    }
                    settings = newMediaTrackSettings(kind.toString(), (Map)data.get("settings"));
                    out.complete(null);
                }

                @Override
                public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                    out.error(err);
                }
                    });
            return out;
        }

        @Override
        public MediaStreamTrack clone() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public MediaTrackConstraints getConstraints() {
            return constraints;
        }

        @Override
        public MediaTrackSettings getSettings() {
            return settings;
        }

        private boolean stopping;
        
        @Override
        public void stop() {
            if (!stopping && readyState != ReadyState.Ended) {
                stopping = true;
                web.execute("registry.get(${0}).stop();", new Object[]{getRefId()});
            }
            
        }

        

        @Override
        public void addEventListener(String eventName, EventListener listener) {
            es.addEventListener(eventName, listener);
        }

        @Override
        public void removeEventListener(String eventName, EventListener listener) {
            es.removeEventListener(eventName, listener);
        }

        @Override
        public boolean dispatchEvent(Event evt) {
            return es.dispatchEvent(evt);
        }
        
    }
    
    public RTCPromise<MediaStream> getUserMedia(MediaStreamConstraints constraints) {
        RTCPromise<MediaStream> out = newPromise(MediaStream.class);
        System.out.println("About to get user media");
        web.execute("try{navigator.getUserMedia("+constraints.toJSON()+", "
                + "function(stream){"
                + "  registry.retain(stream);window.stream = stream;"
                + "  var tracks = stream.getTracks();"
                + "  var len = tracks.length;"
                + "  var tracks_ = [];"
                + "  for (var i=0; i<len; i++) {"
                + "    var track = tracks[i];"
                + "    registry.retain(track);"
                + "    var track_ = {refId : registry.id(track)};"
                + "    ['contentHint', 'id', 'enabled', 'kind', 'label', 'muted', 'readyState', 'readOnly', 'remote'].forEach(function(val, index, array){"
                + "        track_[val] = track[val]||false;"
                + "    });"
                + "    track_.contentHint = track_.contentHint || '';"
                + "    track_.settings = track.getSettings();"
                + "    tracks_.push(track_);"
                + "  }"
                + "  console.log('tracks', tracks_);"
                + "  callback.onSuccess(JSON.stringify({"
                + "    refId:registry.id(stream), "
                + "    id: stream.id||'', "
                + "    active:stream.active||false, "
                + "    ended:stream.ended||false,"
                + "    tracks: tracks_}));"
                + "},"
                + "function(error){"
                + "  callback.onError(JSON.stringify(error));"
                + "})} catch (e)"
                + "{console.log('exception in getUserMedia', e);callback.onError(JSON.stringify(e));}", new Callback<BrowserComponent.JSRef>() {
                    @Override
                    public void onSucess(BrowserComponent.JSRef value) {
                        System.out.println("getUserMedia complete");
                        try {
                            Map res = parseJSON(value.getValue());
                            List<Map> tracks = (List)res.get("tracks");
                            
                            MediaStream stream = newMediaStream(
                                    (String)res.get("refId"), 
                                    (String)res.get("id"), 
                                    (Boolean)res.get("active"), 
                                    (Boolean)res.get("ended"),
                                    tracks);
                            for (MediaStreamTrack track : stream.getVideoTracks()) {
                                ((MediaStreamTrackImpl)track).constraints = constraints.getVideoConstraints();
                            }
                            for (MediaStreamTrack track : stream.getAudioTracks()) {
                                ((MediaStreamTrackImpl)track).constraints = constraints.getAudioConstraints();
                            }
                            ((RTCPromiseImpl)out).complete(stream);
                            
                        } catch (IOException ex) {
                            onError(this, ex, 0, ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                        System.out.println("GetUserMedia failed");
                        ((RTCPromiseImpl)out).error(new AsyncResource.AsyncExecutionException(err));
                    }
                }
        );
        
        return out;
                
    }
    
    public Component getVideoComponent() {
        return web;
    }
    
    private class RTCVideoElementImpl extends RefCountedImpl implements RTCVideoElement {
        private MediaStreamImpl srcObject;
        private EventSupport es = new EventSupport(this);
        private double currentTime;
        private double duration;
        private boolean ended;
        private RTCMediaError error;
        private boolean loop, muted;
        private NetworkState networkState;
        private boolean paused;
        private double playbackRate;
        private TimeRanges played;
        private Preload preload;
        private ReadyState readyState;
        private TimeRanges seekable;
        private boolean seeking;
        private String sinkId;
        private String src;
        private TextTracks textTracks;
        private AudioTracks audioTracks;
        private VideoTracks videoTracks;
        private double volume;
        private boolean autoplay;
        private int videoWidth, videoHeight;
        
        public RTCVideoElementImpl() {
            init(Util.getUUID(), "(function(){var vid = document.createElement('video'); "
                    + "vid.style.position='fixed'; "
                    + "vid.style.top='0'; "
                    + "vid.style.bottom='0', "
                    + "vid.style.left='0'; "
                    + "vid.style.right='0';"
                    + "vid.style.width='100%';"
                    + "return vid})()");
            
            retain();
            addEventListener("abort", evt->{
                updateState();
            });
            
            addEventListener("canplay", evt->{
                readyState = ReadyState.HaveFutureData;
            });
            
            addEventListener("canplaythrough", evt->{
                readyState = ReadyState.HaveEnoughData;
            });
            addEventListener("durationchange", evt->{
                EventImpl e = (EventImpl)evt;
                Object durationObj = e.getData().get("duration");
                if (durationObj instanceof Number) {
                    duration = ((Number)durationObj).doubleValue();
                } else {
                    duration = -1;
                }
            });
            addEventListener("ended", evt->{
                ended = true;
            });
            
            addEventListener("error", evt->{
                EventImpl e = (EventImpl)evt;
                this.error = new RTCMediaError((String)e.getData().get("message"), ((Number)e.getData().get("code")).intValue());
            });
            addEventListener("loadeddata", evt->{
                
            });
            addEventListener("loadstart", evt->{
                networkState = NetworkState.Loading;
            });
            
            addEventListener("pause", evt->{
                paused = true;
            });
            
            addEventListener("play", evt->{
                paused = false;
                ended = false;
            });
            
            addEventListener("playing", evt->{
                ended = false;
            });
            
            addEventListener("progress", evt->{
                
            });
            
            addEventListener("ratechange", evt->{
                EventImpl e = (EventImpl)evt;
                this.playbackRate = ((Number)e.getData().get("playbackRate")).intValue();
            });
            
            addEventListener("seeked", evt->{
                seeking = false;
                
            });
            
            addEventListener("seeking", evt->{
                seeking = true;
            });
            
            addEventListener("timeupdate", evt->{
                EventImpl e = (EventImpl)evt;
                Object currTimeObj = e.getData().get("currentTime");
                if (currTimeObj instanceof Number) {
                    this.currentTime = ((Number)currTimeObj).doubleValue();
                } else {
                    this.currentTime = -1;
                }
            });
            
            addEventListener("volumechange", evt->{
                EventImpl e = (EventImpl)evt;
                this.volume = ((Number)e.getData().get("volume")).doubleValue();
            });
            
            addEventListener("resize", evt->{
                EventImpl e = (EventImpl)evt;
                this.videoWidth = ((Number)e.getData().get("videoWidth")).intValue();
                this.videoHeight = ((Number)e.getData().get("videoHeight")).intValue();
            });
        }
        
        private void updateState() {
            // Copies state from JS to java
        }

        @Override
        public MediaStream getSrcObject() {
            return srcObject;
        }

        @Override
        public void setSrcObject(MediaStream stream) {
            if (stream != srcObject) {
                if (srcObject != null) {
                    srcObject.release();
                }
                srcObject = (MediaStreamImpl)stream;
                srcObject.retain();
                web.execute("registry.get(${0}).srcObject = registry.get(${1});", new Object[]{getRefId(), srcObject.getRefId()});
            }
        }

        @Override
        public void addEventListener(String eventName, EventListener listener) {
            es.addEventListener(eventName, listener);
        }

        @Override
        public void removeEventListener(String eventName, EventListener listener) {
            es.removeEventListener(eventName, listener);
        }

        @Override
        public boolean dispatchEvent(Event evt) {
            return es.dispatchEvent(evt);
        }

        @Override
        public void dealloc() {
            if (srcObject != null) {
                srcObject.release();
                srcObject = null;
            }
            super.dealloc();
        }

        @Override
        public double getCurrentTime() {
            return currentTime;
        }

        @Override
        public double getDuration() {
            return duration;
        }

        @Override
        public boolean isEnded() {
            return ended;
        }

        @Override
        public RTCMediaError getError() {
            return error;
        }

        @Override
        public boolean isLoop() {
            return loop;
        }

        @Override
        public boolean isMuted() {
            return muted;
        }

        @Override
        public NetworkState getNetworkState() {
            return networkState;
        }

        @Override
        public boolean isPaused() {
            return paused;
        }

        @Override
        public double getPlaybackRate() {
            return playbackRate;
        }

        @Override
        public TimeRanges getPlayed() {
            return played;
        }

        @Override
        public Preload getPreload() {
            return preload;
        }

        @Override
        public ReadyState getReadyState() {
            return readyState;
        }

        @Override
        public TimeRanges getSeekable() {
            return seekable;
        }

        @Override
        public boolean isSeeking() {
            return seeking;
        }

        @Override
        public String getSinkId() {
            return sinkId;
        }

        @Override
        public String getSrc() {
            return src;
        }

        @Override
        public TextTracks getTextTracks() {
            return textTracks;
        }

        @Override
        public VideoTracks getVideoTracks() {
            return videoTracks;
        }

        @Override
        public AudioTracks getAudioTracks() {
            return audioTracks;
        }

        @Override
        public double getVolume() {
            return volume;
        }

        @Override
        public boolean isAutoplay() {
            return autoplay;
        }

        @Override
        public void setAutoplay(boolean autoplay) {
            if (autoplay != this.autoplay) {
                this.autoplay = autoplay;
                web.execute("registry.get(${0}).autoplay="+autoplay+";", new Object[]{getRefId()});
            }
        }

        @Override
        public void addTextTrack(TextTrack track) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public MediaStream captureStream() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public CanPlay canPlayType(String type) {
            return CanPlay.Unknown;
        }

        @Override
        public void fastSeek(double time) {
            web.execute("registry.get(${0}).fastSeek("+time+");", new Object[]{getRefId()});
        }

        @Override
        public void load() {
            web.execute("registry.get(${0}).load();", new Object[]{getRefId()});
        }

        @Override
        public void pause() {
            web.execute("registry.get(${0}).pause();", new Object[]{getRefId()});
        }

        @Override
        public RTCPromise play() {
            RTCPromiseImpl out = (RTCPromiseImpl)newPromise(Object.class);
            web.execute("registry.get(${0}).play().then(function(){callback.onSuccess(null)}, function(e){callback.onError(e);});", new Object[]{getRefId()}, new Callback<BrowserComponent.JSRef>() {
                @Override
                public void onSucess(BrowserComponent.JSRef value) {
                    out.complete(null);
                }

                @Override
                public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                    out.error(new AsyncResource.AsyncExecutionException(err));
                }
            });
            return out;
        }

        @Override
        public void setSinkId(String sinkId) {
            web.execute("registry.get(${0}).setSinkId(${1});", new Object[]{getRefId(), sinkId});
        }
        
        public int getVideoWidth() {
            return videoWidth;
        }
        
        public int getVideoHeight() {
            return videoHeight;
        }

    }
    
    public RTCVideoElement createVideo() {
        RTCVideoElementImpl out = new RTCVideoElementImpl();
        return out;
    }
    
    public void append(RTCElement el) {
        if (el instanceof RefCounted) {
            RefCountedImpl ref = (RefCountedImpl)el;
            web.execute("document.body.appendChild(registry.get(${0}))", new Object[]{ref.getRefId()});
        }
    }
    
    
    
    
    
}
