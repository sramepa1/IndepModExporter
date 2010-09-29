package cz.cvut.promod.diagramReferenceModule.frame;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.diagramReferenceModule.frame.ProjectTreeDialog.ProjectTreeDialog;
import cz.cvut.promod.diagramReferenceModule.DiagramReferenceModuleModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.ProcessVertex;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.Set;
import java.util.UUID;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.jgraph.event.GraphSelectionListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.JGraph;
import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:07:34, 16.4.2010
 *
 * Diagram Reference dockable frame. 
 */
public class ReferenceFrame extends ReferenceFrameView implements DockableFrameData, GraphSelectionListener{

    private final static Logger LOG = Logger.getLogger(ReferenceFrame.class);

    private final ReferenceFrameModel model;

    private Object cell;
    private ProcessVertex processVertex;

    private JGraph graph;


    public ReferenceFrame() {
        model = new ReferenceFrameModel();

        initEventHandling();
    }

    private void initEventHandling() {
        connectDiagramButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(processVertex != null){
                    new ProjectTreeDialog(processVertex);

                    if((graph != null) && (cell != null)){
                        graph.setSelectionCell(cell);
                    }

                } else {
                    LOG.error("Not process vertex available.");
                }
            }
        });

        goToDiagramButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if((processVertex != null) && (processVertex.getUuid() != null)){

                    TreePath treePath = null;

                    for(final TreePath projectTreePath : ModelerSession.getProjectService().getProjectPaths()){
                        treePath = ProjectServiceUtils.findProjectDiagram(
                                projectTreePath,
                                processVertex.getUuid()
                        );


                        if(treePath != null){
                            break;
                        }
                    }

                    if(treePath != null){                        
                        ModelerSession.getProjectControlService().setSelectedItem(treePath);
                    } else {
                        JOptionPane.showMessageDialog(
                                ModelerSession.getFrame(),
                                DiagramReferenceModuleModel.ERROR_MESSAGE,
                                DiagramReferenceModuleModel.ERROR_TITLE,
                                JOptionPane.ERROR_MESSAGE);

                        processVertex.setUuid(null);

                        if((graph != null) && (cell != null)){
                            graph.setSelectionCell(cell);
                        }
                    }

                } else {
                    LOG.error("Not process vertex or UUID available.");
                }
            }
        });

        removeConnectionButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(processVertex != null){
                    processVertex.setUuid(null);
                }

                if(cell != null){
                    graph.setSelectionCell(cell);
                }
            }
        });
    }

    public String getDockableFrameName() {
        return DiagramReferenceModuleModel.FRAME_NAME;
    }

    public JComponent getDockableFrameComponent() {
        return this;
    }

    public NotationGuiHolder.Position getInitialPosition() {
        return NotationGuiHolder.Position.BOTTOM;
    }

    public boolean isMaximizable() {
        return false;
    }

    public Set<NotationGuiHolder.Position> getAllowedDockableFramePositions() {
        return model.getPositions();
    }

    public InitialState getInitialState() {
        return InitialState.OPENED;
    }

    public String getDockableFrameTitle() {
        return DiagramReferenceModuleModel.FRAME_TITLE;
    }

    public Icon getButtonIcon() {
        return null;
    }

    public void valueChanged(GraphSelectionEvent e) {
        goToDiagramButton.setEnabled(false);
        removeConnectionButton.setEnabled(false);
        connectDiagramButton.setEnabled(false);
        connectedDiagramLabel.setText(DiagramReferenceModuleModel.NO_DIAGRAM);
        connectedDiagramIDLabel.setText(null);

        cell = getNewlySelectedVertex(e);

        if(cell != null){
            connectDiagramButton.setEnabled(true);

            if(cell instanceof DefaultGraphCell){
                final DefaultGraphCell defaultGraphCell = (DefaultGraphCell) cell;

                if(defaultGraphCell.getUserObject() instanceof ProcessVertex){
                    processVertex = (ProcessVertex) defaultGraphCell.getUserObject();
                    final UUID uuid = processVertex.getUuid();

                    if(uuid != null){
                        connectedDiagramLabel.setText(processVertex.getName());
                        connectedDiagramIDLabel.setText("id: " + processVertex.getUuid());
                        removeConnectionButton.setEnabled(true);
                        goToDiagramButton.setEnabled(true);
                        removeConnectionButton.setEnabled(true);
                    }
                }
            }
        }
    }

    /**
     * Finds newly selected vertex in the graph.
     *
     * @param event represents actual graph selection event
     * @return newly selected vertex; or null if there are no newly selected vertexes or more than one vertex is selected
     */
    private Object getNewlySelectedVertex(final GraphSelectionEvent event) {
        Object newCell = null;

        for(Object cell : event.getCells()){
            if(event.isAddedCell(cell)){
                if(newCell != null) {
                    return null; // multiple vertex selection
                }

                newCell = cell;
            }
        }

        return newCell;
    }

    public void setGraph(final JGraph graph) {
        this.graph = graph;
    }
}
