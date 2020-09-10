/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.webrtc.Event;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.MediaStreamTracks;
import com.codename1.webrtc.Promise;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCConfiguration;
import com.codename1.webrtc.RTCPeerConnection;
import com.codename1.webrtc.RTCPeerConnection.RTCOfferOptions;
import com.codename1.webrtc.RTCPeerConnectionIceEvent;
import com.codename1.webrtc.RTCSessionDescription;
import com.codename1.webrtc.RTCTrackEvent;
import com.codename1.webrtc.RTCVideoElement;
import java.util.Date;

/**
 * This example is adapted from https://webrtc.github.io/samples/src/content/peerconnection/pc1/
 * https://github.com/webrtc/samples/blob/gh-pages/src/content/peerconnection/pc1/js/main.js
 * @author shannah
 */
public class PeerConnectionStatesDemo extends Form implements AutoCloseable {
    
    private RTCVideoElement video1, video2;
    
    private Button startButton = new Button("Start"),
            callButton = new Button("Call"),
            hangupButton = new Button("Hang up");
    {
        startButton.setEnabled(true);
        callButton.setEnabled(false);
        hangupButton.setEnabled(false);
        startButton.addActionListener(evt->start());
        callButton.addActionListener(evt->call());
        hangupButton.addActionListener(evt->hangup());
    }

    
    private Date startTime;
    
    private MediaStream localStream;
    private RTCPeerConnection pc1, pc2;
    private SpanLabel pc1StateDiv = new SpanLabel();
    private SpanLabel pc2StateDiv = new SpanLabel();
    private SpanLabel pc1IceStateDiv = new SpanLabel();
    private SpanLabel pc2IceStateDiv = new SpanLabel();
    private SpanLabel pc1ConnStateDiv = new SpanLabel();
    private SpanLabel pc2ConnStateDiv = new SpanLabel();
    
    private static final RTCOfferOptions offerOptions = new RTCOfferOptions()
            .offerToReceiveAudio(true)
            .offerToReceiveVideo(true);
    
    private RTC rtc;
    
