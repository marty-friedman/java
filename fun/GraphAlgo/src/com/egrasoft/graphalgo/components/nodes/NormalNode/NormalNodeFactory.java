package com.egrasoft.graphalgo.components.nodes.NormalNode;

import com.egrasoft.graphalgo.components.nodes.Node;
import com.egrasoft.graphalgo.components.nodes.NodeFactory;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Factory for normal node objects, implementation of node abstract factory.
 */
public class NormalNodeFactory extends NodeFactory {
    /**
     * @see NodeFactory#instance(InfoPack)
     */
    @Override
    public Node instance(InfoPack data) {
        return new NormalNode(data);
    }

    /**
     * TODO
     */
    @Override
    public InfoPack getInfo() { return new InfoPack(); }

    /**
     * @see NodeFactory#getComponentClass()
     */
    @Override
    public Class<?> getComponentClass() { return NormalNode.class; }
}
