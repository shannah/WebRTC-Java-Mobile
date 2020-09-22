/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.components.SpanButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.io.Preferences;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CN;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Slider;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.table.TableLayout;
import com.codename1.webrtc.Event;
import com.codename1.webrtc.Functor;
import com.codename1.webrtc.MediaDeviceInfo;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCConfiguration;
import com.codename1.webrtc.RTCConfiguration.RTCIceTransportPolicy;
import com.codename1.webrtc.RTCIceCandidate;
import com.codename1.webrtc.RTCIceCredentialType;
import com.codename1.webrtc.RTCIceServer;
import com.codename1.webrtc.RTCIceServers;
import com.codename1.webrtc.RTCPeerConnection;
import com.codename1.webrtc.RTCPeerConnection.RTCOfferOptions;
import com.codename1.webrtc.RTCPeerConnectionIceErrorEvent;
import com.codename1.webrtc.RTCPeerConnectionIceEvent;
import com.codename1.webrtc.RTCSessionDescription;
import com.codename1.webrtc.RTCVideoElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This sample was adapted from https://webrtc.github.io/samples/src/content/peerconnection/trickle-ice/
 * 
 * The source for that sample is available at https://github.com/webrtc/samples/blob/gh-pages/src/content/peerconnection/trickle-ice/js/main.js
 * 
 * @author shannah
 */
public class TrickleIceDemo extends Form implements AutoCloseable {
    private RTC rtc;
    private Button addButton = new Button("Add");
    private Container candidateTBody = new Container();
    private Button gatherButton = new Button("Gather");
    private TextField passwordInput = new TextField();
    private Button removeButton = new Button("Remove");
    private Button resetButton = new Button("Reset");
    private com.codename1.ui.List<Option> servers = new com.codename1.ui.List<Option>();
    private RadioButton allTransports = new RadioButton();
    private RadioButton relayTransport = new RadioButton();
    private RadioButton[] transports = new RadioButton[]{allTransports, relayTransport};
    private SpanLabel error = new SpanLabel();
    private SpanLabel getUserMediaPermissions = new SpanLabel();
    
    private TextField urlInput = new TextField();
    private TextField usernameInput = new TextField();
    private Slider iceCandidatePoolInput = new Slider();
    {
        iceCandidatePoolInput.setMinValue(0);
        iceCandidatePoolInput.setMaxValue(10);
    }
    private Label iceCandidatePoolValue = new Label();
    
    
    {
        
        
        servers.setMinElementHeight(4);
        ButtonGroup transportsGroup = new ButtonGroup();
        for (RadioButton transportBtn : transports) {
            transportsGroup.add(transportBtn);
        }
        
        iceCandidatePoolInput.setEditable(true);
        addButton.addActionListener(evt->addServer());
        gatherButton.addActionListener(evt->start());
        removeButton.addActionListener(evt->removeServer());
        resetButton.addActionListener(evt->{
            Preferences.clearAll();
            while (servers.getModel().getSize() > 0) servers.getModel().removeItem(0);
            setDefaultServer(servers);
            
        });
        iceCandidatePoolInput.addDataChangedListener(((type, index) -> {
            iceCandidatePoolValue.setText(iceCandidatePoolInput.getProgress()+"");
        }));
        
        servers.addActionListener(evt->{
            //selectServer()
        });
    }
    
    private long begin;
    private RTCPeerConnection pc;
    private List<RTCIceCandidate> candidates;
    
    private String allServersKey = "servers";
    
    private void setDefaultServer(com.codename1.ui.List<Option> serversSelect) {
        Option o = new Option();
        o.value = "{\"urls\":[\"stun:stun.l.google.com:19302\"]}";
        o.text = "stun:stun.l.google.com:19302";
        serversSelect.addItem(o);
        revalidateWithAnimationSafety();
        
    }
    
