package com.egrasoft.graphalgo.components.nodes.NormalNode;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.components.nodes.Node;
import com.egrasoft.graphalgo.components.nodes.NodeFactory;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Factory for normal node objects, implementation of node abstract factory.
 */
@FactoryInfo(iconPath = "/com/egrasoft/graphalgo/graphics/NormalNodeIcon.png",
        hintResourceBundleKey = "toolboxHintNormalNode",
        descriptionResourceBundleKey = "toolboxDescriptionNormalNode")
public class NormalNodeFactory extends NodeFactory {
    /**
     * @see NodeFactory#instanceNode(InfoPack)
     */
    @Override
    public Node instanceNode(InfoPack data) {
        return new NormalNode(data);
    }

    /**
     * TODO
     */
    @Override
    public InfoPack getNodeInfo() { return new InfoPack(); }
}
