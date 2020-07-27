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
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.webrtc.ConstrainNumber;
import com.codename1.webrtc.Event;
import com.codename1.webrtc.MediaStream;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.MediaStreamTrack;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;
import com.codename1.webrtc.VideoTrackConstraints;

/**
 * This demo was adapted from https://webrtc.github.io/samples/src/content/getusermedia/resolution/
 * 
 * Source code for original Javscript sample: https://github.com/webrtc/samples/blob/gh-pages/src/content/getusermedia/resolution/js/main.js
 * 
 * 
 * @author shannah
 */
public class ConstraintsDemo extends Form implements AutoCloseable {
    private MediaStream stream;
    private RTCVideoElement video;
    private RTC rtc;
    private SpanLabel messagebox = new SpanLabel();
    private Component videoblock;
    private TextField widthInput = new TextField();
    private Label widthOutput = new Label(), heightOutput = new Label();
    private Label dimensions = new Label();
    private int currentWidth, currentHeight;
    private CheckBox aspectLock = new CheckBox("Lock aspect ratio"), sizeLock = new CheckBox("Lock video size");
    private Button vgaButton=new Button("VGA"), 
            qvgaButton = new Button("QVGA"), 
            hdButton = new Button("HD"), 
            fullHdButton = new Button("Full HD"), 
            fourKButton = new Button("4K"), 
            eightKButton = new Button("8K");
    
    private static MediaStreamConstraints createConstraints(int width, int height) {
        return new MediaStreamConstraints()
            .video()
            .width(ConstrainNumber.exact(width))
            .height(ConstrainNumber.exact(height))
            .stream();
    }

    private static final MediaStreamConstraints qvgaConstraints = createConstraints(320, 240),
            vgaConstraints = createConstraints(640, 480),
            hdConstraints = createConstraints(1280, 720),
            fullHdConstraints = createConstraints(1920, 1080),
            fourKConstraints = createConstraints(4096, 2160),
            eightKConstraints = createConstraints(7680, 4320);
            
    
    
    
    private void gotStream(MediaStream stream) {
        this.stream = stream;
        if (video == null) {
            video = rtc.createVideo();
            video.setAutoplay(true);
            rtc.append(video);
        }
        video.setSrcObject(stream);
        messagebox.setVisible(false);
        videoblock.setVisible(true);
        MediaStreamTrack track = stream.getVideoTracks().get(0);
        VideoTrackConstraints constraints = (VideoTrackConstraints)track.getConstraints();
        System.out.println("Result constraints: " + constraints.toJSONStruct());
        
        if (constraints != null && constraints.getWidth() != null && constraints.getWidth().getExact() != null) {
            widthInput.setText(String.valueOf(constraints.getWidth().getExact()));
            widthOutput.setText(String.valueOf(constraints.getWidth().getExact())+"px");
        } else if (constraints != null && constraints.getWidth() != null && constraints.getWidth().getMin() != null) {
          widthInput.setText(constraints.getWidth().getMin()+"");
          widthOutput.setText(constraints.getWidth().getMin()+"px");
        }
    }
    
    private void errorMessage(String fromWhere, String msg) {
        messagebox.setVisible(true);
        messagebox.setText(msg);
        revalidate();
        System.out.println(fromWhere+": "+msg);
    }
    
    private void clearErrorMessage() {
        messagebox.setVisible(false);
        revalidate();
    }


    private void displayVideoDimensions(String whereSeen) {
        if (video.getVideoWidth() > 0) {
            dimensions.setText("Actual video dimensions: "+video.getVideoWidth() + "x" + video.getVideoHeight() + "px.");
            if (currentWidth != video.getVideoWidth() || currentHeight != video.getVideoHeight()) {
                System.out.println(whereSeen+": "+dimensions.getText());
                currentWidth = video.getVideoWidth();
                currentHeight = video.getVideoHeight();
            }
        } else {
            dimensions.setText("Video not ready");
        }
        revalidate();
    }
    
