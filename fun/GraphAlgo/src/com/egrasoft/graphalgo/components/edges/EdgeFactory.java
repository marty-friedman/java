package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.components.GraphComponentFactory;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Abstract factory class for graph edge components. All the component classes being instantiated in
 * the subclasses of this factory also defined in the edge selector enumeration must have a FactoryInfo
 * annotation applied, otherwise a runtime exception will be thrown at the very beginning of application.
 * @see EdgeFactorySelector
 */
public abstract class EdgeFactory extends GraphComponentFactory{
    /**
     * @see GraphComponentFactory#instance(InfoPack)
     */
    @Override
    public abstract Edge instance(InfoPack data);

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
