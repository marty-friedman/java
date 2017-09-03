package com.egrasoft.graphalgo.components.edges.NondirectionalEdge;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.edges.EdgeFactory;

/**
 * Factory for nondirectional edge objects, implementation of the edge abstract factory.
 */
@FactoryInfo(iconPath = "/com/egrasoft/graphalgo/graphics/NondirectionalEdgeIcon.png",
        hintResourceBundleKey = "toolboxHintNondirectionalEdge",
        descriptionResourceBundleKey = "toolboxDescriptionNondirectionalEdge")
public class NondirectionalEdgeFactory extends EdgeFactory {
    /**
     * @see EdgeFactory#instanceEdge(InfoPack)
     */
    @Override
    public Edge instanceEdge(InfoPack data) {
        return new NondirectionalEdge(data);
    }

    /**
     * TODO
     */
    @Override
    public InfoPack getEdgeInfo() { return new InfoPack(); }
}
