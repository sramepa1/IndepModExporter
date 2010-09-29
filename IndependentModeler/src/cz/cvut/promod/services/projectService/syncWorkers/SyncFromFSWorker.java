package cz.cvut.promod.services.projectService.syncWorkers;

import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.dialogs.SyncDialog;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectItem;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectSubFolder;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:40:56, 13.11.2009
 */

/**
 * SyncFromFSWorker class is a Swing Worker performing synchronization from file system to project navigation.
 */
public class SyncFromFSWorker extends SwingWorker<Boolean, Void> {

    private final Logger LOG = Logger.getLogger(SyncFromFSWorker.class);

    private static final String LOAD_PROJECT_FILE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.error.load.project.file");
    private static final String INVALID_PROJECT_ROOT_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.error.invalid.project.root");
    private static final String SYNC_SUBTREE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.error.subtree");
    private static final String SUBTREE_ROOT_NOT_EXIST_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.error.subtree.not.exist");
    private static final String GENERAL_ERROR =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.default.error.message");
    private static final String DIAGRAM_LOAD_ERROR =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.load.error");
    private static final String NOTATION_IDENRIFIER =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.error.identifier");
    private static final String NOTATION_EXTENSION =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.diagram.error.extension");
    private static final String CANCELED_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.canceled");
    private static final String OFFSET_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.offset.error");
    private static final String DIRECTORY_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.directory.error");
    private static final String EXCEPTION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.exception");


    private final File projectFile;

    private final List<String> pathOffset;

    private final boolean addProjectItems;
    private final boolean overwriteProjectItems;
    private final boolean deleteProjectItems;

    private final SyncDialog syncDialog;

    public SyncFromFSWorker(final File projectFile,
                            final List<String> pathOffset,
                            final boolean addProjectItems,
                            final boolean overwriteProjectItems,
                            final boolean deleteProjectItems,
                            final SyncDialog syncDialog){

        this.projectFile = projectFile;
        this.pathOffset = pathOffset;

        this.addProjectItems = addProjectItems;
        this.overwriteProjectItems = overwriteProjectItems;
        this.deleteProjectItems = deleteProjectItems;

        this.syncDialog = syncDialog;
    }

    /**
     * Performs the synchronization.
     *
     * @return true if no error occurs, false otherwise
     */
    public Boolean doInBackground(){
        showDialog();

        try{
            final ProjectRoot projectRoot =
                    ModelerSession.getProjectControlService().loadProject(projectFile).getProjectRoot();

            if(projectRoot == null){
                syncDialog.appendErrorInfo(LOAD_PROJECT_FILE_ERROR_LABEL, true);
                return false;
            }

            // determines whether the project is loaded in project navigation
            TreePath loadedProjectTreePath = null; //tree path to that project

            for(final TreePath treePath : ModelerSession.getProjectService().getProjectPaths()){
                final ProjectRoot loadedProjectRoot;

                try{
                    loadedProjectRoot = (ProjectRoot) ((DefaultMutableTreeNode) treePath.getPathComponent(1)).getUserObject();
                } catch (ClassCastException exception){
                    LOG.error("Not valid project root in projects.");
                    syncDialog.appendErrorInfo(INVALID_PROJECT_ROOT_ERROR_LABEL, true);
                    return false;
                } catch (NullPointerException exception){
                    LOG.error("Not a project root in projects.");
                    syncDialog.appendErrorInfo(INVALID_PROJECT_ROOT_ERROR_LABEL, true);
                    return false;
                }

                if(projectRoot.getDisplayName().equals(loadedProjectRoot.getDisplayName())
                        && projectRoot.getProjectLocation().equals(loadedProjectRoot.getProjectLocation())){

                    loadedProjectTreePath = treePath;
                    break;
                }
            }

            if(loadedProjectTreePath == null){
                LOG.error("Not such a project in project navigation");
                syncDialog.appendErrorInfo(SYNC_SUBTREE_ERROR_LABEL, true);
                return false;
            }

            File fsSubtreeLocation = projectFile.getParentFile();

            if((pathOffset != null) && (pathOffset.size() != 0)){
                loadedProjectTreePath = syncPathWithOffset(
                        projectFile.getParent(),
                        pathOffset,
                        loadedProjectTreePath,
                        addProjectItems);

                if(loadedProjectTreePath == null){
                    LOG.error("Not such a project in project navigation");
                    syncDialog.appendErrorInfo(INVALID_PROJECT_ROOT_ERROR_LABEL, true);
                    return false;
                }

                File file = new File(projectFile.getParent());

                for(final String fsItem : pathOffset){
                    file = new File(file.getAbsolutePath(), fsItem);
                }

                fsSubtreeLocation = file;
            }

            return syncSubtreeToPN(
                    loadedProjectTreePath,
                    fsSubtreeLocation,
                    projectRoot,
                    addProjectItems, overwriteProjectItems, deleteProjectItems
            );

        } catch (InterruptedException exception){
            // sync has been interrupted by the user

            LOG.info("Synchronization has been interrupted by user.");

            syncDialog.appendErrorInfo(CANCELED_LABEL, true);

            return false;

        } catch (Exception exception){
            // this should never happened, debug + test purposes
            LOG.error("An exception has been thrown during synchronization", exception);
            syncDialog.appendErrorInfo(GENERAL_ERROR, false);
            syncDialog.appendErrorInfo(exception.getMessage(), true);

            return false;
        }
    }

