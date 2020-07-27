/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc;

/**
 *
 * These constraints apply to MediaTrackConstraints objects specified as part of
 * the DisplayMediaStreamConstraints object's video property when using
 * getDisplayMedia() to obtain a stream for screen sharing.
 *
 * @author shannah
 */
public class SharedScreenTrackConstraints extends MediaTrackConstraints {

    public SharedScreenTrackConstraints(MediaStreamConstraints parent) {
        super(parent);
    }

    /**
     * A ConstrainDOMString which specifies whether or not to include the mouse
     * cursor in the generated track, and if so, whether or not to hide it while
     * not moving. The value may be a single one of the following strings, or an
     * array of them to allow the browser flexibility in deciding what to do
     * about the cursor.
     *
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
     * A ConstrainDOMString which specifies the types of display surface that
     * may be selected by the user. This may be a single one of the following
     * strings, or a list of them to allow multiple source surfaces:
     *
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
     * A ConstrainBoolean value which may contain a single Boolean value or a
     * set of them, indicating whether or not to allow the user to choose source
     * surfaces which do not directly correspond to display areas. These may
     * include backing buffers for windows to allow capture of window contents
     * that are hidden by other windows in front of them, or buffers containing
     * larger documents that need to be scrolled through to see the entire
     * contents in their windows.
     *
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

    public static class ConstrainCursor extends ConstrainObject<Cursor> {
    }

    public static class ConstrainDisplaySurface extends ConstrainObject<DisplaySurface> {
    }
}
