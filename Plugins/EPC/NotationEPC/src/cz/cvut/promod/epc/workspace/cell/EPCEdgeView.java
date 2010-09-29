package cz.cvut.promod.epc.workspace.cell;

import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:31:47, 21.1.2010
 *
 * Special implementation of the EdgeView for the EPCNotation plugin. 
 */
public class EPCEdgeView extends EdgeView {

    /**
     * EPCGraphCellEditor class overrides getCellEditorValue() method. For details
     * @see cz.cvut.promod.epc.workspace.cell.EPCGraphCellEditor
     */
    private static final GraphCellEditor epcCellEditor = new EPCGraphCellEditor();    


    public EPCEdgeView(final Object cell){
        super(cell);
    }

    @Override
    public GraphCellEditor getEditor() {
        return epcCellEditor;
    }
}
