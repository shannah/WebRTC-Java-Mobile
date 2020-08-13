#import <Foundation/Foundation.h>

@class iosrtcPlugin;

@interface com_codename1_webrtc_compat_cordova_CordovaNativeImpl : NSObject {
    
    iosrtcPlugin* _iosrtcPlugin;
}
-(void)pluginInitialize:(UIView*)param;
-(void)dispose;
-(BOOL)execute:(NSString*) callbackId param1:(NSString*)action param2:(NSString*)data;
-(BOOL)isSupported;
@end
