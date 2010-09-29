package cz.cvut.promod.services.extensionService;

import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.services.Service;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:40:57, 26.1.2010
 */

/**
 * Extension Service is a access point to all loaded extensions.
 */
public interface ExtensionService extends Service {

    /**
     * Returns the extension that is specified by the identifier.
     *
     * @param identifier is the extension's identifier
     *
     * @return required extension, null if there is not such a extension
     */
    public Extension getExtension(final String identifier);

    /**
     * Returns a list of all extensions.
     *
     * @return a list of all extensions
     */
    public List<Extension> getExtensions();

}
