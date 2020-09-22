/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import static com.codename1.io.Log.e;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.webrtc.Event;
import com.codename1.webrtc.MediaDeviceInfo;
import com.codename1.webrtc.MediaDeviceInfo.Kind;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.MediaStreamTracks;
import com.codename1.webrtc.MessageEvent;
import com.codename1.webrtc.Promise;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCConfiguration;
import com.codename1.webrtc.RTCDataChannel;
import com.codename1.webrtc.RTCDataChannelEvent;
import com.codename1.webrtc.RTCPeerConnection;
import com.codename1.webrtc.RTCPeerConnection.RTCDataChannelInit;
import com.codename1.webrtc.RTCPeerConnection.RTCOfferOptions;
import com.codename1.webrtc.RTCPeerConnectionIceEvent;
import com.codename1.webrtc.RTCSessionDescription;
import com.codename1.webrtc.RTCSessionDescriptionInit;
import com.codename1.webrtc.RTCTrackEvent;
import com.codename1.webrtc.RTCVideoElement;
import java.util.Timer;

/**
 * This sample was adapted from https://webrtc.github.io/samples/src/content/peerconnection/munge-sdp/
 * 
 * The source for that sample is available at https://github.com/webrtc/samples/blob/gh-pages/src/content/peerconnection/munge-sdp/js/main.js
 * 
 * @author shannah
 */
public class MungeSdpDemo extends Form implements AutoCloseable {
    private RTC rtc;
    private Button getMediaButton = new Button("Get Media");
    private Button createPeerConnectionButton = new Button("Create Peer Connection");
    private Button createOfferButton = new Button("Create Offer");
    private Button setOfferButton = new Button("Set Offer");
    private Button createAnswerButton = new Button("Create Answer");
    private Button setAnswerButton = new Button("Set Answer");
    private Button hangupButton = new Button("Hangup");
    
    private TextArea offerSdpTextarea = new TextArea();
    {
        offerSdpTextarea.setRows(10);
    }
    private TextArea answerSdpTextarea = new TextArea();
    {
        answerSdpTextarea.setRows(10);
    }
    
    private ComboBox audioSelect = new ComboBox();
    private ComboBox videoSelect = new ComboBox();
    
    private RTCVideoElement localVideo;
    private RTCVideoElement remoteVideo;
    
    private Container selectSourceDiv = new Container(new BorderLayout());
    
    private RTCPeerConnection localPeerConnection, remotePeerConnection;
    private MediaStream localStream;
    private RTCDataChannel sendChannel;
    private RTCDataChannel receiveChannel;
    
    private RTCDataChannelInit dataChannelOptions = new RTCDataChannelInit();
    {
        dataChannelOptions.setOrdered(true);
    }
    private int dataChannelCounter = 0;
    private RTCOfferOptions offerOptions = new RTCOfferOptions();
    {
        offerOptions.offerToReceiveAudio(true);
        offerOptions.offerToReceiveVideo(true);
    }
    private Object dataChannelDataReceived;
    
    private Timer sendDataLoop;
    
    private class Option {
        private String value="";
        private String text="";

        @Override
        public String toString() {
            return text;
        }
        
        
    }
    