    /**
     * Sync the path before the sync start point.
     *
     * @param parent file system project parent
     * @param pathOffset the offset to the sync starting point
     * @param loadedProjectTreePath given tree path the project navigation
     * @param addProjectItems flag, if true new items will be added
     * @return true if no error occurs, false otherwise
     * @throws InterruptedException when the process is interrupted by the user
     */
    private TreePath syncPathWithOffset(final String parent,
                                  final List<String> pathOffset,
                                  TreePath loadedProjectTreePath,
                                  final boolean addProjectItems) throws InterruptedException{

        File file = new File(parent);

        for(int i = 0; i < pathOffset.size(); i++){

            checkInterruption();

            final String item = pathOffset.get(i);

            file = new File(file.getAbsolutePath(), item);

            if(!file.exists()){
                syncDialog.appendErrorInfo(OFFSET_ERROR_LABEL, false);
                syncDialog.appendErrorInfo("   >> " + file.getAbsolutePath(), true);

                LOG.error("Illegal offset, file not exists: " + file.getAbsolutePath());

                return null;
            }

            if(file.isFile()){ // file (=diagram) can occupy only the last position in the offset
                if(i != pathOffset.size() - 1){
                    LOG.error("File item can only occupy the last position ofthe path offset.");
                    return null;
                }

                return loadedProjectTreePath;

            } else if(file.isDirectory()){
                final TreePath longerTreePath = ProjectServiceUtils.findTreePath(file, loadedProjectTreePath);

                if(longerTreePath == null){
                    if(addProjectItems){
                        AddProjectItemResult result =
                                ModelerSession.getProjectControlService().addSubFolder(file.getName(), loadedProjectTreePath, false);

                        if(result.getStatus() != AddProjectItemStatus.SUCCESS){
                            return null;
                        }

                        loadedProjectTreePath = ProjectServiceUtils.findTreePath(file, loadedProjectTreePath); // add new created node to the path

                    } else {
                       return null;
                    }
                } else {
                    loadedProjectTreePath = longerTreePath; // add next node to the path
                }

            } else {
                LOG.error("Item on the file system has not be identify. It has to be either a file or a directory.");
                return null;
            }
        }

        return loadedProjectTreePath;
    }

