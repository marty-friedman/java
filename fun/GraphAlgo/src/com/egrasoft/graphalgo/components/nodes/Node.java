package com.egrasoft.graphalgo.components.nodes;

import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.GraphComponent;
import com.egrasoft.graphalgo.tools.InfoPack;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Abstract class representing physical node object.
 */
public abstract class Node extends GraphComponent{


    /*==========================Fields==========================*/

    /**
     * Node number. Typically, it's recommended to display it in the frame.
     */
    private int number;

    /**
     * Array of adjacent for the current node edges.
     */
    ArrayList<Edge> edges = new ArrayList<>();

    /**
     * Link to the GUI node representation.
     */
    protected NodeUI ui;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data pack object (must contain <b>{@literal <}"nodeNumber", Integer{@literal >}</b> field)
     */
    public Node(InfoPack data){
        number = (Integer) data.get("nodeNumber");
    }


    /*======================Public Methods======================*/


    /**
     * Adds an edge object to the array of adjacent edges.
     * @param e edge to be added
     */
    public void addAdjacentEdge(Edge e) { edges.add(e); }

    /**
     * Removes an edge from the array of adjacent edges.
     * @param e edge to be removed
     */
    public void removeAdjacentEdge(Edge e) { edges.remove(e); }

    /**
     * Returns the copy of the adjacent edges array.
     * @return copy of edges array
     */
    public ArrayList<Edge> getEdgesCopy() { return new ArrayList<>(edges); }

    /**
     * Returns number of this node.
     * @return node number
     */
    public int getNumber(){ return number; }

    /**
     * Decrements node number.
     */
    public void decrementNumber(){ number--; }


    /*=====================Inherited Methods=====================*/


    /**
     * @see GraphComponent#getUI()
     */
    public NodeUI getUI(){ return ui; }


}
