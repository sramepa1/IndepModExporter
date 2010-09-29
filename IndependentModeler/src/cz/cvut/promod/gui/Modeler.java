package cz.cvut.promod.gui;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.swing.JideButton;
import cz.cvut.promod.actions.LoadDialogAction;
import cz.cvut.promod.gui.dialogs.loadErrors.LoadErrorsDialog;
import cz.cvut.promod.gui.dialogs.newProject.NewProjectDialog;
import cz.cvut.promod.gui.dialogs.pluginsOverview.PluginsOverviewDialogDialog;
import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialog;
import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialogExecutor;
import cz.cvut.promod.gui.listeners.ButtonPopupAdapter;
import cz.cvut.promod.gui.listeners.DockFrameListener;
import cz.cvut.promod.gui.listeners.SideButtonListener;
import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.gui.settings.SettingsDialog;
import cz.cvut.promod.gui.settings.utils.BasicSettingPage;
import cz.cvut.promod.gui.settings.utils.SettingPage;
import cz.cvut.promod.gui.support.utils.DockableFrameWrapper;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.utils.NotationWorkspaceDataDefault;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.workspace.UpdatableWorkspaceComponent;
import cz.cvut.promod.resources.Resources;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.MenuControlService;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import cz.cvut.promod.services.menuService.utils.ModelerMenuItem;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:09:42, 10.10.2009
 */

/**
 * Basic class of the host application.
 */
public class Modeler extends ModelerView{

    private static final Logger LOG = Logger.getLogger(Modeler.class);

    public static final String ACTION_NEW_PROJECT_DIALOG = "modeler.action.new.project";
    public static final String ACTION_EXIT = "modeler.action.exit";
    public static final String ACTION_PROJECT_LOAD = "modeler.action.project.load";
    public static final String ACTION_PROJECT_SAVE = "modeler.action.project.save";
    public static final String ACTION_SWITH_USER = "modeler.action.switch.user";
    public static final String ACTION_PROJECT_NAVIG_SHOW = "modeler.action.navigation";
    public static final String ACTION_SETTINGS_SHOW = "modeler.action.settings";
    public static final String ACTION_PLUGINS_OVERVIEW_SHOW = "modeler.action.plugins.overview";

    public static final String LOAD_SYNC_TITLE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.sync.title");
    public static final String LOAD_SYNC_MESSAGE_ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.sync.message");
    public static final String USER_CHANGE_DIALOG_TITLE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.user.service");
    public static final String USER_CHANGE_DIALOG_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.user.name");
    public static final String OK_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.ok");
    public static final String YES_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.yes");
    public static final String NO_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.no");
    public static final String CANCEL_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.cancel");
    public static final String USER_CHANGE_DIALOG_INVALID_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.user.invalid.name");
    public static final String SAVE_ALL_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.exit.save.all");
    public static final String SKIP_ALL_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.exit.skip.all");
    public static final String PROMOD_EXIT_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.exit");
    public static final String UNSAVED_CHANGE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.exit.diagram.unsaved.change");
    public static final String SETTINGS_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.settings");                
    public static final String FILE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.menu.file");
    public static final String PROJECT_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.menu.project");
    public static final String EDIT_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.menu.edit");
    public static final String WINDOW_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.menu.window");
    public static final String PLUGINS_OVERVIEW_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.plugins.overview");

    public static final Object[] EXIT_DIALOG_OPTIONS = {
            YES_LABEL,NO_LABEL, SAVE_ALL_LABEL, SKIP_ALL_LABEL, CANCEL_LABEL};

    public static enum ExitDialogReturnValues {CONTINUE, STOP, CANCEL}

    private final ModelerModel model;


    public Modeler( ){
        ModelerSession.setFrame(this);
        
        this.model = new ModelerModel();

        initActions();

        initMainMenu();

        initProjectNavigation();

        initEventHandling();
    }

