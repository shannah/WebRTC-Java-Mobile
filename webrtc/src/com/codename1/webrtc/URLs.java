/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A list of URLs.
 * @author shannah
 */
public class URLs extends RTCList<String> implements JSONStruct {

    @Override
    public Object toJSONStruct() {
        ArrayList out = new ArrayList();
        for (String str : this) {
            out.add(str);
        }
        return out;
    }
    
}
