package cz.cvut.promod.resources;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResources {

    private static final Logger LOG = Logger.getLogger(Resources.class);

    private final static Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();
    private final static Map<String, File> configFiles = new HashMap<String, File>();

    /**
     * Returns the icon that has been already loaded or tries to load this icon.
     *
     * @param resourceName is the full name of required resource (= icon)
     * @return the required icon if no error(s) occurred, null otherwise
     */
    public static ImageIcon getIcon(final String resourceName) {
        ImageIcon imageIcon;

        if (icons.containsKey(resourceName)) {
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
     * Loads the icon.
     *
     * @param resourceName is the full name of the icon
     * @return the required icon or null if the there is no resource with given resourceName
     */
    private static ImageIcon loadIcon(final String resourceName) {
        final URL systemResource = ClassLoader.getSystemResource(resourceName);

        if (systemResource == null) {
            LOG.error("Resource " + resourceName + " couldn't be found.");
            return null;
        }

        return new ImageIcon(systemResource);
    }

    /**
     * Returns the resource that has been already loaded or tries to load this icon.
     *
     * @param resourceName is the full name of required resource
     * @return the required icon if no error(s) occurred, null otherwise
     */
    public static File getConfigFile(final String resourceName){
        File configFile;

        if(icons.containsKey(resourceName)){
            configFile = configFiles.get(resourceName);

        } else {
            configFile = loadConfigFile(resourceName);

            if(configFile != null){
                configFiles.put(resourceName, configFile);
            }
        }

        return configFile;
    }

    /**
     * Loads the config resources.
     *
     * @param resourceName is the full name of the resource
     * @return the required resource or null if the there is no resource with given resourceName
     */
    private static File loadConfigFile(final String resourceName) {
        final URL systemResource = ClassLoader.getSystemResource(resourceName);

        if(systemResource == null){
            LOG.error("Resource " + resourceName + " couldn't be found.");
            return null;
        }

        return new File(systemResource.getFile());
    }

}
