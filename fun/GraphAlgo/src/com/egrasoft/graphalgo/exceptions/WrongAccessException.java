package com.egrasoft.graphalgo.exceptions;

/**
 * Runtime exception thrown while trying to modify (remove) an element of SafeCollectionWrapper's wrapped collection.
 */
public class WrongAccessException extends RuntimeException {
    /**
     * Class constructor.
     */
    public WrongAccessException(){}

    /**
     * Class constructor.
     * @param message message to be sent into the superclass constructor.
     */
    public WrongAccessException(String message){
        super(message);
    }
}
