package cz.cvut.promod.epcImageExport.resources;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.*;
import java.net.URL;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:24:00, 7.12.2009
 *
 * Resources holder of the EPCImageExportModule plugin.
 */
public class Resources {

    private static final Logger LOG = Logger.getLogger(Resources.class);

    public static final String RESOURCES_FILE = "epcimageexport";

    private static ResourceBundle resources = null;

    private final static Map<String, ImageIcon>  icons = new HashMap<String, ImageIcon>();


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

    private static ImageIcon loadIcon(final String resourceName) {
        final URL systemResource = ClassLoader.getSystemResource(resourceName);

        if(systemResource == null){
            LOG.error("Resource " + resourceName + " couldn't be found.");
            return null;
        }

        return new ImageIcon(systemResource);
    }

    /**
     * Returns resources fot this module.
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