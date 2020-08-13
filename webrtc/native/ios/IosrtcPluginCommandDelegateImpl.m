/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

#import "CDVCommandDelegateImpl.h"
#import "CDVCommandQueue.h"
#import "CDVPluginResult.h"
#import "CDVViewController.h"
#import "com_codename1_webrtc_compat_cordova_CordovaCallbackManager.h"

@implementation IosrtcPluginCommandDelegateImpl : NSObject

//@synthesize urlTransformer;


- (NSString*)pathForResource:(NSString*)resourcepath
{
    return nil;
}

- (void)flushCommandQueueWithDelayedJs
{
    //_delayResponses = YES;
    //[_commandQueue executePending];
    //_delayResponses = NO;
}

- (void)evalJsHelper2:(NSString*)js
{
    
}

- (void)evalJsHelper:(NSString*)js
{
    
}

- (BOOL)isValidCallbackId:(NSString*)callbackId
{
    
    return YES;
}

- (void)sendPluginResult:(CDVPluginResult*)result callbackId:(NSString*)callbackId
{
    CDV_EXEC_LOG(@"Exec(%@): Sending result. Status=%@", callbackId, result.status);
    // This occurs when there is are no win/fail callbacks for the call.
    if ([@"INVALID" isEqualToString:callbackId]) {
        return;
    }
    // This occurs when the callback id is malformed.
    if (![self isValidCallbackId:callbackId]) {
        NSLog(@"Invalid callback id received by sendPluginResult");
        return;
    }
    int status = [result.status intValue];
    BOOL keepCallback = [result.keepCallback boolValue];
    NSString* argumentsAsJSON = [result argumentsAsJSON];
    BOOL debug = NO;
    BOOL isSuccess = [result.status intValue] == 0 || [result.status intValue] == 1;
#ifdef DEBUG
    debug = YES;
#endif

    //NSString* js = [NSString stringWithFormat:@"cordova.require('cordova/exec').nativeCallback('%@',%d,%@,%d, %d)", callbackId, status, argumentsAsJSON, keepCallback, debug];

    //[self evalJsHelper:js];
    //NSLog(@"Result %@", argumentsAsJSON);
    //public static void sendResult(String callbackId, boolean isSuccess, int status, String args, boolean keepCallback){
    com_codename1_webrtc_compat_cordova_CordovaCallbackManager_sendResult___java_lang_String_boolean_int_java_lang_String_boolean(
        CN1_THREAD_GET_STATE_PASS_ARG 
        fromNSString(CN1_THREAD_GET_STATE_PASS_ARG callbackId),
        isSuccess,
        [result.status intValue],
        fromNSString(CN1_THREAD_GET_STATE_PASS_ARG argumentsAsJSON),
        keepCallback
    );
    
}

- (void)evalJs:(NSString*)js
{
    //[self evalJs:js scheduledOnRunLoop:YES];
}

- (void)evalJs:(NSString*)js scheduledOnRunLoop:(BOOL)scheduledOnRunLoop
{
    
}

- (id)getCommandInstance:(NSString*)pluginName
{
    //return [_viewController getCommandInstance:pluginName];
    return nil;
}

- (void)runInBackground:(void (^)())block
{
    //dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), block);
}

- (NSString*)userAgent
{
    //return [_viewController userAgent];
    return nil;
}

- (NSDictionary*)settings
{
    //return _viewController.settings;
    return nil;
}

@end
