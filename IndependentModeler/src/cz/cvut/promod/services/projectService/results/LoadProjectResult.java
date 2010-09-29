package cz.cvut.promod.services.projectService.results;

import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:32:57, 8.2.2010
 */

/**
 * Class for representing a return value of loadProject(String) method defined in ProjectControlService interface.
 *
 * If the ProjectRoot and event the message are null, it was not possible to load file. Possible reason is not valid
 * project file.
 */
public class LoadProjectResult{

    private final ProjectRoot projectRoot;

    private final List<ConfigurationDifference> messages;


    public LoadProjectResult(final ProjectRoot projectRoot, final List<ConfigurationDifference> messages) {
        this.projectRoot = projectRoot;
        this.messages = messages;
    }

    /**
     * Returns the project root.
     *
     * @return the loaded project root.
     */
    public ProjectRoot getProjectRoot() {
        return projectRoot;
    }

    /**
     * Returns list of messages about ProMod configuration differences.
     *
     * @return list of messages about ProMod configuration differences
     */
    public List<ConfigurationDifference> getMessages() {
        return messages;
    }
}
