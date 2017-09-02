package com.egrasoft.graphalgo.listeners;

import com.egrasoft.graphalgo.controls.ControlAction;
import com.egrasoft.graphalgo.controls.ControlActionHotkey;
import com.egrasoft.graphalgo.components.GraphComponentUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Absract class handling drag events.
 */
public abstract class DragAdapter extends MouseAdapter {
    /**
     * Action performing at the moment.
     */
    public DragAction actionPerforming = DragAction.NONE;

    /**
     * Point of the mouse start-drag event.
     */
    public Point startDrag;

    /**
     * Point of the mouse end-drag event.
     */
    public Point endDrag;

    /**
     * GUI representation of the object located at the startDrag point (or null).
     */
    public GraphComponentUI dragged;

    /**
     * Method producing a submask to be checked by the control actions.
     * @see ControlAction
     * @see ControlActionHotkey
     * @param clicked element being clicked on
     * @return event submask
     */
    protected abstract int getSubmask(GraphComponentUI clicked);

    /**
     * Event of mouse movement, while any mouse button is being pressed.
     * @param e mouse event object
     */
    @Override
    public abstract void mouseDragged(MouseEvent e);

    /**
     * Event of mouse button being pressed.
     * @param e mouse event object
     */
    @Override
    public abstract void mousePressed(MouseEvent e);

    /**
     * Event of mouse button being released.
     * @param e mouse event object
     */
    @Override
    public abstract void mouseReleased(MouseEvent e);

    /**
     * Enumeration describing the current drag action.
     */
    public enum DragAction{NONE, CONNECTING, MOVING, CURVING}
}
