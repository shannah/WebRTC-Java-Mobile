/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;

/**
 * This sample was adapted from https://webrtc.github.io/samples/src/content/getusermedia/gum/
 * 
 * The source for that sample is available at https://github.com/webrtc/samples/tree/gh-pages/src/content/getusermedia/gum
 * 
 * @author shannah
 */
public class BasicDemo extends Form implements AutoCloseable {
    private RTC rtc;
    public BasicDemo() {
        super("Basic WebRTC Demo", new BorderLayout());
        Form hi = this;
        String intro = "This sample was adapted from the \"Basic getUserMedia() Demo\" on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/BasicDemo.java"));
        hi.add(BorderLayout.NORTH, BoxLayout.encloseY(new SpanLabel(intro), viewSource));
        RTC.createRTC().ready(rtc->{
            this.rtc = rtc;
            MediaStreamConstraints constraints = new MediaStreamConstraints()
                    .audio()
                    .echoCancellation(true)
                    .noiseSuppression(true)
                    .video(true)
                    .stream();

           
            rtc.getUserMedia(constraints).then(stream->{
                RTCVideoElement video = rtc.createVideo();
                video.setAutoplay(true);
                video.setSrcObject(stream);
                rtc.append(video);
            }).onCatch(error-> {
                System.out.println("Failed to get user media: "+error.getMessage());
                Log.e(error);
            });
            hi.add(BorderLayout.CENTER, rtc.getVideoComponent());
            hi.revalidate();
        });
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
}
