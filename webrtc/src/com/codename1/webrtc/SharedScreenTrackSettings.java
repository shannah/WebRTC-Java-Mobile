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
public class SharedScreenTrackSettings extends MediaTrackSettings {

    /**
     * @return the cursor
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * @return the displaySurface
     */
    public DisplaySurface getDisplaySurface() {
        return displaySurface;
    }

    /**
     * @param displaySurface the displaySurface to set
     */
    public void setDisplaySurface(DisplaySurface displaySurface) {
        this.displaySurface = displaySurface;
    }

    /**
     * @return the logicalSurface
     */
    public boolean isLogicalSurface() {
        return logicalSurface;
    }

    /**
     * @param logicalSurface the logicalSurface to set
     */
    public void setLogicalSurface(boolean logicalSurface) {
        this.logicalSurface = logicalSurface;
    }
    private Cursor cursor;
    private DisplaySurface displaySurface;
    private boolean logicalSurface;
}