    /**
     *  Initialize actions that belong to the 'virtual' "modeler" notation.
     */
    private void initActions(){
        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_PLUGINS_OVERVIEW_SHOW,
                new ProModAction(PLUGINS_OVERVIEW_LABEL, null, KeyStroke.getKeyStroke(
                        KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        new PluginsOverviewDialogDialog();
                    }
                }
        );

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_PLUGINS_OVERVIEW_SHOW,
                new ProModAction(PLUGINS_OVERVIEW_LABEL, null, KeyStroke.getKeyStroke(
                        KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        new PluginsOverviewDialogDialog();
                    }
                }
        );

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_SETTINGS_SHOW,
                new ProModAction(SETTINGS_LABEL, null, KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        SettingsDialog.showOptionsDialog(model.getSettingPagesModel());
                    }
                }
        );

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_SWITH_USER,
                new ProModAction(USER_CHANGE_DIALOG_LABEL, null, null){
                    public void actionPerformed(ActionEvent event) {
                        new SimpleTextFieldDialog(
                            USER_CHANGE_DIALOG_TITLE_LABEL,
                            USER_CHANGE_DIALOG_LABEL,
                            "",
                            OK_LABEL,
                            CANCEL_LABEL,
                            new SimpleTextFieldDialogExecutor(){
                                public String execute(final String text) {
                                    if(text != null && !text.isEmpty()){
                                        ModelerSession.getUserService().setUser(text);
                                        return null;
                                    }

                                    return USER_CHANGE_DIALOG_INVALID_LABEL;
                                }
                            },
                            ModelerSession.getFrame(),
                            true
                        );
                    }
                });
        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_NEW_PROJECT_DIALOG,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(
                        ACTION_NEW_PROJECT_DIALOG),
                        Resources.getIcon(Resources.MODELER + Resources.NEW_PROJECT_ICON), 
                        KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        new NewProjectDialog();
                    }
                });

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_EXIT,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(ACTION_EXIT), null, null){
                    public void actionPerformed(ActionEvent event) {
                        modelerExit();
                    }
                });

        ModelerSession.getActionService().registerAction(
            ModelerModel.MODELER_IDENTIFIER,
            ACTION_PROJECT_NAVIG_SHOW,
            new ProModAction(ModelerSession.getCommonResourceBundle().getString(ACTION_PROJECT_NAVIG_SHOW), null, null){
               public void actionPerformed(ActionEvent event) {
                    swapProjectNavigationVisibility();

                    // update control components selection
                    putValue(Action.SELECTED_KEY, projectNavigationDockableFrame.isAvailable());
                    projectNavigatorButton.setSelected(projectNavigationDockableFrame.isAvailable());
                }
            }
        );


        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_PROJECT_SAVE,
                new ProModAction(ModelerSession.getCommonResourceBundle().getString(ACTION_PROJECT_SAVE),
                        Resources.getIcon(Resources.MODELER + Resources.SAVE_ALL_ICON),
                        KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK)){
                    public void actionPerformed(ActionEvent event) {
                        LOG.debug("Action save invoked.");

                        final TreePath projectTreePath = ModelerSession.getProjectService().getSelectedProjectPath();

                        if(projectTreePath == null){

                        } else {
                            final boolean syncResult = ModelerSession.getProjectControlService().synchronize(
                                    projectTreePath, true, true, false, false);

                            if (!syncResult){
                                JOptionPane.showMessageDialog(
                                        ModelerSession.getFrame(),
                                        LOAD_SYNC_MESSAGE_ERROR_LABEL,
                                        LOAD_SYNC_TITLE_ERROR_LABEL,
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

        ModelerSession.getActionService().registerAction(
                ModelerModel.MODELER_IDENTIFIER,
                ACTION_PROJECT_LOAD,
                new LoadDialogAction(ModelerSession.getCommonResourceBundle().getString(
                        ACTION_PROJECT_LOAD),
                        Resources.getIcon(Resources.MODELER + Resources.OPEN_ICON),
                        KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)));
    }

    /**
     * Changed the project navigation visibility.
     */
    private void swapProjectNavigationVisibility(){
        if( projectNavigationDockableFrame.isAvailable() ){
            dockingManager.setFrameUnavailable(projectNavigationDockableFrame.getName());
        } else{
            final String currentNotation = ModelerSession.getProjectService().getSelectedProjectNotationIndentifier();

            final NotationGuiHolder specification = model.getNotationGuiHolder(currentNotation);

            if( specification != null ){
                final List<DockableFrame> visibleDockableFrames = new LinkedList<DockableFrame>();

                for( final DockableFrame dockableFrame : specification.getListOfFrames(NotationGuiHolder.Position.LEFT) ){
                    if( dockableFrame.isAvailable() ){
                        visibleDockableFrames.add((dockableFrame));
                        specification.getButton(dockableFrame.getName(), NotationGuiHolder.Position.LEFT).doClick();
                    }
                }

                dockingManager.setFrameAvailable(projectNavigationDockableFrame.getName());

                for( final DockableFrame dockableFrame : visibleDockableFrames ){
                    specification.getButton(dockableFrame.getName(), NotationGuiHolder.Position.LEFT).doClick();
                }
            } else{
                dockingManager.setFrameAvailable(projectNavigationDockableFrame.getName());
            }
        }
    }

    /**
     * Performs the terminating sequence.
     */
    private void modelerExit() {
        LOG.info("Modeler is shutting down.");

        if(!checkForUnsavedChanges()){
            LOG.info("ProMod shut-down has been canceled by user");
            return;
        }

        setVisible(false);

        // finishing notations & modules
        for(final String notationIdentifier : ModelerSession.getNotationService().getNotationsIdentifiers()){
            final NotationSpecificPlugins notationSpecificPlugins = ModelerSession.getNotationService().getNotationSpecificPlugins(notationIdentifier);

            notationSpecificPlugins.getNotation().finish();

            for(final Module module : notationSpecificPlugins.getModules()){
                module.finish();
            }
        }

        // finishing extensions
        for(Extension extension : ModelerSession.getExtensionService().getExtensions()){
            extension.finish();
        }

        dispose();
        System.exit(0);
    }

    /**
     * Looks for unsaved changes in projects.
     *
     * @return true if ProMod is supposed to be terminated, false if the ProMod termination has been canceled
     */
    private boolean checkForUnsavedChanges() {
        for(final TreePath projectTreePath : ModelerSession.getProjectService().getProjectPaths()){
            if(ExitDialogReturnValues.CANCEL.equals(
                    projectNavigationDockableFrame.checkForUnsavedChanges(projectTreePath, projectTreePath)
            )){
                // ProMod termination has been canceled by user
                return false;
            }                                                                   
        }

        return true;
    }

    /**
     * Performs the context switching mechanism.
     */
    private void updateView(){
        String newNotationIdentifier = ModelerSession.getProjectService().getSelectedProjectNotationIndentifier();
        final String oldNotationIdentifier = model.getSelectedNotation();

        //if no diagram is selected, use default modeler view
        if(newNotationIdentifier == null){
            newNotationIdentifier = ModelerModel.MODELER_IDENTIFIER;
        }

        updateProjectDiagram(false); // order of over method invocation with respect to possible automatic sync. mechanisms
        updateWorkspaceComponent(oldNotationIdentifier, false);

        if(!newNotationIdentifier.equals(oldNotationIdentifier)){
            updateModelerView(newNotationIdentifier);
            ModelerSession.getActionControlService().updateActionsVisibility(newNotationIdentifier);
            updateMainMenusVisibility(newNotationIdentifier);
            updateDockableFramesVisibility(newNotationIdentifier, oldNotationIdentifier);
        }

        updateWorkspaceComponent(newNotationIdentifier, true); // order of over method invocation with respect to possible automatic sync. mechanisms
        updateProjectDiagram(true);

        model.setSelectedNotation(newNotationIdentifier);
    }

    /**
     * Updates the project diagram.
     *
     * @param update if true, update will be performed, otherwise over will be performed
     */
    private void updateProjectDiagram(final boolean update) {
        if(ModelerSession.getProjectService().getSelectedDiagram() != null){
            if(update){
                ModelerSession.getProjectService().getSelectedDiagram().getDiagramModel().update();                
            } else {
                ModelerSession.getProjectService().getSelectedDiagram().getDiagramModel().over();
            }
        }
    }

    /**
     * Updates the main menu items visibility.
     *
     * @param newNotationIdentifier is the identifier of active notation
     */
    private void updateMainMenusVisibility(final String newNotationIdentifier) {
       final JMenuBar menuBar = getJMenuBar();

        if(menuBar != null){

           for(int i = 0; i < menuBar.getMenuCount(); i++){
                final JMenuItem menuItem = menuBar.getMenu(i);
                boolean isVisible = updateMenuItemVisibility(menuItem, newNotationIdentifier);

                if(isVisible){
                    menuItem.setEnabled(true);
                } else {
                    menuItem.setEnabled(false);
                }
           }

       }
    }

    /**
     * Recursively goes through the menu structure and ensures that only items related to the currently selected notation
     * will be enabled.
     *
     * @param parentMenuItem is the menu where the recursion begins
     * @param newNotationIdentifier is the identifier of selected notation
     *
     * @return true if the parentMenuItem is supposed to be disabled, false otherwise
     */
    private boolean updateMenuItemVisibility(final JMenuItem parentMenuItem, final String newNotationIdentifier) {
        boolean isEnabled = false;

        parentMenuItem.setVisible(true); // always start with all items visible

        if(parentMenuItem instanceof JMenu){

            final JMenu menu = (JMenu) parentMenuItem;
            for(int i = 0; i < menu.getItemCount(); i++){
                final JMenuItem menuItem = menu.getItem(i);

                if(menuItem != null){ //separator is returned like a null item
                    isEnabled |= updateMenuItemVisibility(menuItem, newNotationIdentifier);
                }
            }

            menu.setEnabled(isEnabled);
            return isEnabled;
        }

        // menu item, hide relatives (menu items with the same text)
        final boolean menuItemEnabled = parentMenuItem.isEnabled();

        if(parentMenuItem instanceof ModelerMenuItem && !menuItemEnabled){
            final ModelerMenuItem menuItem = (ModelerMenuItem) parentMenuItem;

            final List<ModelerMenuItem> relatives = menuItem.getListOfRelatives();
            if(relatives != null){
                if(relatives.isEmpty()){
                    parentMenuItem.setVisible(false);
                } else {
                    if(isAnotherRelativeVisible(relatives)){
                        parentMenuItem.setVisible(false);
                    }
                }
            }
        }

        return menuItemEnabled;
    }

    /**
     * Checks the list of relatives (relative are items in the menu under the same root with the same text on it) and
     * decides whether this any of relative items is visible or not.
     *
     * @param relatives is the list of relative items
     * @return true if any another relative item is visible, false otherwise
     */
    private boolean isAnotherRelativeVisible(final List<ModelerMenuItem> relatives) {
        if(relatives == null || relatives.isEmpty()){
            return false;
        }

        for(ModelerMenuItem modelerMenuItem : relatives){
            if(modelerMenuItem instanceof JMenuItem){
                final JMenuItem menuItem = (JMenuItem) modelerMenuItem;
                if(menuItem.isEnabled()){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Performs the context switch.
     *
     * @param newNotationIdentifier is the identifier of active notation.
     */
    private void updateModelerView(final String newNotationIdentifier) {
        if(newNotationIdentifier != null){
            cardLayoutLeftSidePane.show(leftSidePane, newNotationIdentifier);
            cardLayoutRightSidePane.show(rightSidePane, newNotationIdentifier);
            cardLayoutBottomSidePane.show(bottomSidePane, newNotationIdentifier);
            cardLayoutTopSidePane.show(topSidePane, newNotationIdentifier);

            cardLayoutWorkspacePane.show(workspacePane, newNotationIdentifier);

            cardLayoutToolBar.show(toolBarPane, newNotationIdentifier);

            cardLayoutStatusBar.show(statusBarPane, newNotationIdentifier);
            
        } else {
            LOG.error("Modeler view update error. Notation name cannot be null.");
        }
    }

    /**
     * Performs the update() or ever() methods on workspace component if this component implements the
     * UpdatableWorkspaceComponent interface. 
     *
     * @param notationIdentifier is the identifier of the notation to which the workspace component belongs
     * @param update is true when update() method is supposed to be invoked, the over() methods is invoked otherwise
     */
    private void updateWorkspaceComponent(final String notationIdentifier, final boolean update) {
        if(notationIdentifier != null && !ModelerModel.MODELER_IDENTIFIER.equals(notationIdentifier)){
            final JComponent workspaceComponent;
            try{
                workspaceComponent = model.getNotationGuiHolder(notationIdentifier).getNotationWorkspace().getWorkspaceComponentSingleton();

                if(workspaceComponent instanceof UpdatableWorkspaceComponent){
                    final UpdatableWorkspaceComponent updatableWorkspaceComponent = (UpdatableWorkspaceComponent)workspaceComponent;

                    if(update){
                        updatableWorkspaceComponent.update();
                    } else {
                        ModelerSession.clearFrameTitleText();
                        updatableWorkspaceComponent.over();
                    }
                }

            } catch (NullPointerException exception){
                LOG.error("Not workspace provided, notationIdentifier: " + notationIdentifier);
            }
        }
    }

    /**
     * Updates the dockable frame visibility.
     *
     * @param newNotationIdentifier is the identifier of active notation
     * @param oldNotationIdentifier is the identifier of previous notation.
     */
    private void updateDockableFramesVisibility(final String newNotationIdentifier, final String oldNotationIdentifier) {
        if(newNotationIdentifier == null || newNotationIdentifier.equals(oldNotationIdentifier)){
            // should never happened
            LOG.debug("UpdateDockableFramesVisibility method has been invoked, even though newNotationIdentifier is the same as oldNotationIdentifier.");            
        }

        if(!ModelerModel.MODELER_IDENTIFIER.equals(oldNotationIdentifier) && oldNotationIdentifier != null){
            model.getNotationGuiHolder(oldNotationIdentifier).unable();
        }

        if(!ModelerModel.MODELER_IDENTIFIER.equals(newNotationIdentifier)){
            model.getNotationGuiHolder(newNotationIdentifier).enable();
        }
    }

    /**
     * Initialize main menu.
     */
    private void initMainMenu() {
        final MenuControlService mainMenuControlService;
        try{
            mainMenuControlService = (MenuControlService) ModelerSession.getMenuService();
        } catch(ClassCastException exception){
            LOG.error("Main menu initialization not possible.", exception);
            return;
        }

        setJMenuBar(mainMenuControlService.getMenuBar());

        // insert common menu items
        // 'file' menu
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_EXIT),
                new MenuItemPosition(FILE_LABEL), MenuService.MenuSeparator.BEFORE
        );

        // 'project' menu
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_NEW_PROJECT_DIALOG),
                new MenuItemPosition(PROJECT_LABEL, MenuItemPosition.PlacementStyle.FIRST)
        );

        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_PROJECT_SAVE),
                new MenuItemPosition(PROJECT_LABEL), MenuService.MenuSeparator.BEFORE
        );
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_PROJECT_LOAD),
                new MenuItemPosition(PROJECT_LABEL)
        );

        // 'edit' Menu
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_SETTINGS_SHOW),
                new MenuItemPosition(EDIT_LABEL)
        );

        // 'window' menu
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_PROJECT_NAVIG_SHOW),
                new MenuItemPosition(WINDOW_LABEL), true
        );
        ModelerSession.getMenuService().insertMainMenuItem(ModelerSession.getActionService().getAction(
                ModelerModel.MODELER_IDENTIFIER, ACTION_PLUGINS_OVERVIEW_SHOW),
                new MenuItemPosition(WINDOW_LABEL)
        );
    }

    /**
     * Initialize the event handling.
     */
    private void initEventHandling(){
        ModelerSession.getUserService().getUserValueModel().addValueChangeListener(
                new PropertyChangeListener(){
                    public void propertyChange(PropertyChangeEvent evt) {
                        loggedUserLabel.setText(ModelerSession.getUserService().getUser());
                    }
                }
        );

        switchUserButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_SWITH_USER).actionPerformed(null);
            }
        });

        ModelerSession.getProjectService().getSelectedItem().addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                    updateView();
            }
        });

        // listener of window's closing button (cross button)
        this.addWindowListener(new WindowAdapter(){
             @Override
             public void windowClosing(WindowEvent e){
                 
                ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_EXIT).actionPerformed(null);
             }
        });

        projectNavigatorButton.addActionListener(ModelerSession.getActionService().getAction(ModelerModel.MODELER_IDENTIFIER, ACTION_PROJECT_NAVIG_SHOW));
    }

    /**
     * Installs the plugins into the gui.
     */
    public void deployNotationSpecificPlugIns( ){
        for(final String notationIdentifier : ModelerSession.getNotationService().getNotationsIdentifiers()){
            final NotationSpecificPlugins notationSpecificPlugins = ModelerSession.getNotationService().getNotationSpecificPlugins(notationIdentifier);
            
            installNotationsAndModules(notationSpecificPlugins);
        }
    }

    /**
     * Shows the application frame window.
     */
    public void showFrame(){
        // update view to initial state
        updateView();        

        dockingManager.loadLayoutData();

        dockingManager.setFrameUnavailable(projectNavigationDockableFrame.getName());

        for( final NotationGuiHolder notationGuiHolder : model.getNotationGuiHolders() ){
            for( final DockableFrameWrapper wrapper : notationGuiHolder.getAllDockableFrameFacades() ){
                dockingManager.setFrameAvailable(wrapper.getDockableFrame().getName());
                dockingManager.setFrameUnavailable(wrapper.getDockableFrame().getName());
            }
        }

        setVisible(true);

        projectNavigatorButton.doClick();

        showLoadingErrors();
    }

    /**
     * Decides whether there were any errors during plugins loading and if there were any, then show a visual report
     * about these errors.
     */
    private void showLoadingErrors(){
        final List<PluginLoadErrors> errors = ModelerSession.getNotationService().getErrors();

        if(errors == null){
            LOG.error("Nullary loading error info.");
            return;
        }

        if(!errors.isEmpty()){
            new LoadErrorsDialog(errors);        
        }
    }

    /**
     * Installs the notations ad modules.
     *
     * @param notationSpecificPlugins is the holder of notations and it's modules
     */
    private void installNotationsAndModules(final NotationSpecificPlugins notationSpecificPlugins ){
        final NotationGuiHolder notationGuiHolder = new NotationGuiHolder(
                notationSpecificPlugins.getNotation().getIdentifier(),
                dockingManager
        );

        model.addNotationGuiHolder(notationGuiHolder);

        final Collection<DockableFrameData> dockableFramesData = notationSpecificPlugins.getDockableFramesData();

        if( dockableFramesData != null ){
            for( final DockableFrameData item : dockableFramesData ){

                final DockableFrame frame = createDockableFrame(
                        item.getDockableFrameComponent(),
                        item.getInitialPosition(),
                        item.getDockableFrameTitle(),
                        item.isMaximizable(),
                        item.getInitialState());

                final JMenu menu = ModelerSession.getComponentFactoryService().createMenu(
                        ModelerSession.getCommonResourceBundle().getString("modeler.dockable.frame.move"));

                final JMenuItem moveToLeftMenuItem = ModelerSession.getComponentFactoryService().createMenuItem(
                        ModelerSession.getCommonResourceBundle().getString("modeler.dockable.frame.move.left"));
                moveToLeftMenuItem.addActionListener(new MenuItemMoveListener(notationGuiHolder, frame, NotationGuiHolder.Position.LEFT));

                final JMenuItem moveToRightMenuItem = ModelerSession.getComponentFactoryService().createMenuItem(
                        ModelerSession.getCommonResourceBundle().getString("modeler.dockable.frame.move.right"));
                moveToRightMenuItem.addActionListener(new MenuItemMoveListener(notationGuiHolder, frame, NotationGuiHolder.Position.RIGHT));

                final JMenuItem moveToTopMenuItem = ModelerSession.getComponentFactoryService().createMenuItem(
                        ModelerSession.getCommonResourceBundle().getString("modeler.dockable.frame.move.top"));
                moveToTopMenuItem.addActionListener(new MenuItemMoveListener(notationGuiHolder, frame, NotationGuiHolder.Position.TOP));

                final JMenuItem moveToBottomMenuItem = ModelerSession.getComponentFactoryService().createMenuItem(
                        ModelerSession.getCommonResourceBundle().getString("modeler.dockable.frame.move.bottom"));
                moveToBottomMenuItem.addActionListener(new MenuItemMoveListener(notationGuiHolder, frame, NotationGuiHolder.Position.BOTTOM));
                
                final JPopupMenu popupMenu = ModelerSession.getComponentFactoryService().createPopupMenu();

                if(item.getAllowedDockableFramePositions() != null){
                    boolean insertMenu = false;

                    for(final NotationGuiHolder.Position position : NotationGuiHolder.Position.values()){
                        if(item.getAllowedDockableFramePositions().contains(position)){
                            switch (position){
                                case LEFT:
                                    menu.add(moveToLeftMenuItem);
                                    insertMenu = true;
                                    break;
                                case RIGHT:
                                    menu.add(moveToRightMenuItem);
                                    insertMenu = true;
                                    break;
                                case TOP:
                                    menu.add(moveToTopMenuItem);
                                    insertMenu = true;
                                    break;
                                case BOTTOM:
                                    menu.add(moveToBottomMenuItem);
                                    insertMenu = true;
                                    break;
                                default:
                                    LOG.error("No such a choice for allowed position of dockable frame.");
                            }
                        }
                    }

                    if(insertMenu){
                        popupMenu.add(menu);
                    }
                }

                final JideButton button;
                switch (item.getInitialPosition()){
                    case BOTTOM:
                    case TOP:    
                        button = ModelerSession.getComponentFactoryService().createJideButton(item.getDockableFrameTitle(),
                                                                                                        item.getButtonIcon(),
                                                                                                        0);
                        break;
                    default:
                        // case LEFT:
                        // case RIGHT:
                        button = ModelerSession.getComponentFactoryService().createJideButton(item.getDockableFrameTitle(),
                                                                                                        item.getButtonIcon(),
                                                                                                        1);
                }

                button.addActionListener(new SideButtonListener(notationGuiHolder, dockingManager, frame));
                button.addMouseListener(new ButtonPopupAdapter(button, popupMenu));

                frame.addDockableFrameListener(new DockFrameListener(notationGuiHolder, dockingManager));

                notationGuiHolder.addFrameWithButton(
                        frame,
                        button,
                        popupMenu,
                        moveToLeftMenuItem,
                        moveToRightMenuItem,
                        moveToBottomMenuItem,
                        moveToTopMenuItem,
                        item.getInitialPosition(),
                        item.getInitialState());
            }
        }

        final String notationIdentifier = notationSpecificPlugins.getNotation().getIdentifier();

        // insert notation workspace
        if( notationSpecificPlugins.getNotation().getNotationWorkspaceData() != null ){
            notationGuiHolder.setNotationWorkspace(notationSpecificPlugins.getNotation().getNotationWorkspaceData());
        } else {
            notationGuiHolder.setNotationWorkspace(new NotationWorkspaceDataDefault(notationIdentifier));

            LOG.error("No workspace definition provided. Default panel has been inserted instead. " +
                        "Notation identifier: " + notationIdentifier);
        }

        // insert notation specific tool bar
        addToolBarPane(notationIdentifier, ModelerSession.getToolBarControlService().getCommandBar(notationIdentifier));

        // insert notation specific status bar
        addStatusBar(notationIdentifier, ModelerSession.getStatusBarControlService().getStatusBar(notationIdentifier));

        deployWorkspace(notationIdentifier);
        deployButtons(notationIdentifier);
    }

    /**
     * Installs the workspace component.
     *
     * @param notationIdentifier is the identifier of associated notation
     */
    private void deployWorkspace( final String notationIdentifier ){
        final NotationWorkspaceData notationWorkspaceData = model.getNotationGuiHolder(notationIdentifier).getNotationWorkspace();

        final JComponent workspaceComponent = notationWorkspaceData.getWorkspaceComponentSingleton();

        if(!(workspaceComponent instanceof UpdatableWorkspaceComponent)){
            LOG.warn("The obtained workspace component for notation (" + notationIdentifier + ") doesn't implement the UpdatableWorkspaceComponent inytercae. " +
                    "Any methods of this interface won't be never invoked for this workspace component.");
        }

        addMainWindowPane(notationIdentifier, workspaceComponent);
    }

    /**
     * Deploys buttons.
     *
     * @param notationIdentifier is the notation identifier.
     */
    private void deployButtons( final String notationIdentifier ){
        final NotationGuiHolder spec = model.getNotationGuiHolder(notationIdentifier);

        addSidePane(notationIdentifier, rightSidePane, spec.getListOfButtons(
              NotationGuiHolder.Position.RIGHT), true);

        addSidePane(notationIdentifier, leftSidePane, spec.getListOfButtons(
              NotationGuiHolder.Position.LEFT), true);

        addSidePane(notationIdentifier, bottomSidePane, spec.getListOfButtons(
              NotationGuiHolder.Position.BOTTOM), false);

        addSidePane(notationIdentifier, topSidePane, spec.getListOfButtons(
              NotationGuiHolder.Position.TOP), false);        
    }

    /**
     *  Initializes the common settings dialog.
     */
    public void initSettingsDialog() {
        LOG.info("Initializing settings dialog.");

        // init notation's settings screens
        for(final Notation notation : ModelerSession.getNotationService().getNotations()){
            final NotationSpecificPlugins notationSpecificPlugins =
                    ModelerSession.getNotationService().getNotationSpecificPlugins(notation.getIdentifier());

            List<SettingPageData> settingInfo = notationSpecificPlugins.getNotation().getSettingPages();
            List<AbstractDialogPage> pages = new LinkedList<AbstractDialogPage>();
            pages.add(0, new BasicSettingPage(notation.getFullName()));
            final AbstractDialogPage parentPage = pages.get(0);

            if(settingInfo == null){
                LOG.info("Notation " + notation.getIdentifier() + " provides nullary setting pages list.");
            }

            this.createPages(pages, settingInfo, parentPage);

            model.addSettingPages(pages);

            // init module's settings screens
            for(final Module module : notationSpecificPlugins.getModules()){
                settingInfo = module.getSettingPages();
                pages = new LinkedList<AbstractDialogPage>();

                if(settingInfo != null){
                    this.createPages(pages, settingInfo, parentPage);
                    model.addSettingPages(pages);
                } else {
                    LOG.info("Module " + module.getIdentifier() + " provides nullary setting pages list.");
                }
            }
        }

        // init extension's settings screens
        for(final Extension extension : ModelerSession.getExtensionService().getExtensions()){
            List<SettingPageData> settingInfo = extension.getSettingPages();
            List<AbstractDialogPage> pages = new LinkedList<AbstractDialogPage>();

            pages.add(0, new BasicSettingPage(extension.getName()));
            final AbstractDialogPage parentPage = pages.get(0);

            if(settingInfo == null){
                LOG.info("Extension " + extension.getIdentifier() + " provides nullary setting pages list.");
            }
            this.createPages(pages, settingInfo, parentPage);

            model.addSettingPages(pages);
        }
    }

    /**
     * This method creates AbstractDialogPage instances based on SettingPageData instances
     * @param destination Output list where will be new AbstractDialogPage instances inserted
     * @param settingData SettingPageData instances
     * @param parent Parent of SettingPageData instances - can be set to null
     */
    private void createPages(List<AbstractDialogPage> destination, List<SettingPageData> settingData, AbstractDialogPage parent) {
        if (settingData == null) {
            return;
        }

        for (SettingPageData pageData : settingData) {
            this.createPages(destination, pageData, parent);
        }
    }

    /**
     * This method creates AbstractDialogPage instances based on SettingPageData instance
     * @param destination Output list where will be new AbstractDialogPage instance inserted
     * @param settingData SettingPageData instance
     * @param parent parent of the settingData
     */
    private void createPages(List<AbstractDialogPage> destination, SettingPageData settingData, AbstractDialogPage parent) {
        SettingPage page = new SettingPage(settingData);
        if (parent != null) {
            page.setParentPage(parent);
        }
        destination.add(page);
        List<SettingPageData> children = settingData.getChildren();
        for (SettingPageData child : children) {
            this.createPages(destination, child, page);
        }
    }

    /**
     * Performs dockable frame moves.
     */
    private class MenuItemMoveListener implements ActionListener{

        private final NotationGuiHolder notationGuiHolder;
        private final DockableFrame dockableFrame;
        private final NotationGuiHolder.Position directionOfMovement;

        public MenuItemMoveListener( final NotationGuiHolder notationGuiHolder,
                                     final DockableFrame correspondingFrame,
                                     final NotationGuiHolder.Position directionOfMovement){
            this.notationGuiHolder = notationGuiHolder;
            this.dockableFrame = correspondingFrame;
            this.directionOfMovement = directionOfMovement;
        }

        public void actionPerformed( ActionEvent e ){
            try{
                final String notationIdentifier = notationGuiHolder.getNotationIdentifier();
                final String dockableFrameName = dockableFrame.getName();

                NotationGuiHolder.Position oldPosition = notationGuiHolder.getPosition(dockableFrameName);     

                notationGuiHolder.moveButtonAndFrame(dockableFrameName, directionOfMovement);

                final JPanel oldPanel;
                final CardLayout oldCardLayout;
                boolean isVertical;
                switch (oldPosition){
                    case LEFT:
                        oldPanel = leftSidePane;
                        oldCardLayout = cardLayoutLeftSidePane;
                        isVertical = true;
                        break;
                    case RIGHT:
                        oldPanel = rightSidePane;
                        oldCardLayout = cardLayoutRightSidePane;
                        isVertical = true;
                        break;
                    case TOP:
                        oldPanel = topSidePane;
                        oldCardLayout = cardLayoutTopSidePane;
                        isVertical = false;
                        break;
                    case BOTTOM:
                        oldPanel = bottomSidePane;
                        oldCardLayout = cardLayoutBottomSidePane;
                        isVertical = false;
                        break;
                    default:
                        oldPanel = null;
                        isVertical = true;
                        oldCardLayout = null;
                        LOG.error("No such a position for dockable frame.");
                }

                addSidePane(notationIdentifier, oldPanel, notationGuiHolder.getListOfButtons(oldPosition), isVertical);
                oldCardLayout.show(oldPanel, notationIdentifier);


                final JPanel newPanel;
                final NotationGuiHolder.Position newPosition;
                final CardLayout newCardLayout;
                switch(directionOfMovement){
                    case LEFT:
                        newPanel = leftSidePane;
                        newPosition = NotationGuiHolder.Position.LEFT;
                        isVertical = true;
                        newCardLayout = cardLayoutLeftSidePane;
                        break;
                    case RIGHT:
                        newPanel = rightSidePane;
                        newPosition = NotationGuiHolder.Position.RIGHT;
                        isVertical = true;
                        newCardLayout = cardLayoutRightSidePane;
                        break;
                    case BOTTOM:
                        newPanel = bottomSidePane;
                        newPosition = NotationGuiHolder.Position.BOTTOM;
                        isVertical = false;
                        newCardLayout = cardLayoutBottomSidePane;
                        break;
                    case TOP:
                        newPanel = topSidePane;
                        newPosition = NotationGuiHolder.Position.TOP;
                        isVertical = false;
                        newCardLayout = cardLayoutTopSidePane;
                        break;
                    default:
                        newPanel = null;
                        newPosition = null;
                        isVertical = true;
                        newCardLayout = null;
                        LOG.error("No such a position for dockable frame.");
                }

                addSidePane(notationIdentifier, newPanel, notationGuiHolder.getListOfButtons(newPosition), isVertical);
                newCardLayout.show(newPanel, notationIdentifier);
               
            } catch(IllegalArgumentException exception){
                LOG.error("Illegal position or unknown frame during low level navigation movement.", exception);
            }
        }

    }

}
