package com.egrasoft.graphalgo.listeners;

import com.egrasoft.graphalgo.controls.ControlAction;
import com.egrasoft.graphalgo.controls.ControlActionHotkey;
import com.egrasoft.graphalgo.components.GraphComponentUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Absract class handling mouse click events.
 */
public abstract class ClickAdapter extends MouseAdapter {
    /**
     * Method producing a submask to be checked by the control actions.
     * @see ControlAction
     * @see ControlActionHotkey
     * @param clicked element being clicked on
     * @return event submask
     */
    protected abstract int getSubmask(GraphComponentUI clicked);

    /**
     * Event of mouse button being clicked.
     * @param e mouse event object
     */
    @Override
    public abstract void mouseClicked(MouseEvent e);
}
