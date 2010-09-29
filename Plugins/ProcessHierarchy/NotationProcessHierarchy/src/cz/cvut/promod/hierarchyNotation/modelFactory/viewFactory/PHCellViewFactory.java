package cz.cvut.promod.hierarchyNotation.modelFactory.viewFactory;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.PHVertexView;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:14:15, 16.4.2010
 *
 * DefaultCellViewFactory of ProcessHierarchyNotation plugin.
 */
public class PHCellViewFactory extends DefaultCellViewFactory{

    @Override
    protected VertexView createVertexView(Object cell) {
        return new PHVertexView(cell);
    }

}
