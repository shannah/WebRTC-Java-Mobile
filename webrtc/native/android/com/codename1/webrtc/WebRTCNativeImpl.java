package com.codename1.webrtc;

import android.Manifest;
import static com.codename1.impl.android.AndroidImplementation.checkForPermission;

public class WebRTCNativeImpl {
    public void requestPermissions(String callbackId, boolean audio, boolean video) {
        
        if(audio && !checkForPermission(Manifest.permission.RECORD_AUDIO, "This is required to record audio")){
            RTC.permissionCallback(callbackId, false, false);
            return;
        }
        if(audio && !checkForPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS, "This is required to record audio")){
            RTC.permissionCallback(callbackId, false, false);
            return;
        }
        if (video && !checkForPermission(Manifest.permission.CAMERA, "This is required to record video")) {
            RTC.permissionCallback(callbackId, false, false);
            return;
        }
        RTC.permissionCallback(callbackId, audio, video);
    }

    public boolean isSupported() {
        return true;
    }

}
