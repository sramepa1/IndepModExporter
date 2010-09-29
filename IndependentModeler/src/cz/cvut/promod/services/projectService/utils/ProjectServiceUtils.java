package cz.cvut.promod.services.projectService.utils;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.*;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;

import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:32:38, 12.11.2009
 */

/**
 * Methods completing the ProjectService and ProjectControlService services.
 *
 * Use this methods when not required functionality is in the ProjectService or ProjectControlService service.  
 */
public class ProjectServiceUtils {

    private static final Logger LOG = Logger.getLogger(ProjectServiceUtils.class);

    /* one can never give a project, diagram or subfolder name containing any of this chars */
    public static final char[] DISALLOWES_FILE_NAME_SYMBOLS = {'\\', '/', '?', '%', '*', ':', '|', '"', '<', '>', '.'};

    /**
     * Gives the file extension of the file name specified by the parameter. 
     *
     * @param fileName full file name
     * @return extension of this file
     */
    public static String getFileExtension(final String fileName){
        if(fileName == null){
            return null;
        }

        final int index = fileName.lastIndexOf(ProjectService.FILE_EXTENSION_DELIMITER);

        if(index < 0){
            return null;
        }

        return fileName.substring(index + 1, fileName.length());
    }


    /**
     * Returns the file extension preceding by the '.' symbol that has been assigned to the notation
     *
     * @param notationIdentifier is the notation identifier
     * @return symbol '.' and the file extension associated with the notation specified by the notation identifier
     */
    public static String getNotationFileExtension(final String notationIdentifier) {
        return ProjectService.FILE_EXTENSION_DELIMITER + ModelerSession.getNotationService().getNotation(notationIdentifier).getLocalIOController().getNotationFileExtension();
    }


    /**
     * Check whether file with given name ends with any notation file extension.
     *
     * @param fileName is a file name with it's extension
     * @return true if there is a notation that saves/loads files with files extension
     */
    public static boolean hasNotationFileExtension(final String fileName) {
        for(final Notation notation : ModelerSession.getNotationService().getNotations()){
            final String fileExtension = ProjectService.FILE_EXTENSION_DELIMITER + notation.getLocalIOController().getNotationFileExtension();

            if(fileName.endsWith(fileExtension)){
                return true;
            }
        }

        return false;
    }


    /**
     * Removes any file or directory (even not empty) from the file system.
     *
     * @param path is path to the file or directory to be deleted
     *
     * @return true if the job has been done successfully, false otherwise
     */
    public static boolean removeDirectory(final File path) {
        if (path.isDirectory()) {

            final String[] dirChildren = path.list();

            for (final String dirChild : dirChildren) {
                if (!removeDirectory(new File(path, dirChild))) { //delete sub-directories
                    return false;
                }
            }
        }

        return path.delete();
    }


