package cz.cvut.promod.gui.projectNavigation;

import com.jidesoft.dialog.JideOptionPane;
import cz.cvut.promod.gui.Modeler;
import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.gui.dialogs.addNewDiagramDialog.AddNewDiagramDialog;
import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialog;
import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.executors.AddProjectSubFolderExecutor;
import cz.cvut.promod.gui.projectNavigation.actions.executors.RenameExecutor;
import cz.cvut.promod.gui.projectNavigation.events.ProjectTreeExpandEvent;
import cz.cvut.promod.gui.projectNavigation.listeners.MousePopup;
import cz.cvut.promod.gui.projectNavigation.renderer.ProjectNavigationCellRenderer;
import cz.cvut.promod.resources.Resources;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.MenuControlService;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectItem;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Enumeration;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:53:42, 11.10.2009
 */

/**
 * Project navigation controller.
 */
public class ProjectNavigation extends ProjectNavigationView implements TreeSelectionListener{

    private static final String EXPAND_TIP_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.expand.all");
    private static final String COLLAPSE_TIP_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.collapse.all");
    private static final String ADD_DIAGRAM_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.new.diagram");
    private static final String ADD_SUBFOLDER_TIP_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.new.subfolder");
    public static final String RENAME_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.rename");
    public static final String RENAME_DESCRIPTION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.rename.description");
    public static final String INVALID_NAME_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.add.new.diagram.dialog.error.disallowed");
    public static final String DUPLICATE_NAME_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.project.rename.duplicity");
    public static final String RENAME_ERROR_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.project.rename.error");
    public static final String RENAME_ERROR_TITLE_LABEL =
        ModelerSession.getCommonResourceBundle().getString("modeler.project.rename.error.title");

    public static final String ACTION_EXPAND = "modeler.project.navigation.expand";
    public static final String ACTION_ADD_SUBFOLDER = "modeler.project.navigation.add.subfolder";
    public static final String ACTION_ADD_DIAGRAM = "modeler.project.navigation.add.diagram";
    public static final String ACTION_DELETE = "modeler.project.navigation.delete";
    public static final String ACTION_CLOSE_PROJECT = "modeler.project.navigation.close.project";
    public static final String ACTION_REFRESH = "modeler.project.navigation.refresh";
    public static final String ACTION_RENAME = "modeler.project.navigation.action.rename";

    public static final String ADD_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.add");

    public static final String EXPAND_LABEL = ModelerSession.getCommonResourceBundle().getString(ACTION_EXPAND);


    private final Logger LOG = Logger.getLogger(ProjectNavigation.class);

    public ProjectNavigation(final int dockableFrameIndex) {
        super(dockableFrameIndex);

        setShowContextMenu(false); 

        initActions();

        initPopupMenu();

        initMainMenu();

        initProjectTree();

        initEventHandling();

        ToolTipManager.sharedInstance().registerComponent(projectTree);

        initToolTips();        
    }

    private void initToolTips() {
        addDiagramButton.setToolTipText(ADD_DIAGRAM_LABEL);
        addSubfolderButton.setToolTipText(ADD_SUBFOLDER_TIP_LABEL);
        expandButton.setToolTipText(EXPAND_TIP_LABEL);
        collapseButton.setToolTipText(COLLAPSE_TIP_LABEL);
    }

