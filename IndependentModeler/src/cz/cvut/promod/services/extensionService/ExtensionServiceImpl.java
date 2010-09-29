package cz.cvut.promod.services.extensionService;

import cz.cvut.promod.plugin.extension.Extension;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:41:08, 26.1.2010
 */

/**
 * Implementation of Extension Service.
 */
public class ExtensionServiceImpl implements ExtensionService {

    private final List<Extension> extensionsList;


    public ExtensionServiceImpl(final List<Extension> extensionsList) {
        this.extensionsList = extensionsList;
    }

    /** {@inheritDoc} */
    public Extension getExtension(final String identifier) {
        for(final Extension extension : extensionsList){
            if(extension.getIdentifier().equals(identifier)){
                return extension;
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    public List<Extension> getExtensions() {
        return extensionsList;
    }

    /** {@inheritDoc} */
    public boolean check() {
        return true;
    }
}
