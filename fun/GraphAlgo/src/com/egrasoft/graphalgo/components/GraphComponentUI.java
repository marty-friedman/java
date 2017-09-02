package com.egrasoft.graphalgo.components;

import java.awt.*;

/**
 * Abstract class representing GUI graph component.
 */
public abstract class GraphComponentUI {
    /**
     * Draw method for the graph component.
     * @param g graphics object
     */
    public abstract void draw(Graphics2D g);

    /**
     * Selection method for the graph component.
     * @param g graphics object
     */
    public abstract void select(Graphics2D g);

    /**
     * Method returning a link to the physical graph component associated
     * with this element.
     * @return physical graph component
     */
    public abstract GraphComponent getEntity();

    /**
     * Method checking an intersection of the particular graph component
     * with the given rectangle.
     * @param rect rectangle object
     * @return boolean value (true if this component intersects the rectangle, false otherwise)
     */
    public abstract boolean intersects(Rectangle rect);
}
