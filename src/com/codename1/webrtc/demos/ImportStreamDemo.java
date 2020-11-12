/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;

/**
 *
 * @author shannah
 */
public class ImportStreamDemo extends Form implements AutoCloseable{
    private RTC rtc1, rtc2;
    private RTCVideoElement video1, video2;
    
    public ImportStreamDemo() {
        super("Import Stream Demo",new BorderLayout());
        Button start = new Button("Start");
        Button call = new Button("Call");
        Button hangup = new Button("Hangup");
        Container videos = new Container(new GridLayout(1, 2));
        videos.setPreferredH(CN.getDisplayHeight()/2);
        
        RTC.createRTC().onSuccess(rtc->{
            rtc1 = rtc;
            video1 = rtc.createVideo();
            video1.setAutoplay(true);
            video1.applyStyle("width:100%;height:100%");
            rtc1.append(video1);
            if (videos.getComponentCount() > 0) {
                videos.addComponent(0, rtc1.getVideoComponent());
            } else {
                videos.add(rtc1.getVideoComponent());
            }
            revalidateWithAnimationSafety();
        });
        
        RTC.createRTC().onSuccess(rtc->{
            rtc2 = rtc;
            video2 = rtc.createVideo();
            video2.setAutoplay(true);
            video2.applyStyle("width:100%;height:100%");
            rtc2.append(video2);
            videos.add(rtc2.getVideoComponent());
            revalidateWithAnimationSafety();
        });
        
        start.addActionListener(evt->start());
        call.addActionListener(evt->call());
        hangup.addActionListener(evt->hangup());
        
        add(BorderLayout.CENTER, videos);
        add(BorderLayout.NORTH, GridLayout.encloseIn(3, start, call, hangup));
        
    }
    
    private void start() {
        rtc1.getUserMedia(new MediaStreamConstraints().video(true)).onSuccess(stream->{
            Log.p("Got stream "+stream, Log.DEBUG);
            video1.setSrcObject(stream);
        });
        
    }
    
    private void call() {
        rtc2.importStream(video1.getSrcObject()).onSuccess(stream->{
            Log.p("Got external stream "+stream, Log.DEBUG);
            video2.setSrcObject(stream);
        });
    }
    
    private void hangup() {
        MediaStream stream = video2.getSrcObject();
        for (MediaStreamTrack track : stream.getTracks()) {
            track.stop();
        }
        video2.setSrcObject(null);
        
    }
    
    @Override
    public void close() throws Exception {
        if (rtc1 != null) {
            rtc1.close();
            rtc1 = null;
        }
        
        if (rtc2 != null) {
            rtc2.close();
            rtc2 = null;
        }
    }
    
}
