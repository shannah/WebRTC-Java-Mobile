/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.compat.cordova;


import ca.weblite.codename1.json.JSONObject;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.CN;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.Base64;
import com.codename1.util.Callback;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 * @author shannah
 */
public class EmbeddedCordovaApplication {
    
    
    /**
     * The browser component that houses the app.
     */
    private final BrowserComponent webview;
    

    
    /**
     * Map to store all of the plugins that are registered for this application.
     */
    private Map<String,CordovaPlugin> pluginMap = new HashMap<String,CordovaPlugin>();
    
    /**
     *  Map of plugins that will be added to all CordovaApplication objects by default.
     */
    private static Map<String,CordovaPlugin> globalPluginMap = new HashMap<String,CordovaPlugin>();
    
    /**
     * Shared JSON parser.  Instead of creating a new one for each request, since that
     * may be frequent.
     */
    private JSONParser jsonParser = new JSONParser();
    {
        jsonParser.setUseBooleanInstance(true);
    }
    
    /**
     * Result codes for passing back from Native to Javascript.  These match
     * the cordova result codes in cordova.js.
     */
    public static enum Result {
        NO_RESULT,
        OK,
        CLASS_NOT_FOUND_EXCEPTION,
        ILLEGAL_ACCESS_EXCEPTION,
        INSTANTIATION_EXCEPTION,
        MALFORMED_URL_EXCEPTION,
        IO_EXCEPTION,
        INVALID_ACTION,
        JSON_EXCEPTION,
        ERROR
    };
    
    /*
    private ArrayList massageForJSONEncoding(List list) {
        ArrayList out = (list instanceof ArrayList) ? (ArrayList)list : new ArrayList(list);
        int len = out.size();
        for (int i=0; i<len; i++) {
            Object item = out.get(i);
            if (item instanceof Map) {
                out.set(i, massageForJSONEncoding((Map)item));
            } else if (item instanceof List) {
                out.set(i, massageForJSONEncoding((List)item));
            }
        }
        return out;
    }
    
    private HashMap massageForJSONEncoding(Map map) {
        HashMap out = (map instanceof HashMap) ? (HashMap)map : new HashMap(map);
        for (Map.Entry e : (Set<Map.Entry>)map.entrySet()) {
            Object item = e.getValue();
            if (item instanceof List) {
                e.setValue(massageForJSONEncoding((List)item));
            } else if (item instanceof Map) {
                e.setValue(massageForJSONEncoding((Map)item));
            }
        }
        return out;
    }
    */
    