    public MungeSdpDemo() {
        super("Munge SDP Demo", new BorderLayout());
        Form hi = this;
        String intro = "This sample was adapted from the \"Munge SDP Demo\" on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        GridLayout grid = new GridLayout(5);
        
        selectSourceDiv.add(BorderLayout.NORTH, new SpanLabel("Select an audio & video source, then click Get media"));
        selectSourceDiv.add(BorderLayout.CENTER, BoxLayout.encloseY(
                FlowLayout.encloseIn(new Label("Audio Source:"), audioSelect),
                FlowLayout.encloseIn(new Label("Video Source:"), videoSelect)
        ));
        selectSourceDiv.setVisible(false);
        selectSourceDiv.setHidden(true);
        
        Container north = BoxLayout.encloseY(
                selectSourceDiv,
                FlowLayout.encloseIn(new SpanLabel(intro), viewSource),
                FlowLayout.encloseIn(getMediaButton, createPeerConnectionButton, createOfferButton, setOfferButton,
                        createAnswerButton, setAnswerButton, hangupButton)
                
        );
        
        Container mainContent = new Container(BoxLayout.y());
        mainContent.add(GridLayout.encloseIn(2, new Label("Local"), new Label("Remote")));
        Container rtcPlaceholder = new Container(new BorderLayout());
        rtcPlaceholder.setPreferredH(CN.getDisplayHeight()/2);
        mainContent.add(rtcPlaceholder);
        mainContent.add(
                GridLayout.encloseIn(CN.isTablet() ? 1 : 2, 
                        BoxLayout.encloseY(
                                new Label("Offer SDP"), 
                                offerSdpTextarea
                       ),
                        BoxLayout.encloseY(
                                new Label("Answer SDP"),
                                answerSdpTextarea
                        )
                )
        );
        
        Container center = new Container(BoxLayout.y());
        center.setScrollableY(true);
        center.add(north);
        center.add(mainContent);
        
        Container instructions = new Container(BoxLayout.y());
        instructions.add(new Label("View the console to see logging."));
        instructions.add(new SpanLabel("The RTCPeerConnection objects localPeerConnection and remotePeerConnection are in global scope, so you can inspect them in the console as well."));
        center.add(instructions);
        add(BorderLayout.CENTER, center);
        //add(BorderLayout.NORTH, north);
        
                
        
        
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/MungeSdpDemo.java"));
        Log.p("About to create RTC");
        RTC.createRTC().ready(rtc->{
            Log.p("Inside createRTC() ready callback");
            this.rtc = rtc;
            Component videoCom = rtc.getVideoComponent();
            videoCom.setPreferredH(CN.getDisplayHeight()/2);
            videoCom.setPreferredW(CN.getDisplayWidth());
            rtcPlaceholder.add(BorderLayout.CENTER, rtc.getVideoComponent());
            hi.revalidate();
            init();
            
        });
    }

    private void init() {
        Log.p("Form loaded");
        rtc.enumerateDevices()
                .onSuccess(devices->gotSources((MediaDeviceInfo[])devices))
                .onFail(error->Log.e((Throwable)error));
        
        getMediaButton.addActionListener(evt->getMedia());
        createPeerConnectionButton.addActionListener(evt->createPeerConnection());
        createOfferButton.addActionListener(evt->createOffer());
        setOfferButton.addActionListener(evt->setOffer());
        createAnswerButton.addActionListener(evt->createAnswer());
        setAnswerButton.addActionListener(evt->setAnswer());
        hangupButton.addActionListener(evt->hangup());
        
        $(audioSelect, videoSelect).addActionListener(evt->getMedia());
        
        localVideo = rtc.createVideo();
        localVideo.setAutoplay(true);
        localVideo.applyStyle("max-width:50vw");
        rtc.append(localVideo);
        remoteVideo = rtc.createVideo();
        remoteVideo.setAutoplay(true);
        remoteVideo.applyStyle("max-width:50vw");
        rtc.append(remoteVideo);
        
        
    }
    
    private void getMedia() {
        getMediaButton.setEnabled(false);
        createPeerConnectionButton.setEnabled(true);
        if (localStream != null) {
            localVideo.setSrcObject(null);
            for (MediaStreamTrack track : localStream.getTracks()) {
                track.stop();
            }
        }
        String audioSource = audioSelect.getSelectedItem() == null ? null : ((Option)audioSelect.getSelectedItem()).value;
        Log.p("Selected audio source: "+audioSource);
        String videoSource = videoSelect.getSelectedItem() == null ? null : ((Option)videoSelect.getSelectedItem()).value;
        
        MediaStreamConstraints constraints = new MediaStreamConstraints();
        constraints.audio().optional("sourceId", audioSource);
        constraints.video().optional("sourceId", videoSource);
        
        Log.p("Requested local stream with constraints "+constraints.toJSON());
        rtc.getUserMedia(constraints)
                .onSuccess(stream->gotStream((MediaStream)stream))
                .onFail(e->{
                    Log.p("GetUserMedia error: " + ((Throwable)e).getMessage());
                    Log.e((Throwable)e);
                });
        
    }
    