    private void writeServersToLocalStorage() {
        String allServers = JSON.stringify(map(values(servers.getModel()), o -> {
            Option opt = (Option)o;
            try {
                return JSON.parseMap(opt.value);
            } catch (IOException ex) {
                Log.e(ex);
                return "[error] "+ex.getMessage();
            }
        }));
        Preferences.set(allServersKey, allServers);
        
    }
    
    private void readServersFromLocalStorage() {
        while (servers.getModel().getSize() > 0) servers.getModel().removeItem(0);
        com.codename1.ui.List<Option> serversSelect = servers;
        String storedServers = Preferences.get(allServersKey, null);
        if (storedServers == null || storedServers.isEmpty()) {
            setDefaultServer(serversSelect);
        } else {
            try {
                for (Map server : (List<Map>)JSON.parseList(storedServers)) {
                    Option o = new Option();
                    o.value = JSON.stringify(server);
                    o.text = ((List<String>)server.get("urls")).get(0);
                    serversSelect.addItem(o);
                }
            } catch (IOException ex) {
                Log.e(ex);
                
            }
        }
    }
    private void selectServer() {

        Option option = servers.getSelectedItem();
        try {
            Map value = JSON.parseMap(option.value);
            List<String> urls = (List<String>)value.get("urls");
            urlInput.setText(urls.get(0));
            usernameInput.setText(pipe((String)value.get("username"), ""));
            passwordInput.setText(pipe((String)value.get("credential"), ""));
            
        } catch (IOException ex) {
            Log.e(ex);
        }
    }
    
    
    
    private void addServer() {
        String scheme = Util.split(urlInput.getText(), ":")[0];
        if (!"stun".equals(scheme) && !"turn".equals(scheme) && !"turns".equals(scheme)) {
            Dialog.show("", "URI Scheme "+scheme+" is not value", "OK", null);
            return;
        }
        
        // Store the ICE server as a stringified JSON object in option.value.
        Option option = new Option();
        Map iceServer = new HashMap();
        iceServer.put("urls", Arrays.asList(urlInput.getText()));
        iceServer.put("username", usernameInput.getText());
        iceServer.put("credential", passwordInput.getText());
        
        option.value = JSON.stringify(iceServer);
        option.text = urlInput.getText()+" ";
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        if (truthy(username) || truthy(password)) {
            option.text += " ["+username+":"+password+"]";
        }
        servers.addItem(option);
        urlInput.setText("");
        usernameInput.setText("");
        passwordInput.setText("");
        writeServersToLocalStorage();
        
        
    }
    
    
    private void removeServer() {
        for (int i=servers.getModel().getSize() -1; i>= 0; --i) {
            if (servers.getSelectedIndex() == i) {
                servers.getModel().removeItem(i);
                break;
            }
        }
        writeServersToLocalStorage();
    }
    
    private void start() {
        candidateTBody.removeAll();
        numCandidateRows = 0;
        addCandidatesTableHeader();
        
        gatherButton.setEnabled(false);
        
        // Read the values from the input boxes
        RTCIceServers iceServers = new RTCIceServers();
        int len = servers.getModel().getSize();
        for (int i=0; i<len; i++) {
            try {
                RTCIceServer iceServer = new RTCIceServer(JSON.parseMap(servers.getModel().getItemAt(i).value));
                iceServers.add(iceServer);
            } catch (IOException ex) {
                Log.e(ex);
            }
        }
        
        RTCIceTransportPolicy iceTransports = null;
        for (RadioButton transport : transports) {
            if (transport.isSelected()) {
                //iceTransports = transport.getText();
                iceTransports = RTCIceTransportPolicy.policyFromString(transport.getText());
                break;
            }
        }
        RTCConfiguration config = new RTCConfiguration();
        config.setIceServers(iceServers);
        config.setIceTransportPolicy(iceTransports);
        config.setIceCandidatePoolSize(iceCandidatePoolInput.getProgress());
        
        RTCOfferOptions offerOptions = new RTCOfferOptions();
        offerOptions.setOfferToReceiveAudio(true);
        
        Log.p("Creating new PeerConnection with config="+config);
        
        error.setText("");
        pc = rtc.newRTCPeerConnection(config);
        pc.onicecandidate(evt->iceCallback(evt));
        pc.onicegatheringstatechange(evt->gatheringStateChange(evt));
        pc.addEventListener("icecandidateerror", evt->{
            iceCandidateError((RTCPeerConnectionIceErrorEvent)evt);
        });
        pc.createOffer(offerOptions)
                .onSuccess(desc->{
                    gotDescription(desc);
                })
                .onFail(err-> {
                    noDescription((Throwable)err);
                });
                
        
       
        
        
    }
    
