package com.egrasoft.graphalgo.components.nodes;

import com.egrasoft.graphalgo.FactoryInfo;
import com.egrasoft.graphalgo.MainFrame;
import com.egrasoft.graphalgo.components.nodes.NormalNode.NormalNodeFactory;
import com.egrasoft.graphalgo.exceptions.AnnotationNotPresentException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Enum selector for the node abstract factory.
 */
public enum NodeFactorySelector {
    /**
     * Constant value.
     */
    NORMAL_NODE(new NormalNodeFactory());


    /*==========================Fields==========================*/


    /**
     * Node factory associated with this enum object.
     */
    private NodeFactory fact;

    /**
     * Icon image object to be associated with this kind of nodes and used in toolbox.
     */
    private Image img;

    /**
     * Hint string to be associated with this kind of nodes and used in toolbox.
     */
    private String hint;

    /**
     * Description string to be associated with this kind of nodes and used in toolbox.
     */
    private String description;


    /*=======================Constructors=======================*/


    /**
     * Enum constructor.
     * @param fact node factory to be associated with this item
     */
    NodeFactorySelector(NodeFactory fact){
        try {
            setNodeAttributes(fact);
        } catch (AnnotationNotPresentException | IOException | MissingResourceException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.fact = fact;
    }


    /*======================Private Methods======================*/


    /**
     * Method checking and setting attributes of the current node type. Invoked by the enum constructor.
     * @param fact factory object to checked
     * @throws AnnotationNotPresentException if fact's class doesn't have a FactoryInfo annotation
     * @throws IOException if an exception occured while reading image specified by an iconPath() parameter in
     * FactoryInfo annotation applied to fact's class is incorrect
     * @throws MissingResourceException if string resource bundle doesn't contain hintResourceBundleKey() or
     * descriptionResourceBundleKey() keys defined by FactoryInfo annotation applied to fact's class
     */
    private void setNodeAttributes(NodeFactory fact) throws AnnotationNotPresentException, IOException{
        Class<?> fClass = fact.getClass();
        if (!fClass.isAnnotationPresent(FactoryInfo.class))
            throw new AnnotationNotPresentException("Cannot find a FactoryInfo annotation on "+fClass.getName()+" class");
        FactoryInfo anno = fClass.getAnnotation(FactoryInfo.class);
        ResourceBundle res = MainFrame.getResourceBundle();
        if (getClass().getResource())
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
     * Method returning the node factory object.
     * @return node factory object
     */
    public NodeFactory getFactory(){ return fact; }

    /**
     * Getter-method for img field.
     * @return icon image associated with this kind of nodes
     */
    public Image getImg(){ return img; }

    /**
     * Getter-method for hint field.
     * @return hint string associated with this kind of nodes
     */
    public String getHint() { return hint; }

    /**
     * Getter-method for description field.
     * @return description string associated with this kind of nodes
     */
    public String getDescription() { return description; }


}