package com.egrasoft.graphalgo.controls;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import static com.egrasoft.graphalgo.controls.ControlActionHotkey.Constants.*;

/**
 * Enumeration describing application control actions and managing their hotkeys.
 */
public enum ControlAction {
    MAKE_NODE(MOUSE_CLICK_EVENT | CLICKED_ON_NOTHING,
        LEFT_MOUSE_BUTTON | CTRL_ENABLED
    ),
    CONNECT_NODES(MOUSE_DRAG_EVENT | CLICKED_ON_NODE,
        LEFT_MOUSE_BUTTON | SHIFT_ENABLED
    ),
    MOVE_NODE(MOUSE_DRAG_EVENT | CLICKED_ON_NODE,
        LEFT_MOUSE_BUTTON
    ),
    CURVE_EDGE(MOUSE_DRAG_EVENT | CLICKED_ON_EDGE,
        LEFT_MOUSE_BUTTON
    ),
    DELETE_COMPONENT(KEY_EVENT | SELECTION_SET,
        KeyEvent.VK_DELETE
    ),
    REMOVE_EDGE_CURVING(MOUSE_CLICK_EVENT | CLICKED_ON_EDGE,
        MIDDLE_MOUSE_BUTTON
    ),
    SELECT_COMPONENT(MOUSE_CLICK_EVENT,
        LEFT_MOUSE_BUTTON
    );


    /*==========================Fields==========================*/


    /**
     * Submask field assigned to every control action constant object as they are being initialized.
     * This field cannot be changed. Contains information about the event type (click, drag or keyboard),
     * the element being clicked (for mouse events) and the selection state.
     */
    private int submask;

    /**
     * Hotkeys associated with this action.
     */
    private ArrayList<ControlActionHotkey> hotkeys;


    /*=======================Constructors=======================*/


    /**
     * Enum constructor.
     * @param submask submask value to be applied to this element
     * @param mask mask, which will be used with submask value to add a first hotkey for this element
     */
    ControlAction(int submask, int mask){
        this.submask = submask;
        hotkeys = new ArrayList<>();
        addHotkey(mask);
    }


    /*======================Public Methods======================*/


    /**
     * Method checking if mouse/keyboard event just having been invoked fits this control event.
     * @param submask submask of the event happened
     * @param e event object
     * @return true if the event fits any of this action's hotkeys, false otherwise
     */
    public boolean check(int submask, InputEvent e){
        int mask = submask;

        if (e instanceof MouseEvent){
            MouseEvent me = (MouseEvent) e;
            if (SwingUtilities.isLeftMouseButton(me))
                mask |= LEFT_MOUSE_BUTTON;
            else if (SwingUtilities.isRightMouseButton(me))
                mask |= RIGHT_MOUSE_BUTTON;
            else if (SwingUtilities.isMiddleMouseButton(me))
                mask |= MIDDLE_MOUSE_BUTTON;
        } else {
            mask |= ((KeyEvent) e).getKeyCode();
        }

        if (e.isShiftDown())
            mask |= SHIFT_ENABLED;
        if (e.isControlDown())
            mask |= CTRL_ENABLED;

        for (ControlActionHotkey cf : hotkeys)
            if (cf.check(mask))
                return true;
        return false;
    }

    /**
     * Method adding a new hotkey for this action. Cleares its argument's submask, applying field
     * submask instead.
     * @param mask new hotkey mask
     */
    public void addHotkey(int mask){
        mask &= SUBMASK_CLEAR;
        hotkeys.add(new ControlActionHotkey(submask | mask, hotkeys.size()));
    }

    /**
     * Method removing this element's hotkey specified by its id.
     * @param id identifier of the hotkey to be removed
     */
    public void removeHotkey(int id){
        //TODO
    }

}
