package com.egrasoft.graphalgo.listeners;

import com.egrasoft.graphalgo.controls.ControlAction;
import com.egrasoft.graphalgo.controls.ControlActionHotkey;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Absract class handling keyboard events.
 */
public abstract class TypeAdapter extends KeyAdapter {
    /**
     * Produces control submask for checking a control event to be invoked.
     * @see ControlAction
     * @see ControlActionHotkey
     * @return event submask
     */
    protected abstract int getSubmask();

    /**
     * Event of a key being pressed. Overrides the {@link KeyAdapter#keyPressed(KeyEvent)}
     * method.
     * @param e mouse event object
     */
    @Override
    public abstract void keyPressed(KeyEvent e);
}
