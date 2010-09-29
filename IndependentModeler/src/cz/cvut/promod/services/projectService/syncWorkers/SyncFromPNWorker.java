package cz.cvut.promod.services.projectService.syncWorkers;

import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.projectService.results.SaveProjectResult;
import cz.cvut.promod.services.projectService.dialogs.SyncDialog;
import cz.cvut.promod.services.projectService.treeProjectNode.*;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:10:13, 13.11.2009
 */

/**
 * Class that performs synchronization in a direction from project navigation tree to the file system.
 * This class extends SwingWorker class, so the synchronization is performed asynchronously to the Event Dispatcher Thread.
 * Process of synchronization is NOT thread safety. It is highly recommended to do NOT use this class directly, but use
 * ModelerSession.getProjectControlService().synchronize(...) method instead. If you use this synchronize(...) method,
 * you don't have to worry about concurrency issues, because this method shows a modal dialog displaying synchronization
 * errors and makes the Event Dispatcher Thread wait for the result of the synchronize() method. So, it is not possible to
 * invoke other methods on Event Dispatcher Thread during synchronization. 
 */
public class SyncFromPNWorker extends SwingWorker<Boolean, Void> {

    private final Logger LOG = Logger.getLogger(SyncFromPNWorker.class);

    private static final String IMPOSSIBLE_TO_SAVE_PROJECT_FILE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.project.file.error");
    private static final String PROJECT_ROOT_NOT_FOUND_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.project.root.error");
    private static final String INVALID_TREE_PATH_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.invalid.treepath");
    private static final String MISSING_PROJECT_FILE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.missing.project.file");
    private static final String SYNC_SUBTREE_BUILD_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.subtree.build.error");
    private static final String INVALID_PARENT_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.invalid.parent");
    private static final String DIAGRAM_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.error.diagram");
    private static final String NOTATION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.error.identifier");
    private static final String DIAGRAM_OVERWRITE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.overwritten");
    private static final String DIAGRAM_WRITE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.write");
    private static final String MKDIR_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.mkdir.error");
    private static final String DELETE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.delete.error");

    private final TreePath treePath;

    private final boolean addProjectItems;
    private final boolean overwriteProjectItems;
    private final boolean deleteProjectItems;

    private final SyncDialog syncDialog;

    public SyncFromPNWorker(final TreePath treePath,
                            final boolean addProjectItems,
                            final boolean overwriteProjectItems,
                            final boolean deleteProjectItems,
                            final SyncDialog syncDialog){

        this.treePath = treePath;

        this.addProjectItems = addProjectItems;
        this.overwriteProjectItems = overwriteProjectItems;
        this.deleteProjectItems = deleteProjectItems;

        this.syncDialog = syncDialog;
    }