    private void gotDescription(RTCSessionDescription desc) {
        begin = System.currentTimeMillis();
        candidates = new ArrayList<>();
        pc.setLocalDescription(desc);
        
    }
    
    private void noDescription(Throwable error) {
        Log.p("Error creating offer: "+ error.getMessage(), Log.ERROR);
    }
    
    // Parse the uint32 PRIORITY field into its constituent parts from RFC 5245,
    // type preference, local preference, and (256 - component ID).
    // ex: 126 | 32252 | 255 (126 is host preference, 255 is component ID 1)
    private String formatPriority(long priority) {
        StringBuilder sb = new StringBuilder();
        sb.append(priority >> 24).append(" | ");
        sb.append((priority >> 8) & 0xffff).append(" | ");
        sb.append(priority & 0xff);
        return sb.toString();
    }
    
    /**
     * Try to determine authentication failures and unreachable TURN
     * servers by using heuristics on the candidate types gathered.
     * @return 
     */
    private String getFinalResult() {
        String result = "Done";
        
        // if more than one server is used, it can not be determined
        // which server failed.
        if (servers.size() == 1) {
            RTCIceServer server;
            try {
                server = new RTCIceServer(JSON.parseMap(servers.getModel().getItemAt(0).value));
            } catch (IOException ex) {
                Log.e(ex);
                throw new RuntimeException(ex.getMessage(), ex);
            }
            List<String> types = (List<String>)map(candidates, cand -> {
                return ((RTCIceCandidate)cand).getType();
            });
            // If the server is a TURN server we should have a relay candidate.
            // If we did not get a relay candidate but a srflx candidate
            // authentication might have failed.
            // If we did not get  a relay candidate or a srflx candidate
            // we could not reach the TURN server. Either it is not running at
            // the target address or the clients access to the port is blocked.
            //
            // This only works for TURN/UDP since we do not get
            // srflx candidates from TURN/TCP.
            
            if (server.getUrls().get(0).indexOf("turn:") == 0 &&
                    server.getUrls().get(0).indexOf("?transport=tcp") < 0) {
                if (types.indexOf("relay") == -1) {
                    if (types.indexOf("srflx") > -1) {
                        // a binding response but no relay candidate suggests auth failure.
                        result = "Authentication failed?";
                    } else {
                        result = "Not reachable?";
                    }
                }
            }
        }
        return result;
    }
    
    private void iceCallback(RTCPeerConnectionIceEvent  evt) {
        long elapsed = System.currentTimeMillis() - begin;
        if (evt.getCandidate() != null) {
            if ("".equals(evt.getCandidate().getCandidate())) {
                return;
            }
            RTCIceCandidate candidate = evt.getCandidate();
            addCandidateRow(elapsed, candidate);
        }
    }
    
    private void gatheringStateChange(Event evt) {
        if (pc.getIceGatheringState() != RTCPeerConnection.RTCIceGatheringState.Complete) {
            return;
        }
        long elapsed = System.currentTimeMillis() - begin;
        TableLayout tl = (TableLayout)candidateTBody.getLayout();
        TableLayout.Constraint cnst = tl.createConstraint(numCandidateRows, 1);
        cnst.setHorizontalSpan(7);
            
        pc.close();
        pc.release();
        pc = null;
        gatherButton.setEnabled(true);
        candidateTBody.add(tl.createConstraint(numCandidateRows, 0), elapsed+"");
        candidateTBody.add(cnst, getFinalResult());
        numCandidateRows++;
        
    }
    
