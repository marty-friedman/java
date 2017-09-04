package com.egrasoft.graphalgo.components;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.MainFrame;
import com.egrasoft.graphalgo.exceptions.AnnotationNotPresentException;

import java.awt.*;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Interface used in polymorphic purposes. Implemented by NodeFactorySelector and EdgeFactorySelector
 * enumerations.
 */
public interface GraphComponentFactorySelector {

    /**
     * Getter-method for img field.
     * @return toolbox icon image associated with this kind of components
     */
    Image getImg();

    /**
     * Getter-method for hint field.
     * @return toolbox hint string associated with this kind of components
     */
    String getHint();

    /**
     * Getter-method for description field.
     * @return toolbox description string associated with this kind of components
     */
    String getDescription();

    /**
     * Method checking and setting the attributes of the current component type. Invoked by the enum constructor.
     * @param fact factory object to checked
     * @throws AnnotationNotPresentException if component's class doesn't have a FactoryInfo annotation
     * @throws IllegalArgumentException if image file is not present
     * @throws MissingResourceException if string resource bundle doesn't contain hintResourceBundleKey() or
     * descriptionResourceBundleKey() keys defined by FactoryInfo annotation applied to the component's class
     */
    default void validateAttributes(GraphComponentFactory fact) throws AnnotationNotPresentException {
        Class<?> fClass = fact.getComponentClass();
        if (!fClass.isAnnotationPresent(FactoryInfo.class))
            throw new AnnotationNotPresentException("Cannot find a FactoryInfo annotation on "+fClass.getName()+" class");
        FactoryInfo anno = fClass.getAnnotation(FactoryInfo.class);
        ResourceBundle res = MainFrame.getResourceBundle();
        if (!res.containsKey(anno.hintResourceBundleKey()))
            throw new MissingResourceException("Hint string resource key of "+fClass.getName()+" class was not found", "ResourceBundle", anno.hintResourceBundleKey());
        if (!res.containsKey(anno.descriptionResourceBundleKey()))
            throw new MissingResourceException("Description string resource key of "+fClass.getName()+" class was not found", "ResourceBundle", anno.descriptionResourceBundleKey());
    }

}
