package cz.cvut.promod.services.projectService;

import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.listener.ProjectDiagramListener;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

import com.jgoodies.binding.value.ValueModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:46:55, 10.10.2009
 */

/**
 * ProjectService is responsible for operating the project(s) data.
 *
 * This service provides the developer with an access to particular project data and allows developer
 * to operate with this project data. 
 */
public interface ProjectService extends Service{

    /**
     * ProMod project file extension.
     */
    public static final String PROJECT_FILE_EXTENSION_NAME = "pmp";

    /**
     * File extension delimiter. 
     */
    public static final String FILE_EXTENSION_DELIMITER = ".";

    /**
     * ProMod project file extension with delimiter.
     */
    public static final String PROJECT_FILE_EXTENSION = FILE_EXTENSION_DELIMITER + PROJECT_FILE_EXTENSION_NAME;

    /**
     * Returns null if no diagram is selected in projects navigation tree, or corresponding notation identifier to the
     * selected diagram. There is no equivalent set method. Use ProjectControlService interface instead.
     *
     * @return notation identifier if and only if there is any diagram selected in the project navigation tree, null otherwise
     */
    public String getSelectedProjectNotationIndentifier();

    /**
     * Returns a list of tree paths of all projects opened in project navigation tree.
     *
     * @return the list of tree paths of of all projects opened in project navigation tree,
     */
    public List<TreePath> getProjectPaths();

    /**
     * Returns a list of tree roots (instances of ProjectRoot class) of all projects opened in project navigation tree.
     *
     * @return the list of tree roots (instances of ProjectRoot class) of all projects opened in
     * project navigation tree
     */
    public List<ProjectRoot> getProjects();

    /**
     * Returns a project root (instance of ProjectRoot class) opened in the project navigation tree that is specified
     * as it's name.
     *
     * @param projectName is the name of required project (project root)
     *
     * @return project root if such a project with given name is opened in project navigation tree, null if there is no
     * such a project root
     */
    public ProjectRoot getProject(final String projectName);

    /**
     * Returns a tree node holding a project root (instance of DefaultMutableTreeNode class) opened in the
     * project navigation tree that is specified as it's name.
     *
     * @param projectName is the name of required project (project root)
     *
     * @return an instance of DefaultMutableTreeNode class holding the project root with the given name, null if there
     * is no such a project (and thereby node)
     */
    public DefaultMutableTreeNode getProjectTreeNode(final String projectName);

    /**
     * Returns a tree path to the project that is specified by the given project name.
     *
     * @param projectName is the name of required project
     *
     * @return tree path to the project that is specified by the given project name, null if there is no such
     * a project
     */
    public TreePath getProjectPath(final String projectName);

    /**
     * Checks whether there is such a project with given name opened in project navigation tree.
     *
     * @param projectName is the project name
     *
     * @return true if there is such a project with given name opened in project navigation tree, false otherwise
     */
    public boolean isOpenedProject(final String  projectName);

    /**
     * Returns the tree path of the node that is currently selected in the project navigation tree.
     *
     * @return the tree path of the node that is currently selected in the project navigation tree
     */
    public TreePath getSelectedTreePath();

    /**
     * Returns an observable object that holds the currently selected item in the project navigation tree.
     * This implements kind of Observer pattern.
     *
     * @see com.jgoodies.binding.value.ValueModel for more info about value model and how to listen changes
     * in project navigation tree
     *
     * @return an implementation of value model representing currently selected item in project navigation tree
     */
    public ValueModel getSelectedItem();

    /**
     * Returns the tree path of the currently selected project. This works event if any child of project node is just
     * selected.
     *
     * @return tree path to the node in project navigation tree that represents path to the project that is
     * currently selected or is selected any of this project's child
     */
    public TreePath getSelectedProjectPath();

    /**
     * Returns the project root of the currently selected project. This works event if any child of project node is just
     * selected.
     *
     * @return project  root that represents the project that is currently selected or is selected
     * any of this project's child  
     */
    public ProjectRoot getSelectedProject();

    /**
     * Returns tree path to the currently selected diagram in the project navigation tree.
     *
     * @return tree path to the currently selected diagram in the project navigation tree, null if there is no
     * diagram selected in the project navigation tree
     */
    public TreePath getSelectedDiagramPath();

    /**
     * Returns project diagram of the currently selected diagram in the project navigation tree.
     *
     * @return project diagram of the currently selected diagram in the project navigation tree, null if there is no
     * diagram selected in the project navigation tree
     */
    public ProjectDiagram getSelectedDiagram();

    /**
     * Registers given listener for the given project diagram.
     *
     * Method does NOT check whether the given project diagram is valid project diagram in the project navigation tree.
     * One is supposed to verify this (e.g. using ProjectServiceUtils) or it can be used for special purposes.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils
     *
     * @param projectDiagram is the project diagram dispatching changes
     * @param listener is the listener for event consuming
     */
    public void registerDiagramListener(final ProjectDiagram projectDiagram, final ProjectDiagramListener listener);

    /**
     * Un-registers given listener from the given project diagram.
     *
     * @param projectDiagram is the project diagram dispatching changes
     * @param listener is the listener for event consuming
     */
    public void unRegisterDiagramListener(final ProjectDiagram projectDiagram, final ProjectDiagramListener listener);

    /**
     * Un-registers given listener from all project diagram.
     *
     * @param listener is the listener to be unregistered
     */
    public void unRegisterListener(final ProjectDiagramListener listener);    

}