    public PeerConnectionStatesDemo() {
        super("Peer Connection Demo", new BorderLayout());
        
        Container center = new Container(BoxLayout.y());
        
        Container videoCnt = new Container(new BorderLayout());
        
        String intro = "This sample was adapted from the \"PeerConnection: States Demo\" on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/PeerConnectionStatesDemo.java"));
        add(BorderLayout.NORTH, BoxLayout.encloseY(new SpanLabel(intro), viewSource));
        
        videoCnt.add(BorderLayout.SOUTH, FlowLayout.encloseCenter(startButton, callButton, hangupButton));
        videoCnt.setPreferredH(CN.getDisplayHeight()/2);
        center.add(videoCnt);
        
        center.add("PC1 state:").
        add(pc1StateDiv).
        add("PC1 ICE state:").
        add(pc1IceStateDiv).
        add("PC1 connection state:").
        add(pc1ConnStateDiv).
        
        add("PC2 state:").
        add(pc2StateDiv).
        add("PC2 ICE state:").
        add(pc2IceStateDiv).
        add("PC2 connection state:").
        add(pc2ConnStateDiv).
        
        add(new SpanLabel("View the console to see logging. The MediaStream object localStream, and the RTCPeerConnection objects localPeerConnection and remotePeerConnection are in global scope, so you can inspect them in the console as well."));
        center.setScrollableY(true);
        
        add(BorderLayout.CENTER, center);
        RTC.createRTC().ready(r->{
            rtc = r;
            video1 = rtc.createVideo();
            video1.setAutoplay(true);
            video1.setMuted(true);
            video1.applyStyle("position:fixed;width:50%;height:100%;top:0;left:0;bottom:0;");
            
            video2 = rtc.createVideo();
            video2.setAutoplay(true);
            video2.applyStyle("position:fixed;width:50%;height:100%;top:0;right:0;bottom:0;");
            rtc.append(video1);
            rtc.append(video2);
            
            video1.addEventListener("loadedmetadata", evt->{
                System.out.println("Local video videoWidth: "+video1.getVideoWidth()+"px,  videoHeight: "+video1.getVideoHeight()+"px");
            });
            video2.addEventListener("loadedmetadata", evt->{
                System.out.println("Remote video size changed to "+video2.getVideoWidth()+"x"+video2.getVideoHeight());
                if (startTime != null) {
                    long elapsedTime = System.currentTimeMillis() - startTime.getTime();
                    System.out.println("Setup time: "+elapsedTime+"ms");
                    startTime = null;
                }
            });
            
            videoCnt.add(BorderLayout.CENTER, rtc.getVideoComponent());
            revalidateWithAnimationSafety();
        });
        
    }
    
    private void gotStream(MediaStream stream) {
        Log.p("Received local stream");
        video1.setSrcObject(stream);
        localStream = stream;
        stream.retain();
        callButton.setEnabled(true);
    }
    
    private void start() {
        Log.p("Requesting local stream");
        startButton.setEnabled(false);
        
        rtc.getUserMedia(new MediaStreamConstraints().audio(true).video(true)).onSuccess(stream->{
            gotStream(stream);
        }).onFail(t->{
            Log.e((Throwable)t);
            Dialog.show("Error", "getUserMedia() error: "+((Throwable)t).getMessage(), "OK", null);
        });
            
    } 
    
    
    private void call() {
        callButton.setEnabled(false);
        hangupButton.setEnabled(true);
        Log.p("Starting call");
        startTime = new Date();
        MediaStreamTracks videoTracks = localStream.getVideoTracks();
        MediaStreamTracks audioTracks = localStream.getAudioTracks();
        if (videoTracks.size() > 0) {
            Log.p("Using video device "+videoTracks.get(0).getLabel());
        }
        if (audioTracks.size() > 0) {
            Log.p("Using audio device "+audioTracks.get(0).getLabel());
        }
        RTCConfiguration servers = null;
        pc1 = rtc.newRTCPeerConnection(servers);
        Log.p("Created local peer connection object pc1");
        pc1StateDiv.setText(pc1.getSignalingState()+"");
        pc1.addEventListener("signalingstatechange", evt->stateCallback1());
        
        pc1IceStateDiv.setText(pc1.getIceConnectionState()+"");
        pc1.addEventListener("iceconnectionstatechange", evt->iceStateCallback1());
        pc1.addEventListener("connectionstatechange", evt->connStateCallback1());
        pc1.addEventListener("icecandidate", e->onIceCandidate(pc1, (RTCPeerConnectionIceEvent)e));

        pc2 = rtc.newRTCPeerConnection(servers);
        Log.p("Created remote peer connection object pc2");
        pc2StateDiv.setText(pc2.getSignalingState()+"");
        pc2.addEventListener("signalingstatechange", evt->stateCallback2());
        
        pc2IceStateDiv.setText(pc2.getIceConnectionState()+"");
        pc2.addEventListener("iceconnectionstatechange", evt->iceStateCallback2());
        pc2.addEventListener("connectionstatechange", evt->connStateCallback2());
        pc2.addEventListener("icecandidate", evt->{
            onIceCandidate(pc2, (RTCPeerConnectionIceEvent)evt);
        });
        pc2.addEventListener("track", evt->gotRemoteStream((RTCTrackEvent)evt));
        for (MediaStreamTrack track : localStream.getTracks()) {
            pc1.addTrack(track, localStream);
        }
        Log.p("Adding local stream to peer connection");
        
        pc1.createOffer(offerOptions).onSuccess(offer->{
            gotDescription1(offer);
        })
        .onFail(e-> {
            onCreateSessionDescriptionError((Throwable)e);
        });

        
    }
    
    private void onCreateSessionDescriptionError(Throwable e) {
        Log.p("Failed to create session description: "+e.getMessage(), Log.ERROR);
    }
    
    private void gotDescription1(RTCSessionDescription description) {
        pc1.setLocalDescription(description);
        Log.p("Offer from pc1:\n"+description.getSdp());
        pc2.setRemoteDescription(description);
        pc2.createAnswer()
                .onSuccess(desc->gotDescription2(desc))
                .onFail(e->onCreateSessionDescriptionError((Throwable)e));
    }
    
    private void gotDescription2(RTCSessionDescription description) {
        pc2.setLocalDescription(description);
        Log.p("Answer from pc2\n"+description.getSdp());
        pc1.setRemoteDescription(description);
    }
    
    private void hangup() {
        Log.p("Ending call");
        pc1.close();
        pc2.close();
        pc1StateDiv.setText(pc1StateDiv.getText() + pc1.getSignalingState());
        pc2StateDiv.setText(pc2StateDiv.getText() + pc2.getSignalingState());
        pc1.release();
        pc1 = null;
        pc2.release();
        pc2 = null;
        hangupButton.setEnabled(false);
        callButton.setEnabled(true);
    }
    
    private void gotRemoteStream(RTCTrackEvent e) {
        if (video2.getSrcObject() != e.getStreams().get(0)) {
            video2.setSrcObject(e.getStreams().get(0));
            Log.p("Got remote stream");
        }
    }
    
    
    private void stateCallback1() {
        if (pc1 != null) {
            Log.p("pc1 state change callback, state: "+pc1.getSignalingState());
            pc1StateDiv.setText(pc1StateDiv.getText() + pc1.getSignalingState());
        }
    }
    
    private void stateCallback2() {
        if (pc2 != null) {
            Log.p("pc2 state change callback, state: "+pc2.getSignalingState());
            pc2StateDiv.setText(pc2StateDiv.getText() + pc2.getSignalingState());
        }
    }
    
    private void iceStateCallback1() {
        if (pc1 != null) {
            Log.p("pc1 ICE connection state change callback, state: "+pc1.getIceConnectionState());
            pc1IceStateDiv.setText(pc1IceStateDiv.getText() + pc1.getIceConnectionState());
        }
    }
    
    private void iceStateCallback2() {
        if (pc2 != null) {
            Log.p("pc2 ICE connection state change callback, state: "+pc2.getIceConnectionState());
            pc2IceStateDiv.setText(pc2IceStateDiv.getText() + pc2.getIceConnectionState());
        }
    }
    
    private void connStateCallback1() {
        if (pc1 != null) {
            Log.p("pc1 connection state change callback, state: "+pc1.getConnectionState());
            pc1ConnStateDiv.setText(pc1ConnStateDiv.getText() + pc1.getConnectionState());
        }
    }
    
    private void connStateCallback2() {
        if (pc2 != null) {
            Log.p("pc2 connection state change callback, state: "+pc2.getConnectionState());
            pc2ConnStateDiv.setText(pc2ConnStateDiv.getText() + pc2.getConnectionState());
        }
    }
    
    private RTCPeerConnection getOtherPc(RTCPeerConnection pc) {
        return pc == pc1 ? pc2 : pc1;
    }
    
    private String getName(RTCPeerConnection pc) {
        return pc == pc1 ? "pc1" : "pc2";
    }
    
    private void onIceCandidate(RTCPeerConnection pc, RTCPeerConnectionIceEvent event) {
        getOtherPc(pc).addIceCandidate(event.getCandidate())
                .onSuccess(res->onAddIceCandidateSuccess(pc))
                .onFail(e->{
            Log.e((Throwable)e);
            onAddIceCandidateError(pc, (Throwable)e);
        }).onComplete(e->{
            Log.p(getName(pc)+" ICE candidate:\n"+(event.getCandidate() != null ? event.getCandidate().getCandidate() : "(null"));
        });

    }
       
     private void onAddIceCandidateSuccess(RTCPeerConnection pc) {
        Log.p(getName(pc)+" addIceCandidate success");
    }
    
    
    private void onAddIceCandidateError(RTCPeerConnection pc, Throwable error) {
        Log.p(getName(pc)+" failed to add ICE Candidate: "+error.getMessage(), Log.ERROR);
    }
    
    
    

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
        if (pc1 != null) {
            pc1.release();
            pc1 = null;
        }
        if (pc2 != null) {
            pc2.release();
            pc2 = null;
        }

    }
    
}
