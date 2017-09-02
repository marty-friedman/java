package com.egrasoft.graphalgo.components.nodes;

import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Abstract factory class for graph node components. All subclasses also being instantiated in node
 * selector enum must have a FactoryInfo annotation, otherwise a runtime exception will be thrown at
 * the very start of the application.
 * @see NodeFactorySelector
 */
public abstract class NodeFactory {

    /**
     * Method instantiating a new node object.
     * @param data data pack with properties of new node
     * @return newly created node object
     */
    public abstract Node instanceNode(InfoPack data);

    /**
     * TODO
     */
    public abstract InfoPack getNodeInfo();

}
