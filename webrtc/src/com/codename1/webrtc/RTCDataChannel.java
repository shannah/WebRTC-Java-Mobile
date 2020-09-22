/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCDataChannel interface represents a network channel which can be used for bidirectional peer-to-peer transfers of arbitrary data. Every data channel is associated with an RTCPeerConnection, and each peer connection can have up to a theoretical maximum of 65,534 data channels (the actual limit may vary from browser to browser).
 * 
 * == Events
 * 
 * . {@link #EVENT_BUFFEREDAMOUNTLOW}
 * . {@link #EVENT_CLOSE}
 * . {@link #EVENT_ERROR}
 * . {@link #EVENT_MESSAGE}
 * . {@link #EVENT_OPEN}
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel
 */
public interface RTCDataChannel extends EventTarget, RefCounted {
    
    /**
     * Sent to the channel's onbufferedamountlow event handler when the number of bytes of data in the outgoing data buffer falls below the value specified by bufferedAmountLowThreshold.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/bufferedamountlow_event
     */
    public static final String EVENT_BUFFEREDAMOUNTLOW="bufferedamountlow";
    
    /**
     * Sent to the onclose event handler when the underlying data transport closes.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/close_event
     */
    public static final String EVENT_CLOSE="close";
    
    public static final String EVENT_CLOSING="closing";
    
    /**
     * Sent to the onerror event handler when an error occurs on the data channel.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/error_event
     */
    public static final String EVENT_ERROR="error";
    
    /**
     * Sent to the onmessage event handler when a message has been received from the remote peer. The message contents can be found in the event's data property.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/message_event
     */
    public static final String EVENT_MESSAGE="message";
    
    /**
     * Sent to the onopen event handler when the data channel is first opened, or when an existing data channel's underlying connection re-opens.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/open_event
     */
    public static final String EVENT_OPEN="open";
    
    public RTCDataChannel addOpenListener(OpenListener l);
    public RTCDataChannel removeOpenListener(OpenListener l);
    public RTCDataChannel onopen(OpenListener l);
    
    public RTCDataChannel addMessageListener(MessageListener l);
    public RTCDataChannel removeMessageListener(MessageListener l);
    public RTCDataChannel onmessage(MessageListener l);
    
    public RTCDataChannel addErrorListener(ErrorListener l);
    public RTCDataChannel removeErrorListener(ErrorListener l);
    public RTCDataChannel onerror(ErrorListener l);
    
    public RTCDataChannel addClosingListener(ClosingListener l);
    public RTCDataChannel removeClosingListener(ClosingListener l);
    public RTCDataChannel onclosing(ClosingListener l);
    
    public RTCDataChannel addCloseListener(CloseListener l);
    public RTCDataChannel removeCloseListener(CloseListener l);
    public RTCDataChannel onclose(CloseListener l);
    
    /**
     * The property binaryType on the RTCDataChannel interface is a DOMString which specifies the type of JavaScript object which should be used to represent binary data received on the RTCDataChannel. Values allowed by the WebSocket.binaryType property are also permitted here: "blob" if Blob objects are being used or "arraybuffer" if ArrayBuffer objects are being used. The default is "blob".
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/binaryType
     */
    public String getBinaryType();
    
    /**
     * The read-only RTCDataChannel property bufferedAmount returns the number of bytes of data currently queued to be sent over the data channel.
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/bufferedAmount
     */
    public int getBufferedAmount();
    /**
     * The RTCDataChannel property bufferedAmountLowThreshold is used to specify the number of bytes of buffered outgoing data that is considered "low." The default value is 0.
     * @return
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/bufferedAmountLowThreshold
     */
    public int getBufferedAmountLowThreshold();
    
    /**
     * The read-only RTCDataChannel property id returns an ID number (between 0 and 65,534) which uniquely identifies the RTCDataChannel.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/id
     */
    public int getId();
    
    /**
     * The read-only RTCDataChannel property label returns a DOMString containing a name describing the data channel. These labels are not required to be unique.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/label
     */
    public String getLabel();
    
    /**
     * The read-only RTCDataChannel property maxPacketLifeTime returns the amount of time, in milliseconds, the browser is allowed to take to attempt to transmit a message, as set when the data channel was created, or null.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/maxPacketLifeTime
     */
    public Integer getMaxPacketLifeTime();
    
    /**
     * The read-only RTCDataChannel property maxRetransmits returns the maximum number of times the browser should try to retransmit a message before giving up, as set when the data channel was created, or null, which indicates that there is no maximum.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/maxRetransmits
     */
    public Integer getMaxRetransmits();
    
    /**
     * The read-only RTCDataChannel property negotiated indicates whether the RTCDataChannel's connection was negotiated by the Web app (true) or by the WebRTC layer (false).
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/negotiated
     */
    public boolean isNegotiated();
    
    /**
     * The read-only RTCDataChannel property protocol returns a DOMString containing the name of the subprotocol in use. If no protocol was specified when the data channel was created, then this property's value is "" (the empty string).
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/protocol
     */
    public String getProtocol();
    
    /**
     * The read-only RTCDataChannel property readyState returns an enum of type RTCDataChannelState which indicates the state of the data channel's underlying data connection.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/readyState
     */
    public RTCDataChannelState getReadyState();
    
    /**
     * The RTCDataChannel.close() method closes the RTCDataChannel. Either peer is permitted to call this method to initiate closure of the channel.
     * 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/close
     */
    public void close();
    
    /**
     * The send() method of the RTCDataChannel interface sends data across the data channel to the remote peer.
     * @param bytes 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/send
     */
    public void send(byte[] bytes);
    
    
    /**
     * The send() method of the RTCDataChannel interface sends data across the data channel to the remote peer.
     * @param string 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannel/send
     */
    public void send(String string);
    
    
    public static enum RTCDataChannelState {
        Connecting("connecting"),
        Open("open"),
        Closing("closing"),
        Closed("closed");
        
        private String string;
        
        RTCDataChannelState(String str) {
            this.string = str;
        }

        public boolean matches(String readyStateStr) {
            return string.equals(readyStateStr);
        }
    }
    
    public static interface OpenListener extends EventListener<Event> {}
    public static interface MessageListener extends EventListener<MessageEvent>{}
    public static interface CloseListener extends EventListener<Event> {}
    public static interface ClosingListener extends EventListener<Event> {}
    public static interface BufferedAmountLowListener extends EventListener<Event> {}
    
}
