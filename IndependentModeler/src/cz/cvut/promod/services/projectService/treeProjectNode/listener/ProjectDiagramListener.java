package cz.cvut.promod.services.projectService.treeProjectNode.listener;

import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagramChange;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:06:56, 18.1.2010
 */

/**
 * Listener of changes in an instance of ProjectDiagram.
 *
 * @see cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram
 */
public interface ProjectDiagramListener {

    /**
     * Listener method. Whenever any publishable change occurs, this method of all listeners is invoked.
     *
     * @param change is an object holding info about the change
     *
     * @see cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagramChange
     */
    public void changePerformed(final ProjectDiagramChange change);
}
