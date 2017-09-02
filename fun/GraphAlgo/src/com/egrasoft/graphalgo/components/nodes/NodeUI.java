package com.egrasoft.graphalgo.components.nodes;

import com.egrasoft.graphalgo.components.GraphComponentUI;
import com.egrasoft.graphalgo.tools.InfoPack;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Abstract class representing GUI for graph node.
 */
public abstract class NodeUI extends GraphComponentUI{


    /*==========================Fields==========================*/


    /**
     * Link to a physical node object associated with this element.
     */
    protected Node entity;

    /**
     * Graphics2D shape representing this element.
     */
    protected Ellipse2D.Double shape;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data pack (unused here)
     * @param entity physical node entity
     */
    public NodeUI(InfoPack data, Node entity){
        this.entity = entity;
    }


    /*=====================Protected Methods=====================*/


    /**
     * Method drawing a string centered in particular rectangle.
     * @param g graphics object
     * @param s string being drawn
     * @param rect rectangle to center the string in
     * @param f font being applied to draw the string
     */
    protected void drawCenteredString(Graphics2D g, String s, Rectangle rect, Font f){
        FontMetrics metrics = g.getFontMetrics(f);
        int x = rect.x + (rect.width - metrics.stringWidth(s)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(f);
        g.drawString(s, x, y);
    }


    /*======================Public Methods======================*/


    /**
     * Method moving current node to the specified position.
     * @param coords new location of this node
     */
    public abstract void moveTo(Point coords);

    /**
     * Method moving current node to the specified position.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public abstract void moveTo(int x, int y);

    /**
     * Method returning current node's location.
     * @return location of this node
     */
    public abstract Point getCenterLocation();


    /*=====================Inherited Methods=====================*/


    /**
     * @see GraphComponentUI#draw(Graphics2D)
     */
    @Override
    public abstract void draw(Graphics2D g);

    /**
     * @see GraphComponentUI#select(Graphics2D)
     */
    @Override
    public abstract void select(Graphics2D g);

    /**
     * @see GraphComponentUI#getEntity()
     */
    @Override
    public Node getEntity(){ return entity; }

    /**
     * @see GraphComponentUI#intersects(Rectangle)
     */
    @Override
    public boolean intersects(Rectangle rect) { return shape.intersects(rect); }
}
