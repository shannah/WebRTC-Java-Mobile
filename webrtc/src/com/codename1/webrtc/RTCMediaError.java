/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
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
     * @return the code
     */
    public MediaErrorCode getCode() {
        return code;
    }
    private String message;
    private MediaErrorCode code;
    
    public static enum MediaErrorCode {
        Aborted(1),
        Network(2),
        Decode(3),
        NotSupported(4);
        
        int code;
        MediaErrorCode(int code) {
            this.code = code;
        }
    }
    
}