    /**
     * Constructor.  Creates a new form with the a web view.
     */
    public EmbeddedCordovaApplication(final BrowserComponent webview) {
        pluginMap.putAll(globalPluginMap);
        this.webview = webview;
        webview.addWebEventListener("onError", new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            System.out.println("There was an error: "+evt.getSource());
        
            }
        });
        
        
        Runnable installer = () -> {
            
            webview.addJSCallback("window.echo = function(arg) {callback.onSuccess(arg);}", res->{
                System.out.println(""+res.toString());
            });
            webview.addJSCallback("window.CN1Exec=function(callbackId, service, action, actionArgsJSON){callback.onSuccess(JSON.stringify({args:[callbackId, service, action, actionArgsJSON]}));};", res->{
                try {
                    Map resArgs = jsonParser.parseJSON(new StringReader(res.getValue()));
                    String[] arguments = (String[])((List)(resArgs.get("args"))).toArray(new String[4]);
                    final String callbackId = (String)arguments[0];
                    String service = (String)arguments[1];

                    CordovaPlugin plugin = pluginMap.get(service);
                    if (plugin == null) {
                        webview.execute("cordova.callbackError("+JSONObject.quote(callbackId)+", {message:'Plugin not found', status:"+Result.INVALID_ACTION.ordinal()+", keepCallback:false})");
                        return;
                    }
                    if (!plugin.isInitialized()) {
                        plugin.pluginInitialize();
                    }

                    String action = (String)arguments[2];
                    System.out.println("EmbeddedCordovaApplication#"+service+"#"+action);

                    Callback callback = new Callback() {

                        @Override
                        public void onSucess(Object value) {
                            Map result = (Map)value;
                            value = result.get("message");
                            int status = (Integer)result.get("status");
                            boolean keep = (Boolean)result.get("keepCallback");
                            boolean success = (Boolean)result.get("isSuccess");
                            System.out.println("EmbeddedCordovaApplication#"+service+"#"+action+" onSuccess");
                            //System.out.println("In CN onSuccessCallback.  Value is "+value);
                            /*
                            if (value != null) {
                                System.out.println("Value type: "+value.getClass().getName());
                                
                                if (value instanceof Map) {
                                    Map m = (Map)value;
                                    for (Object key : m.keySet()) {
                                        Object item = m.get(key);
                                        if (item != null) {
                                            System.out.println("Key="+key+", type ="+(item.getClass().getName()));
                                        }
                                    }
                                }
                            }
                            */
                            
                            String valueJSON = null;
                            if (value == null) {
                                valueJSON = "null";
                            } else if (value instanceof String) {
                                valueJSON = JSONObject.quote((String)value);
                            } else if (value instanceof List) {
                                //JSONArray jsonArr = new JSONArray(massageForJSONEncoding((List)value));
                                //valueJSON = jsonArr.toString();
                                Map wrap = new HashMap();
                                wrap.put("root", value);
                                valueJSON = com.codename1.processing.Result.fromContent(wrap).toString();
                                int startPos = valueJSON.indexOf(":")+1;
                                int endPos = valueJSON.lastIndexOf("}");
                                valueJSON = valueJSON.substring(startPos, endPos);
                            } else if (value instanceof Map) {
                                //JSONObject jsonObj = new JSONObject(massageForJSONEncoding((Map)value));
                                //valueJSON = jsonObj.toString();
                                valueJSON = com.codename1.processing.Result.fromContent((Map)value).toString();
                            } else if (value instanceof Number) {
                                try {
                                    valueJSON = JSONObject.numberToString(value);
                                } catch (Exception ex) {
                                    valueJSON = "null";
                                }
                            } else if (value instanceof Boolean) {
                                valueJSON = ((Boolean)value).booleanValue() ? "true" : "false";
                            } else if (value instanceof Character) {
                                valueJSON = "\""+value+"\"";
                            } else {
                                throw new RuntimeException("Result value "+value+" is not a recognized type");
                            }
                            //System.out.println("About call cordova.callbackSuccess "+valueJSON);
                            //System.out.println("About to call cordova.callbackFromNative "+valueJSON+", "+success+","+status+","+keep);
                            webview.execute("cordova.callbackFromNative("+JSONObject.quote(callbackId)+", "+success+", "+status+", ["+valueJSON+"], "+keep+")");
                                    //", {message:"+valueJSON+", status: "+Result.OK.ordinal()+", keepCallback:false})");

                        }

                        @Override
                        public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                            throw new RuntimeException("onError callback should not be used.  Only use onSuccess, and provide the correct isSuccess and status messages");
                            //System.out.println("EmbeddedCordovaApplication#"+service+"#"+action+" onError:"+errorMessage);
                            //webview.execute("cordova.callbackError("+JSONObject.quote(callbackId)+", {message:"+errorMessage+", status: "+Result.ERROR.ordinal()+", keepCallback:false})");
                        }

                    };


                    String actionArgsJSON = (String)arguments[3];

                    // First try the version of the plugin that accepts a JSON string as an argument.
                    System.out.println("EmbeddedCordovaApplication#"+service+"#"+action+" plugin.execute()");
                    if (plugin.execute(callbackId, action, actionArgsJSON, callback)) {
                        return;
                    }

                    // If that version of execute doesn't find a match, then we will convert the JSON string into
                    // a List and try the other version.
                    List actionArgs = null;
                    if (actionArgsJSON == null) {
                        actionArgs = new ArrayList();
                    } else {
                        InputStreamReader r = null;
                        try {
                            r = new InputStreamReader(new ByteArrayInputStream(actionArgsJSON.getBytes()));
                            actionArgs = (List)jsonParser.parseJSON(r).get("root");
                        } catch (Exception ex) {
                            Log.e(ex);
                            throw new RuntimeException(ex.getMessage());
                        } finally {
                            if (r != null) {
                                try {
                                    r.close();
                                } catch (Exception ex){}
                            }
                        }


                    }


                    for (int i=0; i<actionArgs.size(); i++) {
                        Object arg = actionArgs.get(i);
                        if (arg instanceof Map && "ArrayBuffer".equals(((Map)arg).get("CDVType"))) {
                            byte[] bytes = Base64.decode(((String)((Map)arg).get("data")).getBytes());
                            actionArgs.set(i, bytes);
                        }
                    }


                    if (!plugin.execute(callbackId, action, actionArgs, callback)) {
                        webview.execute("cordova.callbackError("+JSONObject.quote(callbackId)+", {message:'Action not found', status:"+Result.ERROR.ordinal()+", keepCallback:false})");
                    }

                } catch (IOException ex) {
                    Log.e(ex);
                    return;
                }
            });
            

            onLoad();
            webview.execute("console.log('about to call native ready');");
            webview.execute("if (!window.device) {window.device = {};}"
                    + "window.device.platform='"+getPlatformString()+"';"
                    + "window._nativeReady = true; cordova.require('cordova/channel').onNativeReady.fire()");
            webview.execute("document.addEventListener('deviceready', function(){callback.onSuccess(true);})", jref->{
                System.out.println("In deviceready callback.  Calling onReady()");
                onReady();
            });
            

            
        };
        webview.addWebEventListener(BrowserComponent.onLoad, evt->{
            installer.run();
        });
        
    }
    
    private String getPlatformString() {
        String cn1Name = CN.getPlatformName();
        if ("ios".equals(cn1Name)) {
            return "iOS";
        }
        if ("and".equals(cn1Name)) {
            return "Android";
        }
        return "browser";
    }
    
    
    
    /**
     * Registers a plugin for this app.  Note:  This is only required if the 
     * plugin has a native component, as this registers the native callbacks
     * for the plugin.
     * 
     * @param service The name of the plugin.  Should match the name used 
     * by the plugin in the javascript layer for communicating with it's native 
     * counterpart.
     * @param plugin The plugin to be registered.
     */
    public void addPlugin(String service, CordovaPlugin plugin) {
        pluginMap.put(service, plugin);
    }
    
    /**
     * Fires the "pause" event to the cordova js layer.
     */
    public void pause() {
        webview.execute("cordova.require('cordova/channel').onPause.fire()");
    }
    
    /**
     * Fires the "resume" event to the cordova js layer.
     */
    public void resume() {
        webview.execute("cordova.require('cordova/channel').onResume.fire()");
    }
    
    /**
     * Registers a global plugin to be included in all CordovaApplication objects.
     * This won't affect existing objects.  Only ones instantiated after this call.
     * @param service The name of the service that the plugin is registered to serve.
     * @param plugin The plugin.
     */
    public static void addGlobalPlugin(String service, CordovaPlugin plugin) {
        globalPluginMap.put(service, plugin);
    }
    
   
    /**
     * Event fired after the application is loaded.  Meant to be overridden by 
     * subclasses.
     */
    protected void onLoad() {
        
    }
    
    protected void onReady() {
        
    }
    
    /**
     * Returns reference to the WebView.
     * @return The browser component where the cordova app is displayed.
     */
    public BrowserComponent getWebview() {
        return webview;
    }
    
    public void dispose() {
        for (CordovaPlugin plugin : pluginMap.values()) {
            plugin.dispose();
        }
        pluginMap.clear();
    }
    
}
