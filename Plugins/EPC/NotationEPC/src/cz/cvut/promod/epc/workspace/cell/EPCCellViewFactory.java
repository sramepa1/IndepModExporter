package cz.cvut.promod.epc.workspace.cell;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;
import org.jgraph.graph.PortView;
import org.jgraph.graph.EdgeView;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:25:37, 17.12.2009
 *
 * Special implementation of DefaultCellViewFactory for the EPCNotation plugin.
 */
public class EPCCellViewFactory extends DefaultCellViewFactory{

    @Override
    protected VertexView createVertexView(final Object cell) {
        return new EPCVertexView(cell);
    }

    @Override
    protected PortView createPortView(final Object cell) {
        return new EPCPortView(cell);
    }

    @Override
    protected EdgeView createEdgeView(Object cell) {
        return new EPCEdgeView(cell);
    }
}
