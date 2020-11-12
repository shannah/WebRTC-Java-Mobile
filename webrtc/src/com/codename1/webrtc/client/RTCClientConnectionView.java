/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import com.codename1.io.Log;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.RTCPeerConnection;
import com.codename1.webrtc.RTCRtpReceiver;
import com.codename1.webrtc.RTCTrackEvent;
import com.codename1.webrtc.RTCTrackEventListener;
import com.codename1.webrtc.RTCVideoContainer;
import java.util.HashMap;
import java.util.Map;

/**
 * A component for displaying the video from a client connection.
 * @author shannah
 */
public class RTCClientConnectionView extends Container {
    private RTCVideoContainer videoContainer;
    
    private RTCClientConnection connection;
    private ConnectionInfo info = new ConnectionInfo();
    private MediaStream stream;
    private Container wrapper = new Container(new BorderLayout());
    
    private Map<RTCPeerConnection.RTCPeerConnectionState, Component> stateViews = new HashMap<>();
   
    
    private void debug(String message) {
        Log.p("[RTCClientConnectionView] "+message, Log.DEBUG);
    }
    
    /**
     * Creates a new view for the given connection.
     * @param connection 
     */
    public RTCClientConnectionView(RTCClientConnection connection) {
        super(new BorderLayout());
        debug("RTCClientConnectionView("+connection+")");
        add(CENTER, wrapper);
        this.connection = connection;
        connection.getPeerConnection().then(pc -> {
            initPeerConnection(pc);
            return null;
        });

        update();
        setGrabsPointerEvents(true);
        videoContainer = new RTCVideoContainer();
        if (connection.getStream() != null) {
            setStream(connection.getStream());
        }
       
        
    }

    public RTCClientConnection getConnection() {
        return connection;
    }
    
    @Override
    public void pointerPressed(int x, int y) {
        super.pointerPressed(x, y);
    }
    
    
    
    private void onTrackRemoved(RTCTrackEvent evt) {
        debug("onTrackRemoved("+evt+")");
        if (evt.getStreams().get(0).getTracks().size() == 0) {
            setStream(null);
            update();
        }
    }
    
    private void onTrackAdded(RTCTrackEvent evt) {
        debug("onTrackAdded("+evt+")");
        if (evt.getStreams().get(0) != stream) {
            setStream(evt.getStreams().get(0));
            update();
        }
    }
    
    
    private void setStream(MediaStream stream) {
        debug("setStream("+stream+")");
        if (stream != this.stream) {
            debug("setStream("+stream+") : stream changed");
            if (this.stream != null) {
                this.stream.release();
                videoContainer.setSrcObject(null);
            }
            this.stream = stream;
            if (this.stream != null) {
                this.stream.retain();
                if (videoContainer != null) {
                    videoContainer.setSrcObject(stream);
                }
            } else {
                if (videoContainer != null) {
                    videoContainer.setSrcObject(null);
                }
            }
        }
        
    }
    
    private void update() {
        debug("update()");
        info.refresh();
        connection.getPeerConnection().then(pc->{
            Component cmp = getComponentForState(pc.getConnectionState());
            if (cmp != null && !wrapper.contains(cmp)) {
                wrapper.removeAll();
                wrapper.add(CENTER, cmp);
                Form f = wrapper.getComponentForm();
                if (f != null) {
                    f.revalidateLater();
                }
            }
            return null;
        });
       
        
    }
    
    private class ConnectionInfo {
        boolean video;
        boolean audio;
        boolean connected;
        boolean connecting;
        
        private void reset() {
            video = false;
            audio = false;
            connected = false;
            connecting = false;
        }
        
        private void refresh() {
            
            debug("ConnectionInfo.refresh()");
            connection.getPeerConnection().then(pc -> {
                reset();
                for (RTCRtpReceiver receiver : pc.getReceivers()) {
                    MediaStreamTrack track = receiver.getTrack();
                    if (track.isEnabled()) {
                        info.connected = true;
                        switch (track.getKind()) {
                            case Audio:
                                info.audio = true;
                                break;
                            case Video:
                                info.video = true;
                                break;
                        }
                    }


                }
                debug("ConnectionInfo.refresh() end");
                return null;
            });
            
        }
    
    }
    
    private RTCTrackEventListener addTrackListener = evt -> {
        onTrackAdded(evt);
    };  
    
    private RTCTrackEventListener removeTrackListener = evt -> {
        onTrackRemoved(evt);
    };
   
    
   
    
    public void initPeerConnection(RTCPeerConnection pc) {
        debug("initPeerConnection("+pc+")");
        pc.addTrackListener(addTrackListener);
        pc.addRemoveTrackListener(removeTrackListener);
    }
   
    
    private Component getComponentForState(RTCPeerConnection.RTCPeerConnectionState state) {
        debug("getComponentForState("+state+")");
        if (stream != null && stream.isActive()) {
            debug("getComponentForState(): upgrading state to connected because stream is active");
            state = RTCPeerConnection.RTCPeerConnectionState.Connected;
        }
        if (state == RTCPeerConnection.RTCPeerConnectionState.Connected) {
            debug("State is connected");
            if (videoContainer == null) {
                videoContainer = new RTCVideoContainer(stream);
            }
            return videoContainer;
            
        }
        Component cmp = stateViews.get(state);
        System.out.println("Cached state for state "+state+" is "+cmp);
        if (cmp == null) {
            cmp = createComponentForState(state);
            if (cmp != null) {
                stateViews.put(state, cmp);
            }
        }
        System.out.println("Got component for state "+cmp);
        return cmp;
    }
    
    
    private Component createComponentForState(RTCPeerConnection.RTCPeerConnectionState state) {
        debug("createComponentForState("+state+")");
        switch (state) {
            case New:
                return new Label("Not Connected");
            case Connecting:
                return new Label("Connecting");
            case Disconnected:
                return new Label("Disconnected");
            case Failed:
                return new Label("Failed");
            case Connected:
                // This will be replaced by the actual VideoContainer when available.
                return new Label("Connecting");
        }
        System.out.println("NO applicable states found.  Returning null");
        return null;
    }
    
    public void cleanup() {
        debug("cleanup()");
        //setPeerConnection(null);
        setStream(null);
        
    }
    
   
}
