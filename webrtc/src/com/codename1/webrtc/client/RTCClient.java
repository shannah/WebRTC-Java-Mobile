/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

/**
 *
 * @author shannah
 */
public interface RTCClient extends AutoCloseable {
   
    /**
     * A message sent from one client to another.
     */
    public class ClientMessage extends ActionEvent {

        /**
         * The username of the user that is sending the message.
         * @return the fromUser
         */
        public String getFromUser() {
            return fromUser;
        }

        /**
         * The username of the user who is the recipient of the message.
         * @return the toUser
         */
        public String getToUser() {
            return toUser;
        }

        /**
         * The string content of the message.
         * @return the message
         */
        public String getMessage() {
            return message;
        }
        
        private String fromUser, toUser;
        private String message;
        
        /**
         * Creates a new message.
         * @param client The client used to send the message.
         * @param fromUser The username of the user who is sending the message.
         * @param toUser The username of the user who is the recipient of the message.
         * @param message The message content.
         */
        public ClientMessage(RTCClient client, String fromUser, String toUser, String message) {
            super(client);
            this.fromUser = fromUser;
            this.toUser = toUser;
            this.message = message;
        }

        @Override
        public String toString() {
            return "ClientMessage{from:"+fromUser+", to:"+toUser+", message:"+message+"}";
        }
        
        
    }
    
    /**
     * An "offer" message.
     */
    public static class Offer extends ClientMessage {
        public Offer(RTCClient client, String fromUser, String toUser, String sdp) {
            super(client, fromUser, toUser, sdp);
        }
        
        public String getSdp() {
            return super.getMessage();
        }
    }
    
    /**
     * An "answer" message.
     */
    public static class Answer extends ClientMessage {
        public Answer(RTCClient client, String fromUser, String toUser, String sdp) {
            super(client, fromUser, toUser, sdp);
        }
        
        public String getSdp() {
            return super.getMessage();
        }
    }
    
    /**
     * A "new ice candiate" message.
     */
    public static class NewIceCandidate extends ClientMessage {
        public NewIceCandidate(RTCClient client, String fromUser, String toUser, String candidate) {
            super(client, fromUser, toUser, candidate);
        }
        
        public String getCandidate() {
            return super.getMessage();
        }
    }
    
    /**
     * A "hangup" message.
     */
    public static class Hangup extends ClientMessage {
        public Hangup(RTCClient client, String fromUser, String toUser) {
            super(client, fromUser, toUser, null);
        }
    }
    
    /**
     * Adds a listener to be notified when messages are received from the client.
     * @param l 
     */
    public void addMessageListener(ActionListener<ClientMessage> l);
    
    /**
     * Removes a listener from the client.
     * @param l 
     */
    public void removeMessageListener(ActionListener<ClientMessage> l);

    /**
     * Sends an ice candidate to the given connection.
     * @param connection
     * @param candidate The ice candidate Sdp
     */
    public void sendIceCandidate(RTCClientConnection connection, String candidate);
    
    /**
     * Sends an offer to the given connection.
     * @param connection
     * @param offerSdp 
     */
    public void sendOffer(RTCClientConnection connection, String offerSdp);
    
    /**
     * Sends an answer to the given connection.
     * @param connection
     * @param answerSdp 
     */
    public void sendAnswer(RTCClientConnection connection, String answerSdp);
    
    /**
     * Sends a hangup message to the given connection.
     */
    public void sendHangup(RTCClientConnection connection);

    
}
