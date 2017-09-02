package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.components.GraphComponentUI;
import com.egrasoft.graphalgo.tools.InfoPack;

import java.awt.*;
import java.awt.geom.*;
import java.util.function.Function;

/**
 * Abstract edge GUI representation.
 */
public abstract class EdgeUI extends GraphComponentUI{


    /*==========================Fields==========================*/


    /**
     * Link to the physical edge object associated with this object.
     */
    protected Edge entity;

    /**
     * If true, this edge is considered to be curved and curvePoint is
     * used as a control point, straight otherwise.
     */
    protected boolean curved = false;

    /**
     * Control point responsible for this edge being curved.
     */
    protected Point curvePoint;

    /**
     * Graphics2D representation of this object (Quadratic Bezier Curve).
     */
    protected QuadCurve2D.Double shape;


    /*=======================Constructors=======================*/


    /**
     * Class constructor.
     * @param data data object (unused here)
     * @param entity edge physical entity object
     */
    public EdgeUI(InfoPack data, Edge entity){ this.entity = entity; }


    /*======================Public Methods======================*/


    /**
     * Method removing the line curving (by setting curved state to false).
     */
    public void removeCurving(){ curved = false; }

    /**
     * Method setting the line curving and its control point.
     * @param coords new control point
     */
    public void setCurving(Point coords){ curved = true; curvePoint = coords; }

    /**
     * Method setting the line curving and its control point.
     * @param x x-coordinate of control point
     * @param y y-coordinate of control point
     */
    public void setCurving(int x, int y){
        curved = true;
        if (curvePoint==null)
            curvePoint = new Point(x, y);
        else
            curvePoint.setLocation(x, y);
    }


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
    public Edge getEntity(){ return entity; }

    /**
     * Test boolean method checking if rectangle intersects this edge. Uses
     * the subdivision of this curve into some pieces and rounding it to the
     * angled line, then checks an intersection with every line segment.
     * Specified by {@link GraphComponentUI#intersects(Rectangle)}.
     * @param rect rectangle to be checked
     * @return boolean value, true if the rectangle intersects the curve, false otherwise
     */
    @Override
    public boolean intersects(Rectangle rect){
        double segments = 5;
        Function<Double, Point> curvePoint = (Double t)->{
            double x = (1-t)*(1-t)*shape.x1 + 2*t*(1-t)*shape.ctrlx + t*t*shape.x2,
                    y = (1-t)*(1-t)*shape.y1 + 2*t*(1-t)*shape.ctrly + t*t*shape.y2;
            return new Point((int)x, (int)y);
        };

        double x1, y1, x2, y2, yt1, yt2, xt1, xt2, dx, dy,
                rx1 = rect.x,
                ry1 = rect.y,
                rx2 = rect.x+rect.width,
                ry2 = rect.y+rect.height;
        Point prev = curvePoint.apply(0.0), cur;
        for (double t=1/segments; t<=1; t+=1/segments, prev = cur){
            cur = curvePoint.apply(t);
            x1 = prev.x;
            y1 = prev.y;
            x2 = cur.x;
            y2 = cur.y;
            if ((x1 < rx1 && x2 < rx1) || (y1 < ry1 && y2 < ry1) || (x1 > rx2 && x2 > rx2) || (y1 > ry2 && y2 > ry2))
                continue;
            if (x1==x2 || y1==y2)
                return true;
            dx = x2-x1;
            dy = y2-y1;
            yt1 = y1 + dy * (rx1 - x1) / dx;
            yt2 = y1 + dy * (rx2 - x1) / dx;
            xt1 = x1 + dx * (ry1 - y1) / dy;
            xt2 = x1 + dx * (ry2 - y1) / dy;

            if ((ry1 < yt1 && yt1 < ry2) || (ry1 < yt2 && yt2 < ry2) || (rx1 < xt1 && xt1 < rx2) || (rx1 < xt2 && xt2 < rx2))
                return true;
        }
        return false;
    }


}
