package cz.cvut.promod.epc.modelFactory.epcGraphModel;

import org.jgraph.graph.*;

import java.util.List;

import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.*;
import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:58:07, 5.12.2009
 *
 * Represents an implementation of common GraphModel for EPC notation diagrams.
 */
public class EPCGraphModel extends DefaultGraphModel{       

    private final static String ERROR_TITLE_LABEL = Resources.getResources().getString("epc.menu.epc.notation");
    private final static String ERROR_EVENTS_LABEL = Resources.getResources().getString("epc.conn.error.events");
    private final static String ERROR_FUNCTIONS_LABEL = Resources.getResources().getString("epc.conn.error.functions");
    private final static String ERROR_MULTIPLE_CONTROL_FLOW_LABEL =
            Resources.getResources().getString("epc.conn.error.multiple.ctr.flow");
    private final static String ERROR_LOGICS_ILLEGAL_FLOW_LABEL =
            Resources.getResources().getString("epc.conn.error.logics.illegal.flow");
    private final static String ERROR_LOGICS_ILLEGAL_NEIGHBOUR_LABEL =
            Resources.getResources().getString("epc.conn.error.logics.illegal.neig");

    
    public EPCGraphModel(){
        super(null, null);        
    }

    public EPCGraphModel(final List roots, final AttributeMap attributes){
        super(roots, attributes);        
    }

    @Override
    public boolean acceptsTarget(Object edge, Object port) {
        return ((Edge) edge).getSource() != port && accepts(edge, port, false);

    }

    @Override
    public boolean acceptsSource(Object edge, Object port) {
        return ((Edge) edge).getTarget() != port && accepts(edge, port, true);

    }

    /**
     * Determines whether the connection is acceptable or not.
     *
     * @param edge is the connecting edge
     * @param port is the source or target port
     * @param source if true, the port specified is source port, otherwise the port is target port
     * @return true if the connection is acceptable, false otherwise
     */
    private boolean accepts(final Object edge, final Object port, final boolean source) {
        final DefaultEdge defaultEdge = (DefaultEdge) edge;
        final DefaultPort sourcePort;
        final DefaultPort targetPort;

        if(source){
            sourcePort = (DefaultPort) port;
            targetPort = (DefaultPort) defaultEdge.getTarget();
        } else {
            sourcePort = (DefaultPort) defaultEdge.getSource();
            targetPort = (DefaultPort) port;            
        }

        if((sourcePort == null) || (targetPort == null)){
            return false;
        }

        final DefaultGraphCell sourceCell = (DefaultGraphCell) sourcePort.getParent();
        final DefaultGraphCell targetCell = (DefaultGraphCell) targetPort.getParent();
        final EdgeModel edgeModel = (EdgeModel) defaultEdge.getUserObject();
        final Object targetUserObject = targetCell.getUserObject();
        final Object sourceUserObject = sourceCell.getUserObject();

        // No event can be connected directly with another event.
        if(sourceCell.getUserObject() instanceof EventModel && targetCell.getUserObject() instanceof EventModel){
            JOptionPane.showMessageDialog(
                    ModelerSession.getFrame(),
                    ERROR_EVENTS_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

            return false;
        }

        // No function can be connected directly with another event.
        if(sourceCell.getUserObject() instanceof FunctionModel && targetCell.getUserObject() instanceof FunctionModel){
            JOptionPane.showMessageDialog(
                    ModelerSession.getFrame(),
                    ERROR_FUNCTIONS_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

            return false;
        }

        // every Event and every Function can have just one incoming and one outgoing edge
        if(EdgeModel.EdgeType.CONTROL_FLOW.equals(edgeModel.getEdgeType())){
            if( targetUserObject instanceof FunctionModel || targetUserObject instanceof EventModel){
                for(Object portEdge : targetPort.getEdges()){
                    final DefaultEdge testingEdge = (DefaultEdge) portEdge;
                    final EdgeModel testingEdgeModel = (EdgeModel) testingEdge.getUserObject();

                    if(edge != testingEdge){ // when a new 'line breaking' point is added, the graph behaves like a new edge would be added

                        if(EdgeModel.EdgeType.CONTROL_FLOW.equals(testingEdgeModel.getEdgeType())){
                            if(testingEdge.getTarget() == targetPort){
                            JOptionPane.showMessageDialog(ModelerSession.getFrame(),
                                ERROR_MULTIPLE_CONTROL_FLOW_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

                                return false;
                            }
                        }
                    }
                }
            }
            if(sourceUserObject instanceof FunctionModel || sourceUserObject instanceof EventModel){
                for(Object portEdge : sourcePort.getEdges()){
                    final DefaultEdge testingEdge = (DefaultEdge) portEdge;
                    final EdgeModel testingEdgeModel = (EdgeModel) testingEdge.getUserObject();

                    if(edge != testingEdge){ // when a new 'line breaking' point is added, the graph behaves like a new edge would be added

                        if(EdgeModel.EdgeType.CONTROL_FLOW.equals(testingEdgeModel.getEdgeType())){
                            if(testingEdge.getSource() == sourcePort){
                                JOptionPane.showMessageDialog(ModelerSession.getFrame(),
                                    ERROR_MULTIPLE_CONTROL_FLOW_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

                                return false;
                            }
                        }
                    }
                }
            }
        }

        // logic functions can be sources or targets only for control flow edges
        if(!EdgeModel.EdgeType.CONTROL_FLOW.equals(edgeModel.getEdgeType())){
            if((sourceUserObject instanceof LogicFunctionModel)
                    || (targetUserObject instanceof LogicFunctionModel)
                    || (targetUserObject instanceof MessageModel)){
                    JOptionPane.showMessageDialog(ModelerSession.getFrame(),
                        ERROR_LOGICS_ILLEGAL_FLOW_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

                        return false;
            }
        }

        // a logic function can be connected only with functions, event or another logic function
        if(targetCell.getUserObject() instanceof LogicFunctionModel){
            if(!(sourceUserObject instanceof LogicFunctionModel
                    || sourceUserObject instanceof EventModel
                    || sourceUserObject instanceof FunctionModel
                    || sourceUserObject instanceof MessageModel)){

                JOptionPane.showMessageDialog(ModelerSession.getFrame(),
                        ERROR_LOGICS_ILLEGAL_NEIGHBOUR_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

                return false;
            }
        }
        if(sourceCell.getUserObject() instanceof LogicFunctionModel){
            if(!(targetUserObject instanceof LogicFunctionModel
                    || targetUserObject instanceof EventModel
                    || targetUserObject instanceof FunctionModel
                    || targetUserObject instanceof MessageModel)){

                JOptionPane.showMessageDialog(ModelerSession.getFrame(),
                        ERROR_LOGICS_ILLEGAL_NEIGHBOUR_LABEL, ERROR_TITLE_LABEL, JOptionPane.ERROR_MESSAGE);

                return false;
            }

        }

        return super.acceptsSource(edge, targetPort);
    }

}
