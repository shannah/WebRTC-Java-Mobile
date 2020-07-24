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
public class SharedScreenTrackConstraints extends MediaTrackConstraints {

    public SharedScreenTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }

    /**
     * @return the cursor
     */
    public ConstrainCursor getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor(ConstrainCursor cursor) {
        this.cursor = cursor;
    }

    /**
     * @return the displaySurface
     */
    public ConstrainDisplaySurface getDisplaySurface() {
        return displaySurface;
    }

    /**
     * @param displaySurface the displaySurface to set
     */
    public void setDisplaySurface(ConstrainDisplaySurface displaySurface) {
        this.displaySurface = displaySurface;
    }

    /**
     * @return the logicalSurface
     */
    public ConstrainBoolean getLogicalSurface() {
        return logicalSurface;
    }

    /**
     * @param logicalSurface the logicalSurface to set
     */
    public void setLogicalSurface(ConstrainBoolean logicalSurface) {
        this.logicalSurface = logicalSurface;
    }
    private ConstrainCursor cursor;
    private ConstrainDisplaySurface displaySurface;
    private ConstrainBoolean logicalSurface;
    
    
    
    public static class ConstrainCursor extends ConstrainObject<Cursor>{}
    
    
    
    public static class ConstrainDisplaySurface extends ConstrainObject<DisplaySurface>{
    }
}
