package com.egrasoft.graphalgo.exceptions;

/**
 * Runtime exception thrown at the very beginning if any concrete component class
 * doesn't have a FactoryInfo annotation applied.
 */
public class AnnotationNotPresentException extends Exception {
    /**
     * Class constructor.
     */
    public AnnotationNotPresentException(){}

    /**
     * Class constructor.
     * @param message message to be sent into the superclass constructor.
     */
    public AnnotationNotPresentException(String message){
        super(message);
    }
}
