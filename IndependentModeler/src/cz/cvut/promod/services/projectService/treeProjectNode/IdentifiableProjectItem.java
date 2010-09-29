package cz.cvut.promod.services.projectService.treeProjectNode;

import java.util.UUID;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:11:41, 1.11.2009
 */

/**
 * Common interface for identifiable project items.
 */
public interface IdentifiableProjectItem extends ProjectItem{

    /**
     * @return the project item identifier
     */
    public UUID getUuid();

}