    /**
     * Prepares the synchronization process and starts the synchronization.
     *
     * @return true if there were no errors during synchronization process, false otherwise
     *
     * @throws Exception when something happened, synchronize(...) (see above) method deals with possible problems
     */
    protected Boolean doInBackground() throws Exception {
        showDialog();

        // First goes the JTree main projects root (root node, invisible for users) and then a project root node.
        if(!ProjectServiceUtils.isValidTreePath(treePath) || treePath.getPathCount() < 2){
            LOG.error("Invalid tree path.");
            syncDialog.appendErrorInfo(INVALID_TREE_PATH_LABEL, true);
            return false;
        }

        final DefaultMutableTreeNode projectRootNode;
        final ProjectRoot projectRoot;
        try{
            projectRootNode = (DefaultMutableTreeNode) treePath.getPathComponent(1);
            projectRoot = (ProjectRoot) projectRootNode.getUserObject();
        } catch (ClassCastException exception){
            // should never happened, because isValidTreePath() method ensures this; testing & debuging purposes
            LOG.error("Project root not found.");
            syncDialog.appendErrorInfo(PROJECT_ROOT_NOT_FOUND_LABEL, true);
            return false;
        }

        final File projectRootFile =
                new File(projectRoot.getProjectLocation(), projectRoot.getDisplayName() + ProjectService.PROJECT_FILE_EXTENSION);

        if(projectRootFile.exists() && overwriteProjectItems &&
                /* overwrite project file only when the project root is set as tree path (constructor argument) */
                treePath.equals(ModelerSession.getProjectService().getProjectPath(projectRoot.getDisplayName()))){

            final SaveProjectResult result = ModelerSession.getProjectControlService().saveProject(treePath); //overwrite the project file
            if(!SaveProjectResult.SUCCESS.equals(result)){
                LOG.error("Not possible to save project file");
                syncDialog.appendErrorInfo(IMPOSSIBLE_TO_SAVE_PROJECT_FILE_LABEL, true);
                return false;
            }

        } else if(!projectRootFile.exists() && addProjectItems){
            final SaveProjectResult result = ModelerSession.getProjectControlService().saveProject(treePath); //create the project file
            if(!SaveProjectResult.SUCCESS.equals(result)){
                LOG.error("Not possible to save project file");
                syncDialog.appendErrorInfo(IMPOSSIBLE_TO_SAVE_PROJECT_FILE_LABEL, true);
                return false;
            }

        } else if(!projectRootFile.exists()){
            LOG.error("Project file not found and synchronization is not allowed to add items to the file system.");
            syncDialog.appendErrorInfo(MISSING_PROJECT_FILE_LABEL, true);
            return false;
        }

        final File path = syncPathToTreeRootToFS(
            new File(projectRootFile.getParent()),
                treePath,
                addProjectItems
        );

        if (path == null){
            syncDialog.appendErrorInfo(SYNC_SUBTREE_BUILD_ERROR_LABEL, true);
            return false;
        }

        /* This is kind of hack to show, that there is no need to continue,
        there are still differences between FS and PN, but the method is not allowed to create FS items. */
        return path.getAbsolutePath().equals("") || syncSubtreeToFS(
                path,
                treePath,
                addProjectItems, overwriteProjectItems, deleteProjectItems);

    }

