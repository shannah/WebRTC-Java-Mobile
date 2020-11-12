/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import com.codename1.io.Log;
import com.codename1.util.AsyncResource;
import com.codename1.webrtc.Event;
import com.codename1.webrtc.EventListener;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.Promise;
import com.codename1.webrtc.RTC;
import static com.codename1.webrtc.RTC.getRTC;
import com.codename1.webrtc.RTCAnswerOptions;
import com.codename1.webrtc.RTCConfiguration;
import com.codename1.webrtc.RTCConnectionStateChangeEventListener;
import com.codename1.webrtc.RTCDataChannel;
import com.codename1.webrtc.RTCDataChannelEventListener;
import com.codename1.webrtc.RTCIceCandidate;
import com.codename1.webrtc.RTCIceCandidateEventListener;
import com.codename1.webrtc.RTCIceConnectionStateChangeEventListener;
import com.codename1.webrtc.RTCIceGatheringStateChangeEventListener;
import com.codename1.webrtc.RTCIdentityAssertion;
import com.codename1.webrtc.RTCIdentityResultEventListener;
import com.codename1.webrtc.RTCNegotiationNeededEventListener;
import com.codename1.webrtc.RTCPeerConnection;
import com.codename1.webrtc.RTCRtpReceivers;
import com.codename1.webrtc.RTCRtpSender;
import com.codename1.webrtc.RTCRtpSenders;
import com.codename1.webrtc.RTCRtpTransceivers;
import com.codename1.webrtc.RTCSctpTransport;
import com.codename1.webrtc.RTCSessionDescription;
import com.codename1.webrtc.RTCSignalingStateChangeEventListener;
import com.codename1.webrtc.RTCStatsReport;
import com.codename1.webrtc.RTCTrackEventListener;
import com.codename1.webrtc.RTCVideoElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a connection from the local user to another user.
 * @author shannah
 */
public class RTCClientConnection {
    private Promise<RTCPeerConnection> peerConnection;
    private String remoteUsername;
    private RTCClientSession session;
    private MediaStream stream;
    private RTCClient client;
    private boolean remoteDescriptionSet, localDescriptionSet;
    private Promise<RTC> rtc;
    
    
    private static final RTCPeerConnection.RTCOfferOptions offerOptions = new RTCPeerConnection.RTCOfferOptions()
            .offerToReceiveAudio(true)
            .offerToReceiveVideo(true);
    
    /**
     * Creates a new connection on the given session to the given username.
     * @param session The session on which to make the connection.
     * @param remoteUsername The username of the remote user to connect to.
     */
    RTCClientConnection(RTCClientSession session, String remoteUsername) {
        this.session = session;
        this.client = session.getClient();
        this.remoteUsername = remoteUsername;
        this.peerConnection = getRTC().then(rtc->{
            RTCPeerConnection pc = rtc.newRTCPeerConnection(new RTCConfiguration());
            decoratePeerConnection(pc);
            return pc;
        });
        
        
    }
    
    
    private Promise<MediaStream> getLocalStream() {
        return rtc.then(rtc->{
            
            return session.getLocalStream().then(sessionStream -> {
                if (RTC.getRTC(sessionStream) != rtc) {
                    debug("getLocalStream() :: RTC mismatch :: generating new local stream");
                    return rtc.getUserMedia(new MediaStreamConstraints().audio(true).video(true)).then(strm->{
                        RTCVideoElement videoEl = rtc.createVideo();
                        videoEl.setAutoplay(true);
                        videoEl.applyStyle("position:fixed;width:10px;height:10px;top:0;left:0");
                        videoEl.setSrcObject(strm);
                        rtc.append(videoEl);
                        return strm;
                    });
                } else {
                    debug("getLocalStream() :: Using session's local stream");
                    return sessionStream;
                }
            });
        });
    }
    
    private Promise<RTC> getRTC() {
        if (rtc == null) {
            rtc = RTC.createRTC();
        }
        return rtc;
        
    }
    
    
    private void debug(String msg) {
        System.out.println("[Connection To "+remoteUsername+"] "+msg);
    }
    
    
    /**
     * Gets the session.
     * @return 
     */
    public RTCClientSession getSession() {
        return session;
    }
    
    
    private RTCIceCandidateEventListener iceCandidateEventListener = evt -> {
        if (evt.getCandidate() != null) {
            debug("Sending new ice candidate on client");
            client.sendIceCandidate(this, evt.getCandidate().toJSON());
        }
    };
    
