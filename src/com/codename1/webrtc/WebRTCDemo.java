package com.codename1.webrtc;


import com.codename1.components.SpanLabel;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.webrtc.demos.BasicDemo;
import com.codename1.webrtc.demos.ConstraintsDemo;
import com.codename1.webrtc.demos.PeerConnectionDemo;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 * 
 * NOTE: As of July 27th, these demos should run on the following platforms:
 * 
 * 1. Java SE (Simulator).  Requires CEF installed.  https://www.codenameone.com/blog/big-changes-jcef.html
 * 2. Android
 * 3. Javascript Port. (Chrome, Firefox, Safari desktop.  Android Chrome.  Currently iOS not working).
 * 
 */
public class WebRTCDemo {

    private Form current;
    private Resources theme;

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        Form hi = new Form("WebRTC Demos", BoxLayout.y());
        
        String intro = "This is a collection of small samples demonstrating various parts of the WebRTC APIs in Codename One. The code for all samples are available in the GitHub repository.\n" +
"\n"
                + "Most of these samples have been adapted from samples on the official WebRTC website.\n" +
"Warning: It is highly recommended to use headphones when testing these samples, as it will otherwise risk loud audio feedback on your system.";
        
        Button github = new Button("Github");
        Button webRTC = new Button("WebRTC Website");
        Button cn1 = new Button("CodeanmeOne Website");
        
        
        
        $(github, cn1, webRTC).setMaterialIcon(FontImage.MATERIAL_LINK, -1);
        cn1.addActionListener(evt->CN.execute("https://www.codenameone.com"));
        github.addActionListener(evt->CN.execute("https://github.com/shannah/CN1WebRTC"));
        webRTC.addActionListener(evt->CN.execute("https://webrtc.github.io/samples/"));
        
        Button basicDemo = new Button("Basic Demo");
        basicDemo.addActionListener(evt->{
            prepareDemo(new BasicDemo()).show();
        });
        Button constraintsDemo = new Button("Constraints Demo");
        constraintsDemo.addActionListener(evt->{
            prepareDemo(new ConstraintsDemo()).show();
        });
        hi.add(new SpanLabel(intro));
        hi.add(GridLayout.encloseIn(3, github, webRTC, cn1));
        
        Button peerConnectionDemo = new Button("Peer Connection Demo");
        peerConnectionDemo.addActionListener(evt->{
            prepareDemo(new PeerConnectionDemo()).show();
        });
        hi.addAll(basicDemo, constraintsDemo, peerConnectionDemo);
        
        
        hi.show();
    }
    
    
    private Form prepareDemo(Form form) {
        Form curr = CN.getCurrentForm();
        form.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK_IOS, evt->{
            if (form instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) form).close();
                } catch (Throwable t){
                    Log.e(t);
                }
            }
            curr.showBack();
        });
        return form;
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
