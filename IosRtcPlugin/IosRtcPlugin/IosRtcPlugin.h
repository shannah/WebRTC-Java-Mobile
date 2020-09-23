//
//  IosRtcPlugin.h
//  IosRtcPlugin
//
//  Created by Steve Hannah on 2020-07-31.
//  Copyright © 2020 Steve Hannah. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

#import <WebRTC/RTCAudioSource.h>
#import <WebRTC/RTCAudioTrack.h>
#import <WebRTC/RTCDataChannel.h>
#import <WebRTC/RTCDtmfSender.h>
#import <WebRTC/RTCEAGLVideoView.h>
#import <WebRTC/RTCIceCandidate.h>
#import <WebRTC/RTCIceServer.h>
#import <WebRTC/RTCMediaConstraints.h>
#import <WebRTC/RTCMediaSource.h>
#import <WebRTC/RTCMediaStream.h>
#import <WebRTC/RTCMediaStreamTrack.h>
#import <WebRTC/RTCPeerConnection.h>
#import <WebRTC/RTCPeerConnectionFactory.h>
#import <WebRTC/RTCSessionDescription.h>
#import <WebRTC/RTCVideoCapturer.h>
#import <WebRTC/RTCVideoRenderer.h>
#import <WebRTC/RTCVideoSource.h>
#import <WebRTC/RTCVideoTrack.h>
@interface iosRtcPluginBase : CDVPlugin {
    UIView* _webView;
}
-(UIView*)webView;
-(void)setWebView:(UIView*)wv;
@end

//! Project version number for IosRtcPlugin.
FOUNDATION_EXPORT double IosRtcPluginVersionNumber;

//! Project version string for IosRtcPlugin.
FOUNDATION_EXPORT const unsigned char IosRtcPluginVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <IosRtcPlugin/PublicHeader.h>


