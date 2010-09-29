package cz.cvut.promod.ph.treeLayout;

import cz.cvut.promod.ph.treeLayout.actions.LayoutAction;
import cz.cvut.promod.ph.treeLayout.resources.Resources;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:22:31, 28.1.2010
 *
 * Model component for ProcessHierarchyTreeLayout plugin.
 */
public class ProcessHierarchyTreeLayoutModel{

    public static final String LAYOUT_ACTION_IDENTIFIER = "process.hierarchy.tree.layout.action"; 

    private final String relatedNotationIdentifier;

    private final ProModAction layoutAction;

    public static final String PROCESS_HIERARCHY_LABEL =
            Resources.getResources().getString("hierarchy.menu.label");
    public static final String LAYOUT_LABEL =
            Resources.getResources().getString("hierarchy.menu.layout");



    public ProcessHierarchyTreeLayoutModel(final String relatedNotationIdentifier){
        this.relatedNotationIdentifier = relatedNotationIdentifier;
        layoutAction = new LayoutAction(getRelatedNotationIdentifier());
    }


    public String getRelatedNotationIdentifier() {
        return relatedNotationIdentifier;
    }

    public String getIdentifier() {
        return "ProcessHierarchy.id";
    }

    public ProModAction getLayoutAction() {
        return layoutAction;
    }

    public String getName() {
        return "ProcessHierarchyTreeLayout";
    }

    public String getDescription() {
        return "Performs tree layouting of diagrams defined by the Process Hierarchy Notation (ProcessHierarchyNotation plugin).";
    }
}
