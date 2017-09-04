package com.egrasoft.graphalgo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which must be applied to every node/edge class, which factory selector is
 * defined in the node/edge selector enumeration respectively.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FactoryInfo {
    /**
     * @return string path to the image icon item for the toolbox button
     */
    String iconPath();

    /**
     * @return resource bundle key for the string hint value
     */
    String hintResourceBundleKey();

    /**
     * @return resource bundle key for the string description value
     */
    String descriptionResourceBundleKey();
}
