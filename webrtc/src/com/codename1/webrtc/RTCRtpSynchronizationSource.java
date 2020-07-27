/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The RTCRtpSynchronizationSource dictionary of the the WebRTC API is used by
 * getSynchronizationSources() to describe a particular synchronization source
 * (SSRC). A synchronization source is a single source that shares timing and
 * sequence number space. Since RTCRtpSynchronizationSource implements
 * RTCRtpContributingSource, its properties are also available.
 *
 * The information provided is based on the last ten seconds of media received.
 *
 * @author shannah
 * @see
 * https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpSynchronizationSource
 */
public class RTCRtpSynchronizationSource extends RTCRtpContributingSource {

}
