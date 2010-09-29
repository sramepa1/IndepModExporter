package cz.cvut.promod.services.projectService;

import javax.swing.tree.*;
import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.io.*;

import cz.cvut.promod.services.projectService.treeProjectNode.*;
import cz.cvut.promod.services.projectService.treeProjectNode.listener.ProjectDiagramListener;
import cz.cvut.promod.services.projectService.localIO.ProjectFileSaver;
import cz.cvut.promod.services.projectService.localIO.ProjectFileLoader;
import cz.cvut.promod.services.projectService.syncWorkers.SyncFromFSWorker;
import cz.cvut.promod.services.projectService.syncWorkers.SyncFromPNWorker;
import cz.cvut.promod.services.projectService.dialogs.SyncDialog;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.results.LoadProjectResult;
import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.results.SaveProjectResult;
import cz.cvut.promod.services.ModelerSession;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.PresentationModel;
import foxtrot.Task;
import foxtrot.Worker;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:50:24, 10.10.2009
 */

/**
 * ProjectControlService implementation.. 
 */
public class ProjectControlServiceImpl  extends Model implements ProjectControlService, ProjectDiagramListener{

    private final Logger LOG = Logger.getLogger(ProjectControlServiceImpl.class);

    private static final String SYNC_FROM_PN_TITLE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.fromPN.title");

    private final DefaultTreeModel treeModel;
    private final DefaultMutableTreeNode rootNode;

    public static final String PROPERTY_SELECTED_TREE_PATH = "selectedTreePath";
    private TreePath selectedTreePath;

    private final TreePath rootTreePath;

    private final Map<ProjectDiagram, Set<ProjectDiagramListener>> listenerMap;

    
    public ProjectControlServiceImpl(){
        rootNode = new DefaultMutableTreeNode("ProjectsRoot");
        this.treeModel = new DefaultTreeModel(rootNode);

        rootTreePath = new TreePath(rootNode);

        listenerMap = new HashMap<ProjectDiagram, Set<ProjectDiagramListener>>();
    }

    /** {@inheritDoc} */
    public boolean check() {       
        return true;
    }

    /** {@inheritDoc} */
    public String getSelectedProjectNotationIndentifier() {
        final ProjectDiagram projectDiagram;

        try{
            if(getSelectedTreePath() != null){
                projectDiagram = (ProjectDiagram) ((DefaultMutableTreeNode) getSelectedTreePath().getLastPathComponent()).getUserObject();
            } else {
                return null;
            }

        } catch (final Exception exception){
            return null;
        }

        return projectDiagram.getNotationIdentifier();
    }

    /** {@inheritDoc} */
    public DefaultTreeModel getProjectTreeModel() {
        return treeModel;
    }

    /** {@inheritDoc} */
    public AddProjectItemResult addProject(final ProjectRoot projectRoot, final boolean select) {

        final String projectName = projectRoot.getDisplayName();

        if(!ProjectServiceUtils.isSyntacticallyCorrectName(projectName)){
            LOG.error("Invalid project name or project name containing disallowed symbol(s), project name: " + projectName);
            return new AddProjectItemResult(AddProjectItemStatus.INVALID_NAME, null);
        }

        if(!ProjectServiceUtils.isUniqueProjectContainerDisplayName(rootNode, projectName)){
            LOG.error("An attempt to add project with existing name has occurred.");
            return new AddProjectItemResult(AddProjectItemStatus.NAME_DUPLICITY, null);
        }

        final DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(projectRoot);

        treeModel.insertNodeInto(projectNode, rootNode, rootNode.getChildCount());

        final TreePath treePath = new TreePath(rootNode).pathByAddingChild(projectNode);

        if(select){
            setSelectedItem(treePath);
        }

        return
            new AddProjectItemResult(AddProjectItemStatus.SUCCESS, treePath);
    }

    /** {@inheritDoc} */
    public void setSelectedItem(TreePath treePath) {
        if(treePath == null){
            LOG.error("Nullary path to select");
            return;
        }
        if(treePath.equals(rootTreePath)){
            LOG.debug("An attempt to select project tree root node has occurred. Selected items will be set to null.");
            treePath = null;
        }
        if(getSelectedTreePath() == null){
            if(getSelectedTreePath() != treePath){
                this.selectedTreePath = treePath;
                firePropertyChange(PROPERTY_SELECTED_TREE_PATH, null, getSelectedTreePath());
            }
        }
        else if( !getSelectedTreePath().equals(treePath)){
            final TreePath oldSelectedTreePath = this.getSelectedTreePath();
            this.selectedTreePath = treePath;
            firePropertyChange(PROPERTY_SELECTED_TREE_PATH, oldSelectedTreePath, getSelectedTreePath());
        }
    }