    private void gotStream(MediaStream stream) {
        Log.p("Received local stream");
        localVideo.setSrcObject(stream);
        localStream = stream;
        localStream.retain();
    }
    
    private void createPeerConnection() {
        createPeerConnectionButton.setEnabled(false);
        createOfferButton.setEnabled(true);
        createAnswerButton.setEnabled(true);
        setOfferButton.setEnabled(true);
        setAnswerButton.setEnabled(true);
        hangupButton.setEnabled(true);
        Log.p("Starting call");
        MediaStreamTracks videoTracks = localStream.getVideoTracks();
        MediaStreamTracks audioTracks = localStream.getAudioTracks();
        
        if (videoTracks.size() > 0) {
            MediaStreamTrack videoTrack = videoTracks.get(0);
            Log.p("Using video device "+videoTrack.getLabel());
        }
        
        if (audioTracks.size() > 0) {
            MediaStreamTrack audioTrack = audioTracks.get(0);
            Log.p("Using audio device: "+audioTrack.getLabel());
        }
        RTCConfiguration servers = null;
        localPeerConnection = rtc.newRTCPeerConnection(servers);
        
        Log.p("Created local peer connection object localPeerConnection");
        
        localPeerConnection.onicecandidate(e->onIceCandidate(localPeerConnection, e));
        RTCDataChannel sendChannel = localPeerConnection.createDataChannel("sendDataChannel", dataChannelOptions);
        sendChannel.retain();
        sendChannel.onopen(e->onSendChannelStateChange())
            .onclose(e->onSendChannelStateChange())
            .onerror(e->onSendChannelStateChange());
        
        remotePeerConnection = rtc.newRTCPeerConnection(servers);
        Log.p("Created remote peer connection object remotePeerConnection");
        remotePeerConnection.onicecandidate(e->onIceCandidate(remotePeerConnection, e));
        remotePeerConnection.ontrack(e->gotRemoteStream(e));
        remotePeerConnection.ondatachannel(e->receiveChannelCallback(e));
        
        for (MediaStreamTrack track : localStream.getTracks()) {
            localPeerConnection.addTrack(track, localStream);
        }
        Log.p("Adding local stream to peer connection");
        
        
        
    }
    
    private void onSetSessionDescriptionSuccess() {
        Log.p("Set session description success.");
    }
    
    private void onSetSessionDescriptionError(Throwable error) {
        Log.p("Failed to set session description: "+error.getMessage());
        Log.e(error);
    }
    
    private void createOffer() {
        localPeerConnection.createOffer(offerOptions)
                .onSuccess(offer->gotDescription1(offer))
                .onFail(e->onCreateSessionDescriptionError((Throwable)e));
    }
    
    private void onCreateSessionDescriptionError(Throwable error) {
        Log.p("Failed to create session description: "+error.getMessage(), Log.ERROR);
        Log.e(error);
    }
    
    private static String join(String delimiter, String... parts) {
        
        StringBuilder out = new StringBuilder();
        int len = parts.length;
        for (int i=0; i<len; i++) {
            if (i>0) {
                out.append(delimiter);
            }
            out.append(parts[i]);
        }
        return out.toString();
    }
    
