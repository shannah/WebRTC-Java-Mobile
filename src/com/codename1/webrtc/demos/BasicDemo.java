/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.webrtc.AudioTrackConstraints;
import com.codename1.webrtc.ConstrainBoolean;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;

/**
 *
 * @author shannah
 */
public class BasicDemo extends Form {
    public BasicDemo() {
        super("Basic WebRTC Demo", new BorderLayout());
        Form hi = this;
        RTC.createRTC().ready(rtc->{
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
            });
            hi.add(BorderLayout.CENTER, rtc.getVideoComponent());
            hi.revalidate();
        });
    }
}
