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
 * The MediaStreamConstraints dictionary is used when calling {@link RTC#getUserMedia(com.codename1.webrtc.MediaStreamConstraints) } to
 * specify what kinds of tracks should be included in the returned MediaStream,
 * and, optionally, to establish constraints for those tracks' settings.
 *
 * @author shannah
 */
public class MediaStreamConstraints implements JSONStruct {

    /**
     * a MediaTrackConstraints object providing the constraints which must be met by the audio track included in the returned MediaStream. If constraints are specified, an audio track is inherently requested.
     * @return the audioConstraints
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/audio
     */
    public AudioTrackConstraints getAudioConstraints() {
        return audioConstraints;
    }

    /**
     * a MediaTrackConstraints object providing the constraints which must be met by the audio track included in the returned MediaStream. If constraints are specified, an audio track is inherently requested.
     * @param audioConstraints the audioConstraints to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/audio
     */
    public void setAudioConstraints(AudioTrackConstraints audioConstraints) {
        this.audioConstraints = audioConstraints;
    }

    /**
     * a MediaTrackConstraints object providing the constraints which must be met by the video track included in the returned MediaStream. If constraints are specified, a video track is inherently requested.
     * @return the videoConstraints
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/video
     */
    public VideoTrackConstraints getVideoConstraints() {
        return videoConstraints;
    }

    /**
     * a MediaTrackConstraints object providing the constraints which must be met by the video track included in the returned MediaStream. If constraints are specified, a video track is inherently requested.
     * @param videoConstraints the videoConstraints to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/video
     */
    public void setVideoConstraints(VideoTrackConstraints videoConstraints) {
        this.videoConstraints = videoConstraints;
    }

    /**
     * a Boolean (which indicates whether or not an audio track is requested)
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/audio
     */
    public Boolean isAudio() {
        return audio;
    }

    /**
     * a Boolean (which indicates whether or not an audio track is requested)
     * @param audio the audio to set
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/audio
     */
    public void setAudio(Boolean audio) {
        this.audio = audio;
        if (audio != null) {
            audioConstraints = null;
        }
    }

    /**
     * a Boolean (which indicates whether or not a video track is requested)
     * @return the video
     */
    public Boolean isVideo() {
        return video;
    }

    /**
     * a Boolean (which indicates whether or not a video track is requested)
     * @param video the video to set
     */
    public void setVideo(Boolean video) {
        this.video = video;
        if (video != null) {
            videoConstraints = null;
        }
    }
    private Boolean audio, video;
    private AudioTrackConstraints audioConstraints;
    private VideoTrackConstraints videoConstraints;

    public String toJSON() {

        return Result.fromContent((Map) toJSONStruct()).toString();

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

    /**
     * Gets audio constraints associated with this stream constraints object.  If 
     * it doesn't have any constraints yet, it will create a new {@link AudioTrackConstraints}
     * object and associate that with the stream constraints.
     * @return 
     * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStreamConstraints/audio
     */
    public AudioTrackConstraints audio() {
        if (audioConstraints == null) {
            audioConstraints = new AudioTrackConstraints(this);
        }
        return audioConstraints;
    }

    /**
     * Sets the audio constraint and returns self for chaining.
     * @param audio
     * @return 
     */
    public MediaStreamConstraints audio(boolean audio) {
        setAudio(audio);
        return this;
    }

    public MediaStreamConstraints video(boolean video) {
        setVideo(video);
        return this;
    }

    /**
     * Returns true if the provided constraints object is requesting audio access at all.
     * @param constraints
     * @return 
     */
    public static boolean isAudioRequested(MediaStreamConstraints constraints) {
        return (constraints.audio != null && constraints.audio) || constraints.audioConstraints != null;
    }

    /**
     * Returns true if the provided constraints object is requesting video access at all.
     * @param constraints
     * @return 
     */
    public static boolean isVideoRequested(MediaStreamConstraints constraints) {
        return (constraints.video != null && constraints.video) || constraints.videoConstraints != null;
    }

}
