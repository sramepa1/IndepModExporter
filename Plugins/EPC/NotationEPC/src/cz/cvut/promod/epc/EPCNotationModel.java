package cz.cvut.promod.epc;

import cz.cvut.promod.epc.frames.graphOptions.GraphOptions;
import cz.cvut.promod.epc.frames.toolChooser.ToolChooser;
import cz.cvut.promod.epc.frames.vertexInfo.VertexInfo;
import cz.cvut.promod.epc.modelFactory.diagramModel.EPCDiagramModel;
import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.epc.workspace.EPCWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.MenuControlService;
import cz.cvut.promod.services.menuService.MenuControlServiceImpl;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.utils.*;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import org.apache.log4j.Logger;


import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.util.*;

import com.jidesoft.status.LabelStatusBarItem;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:43:00, 5.12.2009
 *
 * A model component for the EPCNotation plugin.
 */
public class EPCNotationModel {

    // actions
    public static final String SAVE_ACTION_KEY = "epc.action.save";
    public static final String SAVE_ALL_ACTION_KEY = "epc.action.save.all";
    public static final String UNDO_ACTION_KEY = "epc.action.undo";
    public static final String REDO_ACTION_KEY = "epc.action.redo";
    public static final String DELETE_ACTION_KEY = "epc.action.delete";

    // resources
    public static final String REFRESH_ACTION_KEY = "epc.action.refresh";

    // properties
    public static final String IDENTIFIER = "epc.identifier";
    public static final String PLUGIN_NAME = "epc.name";
    public static final String DESCRIPTION = "epc.description";
    public static final String FULL_NAME = "epc.full.name";
    public static final String ABBREVIATION = "epc.abbreviation";
    public static final String EXTENSION = "epc.file.extension";

    private static final Logger LOG = Logger.getLogger(EPCNotationModel.class);

    private final Properties properties;

    private Set<DockableFrameData> dockableFrames;

    private final EPCWorkspaceData workspace;

    private final Map<String, ProModAction> actions;

    private final  JPopupMenu popupMenu = ModelerSession.getComponentFactoryService().createPopupMenu();


    /**
     * Constructs a new EPCNotationModel.
     *
     * @param properties are the required properties
     * @param selectedToolStatusBarItem is the status bat item holding the actual select tool
     * @throws InstantiationException when the initialization fail
     */
    public EPCNotationModel(final Properties properties,
                            final LabelStatusBarItem selectedToolStatusBarItem) throws InstantiationException{
        this.properties = properties;

        actions = new HashMap<String, ProModAction>();

        initActions();

        checkProperties();

        //init dockable frames
        dockableFrames = new HashSet<DockableFrameData>();

        final ToolChooser toolChooser = new ToolChooser(selectedToolStatusBarItem);
        dockableFrames.add(toolChooser);

        final VertexInfo vertexInfo = new VertexInfo();
        dockableFrames.add(vertexInfo);

        final GraphOptions graphOptions = new GraphOptions();
        dockableFrames.add(graphOptions);

        // init workspace data
        workspace = new EPCWorkspaceData(
                toolChooser.getSelectedToolModel(),
                graphOptions.getGridModel(),
                graphOptions.getLockModel(),
                graphOptions.getViewGridModel(),
                graphOptions.getCellSizeModel(),
                graphOptions.getScaleModel(),
                graphOptions.getMovableBelowZeroModel(),
                actions,
                popupMenu
        );

        // frames event handling
        vertexInfo.initCellSelectionListener(workspace.getGraph());
        graphOptions.initEventHandling(actions);

        initPopupMenu();
    }

    private void initActions() {
        actions.put(SAVE_ALL_ACTION_KEY,
            new ProModAction(Resources.getResources().getString(SAVE_ALL_ACTION_KEY), null, null){
                public void actionPerformed(ActionEvent event) {
                    final TreePath treePath = ModelerSession.getProjectService().getSelectedProjectPath();

                    if(treePath != null){
                        ModelerSession.getProjectControlService().synchronize(
                                treePath,
                                true, true, false, false
                        );
                    }
                }
            }
        );

        actions.put(SAVE_ACTION_KEY,
            new ProModAction(Resources.getResources().getString(SAVE_ACTION_KEY), null, null){
                public void actionPerformed(ActionEvent event) {
                    final TreePath treePath = ModelerSession.getProjectService().getSelectedDiagramPath();

                    if(treePath != null){
                        ModelerSession.getProjectControlService().synchronize(
                                treePath,
                                true, true, false, false
                        );
                    }
                }
            }
        );

        actions.put(REDO_ACTION_KEY,
            new ProModAction(Resources.getResources().getString(REDO_ACTION_KEY), null, null){
                public void actionPerformed(ActionEvent event) {
                    final ProjectDiagram diagram = ModelerSession.getProjectService().getSelectedDiagram();

                    if(diagram.getDiagramModel() instanceof EPCDiagramModel){
                        final EPCDiagramModel diagramModel = (EPCDiagramModel) diagram.getDiagramModel();

                        final UndoManager undoManager = diagramModel.getUndoManager();
                        undoManager.redo();
                        setEnabled(undoManager.canRedo());
                        actions.get(UNDO_ACTION_KEY).setEnabled(undoManager.canUndo()); // enables undo action
                        actions.get(SAVE_ACTION_KEY).setEnabled(true);

                    } else {
                        LOG.error("Unable to perform undo action, because of the casting problem.");
                    }
                }
            }
        );

        actions.put(UNDO_ACTION_KEY,
            new ProModAction(Resources.getResources().getString(UNDO_ACTION_KEY), null, null){
                public void actionPerformed(ActionEvent event) {
                    final ProjectDiagram diagram = ModelerSession.getProjectService().getSelectedDiagram();

                    if(diagram.getDiagramModel() instanceof EPCDiagramModel){
                        final EPCDiagramModel diagramModel = (EPCDiagramModel) diagram.getDiagramModel();

                        final UndoManager undoManager = diagramModel.getUndoManager();
                        undoManager.undo();
                        setEnabled(undoManager.canUndo());
                        actions.get(REDO_ACTION_KEY).setEnabled(undoManager.canRedo()); // enables redo action
                        actions.get(SAVE_ACTION_KEY).setEnabled(true);

                    } else {
                        LOG.error("Unable to perform undo action, because of the casting problem.");
                    }
                }
            }
        );
    }

