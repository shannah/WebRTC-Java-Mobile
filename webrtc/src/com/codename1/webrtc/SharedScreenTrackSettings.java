/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 * Tracks containing video shared from a user's screen (regardless of whether
 * the screen data comes from the entire screen or a portion of a screen, like a
 * window or tab) are generally treated like video tracks, with the exception
 * that they also support the following added settings:
 *
 * @author shannah
 */
public class SharedScreenTrackSettings extends MediaTrackSettings {

    /**
     * A DOMString which indicates whether or not the mouse cursor is being
     * included in the generated stream and under what conditions.
     *
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
     * A DOMString which specifies the type of source the track contains
     *
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
     * A Boolean value which, if true, indicates that the video contained in the
     * stream's video track contains a background rendering context, rather than
     * a user-visible one. This is false if the video being captured is coming
     * from a foreground (user-visible) source.
     *
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