    /**
     *  Schedules JDialog#setVisible(true) invocation on the Event Dispatcher Thread.
     */
    private void showDialog(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                syncDialog.setVisible(true);
            }
        });
    }

    /**
     * Synchronize the file system structure before the subtree that is supposed to be synchronized. This structure to the
     * subtree node is created if and only if the addProjectItems is true. Otherwise no items on the file system are created and
     * the whole synchronization finishes successfully, even though that there is still differences between file system and
     * project navigation.
     *
     * @param file is the project root directory
     *
     * @param treePath is the path to the project tree subtree which is supposed to be synchronized
     * 
     * @param addProjectItems indicates whether new items are supposed to be created on the file system if they are missing
     *
     * @return the file system path related to the subtree chosen to synchronize, null if any error occurs or an instance
     * of file class with empty path in it, if there are still differences but the method is not allowed to create items on the
     * file system
     *
     * @throws InterruptedException user has canceled the synchronization
     */
    private File syncPathToTreeRootToFS(final File file,
                                        final TreePath treePath,
                                        final boolean addProjectItems) throws InterruptedException{
        File path = file;

        for(int i = 1; i < treePath.getPathCount(); i++){
            checkInterruption();

            final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getPathComponent(i);
            final Object userObject = defaultMutableTreeNode.getUserObject();

            // update sync dialog
            if(userObject instanceof ProjectItem){
                syncDialog.updatePosition(((ProjectItem)userObject).getDisplayName());
                
            } else {
                // should never happened
                LOG.error("All project items have to be instances of classes implementing ProjectItem interface.");
                return null;
            }

            if(userObject instanceof ProjectRoot){
                if(i > 1){ // should never happened
                    return null;
                }

            } else if(userObject instanceof ProjectDiagram){
                if(i < 2){ // should never happened
                    return null;
                }

                if(defaultMutableTreeNode == treePath.getLastPathComponent()){
                    return path;
                } else {
                    return null; // diagram cannot have any children in the tree path
                }

            } else if(userObject instanceof ProjectSubFolder){
                if(i < 2){ // should never happened
                    return null;
                }

                final ProjectItem projectItem = (ProjectItem) userObject;
                final File subfolder = new File(path.getAbsolutePath(), projectItem.getDisplayName());

                if(!subfolder.exists()){

                    if(addProjectItems){
                        if(subfolder.mkdir()){
                           LOG.debug("Sync PN -> FS: new subfolder has been created, " + subfolder.getAbsolutePath() + ".");
                        } else {
                            LOG.error("Sync PN -> FS: Creating of a new subfolder has failed failed, " + subfolder.getAbsolutePath() + ".");
                            return null;
                        }
                    } else { // there are differences between PN and FS, but synchronize method is not invoked with addProjectItems flag
                        return new File("");
                    }
                }

                path = new File(subfolder.getAbsolutePath());

            } else {
                // should never happened
               LOG.error("Project node has to be an instance of ProjectRoot, ProjectSubFolder or Project Diagram class.");
            }
        }

        return path;
    }

    /**
     * Synchronize the selected project subtree to the file system. This methods requires the correct starting level
     * on the file system with respect to the project subtree to exist.
     *
     * Important to note that this method synchronize only the SUBTREE 'under' the last tree node in the treePath variable.
     *
     * @param fsLevel is the starting point for the synchronization on the file system
     * @param treePath is the starting point in the project structure for the synchronization
     * @param addProjectItems indicates whether new items are supposed to be created on the file system if they are missing
     * @param overwriteProjectItems indicates whether diagrams that already exists on the file system are supposed to be overwritten
     * @param deleteProjectItems indicates whether not projects items (files or diagrams) are supposed to be deleted from file system
     *
     * @return true if and only if no error occurs, false otherwise
     *
     * @throws InterruptedException when user cancels the synchronization
     */
    private boolean syncSubtreeToFS(final File fsLevel,
                                    final TreePath treePath,
                                    final boolean addProjectItems,
                                    final boolean overwriteProjectItems,
                                    final boolean deleteProjectItems) throws InterruptedException{

        checkInterruption();

        boolean retValue = true;

        final DefaultMutableTreeNode lastTreePathNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        final Object userObject = lastTreePathNode.getUserObject();
        final ProjectItem projectItem;

        projectItem = (ProjectItem) userObject;

        // sync a project diagram
        if(userObject instanceof ProjectDiagram){
            final ProjectDiagram projectDiagram = (ProjectDiagram) userObject;
            final String fileExtension = ProjectServiceUtils.getNotationFileExtension(projectDiagram.getNotationIdentifier());

            final File projectItemFile = new File(fsLevel.getAbsolutePath(), projectItem.getDisplayName() + fileExtension);

            NotationLocalIOController.SaveResult successSaveResult = NotationLocalIOController.SaveResult.SUCCESS;

            if(projectItemFile.exists() && overwriteProjectItems){
                successSaveResult = ModelerSession.getNotationService().getNotation(projectDiagram.getNotationIdentifier()).
                        getLocalIOController().saveProjectDiagram(projectDiagram, projectItemFile.getParent(), false);

                if(successSaveResult == NotationLocalIOController.SaveResult.SUCCESS){
                    LOG.debug("Sync PN -> FS: Diagram " + projectItemFile.getAbsolutePath() + " has been overwritten.");

                } else {
                    publicOverwriteError(projectDiagram, projectItemFile);
                }

            } else if(!projectItemFile.exists() && addProjectItems){
                successSaveResult = ModelerSession.getNotationService().getNotation(projectDiagram.getNotationIdentifier()).
                        getLocalIOController().saveProjectDiagram(projectDiagram, projectItemFile.getParent(), false);

                if(successSaveResult == NotationLocalIOController.SaveResult.SUCCESS){
                    LOG.debug("Sync PN -> FS: Diagram " + projectItemFile.getAbsolutePath() + " has been saved.");
                } else {
                    publicAddDiagramError(projectDiagram, projectItemFile);
                }
            }

            if(successSaveResult != NotationLocalIOController.SaveResult.SUCCESS){
                retValue = false;
            }

        // user object is project root or a subfolder
        } else if(userObject instanceof ProjectContainer){
            final Set<String> synchronizedItems = new HashSet<String>();

            if(userObject instanceof ProjectRoot){
                //add project file (.pmp) to the list of synchronizedItems = do not delete it
                synchronizedItems.add((((ProjectItem)userObject)).getDisplayName() + ProjectService.PROJECT_FILE_EXTENSION);
            }

            for(int i = 0; i < lastTreePathNode.getChildCount(); i++){
                checkInterruption();

                final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) lastTreePathNode.getChildAt(i);
                final ProjectItem projectItemOfChildNode = (ProjectItem) defaultMutableTreeNode.getUserObject();

                final File projectItemFile;
                if(projectItemOfChildNode instanceof ProjectContainer){
                    // project containers are without file extension
                    projectItemFile = new File(fsLevel.getAbsolutePath(), projectItemOfChildNode.getDisplayName());
                } else if(projectItemOfChildNode instanceof ProjectDiagram){
                    // project diagram files do have a file extension
                    final String fileExtension = ProjectServiceUtils.getNotationFileExtension(((ProjectDiagram) projectItemOfChildNode).getNotationIdentifier());
                    projectItemFile = new File(fsLevel.getAbsolutePath(), projectItemOfChildNode.getDisplayName() + fileExtension);
                } else {
                    LOG.error("A project item has to be either project container or project diagram.");
                    syncDialog.appendErrorInfo(INVALID_PARENT_ERROR_LABEL, true);
                    return false;
                }

                if(projectItemFile.exists()){
                    if(projectItemOfChildNode instanceof ProjectDiagram){
                        retValue &= syncSubtreeToFS(
                                                projectItemFile.getParentFile(),
                                                treePath.pathByAddingChild(defaultMutableTreeNode),
                                                addProjectItems, overwriteProjectItems, deleteProjectItems);

                    } else { /* next child is container */
                        retValue &= syncSubtreeToFS(
                                                projectItemFile,
                                                treePath.pathByAddingChild(defaultMutableTreeNode),
                                                addProjectItems, overwriteProjectItems, deleteProjectItems);
                    }

                    synchronizedItems.add(projectItemFile.getName());

                } else if(!projectItemFile.exists() && addProjectItems){

                    /* next child is diagram */
                    if(projectItemOfChildNode instanceof ProjectDiagram){

                        final String fileExtension = ProjectServiceUtils.getNotationFileExtension(((ProjectDiagram) projectItemOfChildNode).getNotationIdentifier());

                        retValue &= syncSubtreeToFS(
                                                projectItemFile.getParentFile(),
                                                treePath.pathByAddingChild(defaultMutableTreeNode),
                                                addProjectItems, overwriteProjectItems, deleteProjectItems);

                        synchronizedItems.add(projectItemOfChildNode.getDisplayName() + fileExtension);

                    } else { /* next child is container */

                        if(projectItemFile.mkdir()){ //make new subfolder
                            LOG.debug("Sync PN -> FS: new subfolder has been created, " + projectItemFile.getAbsolutePath() + ".");

                            retValue &= syncSubtreeToFS(
                                                    projectItemFile,
                                                    treePath.pathByAddingChild(defaultMutableTreeNode),
                                                    addProjectItems, overwriteProjectItems, deleteProjectItems);

                            synchronizedItems.add(projectItemOfChildNode.getDisplayName());

                        } else {
                            publicMkdirError(projectItemFile);
                        }
                    }
                }
            }

            // delete non PN items in this fs level
            if(deleteProjectItems){
                final String[] fsLevelItems = fsLevel.list();

                for(final String fsItem : fsLevelItems){
                    if(!synchronizedItems.contains(fsItem)){ //delete not PN item from FS
                        final File itemToDelete = new File(fsLevel.getAbsolutePath(), fsItem);

                        if(ProjectServiceUtils.removeDirectory(itemToDelete)){
                            LOG.debug("File system item has been deleted, " + itemToDelete.getAbsolutePath());

                        } else {
                            publicDeleteError(itemToDelete);
                            retValue = false;
                        }
                    }
                }
            }
        }

        return  retValue;
    }

    /**
     * done() method is invoked on the Event Dispatcher Thread when the doInBackground() method finishes
     */
    @Override
    protected void done() {
        syncDialog.disableCancelButton();

        syncDialog.updatePosition(
                ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.searching.done")
        );

        try {
            if(!isCancelled()){
                syncDialog.syncComplete(!get());

                return;
            }

        } catch (InterruptedException e) {
            LOG.error("The synchronization from the project navigation has been interrupted.", e);
        } catch (ExecutionException e) {
            LOG.error("There is no return value available.", e);
        }

        syncDialog.syncComplete(true);
    }

    /**
     * Checks whether the task has not been canceled for example by the user.
     *
     * @throws InterruptedException when user cancels the synchronization
     */
    private void checkInterruption() throws InterruptedException{
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    /**
     * Publishes file system item overwrite error.
     *
     * @param projectDiagram is the diagram that is supposed to be overwritten
     *
     * @param projectItemFile is the file path to the existing file of diagram
     */
    private void publicOverwriteError(final ProjectDiagram projectDiagram, final File projectItemFile){
        syncDialog.appendErrorInfo(DIAGRAM_LABEL + " " + projectDiagram.getDisplayName(), false);
        syncDialog.appendErrorInfo(NOTATION_LABEL + " " + projectDiagram.getNotationIdentifier(), false);
        syncDialog.appendErrorInfo(DIAGRAM_OVERWRITE_ERROR_LABEL, false);
        syncDialog.appendErrorInfo(" >> " + projectItemFile.getAbsolutePath(), true);
        LOG.error("Sync PN -> FS: Diagram " + projectItemFile.getAbsolutePath() + " was NOT possible to overwrite.");
    }

    /**
     * Publishes file system item write error.
     *
     * @param projectDiagram is the diagram that is supposed to be written
     *
     * @param projectItemFile is the file path to the not existing file of diagram
     */
    private void publicAddDiagramError(final ProjectDiagram projectDiagram, final File projectItemFile){
        syncDialog.appendErrorInfo(DIAGRAM_LABEL + " " + projectDiagram.getDisplayName(), false);
        syncDialog.appendErrorInfo(NOTATION_LABEL + " " + projectDiagram.getNotationIdentifier(), false);
        syncDialog.appendErrorInfo(DIAGRAM_WRITE_ERROR_LABEL, false);
        syncDialog.appendErrorInfo(" >> " + projectItemFile.getAbsolutePath(), true);

        LOG.error("Sync PN -> FS: Diagram " + projectItemFile.getAbsolutePath() + " has NOT been saved.");
    }

    /**
     * Publishes the make dir error.
     *
     * @param projectItemFile is the path to the dir
     */
    private void publicMkdirError(final File projectItemFile) {
        syncDialog.appendErrorInfo(MKDIR_ERROR_LABEL, false);
        syncDialog.appendErrorInfo("  >>  " +projectItemFile.getAbsolutePath(), true);

        LOG.error("Sync PN -> FS: Creating of a new subfolder has failed, " + projectItemFile.getAbsolutePath() + ".");
    }

    /**
     * Publishes a delete error.
     *
     * @param itemToDelete file system to be deleted
     */
    private void publicDeleteError(final File itemToDelete) {
        syncDialog.appendErrorInfo(DELETE_ERROR_LABEL, false);
        syncDialog.appendErrorInfo("  >> " + itemToDelete.getAbsolutePath(), true) ;

        LOG.error("Sync PN -> FS: Deletion of a file system item has failed, " + itemToDelete.getAbsolutePath() + ".");
    }

}