    private void constraintChange(ActionEvent e) {
        TextArea src = (TextArea)e.getSource();
        int val;
        try {
            val = Integer.parseInt(src.getText());
        } catch (Throwable t) {
            Log.e(t);
            return;
        }
        widthOutput.setText(src.getText());
        MediaStreamTrack track = stream.getVideoTracks().get(0);
        VideoTrackConstraints constraints;
        if (aspectLock.isSelected()) {
            constraints = new MediaStreamConstraints()
                    .video()
                    .width(ConstrainNumber.exact(val))
                    .aspectRatio(ConstrainNumber.exact(video.getVideoWidth()/(double)video.getVideoHeight()));
        } else {
            constraints = new MediaStreamConstraints()
                    .video()
                    .width(ConstrainNumber.exact(val));
        }
        clearErrorMessage();
        System.out.println("applying "+constraints.toJSONStruct());
        track.applyConstraints(constraints)
                .then(res->{
                    System.out.println("applyConstraint success");
                    displayVideoDimensions("applyConstraints");
                })
                .onCatch(err->{
                    errorMessage("applyConstraints", ((Throwable)err).getMessage());
                });
        
    }
    
    private void getMedia(MediaStreamConstraints constraints) {
        if (stream != null) {
            for (MediaStreamTrack track : stream.getTracks()) {
                track.stop();
            }
            stream.release();
            stream = null;
            
        }
        clearErrorMessage();
        videoblock.setVisible(false);
        rtc.getUserMedia(constraints).then(stream->{
            gotStream(stream);
        }).onCatch(err -> {
            errorMessage("getUserMedia", err.getMessage());
        });
        
        
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
    
    private static class Link extends Button {
        private String href;
        public Link(String label, String href) {
            super(label);
            setUIID("Label");
            
            this.href = href;
            addActionListener(evt->{
                CN.execute(href);
            });
        }
    }
    
    
    public ConstraintsDemo() {
        super("Constraints Demo", new BorderLayout());
        Container north = new Container(BoxLayout.y());
        String intro = "This sample was adapted from the \"getUserMedia(): select resolution\" sample on the WebRTC web site.";
        Button viewSource = new Button("View Source");
        FontImage.setMaterialIcon(viewSource, FontImage.MATERIAL_LINK);
        viewSource.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC/blob/master/src/com/codename1/webrtc/demos/ConstraintsDemo.java"));
        north.add(BoxLayout.encloseY(new SpanLabel(intro), viewSource));
        
        north.add(FlowLayout.encloseIn(new Label("WebRTC samples"), new Label("getUserMedia: select resolution")));
        north.add(FlowLayout.encloseIn(new Label("This example uses"), new Link("constraints", "https://w3c.github.io/mediacapture-main/getusermedia.html#media-track-constraints")));
        north.add(new SpanLabel("Click a button to call getUserMedia() with appropriate resolution."));
        north.add(FlowLayout.encloseIn(qvgaButton, vgaButton, hdButton, fullHdButton, fourKButton, eightKButton));
        north.add(dimensions);
        add(BorderLayout.NORTH, north);
        
        Container south = new Container(BoxLayout.y());
        south.add(BoxLayout.encloseX(new Label("Width: "), widthOutput, new Label(":"), widthInput));
        south.add(sizeLock);
        south.add(aspectLock);
        south.add(new Label("For more information see..."));
        south.add(new Link("View source on Github", "http://example.com"));
        
        Container center = new Container(new LayeredLayout());
        center.add(messagebox);
        add(BorderLayout.CENTER, center);
        
        
        RTC.createRTC().ready(rtc_->{
            rtc = rtc_;
            videoblock = rtc.getVideoComponent();
            center.add(videoblock);
            video = rtc.createVideo();
            video.setAutoplay(true);
            rtc.append(video);
            video.addEventListener("loadedmetadata", evt->{
                displayVideoDimensions("loadedmetadata");
            });
            video.addEventListener("resize", evt->{
                displayVideoDimensions("resize");
            });
            widthInput.addActionListener(evt->{
                constraintChange(evt);
            });
            sizeLock.addActionListener(evt->{
                if (sizeLock.isSelected()) {
                    System.out.println("Setting fixed size");
                    //.. Not worred about this yet
                }
            });
            vgaButton.addActionListener(evt->getMedia(vgaConstraints));
            qvgaButton.addActionListener(evt->getMedia(qvgaConstraints));
            hdButton.addActionListener(evt->getMedia(hdConstraints));
            fullHdButton.addActionListener(evt->getMedia(fullHdConstraints));
            fourKButton.addActionListener(evt->getMedia(fourKConstraints));
            eightKButton.addActionListener(evt->getMedia(eightKConstraints));
            
            
        });
    }
    
    
}
