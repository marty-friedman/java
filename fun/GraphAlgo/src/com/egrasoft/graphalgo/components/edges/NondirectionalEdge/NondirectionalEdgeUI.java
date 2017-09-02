package com.egrasoft.graphalgo.components.edges.NondirectionalEdge;

import com.egrasoft.graphalgo.components.edges.EdgeUI;
import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.nodes.NodeUI;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * Class representing a GUI for non-directional edge objects.
 */
class NondirectionalEdgeUI extends EdgeUI {


    /*==========================Fields==========================*/


    /**
     * Edge color.
     */
    private static Color lineColor = new Color(180,0,0);
    /**
     * Unselected edge stroke.
     */
    private static Stroke normalStroke = new BasicStroke(1);
    /**
     * Selected edge stroke.
     */
    private static Stroke selectStroke = new BasicStroke(3);


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data pack object
     * @param edge physical non-directional edge entity
     */
    NondirectionalEdgeUI(InfoPack data, NondirectionalEdge edge){
        super(data, edge);
        NodeUI from = entity.getFromNode().getUI(), to = entity.getToNode().getUI();
        Point fromCoords = from.getCenterLocation(), toCoords = to.getCenterLocation();
        shape = new QuadCurve2D.Double(fromCoords.x, fromCoords.y, fromCoords.x, fromCoords.y, toCoords.x, toCoords.y);
    }


    /*=====================Inherited Methods=====================*/


    /**
     * Method used for drawing a non-directional edge object.
     * @param g graphics object
     */
    @Override
    public void draw(Graphics2D g) {
        NodeUI from = entity.getFromNode().getUI(), to = entity.getToNode().getUI();
        Point fromCoords = from.getCenterLocation(), toCoords = to.getCenterLocation(), curv;
        if (!curved)
            curv = new Point(fromCoords.x, fromCoords.y);
        else
            curv = curvePoint;
        shape.setCurve(fromCoords.x, fromCoords.y, curv.x, curv.y, toCoords.x, toCoords.y);

        g.setStroke(normalStroke);
        g.setColor(lineColor);
        g.draw(shape);
    }

    /**
     * Method used for selection a non-directional edge object.
     * @param g graphics object
     */
    @Override
    public void select(Graphics2D g) {
        g.setStroke(selectStroke);
        g.setColor(lineColor);
        g.draw(shape);
    }


}
