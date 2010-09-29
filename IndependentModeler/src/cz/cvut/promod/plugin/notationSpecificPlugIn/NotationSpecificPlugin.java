package cz.cvut.promod.plugin.notationSpecificPlugIn;

import cz.cvut.promod.plugin.Plugin;

import java.util.Set;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:57:52, 12.10.2009
 */

/**
 * Common interface for so-called notation-specific plugins.
 */
public interface NotationSpecificPlugin extends Plugin {

    /**
     * Returns a set of dockable frames (windows) definition.
     *
     * @return a set of dockable frames (windows) definition
     */
    public Set<DockableFrameData> getDockableFrames();

}
