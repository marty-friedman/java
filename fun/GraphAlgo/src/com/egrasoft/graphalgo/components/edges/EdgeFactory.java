package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Abstract factory for edge objects. All subclasses also being instantiated in edge selector
 * enum must apply a FactoryInfo annotation, otherwise a runtime exception will be thrown at
 * the very start of the application.
 * @see EdgeFactorySelector
 */
public abstract class EdgeFactory {
    /**
     * Method instantiating a new edge object.
     * @param data data pack with the properties of the new edge
     * @return newly created edge object
     */
    public abstract Edge instanceEdge(InfoPack data);

    /**
     * TODO
     */
    public abstract InfoPack getEdgeInfo();
}
