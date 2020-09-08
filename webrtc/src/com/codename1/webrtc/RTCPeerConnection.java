/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The RTCPeerConnection interface represents a WebRTC connection between the
 * local computer and a remote peer. It provides methods to connect to a remote
 * peer, maintain and monitor the connection, and close the connection once it's
 * no longer needed.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection
 */
public interface RTCPeerConnection extends EventTarget, RefCounted {

    /**
     * The RTCPeerConnection.onconnectionstatechange property specifies an
     * EventHandler which is called to handle the connectionstatechange event
     * when it occurs on an instance of RTCPeerConnection. This happens whenever
     * the aggregate state of the connection changes.
     */
    public static final String EVENT_CONNECTIONSTATECHANGE = "connectionstatechange";

    /**
     * The RTCPeerConnection.ondatachannel property is an EventHandler which
     * specifies a function which is called when the datachannel event occurs on
     * an RTCPeerConnection. This event, of type RTCDataChannelEvent, is sent
     * when an RTCDataChannel is added to the connection by the remote peer
     * calling createDataChannel().
     */
    public static final String EVENT_DATACHANNEL = "datachannel";

    /**
     * The RTCPeerConnection property onicecandidate property is an EventHandler
     * which specifies a function to be called when the icecandidate event
     * occurs on an RTCPeerConnection instance. This happens whenever the local
     * ICE agent needs to deliver a message to the other peer through the
     * signaling server.
     */
    public static final String EVENT_ICECANDIDATE = "icecandidate";

    /**
     * The RTCPeerConnection.oniceconnectionstatechange property is an event
     * handler which specifies a function to be called when the
     * iceconnectionstatechange event is fired on an RTCPeerConnection instance.
     * This happens when the state of the connection's ICE agent, as represented
     * by the iceConnectionState property, changes.
     */
    public static final String EVENT_ICECONNECTIONSTATECHANGE = "iceconnectionstatechange";

    /**
     * The RTCPeerConnection.onicegatheringstatechange property is an
     * EventHandler which specifies a function to be called when the
     * icegatheringstatechange event is sent to an RTCPeerConnection instance.
     * This happens when the ICE gathering state - that is, whether or not the ICE
     * agent is actively gathering candidates - changes.
     */
    public static final String EVENT_ICEGATHERINGSTATECHANGE = "icegatheringstatechange";

    /**
     * The RTCPeerConnection.onidentityresult event handler is a property
     * containing the code to execute when the identityresult event, of type
     * RTCIdentityEvent, is received by this RTCPeerConnection. Such an event is
     * sent when an identity assertion is generated, via getIdentityAssertion()
     * or during the creation of an offer or an answer.
     */
    public static final String EVENT_IDENTITYRESULT = "identityresult";

    /**
     * The RTCPeerConnection interface's onnegotiationneeded property is an
     * EventListener which specifies a function which is called to handle the
     * negotiationneeded event when it occurs on an RTCPeerConnection instance.
     * This event is fired when a change has occurred which requires session
     * negotiation. This negotiation should be carried out as the offerer,
     * because some session changes cannot be negotiated as the answerer.
     */
    public static final String EVENT_NEGOTIATIONNEEDED = "negotiationneeded";

    /**
     * The onsignalingstatechange event handler property of the
     * RTCPeerConnection interface specifies a function to be called when the
     * signalingstatechange event occurs on an RTCPeerConnection interface.
     */
    public static final String EVENT_SIGNALINGSTATECHANGE = "signalingstatechange";

    /**
     * The RTCPeerConnection property ontrack is an EventHandler which specifies
     * a function to be called when the track event occurs, indicating that a
     * track has been added to the RTCPeerConnection.
     */
    public static final String EVENT_TRACK = "track";

    /**
     * The read-only RTCPeerConnection property canTrickleIceCandidates returns
     * a Boolean which indicates whether or not the remote peer can accept
     * trickled ICE candidates.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/canTrickleIceCandidates
     */
    public boolean canTrickleIceCandidates();

