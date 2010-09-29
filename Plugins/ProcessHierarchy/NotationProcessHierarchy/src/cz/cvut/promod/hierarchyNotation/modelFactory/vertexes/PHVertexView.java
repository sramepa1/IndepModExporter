package cz.cvut.promod.hierarchyNotation.modelFactory.vertexes;

import org.jgraph.graph.VertexView;
import org.jgraph.graph.GraphCellEditor;
import cz.cvut.promod.hierarchyNotation.modelFactory.cellEditor.ProcessHierarchyCellEditor;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:36:40, 16.4.2010
 *
 * VertexView of ProcessHierarchyNotation plugin.
 */
public class PHVertexView extends VertexView{

    private static final GraphCellEditor phCellEditor = new ProcessHierarchyCellEditor();


    public PHVertexView(final Object cell){
        super(cell);
    }

    @Override
    public GraphCellEditor getEditor() {
        return phCellEditor;
    }
}
