package com.egrasoft.graphalgo;

import com.egrasoft.graphalgo.components.GraphComponent;
import com.egrasoft.graphalgo.components.GraphComponentFactorySelector;
import com.egrasoft.graphalgo.components.edges.Edge;
import com.egrasoft.graphalgo.components.edges.EdgeFactory;
import com.egrasoft.graphalgo.components.edges.EdgeFactorySelector;
import com.egrasoft.graphalgo.components.nodes.Node;
import com.egrasoft.graphalgo.components.nodes.NodeFactory;
import com.egrasoft.graphalgo.components.nodes.NodeFactorySelector;
import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.tools.SafeCollectionWrapper;

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
     * Safe iterator for node collection
     */
    private SafeCollectionWrapper<Node> nodesIter = new SafeCollectionWrapper<>(nodes);

    /**
     * Edge elements container.
     */
    private ArrayList<Edge> edges = new ArrayList<>();

    /**
     * Safe iterator for edge collection
     */
    private SafeCollectionWrapper<Edge> edgesIter = new SafeCollectionWrapper<>(edges);

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
        Node n = nFact.instance(data);
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
        Edge e = eFact.instance(data);
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
            SafeCollectionWrapper<Edge> adjEdges = n.getAdjacentEdges();
            for (Edge e : adjEdges)
                removeGraphComponent(e);
            int i=0;
            while (i < nodes.size()){
                Node nod = nodes.get(i);
                if (nod==n)
                    break;
                i++;
            }
            for (int j = i+1; j<nodes.size(); j++)
                nodes.get(j).decrementNumber();
            nodes.remove(i);
        }
    }

    /**
     * Method setting concrete node factory.
     * @param ns node factory selector constant
     */
    public void setNodeFactory(GraphComponentFactorySelector ns){ nFact = ((NodeFactorySelector)ns).getFactory(); }

    /**
     * Method setting concrete edge factory.
     * @param es edge factory selector constant
     */
    public void setEdgeFactory(GraphComponentFactorySelector es){ eFact = ((EdgeFactorySelector)es).getFactory(); }

    /**
     * Resets and returns safe iterator for the nodes collection.
     * @return collection of node objects
     */
    public SafeCollectionWrapper<Node> getNodes() { return nodesIter; }

    /**
     * Resets and returns safe iterator for the edges collection.
     * @return collection of edge objects
     */
    public SafeCollectionWrapper<Edge> getEdges() { return edgesIter; }

    /**
     * Method invoking a {@link NodeFactory#getInfo()} method to find out some
     * information about new node to be created.
     * @return data object for the new node
     */
    public InfoPack getNodeInfo(){ return nFact.getInfo(); }

    /**
     * Method invoking a {@link EdgeFactory#getInfo()} method to find out some
     * information about new edge to be created.
     * @return data object for the new edge
     */
    public InfoPack getEdgeInfo(){ return eFact.getInfo(); }


}
