package cz.cvut.promod.hierarchyNotation.modelFactory.graphModel;

import org.jgraph.graph.*;
import org.apache.log4j.Logger;

import java.util.List;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.hierarchyNotation.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:26:30, 30.11.2009
 *
 *  Model that doesn't allow 'self referencing' = it means that no edge can have the same
 * starting and target vertex. That would be a non sence in process hierarchy notation.
 */
public class ProcessHierarchyGraphModel extends DefaultGraphModel {

    private static final String TREE_ERROR_LABEL =
            Resources.getResources().getString("hierarchy.tree.structure.check.error");

    private static final Logger LOG = Logger.getLogger(ProcessHierarchyGraphModel.class);

    /**
     * Constructs a new ProcessHierarchyGraphModel.
     */
    public ProcessHierarchyGraphModel(){
        super(null, null);
    }

    /**
     * Constructs a new ProcessHierarchyGraphModel.
     * @param roots are the node's objects.
     * @param attributes are the node's attributes.
     */
    public ProcessHierarchyGraphModel(final List roots, final AttributeMap attributes){
        super(roots, attributes);
    }


    @Override
    public boolean acceptsSource(Object edge, Object port) {
        return (((Edge) edge).getTarget() != port);
    }

    @Override
    public boolean acceptsTarget(final Object edge, final Object targetPort) {

        final String identifier = ModelerSession.getProjectService().getSelectedDiagram().getNotationIdentifier();

        try{
            ProcessHierarchyNotation notation =
                    (ProcessHierarchyNotation) ModelerSession.getNotationService().getNotation(identifier);

            notation.setStatusInfoText("");

            if(notation.checkTreeStructure()){
                final DefaultPort defaultPort = (DefaultPort) targetPort;

                for(final Object testedEdge : defaultPort.getEdges()){
                    if(testedEdge != edge){
                        final DefaultEdge existingEdge = (DefaultEdge) testedEdge;

                        if(existingEdge.getTarget() == targetPort){
                            notation.setStatusInfoText(TREE_ERROR_LABEL);

                            return false;
                        }
                    }
                }
            }

        } catch (Exception exeption){
            LOG.error("Impossible to check tree structure.", exeption);            
        }

        // do not allow self-loops
        return ((Edge)edge).getSource() != targetPort;
    }

}
