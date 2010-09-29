package cz.cvut.promod.epc.workspace;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.epc.frames.toolChooser.ToolChooserModel;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

import javax.swing.*;

import com.jgoodies.binding.value.ValueModel;
import org.jgraph.JGraph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Map;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:59:34, 5.12.2009
 *
 * EPCWorkspaceData represents the NotationWorkspaceData interface implementation for the EPCNotation plugin.  
 */
public class EPCWorkspaceData implements NotationWorkspaceData {

    private static JGraph graph;
    private static EPCWorkspace workspace;

    private final ValueModel selectedToolModel;
    private final ValueModel gridModel;
    private final ValueModel lockModel;
    private final ValueModel viewGridModel;
    private final ValueModel cellSizeModel;
    private final ValueModel scaleModel;
    private final ValueModel movableBelowZeroModel;

    public static final String PROPERTY_SELECTED_CELL = "selectedCell";

    public EPCWorkspaceData(final ValueModel selectedToolModel,
                            final ValueModel gridModel,
                            final ValueModel lockModel,
                            final ValueModel viewGridModel,
                            final ValueModel cellSizeModel,
                            final ValueModel scaleModel,
                            final ValueModel movableBelowZeroModel,
                            final Map<String, ProModAction> actions,
                            final JPopupMenu popupMenu) {

        this.selectedToolModel = selectedToolModel;

        graph = new EPCGraph(selectedToolModel, popupMenu, actions);
        workspace = new EPCWorkspace(graph, actions);

        this.gridModel = gridModel;
        this.lockModel = lockModel;
        this.viewGridModel = viewGridModel;
        this.cellSizeModel = cellSizeModel;
        this.scaleModel = scaleModel;
        this.movableBelowZeroModel = movableBelowZeroModel;

        initEventHandling();
    }

    private void initEventHandling() {
        lockModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                graph.setEditable((Boolean) propertyChangeEvent.getNewValue());
            }
        });

        gridModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                graph.setGridEnabled((Boolean) propertyChangeEvent.getNewValue());
            }
        });

        viewGridModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                graph.setGridVisible((Boolean) propertyChangeEvent.getNewValue());
            }
        });

        cellSizeModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                final Integer gridCellSize = (Integer) propertyChangeEvent.getNewValue();

                graph.setGridSize(gridCellSize.doubleValue());
                graph.refresh();
            }
        });        

        selectedToolModel.addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                final ToolChooserModel.Tool selectedTool = (ToolChooserModel.Tool) propertyChangeEvent.getNewValue();

                boolean portsVisible = false;

                switch(selectedTool){
                    case ADD_CONTROL_FLOW_LINE:
                    case ADD_INFORMATION_SERVICE_FLOW_LINE:
                    case ADD_ORGANIZATION_FLOW_LINE:
                    case ADD_MATERIAL_OUTPUT_FLOW_LINE:
                    case ADD_INFORMATION_FLOW_LINE:
                        portsVisible = true;
                }

                graph.setPortsVisible(portsVisible);
            }
        });

        scaleModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                final double scaleMultiplicator = (((Integer) evt.getNewValue()).doubleValue()) / 100.0;
                
                graph.setScale(scaleMultiplicator);
            }
        });

        movableBelowZeroModel.addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                graph.setMoveBelowZero((Boolean) evt.getNewValue());
            }
        });
    }

    public JComponent getWorkspaceComponentSingleton() {
        return workspace;
    }

    public JGraph getGraph() {
        return graph;
    }
    
}
