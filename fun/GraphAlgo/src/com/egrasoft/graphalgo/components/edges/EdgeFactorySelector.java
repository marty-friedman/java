package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.MainFrame;
import com.egrasoft.graphalgo.components.GraphComponentFactorySelector;
import com.egrasoft.graphalgo.components.edges.NondirectionalEdge.NondirectionalEdgeFactory;
import com.egrasoft.graphalgo.exceptions.AnnotationNotPresentException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Enum selector for the edge abstract factory.
 */
public enum EdgeFactorySelector implements GraphComponentFactorySelector {
    /**
     * Constant value.
     */
    NONDIRECTIONAL_EDGE(new NondirectionalEdgeFactory());


    /*==========================Fields==========================*/


    /**
     * Edge factory associated with this enum object.
     */
    private EdgeFactory fact;

    /**
     * Icon image object to be associated with this kind of edges and used in toolbox.
     */
    private Image img;

    /**
     * Hint string to be associated with this kind of edges and used in toolbox.
     */
    private String hint;

    /**
     * Description string to be associated with this kind of edges and used in toolbox.
     */
    private String description;


    /*=======================Constructors=======================*/


    /**
     * Enum constructor.
     * @param fact edge factory to be associated with this item
     */
    EdgeFactorySelector(EdgeFactory fact){
        try {
            validateAttributes(fact);
            FactoryInfo anno = fact.getComponentClass().getAnnotation(FactoryInfo.class);
            ResourceBundle res = MainFrame.getResourceBundle();
            img = ImageIO.read(getClass().getResource(anno.iconPath()));
            hint = res.getString(anno.hintResourceBundleKey());
            description = res.getString(anno.descriptionResourceBundleKey());
        } catch (AnnotationNotPresentException | IOException | MissingResourceException | IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.fact = fact;
    }


    /*======================Public Methods======================*/


    /**
     * Method returning the edge factory object.
     * @return edge factory object
     */
    public EdgeFactory getFactory(){ return fact; }


    /*=====================Inherited Methods=====================*/


    /**
     * @see GraphComponentFactorySelector#getImg()
     */
    @Override
    public Image getImg(){ return img; }

    /**
     * @see GraphComponentFactorySelector#getHint()
     */
    @Override
    public String getHint() { return hint; }

    /**
     * @see GraphComponentFactorySelector#getDescription()
     */
    @Override
    public String getDescription() { return description; }


}