    /**
     * The read-only connectionState property of the RTCPeerConnection interface
     * indicates the current state of the peer connection by returning one of
     * the string values specified by the enum RTCPeerConnectionState.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/connectionState
     */
    public RTCPeerConnectionState getConnectionState();

    /**
     * The read-only property RTCPeerConnection.currentLocalDescription returns
     * an RTCSessionDescription object describing the local end of the
     * connection as it was most recently successfully negotiated since the last
     * time the RTCPeerConnection finished negotiating and connecting to a
     * remote peer. Also included is a list of any ICE candidates that may
     * already have been generated by the ICE agent since the offer or answer
     * represented by the description was first instantiated.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/currentLocalDescription
     */
    public RTCSessionDescription getCurrentLocalDescription();

    /**
     * The read-only property RTCPeerConnection.currentRemoteDescription returns
     * an RTCSessionDescription object describing the remote end of the
     * connection as it was most recently successfully negotiated since the last
     * time the RTCPeerConnection finished negotiating and connecting to a
     * remote peer. Also included is a list of any ICE candidates that may
     * already have been generated by the ICE agent since the offer or answer
     * represented by the description was first instantiated.
     *
     * @return
     * @see
     * https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection/currentRemoteDescription
     */
    public RTCSessionDescription getCurrentRemoteDescription();

    /**
     * The read-only property RTCPeerConnection.iceConnectionState returns an
     * enum of type RTCIceConnectionState which state of the ICE agent
     * associated with the RTCPeerConnection.
     *
     * @return
     */
    public RTCIceConnectionState getIceConnectionState();

    /**
     * The read-only property RTCPeerConnection.iceGatheringState returns an
     * enum of type RTCIceGatheringState that describes connection's ICE
     * gathering state. This lets you detect, for example, when collection of
     * ICE candidates has finished.
     *
     * @return
     */
    public RTCIceGatheringState getIceGatheringState();

    /**
     * The read-only property RTCPeerConnection.localDescription returns an
     * RTCSessionDescription describing the session for the local end of the
     * connection. If it has not yet been set, this is null.
     *
     * @return
     */
    public RTCSessionDescription getLocalDescription();

    /**
     * The read-only RTCPeerConnection property peerIdentity returns a
     * JavaScript Promise that resolves to an RTCIdentityAssertion which
     * contains a DOMString identifying the remote peer.
     *
     * @return
     */
    public RTCIdentityAssertion getPeerIdentity();

    /**
     * The read-only property RTCPeerConnection.pendingLocalDescription returns
     * an RTCSessionDescription object describing a pending configuration change
     * for the local end of the connection. This does not describe the
     * connection as it currently stands, but as it may exist in the near
     * future. Use RTCPeerConnection.currentLocalDescription or
     * RTCPeerConnection.localDescription to get the current state of the
     * endpoint. For details on the difference, see Pending and current
     * descriptions in WebRTC connectivity.
     *
     * @return
     */
    public RTCSessionDescription getPendingLocalDescription();

    /**
     * The read-only property RTCPeerConnection.pendingRemoteDescription returns
     * an RTCSessionDescription object describing a pending configuration change
     * for the remote end of the connection. This does not describe the
     * connection as it currently stands, but as it may exist in the near
     * future. Use RTCPeerConnection.currentRemoteDescription or
     * RTCPeerConnection.remoteDescription to get the current session
     * description for the remote endpoint. For details on the difference, see
     * Pending and current descriptions in WebRTC connectivity.
     *
     * @return
     */
    public RTCSessionDescription getPendingRemoteDescription();

    /**
     * The read-only property RTCPeerConnection.remoteDescription returns a
     * RTCSessionDescription describing the session (which includes
     * configuration and media information) for the remote end of the
     * connection. If this hasn't been set yet, this is null.
     *
     * @return
     */
    public RTCSessionDescription getRemoteDescription();

    /**
     * The read-only sctp property on the RTCPeerConnection interface returns an
     * RTCSctpTransport describing the SCTP transport over which SCTP data is
     * being sent and received. If SCTP hasn't been negotiated, this value is
     * null.
     *
     * @return
     */
    public RTCSctpTransport getSctp();

