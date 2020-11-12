/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.client;

import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.EventDispatcher;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.Promise;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCConfiguration;
import com.codename1.webrtc.RTCPeerConnection;
import java.util.Iterator;


/**
 * Encapsulates a client session.  A single client session may have many connections, all with the same "local" username.
 * @author shannah
 */
public class RTCClientSession implements Iterable<RTCClientConnection> {
    private final RTCClientConnections connections = new RTCClientConnections();
    private String username;
    private RTCClient client;
    private Promise<RTC> rtc;
    private Promise<MediaStream> localStream;
    
    /**
     * Creates a new client session for the given user.
     * @param client The client to connect to.
     * @param username The username to connect as.
     */
    public RTCClientSession(RTCClient client, String username) {
        setClient(client);
        this.rtc = RTC.createRTC();

        this.username = username;
    }
    
    /**
     * Gets a promise resolving to the local stream for the session.
     * @return 
     */
    public Promise<MediaStream> getLocalStream() {
        if (localStream == null) {
            localStream = rtc.then(rtc-> {
                return rtc.getUserMedia(new MediaStreamConstraints().audio(true).video(true));
            }).then(stream -> {
                ((MediaStream)stream).retain();
                return stream;
            });
        }
        return localStream;
    }
   
    /**
     * Gets all connections on this session to the given username.
     * @param username
     * @return 
     */
    public RTCClientConnections getConnectionsToUser(String username) {
        return connections.getConnectionsToUser(username);
    }
    
    
    private EventDispatcher connectionListeners = new EventDispatcher();
    
    
    /**
     * Gets the client associated with this session.
     * @return the client
     */
    public RTCClient getClient() {
        return client;
    }

    /**
     * Adds a connection to this session.  This will trigger a {@link ConnectionAddedEvent} to be fired
     * to all connection listeners.
     * @param conn 
     */
    public void add(RTCClientConnection conn) {
        if (!connections.contains(conn)) {
            
            conn.setSession(this);
            connections.add(conn);
            fireConnectionAdded(conn);
        }
    }
    
    /**
     * Removes a connection from this session.  This will trigger a {@link ConnectionRemovedEvent} to be fired
     * to all connection listeners.
     * @param conn The connection to remove
     * @return True if the connection was removed. False if the connection was not found and thus not removed.
     */
    public boolean remove(RTCClientConnection conn) {
        if (connections.remove(conn)) {
            fireConnectionRemoved(conn);
            return true;
        } else {
            return false;
        }
    }
    
    
    private void debug(String msg) { System.out.println("[Session("+username+")] " + msg);}
    
    private ActionListener<RTCClient.ClientMessage> clientMessageListener = msg -> {
        if (username.equals(msg.getToUser())) {
            if (msg instanceof RTCClient.Answer) {
                debug("Received answer: "+msg);
                // We want to get the connection to the "from" user - i.e. the user
                // who sent us this message
                for (RTCClientConnection conn : getConnectionsToUser(msg.getFromUser()) ) {
                    conn.handleAnswer(msg.getMessage());
                    return;
                }
            }
            if (msg instanceof RTCClient.Offer) {
                debug("Received offer: "+msg);
                RTCClientConnection conn = new RTCClientConnection(this, msg.getFromUser());
                //RTCPeerConnection pc = rtc.newRTCPeerConnection(new RTCConfiguration());
                //conn.setPeerConnection(pc);
                //pc.release();
                add(conn);
                conn.handleOffer(msg.getMessage());
                return;
            }
            
            if (msg instanceof RTCClient.NewIceCandidate) {
                debug("Received new ice candidate "+msg);
                for (RTCClientConnection conn :getConnectionsToUser(msg.getFromUser())) {
                    conn.handleNewIceCandidate(msg.getMessage());
                }
                return;
            }
        }
    };
    
    
    private void fireConnectionAdded(RTCClientConnection conn) {
        if (connectionListeners != null && connectionListeners.hasListeners()) {
            connectionListeners.fireActionEvent(new ConnectionAddedEvent(conn));
        }
    }
    
    private void fireConnectionRemoved(RTCClientConnection conn) {
        if (connectionListeners != null && connectionListeners.hasListeners()) {
            connectionListeners.fireActionEvent(new ConnectionRemovedEvent(conn));
        }
    }
    
    
    
    /**
     * Sets the client to use for this session.
     * @param client the client to set
     */
    public void setClient(RTCClient client) {
        if (this.client != null) {
            this.client.removeMessageListener(clientMessageListener);
        }
        this.client = client;
        if (this.client != null) {
            this.client.addMessageListener(clientMessageListener);
        }
    }
    
    
    /**
     * Sets the username to connect as in this session.
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the username connected as in this session.
     * @return 
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Adds a connection listener to this session.
     * @param l 
     */
    public void addConnectionListener(ActionListener<ConnectionEvent> l) {
        connectionListeners.addListener(l);
    }
    
    /**
     * Removes a connection listener from this session.
     * @param l 
     */
    public void removeConnectionListener(ActionListener<ConnectionEvent> l) {
        connectionListeners.removeListener(l);
    }
    

    /**
     * Gets iterator to iterate over all of the connections in this session.
     * @return 
     */
    @Override
    public Iterator<RTCClientConnection> iterator() {
        return connections.iterator();
    }
    
    
    
    /**
     * Encapsulates events related to this session, such as when connections are added or removed.
     */
    public static class ConnectionEvent extends ActionEvent {
        private RTCClientConnection connection;
        public ConnectionEvent(RTCClientConnection connection) {
            super(connection.getSession());
            this.connection = connection;
        }
        
        public RTCClientSession getSession() {
            return (RTCClientSession)getSource();
        }
        
        public RTCClientConnection getConnection() {
            return connection;
        }
    }
    
    /**
     * Event fired when a connection is added to this session.
     */
    public static class ConnectionAddedEvent extends ConnectionEvent {
        public ConnectionAddedEvent(RTCClientConnection connection) {
            super(connection);
        }
    }
    
    /**
     * Event fired when a connection is removed from this session.
     */
    public static class ConnectionRemovedEvent extends ConnectionEvent {
        public ConnectionRemovedEvent(RTCClientConnection connection) {
            super(connection);
        }
    }
    
    /**
     * Creates a new connection to the given username, and adds the connection to this session.
     * This will trigger a {@link ConnectionAddedEvent} and it will send an over to the remote user.
     * @param remoteUsername The username to connect to. An offer will be sent to this user.
     * @return 
     */
    public RTCClientConnection connect(String remoteUsername) {
        RTCClientConnection conn = new RTCClientConnection(this, remoteUsername);
        add(conn);
        conn.sendOffer();
        return conn;
    }
   
    
}
