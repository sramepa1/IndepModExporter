package cz.cvut.promod.services.projectService.treeProjectNode;

import com.jgoodies.binding.value.ValueModel;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:44:48, 13.10.2009
 */

/**
 * Common interface for project items.
 */
public interface ProjectItem{

    /**
     * @return the display name of the project item
     */
    public String getDisplayName();

    /**
     * Sets the project item display name.
     *
     * @param displayName new display name
     */
    public void setDisplayName(final String displayName);

}