    /** {@inheritDoc} */
    public List<ProjectRoot> getProjects(){
        final List<ProjectRoot> projects = new LinkedList<ProjectRoot>();             

       for(final TreePath treePath : getProjectPaths()){
            final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            try{
                final ProjectRoot projectRoot = (ProjectRoot) projectNode.getUserObject();

                projects.add(projectRoot);

            } catch (ClassCastException exception){
                LOG.fatal("Root node of project is not ProjectRoot.");
            }

        }

        return projects;
    }

    /** {@inheritDoc} */
    public List<TreePath> getProjectPaths() {
        final List<TreePath> projectItems = new LinkedList<TreePath>();

        for(int i = 0; i < rootNode.getChildCount(); i++){
            final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);

            final TreePath treePath = new TreePath(rootNode);
            projectItems.add(treePath.pathByAddingChild(projectNode));
        }

        return projectItems;
    }

    /** {@inheritDoc} */
    public ProjectRoot getProject(String projectName) {
        final TreePath treePath = getProjectPath(projectName);

        if(treePath != null){
            final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            
            try{
                return (ProjectRoot) projectNode.getUserObject();

            } catch (ClassCastException exception){
                LOG.fatal("Root node of project is not ProjectRoot.");
            }            
        }

        return null;
    }

    /** {@inheritDoc} */
    public DefaultMutableTreeNode getProjectTreeNode(final String projectName) {
        final TreePath projectTreePath = getProjectPath(projectName);

        if(projectTreePath == null){
            return null;
        }

        try{
            return (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        } catch (ClassCastException exception){
            LOG.error("(Impossible to cast tree node to an instance of DefaultMutableTreeNode.");
            return null;
        }
    }

    /** {@inheritDoc} */
    public TreePath getProjectPath(final String projectName) {
        for(final TreePath treePath : getProjectPaths()){
            final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            try{
                final ProjectRoot projectRoot = (ProjectRoot) projectNode.getUserObject();

                if(projectRoot.getDisplayName().equals(projectName)){
                    return treePath;
                }

            } catch (ClassCastException exception){
                LOG.fatal("Root node of project is not ProjectRoot.");
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    public boolean isOpenedProject(String projectName) {
        return getProjectPath(projectName) != null;
    }

    /** {@inheritDoc} */
    public TreePath getSelectedTreePath() {
        return selectedTreePath;
    }

    /** {@inheritDoc} */
    public ValueModel getSelectedItem() {
        final PresentationModel<ProjectControlServiceImpl> presentationModel = new PresentationModel<ProjectControlServiceImpl>(this);

        return presentationModel.getModel(PROPERTY_SELECTED_TREE_PATH);
    }

    /** {@inheritDoc} */
    public AddProjectItemResult addSubFolder(final String subFolderName, final boolean select) {
        if(getSelectedTreePath() != null){
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getSelectedTreePath().getLastPathComponent();
            TreePath parentTreePath = getSelectedTreePath();

            while(!ProjectServiceUtils.isProjectContainer(parent)){
                parentTreePath = parentTreePath.getParentPath();
                parent = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();

                if(parent == rootNode){
                    //any strange behavior occurred, should never happened, testing & debugging
                    LOG.error("No project container to place new subfolder");
                    return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
                }
            }

            return addSubFolder(subFolderName, parentTreePath, select);

        }

        return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
    }

    /** {@inheritDoc} */
    public AddProjectItemResult addSubFolder(final String subFolderName,
                                             final TreePath parentTreePath,
                                             final boolean select) {

        if(!ProjectServiceUtils.isSyntacticallyCorrectName(subFolderName)){
            LOG.error("Invalid subfolder name or name containing disallowed symbol(s), project name: " + subFolderName);
            return new AddProjectItemResult(AddProjectItemStatus.INVALID_NAME, null);            
        }

        if(parentTreePath == null){
            LOG.error("Nullary parent tree path when adding new subfolder.");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
        }

        if(!ProjectServiceUtils.isValidTreePath(parentTreePath)){
            LOG.error("Parent tre e path is not valid.");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
        }

        if(!ProjectServiceUtils.isProjectContainer((DefaultMutableTreeNode) parentTreePath.getLastPathComponent())){
            LOG.error("Parent node is not a project container.");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null); 
        }

        final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();

        if(!ProjectServiceUtils.isUniqueProjectContainerDisplayName(parent, subFolderName)){
            LOG.error("An attempt to add subfolder to a level, where an item with the same display name has been already inserted.");
            return new AddProjectItemResult(AddProjectItemStatus.NAME_DUPLICITY, null);
        }

        final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
        final ProjectSubFolder projectSubFolder = new ProjectSubFolder(subFolderName);
        newNode.setUserObject(projectSubFolder);

        treeModel.insertNodeInto(newNode, parent, parent.getChildCount());

        final TreePath treePath = parentTreePath.pathByAddingChild(newNode);

        if(select){
            //select and expand to the newly added node in project tree
            setSelectedItem(treePath);
        }

        LOG.info("New subfolder '" + projectSubFolder.getDisplayName() + "' has been added.");

        return new AddProjectItemResult(AddProjectItemStatus.SUCCESS, treePath);
    }

    /** {@inheritDoc} */
    public AddProjectItemResult addDiagram(final ProjectDiagram projectDiagram,
                                            final boolean select) {

        if(getSelectedTreePath() != null){
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getSelectedTreePath().getLastPathComponent();
            TreePath parentTreePath = getSelectedTreePath();

            while(!ProjectServiceUtils.isProjectContainer(parent)){
                parentTreePath = parentTreePath.getParentPath();
                parent = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();
            }

            return addDiagram(projectDiagram, parentTreePath, select);
        }

        return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
    }

    /** {@inheritDoc} */
    public AddProjectItemResult addDiagram(final ProjectDiagram projectDiagram,
                                           final TreePath parentTreePath,
                                           final boolean select) {

        if(projectDiagram == null){
            return new AddProjectItemResult(AddProjectItemStatus.INVALID_ITEM_DATA, null);
        }

        final String displayName = projectDiagram.getDisplayName();
        if(!ProjectServiceUtils.isSyntacticallyCorrectName(projectDiagram.getDisplayName())){
            LOG.error("Diagram name contains disallowed symbol(s), project name: " + projectDiagram.getDisplayName());
            return new AddProjectItemResult(AddProjectItemStatus.INVALID_NAME, null);
        }

        final String notationIdentifier = projectDiagram.getNotationIdentifier();
        if(notationIdentifier == null || !ModelerSession.getNotationService().existNotation(notationIdentifier)){
            LOG.error("An attempt to create a new diagram with no existing notation identifier has occurred.");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_NOTATION, null);
        }

        if(parentTreePath == null){
            LOG.error("Nullary parent when adding new diagram");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null); 
        }

        if(!ProjectServiceUtils.isValidTreePath(parentTreePath)){
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null);
        }

        final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)parentTreePath.getLastPathComponent();

        if(!ProjectServiceUtils.isProjectContainer(parent)){
            LOG.error("Last tree path component is not a project container");
            return new AddProjectItemResult(AddProjectItemStatus.ILLEGAL_PARENT, null); 
        }

        final ProjectItem parentProjectItem = (ProjectItem) parent.getUserObject();


        if(!ProjectServiceUtils.isUniqueProjectDiagramName(parent, projectDiagram)){
            LOG.error("An attempt to add new diagram to a level, where an item with the same display name and notation has been already inserted.");
            return new AddProjectItemResult(AddProjectItemStatus.NAME_DUPLICITY, null);
        }

        final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();

        newNode.setUserObject(projectDiagram);

        treeModel.insertNodeInto(newNode, parent, parent.getChildCount());

        final TreePath treePath = parentTreePath.pathByAddingChild(newNode);

        //select and expand to the newly added node in project tree
        if(select){
            setSelectedItem(treePath);
        }

        LOG.info("New diagram has been added, " + parentProjectItem.getDisplayName() + " -> " + displayName + ", notation: "+ notationIdentifier + ".");
        
        return new AddProjectItemResult(AddProjectItemStatus.SUCCESS, treePath);
    }

    /** {@inheritDoc} */
    public String removeProjectItem(final TreePath treePath) {
        final String fileSystemPath = ProjectServiceUtils.getFileSystemPathToProjectItem(treePath);

        if(fileSystemPath == null){
            LOG.error("File system path to the project item couldn't be calculated.");
            return null;
        }        

        final DefaultMutableTreeNode node;
        try{
            node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        } catch (ClassCastException exception){
            LOG.error("All components in the project tree have to be an instance of the DefaultMutableTreeNode class.");
            return null;
        }

        // if the item or it's any child is currently selected, then select item's parent item
        if(ProjectServiceUtils.isTreePathExtension(treePath, getSelectedTreePath())){
            LOG.debug("Selecting parent of the project tree item, that will be deleted.");

            setSelectedItem(treePath.getParentPath());
        }

        // publish change
        if(ProjectServiceUtils.isProjectDiagram(node)){
            publishRemoveEvent(node);
            
        } else if(ProjectServiceUtils.isProjectContainer(node)){
            publishRemoveEvent(node);
        }

        treeModel.removeNodeFromParent(node);

        LOG.info("Project item has been deleted: " + node);

        return fileSystemPath;
    }

    /**
     * Recursivlly goes through project items hierarchy and dispatch the remove event.
     *
     * It removes the automatically removes the project diagram listeners records.
     *
     * @param node where it is supposed to start
     */
    private void publishRemoveEvent(final DefaultMutableTreeNode node) {
        if(ProjectServiceUtils.isProjectDiagram(node)){
            final ProjectDiagram projectDiagram = (ProjectDiagram) node.getUserObject();

            projectDiagram.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, null);
            listenerMap.remove(projectDiagram);

        } else {
            for(int i = 0; i < node.getChildCount(); i++){
                publishRemoveEvent((DefaultMutableTreeNode) node.getChildAt(i)); 
            }
        }
    }

    /** {@inheritDoc} */
    public TreePath getSelectedProjectPath() {
        if(getSelectedTreePath() == null){
            return null;
        }

        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) getSelectedTreePath().getLastPathComponent();
        TreePath treePath = getSelectedTreePath();

        while(!(treeNode.getUserObject() instanceof ProjectRoot)){
            treePath = treePath.getParentPath();
            treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        }

       return treePath;
    }

    /** {@inheritDoc} */
    public ProjectRoot getSelectedProject() {
        if(getSelectedProjectPath() == null){
            return null;
        }

        try{
            final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) getSelectedProjectPath().getLastPathComponent();
            return (ProjectRoot) projectNode.getUserObject();
        } catch (final ClassCastException exception){
            LOG.error("Wrong class casting during looking for selected project.");
        }

        return null;
    }

    /** {@inheritDoc} */
    public TreePath getSelectedDiagramPath() {
        if(getSelectedTreePath() == null){
            return null;
        }

        final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) getSelectedTreePath().getLastPathComponent();

        if(defaultMutableTreeNode.getUserObject() instanceof ProjectDiagram){
            return getSelectedTreePath();
        }

        return null;
    }

    /** {@inheritDoc} */
    public ProjectDiagram getSelectedDiagram() {
        if(getSelectedDiagramPath() == null){
            return null;
        }

        return (ProjectDiagram) ((DefaultMutableTreeNode) getSelectedDiagramPath().getLastPathComponent()).getUserObject();
    }

    /** {@inheritDoc} */
    public SaveProjectResult saveProject(final TreePath projectTreePath){

        try {
            ProjectFileSaver.saveProject(projectTreePath, true);

        } catch (IllegalArgumentException e){
            LOG.error("Not valid project root tree path.");
            return SaveProjectResult.INVALID_TREE_PATH;            

        } catch (XMLStreamException e) {
            LOG.error("An error during xml streaming has occurred.", e);
            return SaveProjectResult.XML_ERROR;

        } catch (FileNotFoundException e) {
            LOG.error("No or wrong file to store project root file.", e);
            return SaveProjectResult.IOERROR;

        } catch (IOException e) {
            LOG.error("IOError during saving project root file.", e);
            return SaveProjectResult.IOERROR;

        } catch (TransformerException e) {
            LOG.error("Xml transformation (formatting) error.", e);
            return SaveProjectResult.XML_ERROR;
        }

        return SaveProjectResult.SUCCESS;
    }

    /** {@inheritDoc} */
    public LoadProjectResult loadProject(final File projectFile) {
        return ProjectFileLoader.loadProjectFile(projectFile);
    }

    /**
     * For more info about concurrency system used see the implementation of
     * synchronize(final File projectFile, final List<String> pathOffset, ...) method. 
     *
     * {@inheritDoc} */
    public boolean synchronize(final TreePath treePath,
                               final boolean addProjectItems,
                               final boolean overwriteProjectItems,
                               final boolean deleteProjectItems,
                               final boolean cancable) {

        if(!SwingUtilities.isEventDispatchThread()){
            LOG.error("Not possible to run synchronization in a different thread than EventDispatcherThread.");
            return false;           
        }

        if(!(addProjectItems || overwriteProjectItems || deleteProjectItems)){
            LOG.info("Synchronization invoked but with no rights to add, overwrite or delete  items from the file system.");
            return true;
        }

        final boolean retValue;

        final SyncDialog syncDialog = new SyncDialog(SYNC_FROM_PN_TITLE_LABEL);

        final SyncFromPNWorker syncFromPNWorker = new SyncFromPNWorker(
                treePath, addProjectItems, overwriteProjectItems, deleteProjectItems,
                syncDialog
        );

        if(cancable){
            syncDialog.setSyncWorker(syncFromPNWorker);
        }

        syncFromPNWorker.execute();

       try {
            retValue = (Boolean) Worker.post(new Task(){
                public Object run() throws Exception {

                    try {
                        return syncFromPNWorker.get();

                    } catch (InterruptedException e) {
                        LOG.error("The synchronization from the project navigation has been interrupted.", e);
                    } catch (ExecutionException e) {
                        LOG.error("There is no return value available.", e);
                    } catch (CancellationException e){
                        LOG.info("Synchronization has been canceled.", e);
                    } catch (Exception e){
                        LOG.error("Unknown synchronization error.", e);
                    }
                    return  false;

                }
            });

        } catch (Exception e) {
            LOG.error("Synchronous Foxtror thread invocation has failed. ProMod can be in a unstable state now.", e);
            return false;
        }

        return retValue;
    }    

    /** {@inheritDoc} */
    public boolean synchronize(final File projectFile,
                               final List<String> pathOffset,
                               final boolean addProjectItems,
                               final boolean overwriteProjectItems,
                               final boolean deleteProjectItems,
                               final boolean cancelable) {

        if(!SwingUtilities.isEventDispatchThread()){
            LOG.error("Not possible to run synchronization in a different thread than EventDispatcherThread.");
            return false;
        }        

        if(!(addProjectItems || overwriteProjectItems || deleteProjectItems)){
            LOG.info("Synchronization invoked but with no rights to add, overwrite or delete items from the project navigation.");
            return true;
        }

        final boolean retValue;

        final SyncDialog syncDialog = new SyncDialog(
                ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.fromFS.title")
        );

        final SyncFromFSWorker syncFromFSWorker = new SyncFromFSWorker(
                                                            projectFile,
                                                            pathOffset,
                                                            addProjectItems, overwriteProjectItems, deleteProjectItems,
                                                            syncDialog);

        // enable possibility to cancel long running synchronization
        if(cancelable){
            syncDialog.setSyncWorker(syncFromFSWorker);
        }

        /*
        Runs the synchronization in SwingWorker thread. This is asynchronous call and EDT continues during the
         synchronization takes a place.
         This thread in it's doInbackgroud() method schedules the show of SyncDialog using SwingUtilities.invokeLater().
         When this SwingWorker thread finishes, then it schedules it's done() method using SwingUtilities.invokeLater()
         too. It is so ensured that the SyncDialog is always disposed (in the done() method) after it has been already
         shown.
         */
        syncFromFSWorker.execute();


        /*
        This is kind of hack, how to ensure, that the synchronize method never finishes earlier that the synchronization
          is done (synchronization in the SwingWorker thread) (by success, failure or has been canceled). For this is
          used SYNCHRONOUS call of Foxtror framework. New task is scheduled and this try block waits for the end of the
          synchronization in the SwingWorker (blocking call of SwingWorker#get() ).
         */
        try {
            retValue = (Boolean) Worker.post(new Task(){
                public Object run() throws Exception {

                    try {
                        return syncFromFSWorker.get();

                    } catch (InterruptedException e) {
                        LOG.error("The synchronization from the file system has been interrupted.", e);
                    } catch (ExecutionException e) {
                        LOG.error("There is no return value available.", e);
                    } catch (CancellationException e){
                        LOG.info("Synchronization has been canceled.", e);
                    } catch (Exception e){
                        LOG.error("Uknown synchronization error.", e);
                    }

                    return  false;
                }
            });
            
        } catch (Exception e) {
            LOG.error("Synchronous Foxtror thread invocation has failed. ProMod can be in unstable state now.", e);
            return false;
        }

        return retValue;
    }

    /** {@inheritDoc} */    
    public void registerDiagramListener(final ProjectDiagram projectDiagram, final ProjectDiagramListener listener) {
        if((projectDiagram == null) || (listener == null)){
            LOG.error("Nullary project diagram or listener to be unregistered");
            return;
        }

        if(listenerMap.containsKey(projectDiagram)){
            final Set<ProjectDiagramListener> set = listenerMap.get(projectDiagram);
            set.add(listener);

            return;
        }

        projectDiagram.addChangeListener(this);

        final Set<ProjectDiagramListener> list = new HashSet<ProjectDiagramListener>();
        list.add(listener);
        listenerMap.put(projectDiagram, list);
    }

    /**
     * {@inheritDoc}
     *
     * Can be used for special event on demand dispatching.
     * One has to be very very careful when using this method himself. There are many consequences.  
     */
    public void changePerformed(final ProjectDiagramChange change) {
        if((change == null) || (change.getProjectDiagram() == null) || change.getChangeType() == null){
            return;
        }

        final ProjectDiagram sourceDiagram = change.getProjectDiagram();

        if(listenerMap.containsKey(sourceDiagram)){
            final Set<ProjectDiagramListener> set = listenerMap.get(sourceDiagram);

            for(final ProjectDiagramListener listener : set){
                // forward change
                listener.changePerformed(change);
            }

        } else {
            LOG.debug("No registered listeners for diagram: " + sourceDiagram.getDisplayName());
        }

        switch (change.getChangeType()){
            case REMOVED_FROM_NAVIGATION:
                LOG.info("Removing diagram from map of listeners: " + sourceDiagram.getDisplayName());
                listenerMap.remove(sourceDiagram);
                break;
        }
    }

    /** {@inheritDoc} */
    public void unRegisterDiagramListener(final ProjectDiagram projectDiagram, final ProjectDiagramListener listener) {
        if((projectDiagram == null) || (listener == null)){
            LOG.error("Nullary info for un-registering listeners.");
            return;
        }

        if(listenerMap.containsKey(projectDiagram)){
            final Set<ProjectDiagramListener> set = listenerMap.get(projectDiagram);
            set.remove(listener);
        }
    }

    /** {@inheritDoc} */
    public void unRegisterListener(final ProjectDiagramListener listener) {
        if(listener == null){
            LOG.error("Nullary info for un-registering listeners.");
            return;
        }

        for(final ProjectDiagram projectDiagram : listenerMap.keySet()){
            unRegisterDiagramListener(projectDiagram, listener);
        }
    }

    /** {@inheritDoc} */
    public void switchChangePublisher(final ProjectDiagram oldProjectDiagram, final ProjectDiagram newProjectDiagram) {
        if(oldProjectDiagram == null || newProjectDiagram == null){
            return;
        }

        if(listenerMap.containsKey(oldProjectDiagram)){
            if(listenerMap.containsKey(newProjectDiagram)){
                LOG.info("New project diagram is already registered as change publisher");

            } else {
                listenerMap.put(newProjectDiagram, listenerMap.get(oldProjectDiagram));
                listenerMap.remove(oldProjectDiagram);

                newProjectDiagram.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, oldProjectDiagram);
            }

        }
    }

    /** {@inheritDoc} */
    public void switchChangeListener(ProjectDiagramListener oldProjectDiagramListener, ProjectDiagramListener newProjectDiagramListener) {
        if(oldProjectDiagramListener == null || newProjectDiagramListener == null){
            return;
        }

        for(final Set<ProjectDiagramListener> listenerSet : listenerMap.values()){
            if(listenerSet.contains(oldProjectDiagramListener)){
                listenerSet.remove(oldProjectDiagramListener);
                listenerSet.add(newProjectDiagramListener);
                LOG.info("Listener has been changed. Old: " + oldProjectDiagramListener.toString() +", new:" + newProjectDiagramListener.toString() );
            }
        }
    }
}
