#import <Foundation/Foundation.h>
#import "com_codename1_webrtc_compat_cordova_CordovaNativeImpl.h"
#import "IosrtcPluginCommandDelegateImpl.h"
#import <objc/message.h>
#import <WebKit/WebKit.h>
@import IosRtcPlugin;



@implementation com_codename1_webrtc_compat_cordova_CordovaNativeImpl



-(iosrtcPlugin*)getIosrtcPlugin {
    
    return _iosrtcPlugin;
}


-(void)pluginInitialize:(UIView*)param {
    WKWebView* webView = (WKWebView*)param;
     if (_iosrtcPlugin == nil) {
        _iosrtcPlugin = [[iosrtcPlugin alloc] init];
        //[_iosrtcPlugin setValue:webView forKey:@"webView"];
        [_iosrtcPlugin setWebView:webView];
        _iosrtcPlugin.commandDelegate = [[IosrtcPluginCommandDelegateImpl alloc] init];
        [_iosrtcPlugin pluginInitialize];
    }
}
-(void)dispose {
    if (_iosrtcPlugin != nil) {
        [_iosrtcPlugin setWebView:nil];
        _iosrtcPlugin.commandDelegate = nil;
        [_iosrtcPlugin dispose];
        _iosrtcPlugin = nil;

    }
}
-(BOOL)execute:(NSString*) callbackId param1:(NSString*)param1 param2:(NSString*)param2{
//-(BOOL)execute:(NSString*)param param1:(NSString*)param1{
    if (![NSThread isMainThread]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self execute:callbackId param1:param1 param2:param2];
            
        });
        return YES;
    }
    NSData* data = [param2 dataUsingEncoding:NSUTF8StringEncoding];
    NSError *e;
    NSArray *array = nil;
    if (param2 == nil || [param2 length] == 0) {
        array = [NSArray array];
    } else {
        array = [NSJSONSerialization JSONObjectWithData:data options:nil error:&e];
    }
    CDVInvokedUrlCommand* command = [[CDVInvokedUrlCommand alloc] initWithArguments:array callbackId:callbackId className:@"iosrtcPlugin" methodName:param1];
    return [self execute:command];
}

- (BOOL)execute:(CDVInvokedUrlCommand*)command
{
    BOOL retVal = YES;
    double started = [[NSDate date] timeIntervalSince1970] * 1000.0;
    CDVPlugin* obj = [self getIosrtcPlugin];
    NSString* methodName = [NSString stringWithFormat:@"%@:", command.methodName];
    SEL normalSelector = NSSelectorFromString(methodName);
    if ([obj respondsToSelector:normalSelector]) {
        ((void (*)(id, SEL, id))objc_msgSend)(obj, normalSelector, command);
    } else {
        // There's no method to call, so throw an error.
        NSLog(@"ERROR: Method '%@' not defined in Plugin '%@'", methodName, command.className);
        retVal = NO;
    }
    double elapsed = [[NSDate date] timeIntervalSince1970] * 1000.0 - started;
    if (elapsed > 10) {
        NSLog(@"THREAD WARNING: ['%@'] took '%f' ms. Plugin should use a background thread.", command.className, elapsed);
    }
    return retVal;
}
-(BOOL)isSupported{
    return YES;
}

-(void)dealloc {
    if (_iosrtcPlugin != nil) {
        [_iosrtcPlugin dispose];
        _iosrtcPlugin = nil;
    }
    [super dealloc];
}
@end
