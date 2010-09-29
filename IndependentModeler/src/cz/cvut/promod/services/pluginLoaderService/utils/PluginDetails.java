package cz.cvut.promod.services.pluginLoaderService.utils;

import java.util.List;
import java.util.LinkedList;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:50:27, 4.2.2010
 */

/**
 * Holds loaded info about the plugin(s) from the plugin configuration file.
 */
public class PluginDetails{

    private final String clazz;

    private final String config;

    private final String alias;

    private final List<PluginDetails> modules;

    /**
     * Constructs new PluginDetails object.
     *
     * @param clazz is the plugin's class ful qualified name
     * @param config is the plugin's configuration file
     * @param alias is the plugin's alias
     */
    public PluginDetails(final String clazz, final String config, final String alias) {
        this.clazz = clazz;
        this.config = config;
        this.alias = alias;

        modules = new LinkedList<PluginDetails>();
    }

    /**
     * Notations have it's modules, these modules are stored in list
     *
     * Do not use with extensions!
     *
     * @param pluginDetails is the plugin detail of the module
     */
    public void addPluginDetails(final PluginDetails pluginDetails){
        modules.add(pluginDetails);
    }

    /**
     * Returns the full qualified class name.
     *
     * @return full qualified class name
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Returns the plugin's configuration class.
     *
     * @return plugin's configuration file
     */
    public String getConfig() {
        return config;
    }

    /**
     * Returns the plugin's alias.
     *
     * @return plugin's alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns all modules of the notation.
     *
     * Do not use with extensions!
     *
     * @return the list of notation's modules.
     */
    public List<PluginDetails> getModules() {
        return modules;
    }

    /**
     * Pops loaded module = returns module in style FIFO
     *
     * Do not use with extensions.
     *
     * @return the most earlier loaded module in the list 
     */
    public PluginDetails pop() {
        if(modules.size() != 0){
            return  modules.remove(0);
        }

        return null;
    }
}
