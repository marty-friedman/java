package com.egrasoft.graphalgo.components.edges.NondirectionalEdge;

import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.edges.EdgeFactory;

/**
 * Factory for nondirectional edge objects, implementation of the edge abstract factory.
 */
public class NondirectionalEdgeFactory extends EdgeFactory {
    /**
     * @see EdgeFactory#instance(InfoPack)
     */
    @Override
    public Edge instance(InfoPack data) {
        return new NondirectionalEdge(data);
    }

    /**
     * TODO
     */
    @Override
    public InfoPack getInfo() { return new InfoPack(); }

    /**
     * @see EdgeFactory#getComponentClass()
     */
    @Override
    public Class<?> getComponentClass() { return NondirectionalEdge.class; }
}
