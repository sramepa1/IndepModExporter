package cz.cvut.promod.epc.workspace.cell;

import com.jgraph.components.labels.MultiLineVertexView;
import org.jgraph.graph.*;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:41:01, 6.12.2009
 *
 * Special implementation of the MultiLineVertexView for the EPCNotation plugin.
 */
public class EPCVertexView extends MultiLineVertexView {

    private static final VertexRenderer RENDERER = new EPCVertexRenderer();

    /**
     * EPCGraphCellEditor class overrides getCellEditorValue() method. For details
     * @see cz.cvut.promod.epc.workspace.cell.EPCGraphCellEditor   
     */
    private static final GraphCellEditor epcCellEditor = new EPCGraphCellEditor();        


    public EPCVertexView(final Object cell){
        super(cell);
    }

    @Override
    public CellViewRenderer getRenderer() {
        return RENDERER;
    }

    @Override
    public GraphCellEditor getEditor() {
        return epcCellEditor;
    }
}
