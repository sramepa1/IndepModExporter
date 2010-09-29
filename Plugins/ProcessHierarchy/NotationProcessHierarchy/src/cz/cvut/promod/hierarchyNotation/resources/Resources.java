package cz.cvut.promod.hierarchyNotation.resources;

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
 * Date: 21:24:00, 7.12.2009
 *
 * Resources for the ProcessHierarchyWorkspaceData plugin.
 */
public class Resources {

    private static final Logger LOG = Logger.getLogger(Resources.class);

    public static final String RESOURCES_FILE = "hierarchy";

    public static final String ICONS = "cz/cvut/promod/hierarchyNotation/icons/";

    public static final String DIAGRAM = "diagram.png";
    public static final String SAVE = "save.png";
    public static final String SAVE_ALL = "saveAll.png";
    public static final String REFRESH = "refresh.png";
    public static final String MOVE = "move.png";
    public static final String PROCESS = "process.png";
    public static final String LINK = "link.png";
    public static final String DELETE = "delete.png";

    public static final String PREVIEW = "hierarchy_preview.png"; 

    private final static Map<String, ImageIcon>  icons = new HashMap<String, ImageIcon>();

    private static ResourceBundle resources = null;

    /**
     * @param resourceName the resource name
     * @return the required icon, null if there is no such an icon
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
     * @param resourceName is the icon's name
     * @return the loaded icon, null if there is no such a icon.
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
