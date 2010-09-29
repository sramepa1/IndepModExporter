package cz.cvut.promod.hierarchyNotation.modelFactory;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.diagramModel.ProcessHierarchyDiagramModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.graphModel.ProcessHierarchyGraphModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.viewFactory.PHCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:52:38, 26.11.2009
 *
 * Implementation of DiagramModelFactory for the ProcessHierarchyNotation plugin.
 *
 * @see cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory
 */
public class ProcessHierarchyModelFactory implements DiagramModelFactory{

    /** {@inheritDoc}
     *
     * Creates an empty model for ProcessHierarchy notation diagram.
     */
    public DiagramModel createEmptyDiagramModel() {
        return new ProcessHierarchyDiagramModel(
                new GraphLayoutCache(new ProcessHierarchyGraphModel(), new PHCellViewFactory())
        );
    }
    
}
