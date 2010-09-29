package cz.cvut.promod.hierarchyNotation;

import com.jgoodies.binding.beans.Model;
import com.jidesoft.status.LabelStatusBarItem;
import cz.cvut.promod.hierarchyNotation.frames.jgraphOptions.GraphOptions;
import cz.cvut.promod.hierarchyNotation.frames.toolChooser.ToolChooser;
import cz.cvut.promod.hierarchyNotation.resources.Resources;
import cz.cvut.promod.hierarchyNotation.workspace.ProcessHierarchyWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:33:41, 25.11.2009
 *
 * Model component for a ProcessHierarchyNotation plugin.
 */
public class ProcessHierarchyNotationModel extends Model {

    public static final String CHECK_TREE_STRUCTURE_LABEL =
            Resources.getResources().getString("hierarchy.tree.structure.check");
    public static final String PROCESS_HIERARCHY_LABEL =
            Resources.getResources().getString("hierarchy.menu.label");               

    public static final String DELETE_ACTION_KEY = "hierarchy.action.delete";
    public static final String REFRESH_ACTION_KEY = "hierarchy.action.refresh";
    public static final String SAVE_ACTION_KEY = "hierarchy.action.save";
    public static final String SAVE_ALL_ACTION_KEY = "hierarchy.action.save.all";

    public static final String IDENTIFIER = "24fd408b-3c50-4db5-a667-a106e56dbe6d";
    public static final String FULL_NAME = "Process Hierarchy";
    public static final String ABBREVIATION = "PH";
    public static final String EXTENSION = "phn";

    public static final String DESCRIPTION = "An implementation of the Process Hierarchy Notation.";
    public static final String NAME = "ProcessHierarchyNotation";

    private Set<DockableFrameData> dockableFrames;

    private final ProcessHierarchyWorkspaceData workspace;

    private final Map<String, ProModAction> actions;

    public static final String CHECK_TREE_STRUCTURE_PROPERTY = "checkTreeStructure";
    private boolean checkTreeStructure;

    public static final String FRAME_OPTION_NAME_DEFAULT = "ph.frame.option.default";
    public static final String FRAME_TOOLS_NAME_DEFAULT = "ph.frame.tools.default";


    public ProcessHierarchyNotationModel(final LabelStatusBarItem statusBarItem){
        actions = new HashMap<String, ProModAction>();

        //init dockable frames
        dockableFrames = new HashSet<DockableFrameData>();

        final GraphOptions graphOptions = new GraphOptions(FRAME_OPTION_NAME_DEFAULT);

        dockableFrames.add(graphOptions);

        final ToolChooser toolChooser = new ToolChooser(FRAME_TOOLS_NAME_DEFAULT);
        dockableFrames.add(toolChooser);

        // init workspace data
        workspace = new ProcessHierarchyWorkspaceData(
                graphOptions.getGridModel(),
                graphOptions.getViewGridModel(),
                graphOptions.getCellSizeModel(),
                graphOptions.getScaleModel(),
                toolChooser.getSelectedToolModel(),
                actions,
                statusBarItem
        );

        initActions();
    }

    private void initActions() {
        actions.put(SAVE_ALL_ACTION_KEY,
            new ProModAction(Resources.getResources().getString(SAVE_ALL_ACTION_KEY),
                    Resources.getIcon(Resources.ICONS + Resources.SAVE_ALL), null){
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
            new ProModAction(Resources.getResources().getString(SAVE_ACTION_KEY),
                    Resources.getIcon(Resources.ICONS + Resources.SAVE), null){
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
    }

    /**
     * Returns plugin's dockable frames.
     * @return dockable frames
     */
    public Set<DockableFrameData> getDockableFrames() {
        return dockableFrames;
    }

    /**
     * Returns notation's workspace component.
     * @return workspace component
     */
    public ProcessHierarchyWorkspaceData getWorkspace() {
        return workspace;
    }

    /**
     * Returns the notation's file extension.
     *
     * @return notation's file extension.
     */
    public String getExtension() {
        return EXTENSION;
    }

    /**
     * Returns the plugin's identifier.
     *
     * @return plugin's identifier
     */
    public String getIdentifier() {
        return  IDENTIFIER;
    }

    /**
     * Returns the notation's name.
     * @return notation's name
     */
    public String getFullName() {
        return FULL_NAME;
    }

    /**
     * Returns the notation's name abbreviation.
     *
     * @return notation's name abbreviation
     */
    public String getAbbreviation() {
        return ABBREVIATION;
    }

    /**
     * Returns the required action.
     *
     * @param key of the required action
     * @return the required action or null if there is no such an action
     */
    public ProModAction getAction(final String key){
        if(actions.containsKey(key)){
            return actions.get(key);
        }

        return null;
    }

    /**
     * @return true if the tree structure is supposed to be checked, false otherwise
     */
    public boolean isCheckTreeStructure() {
        return checkTreeStructure;
    }

    /**
     * Allows the user to set the check tree functionality on and off.
     *
     * @param checkTreeStructure true if there check tree functionality is supposed to be on, false otherwise
     */
    public void setCheckTreeStructure(final boolean checkTreeStructure) {
        final boolean oldValue = this.checkTreeStructure;
        this.checkTreeStructure = checkTreeStructure;
        firePropertyChange(CHECK_TREE_STRUCTURE_PROPERTY, oldValue, checkTreeStructure);
    }

    /**
     * @return the plugin's name
     */
    public String getName() {
        return NAME;
    }

    /**
     * @return the plugin's description 
     */
    public String getDescription() {
        return DESCRIPTION;
    }
}
