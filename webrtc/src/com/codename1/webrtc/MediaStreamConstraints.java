/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import com.codename1.processing.Result;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class MediaStreamConstraints implements JSObject {
    
    
    

    /**
     * @return the audioConstraints
     */
    public AudioTrackConstraints getAudioConstraints() {
        return audioConstraints;
    }

    /**
     * @param audioConstraints the audioConstraints to set
     */
    public void setAudioConstraints(AudioTrackConstraints audioConstraints) {
        this.audioConstraints = audioConstraints;
    }

    /**
     * @return the videoConstraints
     */
    public VideoTrackConstraints getVideoConstraints() {
        return videoConstraints;
    }

    /**
     * @param videoConstraints the videoConstraints to set
     */
    public void setVideoConstraints(VideoTrackConstraints videoConstraints) {
        this.videoConstraints = videoConstraints;
    }

    public Boolean isAudio() {
        return audio;
    }
    
    /**
     * @param audio the audio to set
     */
    public void setAudio(Boolean audio) {
        this.audio = audio;
        if (audio != null && !audio){
            audioConstraints = null;
        }
    }

    /**
     * @return the video
     */
    public Boolean isVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(Boolean video) {
        this.video = video;
        if (video != null && !video) {
            videoConstraints = null;
        }
    }
    private Boolean audio, video;
    private AudioTrackConstraints audioConstraints;
    private VideoTrackConstraints videoConstraints;
    
    public String toJSON() {
        
        return Result.fromContent((Map)toJSONStruct()).toString();
        
    }

    @Override
    public Object toJSONStruct() {
        Map m = new LinkedHashMap();
        if (getAudioConstraints() != null) {
            m.put("audio", getAudioConstraints().toJSONStruct());
                    
        } else {
            if (audio != null) {
                m.put("audio", isAudio());
            }
        }
        if (getVideoConstraints() != null) {
            m.put("video", getVideoConstraints().toJSONStruct());
        } else {
            if (video != null) {
                m.put("video", isVideo());
            }
        }
        return m;
    }
    
    public VideoTrackConstraints video() {
        if (videoConstraints == null) {
            videoConstraints = new VideoTrackConstraints(this);
        }
        return videoConstraints;
    }
    
    public AudioTrackConstraints audio() {
        if (audioConstraints == null) {
            audioConstraints = new AudioTrackConstraints(this);
        }
        return audioConstraints;
    }
    
    public MediaStreamConstraints audio(boolean audio) {
        setAudio(audio);
        return this;
    }
    
    public MediaStreamConstraints video(boolean video) {
        setVideo(video);
        return this;
    }
    
    
    public static boolean isAudioRequested(MediaStreamConstraints constraints) {
        return (constraints.audio != null && constraints.audio) || constraints.audioConstraints != null;
    }
    
    public static boolean isVideoRequested(MediaStreamConstraints constraints) {
        return (constraints.video != null && constraints.video) || constraints.videoConstraints != null;
    }
    
}