    /**
     * The read-only signalingState property on the RTCPeerConnection interface
     * returns one of the string values specified by the RTCSignalingState enum;
     * these values describe the state of the signaling process on the local end
     * of the connection while connecting or reconnecting to another peer. See
     * Signaling in Lifetime of a WebRTC session for more details about the
     * signaling process.
     *
     * @return
     */
    public RTCSignalingState getSignalingState();

    /**
     * When a web site or app using RTCPeerConnection receives a new ICE
     * candidate from the remote peer over its signaling channel, it delivers
     * the newly-received candidate to the browser's ICE agent by calling
     * RTCPeerConnection.addIceCandidate().
     *
     * @param candidate
     * @return
     */
    public Promise addIceCandidate(RTCIceCandidate candidate);

    /**
     * The RTCPeerConnection method addTrack() adds a new media track to the set
     * of tracks which will be transmitted to the other peer.
     *
     * @param track
     * @param stream
     * @return
     */
    public RTCRtpSender addTrack(MediaStreamTrack track, MediaStream... stream);

    /**
     * The RTCPeerConnection.close() method closes the current peer connection.
     */
    public void close();

    /**
     * The createAnswer() method on the RTCPeerConnection interface creates an
     * SDP answer to an offer received from a remote peer during the
     * offer/answer negotiation of a WebRTC connection. The answer contains
     * information about any media already attached to the session, codecs and
     * options supported by the browser, and any ICE candidates already
     * gathered. The answer is delivered to the returned Promise, and should
     * then be sent to the source of the offer to continue the negotiation
     * process.
     *
     * @return
     */
    public Promise<RTCSessionDescription> createAnswer();

    /**
     * The createAnswer() method on the RTCPeerConnection interface creates an
     * SDP answer to an offer received from a remote peer during the
     * offer/answer negotiation of a WebRTC connection. The answer contains
     * information about any media already attached to the session, codecs and
     * options supported by the browser, and any ICE candidates already
     * gathered. The answer is delivered to the returned Promise, and should
     * then be sent to the source of the offer to continue the negotiation
     * process.
     *
     * @param options
     * @return
     */
    public Promise<RTCSessionDescription> createAnswer(RTCAnswerOptions options);

    /**
     * The createDataChannel() method on the RTCPeerConnection interface creates
     * a new channel linked with the remote peer, over which any kind of data
     * may be transmitted.
     *
     * @param label
     * @param options
     * @return
     */
    public RTCDataChannel createDataChannel(String label, RTCDataChannelInit options);

    /**
     * The createOffer() method of the RTCPeerConnection interface initiates the
     * creation of an SDP offer for the purpose of starting a new WebRTC
     * connection to a remote peer.
     *
     * @param options
     * @return
     */
    public Promise<RTCSessionDescription> createOffer(RTCOfferOptions options);

    /**
     * The RTCPeerConnection.getReceivers() method returns an array of
     * RTCRtpReceiver objects, each of which represents one RTP receiver. Each
     * RTP receiver manages the reception and decoding of data for a
     * MediaStreamTrack on an RTCPeerConnection
     *
     * @return
     */
    public RTCRtpReceivers getReceivers();

    /**
     * The RTCPeerConnection method getSenders() returns an array of
     * RTCRtpSender objects, each of which represents the RTP sender responsible
     * for transmitting one track's data.
     *
     * @return
     */
    public RTCRtpSenders getSenders();

    /**
     * The RTCPeerConnection method getStats() returns a promise which resolves
     * with data providing statistics about either the overall connection or
     * about the specified MediaStreamTrack.
     *
     * @param selector
     * @return
     */
    public Promise<RTCStatsReport> getStats(MediaStreamTrack selector);

    /**
     * The RTCPeerConnection.getStreamById() method returns the MediaStream with
     * the given id that is associated with local or remote end of the
     * connection. If no stream matches, it returns null.
     *
     * @param id
     * @return
     */
    public MediaStream getStreamById(String id);

    /**
     * The RTCPeerConnection interface's getTransceivers() method returns a list
     * of the RTCRtpTransceiver objects being used to send and receive data on
     * the connection.
     *
     * @return
     */
    public RTCRtpTransceivers getTransceivers();

