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
import com.codename1.system.NativeLookup;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.BrowserComponent.JSRef;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.events.ActionListener;

import com.codename1.util.AsyncResource;
import com.codename1.util.AsyncResult;
import com.codename1.util.Callback;
import com.codename1.util.StringUtil;
import com.codename1.util.SuccessCallback;
import com.codename1.webrtc.MediaStream.ReadyState;
import com.codename1.webrtc.RTCPeerConnection.RTCDataChannelInit;
import com.codename1.webrtc.RTCStyle.CSSProperty;
import com.codename1.webrtc.compat.cordova.EmbeddedCordovaApplication;
import com.codename1.webrtc.compat.cordova.IOSRTCPlugin;
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
 * The main access point for WebRTC.  An RTC instance wraps a BrowserComponent, which is used to 
 * host all WebRTC sessions. Use the {@link #getVideoComponent() } to access the visual component
 * associated with the RTC session.  This component can be placed into a UI hierarchy.
 * 
 * == Creating RTC Object
 * 
 * Use {@link #createRTC() } to create a new RTC object.  All videos, peer connections, data channels, etc.. will be created by
 * and bound to the resulting RTC object.
 * 
 * [source,java]
 * ----
 * RTC.createRTC().then(rtc->{
 *     //  The RTC object is created.  You can now start working with it.
 * });
 * ----
 * 
 * == Basic Example
 * 
 * The following example is a minimal example that accesses the device camera and microphone
 * and streams the output to the screen.  It highlights the use of {@link #getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }
 * to access the device's camera and microphone.  This is based on the https://webrtc.github.io/samples/src/content/getusermedia/gum/[Basic getUserMedia demo]
 * on the https://webrtc.github.io/[WebRTC web site].
 * 
 * [source,java]
 * ----
 * RTC.createRTC().ready(rtc->{ <1>
        MediaStreamConstraints constraints = new MediaStreamConstraints() <2>
                .audio()
                .echoCancellation(true)
                .noiseSuppression(true)
                .video(true)
                .stream();

        rtc.getUserMedia(constraints).then(stream->{ <3>
            RTCVideoElement video = rtc.createVideo(); <4>
            video.setAutoplay(true);
            video.setSrcObject(stream);
            video.applyStyle("position:fixed;width:100%;height:100%;"); <5>
            rtc.append(video); <6>
        }).onCatch(error-> {
            Log.e(error);
        });
        hi.add(BorderLayout.CENTER, rtc.getVideoComponent()); <7>
        hi.revalidate();
    });
 * ----
 * <1> Create the RTC object using {@link #createRTC() }.  This is an asynchronous call which returns an {@link AsyncResource<RTC>}.
 * <2> Set up requirements for accessing user media using {@link MediaStreamConstraints}
 * <3> Call {@link #getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }, to access camera and mic.  This is an asynchronous call.
 * <4> Create a new Video element using {@link #createVideo() }.  A single RTC instance can include multiple video elements.
 * <5> Set up the video element so that it fills the RTC canvas.   {@link RTCElement#applyStyle(java.lang.String) } allows you to 
 * set arbitrary CSS styles to the video elements.  Positioning is relative to the RTC component itself, so in this case with "position:fixed"
 * it will allow us to fill the entire RTC component with the video element.
 * <6> Append the video to the RTC canvas.  Until we do this, the video element will not appear in the RTC component.
 * <7> Add the RTC's visual component to the Codename One UI hierarchy.
 * 
 * @author shannah
 */
public class RTC implements AutoCloseable {
    private BrowserComponent web;
    private Map<String,RefCounted> registry = new HashMap<>();
    private final AsyncResource<RTC> async = new AsyncResource<RTC>();
    private WebRTCNative webrtcNative;
    private EmbeddedCordovaApplication cordovaApp;
    private static boolean audioPermission, videoPermission;
    private static Map<String,Runnable> callbacks = new HashMap<String,Runnable>();
    private RTCRtpCapabilities senderAudioCapabilities, senderVideoCapabilities, receiverAudioCapabilities, receiverVideoCapabilities;
    
    /**
     * Internal callback function used by Native interface.  Do not use.
     * @param callbackId The callback ID
     * @param audio True if audio permission is granted.
     * @param video True if video permission is granted.
     * @deprecated Internal use only
     */
    public static void permissionCallback(final String callbackId, boolean audio, boolean video) {
        if (audio) audioPermission = true;
        if (video) videoPermission = true;
        CN.callSerially(()->{
            if (callbacks.containsKey(callbackId)) {
                Runnable callback = callbacks.get(callbackId);
                callbacks.remove(callbackId);
                callback.run();
            }
        });
    }
    
    /**
     * Checks/requests native permission for audio/video access.  This will call {@link WebRTCNative#requestPermissions(java.lang.String, boolean, boolean) }
     * of the native interface, if it is supported.  The native interface will request permissions and call the provided callback when complete.
     * 
     * This is called inside {@link #getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.  Currently Android
     * is the only platform that requires this.  For platforms that don't require this, it will automatically grant permissions
     * and call the callback.
     * 
     * The current state of the permissions are stored in {@link #audioPermission} and {@link #videoPermission} respectively.  The native
     * interface will call {@link #permissionCallback(java.lang.String, boolean, boolean) } to trigger the callback.
     * 
     * @param audio True if requesting audio permission.
     * @param video True if requesting video permission
     * @param callback Run on the EDT after permission request is complete.
     */
    private void checkPermission(boolean audio, boolean video, Runnable callback) {
        boolean requiresCheck = audio && !audioPermission || video && !videoPermission;
        String id = Util.getUUID();
        callbacks.put(id, callback);
        if (webrtcNative != null && webrtcNative.isSupported() && requiresCheck) {
            webrtcNative.requestPermissions(id, audio, video);
        } else {
            permissionCallback(id, true, true);
        }
    }
    
    
    private RTC(String htmlBody, String css) {
        init(htmlBody, css);
    }
    
    /**
     * Wrapper around {@link BrowserComponent#execute(java.lang.String, com.codename1.util.SuccessCallback) } that uses 
     * {@link AsyncResult} for callback rather than {@link Callback} or {@link SuccessCallback}.
     * @param javascript The javascript to run.
     * @param params The parameters for insertion.
     * @param onResult Callback.
     */
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
    
    /**
     * Wrapper around {@link BrowserComponent#execute(java.lang.String, com.codename1.util.SuccessCallback) } that uses 
     * {@link AsyncResult} for callback rather than {@link Callback} or {@link SuccessCallback}.
     * @param javascript The javascript to run.
     * @param onResult Callback.
     */
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
    
    /**
     * Creates a new RTC object.
     * @return Promise that resolves to the resulting {@link RTC} object.
     */
    public static AsyncResource<RTC> createRTC() {
        return createRTC("", "");
    }
    
    /**
     * Creates a new RTC object.  Injects HTML content into the webpage body and CSS
     * into the webpage head that is used to host the RTC.
     * @param htmlBody HTML string to inject into the `<body>` of the page.
     * @param css CSS styles to inject into a `<style>` tag in the `<head>` of the page.
     * @return Promise that resolves to the resulting {@link RTC} object.
     */
    public static AsyncResource<RTC> createRTC(String htmlBody, String css) {
        return new RTC(htmlBody, css).async;
    }
    
    private void init(String htmlBody, String css) {
        try {
            webrtcNative = (WebRTCNative)NativeLookup.create(WebRTCNative.class);
            
        } catch (Throwable t) {
            Log.e(t);
        }
        String androidGrantPermissionsAtOrigin = CN.getProperty("android.WebView.grantPermissionsFrom", "");
        if (androidGrantPermissionsAtOrigin.indexOf("http://localhost/") == -1) {
            androidGrantPermissionsAtOrigin += " http://localhost/";
            androidGrantPermissionsAtOrigin = androidGrantPermissionsAtOrigin.trim();
            CN.setProperty("android.WebView.grantPermissionsFrom", androidGrantPermissionsAtOrigin);
        }
        web = new BrowserComponent();
        
        
        ActionListener bootstrapCallback = e->{
            
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
                        } else if (eventType.equals("message")) {
                            evt = new MessageEventImpl(data);
                        } else if (eventType.equals("icecandidate")) {
                            evt = new RTCPeerConnectionIceEventImpl(eventType, data);
                        } else if (eventType.equals("icecandidateerror")) {
                            evt = new RTCPeerConnectionIceErrorEventImpl(data);
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
            web.execute("callback.onSuccess(JSON.stringify({senderVideo: RTCRtpSender.getCapabilities('video'), "
                    + "senderAudio: RTCRtpSender.getCapabilities('audio'),"
                    + "receiverVideo: RTCRtpReceiver.getCapabilities('video'),"
                    + "receiverAudio: RTCRtpReceiver.getCapabilities('audio')"
                    + "}));", res->{
                try {
                    java.util.Map data = (java.util.Map)parseJSON(res.getValue());
                    senderAudioCapabilities = new RTCRtpCapabilities((java.util.Map)data.get("senderAudio"));
                    senderVideoCapabilities = new RTCRtpCapabilities((java.util.Map)data.get("senderVideo"));
                    receiverAudioCapabilities = new RTCRtpCapabilities((java.util.Map)data.get("receiverAudio"));
                    receiverVideoCapabilities = new RTCRtpCapabilities((java.util.Map)data.get("receiverVideo"));
                } catch (Throwable t) {
                    Log.e(t);
                }
                
                
                
                async.complete(RTC.this);
            });
            
        };
        if ("ios".equals(CN.getPlatformName()) && !CN.isSimulator()) {
            // This is iOS
            cordovaApp = new EmbeddedCordovaApplication(web) {
                @Override
                protected void onReady() {
                    System.out.println("In cordovaApp.onReady()");
                    bootstrapCallback.actionPerformed(null);
                }
                
            };
            IOSRTCPlugin plugin = new IOSRTCPlugin(web);
            
            cordovaApp.addPlugin("iosrtcPlugin", plugin);
            plugin.pluginInitialize();
            
        } else {
            web.addWebEventListener(BrowserComponent.onLoad, bootstrapCallback);
        } 
        try {
            String path = "/com_codename1_rtc_RTCBootstrap.html";
            if (cordovaApp != null) {
                path = "/com_codename1_rtc_RTCBootstrap_ios.html";
            }
            String pageContent = Util.readToString(CN.getResourceAsStream(path));
            pageContent = StringUtil.replaceFirst(pageContent, "</body>", htmlBody+"</body>");
            pageContent = StringUtil.replaceFirst(pageContent, "</head>", "<style type='text/css'>\n"+css+"\n</style></head>");
            web.setPage(pageContent, "http://localhost/");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }

    /**
     * Closes the RTC session.  When you are finished with the RTC session, you should call this method to prevent
     * memory leaks, and free up resources such as the camera and microphone.
     * @throws Exception 
     */
    @Override
    public void close() throws Exception {
        for (RefCounted ref : registry.values()) {
            if (ref instanceof AutoCloseable) {
                ((AutoCloseable)ref).close();
            }
        }
        if (cordovaApp != null) {
            cordovaApp.dispose();
        }
    }
    
    /**
     * A utility class that provides event listener and dispatch functionality to 
     * help other classes support the {@link EventTarget} interface.  This is used
     * similar to the way PropertyChangeSupport is used, via composition.
     */
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
    
    /**
     * Implementation of the {@link Event} interface.
     */
    private class EventImpl implements Event {
        private String type;
        private boolean canceled;
        private Map data;
        
        /**
         * Creates a new event of the given type.
         * @param type 
         */
        public EventImpl(String type) {
            this.type = type;
        }
        
        /**
         * Creates a new event of the given type, and associated data.
         * @param type The type of the event
         * @param data Data stored in the event.
         */
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
    
    /**
     * Implementation of {@link RTCDataChannelEvent}.
     */
    private class RTCDataChannelEventImpl extends EventImpl implements RTCDataChannelEvent, RefCounted {
        private RTCDataChannel channel;
        private RefCountedSupport rcs = new RefCountedSupport(this) {
            @Override
            public void dealloc() {
                if (channel != null) {
                    channel.release();
                    channel = null;
                }
                super.dealloc();
            }
            
        };
        @Override
        public RTCDataChannel getChannel() {
            return channel;
        }
        
        /**
         * Creates a new event with given data.
         * @param data Map of data.  Expected to have at least "channel" property.
         */
        RTCDataChannelEventImpl(Map data) {
            super("datachannel", data);
            
            MapWrap w = new MapWrap(data);
            if (w.has("refId")) {
                rcs.setRefId(w.getString("refId", null));
                rcs.retain();
            }
            if (w.has("channel")) {
                Map channelData = (Map)w.get("channel");
                
                channel = (RTCDataChannelImpl)registry.get((String)channelData.get("refId"));
                if (channel == null) {
                    channel = new RTCDataChannelImpl(channelData);
                } else {
                    channel.retain();
                }
            }
            
        }

        @Override
        public void retain() {
            rcs.retain();
        }

        @Override
        public void release() {
            rcs.release();
        }
        
        
        
        
    }
    
    private class MessageEventImpl extends EventImpl implements MessageEvent, RefCounted {
        private String origin;
        private String lastEventId;
        private MessageEventSource source;
        private MessagePorts ports;
        private RefCountedSupport rcs = new RefCountedSupport(this) {
            @Override
            public void dealloc() {
                if (ports != null) {
                    ports.clear();
                    ports = null;
                }
                super.dealloc();
            }
            
        };
        
        MessageEventImpl(Map data) {
            super("message", data);
            
            MapWrap w = new MapWrap(data);
            if (!w.has("refId")) {
                throw new IllegalArgumentException("Received MessageEvent without refId.  This is an internal error.  MessageEvents must be propagated with a refId");
            }
            rcs.setRefId(w.getString("refId", null));
            rcs.retain();
            origin = w.getString("origin", null);
            lastEventId = w.getString("lastEventId", null);
            if (w.has("source")) {
                source = new MessageEventSourceImpl((Map)w.get("source", null));
            }
            
            if (w.has("ports")) {
                ports = new MessagePorts();
                List<Map> portsList = (List<Map>)w.get("ports", null);
                for (Map m : portsList) {
                    MessagePortImpl port = (MessagePortImpl)registry.get((String)m.get("refId"));
                    if (port == null) {
                        port = new MessagePortImpl(m);
                    } else {
                        port.retain();
                    }
                    ports.add(port);
                    port.release();  // because the MessagePorts retains it.
                }
            }
        }

        @Override
        public String getOrigin() {
            return origin;
        }

        @Override
        public String getLastEventId() {
            return lastEventId;
        }

        @Override
        public MessageEventSource getSource() {
            return source;
        }

        @Override
        public MessagePorts getPorts() {
            return ports;
        }

        @Override
        public void retain() {
            if (rcs != null) {
                rcs.retain();
            }
        }

        @Override
        public void release() {
            if (rcs != null) {
                rcs.release();
            }
        }
        
    }
    
    private class MessageEventSourceImpl implements MessageEventSource {
        MessageEventSourceImpl(Map data) {
           
        }
    }
    
     /**
      * Produce a string in double quotes with backslash sequences in all the
      * right places. A backslash will be inserted within </, allowing JSON
      * text to be delivered in HTML. In JSON text, a string cannot contain a
      * control character or an unescaped quote or backslash.
      * @param string A String
      * @return  A String correctly formatted for insertion in a JSON text.
      */
     private static String quote(String string) {
         if (string == null || string.length() == 0) {
             return "\"\"";
         }

         char         b;
         char         c = 0;
         int          i;
         int          len = string.length();
         StringBuilder sb = new StringBuilder(len + 4);
         String       t;

         sb.append('"');
         for (i = 0; i < len; i += 1) {
             b = c;
             c = string.charAt(i);
             switch (c) {
             case '\\':
             case '"':
                 sb.append('\\');
                 sb.append(c);
                 break;
             case '/':
                 if (b == '<') {
                     sb.append('\\');
                 }
                 sb.append(c);
                 break;
             case '\b':
                 sb.append("\\b");
                 break;
             case '\t':
                 sb.append("\\t");
                 break;
             case '\n':
                 sb.append("\\n");
                 break;
             case '\f':
                 sb.append("\\f");
                 break;
             case '\r':
                 sb.append("\\r");
                 break;
             default:
                 if (c < ' ') {
                     t = "000" + Integer.toHexString(c);
                     sb.append("\\u" + t.substring(t.length() - 4));
                 } else {
                     sb.append(c);
                 }
             }
         }
         sb.append('"');
         return sb.toString();
     }
    
    private class MessagePortImpl extends RefCountedImpl implements MessagePort {
        private EventSupport es = new EventSupport(this);
        MessagePortImpl(Map data) {
            MapWrap w = new MapWrap(data);
            if (w.has("refId")) {
                setRefId(w.getString("refId", null));
            }
            
        }

        @Override
        public void postMessage(String message) {
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage(${1})", new Object[]{getRefId(), message});
        }

        @Override
        public void postMessage(int message) {
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage(${1})", new Object[]{getRefId(), message});
        }

        @Override
        public void postMessage(double message) {
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage(${1})", new Object[]{getRefId(), message});
        }

        @Override
        public void postMessage(byte[] message) {
            StringBuilder sb = new StringBuilder();
            int len = message.length;
            sb.append("[");
            for (int i=0; i<len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(message[i]);
            }
            sb.append("]");
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage("+sb+")", new Object[]{getRefId()});
        }

        @Override
        public void postMessage(String[] message) {
            StringBuilder sb = new StringBuilder();
            int len = message.length;
            sb.append("[");
            for (int i=0; i<len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(quote(message[i]));
            }
            sb.append("]");
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage("+sb+")", new Object[]{getRefId()});
        }

        @Override
        public void postMessage(int[] message) {
            StringBuilder sb = new StringBuilder();
            int len = message.length;
            sb.append("[");
            for (int i=0; i<len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(message[i]);
            }
            sb.append("]");
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage("+sb+")", new Object[]{getRefId()});
        }

        @Override
        public void postMessage(double[] message) {
            StringBuilder sb = new StringBuilder();
            int len = message.length;
            sb.append("[");
            for (int i=0; i<len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(message[i]);
            }
            sb.append("]");
            web.execute(""
                    + "var port = registry.get(${0});"
                    + "port.postMessage("+sb+")", new Object[]{getRefId()});
        }

        @Override
        public void start() {
            web.execute("var port = registry.get(${0});"
                    + "port.start();", new Object[]{getRefId()});
        }

        @Override
        public void close() {
            web.execute("var port = registry.get(${0});"
                    + "port.close();", new Object[]{getRefId()});
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
    
    /**
     * Implementation of {@link RTCPeerConnectionIceEvent} which is used in "icecandidate" events.
     */
    private class RTCPeerConnectionIceEventImpl extends EventImpl implements RTCPeerConnectionIceEvent {
        private RTCIceCandidate candidate;
        
        /**
         * Creates a new event. 
         * @param type Should always be "icecandidate"
         * @param data The data.  Should at least contain "candidate" property.
         */
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
    
    /**
     * Implementation of {@link RTCPeerConnectionIceErrorEvent} which is fired with the "icecandidateerror" event.
     */
    private class RTCPeerConnectionIceErrorEventImpl extends EventImpl implements RTCPeerConnectionIceErrorEvent {

        /**
         * @return the address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @return the errorText
         */
        public String getErrorText() {
            return errorText;
        }

        /**
         * @return the port
         */
        public int getPort() {
            return port;
        }

        /**
         * @return the url
         */
        public String getURL() {
            return url;
        }
        private int errorCode;
        private String address;
        private String errorText;
        private int port;
        private String url;

        /**
         * Creates new event
         * @param errorCode The error code.
         */
        public RTCPeerConnectionIceErrorEventImpl(Map data) {
            super("icecandidateerror");
            MapWrap w = new MapWrap(data);
            this.errorCode = w.getInt("errorCode", 0);
            this.errorText = w.getString("errorText", "");
            this.address = w.getString("address", "");
            this.port = w.getInt("port", 0);
            this.url = w.getString("url", "");
        }

        @Override
        public int getErrorCode() {
            return this.errorCode;
        }
        
        
        
        
    }
    
    /**
     * Implementation of {@link RTCRtpSender}.
     */
    private class RTCRtpSenderImpl extends RefCountedImpl implements RTCRtpSender {
        private RTCDTMFSender dtmf;
        
        private MediaStreamTrack track;
        private RTCRtpSendParameters parameters;
        private RTCStatsReport stats;
        
        private boolean ready;
        private List<Runnable> onReady = new ArrayList<Runnable>();
        
        /**
         * Creates new sender with the given track.
         * 
         * NOTE: When using this version of the constructor, the sender object will be in a "not ready"
         * state until the {@link #fireReady()} method is called. This is because it hasn't yet been 
         * associated with a peer inside the web view.  This version of the constructor is used by {@link RTCPeerConnection#addTrack(com.codename1.webrtc.MediaStreamTrack, com.codename1.webrtc.MediaStream...) }
         * so you can see that implementation to see the flow.  It calls {@link #fireReady() } in its javascript callback.
         * @param track The track to include in the sender object.
         */
        RTCRtpSenderImpl(MediaStreamTrack track) {
            this.track = track;
            track.retain();
            
            
        }
        
        /**
         * A callback that is fired to indicate that the object has been bound to a 
         * peer in the webview and is ready to use.
         */
        void fireReady() {
            ready = true;
            while (onReady != null && !onReady.isEmpty()) {
                onReady.remove(0).run();
            }
            onReady = null;
        }
        
        /**
         * Creates a new sender object using data retrieved from a JS callback.
         * @param data The data.  Should contain at least `refId` and `track` properties.
         */
        RTCRtpSenderImpl(Map data) {
            setRefId((String)data.get("refId"));
            ready = true;
            retain();
            MapWrap w = new MapWrap(data);
            Map trackInfo = (Map)w.get("track", null);
            
            if (trackInfo != null) {
                track = (MediaStreamTrack)registry.get((String)trackInfo.get("refId"));
                
                // There are two possibilities for the track
                // 1. The track already exists in the registry.  In this case we need to 
                //    explicitly "retain" to increment its reference count.
                // 2. The track doesn't already exist.  In this case we create a new object
                //    We don't retain it explicitly because retain is always called RefCounted objects
                //    are created.
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
        public Promise setParameters(RTCRtpSendParameters parameters) {
            return new Promise((resolve, reject) ->{
                setParameters(parameters, resolve, reject);
            });
            
            
        }
        
        private void setParameters(RTCRtpSendParameters parameters, Functor resolve, Functor reject) {
            if (!ready) {
                onReady.add(()->setParameters(parameters, resolve, reject));
                return;
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
                            reject.call(err);
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
                            
                            resolve.call(null);
                                
                        } catch (Throwable t) {
                            Log.e(t);
                            reject.call(t);
                        }
                    });
            
        }

        @Override
        public Promise replaceTrack(MediaStreamTrack newTrack) {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    /**
     * Implements the {@link RTCRtpReceiver} interface.
     */
    private class RTCRtpReceiverImpl extends RefCountedImpl implements RTCRtpReceiver {
        private MediaStreamTrack track;
        
        private RTCRtpContributingSources contributingSources;
        private RTCRtpParameters parameters;
        private RTCRtpSynchronizationSources rtcRtpSynchronizationSources;
        
        /**
         * Creates new receiver with given data.
         * @param data Data.  Should contain at least `refId` and `track`.
         */
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
        public Promise<RTCStatsReport> getStats() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public RTCRtpSynchronizationSources getSynchronizationSources() {
            throw new RuntimeException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        
        
    }
    
    /**
     * Implementation of {@link RTCRtpTransceiver}
     */
    private class RTCRtpTransceiverImpl extends RefCountedImpl implements RTCRtpTransceiver {
        private RTCRtpTransceiverDirection currentDirection;
        
        private String mid;
        private RTCRtpReceiver receiver;
        private RTCRtpSender sender;
        private Timer poller;
        
        /**
         * Creates new transceiver
         * @param data The data.  Should contain at least `refId`, `mid`, `sender`, `receiver`.
         */
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
            if (sender != null) {
                sender.release();
                sender = null;
            }
            if (receiver != null) {
                receiver.release();
                receiver = null;
            }
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

        private String q(String val) {
            if (val == null) {
                return "null";
            }
            return "'" + val + "'";
        }
        
        @Override
        public void setCodecPreferences(RTCRtpCodecCapability... codecs) {
            StringBuilder sb = new StringBuilder();
            sb.append("var codecs = [");
            boolean first = true;
            for (RTCRtpCodecCapability codec : codecs) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append("RTCRtpSender.getCapabilities('audio').codecs.find(c => c.mimeType==").append(q(codec.getMimeType()))
                        .append(" && c.channels == ").append(codec.getChannels())
                        .append(" && c.clockRate==").append(codec.getClockRate()).append(")")
                        .append(" || RTCRtpSender.getCapabilities('video').codecs.find(c=> c.mimeType==").append(q(codec.getMimeType()))
                        .append(" && c.clockRate==").append(codec.getClockRate()).append(" && ").append(q(codec.getSdpFmtpLine()))
                        .append("==c.sdpFmtpLine)");
            }
            sb.append("]; t.setCodecPreferences(codecs);");
            web.execute("var t = registry.get(${0}); if (t) {"
                    + "try {"+ sb.toString()+" callback.onSuccess(null);} catch(e){callback.onError(e);}}"
                            + " else callback.onError(new Error('Transceiver not found'));", new Object[]{getRefId()}, res->{
                    
                });
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
    
    /**
     * Implementation of {@link RTCTrackEvent}.
     */
    private class RTCTrackEventImpl extends EventImpl implements RTCTrackEvent, RefCounted {
        private RTCRtpReceiver receiver;
        private MediaStreams streams;
        private MediaStreamTrack track;
        private RTCRtpTransceiver transceiver;
        
        /**
         * Since this extends EventImpl and not RefCountedImpl, we use a RefCountedSupport
         * to encapsulate the reference counting.
         */
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
        
        /**
         * Creates a new track event.
         * @param data The data.  Should contain at least `refId`, `streams`, `track`, `transceiver`, and `receiver`.
         */
        RTCTrackEventImpl(Map data) {
            super("track", data);
            System.out.println("Creating RTCTrackEventImpl with data "+data);
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
    
    /**
     * Utility wrapper for the {@link JSONParser#parseJSON(java.io.Reader) } that configures it
     * with booleans instead of strings, just how we like.
     * @param json
     * @return
     * @throws IOException 
     */
    private static Map parseJSON(String json) throws IOException {
        JSONParser p = new JSONParser();
        p.setUseBoolean(true);
        p.setIncludeNullsInstance(true);
        
        return p.parseJSON(new StringReader(json));
        
    }
    
   
   
   
    /**
     * A utility class to help objects implement {@link RefCounted}
     */
    private class RefCountedSupport extends RefCountedImpl {
        public RefCountedSupport(RefCounted proxying) {
            super(proxying);
        }
    }
    
    /**
     * Base implementation of any class that uses reference counting to manage binding
     * to a peer in the web view.
     */
    private class RefCountedImpl implements RefCounted {
        
        /**
         * If using composition rather than inheritance, {@link #proxying} will refer
         * to the actual java object that is being reference counted.
         */
        private RefCounted proxying=this;
        
        /**
         * The unique ID of the object. This is used to maintain the binding between the
         * java object and its peer javascript object.
         */
        private String refId;
        
        /**
         * The current number of active references to this object.
         */
        private int count;
        
        /**
         * Creates new object that reference counts the given proxying object
         * @param proxying The object that is to be reference counted.
         */
        public RefCountedImpl(RefCounted proxying) {
            this.proxying = proxying;
        }
        
        /**
         * Default constructor that should be used by subclasses.
         */
        public RefCountedImpl() {
            
        }
        
        /**
         * Initializes the object, assigning a unique reference ID, and Javascript code that should be used
         * to create the Javascript peer.  This should only be used for initializing objects that are created
         * on the java side.  For objects that are created to be peers of existing javascript objects, do not
         * use this method.  Simply call {@link #setRefId(java.lang.String) } with the refId provided in the 
         * javascript callback, and then call {@link #retain() }.
         * 
         * @param refId The reference ID to assign to the object.
         * @param javascript Javascript code to create the javascript peer.  Should return the javascript peer object itself.
         */
        public void init(String refId, String javascript) {
            init(refId, javascript, null, null);
        }
        
        /**
         * Initializes the object, assigning a unique reference ID, and Javascript code that should be used
         * to create the Javascript peer.  This should only be used for initializing objects that are created
         * on the java side.  For objects that are created to be peers of existing javascript objects, do not
         * use this method.  Simply call {@link #setRefId(java.lang.String) } with the refId provided in the 
         * javascript callback, and then call {@link #retain() }.
         * 
         * @param refId The reference ID to assign to the object.
         * @param javascript Javascript code to create the javascript peer.  Should return the javascript peer object itself.
         * @param callbackJs Javascript code to call the callback.
         * @param callback Callback to be executed when instantiation is done.
         */
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
        
       
        /**
         * Sets the reference ID of the Java object.
         * @param id 
         */
        public void setRefId(String id) {
            refId = id;
            
        }
        
        public String getRefId() {
            return refId;
        }
        
        public void dealloc() {
            
        }
        
    }
    
    
    /**
     * Implementation of {@link MediaStream}.
     */
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
    
    
    
    /**
     * Creates {@link MediaTrackSettings} object of given kind, with given data.
     * @param kind The kind. Either "audio" or "video"
     * @param m The data to populate the settings.  Generally passed as javascript callback.
     * @return 
     */
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
    
    /**
     * Creates new {@link MediaStreamTrack} with given data.
     * @param data Data.  Should contain at least `id`, `refId`, `contentHint`, `enabled`, `kind`, `label`, `muted`, `readonly`, `readyState`, `remote`, and `settings`.
     * @return 
     */
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
    
    /**
     * Creates new {@link MediaStream}
     * @param data The data.  Should contain at least `refId`, `id`, `active`, `ended`, and `tracks`.
     * @return 
     */
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
    
    /**
     * Creates new {@link MediaStream}
     * @param refId The unique refId to bind the javascript peer to the java peer.
     * @param id The stream ID
     * @param active 
     * @param ended
     * @param tracks
     * @return 
     */
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
    
    private class MediaDeviceInfoImpl implements MediaDeviceInfo {
        private String deviceId, groupId, label;
        private Kind kind;
        
        MediaDeviceInfoImpl(Map vals) {
            MapWrap w = new MapWrap(vals);
            deviceId = w.getString("deviceId", "");
            groupId = w.getString("groupid", "");
            label = w.getString("label", "");
            kind = Kind.fromString(w.getString("kind", ""));
            
        }
        
        @Override
        public String getDeviceId() {
            return deviceId;
        }

        @Override
        public String getGroupId() {
            return groupId;
        }

        @Override
        public Kind getKind() {
            return kind;
        }

        @Override
        public String getLabel() {
            return label;
        }
        
        public String toString() {
            return kind+": "+label+" deviceId="+deviceId+", groupId="+groupId;
        }
        
    }
    
    /**
     * Implementation of {@link MediaStreamTrack}
     */
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
        
        /**
         * Creates new MediaStreamTrack.
         * @param data The data.  Should contain at least `refId`, `contentHint`, `enabled`, `id`, `kind`, `label`, `muted`, `readonly`, `readyState`, `remote`, and settings.
         */
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
        public Promise applyConstraints(MediaTrackConstraints constraints) {
            return new Promise((resolve, reject)->{
                applyConstraints(constraints, resolve, reject);
            });
        }
        
        private void applyConstraints(MediaTrackConstraints constraints, Functor resolve, Functor reject) {
            if (stopping || readyState == ReadyState.Ended) {
                throw new IllegalStateException("Track is already ended");
            }
           
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
                    resolve.call(null);
                }

                @Override
                public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                    reject.call(err);
                }
                    });
            
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
    
    /**
     * A promise that is used for {@link #getUserMedia(com.codename1.webrtc.MediaStreamConstraints) }.  It 
     * 
     */
    private class MediaStreamRequest {
        /**
         * Whether native permissions have been requested yet.  The should only be requested
         * once if necessary.  If permissions aren't granted and this flag is already set,
         * then the promise will fail.
         */
        private boolean permissionRequested;
    }
    
    public Promise<MediaDeviceInfo[]> enumerateDevices() {
        return new Promise<MediaDeviceInfo[]>((resolve, reject) -> {
            enumerateDevices(resolve, reject);
        });
    }
        
    private void enumerateDevices(Functor resolve, Functor reject) {
        
        web.execute("try {navigator.mediaDevices.enumerateDevices().then("
                + "function(devices) {"
                + "  var _devices = [];"
                + "  for (var i=0; i<devices.length; i++) {"
                + "    var device = devices[i];"
                + "    var _device = {"
                + "      deviceId : device.deviceId,"
                + "      groupId : device.groupId,"
                + "      kind : device.kind,"
                + "      label : device.label"
                + "    };"
                + "    _devices.push(_device);"
                + "  }"
                + "  console.log('devices: ', _devices);"
                + "  callback.onSuccess(JSON.stringify({devices:_devices}));"
                + "}).catch(function(err) {"
                + "  callback.onError(err);"
                + "});"
                + "} catch (e) {"
                + "  callback.onError(e);"
                + "}", res-> {
                    System.out.println("Devices: "+res.getValue());
                    List<MediaDeviceInfo> devices = new ArrayList<>();
                    try {
                        Map parsed = parseJSON(res.getValue());
                        List<Map> rawDevices =(List<Map>)parsed.get("devices");
                        for (Map rawDevice : rawDevices) {
                            devices.add(new MediaDeviceInfoImpl(rawDevice));
                        }
                        resolve.call(devices.toArray(new MediaDeviceInfo[devices.size()]));
                    } catch (IOException ex) {
                        reject.call(ex);
                    }
                });
        
        
    }
    
    /**
     * Obtain access to the device camera and/or microphone.
     * @param constraints The constraints.
     * @return Promise that resolves to {@link MediaStream}.
     */
    public Promise<MediaStream> getUserMedia(MediaStreamConstraints constraints) {
        MediaStreamRequest request = new MediaStreamRequest();
        return new Promise<MediaStream>((resolve, reject)->{
            getUserMedia(constraints, request, resolve, reject);
        });
        
    }
    
    private void getUserMedia(MediaStreamConstraints constraints, MediaStreamRequest request, Functor resolve, Functor reject) {
        boolean requiresPermissions = !audioPermission && MediaStreamConstraints.isAudioRequested(constraints)
                || !videoPermission && MediaStreamConstraints.isVideoRequested(constraints);
        
        if (requiresPermissions) {
            if (request.permissionRequested) {
                reject.call(new RuntimeException("Permission denied"));
                return;
            }
            request.permissionRequested = true;
            checkPermission(MediaStreamConstraints.isAudioRequested(constraints), MediaStreamConstraints.isVideoRequested(constraints), ()->{
                getUserMedia(constraints, request, resolve, reject);
            });
            return;
        }
        
        web.execute("try{navigator.mediaDevices.getUserMedia("+constraints.toJSON()+").then("
                + "function(stream){"
                + "  console.log('getUserMedia callback', stream);"
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
                + "    track_.label = track_.label || '';"
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
                + "}).catch("
                + "function(error){"
                + "  console.log('getUserMedia error callback', error);"
                + "  callback.onError(error.message, 0);"
                + "})} catch (e)"
                + "{console.log('exception in getUserMedia', e);callback.onError(e.message, 0);}", new Callback<BrowserComponent.JSRef>() {
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
                            resolve.call(stream);
                            
                        } catch (IOException ex) {
                            onError(this, ex, 0, ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                        
                        reject.call(err);
                    }
                }
        );
        
        
                
    }
    
    /**
     * Gets the UI component associated with the RTC.  Place this component in your UI hierarchy.
     * @return 
     */
    public Component getVideoComponent() {
        return web;
    }
    
    private class RTCVideoElementImpl extends RTCMediaElementImpl implements RTCVideoElement {
        
        private int videoWidth, videoHeight;
        private boolean playsInline = true;
        
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

        @Override
        public boolean playsInline() {
            return playsInline;
        }

        @Override
        public void setPlaysInline(boolean playsInline) {
            if (playsInline != this.playsInline) {
                this.playsInline = playsInline;
                web.execute("registry.get(${0}).playsInline = "+playsInline+";", new Object[]{getRefId()});
            }
        }

       
    }
    
    /**
     *
     */
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
            String playsInline = "";
            if ("video".equals(type)) {
                playsInline = "vid.playsInline=true;";
            }
            init(Util.getUUID(), "(function(){var vid = document.createElement('"+type+"'); "
                    + playsInline
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
                if (srcObject != null) {
                    srcObject.retain();
                }
                if (srcObject != null) {
                    web.execute("registry.get(${0}).srcObject = registry.get(${1});", new Object[]{getRefId(), srcObject.getRefId()});
                } else {
                    web.execute("registry.get(${0}).srcObject = null;", new Object[]{getRefId()});
                }
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
        public Promise play() {
            return new Promise((resolve, reject) -> {
                web.execute("registry.get(${0}).play().then(function(){callback.onSuccess(null)}, function(e){callback.onError(e);});", new Object[]{getRefId()}, new Callback<BrowserComponent.JSRef>() {
                    @Override
                    public void onSucess(BrowserComponent.JSRef value) {
                        resolve.call(null);
                    }

                    @Override
                    public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                        reject.call(err);
                    }
                });
            
            });
            
        }

        @Override
        public Promise setSinkId(String sinkId) {
            return new Promise((resolve, reject) -> {
                web.execute("registry.get(${0}).setSinkId(${1}).then(function() {callback.onSuccess(null);}).catch(function(e){callback.onError(e)});", new Object[]{getRefId(), sinkId}, new Callback<BrowserComponent.JSRef>() {
                    @Override
                    public void onSucess(BrowserComponent.JSRef value) {
                        resolve.call(null);
                    }

                    @Override
                    public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                        reject.call(err);
                    }
                });
            });
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
    
    /**
     * Creates a video element that can be used to display a video stream.
     * @return 
     */
    public RTCVideoElement createVideo() {
        RTCVideoElementImpl out = new RTCVideoElementImpl();
        return out;
    }
    
    /**
     * Creates an audio element that can be used to play an audio stream.
     * @return 
     */
    public RTCAudioElement createAudio() {
        return new RTCAudioElementImpl();
    }
   
    /**
     * Appends an audio or video element to the RTC canvas.  Use {@link RTCElement#applyStyle(java.lang.String) }
     * to position the element the way you like.
     * @param el 
     */
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
        public Promise addIceCandidate(RTCIceCandidate candidate) {
            return new Promise((resolve, reject) -> {
                if (isClosed()) {
                    reject.call(new IllegalStateException("Connection is closed"));
                    return;
                }
                if (candidate == null) {
                    // The spec allows for nulls, but should just resolve silently.
                    // This behaviour is copied from adapter-latest.js
                    resolve.call(null);
                    return;
                }
                String candidateString = candidate == null ? null : candidate.toJSON();
                System.out.println("calling addIceCandidate("+candidateString+")");
                execute("registry.get(${0}).addIceCandidate("+candidateString+").then(function(){"
                        + "  callback.onSuccess(null);"
                        + "}).catch(function(error){"
                        + "  callback.onError(error);"
                        + "});", new Object[]{getRefId()}, (jsref, error)->{
                            if (error != null) {
                                System.out.println("Result of addIceCandidate("+candidateString+"):" + error);
                                reject.call(error);
                            } else {
                                System.out.println("Result of addIceCandidate("+candidateString+"): success");
                                resolve.call(null);
                            }
                        });
            });
           
            
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
            
            
            String js = "var conn = registry.get(${0});"
                    + "var sender = conn.addTrack"+addTrackParams.toString()+"; "
                    + "callback.onSuccess(JSON.stringify(cn1.wrapRTCRtpSender(sender)));";
            
            execute(js, params, (res,error) -> {
                System.out.println("just got back from JS call to addTrack to a PeerConnection " + res);
                try {
                    if (error != null) {
                        
                        Log.e(error);
                        return;
                    }
                    
                    Map data = parseJSON(res.getValue());
                   
                    System.out.println("RTCRtpSender data: "+data);
                    sender.setRefId((String)data.get("refId"));
                    
                    if (senders == null) {
                        senders = new RTCRtpSenders();
                        senders.add(sender);
                    }
                    System.out.println("Firing sender ready");
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
        public Promise<RTCSessionDescription> createAnswer(RTCAnswerOptions options) {
            return new Promise<RTCSessionDescription>((resolve, reject)->{
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
                                reject.call(error);
                            } else {
                                try {
                                    Map data = parseJSON(res.getValue());
                                    RTCSessionDescription desc = new RTCSessionDescriptionImpl(data);
                                    resolve.call(desc);
                                } catch (IOException ex) {
                                    Log.e(ex);
                                    reject.call(ex);
                                }

                            }
                        });
            });
        }

        @Override
        public RTCDataChannel createDataChannel(String label, RTCDataChannelInit options) {
            return new RTCDataChannelImpl(this, label,  options);
        }

        @Override
        public Promise<RTCSessionDescription> createOffer(RTCOfferOptions options) {
            return new Promise<RTCSessionDescription>((resolve, reject) -> {
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
                                reject.call(error);
                            } else {
                                try {
                                    Map data = parseJSON(res.getValue());
                                    RTCSessionDescription desc = new RTCSessionDescriptionImpl(data);
                                    resolve.call(desc);
                                } catch (IOException ex) {
                                    Log.e(ex);
                                    reject.call(ex);
                                }

                            }
                        });
            });
            
        }

        @Override
        public Promise<RTCSessionDescription> createAnswer() {
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
        public Promise<RTCStatsReport> getStats(MediaStreamTrack selector) {
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
        public Promise setLocalDescription(RTCSessionDescription sessionDescription) {
            return new Promise((resolve, reject) -> {
                execute("var conn = registry.get(${0}); conn.setLocalDescription("+sessionDescription.toJSON()+").then(function(res) {"
                    + "  callback.onSuccess(null);"
                    + "}).catch(function(error) {"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            reject.call(error);
                        } else {
                            localDescription = sessionDescription;
                            resolve.call(null);
                        }
                    });
            });

        }

        @Override
        public Promise setRemoteDescription(RTCSessionDescription sessionDescription) {
            return new Promise((resolve, reject) -> {
                execute("var conn = registry.get(${0}); conn.setRemoteDescription("+sessionDescription.toJSON()+").then(function(res) {"
                    + "  callback.onSuccess(null);"
                    + "}).catch(function(error) {"
                    + "  callback.onError(error);"
                    + "});", new Object[]{getRefId()}, (res, error) -> {
                        if (error != null) {
                            reject.call(error);
                        } else {
                            remoteDescription = sessionDescription;
                            resolve.call(null);
                        }
                    });
            
            });
            
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
            String strConfiguration = configuration == null ? null :
                    Result.fromContent((Map)configuration.toJSONStruct()).toString();
            init(Util.getUUID(), "new RTCPeerConnection("+strConfiguration+")");
            
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
        private RTCIceProtocol protocol;
        private String relatedAddress;
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
            if (protocol != null) out.put("protocol", protocol.stringValue());
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
            if (m.has("protocol")) {
                protocol = RTCIceProtocol.protocolFromString((String)m.get("protocol", ""));
            }
            relatedAddress = (String)m.get("relatedAddress", null);
            sdpMid = (String)m.get("sdpMid", null);
            sdpMLineIndex = m.getInt("sdpMLineIndex", null);
            tcpType = (String)m.get("tcpType", null);
            type = (String)m.get("type", null);
            usernameFragment = (String)m.get("usernameFragment", null);
        }

        @Override
        public String toString() {
            return toJSON();
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

        @Override
        public RTCIceProtocol getProtocol() {
            return protocol;
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
    
    
    /**
     * Convenience method for creating a {@link ConstrainString} with exact value.
     * @param val The exact value to constrain to.
     * @return The ConstrainString with provided exact value.
     * @see ConstrainString#exact(java.lang.String) 
     */
    public static ConstrainString exact(String val) {
        return new ConstrainString().exact(val);
    }
    
    /**
     * Convenience method for creating a {@link ConstrainString} with ideal value.
     * @param val The ideal value to constrain to.
     * @return The ConstrainString with provided ideal value.
     * @see ConstrainString#ideal(java.lang.String) 
     */
    public static ConstrainString ideal(String val) {
        return (ConstrainString)new ConstrainString().ideal(val);
    }
    
    /**
     * Gets the sender capabilities for the given media type.  
     * @param type Either video or audio.
     * @return The sender capabilities for the given media type.
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSender/getCapabilities
     */
    public RTCRtpCapabilities getSenderCapabilities(String type) {
        switch (type) {
            case "audio":
                return senderAudioCapabilities;
            case "video":
                return senderVideoCapabilities;
            default:
                throw new IllegalArgumentException("Unrecognized rtp capability type.  Looking or audio or video but found "+type);
        }
    }
    
    /**
     * 
     * Gets the receiver capabilities for the given media type.  
     * @param type Either video or audio.
     * @return The receiver capabilities for the given media type.
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver/getCapabilities
     */
    public RTCRtpCapabilities getReceiverCapabilities(String type) {
        switch (type) {
            case "audio":
                return receiverAudioCapabilities;
            case "video":
                return receiverVideoCapabilities;
            default:
                throw new IllegalArgumentException("Unrecognized rtp capability type.  Looking or audio or video but found "+type);
        }
    }
}
