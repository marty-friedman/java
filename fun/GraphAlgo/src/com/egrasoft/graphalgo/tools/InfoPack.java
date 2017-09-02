package com.egrasoft.graphalgo.tools;

import com.egrasoft.graphalgo.components.nodes.Node;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Class storing data for initializing graph components. Typically, it's
 * just a wrapper for {@link HashMap} class, using {@link String} as a key
 * and {@link Object} as a value. Furthermore, it provides key/val checking
 * as well.
 */
public class InfoPack {
    /**
     * HashMap object storage.
     */
    private HashMap<String, Object> data;

    /**
     * Known key tags. <br>
     * - nodeCoords (Point value) <br>
     * - fromNode (Node value) <br>
     * - toNode (Node value) <br>
     * - nodeNumber (Integer value)
     */
    private static TreeSet<String> keys;

    {
        data = new HashMap<>();
        keys = new TreeSet<>(Arrays.asList("nodeCoords", "fromNode", "toNode", "nodeNumber"));
    }

    /**
     * Put-method for data storage.
     * @param key key object
     * @param val value object
     * @throws IllegalArgumentException if key is not valid or the value type mismatches the expected one in accordance with key
     */
    public void put(String key, Object val){
        String illegalType = "Object being put into the data pack under the key \"" + key + "\" is not a valid object of an expected type",
                unknownKey = "Unknown key value detected while putting an object into the data pack";
        boolean illegal = false;
        switch(key){
            case "nodeCoords":
                if (!(val instanceof Point))
                    illegal = true;
                break;
            case "fromNode": case "toNode":
                if (!(val instanceof Node))
                    illegal = true;
                break;
            case "nodeNumber":
                if (val instanceof Node)
                    illegal = true;
                break;
            default:
                throw new IllegalArgumentException(unknownKey);
        }
        if (illegal)
            throw new IllegalArgumentException(illegalType);

        data.put(key, val);
    }

    /**
     * Get-method for data storage.
     * @param key key object
     * @return value object
     * @throws IllegalArgumentException if key is not valid
     */
    public Object get(String key){
        String unknownkey = "Unknown key value detected while putting an object into the data pack";
        if (!keys.contains(key))
            throw new IllegalArgumentException(unknownkey);
        return data.get(key);
    }
}
