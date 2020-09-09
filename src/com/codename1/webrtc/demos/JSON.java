/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.io.JSONParser;
import com.codename1.processing.Result;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shannah
 */
class JSON {
    static String stringify(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof Map) {
            return Result.fromContent((Map)o).toString();
        } else if (o instanceof List) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            List l = (List)o;
            int len = l.size();
            for (int i=0; i<len; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(stringify(l.get(i)));
            }
            sb.append("]");
            return sb.toString();
            
        } else if (o instanceof String) {
            return quote((String)o);
        } else if (o instanceof Number || o instanceof Boolean) {
            return String.valueOf(o);
        } else {
            return "[Object]";
        }
    }
    
    static Map parseMap(String json) throws IOException {
        JSONParser p = new JSONParser();
        p.setUseBooleanInstance(true);
        return p.parseJSON(new StringReader(json));
    }
    
    static List parseList(String json) throws IOException {
        Map m = parseMap(json);
        return (List)m.get("root");
    }
    
    private static String quote(String string) {
         if (string == null || string.length() == 0) {
             return "\"\"";
         }

         char         b;
         char         c = 0;
         int          i;
         int          len = string.length();
         StringBuilder sb = new StringBuilder(len + 4);
         String       t;

         sb.append('"');
         for (i = 0; i < len; i += 1) {
             b = c;
             c = string.charAt(i);
             switch (c) {
             case '\\':
             case '"':
                 sb.append('\\');
                 sb.append(c);
                 break;
             case '/':
                 if (b == '<') {
                     sb.append('\\');
                 }
                 sb.append(c);
                 break;
             case '\b':
                 sb.append("\\b");
                 break;
             case '\t':
                 sb.append("\\t");
                 break;
             case '\n':
                 sb.append("\\n");
                 break;
             case '\f':
                 sb.append("\\f");
                 break;
             case '\r':
                 sb.append("\\r");
                 break;
             default:
                 if (c < ' ') {
                     t = "000" + Integer.toHexString(c);
                     sb.append("\\u" + t.substring(t.length() - 4));
                 } else {
                     sb.append(c);
                 }
             }
         }
         sb.append('"');
         return sb.toString();
     }
    
}
