package com.egrasoft.graphalgo.components.edges;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.MainFrame;
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
public enum EdgeFactorySelector {
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
            setEdgeAttributes(fact);
        } catch (AnnotationNotPresentException | IOException | MissingResourceException | IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.fact = fact;
    }


    /*======================Private Methods======================*/


    /**
     * Method checking and setting attributes of the current edge type. Invoked by the enum constructor.
     * @param fact factory object to checked
     * @throws AnnotationNotPresentException if fact's class doesn't have a FactoryInfo annotation
     * @throws IOException if an IO exception occured while reading image specified by an iconPath() parameter in
     * FactoryInfo annotation applied to fact's class is incorrect
     * @throws IllegalArgumentException if image file is not present
     * @throws MissingResourceException if string resource bundle doesn't contain hintResourceBundleKey() or
     * descriptionResourceBundleKey() keys defined by FactoryInfo annotation applied to fact's class
     */
    private void setEdgeAttributes(EdgeFactory fact) throws AnnotationNotPresentException, IOException{
        Class<?> fClass = fact.getClass();
        if (!fClass.isAnnotationPresent(FactoryInfo.class))
            throw new AnnotationNotPresentException("Cannot find a FactoryInfo annotation on "+fClass.getName()+" class");
        FactoryInfo anno = fClass.getAnnotation(FactoryInfo.class);
        ResourceBundle res = MainFrame.getResourceBundle();
        img = ImageIO.read(getClass().getResource(anno.iconPath()));
        if (!res.containsKey(anno.hintResourceBundleKey()))
            throw new MissingResourceException("Hint string resource key of "+fClass.getName()+" class was not found", "ResourceBundle", anno.hintResourceBundleKey());
        hint = res.getString(anno.hintResourceBundleKey());
        if (!res.containsKey(anno.descriptionResourceBundleKey()))
            throw new MissingResourceException("Description string resource key of "+fClass.getName()+" class was not found", "ResourceBundle", anno.descriptionResourceBundleKey());
        description = res.getString(anno.descriptionResourceBundleKey());
    }


    /*======================Public Methods======================*/


    /**
     * Method returning the edge factory object.
     * @return edge factory object
     */
    public EdgeFactory getFactory(){ return fact; }

    /**
     * Getter-method for img field.
     * @return icon image associated with this kind of edges
     */
    public Image getImg(){ return img; }

    /**
     * Getter-method for hint field.
     * @return hint string associated with this kind of edges
     */
    public String getHint() { return hint; }

    /**
     * Getter-method for description field.
     * @return description string associated with this kind of edges
     */
    public String getDescription() { return description; }


}
