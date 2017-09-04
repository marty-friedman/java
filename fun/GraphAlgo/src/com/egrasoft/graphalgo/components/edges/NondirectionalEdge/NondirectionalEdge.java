package com.egrasoft.graphalgo.components.edges.NondirectionalEdge;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Implementation of edge class, representing an nondirectional, unweighted edge.
 */
@FactoryInfo(iconPath = "/com/egrasoft/graphalgo/graphics/NondirectionalEdgeIcon.png",
        hintResourceBundleKey = "toolboxHintNondirectionalEdge",
        descriptionResourceBundleKey = "toolboxDescriptionNondirectionalEdge")
class NondirectionalEdge extends Edge {
    /**
     * Class constructor.
     * @param data data pack object
     */
    NondirectionalEdge(InfoPack data) {
        super(data);
        ui = new NondirectionalEdgeUI(data, this);
    }
}
