package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.components.GraphComponent;
import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.nodes.Node;

/**
 * Abstract class representing physical graph edge component.
 */
public abstract class Edge extends GraphComponent{


    /*==========================Fields==========================*/


    /**
     * Link to the physical start-node object.
     */
    protected Node from;

    /**
     * Link to the physical end-node object.
     */
    protected Node to;

    /**
     * Link to the GUI representation of this element.
     */
    protected EdgeUI ui;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data pack object (must contain <b>{@literal <}"fromNode", Node{@literal >}</b> and <b>{@literal <}"toNode", Node{@literal >}</b> fields)
     */
    public Edge(InfoPack data){
        from = (Node) data.get("fromNode");
        to = (Node) data.get("toNode");
    }


    /*======================Public Methods======================*/


    /**
     * Getter-method for the start-node of this edge.
     * @return start-node object
     */
    public Node getFromNode(){ return from; }

    /**
     * Getter method for the end-node of this edge.
     * @return end-node object
     */
    public Node getToNode(){ return to; }


    /*=====================Inherited Methods=====================*/

    /**
     * @see GraphComponent#getUI()
     */
    public EdgeUI getUI(){ return ui; }


}
