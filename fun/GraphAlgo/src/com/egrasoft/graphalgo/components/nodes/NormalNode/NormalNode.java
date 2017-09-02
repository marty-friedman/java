package com.egrasoft.graphalgo.components.nodes.NormalNode;

import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.nodes.Node;

/**
 * Implementation of node class.
 */
class NormalNode extends Node {
    /**
     * Class constructor.
     * @param data data pack object
     */
    NormalNode(InfoPack data) {
        super(data);
        ui = new NormalNodeUI(data, this);
    }
}
