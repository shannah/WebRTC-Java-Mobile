/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.EventDispatcher;

/**
 * An in-memory mock client used for testing purposes.
 * @author shannah
 */
public class MockRTCClient implements RTCClient {
    private EventDispatcher messageListeners = new EventDispatcher();

    @Override
    public void addMessageListener(ActionListener<ClientMessage> l) {
        messageListeners.addListener(l);
    }

    @Override
    public void removeMessageListener(ActionListener<ClientMessage> l) {
        messageListeners.removeListener(l);
    }

    @Override
    public void sendIceCandidate(RTCClientConnection connection, String candidate) {
        NewIceCandidate evt = new NewIceCandidate(this, connection.getLocalUsername(), connection.getRemoteUsername(), candidate);
        messageListeners.fireActionEvent(evt);
    }

    @Override
    public void sendOffer(RTCClientConnection connection, String offerSdp) {
        Offer offer = new Offer(this, connection.getLocalUsername(), connection.getRemoteUsername(), offerSdp);
        System.out.println("[MockRTCClient] Firing offer "+offer);
        messageListeners.fireActionEvent(offer);
    }

    @Override
    public void sendAnswer(RTCClientConnection connection, String answerSdp) {
        Answer answer = new Answer(this, connection.getLocalUsername(), connection.getRemoteUsername(), answerSdp);
        System.out.println("[MockRTCClient] Firing answer "+answer);
        messageListeners.fireActionEvent(answer);
        
    }
    
}
