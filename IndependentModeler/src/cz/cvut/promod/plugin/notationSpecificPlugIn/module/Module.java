package cz.cvut.promod.plugin.notationSpecificPlugIn.module;

import cz.cvut.promod.plugin.notationSpecificPlugIn.NotationSpecificPlugin;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:58:36, 12.10.2009
 */

/**
 * Module is notation specific type of plugin. It is supposed to extend some functionality of a notation. Modules can
 * add some dockable frames, use tool bar, status bar, menus, etc.
 *
 * Any module is always related to a notation.
 *
 * There are several possible ways how to instantiate the module. An author of a module has to provide the module's
 * definition with one of following types constructor. If the PluginLoaderService finds such a constructor, then it
 * uses this one. Priority/preferred constructor is determined using order rules. To get understand these rules then
 * check out comments about the Plugin Loader Service.
 *
 * @see cz.cvut.promod.services.pluginLoaderService.PluginLoaderService for more details about module instantiation 
 */
public interface Module extends NotationSpecificPlugin {

    /**
     * Returns the notation identifier which is the module related to.
     *
     * @return notation identifier
     */
    public String getRelatedNotationIdentifier();

}