    /**
     * The RTCPeerConnection.removeTrack() method tells the local end of the
     * connection to stop sending media from the specified track, without
     * actually removing the corresponding RTCRtpSender from the list of senders
     * as reported by RTCPeerConnection.getSenders().
     *
     * @param sender
     */
    public void removeTrack(RTCRtpSender sender);

    /**
     * The WebRTC API's RTCPeerConnection interface offers the restartIce()
     * method to allow a web application to easily request that ICE candidate
     * gathering be redone on both ends of the connection.
     */
    public void restartIce();

    /**
     * The RTCPeerConnection.setIdentityProvider() method sets the Identity
     * Provider (IdP) to the triplet given in parameter: its name, the protocol
     * used to communicate with it (optional) and an optional username. The IdP
     * will be used only when an assertion is needed.
     *
     * @param domainName
     */
    public void setIdentityProvider(String domainName);

    public void setIdentityProvider(String domainName, String protocol);

    public void setIdentityProvider(String domainName, String protocol, String username);

    /**
     * The RTCPeerConnection method setLocalDescription() changes the local
     * description associated with the connection. This description specifies
     * the properties of the local end of the connection, including the media
     * format.
     *
     * @param sessionDescription
     * @return
     */
    public Promise setLocalDescription(RTCSessionDescription sessionDescription);

    /**
     * The RTCPeerConnection method setRemoteDescription() sets the specified
     * session description as the remote peer's current offer or answer. The
     * description specifies the properties of the remote end of the connection,
     * including the media format.
     *
     * @param sessionDescription
     * @return
     */
    public Promise setRemoteDescription(RTCSessionDescription sessionDescription);

    public static class RTCDataChannelInit implements JSONStruct {

        /**
         * @return the ordered
         */
        public boolean isOrdered() {
            return ordered;
        }

        /**
         * @param ordered the ordered to set
         */
        public void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        /**
         * @return the maxPacketLifeTime
         */
        public Integer getMaxPacketLifeTime() {
            return maxPacketLifeTime;
        }

        /**
         * @param maxPacketLifeTime the maxPacketLifeTime to set
         */
        public void setMaxPacketLifeTime(Integer maxPacketLifeTime) {
            this.maxPacketLifeTime = maxPacketLifeTime;
        }

        /**
         * @return the maxRetransmits
         */
        public Integer getMaxRetransmits() {
            return maxRetransmits;
        }

        /**
         * @param maxRetransmits the maxRetransmits to set
         */
        public void setMaxRetransmits(Integer maxRetransmits) {
            this.maxRetransmits = maxRetransmits;
        }

        /**
         * @return the protocol
         */
        public String getProtocol() {
            return protocol;
        }

        /**
         * @param protocol the protocol to set
         */
        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        /**
         * @return the negotiated
         */
        public boolean isNegotiated() {
            return negotiated;
        }

