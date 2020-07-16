/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * @author shannah
 */
 public enum ResizeMode implements JSObject {
        None("none"),
        CropAndScale("crop-and-scale");
        private String string;
        ResizeMode(String str){
            string = str;
        }

        @Override
        public Object toJSONStruct() {
            return string;
        }
        
        public boolean matches(String str) {
            return string.equals(str);
        }
    }
