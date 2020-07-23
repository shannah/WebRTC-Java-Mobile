/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.Util;
import com.codename1.processing.Result;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.BrowserComponent.JSRef;
import com.codename1.ui.CN;
import com.codename1.ui.Component;

import com.codename1.util.AsyncResource;
import com.codename1.util.AsyncResult;
import com.codename1.util.Callback;
import com.codename1.util.StringUtil;
import com.codename1.util.SuccessCallback;
import com.codename1.webrtc.MediaStream.ReadyState;
import com.codename1.webrtc.RTCPeerConnection.RTCDataChannelInit;
import com.codename1.webrtc.RTCStyle.CSSProperty;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

/**
 *
 * @author shannah
 */
public class RTC implements AutoCloseable {
    private BrowserComponent web;
    private Map<String,RefCounted> registry = new HashMap<>();
    private final AsyncResource<RTC> async = new AsyncResource<RTC>();
    
    private RTC(String htmlBody, String css) {
        init(htmlBody, css);
    }
    
    
    private void execute(String javascript, Object[] params, AsyncResult<JSRef> onResult) {
        web.execute(javascript, params, new Callback<JSRef>() {
            @Override
            public void onSucess(JSRef value) {
                onResult.onReady(value, null);
            }

            @Override
            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                onResult.onReady(null, err);
            }
        
        });
    }
    
    private void execute(String javascript, AsyncResult<JSRef> onResult) {
        web.execute(javascript, new Callback<JSRef>() {
            @Override
            public void onSucess(JSRef value) {
                onResult.onReady(value, null);
            }

            @Override
            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                onResult.onReady(null, err);
            }
        
        });
    }
    
    
    public static AsyncResource<RTC> createRTC() {
        return createRTC("", "");
    }
    
    public static AsyncResource<RTC> createRTC(String htmlBody, String css) {
        return new RTC(htmlBody, css).async;
    }
    
    private void init(String htmlBody, String css) {
        web = new BrowserComponent();
        web.addWebEventListener(BrowserComponent.onLoad, e->{
            
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
                        Event evt;
                        if (eventType.equals("datachannel")) {
                            evt = new RTCDataChannelEventImpl(data);
                        } else if (eventType.equals("icecandidate")) {
                            
                            evt = new RTCPeerConnectionIceEventImpl(eventType, data);
                        } else if (eventType.equals("icecandidateerror")) {
                            evt = new RTCPeerConnectionIceErrorEventImpl(((Number)data.get("errorCode")).intValue());
                        } else if (eventType.equals("track")) {
                            evt = new RTCTrackEventImpl(data);
                        } else {
                            evt = new EventImpl(eventType, data);
                        }
                        EventTarget eventTarget = (EventTarget)targetObject;
                        eventTarget.dispatchEvent(evt);
                        if (evt instanceof RefCounted) {
                            ((RefCounted)evt).release();
                        }
                    }
                } catch (IOException ex) {
                    Log.e(ex);
                }
            });
            async.complete(RTC.this);
        });
        try {
            String pageContent = Util.readToString(CN.getResourceAsStream("/com_codename1_rtc_RTCBootstrap.html"));
            pageContent = StringUtil.replaceFirst(pageContent, "</body>", htmlBody+"</body>");
            pageContent = StringUtil.replaceFirst(pageContent, "</head>", "<style type='text/css'>\n"+css+"\n</style></head>");
            web.setPage(pageContent, "http://localhost/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }

    @Override
    public void close() throws Exception {
        for (RefCounted ref : registry.values()) {
            if (ref instanceof AutoCloseable) {
                ((AutoCloseable)ref).close();
            }
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
    
    private class RTCDataChannelEventImpl extends EventImpl implements RTCDataChannelEvent {
        private RTCDataChannel channel;
        @Override
        public RTCDataChannel getChannel() {
            return channel;
        }
        
        RTCDataChannelEventImpl(Map data) {
            super("datachannel", data);
            channel = new RTCDataChannelImpl(data);
        }
        
    }
    
    private class RTCPeerConnectionIceEventImpl extends EventImpl implements RTCPeerConnectionIceEvent {
        private RTCIceCandidate candidate;
        
        public RTCPeerConnectionIceEventImpl(String type, Map data) {
            super(type, data);
            Map candidateInfo = (Map)data.get("candidate");
            if (candidateInfo != null) {
                candidate = new RTCIceCandidateImpl(candidateInfo);
            }
        }
        
        @Override
        public RTCIceCandidate getCandidate() {
            return candidate;
        }
        
    }
    
    private class RTCPeerConnectionIceErrorEventImpl extends EventImpl implements RTCPeerConnectionIceErrorEvent {
        private int errorCode;

        public RTCPeerConnectionIceErrorEventImpl(int errorCode) {
            super("icecandidateerror");
            this.errorCode = errorCode;
        }

        @Override
        public int getErrorCode() {
            return this.errorCode;
        }
        
        
        
        
    }
    
    private MediaStreamTrack findTrackById(String id) {
        for (RefCounted o : registry.values()) {
            if (o instanceof MediaStreamTrack) {
                MediaStreamTrack track = (MediaStreamTrack)o;
                if (id.equals(track.getId())) {
                    return track;
                }
            }
        }
        return null;
    }
    
    private class RTCRtpSenderImpl extends RefCountedImpl implements RTCRtpSender {
        private RTCDTMFSender dtmf;
        
        private MediaStreamTrack track;
        private RTCRtpSendParameters parameters;
        private RTCStatsReport stats;
        
        private boolean ready;
        private List<Runnable> onReady = new ArrayList<Runnable>();
        
        RTCRtpSenderImpl(MediaStreamTrack track) {
            this.track = track;
            track.retain();
            
            
        }
        
        void fireReady() {
            ready = true;
            while (onReady != null && !onReady.isEmpty()) {
                onReady.remove(0).run();
            }
            onReady = null;
        }
        
        RTCRtpSenderImpl(Map data) {
            setRefId((String)data.get("refId"));
            ready = true;
            retain();
            MapWrap w = new MapWrap(data);
            Map trackInfo = (Map)w.get("track", null);
            
            if (trackInfo != null) {
                track = (MediaStreamTrack)registry.get((String)trackInfo.get("refId"));
                //if (track == null) {
                //    track = findTrackById((String)trackInfo.get("id"));
                //}
                if (track == null) {
                    track = new MediaStreamTrackImpl(trackInfo);
                    
                } else {
                    track.retain();
                }
                
                
                
            }
            
            
            
        }

        @Override
        public void dealloc() {
            if (!ready) {
                onReady.add(()->dealloc());
                return;
            }
            if (track != null) {
                track.release();
                track = null;
            }
            super.dealloc();
        }
        
        
        
        
        
        @Override
        public RTCDTMFSender getDtmf() {
            return dtmf;
        }

        @Override
        public MediaStreamTrack getTrack() {
            return track;
        }

        @Override
        public RTCRtpSendParameters getParameters() {
            return parameters;
        }

        @Override
        public RTCStatsReport getStats() {
            return stats;
        }

        @Override
        public RTCPromise setParameters(RTCRtpSendParameters parameters) {
            return setParameters(parameters, new RTCPromiseImpl());
            
        }
        
        private RTCPromise setParameters(RTCRtpSendParameters parameters, RTCPromiseImpl out) {
            if (!ready) {
                onReady.add(()->setParameters(parameters, out));
                return out;
            }
            execute("var sender = registry.get(${0}); sender.setParameters("+Result.fromContent((Map)parameters.toJSONStruct()).toString()+").then(function() {"
                    + "  callback.onSuccess(JSON.stringify({"
                    + "    track: cn1.wrapMediaStreamTrack(sender.track),"
                    + "    parameters: cn1.wrapRTCRtpSendParameters(sender.parameters)"
                    + "  }));"
                    + "}).catch(function(error) {"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, err)->{
                        if (err != null) {
                            out.error(err);
                            return;
                        }
                        
                        try {
                            Map data = parseJSON(res.getValue());
                            Map trackInfo = (Map)data.get("track");
                            MediaStreamTrack track = (MediaStreamTrack)registry.get((String)trackInfo.get("refId"));
                            //if (track == null) {
                            //    track = findTrackById((String)trackInfo.get("id"));
                            //}
                            if (track != null) {
                                if (track != this.track) {
                                    if (this.track != null) {
                                        this.track.release();
                                    }
                                    this.track = track;
                                    this.track.retain();
                                } 
                            } else {
                                track = new MediaStreamTrackImpl(trackInfo);
                                if (this.track != null) {
                                    this.track.release();
                                }
                                this.track = track;
                            }
                            
                            this.parameters = parameters;
                            
                            out.complete(null);
                                
                        } catch (Throwable t) {
                            Log.e(t);
                            out.error(t);
                        }
                    });
            return out;
        }

        @Override
        public RTCPromise replaceTrack(MediaStreamTrack newTrack) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    private class RTCRtpReceiverImpl extends RefCountedImpl implements RTCRtpReceiver {
        private MediaStreamTrack track;
        
        private RTCRtpContributingSources contributingSources;
        private RTCRtpParameters parameters;
        private RTCRtpSynchronizationSources rtcRtpSynchronizationSources;
        
        
        RTCRtpReceiverImpl(Map data) {
            MapWrap w = new MapWrap(data);
            setRefId((String)data.get("refId"));
            retain();
            Map trackInfo = (Map)data.get("track");
            MediaStreamTrack existingTrack = (MediaStreamTrack)registry.get((String)trackInfo.get("refId"));
            //if (existingTrack == null) existingTrack = findTrackById((String)trackInfo.get("id"));
            if (existingTrack != null) {
                track = existingTrack;
                track.retain();
            } else {
                track = newMediaStreamTrack(trackInfo);
            }
            
            // Todo  Parse contributingSources
            // Todo Parse Parameters
        }

        @Override
        public void dealloc() {
            if (track != null) {
                track.release();
                track = null;
            }
            super.dealloc();
        }
        
        
        
        @Override
        public MediaStreamTrack getTrack() {
            return track;
        }

       

        @Override
        public RTCRtpContributingSources getContributingSources() {
            return contributingSources;
        }

        @Override
        public RTCRtpParameters getParameters() {
            return parameters;
        }

        @Override
        public RTCPromise<RTCStatsReport> getStats() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RTCRtpSynchronizationSources getSynchronizationSources() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        
        
    }
    
    private class RTCRtpTransceiverImpl extends RefCountedImpl implements RTCRtpTransceiver {
        private RTCRtpTransceiverDirection currentDirection;
        
        private String mid;
        private RTCRtpReceiver receiver;
        private RTCRtpSender sender;
        private Timer poller;
        
        
        RTCRtpTransceiverImpl(Map data) {
            
            MapWrap w = new MapWrap(data);
            String refId = w.getString("refId", "");
            if (refId.isEmpty()) {
                throw new IllegalArgumentException("No refId provided for RTCRtpTransceiver");
            }
            setRefId(refId);
            retain();
            mid = (String)w.get("mid", "");
            String currDirectionStr = w.getString("currentDirection", "inactive");
            for (RTCRtpTransceiverDirection tdir : RTCRtpTransceiverDirection.values()) {
                if (tdir.matches(currDirectionStr)) {
                    currentDirection = tdir;
                    break;
                }
            }
            
            sender = new RTCRtpSenderImpl((Map)w.get("sender", null));
            receiver = new RTCRtpReceiverImpl((Map)w.get("receiver", null));
            
            poller = CN.setInterval(1000, ()->{
                web.execute("var t = registry.get(${0}); if (t) callback.onSuccess(t.currentDirection) else callback.onSuccess(null)", new Object[]{refId}, res->{
                    String dir = res.getValue();
                    if (dir == null) {
                        currentDirection = RTCRtpTransceiverDirection.Stopped;
                        
                    }
                    for (RTCRtpTransceiverDirection tdir : RTCRtpTransceiverDirection.values()) {
                        if (tdir.matches(dir)) {
                            currentDirection = tdir;
                            break;
                        }
                    }
                    if (currentDirection == null || currentDirection == RTCRtpTransceiverDirection.Stopped) {
                        if (poller != null) {
                            poller.cancel();
                            poller = null;
                        }
                    }
                });
            });
        }

        @Override
        public void dealloc() {
            sender.release();
            sender = null;
            receiver.release();
            receiver = null;
            if (poller != null) {
                poller.cancel();
                poller = null;
            }
            super.dealloc();
        }
        
        

        @Override
        public RTCRtpTransceiverDirection getCurrentDirection() {
            return currentDirection;
        }

        @Override
        public RTCRtpTransceiverDirection getDirection() {
            return currentDirection;
        }

        @Override
        public String getMid() {
            return mid;
        }

        @Override
        public RTCRtpReceiver getReceiver() {
            return receiver;
        }

        @Override
        public RTCRtpSender getSender() {
            return sender;
        }

        @Override
        public boolean isStopped() {
            return currentDirection == null || currentDirection == RTCRtpTransceiverDirection.Stopped;
        }

        @Override
        public void setCodecPreferences(RTCRtpCodecCapability... codecs) {
            
        }

        @Override
        public void stop() {
            web.execute("registry.get(${0}).stop()", new Object[]{getRefId()});
            
        }

        @Override
        public void setDirection(RTCRtpTransceiverDirection dir) {
            web.execute("registry.get(${0}).direction=${1}", new Object[]{getRefId(), dir.stringValue()});
        }
    }
    
    
    private class RTCTrackEventImpl extends EventImpl implements RTCTrackEvent, RefCounted {
        private RTCRtpReceiver receiver;
        private MediaStreams streams;
        private MediaStreamTrack track;
        private RTCRtpTransceiver transceiver;
        private RefCountedSupport ref=new RefCountedSupport(this) {
            @Override
            public void dealloc() {
                if (streams != null) {
                    streams.clear();
                    streams = null;
                }
                if (track != null) {
                    track.release();
                    track = null;
                }
                if (transceiver != null) {
                    transceiver.release();
                    transceiver = null;
                }
                if (receiver != null) {
                    receiver.release();
                    receiver = null;
                }
                
                
                super.dealloc();
            }
            
        };
        
        RTCTrackEventImpl(Map data) {
            super("track", data);
            ref.setRefId((String)data.get("refId"));
            ref.retain();
            streams = new MediaStreams();
            List<Map> streamsData = (List<Map>)data.get("streams");
            for (Map streamMap : streamsData) {
                MediaStream stream = (MediaStream)registry.get((String)streamMap.get("refId"));
                
                boolean release = false;
                if (stream == null) {
                    stream = newMediaStream(streamMap);
                    release = true;
                }
                streams.add(stream);
                if (release) {
                    stream.release();
                }
            }
            Map trackMap = (Map)data.get("track");
            track = (MediaStreamTrack)registry.get((String)trackMap.get("refId"));
            
            //if (track == null) track = findTrackById((String)trackMap.get("id"));
            if (track == null) {
                track = new MediaStreamTrackImpl(trackMap);
            } else {
                track.retain();
            }
            
            Map transceiverMap = (Map)data.get("transceiver");
            String transceiverRefId = (String)transceiverMap.get("refId");
            RTCRtpTransceiver existingTransceiver = (RTCRtpTransceiver)registry.get(transceiverRefId);
            if (existingTransceiver != null) {
                transceiver = existingTransceiver;
                transceiver.retain();
            } else {
                transceiver = new RTCRtpTransceiverImpl(transceiverMap);
            }
            
            Map receiverMap = (Map)data.get("receiver");
            String receiverRefId = (String)receiverMap.get("refId");
            RTCRtpReceiver existingReceiver = (RTCRtpReceiver)registry.get(receiverRefId);
            if (existingReceiver != null) {
                receiver = existingReceiver;
                receiver.retain();
            } else {
                receiver = new RTCRtpReceiverImpl(receiverMap);
            }
            
        }
        
        @Override
        public RTCRtpReceiver getReceiver(){ return receiver;}
        @Override
        public MediaStreams getStreams(){ return streams;}
        @Override
        public MediaStreamTrack getTrack(){ return track;}
        @Override
        public RTCRtpTransceiver getTransceiver(){ return transceiver;}

        @Override
        public void retain() {
            ref.retain();
        }

        @Override
        public void release() {
            if (ref != null) {
                ref.release();
            }
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
    
    private class RefCountedSupport extends RefCountedImpl {
        public RefCountedSupport(RefCounted proxying) {
            super(proxying);
        }
    }
    
    
    private class RefCountedImpl implements RefCounted {
        
        private RefCounted proxying=this;
        private String refId;
        private int count;
        
        public RefCountedImpl(RefCounted proxying) {
            this.proxying = proxying;
        }
        
        public RefCountedImpl() {
            
        }
        
        public void init(String refId, String javascript) {
            init(refId, javascript, null, null);
        }
        
        public void init(String refId, String javascript, String callbackJs, SuccessCallback<JSRef> callback) {
            this.refId = refId;
            String js = "var o = ("+javascript+");registry.retain(o, ${0})";

            if (callback != null && callbackJs != null) {
                web.execute(js + ";" + callbackJs, new Object[]{refId}, callback);
            
            } else {
                web.execute(js, new Object[]{refId});
            }
        }

        @Override
        public void retain() {
            if (refId == null) {
                throw new IllegalStateException("Attempting to retain object that has no refId");
            }
            if (registry.containsKey(refId)) {
                count++;
                web.execute("registry.retain(${0})", new Object[]{refId});
            } else {
                // If this object is not yet in the java registry, then we don't
                // need to call retain on the javascript side because it will have
                // already been retained once.
                count++;
                registry.put(refId, proxying);
            }
            
        }

        @Override
        public void release() {
            if (refId == null) {
                throw new IllegalStateException("Attempting to release object that has no refId");
            }
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
    
    private MediaStream findStreamById(String id) {
        for (RefCounted ref : registry.values()) {
            if (ref instanceof MediaStream) {
                MediaStream stream = (MediaStream)ref;
                if (id.equals(stream.getId())) {
                    return stream;
                }
            }
        }
        return null;
    }
    
    private class MediaStreamImpl extends RefCountedImpl implements MediaStream, AutoCloseable {
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

        @Override
        public void close() throws Exception {
            if (tracks != null) {
                for (MediaStreamTrack track : tracks) {
                    track.stop();
                }
                tracks.clear();
                tracks = null;
            }
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
                        (boolean)data.get("readonly"),
                        (String)data.get("readyState"),
                        (boolean)data.get("remote"),
                        newMediaTrackSettings((String)data.get("kind"), (Map)data.get("settings"))
                                
                );
    }
    
    private MediaStream newMediaStream(Map data) {
        MapWrap w = new MapWrap(data);
        String refId = w.getString("refId", "");
        if (refId.isEmpty()) {
            throw new IllegalArgumentException("No refId supplied");
        }
        String id = w.getString("id", "");
        boolean active = w.getBoolean("active", true);
        boolean ended = w.getBoolean("ended", false);
        List<Map> tracks = (List<Map>)w.get("tracks");
        return newMediaStream(refId, id, active, ended, tracks);
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
        private boolean readonly;
        private ReadyState readyState;
        private boolean remote;
        private MediaTrackSettings settings;
        private MediaTrackConstraints constraints;
        private EventSupport es = new EventSupport(this);
        
        private MediaStreamTrackImpl(Map data) {
            MapWrap w = new MapWrap(data);
            String refId = w.getString("refId", "");
            if (refId.isEmpty()) {
                throw new IllegalArgumentException("No refId supplied for MediaStreamTrack");
            }
            String contentHint = w.getString("contentHint", "");
            boolean enabled = w.getBoolean("enabled", true);
            String id = w.getString("id", "");
            String kind = w.getString("kind", "");
            if (kind.isEmpty()) {
                throw new IllegalArgumentException("No kind supplied for MediaStreamTrack");
            }
            String label = w.getString("label", "");
            boolean muted = w.getBoolean("muted", false);
            boolean readonly = w.getBoolean("readonly", false);
            String readyState = w.getString("readyState", "");
            if (readyState.isEmpty()) {
                throw new IllegalArgumentException("No readyState supplied for MediaStreamTrack");
            }
            boolean remote = w.getBoolean("remote", false);
            MediaTrackSettings settings = newMediaTrackSettings(kind, (Map)w.get("settings", null));
            
            //MediaTrackConstraints constraints = newMediaTrackConstraints(kind, (Map)w.get("constraints", null));
            init(refId, id, contentHint, enabled, kind, label, muted, readonly, readyState, remote, settings);
            //this.constraints = constraints;
            
        }
        
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
            init(refId, id, contentHint, enabled, kind, label, muted, readonly, readyState, remote, settings);
        }
        
        private void init(
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
            this.readonly = readonly;
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
            return readonly;
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
            web.execute("var track = registry.get(${0});track.applyConstraints("+Result.fromContent((Map)constraints.toJSONStruct()).toString()+")"
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
                + "    ['contentHint', 'id', 'enabled', 'kind', 'label', 'muted', 'readyState', 'readonly', 'remote'].forEach(function(val, index, array){"
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
                        
                        ((RTCPromiseImpl)out).error(new AsyncResource.AsyncExecutionException(err));
                    }
                }
        );
        
        return out;
                
    }
    
    public Component getVideoComponent() {
        return web;
    }
    
    private class RTCVideoElementImpl extends RTCMediaElementImpl implements RTCVideoElement {
        
        private int videoWidth, videoHeight;
        
        public RTCVideoElementImpl() {
            super("video");
            addEventListener("resize", evt->{
                EventImpl e = (EventImpl)evt;
                this.videoWidth = ((Number)e.getData().get("videoWidth")).intValue();
                this.videoHeight = ((Number)e.getData().get("videoHeight")).intValue();
            });
        }
        
        
        public int getVideoWidth() {
            return videoWidth;
        }
        
        public int getVideoHeight() {
            return videoHeight;
        }

       
    }
    
    private class RTCAudioElementImpl extends RTCMediaElementImpl implements RTCAudioElement {
        public RTCAudioElementImpl() {
            super("audio");
        }
    }
    
    private class RTCMediaElementImpl extends RefCountedImpl implements RTCMediaElement {

        
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
        private String id;
        
        public RTCMediaElementImpl(String type) {
            init(Util.getUUID(), "(function(){var vid = document.createElement('"+type+"'); "
                    //+ "vid.style.position='fixed'; "
                    //+ "vid.style.top='0'; "
                    //+ "vid.style.bottom='0', "
                    //+ "vid.style.left='0'; "
                    //+ "vid.style.right='0';"
                    //+ "vid.style.width='100%';"
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

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(String id) {
            if (!Objects.equals(id, this.id)) {
                this.id = id;
                web.execute("registry.get(${0}).id=${1};", new Object[]{getRefId(), id});
            }
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
        

        @Override
        public void fill() {
            applyStyle("position:fixed;width:100%;height:100%;top:0;left:0;right:0;bottom:0");
        }

        @Override
        public void applyStyle(RTCStyle style) {
            StringBuilder js = new StringBuilder();
            js.append("var el = registry.get(${0});");
            int index=1;
            List params = new ArrayList();
            params.add(getRefId());
            for (CSSProperty prop : style) {
                js.append("el.style.setProperty('"+prop.getProperty()+"', ${"+index+"});");
                params.add(prop.getValue());
                index++;
            }
            web.execute(js.toString(), params.toArray(new Object[params.size()]));
        }

        @Override
        public void applyStyle(String css) {
            applyStyle(RTCStyle.createStyle(css));
        }

        @Override
        public void setMuted(boolean muted) {
            if (this.muted != muted) {
                this.muted = muted;
                web.execute("registry.get(${0}).muted="+muted, new Object[]{getRefId()});
            }
        }
        
    }
    
    public RTCVideoElement createVideo() {
        RTCVideoElementImpl out = new RTCVideoElementImpl();
        return out;
    }
    
    public RTCAudioElement createAudio() {
        return new RTCAudioElementImpl();
    }
    
    public void append(RTCElement el) {
        if (el instanceof RefCounted) {
            RefCountedImpl ref = (RefCountedImpl)el;
            web.execute("document.body.appendChild(registry.get(${0}))", new Object[]{ref.getRefId()});
        }
    }
    
    
    private class RTCPeerConnectionImpl extends RefCountedImpl implements RTCPeerConnection {

        private boolean canTrickleIceCandidates;
        private RTCPeerConnectionState connectionState;
        private RTCSessionDescription currentLocalDescription, 
                currentRemoteDescription, 
                localDescription, 
                remoteDescription, 
                pendingLocalDescription, 
                pendingRemoteDescription;
        
       
        private RTCIceConnectionState iceConnectionState;
        private RTCIceGatheringState iceGatheringState;
        private RTCIdentityAssertion peerIdentity;
        private RTCSctpTransport sctp;
        private RTCSignalingState signalingState;
        
        private RTCRtpReceivers receivers;
        private RTCRtpSenders senders;
        private EventSupport es = new EventSupport(this);
        
        private boolean isClosed() {
            return connectionState == RTCPeerConnectionState.Closed;
        }
        
        @Override
        public boolean canTrickleIceCandidates() {
            if (isClosed()) return false;
            return canTrickleIceCandidates;
        }

        @Override
        public RTCPeerConnectionState getConnectionState() {
            return connectionState;
        }

        @Override
        public RTCSessionDescription getCurrentLocalDescription() {
            return currentLocalDescription;
        }

        @Override
        public RTCSessionDescription getCurrentRemoteDescription() {
            return currentRemoteDescription;
        }

        @Override
        public RTCIceConnectionState getIceConnectionState() {
            return iceConnectionState;
        }

        @Override
        public RTCIceGatheringState getIceGatheringState() {
            return iceGatheringState;
        }

        @Override
        public RTCSessionDescription getLocalDescription() {
            return localDescription;
        }

        @Override
        public RTCIdentityAssertion getPeerIdentity() {
            return peerIdentity;
        }

        @Override
        public RTCSessionDescription getPendingLocalDescription() {
            return pendingLocalDescription;
        }

        @Override
        public RTCSessionDescription getPendingRemoteDescription() {
            return pendingRemoteDescription;
        }

        @Override
        public RTCSessionDescription getRemoteDescription() {
            return remoteDescription;
        }

        @Override
        public RTCSctpTransport getSctp() {
            return sctp;
        }

        @Override
        public RTCSignalingState getSignalingState() {
            return signalingState;
        }

        @Override
        public RTCPromise addIceCandidate(RTCIceCandidate candidate) {
            RTCPromiseImpl out = (RTCPromiseImpl)newPromise(Object.class);
            if (isClosed()) {
                out.error(new IllegalStateException("Connection is closed"));
                return out;
            }
            execute("registry.get(${0}).addIceCandidate("+candidate.toJSON()+").then(function(){"
                    + "  callback.onSuccess(null);"
                    + "}).catch(function(error){"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (jsref, error)->{
                        if (error != null) {
                            out.error(error);
                        } else {
                            out.complete(null);
                        }
                    });
            return out;
        }

        @Override
        public RTCRtpSender addTrack(MediaStreamTrack track, MediaStream... stream) {
            RTCRtpSenderImpl sender = new RTCRtpSenderImpl(track);
            Object[] params = new Object[stream.length+2];
            StringBuilder addTrackParams = new StringBuilder();
            params[0] = getRefId();
            params[1] = ((MediaStreamTrackImpl)track).getRefId();
            addTrackParams.append("(registry.get(${1})");
            for (int i=2; i<params.length; i++) {
                params[i] = ((MediaStreamImpl)stream[i-2]).getRefId();
                addTrackParams.append(", registry.get(${"+i+"})");
            }
            addTrackParams.append(")");
            
            
            String js = "var conn = registry.get(${0});var sender = conn.addTrack"+addTrackParams.toString()+"; callback.onSuccess(JSON.stringify(cn1.wrapRTCRtpSender(sender)));";
            
            execute(js, params, (res,error) -> {
                try {
                    if (error != null) {
                        
                        Log.e(error);
                        return;
                    }
                    
                    Map data = parseJSON(res.getValue());
                    sender.setRefId((String)data.get("refId"));
                    
                    if (senders == null) {
                        senders = new RTCRtpSenders();
                        senders.add(sender);
                    }
                    sender.fireReady();
                    
                } catch (IOException ex) {
                    Log.e(ex);
                }
            });
            return sender;
        }

        @Override
        public void close() {
            
            close(true);
                    
        }
        
        private void close(boolean release) {
            
            if (signalingState == RTCSignalingState.Closed) {
                
                return;
            }
            
            web.execute("registry.get(${0}).close()", new Object[]{getRefId()});
            if (receivers != null) {
                receivers.clear();
                receivers = null;
            }
            if (senders != null) {
                senders.clear();
            }
            connectionState = RTCPeerConnectionState.Closed;
            signalingState = RTCSignalingState.Closed;
            currentLocalDescription = null;
            currentRemoteDescription = null;
            if (release) {
                release();
            }
        }

        @Override
        public void dealloc() {
            close(false);
            super.dealloc();
        }
        
        

        @Override
        public RTCPromise<RTCSessionDescription> createAnswer(RTCAnswerOptions options) {
            RTCPromiseImpl<RTCSessionDescription> out = new RTCPromiseImpl<RTCSessionDescription>();
            String paramString = "";
            if (options != null) {
                paramString = Result.fromContent((Map)options.toJSONStruct()).toString();
            }
            execute("var conn=registry.get(${0});conn.createAnswer("+paramString+").then(function(desc) {"
                    + "  callback.onSuccess(JSON.stringify(cn1.wrapRTCSessionDescription(desc)));"
                    + "}).catch(function(error){"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            Log.e(error);
                            out.error(error);
                        } else {
                            try {
                                Map data = parseJSON(res.getValue());
                                RTCSessionDescription desc = new RTCSessionDescriptionImpl(data);
                                out.complete(desc);
                            } catch (IOException ex) {
                                Log.e(ex);
                                out.error(ex);
                            }
                            
                        }
                    });
            return out;
        }

        @Override
        public RTCDataChannel createDataChannel(String label, RTCDataChannelInit options) {
            return new RTCDataChannelImpl(this, label,  options);
        }

        @Override
        public RTCPromise<RTCSessionDescription> createOffer(RTCOfferOptions options) {
            RTCPromiseImpl<RTCSessionDescription> out = new RTCPromiseImpl<RTCSessionDescription>();
            String paramString = "";
            if (options != null) {
                paramString = Result.fromContent((Map)options.toJSONStruct()).toString();
            }
            execute("var conn=registry.get(${0});conn.createOffer("+paramString+").then(function(desc) {"
                    + "  console.log('offer created', desc);"
                    + "  callback.onSuccess(JSON.stringify(cn1.wrapRTCSessionDescription(desc)));"
                    + "}).catch(function(error){"
                    + "  console.log('error creating offer', error);"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            Log.e(error);
                            out.error(error);
                        } else {
                            try {
                                Map data = parseJSON(res.getValue());
                                RTCSessionDescription desc = new RTCSessionDescriptionImpl(data);
                                out.complete(desc);
                            } catch (IOException ex) {
                                Log.e(ex);
                                out.error(ex);
                            }
                            
                        }
                    });
            return out;
            
        }

        @Override
        public RTCPromise<RTCSessionDescription> createAnswer() {
            return createAnswer(null);
        }
        
       

       
        @Override
        public RTCRtpReceivers getReceivers() {
            return receivers;
        }

        @Override
        public RTCRtpSenders getSenders() {
            return senders;
        }

        @Override
        public RTCPromise<RTCStatsReport> getStats(MediaStreamTrack selector) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public MediaStream getStreamById(String id) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RTCRtpTransceivers getTransceivers() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void removeTrack(RTCRtpSender sender) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void restartIce() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    

        @Override
        public void setIdentityProvider(String domainName) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setIdentityProvider(String domainName, String protocol) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setIdentityProvider(String domainName, String protocol, String username) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RTCPromise setLocalDescription(RTCSessionDescription sessionDescription) {
            RTCPromiseImpl out = new RTCPromiseImpl();
            execute("var conn = registry.get(${0}); conn.setLocalDescription("+sessionDescription.toJSON()+").then(function(res) {"
                    + "  callback.onSuccess(null);"
                    + "}).catch(function(error) {"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            out.error(error);
                        } else {
                            localDescription = sessionDescription;
                            out.complete(null);
                        }
                    });
            return out;
        }

        @Override
        public RTCPromise setRemoteDescription(RTCSessionDescription sessionDescription) {
            RTCPromiseImpl out = new RTCPromiseImpl();
            execute("var conn = registry.get(${0}); conn.setRemoteDescription("+sessionDescription.toJSON()+").then(function(res) {"
                    + "  callback.onSuccess(null);"
                    + "}).catch(function(error) {"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            out.error(error);
                        } else {
                            remoteDescription = sessionDescription;
                            out.complete(null);
                        }
                    });
            return out;
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

        public RTCPeerConnectionImpl(RTCConfiguration configuration) {
            
            init(Util.getUUID(), "new RTCPeerConnection("+Result.fromContent((Map)configuration.toJSONStruct()).toString()+")");
            
            retain();
            addEventListener("connectionstatechange", evt-> {
                EventImpl e = (EventImpl)evt;
                String stateStr = (String)e.getData().get("connectionState");
                for (RTCPeerConnectionState state : RTCPeerConnectionState.values()) {
                    if (state.matches(stateStr)) {
                        connectionState = state;
                        break;
                    }
                }
            });
            addEventListener("datachannel", evt->{
               
                
            });
            addEventListener("icecandidate", evt->{
                
            });
            addEventListener("icecandidateerror", evt->{
                RTCPeerConnectionIceErrorEventImpl e = (RTCPeerConnectionIceErrorEventImpl)evt;
                if (e.getErrorCode() >= 700 && e.errorCode < 799) {
                    
                }
            });
            addEventListener("iceconnectionstatechange", evt->{
               EventImpl e = (EventImpl)evt;
               String newState = (String)e.getData().get("iceConnectionState");
               for (RTCIceConnectionState state : RTCIceConnectionState.values()) {
                   if (state.matches(newState)) {
                       iceConnectionState = state;
                       break;
                              
                   }
               }
            });
            addEventListener("icegatheringstatechange", evt->{
                EventImpl e = (EventImpl)evt;
                String newState = (String)e.getData().get("iceGatheringState");
                for (RTCIceGatheringState state : RTCIceGatheringState.values()) {
                    if (state.matches(newState)) {
                        iceGatheringState = state;
                        break;
                    }
                }
            });
            
            addEventListener("negotiationneeded", evt->{
                
            });
            addEventListener("signalingstatechange", evt->{
                EventImpl e = (EventImpl)evt;
                String newState = (String)e.getData().get("signalingState");
                for (RTCSignalingState state : RTCSignalingState.values()) {
                    if (state.matches(newState)) {
                        signalingState = state;
                        break;
                    }
                }
                
                
            });
            
            addEventListener("track", evt->{
                
            });
            
            
            
        }
        
        
        
    }
    
    private class RTCDataChannelImpl extends RefCountedImpl implements RTCDataChannel {
        private String binaryType;
        private int bufferedAmount;
        private int bufferedAmountLowThreshold;
        private int id;
        private String label;
        private Integer maxPacketLifeTime;
        private Integer maxRetransmits;
        private boolean negotiated;
        private boolean ordered;
        private String protocol;
        private RTCDataChannelState readyState;
        private EventSupport es = new EventSupport(this);
        
        private Timer bufferedAmountPollTimer;
        
        private void startBufferedAmountPolling() {
            if (bufferedAmountPollTimer != null) {
                bufferedAmountPollTimer = CN.setInterval(300, ()->{
                    web.execute("callback.onSuccess(registry.get(${0}).bufferedAmount)", new Object[]{getRefId()}, res->{
                        bufferedAmount = res.getInt();
                    });
                });
            }
        }
        
        private void stopBufferedAmountPolling() {
            if (bufferedAmountPollTimer != null) {
                bufferedAmountPollTimer.cancel();
                bufferedAmountPollTimer = null;
            }
        }
        
        @Override
        public String getBinaryType() {
            return binaryType;
        }

        @Override
        public int getBufferedAmount() {
            return bufferedAmount;
        }

        @Override
        public int getBufferedAmountLowThreshold() {
            return bufferedAmountLowThreshold;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public Integer getMaxPacketLifeTime() {
            return maxPacketLifeTime;
        }

        @Override
        public Integer getMaxRetransmits() {
            return maxRetransmits;
        }

        @Override
        public boolean isNegotiated() {
            return negotiated;
        }

        @Override
        public String getProtocol() {
            return protocol;
        }

        @Override
        public RTCDataChannelState getReadyState() {
            return readyState;
        }

        @Override
        public void close() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void send(byte[] bytes) {
            
        }

        @Override
        public void send(Blob blob) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void send(String string) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        RTCDataChannelImpl(RTCPeerConnectionImpl conn, String label, RTCDataChannelInit options) {
            String jsString = "registry.get('"+conn.getRefId()+"').createDataChannel("+Result.fromContent((Map)options.toJSONStruct()).toString()+")";
            negotiated = options.isNegotiated();
            ordered = options.isOrdered();
            id = options.getId();
            maxPacketLifeTime = options.getMaxPacketLifeTime();
            maxRetransmits = options.getMaxRetransmits();
            protocol = options.getProtocol();
            this.label = label;
            
            init(Util.getUUID(), jsString, "callback.onSuccess(JSON.stringify({"
                    + "binaryType: o.binaryType,"
                    + "bufferedAmount : o.bufferedAmount,"
                    + "bufferedAmountLowThreshold : o.bufferedAmountLowThreshold,"
                    + "id : o.id,"
                    + "label : o.label,"
                    + "maxPacketLifeTime : o.maxPacketLifeTime,"
                    + "maxRetransmits : o.maxRetransmits,"
                    + "negotiated : o.negotiated,"
                    + "ordered : o.ordered,"
                    + "protocol : o.protocol"
                    + "}))", res->{
                try {
                    Map parsedData = parseJSON(res.getValue());
                    init(parsedData);
                } catch (IOException ex) {
                    Log.e(ex);
                }
            });
            retain();
            
            
        }
        
        RTCDataChannelImpl(Map data) {
            init(data);
        }
        
        private void init(Map data) {
            if (getRefId() == null) {
                setRefId((String)data.get("refId"));
            }
            retain();
            MapWrap m = new MapWrap(data);
            binaryType = (String)m.get("binaryType", "blob");
            bufferedAmount = m.getInt("bufferedAmount", 0);
            bufferedAmountLowThreshold = m.getInt("bufferedAmountLowThreshold", 0);
            id = m.getInt("id", 0);
            label = (String)m.get("label", "");
            maxPacketLifeTime = m.getInt("maxPacketLifeTime", null);
            maxRetransmits = m.getInt("maxRetransmits", null);
            negotiated = (Boolean)m.get("negotiated", false);
            ordered = (Boolean)m.get("ordered", false);
            protocol = (String)m.get("protocol", "");
            String readyStateStr = (String)m.get("readyState", "connecting");
            for (RTCDataChannelState state : RTCDataChannelState.values()) {
                if (state.matches(readyStateStr)) {
                    readyState = state;
                    break;
                }
            }
            
            addEventListener("bufferedamountlow", evt->{
                
            });
            
            addEventListener("close", evt->{
                readyState = RTCDataChannelState.Closed;
                CN.setTimeout(500, ()->{
                    release();
                });
            });
            
            addEventListener("error", evt->{
                
            });
            
            addEventListener("open", evt->{
                readyState = RTCDataChannelState.Open;
                
            });
            
        }
        
        
        
    }
    
    public RTCPeerConnection newRTCPeerConnection(RTCConfiguration configuration) {
        return new RTCPeerConnectionImpl(configuration);
    }
    
    
    private class RTCIceCandidateImpl implements RTCIceCandidate {
        private String candidate, component, foundation, ip;
        private int port;
        private Integer relatedPort;
        private int priority;
        private String protocol, relatedAddress;
        private String sdpMid;
        private Integer sdpMLineIndex;
        private String tcpType;
        private String type;
        private String usernameFragment;
        
        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            if (candidate != null) out.put("candidate", candidate);
            if (component != null) out.put("component", component);
            if (foundation != null) out.put("foundation", foundation);
            if (ip != null) out.put("ip", ip);
            out.put("port", port);
            if (relatedPort != null) out.put("relatedPort", relatedPort);
            out.put("priority", priority);
            if (protocol != null) out.put("protocol", protocol);
            if (relatedAddress != null) out.put("relatedAddress", relatedAddress);
            if (sdpMid != null) out.put("sdpMid", sdpMid);
            if (sdpMLineIndex != null) out.put("sdpMLineIndex", sdpMLineIndex);
            if (tcpType != null) out.put("tcpType", tcpType);
            if (type != null) out.put("type", type);
            if (usernameFragment != null) out.put("usernameFragment", usernameFragment);
            return out;
        }

        RTCIceCandidateImpl(Map data) {
            MapWrap m = new MapWrap(data);
            candidate = (String)m.get("candidate", "");
            component = (String)m.get("component", "");
            foundation = (String)m.get("foundation", "");
            ip = (String)m.get("ip", "");
            port = m.getInt("port", 0);
            relatedPort = m.getInt("relatedPort", null);
            priority = m.getInt("priority", 0);
            protocol = (String)m.get("protocol", "");
            relatedAddress = (String)m.get("relatedAddress", null);
            sdpMid = (String)m.get("sdpMid", null);
            sdpMLineIndex = m.getInt("sdpMLineIndex", null);
            tcpType = (String)m.get("tcpType", null);
            type = (String)m.get("type", null);
            usernameFragment = (String)m.get("usernameFragment", null);
        }
        
        
        
        
        @Override
        public String getCandidate() {
            return candidate;
        }

        @Override
        public String getComponent() {
            return component;
        }

        @Override
        public String getFoundation() {
            return foundation;
        }

        @Override
        public String getIp() {
            return ip;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public long getPriority() {
            return priority;
        }

        @Override
        public String getRelatedAddress() {
            return relatedAddress;
        }

        @Override
        public int getRelatedPort() {
            return relatedPort;
        }

        @Override
        public String getSdpMid() {
            return sdpMid;
        }

        @Override
        public Integer getSdpMLineIndex() {
            return sdpMLineIndex;
        }

        @Override
        public String getTcpType() {
            return tcpType;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getUsernameFragment() {
            return usernameFragment;
        }

        @Override
        public String toJSON() {
            return Result.fromContent((Map)toJSONStruct()).toString();
        }

        
        
    }
    
    
    private class RTCSessionDescriptionImpl implements RTCSessionDescription {
        private RTCSdpType type;
        private String sdp;
        
        RTCSessionDescriptionImpl(Map data) {
            if (data.containsKey("type")) {
                for (RTCSdpType t : RTCSdpType.values()) {
                    if (t.matches((String)data.get("type"))) {
                        type = t;
                        break;
                    }
                    
                }   
            }
            
            if (data.containsKey("sdp")) {
                sdp = (String)data.get("sdp");
            }
        }
        
        
        @Override
        public RTCSdpType getType() {
            return type;
        }

        @Override
        public String getSdp() {
            return sdp;
        }

        @Override
        public String toJSON() {
            return Result.fromContent((Map)toJSONStruct()).toString();
        }

        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            if (type != null) {
                out.put("type", type.stringValue());
            }
            if (sdp != null) {
                out.put("sdp", sdp);
            }
            return out;
        }
        
    }
    
    
    public static RTCPromise all(RTCPromise... promises) {
        AsyncResource[] asyncs = new AsyncResource[promises.length];
        for (int i=0; i<promises.length; i++) {
            asyncs[i] = (AsyncResource)promises[i];
        }
        RTCPromiseImpl out = new RTCPromiseImpl();
        AsyncResource.all(asyncs).onResult((res, err)->{
            if (err != null) {
                out.error(err);
            } else {
                out.complete(null);
            }
        });
        return out;
    }
    
}