    private void initPopupMenu() {
        final Action deleteAction = getAction(DELETE_ACTION_KEY);
        deleteAction.setEnabled(true);
        popupMenu.add(deleteAction);
    }

    public InsertMenuItemResult addPopupMenuAction(final ProModAction proModAction, final MenuItemPosition menuItemPosition,final MenuService.MenuSeparator menuSeparator, final boolean checkable)  {
        return ModelerSession.getMenuService().insertAction(null, popupMenu, proModAction, menuSeparator, menuItemPosition, checkable);
    }

    private void checkProperties() throws InstantiationException{
        if(!properties.containsKey(EPCNotationModel.FULL_NAME)) {
            LOG.error("Missing property " + EPCNotationModel.FULL_NAME);
            throw new InstantiationException("Missing property " + EPCNotationModel.FULL_NAME);
        }
        if(!properties.containsKey(EPCNotationModel.IDENTIFIER)){
            LOG.error("Missing property " + EPCNotationModel.IDENTIFIER);
            throw new InstantiationException("Missing property " + EPCNotationModel.IDENTIFIER); 
        }
        if(!properties.containsKey(EPCNotationModel.ABBREVIATION)){
            LOG.error("Missing property " + EPCNotationModel.ABBREVIATION);
            throw new InstantiationException("Missing property " + EPCNotationModel.ABBREVIATION);
        }
        if(!properties.containsKey(EPCNotationModel.EXTENSION)){
            LOG.error("Missing property " + EPCNotationModel.EXTENSION);
            throw new InstantiationException("Missing property " + EPCNotationModel.EXTENSION);
        }
        if(!properties.containsKey(REFRESH_ACTION_KEY)){
            LOG.error("Missing property " + EPCNotationModel.REFRESH_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.REFRESH_ACTION_KEY);
        }
        if(!properties.containsKey(DELETE_ACTION_KEY)){
            LOG.error("Missing property " + EPCNotationModel.DELETE_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.DELETE_ACTION_KEY);
        }
        if(!properties.containsKey(UNDO_ACTION_KEY)){
            LOG.error("Missing property " + EPCNotationModel.UNDO_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.UNDO_ACTION_KEY);
        }
        if(!properties.containsKey(REDO_ACTION_KEY)){
            LOG.error("Missing property " + EPCNotationModel.REDO_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.REDO_ACTION_KEY);
        }
        if(!properties.containsKey(SAVE_ACTION_KEY)){
            LOG.error("Missing property " + EPCNotationModel.SAVE_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.SAVE_ACTION_KEY);
        }
        if(!properties.containsKey(SAVE_ALL_ACTION_KEY)){
            LOG.error(EPCNotationModel.SAVE_ALL_ACTION_KEY);
            throw new InstantiationException("Missing property " + EPCNotationModel.SAVE_ALL_ACTION_KEY);
        }
    }

    /**
     * @return plugin's identifier
     */
    public String getIdentifier() {
        return properties.getProperty(EPCNotationModel.IDENTIFIER);
    }

    /**
     * @return notation's name.
     */
    public String getFullName() {
        return properties.getProperty(EPCNotationModel.FULL_NAME);
    }

    /**
     * @return notation's name abbreviation
     */
    public String getAbbreviation() {
        return properties.getProperty(EPCNotationModel.ABBREVIATION);
    }

    /*
     * @return notation's file extension
     */
    public String getExtension() {
        return properties.getProperty(EPCNotationModel.EXTENSION);
    }

    /**
     * @return dockable frames
     */
    public Set<DockableFrameData> getDockableFrames() {
        return dockableFrames;
    }

    /**
     * @return notation's workspace component
     */
    public NotationWorkspaceData getWorkspace() {
        return workspace;
    }

    /**
     * Returns required plugin's action identifier.
     *
     * @param key is the key of the action
     * @return the required action identifier, null if there is no such an action
     */
    public String getActionIdentifier(final String key){
        return properties.getProperty(key);        
    }


    /**
     * Returns required plugin's action.
     *
     * @param key is the key of the action
     * @return the required action, null if there is no such an action
     */
    public ProModAction getAction(final String key){
        if(actions.containsKey(key)){
            return actions.get(key);
        }

        return null;
    }


    /**
     * @return the plugin's name
     */
    public String getName() {
        return properties.getProperty(EPCNotationModel.PLUGIN_NAME);
    }

    /**
     * @return the plugin's description
     */
    public String getDescription() {
        return properties.getProperty(EPCNotationModel.DESCRIPTION);
    }
}
