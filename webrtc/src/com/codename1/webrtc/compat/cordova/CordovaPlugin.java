/**
 * Copyright 2015 Codename One
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codename1.webrtc.compat.cordova;

import com.codename1.util.Callback;
import java.util.List;

/**
 * Interface for Cordova native plugins.
 * <p>This is really a "single-method" interface, but two variants of the "execute" method are
 * provided.  Codename One will try {@link #execute(java.lang.String, java.lang.String, com.codename1.util.Callback) }
 * first, and {@link #execute(java.lang.String, java.util.List, com.codename1.util.Callback) } the action was unhandled.
 * Typically you would only implement one of these methods, and just return <code>false</code> from the other.</p>
 * <p>This API is meant to closely mirror the <a href="https://cordova.apache.org/docs/en/latest/guide/platforms/android/plugin.html">Cordova Android Plugin API</a>.</p>
 * <p>For full instructions on devloping native Cordova Plugins in Codename One, see <a href="https://github.com/codenameone/CN1Cordova/wiki/Plugin-Development">the Plugin Development wiki page</a>.</p>
 * @author shannah
 * @see CordovaApplication#addPlugin(java.lang.String, com.codename1.cordova.CordovaPlugin) 
 * @see CordovaApplication#addGlobalPlugin(java.lang.String, com.codename1.cordova.CordovaPlugin) 
 */
public interface CordovaPlugin {
    
    /**
     * Handles "exec" callback from cordova.  With this version, the "args" have
     * been pre-parsed into primitive (rather boxed) types: Doubles and Strings mostly.
     * @param action The name of the action to perform.
     * @param args List of arguments passed to the action from cordova.  Possible types include:
     * <ul><li>String</li><li>Double</li><li>byte[]</li><li>Boolean</li></ul>
     * @param callback The callback to fire when execution is complete.  If the request is successful,
     * the method should call the callback.onSuccess() method.  If the request fails, it should call
     * the callback.onError() method.
     * @return Return true if the request was handled.  <code>false</code> otherwise.  If both variants
     * of <code>execute()</code> return <code>false</code>, then cordova will receive an action not found error.
     */
    public boolean execute(String callbackId, String action, List args, Callback callback);
    
    /**
     * Handles the "exec" callback from cordova.  With this version, the "args" are still unparsed in JSON
     * format.  This variant is provided when you would need to parse the JSON directly (e.g. when passing
     * to a native interface) and you don't want to waste the performance on having CN1 also parse it first.
     * @param action The name of the action to perform.
     * @param jsonArgs Arguments passed to the action from cordova encoded as a JSON string.
     * 
     * @param callback The callback to fire when execution is complete.  If the request is successful,
     * the method should call the callback.onSuccess() method.  If the request fails, it should call
     * the callback.onError() method.
     * @return <code>true</code> if the request was handled.  <code>false</code> otherwise.  If both variants
     * of <code>execute()</code> return <code>false</code>, then cordova will receive an action not found error.
     */
    public boolean execute(String callbackId, String action, String jsonArgs, Callback callback);
    
    public void dispose();
    
    public void pluginInitialize();
    public boolean isInitialized();
        
    
}
