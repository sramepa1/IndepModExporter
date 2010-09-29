package cz.cvut.promod.ph.treeLayout.resources;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.net.URL;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 12:17:45, 28.1.2010
 *
 * Resources used by ProcessHierarchyTreeLayout plugin.
 */
public class Resources {

    private static final Logger LOG = Logger.getLogger(Resources.class);

    public static final String RESOURCES_FILE = "hierarchyLayout";

    public static final String ICONS = "cz/cvut/promod/ph/treeLayout/icons/";

    public static final String LAYOUT = "layout.png";

    private final static Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

    private static ResourceBundle resources = null;

    /**
     * Returns required icon.
     *
     * @param resourceName ois the name of the icon.
     * @return required icon or null if there is no such an icon
     */
    public static ImageIcon getIcon(final String resourceName){
        ImageIcon imageIcon;

        if(icons.containsKey(resourceName)){
            imageIcon = icons.get(resourceName);

        } else {
            imageIcon = loadIcon(resourceName);

            if(imageIcon != null){
                icons.put(resourceName, imageIcon);
            }
        }

        return imageIcon;

    }

    /**
     * Loads the required icon.
     *
     * @param resourceName is the name of the icon
     * @return the loaded icon or null if there is not such an icon
     */
    private static ImageIcon loadIcon(final String resourceName) {
        final URL systemResource = ClassLoader.getSystemResource(resourceName);

        if(systemResource == null){
            LOG.error("Resource " + resourceName + " couldn't be found.");
            return null;
        }

        return new ImageIcon(systemResource);
    }

    /**
     * Returns resources for this notation.
     *
     * @return the resource bundle
     */
    public static ResourceBundle getResources(){
        if(resources == null){
            try{
                resources = ResourceBundle.getBundle(RESOURCES_FILE);

            } catch (MissingResourceException e){
                LOG.error("Couldn't load the resource bundle file.");
            }
        }

        return resources;
    }

}
