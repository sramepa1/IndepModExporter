package cz.cvut.promod.hierarchyNotation.workspace;

import org.jgraph.JGraph;
import org.apache.log4j.Logger;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.workspace.UpdatableWorkspaceComponent;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.diagramModel.ProcessHierarchyDiagramModel;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;

import javax.swing.*;

import com.jidesoft.status.LabelStatusBarItem;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:13:22, 30.11.2009
 *
 * Represents the top layer of ProcessHierarchyWorkspaceData plugin.
 */
public class ProcessHierarchyWorkspace extends JScrollPane implements UpdatableWorkspaceComponent {

    private final Logger LOG = Logger.getLogger(ProcessHierarchyWorkspace.class);

    private final JGraph graph;

    final LabelStatusBarItem statusBarItem;

    
    public ProcessHierarchyWorkspace(final JGraph graph, final LabelStatusBarItem statusBarItem) {
        super(graph);

        this.graph = graph;
        this.statusBarItem = statusBarItem;
    }

    public void update() {
        try{
            final ProjectRoot projectRoot = ModelerSession.getProjectService().getSelectedProject();
            final ProjectDiagram selectedDiagram = ModelerSession.getProjectService().getSelectedDiagram();
            final DiagramModel diagramModel = selectedDiagram.getDiagramModel();
            final ProcessHierarchyDiagramModel processHierarchyDiagramModel =
                    (ProcessHierarchyDiagramModel) diagramModel;

            statusBarItem.setText(""); // not necessary

            ModelerSession.setFrameTitleText(projectRoot.getDisplayName() + " - " + selectedDiagram.getDisplayName());

            graph.setGraphLayoutCache(processHierarchyDiagramModel.getGraphLayoutCache());

            // forces all ports are repainted, even when the graph has just been loaded
            graph.getGraphLayoutCache().update();

        } catch (ClassCastException exception){
            LOG.error("Invalid diagram model for process hierarchy workspace.", exception);
        } catch (Exception exception){
            LOG.error("An error has occurred during diagram switching.", exception);
        }
    }

    public void over() {
        statusBarItem.setText("");
        graph.getSelectionModel().clearSelection();
    }

}