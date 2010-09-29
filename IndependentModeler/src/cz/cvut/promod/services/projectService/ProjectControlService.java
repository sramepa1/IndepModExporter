package cz.cvut.promod.services.projectService;

import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.listener.ProjectDiagramListener;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.results.LoadProjectResult;
import cz.cvut.promod.services.projectService.results.SaveProjectResult;

import javax.swing.tree.*;
import java.util.List;
import java.io.File;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:48:28, 10.10.2009
 */
public interface ProjectControlService extends ProjectService{

    /**
     * Returns the model used by the project navigation tree.
     *
     * One is supposed to avoid using this model. Use other methods to changes tree state!
     *
     * @return project navigation tree model
     */
    public DefaultTreeModel getProjectTreeModel();

    /**
     * Set the required item in the project navigation tree.
     *
     * @param treePath is the tree path to be selected in the project navigation tree
     */
    public void setSelectedItem(final TreePath treePath);

    /**
     * Adds new project item to the project navigation tree. Do not store anything on the file system (for this use
     * synchronization).
     *
     * Be careful that project name can have any project name (not null or empty), but some illegal
     * names wont be possible to save to the file system during synchronization.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils to check correctness of name
     *
     * @param projectRoot is the project root of the project that is supposed to be added to the project navigation
     * tree. The display name cannot be null, an empty string and cannot contain any disallowed symbols.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more info about disallowed symbols
     *
     * @param select if true, the newly added project item (project) will be selected in project tree navigation
     *
     * @return an object of AddProjectItemResult representing result of the operation
     *
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemResult for more detail info about the result
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemStatus for more details about relevant error
     * message 
     */
    public AddProjectItemResult addProject(final ProjectRoot projectRoot, final boolean select);


    /**
     * Adds a new subfolder with given name to the required tree path in the project navigation tree.
     * The required tree path has to be a valid tree in the project navigation tree and the last path
     * component is supposed to be a project container (project root or a subfolder). One can never add 
     * subfolder under a diagram node.
     *
     * @param subFolderName is the name for the new subfolder, cannot by nullary or an empty string. It cannot contains
     * any disallowed symbols as well.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about disallowed symbols
     *
     * @param parentTreePath is the tree path to the parent node, under that is the the new subfolder supposed to
     * be added. Has to be valid.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about path validity
     *
     * @param select if true, the newly added subfolder node will be selected in project tree navigation
     *
     * @return an object of AddProjectItemResult representing result of the operation
     *
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemResult for more detail info about the result
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemStatus for more details about relevant error
     * message
     */
    public AddProjectItemResult addSubFolder(final String subFolderName,
                                             final TreePath parentTreePath,
                                             final boolean select);

    /**
     * Adds a new subfolder with given name to the tree path of the closest project container in the project
     * navigation tree.
     *
     * @param subFolderName is the name for the new subfolder, cannot by nullary or an empty string. It cannot contains
     * any disallowed symbols as well.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about disallowed symbols
     *
     * @param select if true, the newly added subfolder node will be selected in project tree navigation
     *
     * @return an object of AddProjectItemResult representing result of the operation
     *
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemResult for more detail info about the result
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemStatus for more details about relevant error
     * message
     */
    public AddProjectItemResult addSubFolder(final String subFolderName, final boolean select);
    
    /**
     * Adds a new project diagram under specified parent node to the project navigation tree. One cannot save
     * 2 diagrams with the same name and the same notation under one parent node.
     *
     * Note: It is possible to insert more than just one diagram having the same name under one parent node
     * in the project navigation tree, but each of these diagrams has to be of a different notation
     *
     * @param projectDiagram is the project diagram that is supposed to be inserted to the project navigation tree. The
     * name cannot contain any disallowed symbols.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about disallowed symbols
     *
     * @param parentTreePath is the path to the parent node, under that is the project diagram supposed to be inserted.
     * The tree path has to be valid and the last path component has to be a project container (project root or
     * subfolder)
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about path validity
     *
     * @param select if true, the newly added diagram node will be selected in project tree navigation
     *
     * @return an object of AddProjectItemResult representing result of the operation
     *
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemResult for more detail info about the result
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemStatus for more details about relevant error
     * message
     */
    public AddProjectItemResult addDiagram(final ProjectDiagram projectDiagram,
                                           final TreePath parentTreePath,
                                           final boolean select);

    /**
     * Adds a new project diagram to the tree path of the closest project container in the project
     * navigation tree.
     *
     * Note: It is possible to insert more than just one diagram having the same name under one parent node
     * in the project navigation tree, but each of these diagrams has to be of a different notation
     *
     * @param projectDiagram is the project diagram that is supposed to be inserted to the project navigation tree. The
     * name cannot contain any disallowed symbols.
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about disallowed symbols
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about path validity
     *
     * @param select if true, the newly added diagram node will be selected in project tree navigation
     *
     * @return an object of AddProjectItemResult representing result of the operation
     *
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemResult for more detail info about the result
     * @see cz.cvut.promod.services.projectService.results.AddProjectItemStatus for more details about relevant error
     * message
     */
    public AddProjectItemResult addDiagram(final ProjectDiagram projectDiagram,
                                            final boolean select);

