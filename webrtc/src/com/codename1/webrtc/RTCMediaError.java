/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * The MediaError interface represents an error which occurred while handling
 * media in an HTML media element based on HTMLMediaElement, such as <audio> or
 * <video>.
 *
 * A MediaError object describes the error in general terms using a numeric code
 * categorizing the kind of error, and a message, which provides specific
 * diagnostics about what went wrong.
 *
 * @author shannah
 * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaError
 */
public class RTCMediaError extends RuntimeException {

    public RTCMediaError(String message, int code) {
        super(message);
        switch (code) {
            case 1:
                this.code = MediaErrorCode.Aborted;
                break;
            case 2:
                this.code = MediaErrorCode.Network;
                break;
            case 3:
                this.code = MediaErrorCode.Decode;
                break;
            case 4:
                this.code = MediaErrorCode.NotSupported;
        }
    }

    /**
     * A number which represents the general type of error that occurred, as
     * follows:
     *
     * @return the code
     */
    public MediaErrorCode getCode() {
        return code;
    }
    private String message;
    private MediaErrorCode code;

    public static enum MediaErrorCode {
        /**
         * The fetching of the associated resource was aborted by the user's
         * request.
         */
        Aborted(1),
        /**
         * Some kind of network error occurred which prevented the media from
         * being successfully fetched, despite having previously been available.
         */
        Network(2),
        /**
         * Despite having previously been determined to be usable, an error
         * occurred while trying to decode the media resource, resulting in an
         * error.
         */
        Decode(3),
        /**
         * The associated resource or media provider object (such as a
         * MediaStream) has been found to be unsuitable.
         */
        NotSupported(4);

        int code;

        MediaErrorCode(int code) {
            this.code = code;
        }
    }

}
