package com.egrasoft.graphalgo.components.nodes;

import com.egrasoft.graphalgo.components.GraphComponentFactory;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Abstract factory class for graph node components. All the component classes being instantiated in
 * the subclasses of this factory also defined in the node selector enumeration must have a FactoryInfo
 * annotation applied, otherwise a runtime exception will be thrown at the very beginning of application.
 * @see NodeFactorySelector
 */
public abstract class NodeFactory extends GraphComponentFactory {
    /**
     * @see GraphComponentFactory#instance(InfoPack)
     */
    @Override
    public abstract Node instance(InfoPack data);

    /**
     * @see GraphComponentFactory#getInfo()
     */
    @Override
    public abstract InfoPack getInfo();

    /**
     * @see GraphComponentFactory#getComponentClass()
     */
    public abstract Class<?> getComponentClass();
}
