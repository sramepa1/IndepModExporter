package cz.cvut.promod.hierarchyNotation;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;
import cz.cvut.promod.gui.Modeler;
import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.hierarchyNotation.ioController.ProcessHierarchyIOController;
import cz.cvut.promod.hierarchyNotation.modelFactory.ProcessHierarchyModelFactory;
import cz.cvut.promod.hierarchyNotation.resources.Resources;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.utils.InsertMenuItemResult;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;

import javax.swing.*;
import java.util.List;
import java.util.Set;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:30:50, 25.11.2009
 *
 * Implementation of a ProcessHierarchyNotation plugin.
 */
public class ProcessHierarchyNotation implements cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation{

    private final ProcessHierarchyNotationModel model;

    private final ProcessHierarchyModelFactory modelFactory;

    private final ProcessHierarchyIOController ioController;

    private final ValueModel checkTreeStructureModel;

    private static final Icon ICON = Resources.getIcon(Resources.ICONS + Resources.DIAGRAM);

    private final LabelStatusBarItem statusBarItem = ModelerSession.getComponentFactoryService().createLabelStatusBarItem();

    /**
     * Parameter-less constructor.
     */
    public ProcessHierarchyNotation(){
        model = new ProcessHierarchyNotationModel(statusBarItem);

        final PresentationModel<ProcessHierarchyNotationModel> presentation =
                new PresentationModel<ProcessHierarchyNotationModel>(model);

        checkTreeStructureModel = presentation.getModel(ProcessHierarchyNotationModel.CHECK_TREE_STRUCTURE_PROPERTY);

        checkTreeStructureModel.setValue(true); // initialize is this check turned on

        modelFactory = new ProcessHierarchyModelFactory();

        ioController = new ProcessHierarchyIOController(model.getExtension(), getIdentifier());

        initComponents();
    }

    /** {@inheritDoc} */
    public String getName() {
        return model.getName();
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return model.getDescription();
    }

    /** {@inheritDoc} */
    private void initComponents() {
        statusBarItem.setHorizontalAlignment(JLabel.RIGHT);
    }

    /** {@inheritDoc} */
    public Set<DockableFrameData> getDockableFrames() {
        return model.getDockableFrames();
    }

    /** {@inheritDoc} */
    public String getFullName() {
        return model.getFullName();
    }

    /** {@inheritDoc} */
    public String getAbbreviation() {
        return model.getAbbreviation();
    }

    /** {@inheritDoc} */
    public Icon getNotationIcon() {
        return ICON;
    }

    /** {@inheritDoc} */
    public String getNotationToolTip() {
        return "Process Hierarchy.";
    }

    /** {@inheritDoc} */
    public NotationWorkspaceData getNotationWorkspaceData() {
        return model.getWorkspace();
    }

    /** {@inheritDoc} */
    public ImageIcon getNotationPreviewImage() {
        return Resources.getIcon(Resources.ICONS + Resources.PREVIEW);
    }

    /** {@inheritDoc} */
    public String getNotationPreviewText() {
        return "Process Hierarchy Notation is supposed to be used for a process or organization hierarchy modeling. It is not a standard kind of flow-chart. Use the processes and build the hierarchical structure.";
    }

    /** {@inheritDoc} */
    public DiagramModelFactory getDiagramModelFactory() {
        return modelFactory; 
    }

    /** {@inheritDoc} */
    public NotationLocalIOController getLocalIOController() {
        return ioController;
    }

    /** {@inheritDoc} */
    public String getIdentifier() {
        return model.getIdentifier();
    }

    /**
     * Process Hierarchy notation does NOT support popup menu functionality.
     *
     * {@inheritDoc}
     */
     public InsertMenuItemResult addPopupMenuItem(ProModAction proModAction, MenuItemPosition menuItemPosition, MenuService.MenuSeparator menuSeparator, boolean checkable){
        return InsertMenuItemResult.POPUP_NOT_SUPPORTED;
    }

    /** {@inheritDoc} */
    public List<SettingPageData> getSettingPages() {
        return null;
    }

    /** {@inheritDoc} */
    public void init() {
        initActions();

        initToolBar();

        initStatusBar();

        initMainMenu();
    }

    private void initMainMenu() {
        ModelerSession.getMenuService().insertMainMenuItem(
                model.getAction(ProcessHierarchyNotationModel.SAVE_ACTION_KEY),
                new MenuItemPosition(Modeler.FILE_LABEL, MenuItemPosition.PlacementStyle.FIRST
                )
        );

        ModelerSession.getMenuService().insertMainMenuItem(
                model.getAction(ProcessHierarchyNotationModel.REFRESH_ACTION_KEY),
                new MenuItemPosition(ProcessHierarchyNotationModel.PROCESS_HIERARCHY_LABEL)
        );
    }

    private void initStatusBar() {

        ModelerSession.getStatusBarService().addStatusBarItem(
                getIdentifier(), statusBarItem, JideBoxLayout.VARY
        );
    }

    private void initToolBar() {
        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(ProcessHierarchyNotationModel.SAVE_ACTION_KEY)
        );

        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(ProcessHierarchyNotationModel.SAVE_ALL_ACTION_KEY)
        );

        ModelerSession.getToolBarControlService().getCommandBar(getIdentifier()).addSeparator();
        
        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(ProcessHierarchyNotationModel.REFRESH_ACTION_KEY)
        );

        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(ProcessHierarchyNotationModel.DELETE_ACTION_KEY)
        );

        ModelerSession.getToolBarControlService().getCommandBar(getIdentifier()).addSeparator();

        ModelerSession.getToolBarControlService().getCommandBar(getIdentifier()).add(createTreeTestCheckBox());
    }

    private JCheckBox createTreeTestCheckBox() {
        final JCheckBox checkBox = ModelerSession.getComponentFactoryService().createCheckBox();
        checkBox.setText(ProcessHierarchyNotationModel.CHECK_TREE_STRUCTURE_LABEL);

        com.jgoodies.binding.adapter.Bindings.bind(checkBox, checkTreeStructureModel);

        return checkBox;
    }

    private void initActions() {
        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                ProcessHierarchyNotationModel.REFRESH_ACTION_KEY,
                model.getAction(ProcessHierarchyNotationModel.REFRESH_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                ProcessHierarchyNotationModel.DELETE_ACTION_KEY,
                model.getAction(ProcessHierarchyNotationModel.DELETE_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                ProcessHierarchyNotationModel.SAVE_ACTION_KEY,
                model.getAction(ProcessHierarchyNotationModel.SAVE_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                ProcessHierarchyNotationModel.SAVE_ALL_ACTION_KEY,
                model.getAction(ProcessHierarchyNotationModel.SAVE_ALL_ACTION_KEY)
        );
    }

    /** {@inheritDoc} */
    public void finish() { }

    public boolean checkTreeStructure(){
        return (Boolean) checkTreeStructureModel.getValue();
    }

    /**
     * Sets a text to the label status bar item.
     *
     * @param text is the text to be displayed in the status bar
     */
    public void setStatusInfoText(final String text){
        statusBarItem.setText(text);
    }
}
