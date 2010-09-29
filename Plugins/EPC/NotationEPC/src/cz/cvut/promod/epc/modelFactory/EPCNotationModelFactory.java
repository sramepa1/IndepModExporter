package cz.cvut.promod.epc.modelFactory;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.epc.modelFactory.diagramModel.EPCDiagramModel;
import cz.cvut.promod.epc.modelFactory.epcGraphModel.EPCGraphModel;
import cz.cvut.promod.epc.workspace.cell.EPCCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:55:48, 5.12.2009
 *
 * Impelemtation of DiagramModelFactory interface for the EPCNotation plugin.
 */
public class EPCNotationModelFactory implements DiagramModelFactory {

    /** {@inheritDoc} 
     *
     * Creates an empty model for EPC notation diagram.
     */
    public DiagramModel createEmptyDiagramModel(){
        return new EPCDiagramModel(
                new GraphLayoutCache(new EPCGraphModel(), new EPCCellViewFactory()));
    }

}
