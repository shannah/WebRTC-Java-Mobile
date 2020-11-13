/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.CN.NORTH;
import static com.codename1.ui.CN.callSerially;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.webrtc.RTCVideoContainer;
import com.codename1.webrtc.client.MockRTCClient;
import com.codename1.webrtc.client.RTCClient;
import com.codename1.webrtc.client.RTCClientConnectionView;
import com.codename1.webrtc.client.RTCClientSession;
import com.codename1.webrtc.client.RTCClientSession.ConnectionAddedEvent;
import com.codename1.webrtc.client.RTCClientSession.ConnectionEvent;
import com.codename1.webrtc.client.RTCClientSession.ConnectionRemovedEvent;

/**
 *
 * @author shannah
 */
public class RTCClientDemo extends Form implements AutoCloseable {
    
    private RTCClient client = new MockRTCClient();
    private RTCClientSession sessions[] = new RTCClientSession[2];
    private String usernames[] = new String[]{"Steve", "John"};
    private Button newConnectionButton = new Button("New Connection");
    private Container wrapper = new Container(new GridLayout(2, 1));
    private CallView callViews[] = new CallView[]{ new CallView(usernames[0]), new CallView(usernames[1])};
    
    private class CallView extends Container {
        private Container wrapper = new Container(new GridLayout(2));
        CallView(String label) {
            super(new BorderLayout());
            add(NORTH, new Label(label));
            add(CENTER, wrapper);
        }
        
        public void addWithLabel(String label, Component cmp) {
            Container cnt = new Container(new BorderLayout());
            cnt.add(BorderLayout.NORTH, new Label(label));
            cnt.add(BorderLayout.CENTER, cmp);
            wrapper.add(cnt);
        }
        
        public void removeParent(Component cmp) {
            if (!contains(cmp)) {
                return;
            }
            removeComponent(cmp.getParent());
        }
    }
    
    
    public RTCClientDemo() {
        super("Client Demo", new BorderLayout());
        add(NORTH, newConnectionButton);
        for (CallView callView : callViews) {
            wrapper.add(callView);
        }
        add(CENTER, wrapper);
        init();
        int i =0;
        for (RTCClientSession session : getSessions()) {
            CallView cv = callViews[i++];
            session.getLocalStream().then(stream->{
                return new RTCVideoContainer(stream);
            }).then(cnt -> {
                System.out.println("Addign local stream");
                cv.addWithLabel("Local", (RTCVideoContainer)cnt);
                revalidateWithAnimationSafety();
                return null;

            });
        }
        
    }
    
    private RTCClientSession[] getSessions() {
        for (int i=0; i<sessions.length; i++) {
            RTCClientSession session = sessions[i];
            if (session == null) {
                session = new RTCClientSession(client, usernames[i]);
                sessions[i] = session;
                session.addConnectionListener(evt -> {
                    callSerially(()->{
                        handleConnectionEvent(evt);
                    });
                });
            }
        }
        
            
            
        return sessions;
    }
    
    private int findSession(RTCClientSession session) {
        int len = sessions.length;
        for (int i=0; i<len; i++) {
            if (sessions[i] == session) {
                return i;
            }
        }
        return -1;
    }
    
    private void handleConnectionEvent(ConnectionEvent evt) {
        if (evt instanceof ConnectionAddedEvent) {
            ConnectionAddedEvent cevt = (ConnectionAddedEvent)evt;
            int sessionIndex = findSession(cevt.getSession());
            CallView view = callViews[sessionIndex];
            System.out.println("Adding PeerConnectionView");
            view.addWithLabel("Remote", new RTCClientConnectionView(evt.getConnection()));
            /*
            MediaStream lstream = cevt.getSession().getLocalStream().await();
            RTC lstreamRTC = RTC.getRTC(lstream);
            if (lstreamRTC.getVideoComponent().getParent() != null) {
                // We need to import the stream 
                RTC lrtc = RTC.createRTC().await();
                lstream = lrtc.importStream(lstream).await();
            } else {
                System.out.println("Just adding stream direct");
            }
            view.addWithLabel("Local Again: ", new RTCVideoContainer(lstream));
*/
            revalidateWithAnimationSafety();   
           
            return;
        }
        
        if (evt instanceof ConnectionRemovedEvent) {
            ConnectionAddedEvent cevt = (ConnectionAddedEvent)evt;
            int sessionIndex = findSession(cevt.getSession());
            CallView view = callViews[sessionIndex];
            for (Component child : $("*", view)) {
                if (!(child instanceof RTCClientConnectionView)) {
                    continue;
                }
                RTCClientConnectionView pcv = (RTCClientConnectionView)child;
                if (pcv.getConnection() == evt.getConnection()) {
                    view.removeParent(pcv);
                    pcv.cleanup();
                }
            }
            revalidateWithAnimationSafety();
            return;
        }
    }
    
    private void init() {
        newConnectionButton.addActionListener(evt-> {
            CN.callSerially(()->sessions[0].connect(usernames[1]));
        });
    }
    
    
    

    @Override
    public void close() throws Exception {
        for (RTCClientSession s : sessions) {
            try {
                s.close();
            } catch (Throwable t){}
        }
        
    }
    
}
