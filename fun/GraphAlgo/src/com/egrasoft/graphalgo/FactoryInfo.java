package com.egrasoft.graphalgo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FactoryInfo {
    String iconPath();
    String hintResourceBundleKey();
    String descriptionResourceBundleKey();
}