    /**
     * Sync the subfoler. Used in recursion.
     *
     * @param treePath current tree path
     * @param fsSubtreeLocation current file system location
     * @param projectRoot project root
     * @param addProjectItems if true new items will be added
     * @param overwriteProjectItems if true current items will be over-writtem
     * @param deleteProjectItems if true missing items will be deleted
     * @return true if no error occurs, false otherwise
     * @throws InterruptedException if the process is interrupted by the user
     */
    private boolean syncSubtreeToPN(final TreePath treePath,
                                    final File fsSubtreeLocation,
                                    final ProjectRoot projectRoot,
                                    final boolean addProjectItems,
                                    final boolean overwriteProjectItems,
                                    final boolean deleteProjectItems) throws InterruptedException{

        checkInterruption();

        if(!fsSubtreeLocation.exists()){
            syncDialog.appendErrorInfo(SUBTREE_ROOT_NOT_EXIST_ERROR_LABEL, true);
            return false;
        }

        syncDialog.updatePosition(SyncWorkersUtils.getMessageString(fsSubtreeLocation));

        boolean retValue = true;

        final String name = fsSubtreeLocation.getName();

        if(fsSubtreeLocation.isFile() && ProjectServiceUtils.hasNotationFileExtension(name)){
            // diagram file

            final TreePath childTreePath = ProjectServiceUtils.findTreePath(fsSubtreeLocation, treePath);
            final String extension = ProjectServiceUtils.getFileExtension(name);
            final String notationIdentifier = ModelerSession.getNotationService().getNotationIdentifier(extension);

            if(childTreePath == null && addProjectItems){ //add a new diagram
                final ProjectDiagram projectDiagram;
                try{
                    projectDiagram = ModelerSession.getNotationService().getNotation(notationIdentifier).getLocalIOController().loadProjectDiagram(fsSubtreeLocation.getAbsolutePath());
                } catch (Exception exception){
                    LOG.error("Synchronization failed during a diagram loading.", exception);
                    syncDialog.appendErrorInfo(DIAGRAM_LOAD_ERROR, false);
                    syncDialog.appendErrorInfo(NOTATION_IDENRIFIER + " " + notationIdentifier, false);
                    syncDialog.appendErrorInfo(NOTATION_EXTENSION + " " + extension, false);
                    syncDialog.appendErrorInfo(fsSubtreeLocation.getAbsolutePath(), true);
                    syncDialog.appendErrorInfo(EXCEPTION_LABEL + " " + exception.toString(), true);

                    return  false;
                }

                final AddProjectItemResult result =
                        ModelerSession.getProjectControlService().addDiagram(projectDiagram, treePath, false);

                if(result.getStatus() != AddProjectItemStatus.SUCCESS){
                    syncDialog.appendErrorInfo(
                            SyncWorkersUtils.reportDiagramError(result.getStatus(), fsSubtreeLocation, treePath, false),
                            true);

                    return false;
                }

            } else if(childTreePath != null && overwriteProjectItems){ // overwrite user's object of existing diagram
                final ProjectDiagram projectDiagram;
                try{
                    projectDiagram = ModelerSession.getNotationService().getNotation(notationIdentifier).getLocalIOController().loadProjectDiagram(fsSubtreeLocation.getAbsolutePath());
                } catch (Exception exception){
                    LOG.error("Synchronization failed during a diagram loading.", exception);
                    syncDialog.appendErrorInfo(DIAGRAM_LOAD_ERROR, false);
                    syncDialog.appendErrorInfo(NOTATION_IDENRIFIER + " " + notationIdentifier, false);
                    syncDialog.appendErrorInfo(NOTATION_EXTENSION + " " + extension, false);
                    syncDialog.appendErrorInfo(fsSubtreeLocation.getAbsolutePath(), true);
                    syncDialog.appendErrorInfo(exception.getMessage(), true);

                    return  false;
                }

                if(childTreePath.getLastPathComponent() instanceof DefaultMutableTreeNode){
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) childTreePath.getLastPathComponent();

                    if(node.getUserObject() instanceof ProjectDiagram){
                        final ProjectDiagram oldProjectDiagram = (ProjectDiagram) node.getUserObject();

                        ModelerSession.getProjectControlService().switchChangePublisher(oldProjectDiagram, projectDiagram);                            
                    }
                }

                
                ModelerSession.getProjectControlService().getProjectTreeModel().valueForPathChanged(childTreePath, projectDiagram);

                LOG.info("aa");
            }

            return true;

        } else if(fsSubtreeLocation.isFile()){
            if(fsSubtreeLocation.getAbsolutePath().equals(projectRoot.getProjectFile().getAbsolutePath())){
                // the project file (.pmp)
                // the project item is not updated                  
            }

        } else if (fsSubtreeLocation.isDirectory()){

             final Set<File> synchronizedItems = new HashSet<File>();

            /* Condition (fsSubtreeLocation.list() != null) ensures that the directory is readable (due to user's rights).
             For example for the ordinary user the 'System Volume Information' directory is not accessible. */

            if(fsSubtreeLocation.list() == null){
                // File system directory is not possible to open (due to the user's rights)
                syncDialog.appendErrorInfo(DIRECTORY_ERROR_LABEL, false);
                syncDialog.appendErrorInfo("  >> " + fsSubtreeLocation.getAbsolutePath(), true);

            } else {

                for(final String fsItem : fsSubtreeLocation.list()){

                    checkInterruption();

                    final File childFile = new File(fsSubtreeLocation.getAbsolutePath(), fsItem);

                    if(childFile.isFile()){
                        synchronizedItems.add(childFile);

                        retValue &= syncSubtreeToPN(
                                treePath,
                                childFile,
                                projectRoot,
                                addProjectItems, overwriteProjectItems, deleteProjectItems
                        );

                    } else if(childFile.isDirectory()){
                        synchronizedItems.add(childFile);

                        final TreePath subfolderTreePath = ProjectServiceUtils.findTreePath(childFile, treePath);

                        if(subfolderTreePath == null){
                            if(addProjectItems){
                                //add a new project subfolder
                                final AddProjectItemResult result =
                                        ModelerSession.getProjectControlService().addSubFolder(childFile.getName(), treePath, false);

                                if(result.getStatus() == AddProjectItemStatus.SUCCESS){
                                    //new project subfolder has been added, recursive search will be performed on this folder now

                                    final TreePath newSubfolderPath = ProjectServiceUtils.findTreePath(childFile, treePath);

                                    retValue &= syncSubtreeToPN(
                                                    newSubfolderPath,
                                                    childFile,
                                                    projectRoot,
                                                    addProjectItems, overwriteProjectItems, deleteProjectItems);

                                } else {
                                    //new project subfolder couldn't be added, no recursive search will performed on this folder
                                    syncDialog.appendErrorInfo(SyncWorkersUtils.reportDiagramError(
                                            result.getStatus(), childFile, treePath, false), true);

                                    retValue &= false;
                                }
                            }

                        } else {
                            // subfolder exists in PN, there is no need to overwrite or add it
                            retValue &= syncSubtreeToPN(
                                    subfolderTreePath,
                                    childFile,
                                    projectRoot,
                                    addProjectItems, overwriteProjectItems, deleteProjectItems
                            );
                        }
                    }
                }
            }

            if(deleteProjectItems){
                final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

                for(int i = 0; i < parentNode.getChildCount(); i++){
                    final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                    final ProjectItem projectItem = (ProjectItem) childNode.getUserObject();

                    if(projectItem instanceof ProjectSubFolder){
                        if(!isSubfolderOnFS(projectItem.getDisplayName(), synchronizedItems)){
                            ModelerSession.getProjectControlService().removeProjectItem(treePath.pathByAddingChild(childNode));

                            LOG.info("Project subfolder '" + projectItem.getDisplayName() + "' has been removed.");

                        }
                    } else if(projectItem instanceof ProjectDiagram){
                        final TreePath childTreePath = treePath.pathByAddingChild(childNode);

                        final String diagramFileName =  new File(ProjectServiceUtils.getFileSystemPathToProjectItem(childTreePath)).getName();


                        if(!isDiagramOnFS(diagramFileName, synchronizedItems)){
                            ModelerSession.getProjectControlService().removeProjectItem(childTreePath);

                            LOG.info("Project diagram '" + projectItem.getDisplayName() + "' has been removed.");
                        }
                    }
                }
            }

        } else {
            LOG.error("An unknown file system has been identify. File system item has to be either file or directory.");
            syncDialog.appendErrorInfo(GENERAL_ERROR, true);             
            return false;
        }