    private RTCTrackEventListener trackListener = evt -> {
        debug("Received track.  Setting the stream");
        if (stream != evt.getStreams().get(0)) {
            setStream(evt.getStreams().get(0));
        }
    };   
    
    private RTCTrackEventListener removeTrackListener = evt -> {
        if (evt.getStreams().get(0).getTracks().size() == 0) {
            setStream(null);
        }
    };
    
    private RTCNegotiationNeededEventListener negotiationNeededListener = evt -> {
        final StringBuilder offerSdp = new StringBuilder();
        peerConnection.then(peerConnection-> {
            return peerConnection.createOffer(offerOptions).then(offer -> {
                debug("[negotiationNeeded] Setting local description " + offer.getSdp());
                offerSdp.setLength(0);
                offerSdp.append(offer.getSdp());
                return peerConnection.setLocalDescription(offer);
            }).then(__ -> {
                localDescriptionSet = true;

                debug("[negotiationNeeded] Sending offer");
                if (!offerSdp.toString().equals(peerConnection.getLocalDescription().getSdp())) {
                    debug("[negotiationNeeded] Offer SDP didn't match that SDP of the local description in the peer connection");
                }
                client.sendOffer(this, peerConnection.getLocalDescription().getSdp());
                return null;
            });
            
        });
    };
    
    private RTCIceConnectionStateChangeEventListener iceConnectionStateChangeListener = evt -> {
        
        peerConnection.then(peerConnection->{
            switch (peerConnection.getIceConnectionState()) {
                case Closed:
                case Failed:
                    undecoratePeerConnection();
                    session.remove(this);
                    break;
            }
            return null;
        });
        
    };

    private void decoratePeerConnection(RTCPeerConnection pc) {
        pc.addIceCandidateListener(iceCandidateEventListener);
        pc.addTrackListener(trackListener);
        pc.addRemoveTrackListener(removeTrackListener);
        pc.addNegotiationNeededListener(negotiationNeededListener);
        pc.addIceConnectionStateChangeListener(iceConnectionStateChangeListener);
         
    }
    
    private void undecoratePeerConnection() {
        peerConnection.then(pc -> {
            pc.removeIceCandidateListener(iceCandidateEventListener);
            pc.removeTrackListener(trackListener);
            pc.removeRemoveTrackListener(removeTrackListener);
            pc.removeNegotiationNeededListener(negotiationNeededListener);
            pc.removeIceConnectionStateChangeListener(iceConnectionStateChangeListener);
            return null;
        });
    }
    
    /**
     * Gets a Promise that resolves to the peer connection 
     * @return 
     */
    public Promise<RTCPeerConnection> getPeerConnection() {

        return peerConnection;
    }
    
    /**
     * Gets the current stream associated with this connection.  This would be the stream received from the remote
     * user.
     * @return 
     */
    public MediaStream getStream() {
        return stream;
    }
    
   
    /**
     * Sets the stream associated with this connection.
     * @param stream 
     */
    public void setStream(MediaStream stream) {
        debug("setStream("+stream+")");
        if (stream != this.stream) {
            debug("stream changed");
            if (this.stream != null) {
                this.stream.release();
            }
            this.stream = stream;
            if (this.stream != null) {
                this.stream.retain();
            }
        }
    }
    
    /**
     * Gets the remote username.
     * @return 
     */
    public String getRemoteUsername() {
        return remoteUsername;
        
    }
    
    /**
     * Gets the local username.s
     * @return 
     */
    public String getLocalUsername() {
        return session.getUsername();
    }
    
    void handleOffer(String sdp) {
         
        debug("handling offer");
        debug("Setting remote description");
        
        peerConnection.then(peerConnection -> {
            return getRTC().then(rtc->{
                return peerConnection.setRemoteDescription(rtc.createSessionDescription(RTCSessionDescription.RTCSdpType.Offer, sdp));
            }).then(__->{
                remoteDescriptionSet = true;

                
                return getLocalStream().then(localStream -> {
                    for (MediaStreamTrack track : localStream.getTracks()) {
                        debug("Adding track: "+track.getId());
                        peerConnection.addTrack(track, localStream);
                    }
                    return peerConnection.createAnswer();
                });
                
                
            })
            .then(desc -> {
                debug("handleOffer() :: Setting localDescription");
                return peerConnection.setLocalDescription((RTCSessionDescription)desc);
            }).then(__ -> {
                localDescriptionSet = true;

                debug("handleOffer():: Sending Answer to client");
                client.sendAnswer(this, peerConnection.getLocalDescription().getSdp());
                return true;
            });
        });
        

    }
    
