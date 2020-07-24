/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * Interface implemented by objects that can be "serialized" as JSON structure.
 * @author shannah
 */
public interface JSObject {
    
    /**
     * Converts the object to a JSON structure.  A JSON structure may be a list, map, string, boolean, or number.  Lists and maps
     * may only contain values that are themselves JSON structures.
     * @return 
     */
    public Object toJSONStruct();
}
