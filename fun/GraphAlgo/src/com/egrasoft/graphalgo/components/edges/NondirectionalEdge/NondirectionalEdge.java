package com.egrasoft.graphalgo.components.edges.NondirectionalEdge;

import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.tools.InfoPack;

/**
 * Implementation of edge class, representing an nondirectional, unweighted edge.
 */
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