    void handleAnswer(String sdp) {
        debug("Handling answer");
        debug("Setting remote description");
        getRTC().then(rtc -> {
            return peerConnection.then(peerConnection -> {
                return peerConnection
                        .setRemoteDescription(
                                rtc.createSessionDescription(RTCSessionDescription.RTCSdpType.Answer, sdp))
                        .then(__ -> {
                            remoteDescriptionSet = true;
                            return null;
                         });
            });
        });
    }
    
    
    
    void handleNewIceCandidate(String candidate) {
        debug("Handing new ice candidate");
        peerConnection.then(peerConnection -> {
            return getRTC().then(rtc->{

                return peerConnection.addIceCandidate(rtc.newRTCIceCandidate(candidate));

            });

        });
        
    }
    
    void setSession(RTCClientSession session) {
        this.session = session;
        if (this.session != null) {
            this.client = session.getClient();
        }
    }
    
    public Promise sendOffer() {
        debug("[sendOffer] Sending offer");
        //setPeerConnection(null);
        return getRTC().then(rtc->{
            return peerConnection.then(peerConnection -> {
                return getLocalStream().then(stream -> {
                    
                    return Promise.resolve(null).then(__ -> {
                        for (MediaStreamTrack track : stream.getTracks()) {
                            debug("[sendOffer] Adding track to offer: "+track.getId());
                            peerConnection.addTrack(track, stream);
                        }
                        /*
                        final StringBuilder offerSdp = new StringBuilder();
                        return peerConnection.createOffer(offerOptions).then(offer -> {
                            debug("[sendOffer] Setting local description for offer "+offer);
                            offerSdp.setLength(0);
                            offerSdp.append(offer.getSdp());
                            return peerConnection.setLocalDescription(offer);
                        }).then(___ -> {
                            localDescriptionSet = true;
                            flushCandidatesQueue();
                            debug("[sendOffer] Sending offer to client");
                            if (!offerSdp.toString().equals(peerConnection.getLocalDescription().getSdp())) {
                                debug("[sendOffer] Offer SDP didn't match that SDP of the local description in the peer connection");
                            }
                            client.sendOffer(this, peerConnection.getLocalDescription().getSdp());
                            return null;
                        });
                        */
                        return null;
                    });
                });
            });
        });


    }
    
    
    
    
    private void importStream(AsyncResource<MediaStream> out, MediaStream stream, RTCPeerConnection externalPC, RTCPeerConnection internalPC) {
        

        externalPC.onicecandidate(evt->{

            internalPC.addIceCandidate(evt.getCandidate());
        });
        internalPC.onicecandidate(evt->{
            externalPC.addIceCandidate(evt.getCandidate());
        });
        internalPC.ontrack(evt->{
            if (!out.isDone()) {
                out.complete(evt.getStreams().get(0));
            }
        });

        for (MediaStreamTrack track : stream.getTracks()) {
            externalPC.addTrack(track, stream);
        }

        externalPC.createOffer(new RTCPeerConnection.RTCOfferOptions()
            .offerToReceiveAudio(true)
            .offerToReceiveVideo(true)).then(desc->{
                Promise p1 = externalPC.setLocalDescription(desc);
                Promise p2 = internalPC.setRemoteDescription(desc);
                return Promise.all(p1, p2);
            }).then(res->{
                return internalPC.createAnswer();

            }).then(res->{
                RTCSessionDescription desc = (RTCSessionDescription)res;
                internalPC.setLocalDescription(desc);
                externalPC.setRemoteDescription(desc);
                return null;
            }).except(err->{
                out.error((Throwable)err);
                return null;
            });

       
    }
    
    private Promise<MediaStream> importStream(MediaStream stream) {
        AsyncResource<MediaStream> out = new AsyncResource<MediaStream>();
        RTC externalRTC = RTC.getRTC(stream);
        RTC internalRTC = rtc.getValue();
        if (externalRTC == internalRTC) {
            out.complete(stream);
        } else {
            RTCPeerConnection externalPC = externalRTC.newRTCPeerConnection(new RTCConfiguration());
            RTCPeerConnection internalPC = internalRTC.newRTCPeerConnection(new RTCConfiguration());
            importStream(out, stream, externalPC, internalPC);
        }
        
        return Promise.promisify(out);
    }
    
}
