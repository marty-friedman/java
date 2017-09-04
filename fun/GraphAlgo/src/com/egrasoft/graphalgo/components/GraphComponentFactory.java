package com.egrasoft.graphalgo.components;

import com.egrasoft.graphalgo.tools.InfoPack;

public abstract class GraphComponentFactory {
    /**
     * Method instantiating a new graph component object.
     * @param data data pack with the properties of the new graph component
     * @return newly created component object
     */
    public abstract GraphComponent instance(InfoPack data);

    /**
     * TODO
     */
    public abstract InfoPack getInfo();

    /**
     * Method returning this component's class object.
     */
    public abstract Class<?> getComponentClass();
}
