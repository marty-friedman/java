package com.egrasoft.graphalgo.controls;

import java.util.function.Function;

import static com.egrasoft.graphalgo.controls.ControlActionHotkey.Constants.*;

/**
 * Class representing the control action's hotkey objects. Hotkey building, detecting and storing
 * is based on the integer mask, which has the following structure (from the lowest byte to the
 * highest): <br>
 * - 16 bits for storing event code. If it's a keyboard hotkey, these 16 bits contain the key code,
 * else they're specified by applying {@link Constants#LEFT_MOUSE_BUTTON},
 * {@link Constants#RIGHT_MOUSE_BUTTON} or {@link Constants#MIDDLE_MOUSE_BUTTON} masks; <br>
 * - 2 bits for storing the hotkey type. They can be specified by by applying
 * {@link Constants#MOUSE_CLICK_EVENT}, {@link Constants#MOUSE_DRAG_EVENT} or {@link Constants#KEY_EVENT}
 * masks; <br>
 * - 1 bit set if shift-modifier should be pressed; <br>
 * - 1 bit set if ctrl-modifier should be pressed; <br>
 * - 2 bits responsible for the object being clicked and specified by applying
 * {@link Constants#CLICKED_ON_NOTHING}, {@link Constants#CLICKED_ON_NODE}, {@link Constants#CLICKED_ON_EDGE}
 * masks or 0 if it doesn't matter; <br>
 * - 2 bits representing selection state, specified by {@link Constants#SELECTION_SET},
 * {@link Constants#SELECTION_UNSET} fields or 0 if it doesn't matter. <br>
 * Bits 17-18, 21-22 and 23-24 are also called "submask" and are permanent for a particular control
 * action (see {@link ControlAction}).
 */
public class ControlActionHotkey {


    /*==========================Fields==========================*/


    /**
     * This hotkey's mask object.
     */
    private int mask;

    /**
     * Id of this hotkey.
     */
    private int id;

    /**
     * Predicate analyzing given integer mask in comparison to this object's mask value.
     */
    private Function<Integer, Boolean> predicate;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param mask new hotkey's mask value
     * @param id new hotkey's identifier
     */
    public ControlActionHotkey(int mask, int id){
        validate(mask);
        this.mask = mask;
        this.id = id;
        predicate = getPredicate();
    }


    /*======================Private Methods======================*/


    /**
     * Throws an exception if mask given to class constructor is not valid mask value.
     * @param mask mask to be analyzed
     * @throws IllegalArgumentException if mask is not valid
     */
    private void validate(int mask){
        switch(mask & TestConstants.TEST_EVENT){
            case MOUSE_CLICK_EVENT: case MOUSE_DRAG_EVENT: case KEY_EVENT:
                break;
            default:
                throw new IllegalArgumentException("Incorrect \"event type\" mask");
        }
        if ((mask & TestConstants.TEST_EVENT) == MOUSE_CLICK_EVENT || (mask & TestConstants.TEST_EVENT) == MOUSE_DRAG_EVENT) {
            switch (mask & TestConstants.TEST_MOUSE_BUTTON) {
                case MIDDLE_MOUSE_BUTTON: case LEFT_MOUSE_BUTTON: case RIGHT_MOUSE_BUTTON:
                    break;
                default:
                    throw new IllegalArgumentException("Incorrect \"mouse button\" mask");
            }
        }
        switch (mask & TestConstants.TEST_SELECTION){
            case SELECTION_SET: case SELECTION_UNSET: case 0:
                break;
            default:
                throw new IllegalArgumentException("Incorrect \"selected component\" mask");
        }

    }

    /**
     * Creates a predicate object comparing this hotkey's mask to the given one.
     * @return predicate, obtaining an integer mask value and returning boolean value (true if the given hotkey mask fits this one, false otherwise)
     */
    private Function<Integer, Boolean> getPredicate(){
        return (Integer ex)->{
            int req = mask;
            int equalMask = TestConstants.TEST_EVENT | TestConstants.TEST_CODE | TestConstants.TEST_SHIFT | TestConstants.TEST_CTRL;
            if ((req & equalMask) != (ex & equalMask))
                return false;
            int reqClicked = req & TestConstants.TEST_CLICKED, exClicked = ex & TestConstants.TEST_CLICKED,
                    reqSelection = req & TestConstants.TEST_SELECTION, exSelection = ex & TestConstants.TEST_SELECTION;
            if (reqClicked!=0 && reqClicked!=exClicked)
                return false;
            if (reqSelection!=0 && reqSelection!=exSelection)
                return false;
            return true;
        };
    }


    /*======================Public Methods======================*/


    /**
     * Getter-method for mask value.
     * @return this hotkey's mask value
     */
    public int getMask(){ return mask; }

    /**
     * Getter-method for identifier.
     * @return this hotkey's id
     */
    public int getId(){ return id; }

    /**
     * Method checking the event mask to represent the same hotkey as this one.
     * @param event mask value to be checked
     * @return boolean value (true if it's the same, false otherwise)
     */
    public boolean check(int event){ return predicate.apply(event); }


    /*======================Nested Classes======================*/


    /**
     * Nested class storing private check-mask constants.
     */
    private static class TestConstants{
        static final int TEST_EVENT = (0b11 << 16);
        static final int TEST_MOUSE_BUTTON = 0b11;
        static final int TEST_SHIFT = (0b1 << 18);
        static final int TEST_CTRL = (0b1 << 19);
        static final int TEST_CLICKED = (0b11 << 20);
        static final int TEST_SELECTION = (0b11 << 22);
        static final int TEST_CODE = 0xFFFF;
    }

    /**
     * Nested class storing public constants representing different mask values.
     */
    public static class Constants {
        public static final int MOUSE_CLICK_EVENT = (1 << 16);
        public static final int MOUSE_DRAG_EVENT = (2 << 16);
        public static final int KEY_EVENT = (3 << 16);

        public static final int LEFT_MOUSE_BUTTON = 1;
        public static final int RIGHT_MOUSE_BUTTON = 2;
        public static final int MIDDLE_MOUSE_BUTTON = 3;

        public static final int SHIFT_ENABLED = (1 << 18);
        public static final int CTRL_ENABLED = (1 << 19);

        public static final int CLICKED_ON_NOTHING = (1 << 20);
        public static final int CLICKED_ON_NODE = (2 << 20);
        public static final int CLICKED_ON_EDGE = (3 << 20);

        public static final int SELECTION_UNSET = (1 << 22);
        public static final int SELECTION_SET = (2 << 22);

        public static final int SUBMASK_CLEAR = 0x000C_FFFF;
    }


}