    private Promise setOffer() {
        // Restore the SDP from the textarea. Ensure we use CRLF which is what is generated
        // even though https://tools.ietf.org/html/rfc4566#section-5 requires
        // parsers to handle both LF and CRLF.
        String sdp = offerSdpTextarea.getText();
        String[] parts = Util.split(sdp, "\n");
        int len = parts.length;
        for (int i=0; i<len; i++) {
            parts[i] = parts[i].trim();
        }
        sdp = join("\r\n", parts);
        
        RTCSessionDescriptionInit offer = new RTCSessionDescriptionInit()
                .type(RTCSessionDescription.RTCSdpType.Offer)
                .sdp(sdp);
        
        Log.p("Modified Offer from localPeerConnection\n"+sdp);
        Log.p("localPeerConnection.setLocalDescription("+offer+")");
        return localPeerConnection.setLocalDescription(offer)
                .onSuccess(ignore->onSetSessionDescriptionSuccess())
                .onFail(e->onSetSessionDescriptionError((Throwable)e))
                .then(arg->{
                    Log.p("remotePeerConnection.setRemoteDescription("+offer+")");
                    return remotePeerConnection.setRemoteDescription(offer);
                })
                .onSuccess(ignore->{
                    onSetSessionDescriptionSuccess();
                }).onFail(e->{
                    onSetSessionDescriptionError((Throwable)e);
                });
        
        
    }
    
    private void gotDescription1(RTCSessionDescription desc) {
        offerSdpTextarea.setEnabled(true);
        offerSdpTextarea.setText(desc.getSdp());
    }
    
    private Promise createAnswer() {
        return remotePeerConnection.createAnswer()
                .onSuccess(answer->gotDescription2(answer))
                .onFail(e->onCreateSessionDescriptionError((Throwable)e));
    }
    
    private Promise setAnswer() {
        // Restore the SDP from the textarea. Ensure we use CRLF which is what is generated
        // even though https://tools.ietf.org/html/rfc4566#section-5 requires
        // parsers to handle both LF and CRLF.
        String sdp = answerSdpTextarea.getText();
        String[] parts = Util.split(sdp, "\n");
        int len = parts.length;
        for (int i=0; i<len; i++) {
            parts[i] = parts[i].trim();
        }
        sdp = join("\r\n", parts);
        final String fSdp = sdp;
        RTCSessionDescriptionInit answer = new RTCSessionDescriptionInit()
                .type(RTCSessionDescription.RTCSdpType.Answer)
                .sdp(sdp);
        Log.p("remotePeerConnection.setLocalDescription("+answer+")");
        return remotePeerConnection.setLocalDescription(answer)
                .onSuccess(ignore->{
                    onSetSessionDescriptionSuccess();
                }).onFail(e->{
                    onSetSessionDescriptionError((Throwable)e);
                }).always(ignore->{
                    Log.p("Modified answer from remote peer connection\n" + fSdp);
                    Log.p("localPeerConnection.setRemoteDescription("+answer+")");
                    return localPeerConnection.setRemoteDescription(answer);
                }).onSuccess(ignore->onSetSessionDescriptionSuccess())
                .onFail(e->onSetSessionDescriptionError((Throwable)e));
    }
    
    private void gotDescription2(RTCSessionDescription description) {
        answerSdpTextarea.setEnabled(true);
        answerSdpTextarea.setText(description.getSdp());
    }
    
    public void sendData() {
        switch (sendChannel.getReadyState()) {
            case Open:
                sendChannel.send(dataChannelCounter+"");
                Log.p("DataChannel send counter: "+dataChannelCounter);
                dataChannelCounter++;
        }
        
    }
    
    private void hangup() {
        remoteVideo.setSrcObject(null);
        Log.p("Ending call");
        for (MediaStreamTrack track : localStream.getTracks()) {
            track.stop();
        }
        if (sendChannel != null) {
            sendChannel.close();
            sendChannel.release();
            sendChannel = null;
        }
        if (receiveChannel != null) {
            receiveChannel.close();
            receiveChannel.release();
            receiveChannel = null;
        }
        if (localPeerConnection != null) {
            localPeerConnection.close();
            localPeerConnection = null;
        }
        if (remotePeerConnection != null) {
            remotePeerConnection.close();
            remotePeerConnection = null;
        }

        offerSdpTextarea.setEnabled(false);
        answerSdpTextarea.setEnabled(false);
        getMediaButton.setEnabled(true);
        createPeerConnectionButton.setEnabled(false);
        createOfferButton.setEnabled(false);
        setOfferButton.setEnabled(false);
        createAnswerButton.setEnabled(false);
        setAnswerButton.setEnabled(false);
        hangupButton.setEnabled(false);
        
    }
    