    /**
     * Removes project item specified by the argument from the project navigation tree.
     *
     * If the project item that is supposed to be deleted is currently selected or it's any child,
     * then the parent of this item will selected automatically.
     *
     * If the item to be deleted is or contains some project diagrams, then REMOVE event of this items
     * is automatically dispatched.
     *
     * The project diagram is automatically removed.
     *
     * @param treePath specifies the project item, that is supposed to be removed
     *
     * @return file system path to the item, that was deleted, null if any error occurs
     */
    public String removeProjectItem(final TreePath treePath);

    /**
     * Synchronization from the project navigation tree to the file system.
     *
     * It is possible to run this sync only from EventDispatcherThread.
     *
     * @param treePath is the tree path to the root of the sub-tree that is supposed to be synchronized.
     * If the last path component's user's object is any instance if ProjectDiagram, the synchronization is
     * performed only on this diagram. If the last path component's user object is any implementation of
     * ProjectContainer interface than this node is taken as the root of the sub-tree where will be
     * synchronization performed.
     *
     * @param addProjectItems indicates whether new items are supposed to be created on the file system if there are missing
     * @param overwriteProjectItems indicates whether diagrams that already exists on the file system are supposed to be overwritten
     * @param deleteProjectItems indicates whether no project items (files or diagrams) are supposed to be deleted from file system
     * one should be very careful when using this flag, because all deletion are un-doable.
     * @param cancelable if true, the the synchronization can be canceled by the user
     * @return true if no error has occurred during synchronization, false otherwise
     */
    public boolean synchronize(final TreePath treePath,
                               final boolean addProjectItems,
                               final boolean overwriteProjectItems,
                               final boolean deleteProjectItems,
                               final boolean cancelable);

    /**
     * Synchronization from the file system to the project navigation.
     *
     * It is possible to run this sync only from EventDispatcherThread.
     *
     * @param projectFile is the actual project file of the project, the placement of the project file is the
     * file system location from where the synchronization starts (can be specify by the path offset)
     * @param pathOffset is the specification of a particular directory extending the project file location from where
     * the synchronization supposed to start. E.g. the project file location is D:\Project\MyProject.pmp. The path
     * offset is overview/diagrams. So the synchronization will start from the location D:\Project\overview\diagrams.
     * @param addProjectItems indicates whether new items are supposed to be created in the project navigation if there are missing
     * @param overwriteProjectItems indicates whether diagrams that already exists in the project navigation are supposed to be overwritten
     * @param deleteProjectItems indicates whether no project items are supposed to be deleted from the project navigation during synchronization
     * @param cancelable if true, the the synchronization can be canceled by the user
     * @return true if no error has occurred during synchronization, false otherwise
     */
    public boolean synchronize(final File projectFile,
                               final List<String> pathOffset,
                               final boolean addProjectItems,
                               final boolean overwriteProjectItems,
                               final boolean deleteProjectItems,
                               final boolean cancelable);

    /**
     * Save the project root represented by the tree path to the project root.
     * This method automatically overwrites the old file, if any exists. One should always use
     * something like new File(<path to project file>).exists() to be sure. If the required
     * directory structure doesn't exist then this structure is automatically created for you.
     *
     * Note, that this method uses a static method of ProjectFileSave class
     *
     * @see cz.cvut.promod.services.projectService.localIO.ProjectFileSaver to save the project with formatting
     * enabled. One can use this static method straight instead of using this method. Then, you can let the
     * saver omit the formatting to lower RAM consumption and increase performance.
     *
     * @param projectTreePath is the tree path to the project root in project navigation tree. The tree path has to be
     * valid and tha last path component has to be a valid project root
     *
     * @see cz.cvut.promod.services.projectService.utils.ProjectServiceUtils for more details about tree path validity 
     *
     * @return a value defined by the SaveProjectResult enumeration
     *
     * @see cz.cvut.promod.services.projectService.results.SaveProjectResult for more details about return values
     * and it's conditions 
     */
    public SaveProjectResult saveProject(final TreePath projectTreePath);


    /**
     * Loads the project diagram and returns and object of LoadProjectResult class.
     *
     * @see cz.cvut.promod.services.projectService.results.LoadProjectResult
     *
     * @param projectFile is the file specifynig the ProMod project file
     *
     * @return an object of LoadProjectResult class holding the project root and other info
     */
    public LoadProjectResult loadProject(final File projectFile);

    /**
     * Switch one project diagram for another. All listeners that have been registered with the old project diagram
     * will be registered with the new diagram diagram.
     *
     * If any of the project diagrams is null then this method will have no effect.
     *
     * @param oldProjectDiagram is the old project diagram
     * @param newProjectDiagram is the new project diagram
     */
    public void switchChangePublisher(final ProjectDiagram oldProjectDiagram, final ProjectDiagram newProjectDiagram);

    /**
     * Switch one project diagram listener for another.
     *
     * If any of the project diagram listeners is null then this method will have no effect.
     *
     * @param oldProjectDiagramListener is the old project diagram listener
     * @param newProjectDiagramListener is the new project diagram listener
     */
    public void switchChangeListener(final ProjectDiagramListener oldProjectDiagramListener, final ProjectDiagramListener newProjectDiagramListener);    
    
}