    /**
     * Initialize the popup menu.
     */
    private void initPopupMenu() {
        //TODO: refactor these two calls of insertProjectNavigationPopupMenuItem,
        // so there will be only one insertProjectNavigationPopupMenuItem implementation
        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_SUBFOLDER),
                new MenuItemPosition(ADD_LABEL)
        );

        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_DIAGRAM),
                new MenuItemPosition(
                        ADD_LABEL,
                        MenuItemPosition.PlacementStyle.FIRST)
        );

        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_RENAME),
                new MenuItemPosition(""),
                null
        );        

        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_DELETE),
                new MenuItemPosition(""),
                null
        );

        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_CLOSE_PROJECT),
                new MenuItemPosition(""),
                null
        );

        ModelerSession.getMenuService().insertProjectNavigationPopupMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_REFRESH),
                new MenuItemPosition(""),
                null
        );

    }

    /**
     * Initialize the main menu.
     */
    public void initMainMenu(){
        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_DIAGRAM),
                new MenuItemPosition(
                        Modeler.PROJECT_LABEL +
                        MenuService.PLACEMENT_DELIMITER +
                        ADD_LABEL)
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_SUBFOLDER),
                new MenuItemPosition(
                        Modeler.PROJECT_LABEL +
                        MenuService.PLACEMENT_DELIMITER +
                        ADD_LABEL)
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_CLOSE_PROJECT),
                new MenuItemPosition(Modeler.PROJECT_LABEL + MenuService.PLACEMENT_DELIMITER, 4)
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_REFRESH),
                new MenuItemPosition(Modeler.PROJECT_LABEL + MenuService.PLACEMENT_DELIMITER, MenuItemPosition.PlacementStyle.LAST)
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_RENAME),
                new MenuItemPosition(Modeler.FILE_LABEL + MenuService.PLACEMENT_DELIMITER, 1)
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_DELETE),
                new MenuItemPosition(Modeler.FILE_LABEL + MenuService.PLACEMENT_DELIMITER, 1)
        );
    }

    private void initActions() {
        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_RENAME,
                new ProModAction(
                        RENAME_LABEL,
                        Resources.getIcon(Resources.MODELER + Resources.RENAME_ICON),
                        KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        final TreePath treePath = ModelerSession.getProjectService().getSelectedTreePath();

                        if(treePath != null){
                        if(treePath.getLastPathComponent() instanceof DefaultMutableTreeNode){
                            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();

                            if(node.getUserObject() instanceof ProjectItem){
                                final ProjectItem projectItem = (ProjectItem) node.getUserObject();

                                new SimpleTextFieldDialog(
                                    RENAME_LABEL,
                                    RENAME_DESCRIPTION_LABEL,
                                    projectItem.getDisplayName(),
                                    Modeler.OK_LABEL,
                                    Modeler.CANCEL_LABEL,
                                    new RenameExecutor(treePath, projectItem, projectTree),
                                    ModelerSession.getFrame(),
                                    true
                                );
                            }
                        }
                    }
                }
                }
        );

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_EXPAND,
                new ProModAction(EXPAND_LABEL, null, null){
                    public void actionPerformed(ActionEvent event) {
                        final ProjectTreeExpandEvent expandEvent;
                        try{
                            expandEvent = (ProjectTreeExpandEvent) event;
                        } catch (ClassCastException exception){
                            LOG.error("Invalid event for project tree expanding/collapsing", exception);
                            return;
                        }

                        final TreePath treePath = expandEvent.getTreePath();

                        expandAll(treePath, expandEvent.isExpand(), expandEvent.getDepth());
                    }
                }
        );

        ModelerSession.getActionControlService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_ADD_SUBFOLDER,
                new ProModAction(
                        ModelerSession.getCommonResourceBundle().getString(ACTION_ADD_SUBFOLDER),
                        Resources.getIcon(Resources.NAVIGATION + Resources.ICONS + Resources.FOLDER_ADD_ICON),
                        KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK)){

                    public void actionPerformed(ActionEvent event) {
                        if(ModelerSession.getProjectService().getSelectedProject() == null){
                            LOG.warn("An attempt to insert new subfolder without project has occurred.");

                            JOptionPane.showMessageDialog(
                                    ModelerSession.getFrame(),
                                    ModelerSession.getCommonResourceBundle().getString("modeler.add.dialog.error.noSelectedProject"),
                                    ModelerSession.getCommonResourceBundle().getString("modeler.add.dialog.error.noSelProjectTitle"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        new SimpleTextFieldDialog(
                                ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.title"),
                                ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.label"),
                                "",
                                ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.confirm"),
                                ModelerSession.getCommonResourceBundle().getString("modeler.cancel"),
                                new AddProjectSubFolderExecutor(),
                                ModelerSession.getFrame(),
                                true
                        );
                    }
                });

        ModelerSession.getActionControlService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_ADD_DIAGRAM,
                new ProModAction(
                        ModelerSession.getCommonResourceBundle().getString(ACTION_ADD_DIAGRAM),
                        Resources.getIcon(Resources.NAVIGATION + Resources.ICONS + Resources.DIAGRAM_ADD_ICON),
                        KeyStroke.getKeyStroke(
                        KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK)){

                    public void actionPerformed(ActionEvent event) {
                        if(ModelerSession.getProjectService().getSelectedProject() == null){
                            LOG.warn("An attempt to insert new diagram without project has occurred.");

                            JOptionPane.showMessageDialog(
                                    ModelerSession.getFrame(),
                                    ModelerSession.getCommonResourceBundle().getString("modeler.add.dialog.error.noSelectedProject"),
                                    ModelerSession.getCommonResourceBundle().getString("modeler.add.dialog.error.noSelProjectTitle"),
                                    JOptionPane.ERROR_MESSAGE);

                            return;
                        }

                        new AddNewDiagramDialog();
                    }
                });

        ModelerSession.getActionControlService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_DELETE,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(
                        ACTION_DELETE),
                        Resources.getIcon(Resources.MODELER + Resources.DELETE_ICON),
                        null){
                    public void actionPerformed(ActionEvent event) {
                        removeItem();
                    }
                });

        ModelerSession.getActionControlService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_CLOSE_PROJECT,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(ACTION_CLOSE_PROJECT), null, null){
                    public void actionPerformed(ActionEvent event) {
                        closeProject();
                    }
                });

        ModelerSession.getActionControlService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                null,
                ACTION_REFRESH,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(
                        ACTION_REFRESH),
                        null,
                        null){
                    public void actionPerformed(ActionEvent event) {
                        refreshProject();
                    }
                });        
    }

    /**
     * Refresh the selected project with the file system structure.
     */
    public void refreshProject(){
        final ProjectRoot selectedProjectRoot = ModelerSession.getProjectService().getSelectedProject();

        if(selectedProjectRoot == null){
            LOG.warn("There is no selected project to be refreshed.");
            return;
        }

        ModelerSession.getProjectControlService().synchronize(
                selectedProjectRoot.getProjectFile(),
                null,
                true, false, true, /*add, NOT over-write, delete */
                true
        );
    }

    /**
     * Closes the selected project = removes this project from the tree project model.
     */
    private void closeProject() {
        final TreePath treePath = ModelerSession.getProjectService().getSelectedProjectPath();

        if(treePath != null){        
            if(!Modeler.ExitDialogReturnValues.CANCEL.equals(checkForUnsavedChanges(treePath, treePath))){
                ModelerSession.getProjectControlService().removeProjectItem(treePath);
            }
        }
    }

    /**
     * Removed project selected item.
     */
    private void removeItem() {
        final TreePath selectedTreePath = ModelerSession.getProjectService().getSelectedTreePath();

        if(selectedTreePath!= null){
        final DefaultMutableTreeNode node;
        try{
            node = (DefaultMutableTreeNode) selectedTreePath.getLastPathComponent();
        } catch (ClassCastException exception){
            LOG.error("All project navigation nodes have to be an instance of DefaultMutableTreePath class.");
            return;
        }

        if(ProjectServiceUtils.isProjectDiagram(node)){
            final String title = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.diagram.title");
            final String message = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.diagram.message");

            //show project dialog confirm. dialog
            if(showRemoveConfirmDialog(title, message)){

                final String fsPath = ModelerSession.getProjectControlService().removeProjectItem(selectedTreePath);

                if(fsPath == null){
                    //should never happened, testing & debugging
                    LOG.error("Removal of project diagram has failed. Node: " + node.toString());
                } else {
                    //remove the file associated with the project diagram
                    if(!ProjectServiceUtils.deleteFSItem(selectedTreePath.getParentPath(), new File(fsPath).getName())){
                        // file system deletion has failed
                        LOG.error("ProMod couldn't delete selected diagram from file system: " + fsPath);
                    }
                }
            }

        //show project subfolder confirm. dialog
        } else if(ProjectServiceUtils.isProjectSubfolder(node)){
            final String title = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.diagram.title");
            final String message = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.subfolder.message");

            if(showRemoveConfirmDialog(title, message)){

                final String fsPath = ModelerSession.getProjectControlService().removeProjectItem(selectedTreePath);

                if(fsPath == null){
                    //should never happened, testing & debugging
                    LOG.error("Removal of project subfolder has failed. Node: " + node.toString());
                } else {
                    //remove the file associated with the project diagram
                    if(!ProjectServiceUtils.deleteFSItem(selectedTreePath.getParentPath(), new File(fsPath).getName())){
                        // file system deletion has failed
                        LOG.error("ProMod couldn't delete selected subfolder from file system: " + fsPath);
                    }
                }
            }

        // show !CLOSE! project dialog
        } else if(ProjectServiceUtils.isProjectRoot(node)){
            final String title = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.project.title");
            final String message = ModelerSession.getCommonResourceBundle().getString("modeler.project.navigation.delete.project.message");

            if(showRemoveConfirmDialog(title, message)){

                final String fsPath = ModelerSession.getProjectControlService().removeProjectItem(selectedTreePath);

                if(fsPath == null){
                    //should never happened, testing & debugging
                    LOG.error("Removal of project has failed.");
                }
            }
        } else {
            LOG.error("A user object of all nodes has to be an instance of ProjectRoot, ProjectSubFolder or ProjectDiagram class.");
        }
        }

    }

    /**
     * Shows the confirm dialog before removing a project item.
     * @param title is the title of the dialog.
     * @param message is the text to be displayed
     * @return true if the user's answer is YES
     */
    private boolean showRemoveConfirmDialog(final String title,
                                     final String message){
            final Object[] options = {
                    ModelerSession.getCommonResourceBundle().getString("modeler.ok"),
                    ModelerSession.getCommonResourceBundle().getString("modeler.cancel")
            };

            int dialogReturnValue = JOptionPane.showOptionDialog(
                    ModelerSession.getFrame(),
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

        return dialogReturnValue == JOptionPane.YES_OPTION;
    }

    /**
     * Initialize the event handling.
     */
    private void initEventHandling() {
        projectTree.addTreeSelectionListener(this);

        //
        ModelerSession.getProjectService().getSelectedItem().addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                TreePath treePath = (TreePath) evt.getNewValue();

                // using JTree#setSelectionPath(null) has no effect only firing valueChange event holding old tree selection value
                // so if null comes, the selection tree path is selected to the tree modelFactory's root node
                if(treePath == null){
                    treePath = new TreePath(ModelerSession.getProjectControlService().getProjectTreeModel().getRoot());    
                }

                projectTree.setSelectionPath(treePath);
            }
        });

        final MenuControlService menuControlService = (MenuControlService) ModelerSession.getMenuService();
        projectTree.addMouseListener(new MousePopup(menuControlService.getProjectTreePopupMenu(), projectTree));

        expandButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                expandAll(true);
            }
        });

        collapseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                expandAll(false);
            }
        });

        Action action = ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_DIAGRAM);
        if(action != null){
            addDiagramButton.setAction(action);
            addDiagramButton.setText("");
        } else {
            LOG.error("Add diagram action is not available.");            
        }

        action = ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_ADD_SUBFOLDER);
        if(action != null){
            addSubfolderButton.setAction(action);
            addSubfolderButton.setText("");
        } else {
            LOG.error("Add subfolder action is not available.");            
        }
    }

    /**
     * Initialize the tree.
     */
    private void initProjectTree() {
        projectTree.setCellRenderer(new ProjectNavigationCellRenderer());

        projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);        

        final TreeModel treeModel = ModelerSession.getProjectControlService().getProjectTreeModel();

        if(treeModel == null){
            LOG.error("TreeModel is null.");
        } else {
            projectTree.setModel(treeModel);
        }

    }

    /**
     * Expands or collapses the entire project tree.
     *
     * @param expand true for expansion, false for collapse
     */
    private void expandAll(boolean expand) {
        final TreeNode root = (TreeNode) projectTree.getModel().getRoot();
        expandAll(new TreePath(root), expand, -1);

        projectTree.expandPath(new TreePath(root));
    }

    /**
     * Expands/collapses project tree navigation subtree that is specified by it's root - parent parameter.
     *
     * @param parent is the root of subtree to be expanded/collapsed
     * @param expand true for expanding, false for collapsing
     * @param depth is depth for expanding, 0 means no expanding, -1 stands for expand all 
     */
    private void expandAll(final TreePath parent, boolean expand, final int depth) {
        final TreeNode node = (TreeNode)parent.getLastPathComponent();

        if(depth == 0){ // depth == 0 -> stop, if depth < 0 -> expand all
            return;
        }

        if (node.getChildCount() >= 0) {
            for (final Enumeration enumeration = node.children(); enumeration.hasMoreElements(); ) {
                final TreeNode treeNode = (TreeNode) enumeration.nextElement();
                final TreePath path = parent.pathByAddingChild(treeNode);

                expandAll(path, expand, depth - 1);
            }
        }

        if (expand) {
            projectTree.expandPath(parent);
        } else {
            projectTree.collapsePath(parent);
        }
    }

    /**
     * Updates the ProjectService data when the project item selection has changed.
     * @param e is the tree selection change
     */
    public void valueChanged(TreeSelectionEvent e) {
        ModelerSession.getProjectControlService().setSelectedItem(e.getPath());
    }

    /**
     * Goes though all projects and all it's diagrams and checks whether the 'changed' flag of this
     * diagrams is not set to true. Then it gives the user list of options what to do.
     *
     * @param itemTreePath is the tree path to the currently tested project navigation tree item
     * @param rootTreePath is the tree path to the the currently tested project
     *
     * @return  CONTINUE if the test is supposed to continue in a ordinary way, STOP if all changes are supposed to
     * be skipped and CANCEL for cancel ProMod existing
     */
    public Modeler.ExitDialogReturnValues checkForUnsavedChanges(final TreePath itemTreePath,
                                                          final TreePath rootTreePath) {

        final Object lastPathComponent = itemTreePath.getLastPathComponent();
        final ProjectRoot projectRoot =
                (ProjectRoot) ((DefaultMutableTreeNode)rootTreePath.getLastPathComponent()).getUserObject();

        if(lastPathComponent != null){
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) lastPathComponent;

            if(treeNode.getUserObject() instanceof ProjectDiagram){
                final ProjectDiagram projectDiagram = (ProjectDiagram) treeNode.getUserObject();

                if(projectDiagram.isChanged()){
                    final int dialogResult = JideOptionPane.showOptionDialog(this,
                            "["+ projectDiagram.getDisplayName() + "] " + Modeler.UNSAVED_CHANGE_LABEL,
                            Modeler.PROMOD_EXIT_LABEL + " - [" + projectRoot.getDisplayName() + "]",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, Modeler.EXIT_DIALOG_OPTIONS, Modeler.EXIT_DIALOG_OPTIONS[0]);

                        switch (dialogResult){
                            case 0: // yes
                                ModelerSession.getProjectControlService().synchronize(
                                    itemTreePath, true, true, false, true
                                );
                                break;
                            case 1: //no
                                break;
                            case 2: //save all
                                LOG.info("Saving all changes in project: " + projectRoot.getDisplayName() + ".");
                                ModelerSession.getProjectControlService().synchronize(
                                        rootTreePath, true, true, false, true
                                );
                                 return Modeler.ExitDialogReturnValues.STOP;
                            case 3: // skip all
                                LOG.info("Skipping all changes in project: " + projectRoot.getDisplayName() + ".");
                                return Modeler.ExitDialogReturnValues.STOP;
                            case 4:
                            case -1: // user clicked the closing cross of the dialog
                                return Modeler.ExitDialogReturnValues.CANCEL;
                        }
                }

            } else {
                for(int i = 0; i < treeNode.getChildCount(); i++){
                    final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) treeNode.getChildAt(i);

                    final Modeler.ExitDialogReturnValues value = checkForUnsavedChanges(itemTreePath.pathByAddingChild(childNode), rootTreePath);

                    switch (value){
                        case CANCEL:
                            return Modeler.ExitDialogReturnValues.CANCEL;
                        case STOP:
                            return Modeler.ExitDialogReturnValues.STOP;
                    }
                }
            }
        }

        return Modeler.ExitDialogReturnValues.CONTINUE;
    }
}