    private void gotRemoteStream(Event evt) {
        RTCTrackEvent e = (RTCTrackEvent)evt;
        if (remoteVideo.getSrcObject() != e.getStreams().get(0)) {
            remoteVideo.setSrcObject(e.getStreams().get(0));
            Log.p("Received remote stream");
        }
    }
    
    private RTCPeerConnection getOtherPc(RTCPeerConnection pc) {
        return (pc == localPeerConnection) ? remotePeerConnection : localPeerConnection;
    }
    
    private String getName(RTCPeerConnection pc) {
        return (pc == localPeerConnection) ? "localPeerConnection" : "remotePeerConnection";
    }
    
    private Promise onIceCandidate(RTCPeerConnection pc, Event e) {
        RTCPeerConnectionIceEvent event = (RTCPeerConnectionIceEvent)e;
        return getOtherPc(pc).addIceCandidate(event.getCandidate())
                .onSuccess(ignore->onAddIceCandidateSuccess(pc))
                .onFail(error->onAddIceCandidateError(pc, (Throwable)error))
                .onComplete(ignore->Log.p(getName(pc)+" ICE candidate:\n"+(event.getCandidate() != null ? event.getCandidate().getCandidate() : "(null)")));
       
    }
    
    private void onAddIceCandidateSuccess(RTCPeerConnection pc) {
        Log.p("AddIceCandidate success.");
    }
    
    private void onAddIceCandidateError(RTCPeerConnection pc, Throwable e) {
        Log.p("Failed to add Ice Candidate: "+e.getMessage(), Log.ERROR);
        Log.e(e);
    }
    
    private void receiveChannelCallback(RTCDataChannelEvent event) {
        Log.p("Receive Channel Callback");
        
        receiveChannel = event.getChannel()
            .onmessage(evt->onReceiveMessageCallback(evt))
            .onopen(evt->onReceiveChannelStateChange())
            .onclose(evt->onReceiveChannelStateChange());
        
    }
    
    
    private void onReceiveMessageCallback(MessageEvent event) {
        Object dataChannelDataReceived = event.getData();
        Log.p("DataChannel receive counter: "+dataChannelDataReceived);
        
    }
    
    private void onSendChannelStateChange() {
        if (sendChannel == null) {
            return;
        }
        Log.p("Send channel state is "+sendChannel.getReadyState());
        switch (sendChannel.getReadyState()) {
            case Open:
                sendDataLoop = CN.setInterval(1000, ()->sendData());
            default:
                if (sendDataLoop != null) {
                    sendDataLoop.cancel();
                    sendDataLoop = null;
                }
        }
    }
    
    
    
    private void onReceiveChannelStateChange() {
        Log.p("Receive channel state is "+receiveChannel.getReadyState());
    }
   
    
    
    private void gotSources(MediaDeviceInfo[] sourceInfos) {
        selectSourceDiv.setVisible(true);
        selectSourceDiv.setHidden(false);
        int audioCount = 0;
        int videoCount = 0;
        for (int i=0; i < sourceInfos.length; i++) {
            Option option = new Option();
            option.value = sourceInfos[i].getDeviceId();
            option.text = sourceInfos[i].getLabel();
            switch (sourceInfos[i].getKind()) {
                case AudioInput: {
                    audioCount++;
                    if ("".equals(option.text)) {
                        option.text = "Audio "+audioCount;
                    }
                    audioSelect.getModel().addItem(option);
                    break;
                }
                case VideoInput: {
                    videoCount++;
                    if ("".equals(option.text)) {
                        option.text = "Video "+videoCount;
                    }
                    videoSelect.getModel().addItem(option);
                    break;
                }
                default:
                    Log.p("Unknown source: "+sourceInfos[i]+", kind="+sourceInfos[i].getKind());
                
            }
            
        }
        revalidateWithAnimationSafety();
    }
    
    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
}