    private void iceCandidateError(RTCPeerConnectionIceErrorEvent e) {
        // The interesting attributes of the error are
        // * the url (which allows looking up the server)
        // * the errorCode and errorText
        error.setText("The server "+e.getURL()+" returned an error with code="+e.getErrorCode()+":\n"+e.getErrorText());
    }

    private void onReady() {
        readServersFromLocalStorage();
        rtc.enumerateDevices().onSuccess(devices->{
            for (MediaDeviceInfo device : devices) {
                if (!"".equals(device.getLabel())) {
                    getUserMediaPermissions.setVisible(true);
                    getUserMediaPermissions.setHidden(false);
                    revalidateWithAnimationSafety();
                    
                }
            }
        });
    }
    
    private int numCandidateRows = 0;
    
    private void addCandidateRow(long time, RTCIceCandidate candidate) {
        System.out.println("Adding candidate row: "+candidate);
        TableLayout tl = (TableLayout)candidateTBody.getLayout();
        Container c = candidateTBody;
        int col = 0;
        int row = numCandidateRows++;
        c.add(tl.createConstraint(row, col++), time+"");
        c.add(tl.createConstraint(row, col++), pipe(candidate.getComponent(), ""));
        c.add(tl.createConstraint(row, col++), pipe(candidate.getType(), ""));
        c.add(tl.createConstraint(row, col++), pipe(candidate.getFoundation(), ""));
        String protocolString = candidate.getProtocol() == null ? null : candidate.getProtocol().stringValue();
        c.add(tl.createConstraint(row, col++), pipe(protocolString, ""));
        c.add(tl.createConstraint(row, col++), pipe(candidate.getIp(), ""));
        c.add(tl.createConstraint(row, col++), candidate.getPort()+"");
        c.add(tl.createConstraint(row, col++), formatPriority(candidate.getPriority()));
        revalidateWithAnimationSafety();
        
    }
    
    
    
