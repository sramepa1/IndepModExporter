package cz.cvut.promod.services.projectService.results;

import javax.swing.tree.TreePath;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:17:50, 16.11.2009
 */

/**
 * Add project item result holder.
 */
public class AddProjectItemResult {

    final private AddProjectItemStatus status;

    final private TreePath treePath;

    public AddProjectItemResult(final AddProjectItemStatus status,
                                final TreePath treePath){

        this.status = status;
        this.treePath = treePath;
    }

    public AddProjectItemStatus getStatus() {
        return status;
    }

    public TreePath getTreePath() {
        return treePath;
    }
}
