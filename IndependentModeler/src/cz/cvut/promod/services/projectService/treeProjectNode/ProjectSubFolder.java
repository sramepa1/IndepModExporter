package cz.cvut.promod.services.projectService.treeProjectNode;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.beans.Model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:02:57, 13.10.2009
 */

/**
 * Represents the project subfolder.
 */
public final class ProjectSubFolder implements ProjectContainer {

    private String displayName;

    /**
     * Creates a new project subfolder.
     *
     * @param displayName is the display name of the project subfolder.
     */
    public ProjectSubFolder(final String displayName) {
        this.displayName = displayName;
    }

    /** {@inheritDoc} */    
    public String getDisplayName() {
        return displayName;
    }

    /** {@inheritDoc} */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString(){
        return getDisplayName();
    }

}
