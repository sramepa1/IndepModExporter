package cz.cvut.promod.epc;

import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;
import cz.cvut.promod.epc.ioController.EPCNotationIOController;
import cz.cvut.promod.epc.modelFactory.EPCNotationModelFactory;
import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.epc.settings.EPCSettings;
import cz.cvut.promod.gui.Modeler;
import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.utils.InsertMenuItemResult;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:41:58, 5.12.2009
 *
 * Implementation of EPC modeling notation.
 */
public class EPCNotation implements cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation{

    private static final Logger LOG = Logger.getLogger(EPCNotation.class);

    public static final String EPC_LABEL = Resources.getResources().getString("epc.menu.epc");

    private final LabelStatusBarItem selectedToolStatusBarItem;

    private final EPCNotationModel model;

    private final EPCNotationModelFactory modelFactory;

    private final EPCNotationIOController ioController;

    private final String NOTATION_DESCRIPTION =
            Resources.getResources().getString("epc.description");
    private final String NOTATION_TOOL_TIP =
            Resources.getResources().getString("epc.tool.tip");


    public EPCNotation(final File propertiesFile) throws InstantiationException {
        final Properties properties = new Properties();
        try {
            properties.load(new FileReader(propertiesFile));
        } catch (IOException e) {
            LOG.error("Properties for the EPC Notations couldn't be read.", e);
            throw new InstantiationException("Mandatory properties couldn't be read.");
        }

        final EPCSettings settings = new EPCSettings(properties);
        EPCSettings.setInstance(settings);

        selectedToolStatusBarItem = new LabelStatusBarItem();

        model = new EPCNotationModel(properties, selectedToolStatusBarItem);

        modelFactory = new EPCNotationModelFactory();

        ioController = new EPCNotationIOController(model.getExtension(), getIdentifier());
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
        return Resources.getIcon(Resources.ICONS + Resources.DIAGRAM);
    }

    /** {@inheritDoc} */
    public String getNotationToolTip() {
        return NOTATION_TOOL_TIP;
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
        return NOTATION_DESCRIPTION;
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

    public InsertMenuItemResult addPopupMenuItem(final ProModAction proModAction, final MenuItemPosition menuItemPosition,final MenuService.MenuSeparator menuSeparator, final boolean checkable){
       return model.addPopupMenuAction(proModAction, menuItemPosition, menuSeparator, checkable);
    }

    /** {@inheritDoc} */
    public void init() {
        initActions();

        initMainMenu();

        initToolBar();

        initStatusBar();
    }

    /**
     * Initialize epc notation specific status bar items.
     */
    private void initStatusBar() {
        ModelerSession.getStatusBarService().addStatusBarItem(
                getIdentifier(), selectedToolStatusBarItem, JideBoxLayout.FIX
        );
    }

    private void initToolBar() {
        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(EPCNotationModel.UNDO_ACTION_KEY)
        );

        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(EPCNotationModel.REDO_ACTION_KEY)
        );

        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(EPCNotationModel.SAVE_ACTION_KEY)
        );

        ModelerSession.getToolBarService().addAction(
                getIdentifier(),
                model.getAction(EPCNotationModel.SAVE_ALL_ACTION_KEY)
        );
    }

    /** {@inheritDoc} */
    public void finish() { }

    private void initActions() {
        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.REFRESH_ACTION_KEY),
                model.getAction(EPCNotationModel.REFRESH_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.DELETE_ACTION_KEY),
                model.getAction(EPCNotationModel.DELETE_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.UNDO_ACTION_KEY),
                model.getAction(EPCNotationModel.UNDO_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.REDO_ACTION_KEY),
                model.getAction(EPCNotationModel.REDO_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.SAVE_ACTION_KEY),
                model.getAction(EPCNotationModel.SAVE_ACTION_KEY)
        );

        ModelerSession.getActionService().registerAction(
                getIdentifier(),
                model.getActionIdentifier(EPCNotationModel.SAVE_ALL_ACTION_KEY),
                model.getAction(EPCNotationModel.SAVE_ALL_ACTION_KEY)
        );
    }

    private void initMainMenu() {
        ModelerSession.getMenuService().insertMainMenuItem(
            model.getAction(EPCNotationModel.SAVE_ACTION_KEY),
            new MenuItemPosition(Modeler.FILE_LABEL, MenuItemPosition.PlacementStyle.FIRST)
        );


        ModelerSession.getMenuService().insertMainMenuItem(
            model.getAction(EPCNotationModel.REFRESH_ACTION_KEY),
            new MenuItemPosition(EPC_LABEL)
        );
    }

    /** {@inheritDoc} */
    public List<SettingPageData> getSettingPages() {
        return EPCSettings.getInstance().getSettingPages();
    }
}
