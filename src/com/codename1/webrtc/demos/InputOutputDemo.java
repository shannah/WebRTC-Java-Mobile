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
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.webrtc.ConstrainString;
import com.codename1.webrtc.MediaDeviceInfo;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.Promise;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;

/**
 * Adapted from https://webrtc.github.io/samples/src/content/devices/input-output/
 * @author shannah
 */
public class InputOutputDemo extends Form implements AutoCloseable {
    private RTC rtc;
    private RTCVideoElement videoElement;
    private ComboBox audioInputSelect = new ComboBox();
    private ComboBox audioOutputSelect = new ComboBox();
    private ComboBox videoSelect = new ComboBox();
    private ComboBox[] selectors = new ComboBox[]{audioInputSelect, audioOutputSelect, videoSelect};
    private MediaStream stream;

    public InputOutputDemo() {
        super("Select sources & outputs", new BorderLayout());
        
        RTC.createRTC().onSuccess(rtc->{
            InputOutputDemo.this.rtc = rtc;
            
            rtc.enumerateDevices().onSuccess(devices->gotDevices(devices))
                    .onFail(error->handleError((Throwable)error));
            
            SpanLabel lbl = new SpanLabel("Get available audio, video sources and audio output devices from mediaDevices.enumerateDevices() then set the source for getUserMedia() using a deviceId constraint.");
            Button viewSource = new Button("View Source");
            FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
            viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/InputOutputDemo.java"));

            
            Container inputsCnt = BoxLayout.encloseY(
                    lbl,
                    viewSource,
                    FlowLayout.encloseIn(new Label("Audio input source:"), audioInputSelect),
                    FlowLayout.encloseIn(new Label("Audio output destination:"), audioOutputSelect),
                    FlowLayout.encloseIn(new Label("Video source:"), videoSelect)
                    
            );
            add(BorderLayout.NORTH, inputsCnt);
            
            videoElement = rtc.createVideo();
            videoElement.applyStyle("width:100%;height:100%");
            videoElement.setAutoplay(true);
                    
            rtc.append(videoElement);
            add(BorderLayout.CENTER, rtc.getVideoComponent());
            
            audioInputSelect.addActionListener(evt->start());
            audioOutputSelect.addActionListener(evt->changeAudioDestination());
            videoSelect.addActionListener(evt->start());
            revalidateWithAnimationSafety();
            

            start();
        });
    }
    
    private class Option {
        private String value;
        private String text;

        @Override
        public String toString() {
            return text;
        }
        
        
    }
    
    
    public void gotDevices(MediaDeviceInfo[] deviceInfos) {
        System.out.println("in gotDevices");
        String[] values = new String[selectors.length];
        for (int i=0; i<selectors.length; i++) {
            values[i] = selectors[i].getModel().getSize() == 0 ? null : ((Option)selectors[i].getSelectedItem()).value;
            while (selectors[i].getModel().getSize() > 0) {
                selectors[i].getModel().removeItem(0);
            }
        }
        
        for (int i=0; i < deviceInfos.length; i++) {
            MediaDeviceInfo deviceInfo = deviceInfos[i];
            Option option = new Option();
            option.value = deviceInfo.getDeviceId();
            option.text = deviceInfo.getLabel();
            switch (deviceInfo.getKind()) {
                case AudioInput:
                    if (option.text == null || option.text.isEmpty()) {
                        option.text = "microphone "+(audioInputSelect.getModel().getSize()+1);
                    }
                    audioInputSelect.getModel().addItem(option);
                    break;
                    
                case AudioOutput:
                    if (option.text == null || option.text.isEmpty()) {
                        option.text = "speaker "+(audioOutputSelect.getModel().getSize()+1);
                    }
                    audioOutputSelect.getModel().addItem(option);
                    break;
                    
                case VideoInput:
                    if (option.text == null || option.text.isEmpty()) {
                        option.text = "camera "+(videoSelect.getModel().getSize()+1);
                    }
                    videoSelect.getModel().addItem(option);
                    break;
                default:
                    Log.p("Some other kind of source/device: "+ deviceInfo);
            }
            
        }
        
        for (int i=0; i<selectors.length; i++) {
            ComboBox selector = selectors[i];
            for (int j=0; j<values.length; j++) {
                String value = values[j];
                for (int k=0; k<selector.getModel().getSize(); k++) {
                    Option option = (Option)selector.getModel().getItemAt(k);
                    if (option.value.equals(value)) {
                        selector.setSelectedItem(option);
                        break;
                    }
                }
            }
        }
        revalidateWithAnimationSafety();

    }
        
    
    private void attachSinkId(RTCVideoElement element, String sinkId) {
        if (element.getSinkId() == null) {
            element.setSinkId(sinkId).onSuccess(res->Log.p("Success, audio output device attached: "+sinkId))
                    .onFail(err->{
                Throwable error = (Throwable)err;
                Log.e(error);
                audioOutputSelect.setSelectedIndex(0);
            });
                
        } else {
            Log.p("Browser does not support output device selection.", Log.WARNING);
        }
        
    }
    
    private void changeAudioDestination() {
        String audioDestination = ((Option)audioOutputSelect.getSelectedItem()).value;
        attachSinkId(videoElement, audioDestination);
        
    }
    
    private Promise<MediaDeviceInfo[]> gotStream(MediaStream stream) {
        this.stream = stream;
        System.out.println("Setting stream");
        videoElement.setSrcObject(stream);
        return rtc.enumerateDevices();
        
    }
    
    void handleError(Throwable error) {
        Log.p("RTC.getuserMedia error: " + error.getMessage(), Log.ERROR);
        Log.e(error);
    }
    
    private void start() {
        if (stream != null) {
            for (MediaStreamTrack track : stream.getTracks()) {
                track.stop();
            }
        }
        
        String audioSource = (audioInputSelect.getModel().getSize() > 0) ? 
                ((Option)audioInputSelect.getSelectedItem()).value : 
                null;
        String videoSource = videoSelect.getModel().getSize() > 0 ?
                ((Option)videoSelect.getSelectedItem()).value :
                null;
        MediaStreamConstraints constraints = new MediaStreamConstraints();
        if (audioSource != null) {
            constraints.audio().deviceId(new ConstrainString().exact(audioSource));
        } else {
            constraints.audio(true);
        }
        if (videoSource != null) {
            constraints.video().deviceId(new ConstrainString().exact(videoSource));
        } else {
            constraints.video(true);
        }
        System.out.println("Audio source: "+audioSource);
        System.out.println("Video source: "+videoSource);
        System.out.println(constraints.toJSON());
        rtc.getUserMedia(constraints).then(stream -> {
           return gotStream((MediaStream)stream);
        }).onSuccess(devices->gotDevices((MediaDeviceInfo[])devices))
                .onFail(err-> handleError((Throwable)err));
 
    }
    
    
    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
    
}
