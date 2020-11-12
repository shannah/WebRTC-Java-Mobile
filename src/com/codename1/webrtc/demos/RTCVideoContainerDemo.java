/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoContainer;
import com.codename1.webrtc.RTCVideoElement;

/**
 *
 * @author shannah
 */
public class RTCVideoContainerDemo extends Form implements AutoCloseable{
    private RTCVideoContainer video1, video2;
    private Container videos;
    Button start = new Button("Start");
    Button call = new Button("Call");
    Button hangup = new Button("Hangup");
    
    public RTCVideoContainerDemo() {
        super("RTCVideoContainer Demo",new BorderLayout());
        
        videos = new Container(new GridLayout(1, 2));
        videos.setPreferredH(CN.getDisplayHeight()/2);
        
        
        $(call, hangup).setEnabled(false);
        start.addActionListener(evt->start());
        call.addActionListener(evt->call());
        hangup.addActionListener(evt->hangup());
        
        add(BorderLayout.CENTER, videos);
        add(BorderLayout.NORTH, GridLayout.encloseIn(3, start, call, hangup));
        
    }
    
    private void start() {
        start.setEnabled(false);
        call.setEnabled(true);
        RTC.createRTC().then(rtc->{
            return rtc.getUserMedia(new MediaStreamConstraints().video(true));
        }).then(stream -> {
            video1 = new RTCVideoContainer((MediaStream)stream);
            videos.addComponent(0, video1);
            revalidateWithAnimationSafety();
            return null;
        });
        
    }
    
    private void call() {
        call.setEnabled(false);
        if (video2 != null && video2.getSrcObject() != null) {
            try {
                RTC.getRTC(video2.getSrcObject()).close();
            } catch (Throwable t){}
        }
        RTC.createRTC().then(rtc->{
            return rtc.importStream(video1.getSrcObject());
        }).then(stream -> {
            if (video2 == null) {
                video2 = new RTCVideoContainer((MediaStream)stream);
                videos.addComponent(video2);
            } else {
                video2.setSrcObject((MediaStream)stream);
            }
            hangup.setEnabled(true);
            revalidateWithAnimationSafety();
            return null;
        });
        
        
    }
    
    private void hangup() {
        hangup.setEnabled(false);
        MediaStream stream = video2.getSrcObject();
        for (MediaStreamTrack track : stream.getTracks()) {
            track.stop();
        }
        video2.setSrcObject(null);
        call.setEnabled(true);
        
    }
    
    @Override
    public void close() throws Exception {
        if (video1 != null) {
            if (video1.getSrcObject() != null) {
                RTC.getRTC(video1.getSrcObject()).close();
            }
            video1 = null;
        }
        
        if (video2 != null) {
            if (video2.getSrcObject() != null) {
            
                RTC.getRTC(video2.getSrcObject()).close();
            }
            video2 = null;
        }
    }
    
}