    /**
     * Checks if there is no duplicity in display names in one project tree level.
     *
     * @param parent defines the required level
     * @param displayName name of project item
     * @return true if there is no same display name in the level
     */
    public static boolean isUniqueProjectContainerDisplayName(final TreeNode parent, final String displayName) {
        for(int i = 0; i < parent.getChildCount(); i++){

            final ProjectItem projectItem = (ProjectItem) ((DefaultMutableTreeNode) parent.getChildAt(i)).getUserObject();

            if(projectItem.getDisplayName().equals(displayName) && projectItem instanceof ProjectContainer){
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether user's object stored in an instance of DefaultMutableTreeNode is an instance of
     * ProjectContainer class.
     *
     * @param parent is a instance of DefaultMutableTreeNode which is supposed to hold the mentioned user's object
     * @return true if and only if the user's object is an instance of class extending Project container interface
     */
    public static boolean isProjectContainer(final DefaultMutableTreeNode parent){
        final ProjectItem projectItem;

        try{
            projectItem = (ProjectItem) parent.getUserObject();
        } catch (ClassCastException exception){
            return false;
        }

        return projectItem != null && projectItem instanceof ProjectContainer;
    }


    /**
     * Checks if there is already no another diagram of the same name and notation under the tree parent.
     *
     * @param parent is common the tree parent
     * @param projectDiagram is the "new" project diagram that's name and notation identifier will be tested
     *
     * @return true if and only of there is not any diagram with the same name and same notation under the given tree parent yet
     */
    public static boolean isUniqueProjectDiagramName(final TreeNode parent, final ProjectDiagram projectDiagram) {
        for(int i = 0; i < parent.getChildCount(); i++){

            final Object userObject = ((DefaultMutableTreeNode) parent.getChildAt(i)).getUserObject();

            if( userObject instanceof ProjectDiagram){
                final ProjectDiagram existingDiagram = (ProjectDiagram) userObject;

                if((existingDiagram.getDisplayName().equals(projectDiagram.getDisplayName()))
                        && (existingDiagram.getNotationIdentifier().equals(projectDiagram.getNotationIdentifier()))){
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks whether the tree path provided as an argument is valid tree path accordingly to the tree model.
     *
     * @param treePath a tree path to check for validity
     * @return true if and only if the treePath is valid, false otherwise
     */
    public static boolean isValidTreePath(final TreePath treePath){
        final TreeModel treeModel = ModelerSession.getProjectControlService().getProjectTreeModel();
        final TreeNode rootNode = (TreeNode) treeModel.getRoot();

        if((treePath == null) || (rootNode != treePath.getPathComponent(0))){
            return false;
        }

        final int pathCount = treePath.getPathCount();

        for(int i = 1; i < pathCount; i++){
            final Object parent = treePath.getPathComponent(i - 1);
            final Object child = treePath.getPathComponent(i);

            final int index = treeModel.getIndexOfChild(parent, child);

            if(index < 0){
                return false;
            }

            final DefaultMutableTreeNode node;
            try{
                 node = (DefaultMutableTreeNode) child;
            } catch (ClassCastException exception){
                LOG.error("All project tree nodes have to be an instance of DefaultMutableTreeNode class.");
                return false;
            }

            final Object userObject = node.getUserObject();

            //second node has to represent a project root
            if(i == 1){
                if(!(userObject instanceof ProjectRoot)){
                    return false;
                }
            }

            if(userObject instanceof ProjectRoot){
                if(i != 1){
                    return false;
                }
            } else if(userObject instanceof ProjectSubFolder){
                // do nothing
            } else if(userObject instanceof ProjectDiagram){
                if(i != (pathCount - 1)){
                    // diagram node has no other children
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Looks for the tree path of a project diagram specified by the it's uuid. The search starts by the project node
     * specified by the treePath argument and continues recursively like DFS search.
     *
     * Due to performance reason this method doesn't check the given tree path for validity. Programmer should do
     * it on his/her own (using ProjectServiceUtils.isValidTreePath() method).
     *
     * @param treePath is the tree path of the node, where the search is supposed to start
     * @param uuid is the uuid of searched project diagram
     *
     * @return tree path to the project diagram with given uuid, null if no diagram was found
     */
    public static TreePath findProjectDiagram(final TreePath treePath, final UUID uuid) {

        final DefaultMutableTreeNode parentNode;
        try{
            parentNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        } catch (Exception e){
            LOG.error("All items in the project tree modelFactory have to be an instance of DefaultMutableTreeNode class or " +
                    "any class extending DefaultMutableTreeNode class.", e);
            return null;
        }

        if(parentNode.getUserObject() instanceof ProjectDiagram){
            if(((ProjectDiagram) parentNode.getUserObject()).getUuid().equals(uuid)){
                return treePath;
            }

        } else if(parentNode.getUserObject() instanceof ProjectContainer){

            // is not a ProjectDiagram but a ProjectContainer
            TreePath resultTreePath;

            for(int i = 0; i < parentNode.getChildCount(); i++){
                final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

                final TreePath childTreePath = treePath.pathByAddingChild(childNode);

                resultTreePath = findProjectDiagram(childTreePath, uuid);

                if(resultTreePath != null){
                    return resultTreePath;
                }
            }

        } else {
            LOG.error("Project's node user's object has to be an instance of ProjectDiagram or ProjectContainer interface.");
            return null;
        }

        return null;
    }

    /**
     * Finds tree path to the child node of a node specified by  parentTreePath which is one of method's arguments.
     * This child node has to have the same name and event the same notation. If the first method argument is a
     * directory, that method compares just display name and the directory name. If there is a real file specified by
     * the file argument, that this method compares display name of project item and name of the file without file
     * extension and project diagram notation and the notation that is specified by the file's extension.
     *
     * @param file that specifies the directory or project diagram of any notation
     * @param parentTreePath specifies the path to the parent node where the search is supposed to be performed
     * @return tree path of the existing project subfolder or project diagram if exists, null otherwise
     */
    public static TreePath findTreePath(final File file, final TreePath parentTreePath) {
        final String name = file.getName();

        try{
            final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();

            if(file.isFile() && ProjectServiceUtils.hasNotationFileExtension(name)){

                final String fileNameWithoutExt = name.substring(0, name.lastIndexOf(ProjectService.FILE_EXTENSION_DELIMITER));
                final String extension = name.substring(name.lastIndexOf(ProjectService.FILE_EXTENSION_DELIMITER)  + 1, name.length());
                final String notationIdentifier = ModelerSession.getNotationService().getNotationIdentifier(extension);

                if(notationIdentifier == null){ // there is no such a notation for this file extension
                    return null;
                }

                if(fileNameWithoutExt == null || fileNameWithoutExt.equals("")){
                    return null;
                }

                for(int i = 0; i < parentNode.getChildCount(); i++){
                    final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

                    final ProjectItem usersProjectItem = (ProjectItem) childNode.getUserObject();

                    if(usersProjectItem instanceof ProjectDiagram){
                        final ProjectDiagram projectDiagram = (ProjectDiagram) usersProjectItem;

                        if(projectDiagram.getDisplayName().equals(fileNameWithoutExt) && notationIdentifier.equals(projectDiagram.getNotationIdentifier())){
                            return parentTreePath.pathByAddingChild(childNode);
                        }

                    }
                }
            } else if (file.isDirectory()){
                for(int i = 0; i < parentNode.getChildCount(); i++){
                    final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

                    final ProjectItem usersProjectItem = (ProjectItem) childNode.getUserObject();

                    if(usersProjectItem instanceof ProjectContainer){
                        if(usersProjectItem.getDisplayName().equals(name))
                        return parentTreePath.pathByAddingChild(childNode);
                    }
                }
            }

        } catch(ClassCastException exception){
            LOG.error("An node in project tree modelFactory is not an instance of DefaultMutableTreeNode.");
        }

        return null;
    }


    /**
     * Creating a file system path of the file that's is related to the project item.
     * If a project root is specified by the tree path and the project file (.pmp) is
     * saved in C:\Project\myProject.pmp, then this method returns C:\Project. This
     * works in the same way for project subfolders and diagrams. Files related to the
     * project diagrams have an extension associated with a notation loaded.
     *
     * @param treePath to the project item, has to have at least 2 components
     *
     * @return a file system path, null if any error has occurred
     */
    public static String getFileSystemPathToProjectItem(final TreePath treePath){
        if(!isValidTreePath(treePath)){
            return null;
        }

        final StringBuffer fileSystemPath = new StringBuffer();

        try{
            ProjectRoot projectRoot = (ProjectRoot) ((DefaultMutableTreeNode)treePath.getPathComponent(1)).getUserObject();

            fileSystemPath.append(projectRoot.getProjectLocation());

            final int pathCount = treePath.getPathCount();

            for(int i = 2; i < pathCount; i++){
                final Object object = ((DefaultMutableTreeNode)treePath.getPathComponent(i)).getUserObject();

                if(object instanceof ProjectSubFolder){
                    final String name = ((ProjectSubFolder)object).getDisplayName();

                    if(name == null || name.equals("")){
                        return null;
                    }

                    fileSystemPath.append(System.getProperty("file.separator"));
                    fileSystemPath.append(name);

                } else if(object instanceof ProjectDiagram){
                    final ProjectDiagram projectDiagram = (ProjectDiagram)object;
                    final String name = projectDiagram.getDisplayName();
                    final String extension = getNotationFileExtension(projectDiagram.getNotationIdentifier());

                    if(name == null || name.equals("") || extension == null || extension.equals("")){
                        return null;
                    }

                    fileSystemPath.append(System.getProperty("file.separator"));
                    fileSystemPath.append(name).append(extension);

                } else {
                    LOG.error("Any project item after project root can be only project subfolder or a project diagram.");
                    return  null;
                }
            }

            return fileSystemPath.toString();

        } catch (Exception exception){
            LOG.error("An error during file system path sequence creation has occurred.", exception);
        }

        return null;
    }

    /**
     * @param node is an instance od DefaultMutableTreeNode
     * @return true if the node holds and instance of ProjectDiagram, false otherwise
     */
    public static boolean isProjectDiagram(final DefaultMutableTreeNode node){
        return node != null && isProjectDiagram(node.getUserObject());

    }

    /**
     * @param object an object
     * @return true if the object is an instance of ProjectDiagram
     */
    public static boolean isProjectDiagram(final Object object){
        return object instanceof ProjectDiagram;
    }


    /**
     * @param node is an instance od DefaultMutableTreeNode
     * @return true if the node holds and instance of ProjectSubFolder, false otherwise
     */
    public static boolean isProjectSubfolder(final DefaultMutableTreeNode node){
        return node != null && isProjectSubfolder(node.getUserObject());

    }

    /**
     * @param object an object
     * @return true if the object is an instance of ProjectSubFolder
     */
    public static boolean isProjectSubfolder(final Object object){
        return object instanceof ProjectSubFolder;
    }

    /**
     * @param node is an instance od DefaultMutableTreeNode
     * @return true if the node holds and instance of ProjectRoot , false otherwise
     */
    public static boolean isProjectRoot(final DefaultMutableTreeNode node){
        return node != null && isProjectRoot(node.getUserObject());

    }

    /**
     * @param object an object
     * @return true if the object is an instance of ProjectRoot
     */
    public static boolean isProjectRoot(final Object object){
        return object instanceof ProjectRoot;
    }


    /**
     * Deletes an item (file or directory) from file system which path is specified by the projectParentNodeTreePath variable. The path is build
     * from the project root file (absolut path) and then by adding subfolders names. All user's object in nodes in the
     * tree path has to be implementations of ProjectContainer interface, except the first one, which is the model root.
     * Finally the itemName variable is added to the end of the built path. And the item with the resulting path is deleted.
     *
     * @param projectParentNodeTreePath is a tree path to build the file system path the the item that is supposed to be deleted
     * @param itemName is a name of the item to be deleted (e.g. myfile.pmd, mydir, ...)

     * @return true on success, false otherwise
     */
    public static boolean deleteFSItem(final TreePath projectParentNodeTreePath, final String itemName) {
        if(!ProjectServiceUtils.isValidTreePath(projectParentNodeTreePath) || projectParentNodeTreePath.getPathCount() < 2){
            return false;
        }

        File file;
        try{
            ProjectRoot projectRoot = (ProjectRoot) ((DefaultMutableTreeNode) projectParentNodeTreePath.getPathComponent(1)).getUserObject();
            file = new File((projectRoot.getProjectLocation()));

            for(int i = 2; i < projectParentNodeTreePath.getPathCount(); i++){
                ProjectContainer projectContainer = (ProjectContainer) ((DefaultMutableTreeNode) projectParentNodeTreePath.getPathComponent(i)).getUserObject();

                file = new File(file.getAbsolutePath(), projectContainer.getDisplayName());
            }

        } catch (ClassCastException exception){
            LOG.error("Not valid tree path or even project structure. The path do the file system item is not possible to build.", exception);
            return false;
        }

        file = new File(file.getAbsolutePath(), itemName);

        return !file.exists() || ProjectServiceUtils.removeDirectory(file);
    }


    /**
     * Compares two tree paths and returns true if the path specified by the second method's parameter is an
     * extension of the path specified by the first argument of the method.
     *
     * The path specified by the second argument is checked for validity,
     * @see ProjectServiceUtils isValidTreePath(final TreePath treePath)
     *
     * Reference comparison is used.
     *
     * @param originalTreePath is the shorter path
     * @param extendedTreePath is the extended path
     *
     * @return true if the extended path is the same like the original path or it extends the original path and the
     *          extended path is valid tree path (with regard to the project tree model), false otherwise
     */
    public static boolean isTreePathExtension(final TreePath originalTreePath, final TreePath extendedTreePath){
        if(!isValidTreePath(extendedTreePath)){
            return false;
        }

        if(originalTreePath.getPathCount() > extendedTreePath.getPathCount()){
            return false;
        }

        for(int i = 0; i < originalTreePath.getPathCount(); i++){
            if(originalTreePath.getPathComponent(i) != extendedTreePath.getPathComponent(i)){
                return false;
            }
        }

        return true;

    }

    /**
     * Checks whether the name given as a method's argument doesn't contain any of disallowed symbols.
     * This should ensure that all project's, subfolder's and diagram's names used in ProMod will be then
     * possible to save on file system in main OSs (MS Windows, Linux, Solaris). 
     *
     * @param name to be checked
     * @return true if and only if the name given as argument doesn't contain any disallowed symbol, false otherwise
     */
    public static boolean isSyntacticallyCorrectName(final String name){
        if(name == null || name.isEmpty()){
            return false;
        }

        for(final char ch : DISALLOWES_FILE_NAME_SYMBOLS){
            if(name.indexOf(ch) > -1){
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the string of disallowed symbols as a string using specified delimiter, e.g. (delimiter is ',')
     * ?,:,|,\,/ etc.
     *
     * @param delimiter is the separator
     * @return the string of all disallowed symbols
     */
    public static String getDisallowedNameSymbols(final char delimiter){
        final StringBuffer symbols = new StringBuffer();

        final int symbolCount = DISALLOWES_FILE_NAME_SYMBOLS.length;
        for(int i = 0; i < symbolCount; i++){
            final char symbol = DISALLOWES_FILE_NAME_SYMBOLS[i];

            symbols.append(symbol);

            if(i != (symbolCount - 1)){
                symbols.append(delimiter);
            }
        }

        return symbols.toString();
    }

}
