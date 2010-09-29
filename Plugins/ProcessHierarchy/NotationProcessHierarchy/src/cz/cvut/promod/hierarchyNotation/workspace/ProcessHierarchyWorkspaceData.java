package cz.cvut.promod.hierarchyNotation.workspace;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.hierarchyNotation.frames.toolChooser.ToolChooserModel;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

import javax.swing.*;

import com.jgoodies.binding.value.ValueModel;
import com.jidesoft.status.LabelStatusBarItem;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import org.jgraph.JGraph;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:33:29, 26.11.2009
 *
 * ProcessHierarchyWorkspaceData encapsulate the workspace component of ProcessHierarchyWorkspaceData plugin.
 */
public class ProcessHierarchyWorkspaceData implements NotationWorkspaceData {

    private static JGraph graph;
    private static ProcessHierarchyWorkspace workspace;

    private final ValueModel gridModel;
    private final ValueModel viewGridModel;
    private final ValueModel cellSizeModel;
    private final ValueModel selectedToolModel;
    private final ValueModel scaleModel;


    public ProcessHierarchyWorkspaceData(final ValueModel gridModel,
                                         final ValueModel viewGridModel,
                                         final ValueModel cellSizeModel,
                                         final ValueModel scaleModel,
                                         final ValueModel selectedToolModel,
                                         final Map<String, ProModAction> actions,
                                         final LabelStatusBarItem statusBarItem){

        graph = new ProcessHierarchyGraph(actions, selectedToolModel);
        workspace = new ProcessHierarchyWorkspace(graph, statusBarItem);
        
        this.gridModel = gridModel;
        this.viewGridModel = viewGridModel;
        this.cellSizeModel =cellSizeModel;
        this.selectedToolModel = selectedToolModel;
        this.scaleModel = scaleModel;

        initEventHandling();
    }

    private void initEventHandling() {
        scaleModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                final double scaleMultiplicator = (((Integer) evt.getNewValue()).doubleValue()) / 100.0;

                graph.setScale(scaleMultiplicator);
            }
        });

        gridModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                graph.setGridEnabled((Boolean) propertyChangeEvent.getNewValue());        
            }
        });

        viewGridModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                graph.setGridVisible((Boolean) propertyChangeEvent.getNewValue());
            }
        });

        cellSizeModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                final Integer gridCellSize = (Integer) propertyChangeEvent.getNewValue();

                graph.setGridSize(gridCellSize);
                graph.refresh();
            }
        });

        selectedToolModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                final ToolChooserModel.Tool selectedTool = (ToolChooserModel.Tool) propertyChangeEvent.getNewValue();

                boolean portsVisible = false;

                switch(selectedTool){
                    case ADD_EDGE:
                        portsVisible = true;
                        break;
                }

                graph.setPortsVisible(portsVisible);
            }
        });
    }

    /** {@inheritDoc} */
    public JComponent getWorkspaceComponentSingleton() {
        return workspace;
    }

    public JGraph getGraph() {
        return graph;
    }
}
