//
//  iosRtcPluginBase.m
//  IosRtcPlugin
//
//  Created by Steve Hannah on 2020-08-04.
//  Copyright © 2020 Steve Hannah. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IosRtcPlugin.h"
@implementation iosRtcPluginBase
-(UIView*)webView {
    return _webView;
}
-(void)setWebView:(UIView*)wv {
    _webView = wv;
}
@end