        /**
         * @param negotiated the negotiated to set
         */
        public void setNegotiated(boolean negotiated) {
            this.negotiated = negotiated;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }
        private boolean ordered;
        private Integer maxPacketLifeTime;
        private Integer maxRetransmits;
        private String protocol;
        private boolean negotiated;
        private int id;

        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            out.put("ordered", ordered);
            if (maxPacketLifeTime != null) {
                out.put("maxPacketLifeTime", maxPacketLifeTime);
            }
            if (maxRetransmits != null) {
                out.put("maxRetransmits", maxRetransmits);
            }
            if (protocol != null) {
                out.put("protocol", protocol);
            }
            out.put("negotiated", negotiated);
            if (id != 0) {
                out.put("id", id);
            }
            return out;
        }
    }

    /**
     * The RTCBundlePolicy enum defines string constants which are used to
     * request a specific policy for gathering ICE candidates if the remote peer
     * isn't compatible with the SDP BUNDLE standard for bundling multiple media
     * streams on a single transport link.
     */
    public static enum RTCBundlePolicy {

        /**
         * On BUNDLE-aware connections, the ICE agent should gather candidates
         * for all of the media types in use (audio, video, and data).
         * Otherwise, the ICE agent should only negotiate one audio and video
         * track on separate transports.
         */
        Balanced("balanced"),
        /**
         * The ICE agent should gather candidates for each track, using separate
         * transports to negotiate all media tracks for connections which aren't
         * BUNDLE-compatible.
         */
        MaxCompat("max-compat"),
        /**
         * The ICE agent should gather candidates for just one track. If the
         * connection isn't BUNDLE-compatible, then the ICE agent should
         * negotiate just one media track.
         */
        MaxBundle("max-bundle");

        String string;

        RTCBundlePolicy(String str) {
            this.string = str;
        }

    }

    /**
     * The RTCIceConnectionState enum defines the string constants used to
     * describe the current state of the ICE agent and its connection to the ICE
     * server (that is, the STUN or TURN server).
     */
    public static enum RTCIceConnectionState {
        /**
         * The ICE agent is gathering addresses or is waiting to be given remote
         * candidates through calls to RTCPeerConnection.addIceCandidate() (or
         * both).
         */
        New("new"),
        /**
         * The ICE agent has been given one or more remote candidates and is
         * checking pairs of local and remote candidates against one another to
         * try to find a compatible match, but has not yet found a pair which
         * will allow the peer connection to be made. It's possible that
         * gathering of candidates is also still underway.
         */
        Checking("checking"),
        /**
         * A usable pairing of local and remote candidates has been found for
         * all components of the connection, and the connection has been
         * established. It's possible that gathering is still underway, and it's
         * also possible that the ICE agent is still checking candidates against
         * one another looking for a better connection to use.
         */
        Connected("connected"),
        /**
         * The ICE agent has finished gathering candidates, has checked all
         * pairs against one another, and has found a connection for all
         * components.
         */
        Completed("completed"),
        /**
         * The ICE candidate has checked all candidates pairs against one
         * another and has failed to find compatible matches for all components
         * of the connection. It is, however, possible that the ICE agent did
         * find compatible connections for some components.
         */
        Failed("failed"),
        /**
         * Checks to ensure that components are still connected failed for at
         * least one component of the RTCPeerConnection. This is a less
         * stringent test than "failed" and may trigger intermittently and
         * resolve just as spontaneously on less reliable networks, or during
         * temporary disconnections. When the problem resolves, the connection
         * may return to the "connected" state.
         */
        Disconnected("disconnected"),
        /**
         * The ICE agent for this RTCPeerConnection has shut down and is no
         * longer handling requests.
         */
        Closed("closed");

        String string;

        RTCIceConnectionState(String str) {
            this.string = str;
        }

        public boolean matches(String str) {
            return string.equals(str);
        }

    }

    /**
     * The RTCIceGatheringState enum defines string constants which reflect the
     * current status of ICE gathering, as returned using the
     * RTCPeerConnection.iceGatheringState property. You can detect when this
     * value changes by watching for an event of type icegatheringstatechange.
     */
    public static enum RTCIceGatheringState {
        /**
         * The peer connection was just created and hasn't done any networking
         * yet.
         */
        New("new"),
        /**
         * The ICE agent is in the process of gathering candidates for the
         * connection.
         */
        Gathering("gathering"),
        /**
         * The ICE agent has finished gathering candidates. If something happens
         * that requires collecting new candidates, such as a new interface
         * being added or the addition of a new ICE server, the state will
         * revert to "gathering" to gather those candidates.
         */
        Complete("complete");

        String string;

        RTCIceGatheringState(String str) {
            this.string = str;
        }

        public boolean matches(String str) {
            return string.equals(str);
        }

    }

    /**
     * The RTCIceTransportPolicy enum defines string constants which can be used
     * to limit the transport policies of the ICE candidates to be considered
     * during the connection process.
     */
    public static enum RTCIceTransportPolicy {
        /**
         * All ICE candidates will be considered.
         */
        All("all"),
        /**
         * Only ICE candidates with public IP addresses will be considered.
         * Removed from the specification's May 13, 2016 working draft.
         */
        Public("public"),
        /**
         * Only ICE candidates whose IP addresses are being relayed, such as
         * those being passed through a TURN server, will be considered.
         */
        Relay("relay");

        String string;

        RTCIceTransportPolicy(String str) {
            this.string = str;
        }

    }

    /**
     * The RTCPeerConnectionState enum defines string constants which describe
     * states in which the RTCPeerConnection may be. These values are returned
     * by the connectionState property. This state essentially represents the
     * aggregate state of all ICE transports (which are of type RTCIceTransport
     * or RTCDtlsTransport) being used by the connection.
     */
    public static enum RTCPeerConnectionState {
        /**
         * At least one of the connection's ICE transports (RTCIceTransports or
         * RTCDtlsTransports) are in the "new" state, and none of them are in
         * one of the following states: "connecting", "checking", "failed", or
         * "disconnected", or all of the connection's transports are in the
         * "closed" state.
         */
        New("new"),
        /**
         * One or more of the ICE transports are currently in the process of
         * establishing a connection; that is, their RTCIceConnectionState is
         * either "checking" or "connected", and no transports are in the
         * "failed" state. <<< Make this a link once I know where that will be
         * documented
         */
        Connecting("connecting"),
        /**
         * Every ICE transport used by the connection is either in use (state
         * "connected" or "completed") or is closed (state "closed"); in
         * addition, at least one transport is either "connected" or
         * "completed".
         */
        Connected("connected"),
        /**
         * At least one of the ICE transports for the connection is in the
         * "disconnected" state and none of the other transports are in the
         * state "failed", "connecting", or "checking".
         */
        Disconnected("disconnected"),
        /**
         * One or more of the ICE transports on the connection is in the
         * "failed" state.
         */
        Failed("failed"),
        /**
         * The RTCPeerConnection is closed.
         *
         * This value was in the RTCSignalingState enum (and therefore found by
         * reading the value of the signalingState) property until the May 13,
         * 2016 draft of the specification.
         */
        Closed("closed");

        String string;

        RTCPeerConnectionState(String str) {
            this.string = str;
        }

        /**
         *
         * @param stateStr
         * @return
         */
        public boolean matches(String stateStr) {
            return string.equals(stateStr);
        }

    }

    /**
     * The RTCRtcpMuxPolicy enum defines string constants which specify what ICE
     * candidates are gathered to support non-multiplexed RTCP. <<<add a link to
     * info about multiplexed RTCP.
     */
    public static enum RTCRtcpMuxPolicy {
        /**
         * Instructs the ICE agent to gather both RTP and RTCP candidates. If
         * the remote peer can multiplex RTCP, then RTCP candidates are
         * multiplexed atop the corresponding RTP candidates. Otherwise, both
         * the RTP and RTCP candidates are returned, separately.
         */
        Negotiate("negotiate"),
        /**
         * Tells the ICE agent to gather ICE candidates for only RTP, and to
         * multiplex RTCP atop them. If the remote peer doesn't support RTCP
         * multiplexing, then session negotiation fails.
         */
        Require("require");

        String string;

        RTCRtcpMuxPolicy(String str) {
            this.string = str;
        }

    }

    /**
     * The RTCSignalingState enum specifies the possible values of
     * RTCPeerConnection.signalingState, which indicates where in the process of
     * signaling the exchange of offer and answer the connection currently is.
     */
    public static enum RTCSignalingState {

        /**
         * There is no ongoing exchange of offer and answer underway. This may
         * mean that the RTCPeerConnection object is new, in which case both the
         * localDescription and remoteDescription are null; it may also mean
         * that negotiation is complete and a connection has been established.
         */
        Stable("stable"),
        /**
         * The local peer has called RTCPeerConnection.setLocalDescription(),
         * passing in SDP representing an offer (usually created by calling
         * RTCPeerConnection.createOffer()), and the offer has been applied
         * successfully.
         */
        HaveLocalOffer("have-local-offer"),
        /**
         * The remote peer has created an offer and used the signaling server to
         * deliver it to the local peer, which has set the offer as the remote
         * description by calling RTCPeerConnection.setRemoteDescription().
         */
        HaveRemoteOffer("have-remote-offer"),
        /**
         * The offer sent by the remote peer has been applied and an answer has
         * been created (usually by calling RTCPeerConnection.createAnswer())
         * and applied by calling RTCPeerConnection.setLocalDescription(). This
         * provisional answer describes the supported media formats and so
         * forth, but may not have a complete set of ICE candidates included.
         * Further candidates will be delivered separately later.
         */
        HaveLocalPranswer("have-local-pranswer"),
        /**
         * A provisional answer has been received and successfully applied in
         * response to an offer previously sent and established by calling
         * setLocalDescription().
         */
        HaveRemotePranswer("have-remote-pranswer"),
        /**
         * The connection is closed.
         */
        Closed("closed");

        String string;

        RTCSignalingState(String str) {
            this.string = str;
        }

        boolean matches(String str) {
            return string.equals(str);
        }

    }

    /**
     * The RTCOfferOptions dictionary is used to provide optional settings when
     * creating an RTCPeerConnection offer with the createOffer() method.
     *
     * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCOfferOptions
     */
    public static class RTCOfferOptions implements JSONStruct {

        /**
         * A Boolean which, when set to true, tells createOffer() to generate
         * and use new values for the identifying properties of the SDP it
         * creates, resulting in a request that triggers renegotiation of the
         * ICE connection. This is useful if network conditions have changed in
         * a way that make the current configuration untenable or impractical,
         * for instance.
         *
         * @return the iceRestart
         */
        public Boolean isIceRestart() {
            return iceRestart;
        }

        /**
         * @param iceRestart the iceRestart to set
         */
        public void setIceRestart(Boolean iceRestart) {
            this.iceRestart = iceRestart;
        }

        public RTCOfferOptions iceRestart(Boolean iceRestart) {
            setIceRestart(iceRestart);
            return this;
        }

        /**
         * @return the offerToReceiveAudio
         */
        public Boolean isOfferToReceiveAudio() {
            return offerToReceiveAudio;
        }

        /**
         * @param offerToReceiveAudio the offerToReceiveAudio to set
         */
        public void setOfferToReceiveAudio(Boolean offerToReceiveAudio) {
            this.offerToReceiveAudio = offerToReceiveAudio;
        }

        public RTCOfferOptions offerToReceiveAudio(Boolean offerToReceiveAudio) {
            setOfferToReceiveAudio(offerToReceiveAudio);
            return this;
        }

        /**
         * @return the offerToReceiveVideo
         */
        public Boolean isOfferToReceiveVideo() {
            return offerToReceiveVideo;
        }

        /**
         * @param offerToReceiveVideo the offerToReceiveVideo to set
         */
        public void setOfferToReceiveVideo(Boolean offerToReceiveVideo) {
            this.offerToReceiveVideo = offerToReceiveVideo;
        }

        public RTCOfferOptions offerToReceiveVideo(Boolean offerToReceiveVideo) {
            setOfferToReceiveVideo(offerToReceiveVideo);
            return this;
        }

        /**
         * @return the voiceActivityDetection
         */
        public Boolean isVoiceActivityDetection() {
            return voiceActivityDetection;
        }

        /**
         * @param voiceActivityDetection the voiceActivityDetection to set
         */
        public void setVoiceActivityDetection(Boolean voiceActivityDetection) {
            this.voiceActivityDetection = voiceActivityDetection;
        }

        public RTCOfferOptions voiceActivityDetection(Boolean voiceActivityDetection) {
            setVoiceActivityDetection(voiceActivityDetection);
            return this;
        }

        private Boolean iceRestart;
        private Boolean offerToReceiveAudio;
        private Boolean offerToReceiveVideo;
        private Boolean voiceActivityDetection;

        @Override
        public Object toJSONStruct() {
            Map out = new LinkedHashMap();
            if (iceRestart != null) {
                out.put("iceRestart", iceRestart);
            }
            if (offerToReceiveAudio != null) {
                out.put("offerToReceiveAudio", offerToReceiveAudio);
            }
            if (offerToReceiveVideo != null) {
                out.put("offerToReceiveVideo", offerToReceiveVideo);
            }
            if (voiceActivityDetection != null) {
                out.put("voiceActivityDetection", voiceActivityDetection);
            }
            return out;
        }
    }

}