    private void addCandidatesTableHeader() {
        TableLayout tl = (TableLayout)candidateTBody.getLayout();
        Container c = candidateTBody;
        int col = 0;
        
        for (String header : new String[]{"Time", "Component",  "Type", "Foundation", "Protocol", "Address", "Port", "Priority"}) {
            c.add(tl.createConstraint(0, col++), header);
        }
        numCandidateRows++;
        revalidateWithAnimationSafety();
    }
    
   
    
   
    public TrickleIceDemo() {
        super("Trickle Ice Demo", new BorderLayout());
        Form hi = this;
        String intro = "This sample was adapted from the \"Trick Ice Demo\" on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/TrickleIceDemo.java"));
        hi.add(BorderLayout.NORTH, BoxLayout.encloseY(new SpanLabel(intro), viewSource));
        
        Container instructions = new Container(BoxLayout.y());
        instructions.add(new SpanLabel("This page tests the trickle ICE functionality in a WebRTC implementation. It creates a PeerConnection with the specified ICEServers, and then starts candidate gathering for a session with a single audio stream. As candidates are gathered, they are displayed in the text box below, along with an indication when candidate gathering is complete."));
        instructions.add(new SpanLabel("Note that if no getUserMedia permissions for this origin are persisted only candidates from a single interface will be gathered in Chrome."));
        SpanButton seeDraft = new SpanButton("See the RTCWEB IP address handling recommendations draft for details.");
        seeDraft.setUIID("Label");
        seeDraft.addActionListener(evt->{
            CN.execute("https://tools.ietf.org/html/draft-ietf-rtcweb-ip-handling-01");
        });
        instructions.add(seeDraft);
        instructions.add(new SpanLabel("You have given permission, candidate from multiple interface will be gathered."));
        
        instructions.add(new SpanLabel("Individual STUN and TURN servers can be added using the Add server / Remove server controls below; in addition, the type of candidates released to the application can be controlled via the IceTransports constraint."));
        instructions.add(new SpanLabel("If you test a STUN server, it works if you can gather a candidate with type \"srflx\". If you test a TURN server, it works if you can gather a candidate with type \"relay\"."));
        instructions.add(new SpanLabel("If you test just a single TURN/UDP server, this page even allows you to detect when you are using the wrong credential to authenticate."));
        
        Label heading = new Label("ICE servers");
        
        Container serversWrap = new Container(new BorderLayout());
        serversWrap.add(BorderLayout.NORTH, heading);
        
        Container inputsTbl = new Container(new GridLayout(3, 2));
        
        inputsTbl.add(new Label("STUN or TURN URI:"))
                .add(urlInput)
                .add(new Label("TURN username:"))
                .add(usernameInput)
                .add(new Label("TURN password:"))
                .add(passwordInput)
                ;
        
        serversWrap.add(BorderLayout.CENTER, BoxLayout.encloseY(servers, inputsTbl));
        serversWrap.add(BorderLayout.SOUTH, GridLayout.encloseIn(3, addButton, removeButton, resetButton));
        
        heading = new Label("ICE options");
        Container optionsWrap = new Container(new BorderLayout());
        optionsWrap.add(BorderLayout.NORTH, heading);
        inputsTbl = new Container(new TableLayout(2, 2));
        inputsTbl.add("IceTransports value:").add(FlowLayout.encloseIn(transports))
                .add("ICE Candidate Pool:").add(BoxLayout.encloseX(iceCandidatePoolValue, iceCandidatePoolInput));
        optionsWrap.add(BorderLayout.CENTER, inputsTbl);
        optionsWrap.add(BorderLayout.SOUTH, FlowLayout.encloseIn(gatherButton));
        
        candidateTBody = new Container(new TableLayout(1, 8));
        
        
        Container candidateNotes = new Container(BoxLayout.y());
        candidateNotes.add(new SpanLabel("Note: errors from onicecandidateerror above are not neccessarily fatal. For example an IPv6 DNS lookup may fail but relay candidates can still be gathered via IPv4."));
        
        
        Container center = new Container(BoxLayout.y());
        
        center.add(instructions);
        
        center.add(serversWrap);
        
        center.add(optionsWrap);
        center.add(candidateNotes);
        center.add(candidateTBody);
        
        center.add(error);
        center.add(getUserMediaPermissions);
        center.setScrollableY(true);
        
        add(BorderLayout.CENTER, center);
        
        
        
        RTC.createRTC().ready(rtc->{
            this.rtc = rtc;
            onReady();
            revalidateWithAnimationSafety();
        });
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
    
    // ------------ Compatibility/Utility Functions ----------------------------
    
    /**
     * Gets the values of a list model as an Iterable
     * @param model
     * @return 
     */
    private Iterable values(ListModel model) {
        List out = new ArrayList();
        int len = model.getSize();
        for (int i=0; i<len; i++) {
            out.add(model.getItemAt(i));
        }
        return out;
    }
    
    /**
     * Maps values of input iterable to new values using given functor.
     * @param input The iterable to map
     * @param f Mapping function
     * @return The mapped iterable.
     */
    private Iterable map(Iterable input, Functor f) {
        List out = new ArrayList();
        for (Object o : input) {
            out.add(f.call(o));
        }
        return out;
    }
    
    /**
     * Returns first non-null string.
     * @param values
     * @return 
     */
    private String pipe(String... values) {
        for (String v : values) {
            if (v != null) {
                return v;
            }
        }
        
        return null;
    }
    
    private boolean falsey(String val) {
        return val == null || val.isEmpty();
    }
    
    private boolean truthy(String val) {
        return !falsey(val);
    }
    
    /**
     * Encapsulation of options for combo boxes.
     */
    private class Option {
        private String value="";
        private String text="";

        @Override
        public String toString() {
            return text;
        }
        
        
    }
}
