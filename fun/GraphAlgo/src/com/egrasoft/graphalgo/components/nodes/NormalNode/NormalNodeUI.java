package com.egrasoft.graphalgo.components.nodes.NormalNode;

import com.egrasoft.graphalgo.tools.InfoPack;
import com.egrasoft.graphalgo.components.nodes.NodeUI;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Class representing a GUI for normal node objects.
 */
class NormalNodeUI extends NodeUI{


    /*==========================Fields==========================*/


    /**
     * Node background color.
     */
    private static Color fillColor = new Color(255,255,200);
    /**
     * Node border color.
     */
    private static Color borderColor = new Color(0,0,0);
    /**
     * Node text color.
     */
    private static Color textColor = new Color(50, 0, 180);
    /**
     * Node border stroke when not selected.
     */
    private static Stroke normalStroke = new BasicStroke(1);
    /**
     * Selected node border stroke.
     */
    private static Stroke selectStroke = new BasicStroke(3);
    /**
     * Node text font.
     */
    private static Font textFont = new Font("Arial", Font.BOLD, 14);
    /**
     * Node diameter.
     */
    private int shapeSize = 30;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data pack object (must contain <b>{@literal <}"nodeCoords", Point{@literal >}</b> field)
     * @param node physical normal node entity
     */
    NormalNodeUI(InfoPack data, NormalNode node) {
        super(data, node);
        Point coords = (Point) data.get("nodeCoords");
        shape = new Ellipse2D.Double(coords.x-shapeSize/2, coords.y-shapeSize/2, shapeSize, shapeSize);
    }


    /*=====================Inherited Methods=====================*/

    /**
     * @see NodeUI#getCenterLocation()
     */
    @Override
    public Point getCenterLocation() {
        return new Point((int)(shape.x+shape.width/2), (int)(shape.y+shape.height/2));
    }

    /**
     * @see NodeUI#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D g) {
        g.setStroke(normalStroke);

        g.setColor(fillColor);
        g.fill(shape);

        g.setColor(borderColor);
        g.draw(shape);

        g.setColor(textColor);
        g.setFont(textFont);
        drawCenteredString(g, String.valueOf(entity.getNumber()), shape.getBounds(), textFont);
    }

    /**
     * @see NodeUI#select(Graphics2D)
     */
    @Override
    public void select(Graphics2D g) {
        g.setStroke(selectStroke);
        g.setColor(borderColor);
        g.draw(shape);
    }

    /**
     * @see NodeUI#moveTo(Point)
     */
    @Override
    public void moveTo(Point coords) { shape.setFrame(coords.x-shapeSize/2, coords.y-shapeSize/2, shapeSize, shapeSize); }

    /**
     * @see NodeUI#moveTo(int x, int y)
     */
    @Override
    public void moveTo(int x, int y) { shape.setFrame(x-shapeSize/2, y-shapeSize/2, shapeSize, shapeSize); }


}
