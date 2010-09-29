package cz.cvut.promod.services.pluginLoaderService;

import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginDetails;
import cz.cvut.promod.plugin.extension.Extension;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:56:10, 12.10.2009
 */

/**
 * PluginLoaderService's responsibility is loading/instantiating of plugins specified in the ProMod plugin file.
 * This class is not supposed to be used by plugin developers at all.
 */
public interface PluginLoaderService extends Service{

    /**
     * An identifier of a plugin is not supposed to be an empty string or it's length is not supposed to exceed this
     * constant.
     */
    public static final int MAX_PLUGIN_IDENTIFIER_LENGTH = 36;


    /**
     * Loads definition of plugins from the file. The file is validate against the xsd schema file.
     *
     * @param pluginDefinitionFile is the file path that is supposed to contain the plugin definitions
     *
     * @return true if no error has occurred, false otherwise
     */
    public boolean loadPlugInsDefinition(final String pluginDefinitionFile);

    /**
     * Returns loaded information from the plugin definition file related to the notations and modules.
     *
     * The returned list is NOT supposed to be changed in any way (insert, delete, edit).
     *
     * @return info about notations and modules specified in plugin definition file
     */
    public List<PluginDetails> getNotationModulesInstInfo();


    /**
     * Returns loaded information from the plugin definition file related to the extensions.
     *
     * The returned list is NOT supposed to be changed in any way (insert, delete, edit).
     *
     * @return info about extensions specified in plugin definition file
     */
    public List<PluginDetails> getExtensionInstInfo();

    /**
     * Performs plugin instantiation. Results of this methods are then available by 'getters' of this service.
     * Plugins that are not possible to instantiate or plugins that are not valid won't be available be getters.
     * See error (using getErrors() method) to see why any plugins are missing.
     */
    public void instantiatePlugins();

    /**
     * Returns list of instantiated notations and modules. This list respects the order of notations and modules as
     * is defined by the definition in the plugin definition file.
     *
     * All notations and/or modules that do NOT fulfil all requirements are omitted from this list.
     *
     * The returned list is NOT supposed to be changed in any way (insert, delete, edit).
     *
     * @return list of instantiated notations and modules
     */
    public List<NotationSpecificPlugins> getNotationSpecificPlugins();

    /**
     * Returns list of instantiated extensions. This list respects the order of extensions like
     * is defined by the definition in the plugin definition file.
     *
     * The returned list is NOT supposed to be changed in any way (insert, delete, edit).
     *
     * All extensions that do NOT fulfil all requirements are omitted from this list.
     *
     * @return list of instantiated extensions
     */
    public List<Extension> getExtensions();

    /**
     * Returns a list holding information about errors that has occurred during plugin loading and instantiating.
     * The key is the alias defined in the plugin definition file. ProMod application doesn't require uniqueness for
     * this aliases, but it is strongly recommended. Otherwise it is very difficult to analyze the error log.
     *
     * The returned list is NOT supposed to be changed in any way (insert, delete, edit).
     *
     * @return list errors
     */
    public List<PluginLoadErrors> getErrors();
}
