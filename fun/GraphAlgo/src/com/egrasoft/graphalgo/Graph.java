package com.egrasoft.graphalgo;

import com.egrasoft.graphalgo.components.GraphComponent;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.edges.EdgeFactory;
import com.egrasoft.graphalgo.components.edges.EdgeFactorySelector;
import com.egrasoft.graphalgo.components.nodes.Node;
import com.egrasoft.graphalgo.components.nodes.NodeFactory;
import com.egrasoft.graphalgo.components.nodes.NodeFactorySelector;
import com.egrasoft.graphalgo.tools.InfoPack;

import java.util.ArrayList;

/**
 * Class responsible for physical graph components' representations
 * storing and handling.
 */
public class Graph {


    /*==========================Fields==========================*/


    /**
     * Node elements container.
     */
    private ArrayList<Node> nodes = new ArrayList<>();

    /**
     * Edge elements container.
     */
    private ArrayList<Edge> edges = new ArrayList<>();

    /**
     * Node abstract factory.
     */
    private NodeFactory nFact;

    /**
     * Edge abstract factory.
     */
    private EdgeFactory eFact;


    /*======================Public Methods======================*/


    /**
     * Method creating new {@link Node} object and adding it to the nodes collection.
     * @param data node component data
     * @return newly created node object
     */
    public Node addNode(InfoPack data){
        data.put("nodeNumber", nodes.size()+1);
        Node n = nFact.instanceNode(data);
        nodes.add(n);
        return n;
    }

    /**
     * Method creating new {@link Edge} object and adding it to the edges collection.
     * Besides, invokes {@link Node#addAdjacentEdge(Edge)} method for connected nodes.
     * @param data edge component data
     * @return newly created edge object
     */
    public Edge addEdge(InfoPack data){
        Edge e = eFact.instanceEdge(data);
        e.getFromNode().addAdjacentEdge(e);
        e.getToNode().addAdjacentEdge(e);
        edges.add(e);
        return e;
    }

    /**
     * Method removing a graph component.
     * @param elem element to be removed
     */
    public void removeGraphComponent(GraphComponent elem){
        if (elem instanceof Edge){
            Edge e = (Edge) elem;
            e.getFromNode().removeAdjacentEdge(e);
            e.getToNode().removeAdjacentEdge(e);
            edges.remove(e);
        } else if (elem instanceof Node){
            Node n = (Node) elem;
            ArrayList<Edge> edges = n.getEdgesCopy();
            for (Edge e : edges)
                removeGraphComponent(e);
            nodes.remove(n);
        }
    }

    /**
     * Method setting particular implementations of abstract node and edge factories.
     * @param ns node factory selector constant
     * @param es edge factory selector constant
     */
    public void setFactories(NodeFactorySelector ns, EdgeFactorySelector es){ nFact = ns.getFactory(); eFact = es.getFactory(); }

    /**
     * Returns the nodes collection.
     * @return collection of node objects
     */
    public ArrayList<Node> getNodes() { return nodes; }

    /**
     * Returns the edges collection.
     * @return collection of edge objects
     */
    public ArrayList<Edge> getEdges() { return edges; }

    /**
     * Method invoking a {@link NodeFactory#getNodeInfo()} method to find out some
     * information about new node to be created.
     * @return data object for the new node
     */
    public InfoPack getNodeInfo(){ return nFact.getNodeInfo(); }

    /**
     * Method invoking a {@link EdgeFactory#getEdgeInfo()} method to find out some
     * information about new edge to be created.
     * @return data object for the new edge
     */
    public InfoPack getEdgeInfo(){ return eFact.getEdgeInfo(); }


}
