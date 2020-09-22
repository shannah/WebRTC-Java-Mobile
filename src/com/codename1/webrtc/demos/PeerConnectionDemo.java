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
public class PeerConnectionDemo extends Form implements AutoCloseable {
    private Button startButton = new Button("Start"),
            callButton = new Button("Call"),
            hangupButton = new Button("Hang up");
    {
        callButton.setEnabled(false);
        hangupButton.setEnabled(false);
        startButton.addActionListener(evt->{
            start();
        });
        callButton.addActionListener(evt->{
            call();
        });
        hangupButton.addActionListener(evt->{
            hangup();
        });
    }

    
    private Date startTime;
    private RTCVideoElement localVideo, remoteVideo;
    private MediaStream localStream;
    private RTCPeerConnection pc1, pc2;
    
    private static final RTCOfferOptions offerOptions = new RTCOfferOptions()
            .offerToReceiveAudio(true)
            .offerToReceiveVideo(true);
    
    private RTC rtc;
    
    public PeerConnectionDemo() {
        super("Peer Connection Demo", new BorderLayout());
        
        String intro = "This sample was adapted from the \"Basic PeerConnection Demo\" on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/PeerConnectionDemo.java"));
        add(BorderLayout.NORTH, BoxLayout.encloseY(new SpanLabel(intro), viewSource));
        
        add(BorderLayout.SOUTH, FlowLayout.encloseCenter(startButton, callButton, hangupButton));
        RTC.createRTC().ready(r->{
            rtc = r;
            localVideo = rtc.createVideo();
            localVideo.setAutoplay(true);
            localVideo.setMuted(true);
            localVideo.applyStyle("position:fixed;width:50%;height:100%;top:0;left:0;bottom:0;");
            
            remoteVideo = rtc.createVideo();
            remoteVideo.setAutoplay(true);
            remoteVideo.applyStyle("position:fixed;width:50%;height:100%;top:0;right:0;bottom:0;");
            rtc.append(localVideo);
            rtc.append(remoteVideo);
            
            localVideo.onloadedmetadata(evt->{
                System.out.println("Local video videoWidth: "+localVideo.getVideoWidth()+"px,  videoHeight: "+localVideo.getVideoHeight()+"px");
            });
            remoteVideo.onloadedmetadata( evt->{
                System.out.println("Remote video size changed to "+remoteVideo.getVideoWidth()+"x"+remoteVideo.getVideoHeight());
                if (startTime != null) {
                    long elapsedTime = System.currentTimeMillis() - startTime.getTime();
                    System.out.println("Setup time: "+elapsedTime+"ms");
                    startTime = null;
                }
            });
            
            add(BorderLayout.CENTER, rtc.getVideoComponent());
            revalidateWithAnimationSafety();
        });
        
    }
    
    private String getName(RTCPeerConnection pc) {
        return pc == pc1 ? "pc1" : "pc2";
    }
    
    private RTCPeerConnection getOtherPc(RTCPeerConnection pc) {
        return pc == pc1 ? pc2 : pc1;
    }
    
    
    private void start() {
        System.out.println("Requesting local stream");
        startButton.setEnabled(false);
        
        rtc.getUserMedia(new MediaStreamConstraints().audio(true).video(true)).onSuccess(stream->{
            System.out.println("Received local stream");
            localVideo.setSrcObject(stream);
            localStream = stream;
            callButton.setEnabled(true);
        }).onFail(t->{
            Log.e((Throwable)t);
            Dialog.show("Error", "getUserMedia() error: "+((Throwable)t).getMessage(), "OK", null);
        });
            
    }    
    
    
    
    
    private void call() {
        callButton.setEnabled(false);
        hangupButton.setEnabled(true);
        startTime = new Date();
        MediaStreamTracks videoTracks = localStream.getVideoTracks();
        MediaStreamTracks audioTracks = localStream.getAudioTracks();
        if (videoTracks.size() > 0) {
            System.out.println("Using video device "+videoTracks.get(0).getLabel());
        }
        if (audioTracks.size() > 0) {
            System.out.println("Using audio device "+audioTracks.get(0).getLabel());
        }
        RTCConfiguration configuration = new RTCConfiguration();
        pc1 = rtc.newRTCPeerConnection(configuration);
        System.out.println("Created local peer connection object pc1");
        pc1.onicecandidate(pevt->{
            String candidate = pevt.getCandidate() == null ? null : pevt.getCandidate().getCandidate();
            Log.p("[CALL] pc1.onicecandidate "+candidate);
            onIceCandidate(pc1, pevt);
        });
        pc2 = rtc.newRTCPeerConnection(configuration);
        System.out.println("Created remote peer connection object pc2");
        pc2.onicecandidate(pevt->{
            String candidate = pevt.getCandidate() == null ? null : pevt.getCandidate().getCandidate();
            Log.p("[CALL] pc2.onicecandidate "+candidate);
            onIceCandidate(pc2, pevt);
        });
        pc1.oniceconnectionstatechange(evt->{
            Log.p("[CALL] pc1.oniceconnectionstatechange "+pc1.getIceConnectionState());
            onIceStateChange(pc1, evt);
        });
        pc2.oniceconnectionstatechange(evt->{
            Log.p("[CALL] pc2.oniceconnectionstatechange "+pc2.getIceConnectionState());
            onIceStateChange(pc2, evt);
        });
        pc2.ontrack(revt->{
            Log.p("[CALL] pc2.ontrack " + revt.getTrack().getId());
            gotRemoteStream(revt);
        });
        
        for (MediaStreamTrack track : localStream.getTracks()) {
            Log.p("[CALL] pc1.addTrack "+track.getId());
            pc1.addTrack(track, localStream);
        }
        System.out.println("Added local stream to pc1");
        
        
        System.out.println("pc1 createOffer start");
        pc1.createOffer(offerOptions).onSuccess(offer->{
            Log.p("[CALL] createOffer.onSucces");
            onCreateOfferSuccess(offer);
        })
        .onFail(e-> {
            onCreateSessionDescriptionError((Throwable)e);
        });

        
        
        
        
    }
    
    private void onCreateSessionDescriptionError(Throwable e) {
        Log.p("Failed to create session description: "+e.getMessage());
        Log.e(e);
        
    }
    
    
    
    
    
    
    private void onCreateOfferSuccess(RTCSessionDescription desc) {
        System.out.println("offer from pc1\n"+desc.getSdp());
        System.out.println("pc1 setLocalDescription start");
        
        Promise p1 = pc1.setLocalDescription(desc).onSuccess(res->{
                Log.p("pc1.setLocalDescription.onSuccess");
                onSetLocalSuccess(pc1);
        
        }).onFail(e->{
                Log.p("pc1.setLocalDescription.onFail");
                Log.e((Throwable)e);
                onSetSessionDescriptionError();
            });
            
        
        System.out.println("pc2 setRemoteDescription start");
        
        Promise p2 = pc2.setRemoteDescription(desc).onSuccess(res->{
            Log.p("pc2.setRemoteDescription.onSuccess");
            onSetRemoteSuccess(pc2);
            
        }).onFail(t->{
                    Log.p("pc2.setRemoteDescription.onFail");
                    Log.e((Throwable)t);
                    onSetSessionDescriptionError();
                });
        
        
        Promise.all(p1, p2).onSuccess(res->{
            System.out.println("pc2 createAnswer start");
            
            pc2.createAnswer().onSuccess(answer->onCreateAnswerSuccess(answer))
                    .onFail(e->onCreateSessionDescriptionError((Throwable)e));
                
        });
        
        
    }
    
    private void onCreateAnswerSuccess(RTCSessionDescription desc) {
        System.out.println("Anwswer from pc2:\n"+desc.getSdp());
        System.out.println("pc2 setLocalDescription start");

        pc2.setLocalDescription(desc).onSuccess(res -> onSetLocalSuccess(pc2))
                .onFail(e->{
            Log.e((Throwable)e);
            onSetSessionDescriptionError((Throwable)e);
            
        });
        
        System.out.println("pc1 setRemoteDescription start");
        
        pc1.setRemoteDescription(desc).onSuccess(res->onSetRemoteSuccess(pc1))
                .onFail(e->{
            Log.e((Throwable)e);
            onSetSessionDescriptionError((Throwable)e);
        });

    }
    
    private void onSetSessionDescriptionError() {
        System.out.println("Error in setSessionDescription");
        Log.e(new RuntimeException("Error"));
    }
    
    private void onSetLocalSuccess(RTCPeerConnection pc) {
        System.out.println(getName(pc)+" setLocalDescription complete");
    }
    
    private void onSetRemoteSuccess(RTCPeerConnection pc) {
        System.out.println(getName(pc)+" setRemoteDescription complete");
    }
    
    
    
    private void gotRemoteStream(RTCTrackEvent e) {
        if (remoteVideo.getSrcObject() != e.getStreams().get(0)) {
            System.out.println("pc2 received remote stream");
            remoteVideo.setSrcObject(e.getStreams().get(0));
        }
    }
    
    
    
    private void onIceCandidate(RTCPeerConnection pc, RTCPeerConnectionIceEvent event) {
        if (event.getCandidate() == null) {
            System.out.println("No ICE candidate included in event");
            return;
        }
        
        getOtherPc(pc).addIceCandidate(event.getCandidate()).onSuccess(res->onAddIceCandidateSuccess(pc))
                .onFail(e->{
            Log.e((Throwable)e);
            onAddIceCandidateError(pc, (Throwable)e);
        }).onComplete(e->{
            System.out.println(getName(pc)+" ICE candidate:\n"+(event.getCandidate() != null ? event.getCandidate().getCandidate() : "(null"));
            
        });
         
        
    }
    
    private void onSetSessionDescriptionError(Throwable e) {
        System.out.println("Failed to set session description: "+e.getMessage());
    }
    
    private void onAddIceCandidateSuccess(RTCPeerConnection pc) {
        System.out.println(getName(pc)+" addIceCandidate success");
    }

    private void onAddIceCandidateError(RTCPeerConnection pc, Throwable error) {
        System.out.println(getName(pc)+" failed to add ICE Candidate: "+error.getMessage());
    }
    
    private void onIceStateChange(RTCPeerConnection pc, Event event) {
        if (pc != null) {
            System.out.println(getName(pc)+" ICE state: "+pc.getIceConnectionState());
            System.out.println("Ice state change event: " + event);
        }
    }
    
    
    private void hangup() {
        System.out.println("Ending call");
        if (pc1 != null) {
            pc1.close();
            pc1.release();
            pc1 = null;
        }
        if (pc2 != null) {
            pc2.close();
            pc2.release();
            pc2 = null;
        }
        
        hangupButton.setEnabled(false);
        callButton.setEnabled(true);
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
    
}
