package cz.cvut.promod.ph.treeLayout.actions;

import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.hierarchyNotation.modelFactory.diagramModel.ProcessHierarchyDiagramModel;
import cz.cvut.promod.hierarchyNotation.workspace.ProcessHierarchyWorkspaceData;
import cz.cvut.promod.ph.treeLayout.resources.Resources;
import cz.cvut.promod.ph.treeLayout.settings.TreeLayoutSettings;

import java.awt.event.ActionEvent;
import java.util.Map;

import org.jgraph.JGraph;
import org.apache.log4j.Logger;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.tree.JGraphTreeLayout;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 12:04:00, 28.1.2010
 *
 * Layouting action that performs the tree layout positioning.
 */
public class LayoutAction extends ProModAction{

    private static final Logger LOG = Logger.getLogger(LayoutAction.class);

    private final String notationIdentifier;

    public LayoutAction(final String notationIdentifier){
        super("Layout", Resources.getIcon(Resources.ICONS + Resources.LAYOUT), null);

        this.notationIdentifier = notationIdentifier;
    }

    /**
     * Method performing the actual layouting.
     *
     * @param event the action event
     */
    public void actionPerformed(ActionEvent event) {
        final ProjectDiagram projectDiagram = ModelerSession.getProjectService().getSelectedDiagram();

        if(projectDiagram.getNotationIdentifier().equals(notationIdentifier)){
            final DiagramModel diagramModel = projectDiagram.getDiagramModel();

            if(diagramModel instanceof ProcessHierarchyDiagramModel){
                final NotationWorkspaceData notationWorkspaceData =
                        ModelerSession.getNotationService().getNotation(notationIdentifier).getNotationWorkspaceData();

                final ProcessHierarchyWorkspaceData workspaceData;
                try{
                    workspaceData = (ProcessHierarchyWorkspaceData) notationWorkspaceData;
                } catch (ClassCastException exception){
                    LOG.error("Not valid ProcessHierarchyWorkspaceData.");
                    return;
                }

                final JGraph graph = workspaceData.getGraph();

                final Object[] selectedCells = graph.getSelectionModel().getSelectionCells();

                if(selectedCells.length != 1){
                    LOG.debug("More than one (or no) selected root vertexes.");
                    return;
                }

                JGraphFacade facade = new JGraphFacade(graph, selectedCells /* roots */);

                final JGraphTreeLayout layout = new JGraphTreeLayout();
                switch (TreeLayoutSettings.getInstance().getVerticalLayout()){
                    case BOTTOM:
                        layout.setAlignment(SwingConstants.BOTTOM);
                        break;
                    case TOP:
                        layout.setAlignment(SwingConstants.TOP);
                        break;
                    case CENTER:
                        layout.setAlignment(SwingConstants.CENTER);
                        break;
                }


                layout.run(facade);
                Map nested = facade.createNestedMap(true, true);
                graph.getGraphLayoutCache().edit(nested);

            } else {
                LOG.error("Not relevant diagram model.");
            }

        } else {
            LOG.error("Not relevant notation identifier.");
        }
    }
}