        return retValue;
    }

    /**
     * Checks whether there is the required diagram on FS.
     *
     * @param diagramFileName diagram file name
     * @param synchronizedItems sync items
     * @return true when found, false otherwise
     */
    private boolean isDiagramOnFS(final String diagramFileName, final Set<File> synchronizedItems) {
        for(final File file : synchronizedItems){
            if(file.isFile()){
                if(file.getName().equals(diagramFileName)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether there is the required subfolder on FS.
     *
     * @param subfolderName subfolder file name
     * @param synchronizedItems sync items
     * @return true when found, false otherwise
     */
    private boolean isSubfolderOnFS(final String subfolderName, final Set<File> synchronizedItems) {
        for(final File file : synchronizedItems){
            if(file.isDirectory()){
                if(file.getName().equals(subfolderName)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Shows the sync dialog.
     */
    public void showDialog(){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                syncDialog.setVisible(true);
            }
        });
    }

    /**
     * An object of SwingWorker invokes this done method on the Event Dispatcher Thread (EDT) when the doInBackground method
     * finishes. This invocation is enqueued to the end of EDT's EventQueue. So it has to be queued after the task,
     * tak shows the dialog. 
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
            LOG.error("The synchronization from the file system has been interrupted.", e);
        } catch (ExecutionException e) {
            LOG.error("There is no return value available.", e);
        }

        syncDialog.syncComplete(true);
    }

    private void checkInterruption() throws InterruptedException{
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }                
    }
}

