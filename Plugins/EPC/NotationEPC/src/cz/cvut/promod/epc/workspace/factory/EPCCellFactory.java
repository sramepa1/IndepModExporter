package cz.cvut.promod.epc.workspace.factory;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.apache.log4j.Logger;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Hashtable;
import java.util.UUID;

import cz.cvut.promod.epc.frames.toolChooser.ToolChooserModel;
import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.*;
import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.ComputerHWModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:40:07, 7.12.2009
 *
 * EPCCellFactory is responsible for creating a new vertexes in the EPCNotation plugin.
 */
public class EPCCellFactory {

    private static final Logger LOG = Logger.getLogger(EPCCellFactory.class);

    private static final int DASH_LINE_SEGMENT_LENGTH = 10;
    private static final int DASH_SPACE_SEGMENT_LENGTH = 10;

    private static final int DOTTED_LINE_SEGMENT_LENGTH = 2;
    private static final int DOTTED_SPACE_SEGMENT_LENGTH = 10;

    private static final int DOT_AND_DASH_SEGMENT_1 = 10;
    private static final int DOT_AND_DASH_SEGMENT_2 = 4;
    private static final int DOT_AND_DASH_SEGMENT_3 = 2;
    private static final int DOT_AND_DASH_SEGMENT_4 = 4;

    /**
     *  Creates the required vertex.
     *
     * @param point is the point to be the vertex positioned
     * @param tool is the selected tool - vertex type
     * @return required vertex in form of graph cell
     */
    public static DefaultGraphCell createVertex(final Point2D point, final ToolChooserModel.Tool tool) {
        final DefaultGraphCell cell = new DefaultGraphCell();

        switch (tool){
            case ADD_FUNCTION:
                cell.setUserObject(new FunctionModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(FunctionModel.installAttributes(point));
                break;

            case ADD_EVENT:
                cell.setUserObject(new EventModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(EventModel.installAttributes(point));
                break;

            case ADD_AND:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.AND, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.AND));
                break;

            case ADD_OR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.OR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.OR));
                break;

            case ADD_XOR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.XOR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.XOR));
                break;

            case ADD_AND_OR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.AND_OR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.AND_OR));
                break;

            case ADD_AND_XOR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.AND_XOR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.AND_XOR));
                break;

            case ADD_OR_AND:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.OR_AND, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.OR_AND));
                break;

            case ADD_OR_XOR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.OR_XOR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.OR_XOR));
                break;

            case ADD_XOR_AND:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.XOR_AND, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.XOR_AND));
                break;

            case ADD_XOR_OR:
                cell.setUserObject(new LogicFunctionModel(LogicFunctionModel.LogicOperator.XOR_OR, UUID.randomUUID()));
                cell.getAttributes().applyMap(LogicFunctionModel.installAttributes(point, LogicFunctionModel.LogicOperator.XOR_OR));
                break;
            
            case ADD_DELIVERABLE:
                cell.setUserObject(new DeliverableModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(DeliverableModel.installAttributes(point));
                break;

            case ADD_ORGANIZATION_UNIT:
                cell.setUserObject(new OrganizationUnitModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(OrganizationUnitModel.installAttributes(point));
                break;

            case ADD_ORGANIZATION_ROLE:
                cell.setUserObject(new OrganizationRoleModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(OrganizationRoleModel.installAttributes(point));
                break;

            case ADD_INFORMATION_OBJECT:
                cell.setUserObject(new InformationObjectModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(InformationObjectModel.installAttributes(point));
                break;

            case ADD_HW:
                cell.setUserObject(new ComputerHWModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(ComputerHWModel.installAttributes(point));
                break;

            case ADD_APP_SW:
                cell.setUserObject(new ApplicationSoftwareModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(ApplicationSoftwareModel.installAttributes(point));                
                break;

            case ADD_MACHINE:
                cell.setUserObject(new MachineModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(MachineModel.installAttributes(point));
                break;

            case ADD_GOAL:
                cell.setUserObject(new GoalModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(GoalModel.installAttributes(point));
                break;

            case ADD_MESSAGE:
                cell.setUserObject(new MessageModel(UUID.randomUUID()));
                cell.getAttributes().applyMap(MessageModel.installAttributes(point));
                break;

            default:
                LOG.error("No such a vertex type exists in EPC notation.");
                return null;
        }

        final DefaultPort defaultPort = new DefaultPort();
        cell.add(defaultPort);

        return cell;
    }

    /**
     * Creates required edge.
     *
     * @param tool is the selected tool - edge type
     * 
     * @return an edge
     */
    public static DefaultEdge createEdge(final ToolChooserModel.Tool tool) {
        final DefaultEdge edge = new DefaultEdge();

        final EdgeModel.EdgeType edgeType;
        switch (tool){
            case ADD_CONTROL_FLOW_LINE:
                edgeType = EdgeModel.EdgeType.CONTROL_FLOW;
                break;
            case ADD_MATERIAL_OUTPUT_FLOW_LINE:
                edgeType = EdgeModel.EdgeType.MATERIAL_FLOW;
                break;
            case ADD_INFORMATION_FLOW_LINE:
                edgeType = EdgeModel.EdgeType.INFORMATION_FLOW;
                break;
            case ADD_ORGANIZATION_FLOW_LINE:
                edgeType = EdgeModel.EdgeType.ORGANIZATION_FLOW;
                break;
            case ADD_INFORMATION_SERVICE_FLOW_LINE:
                edgeType = EdgeModel.EdgeType.INFORMATION_SERVICES_FLOW;
                break;
            default:
                // should never happened, testing & developing purposes
                LOG.error("No proper tool for any kind of edge.");
                return null;
        }

        edge.setUserObject(new EdgeModel(edgeType));

        edge.getAttributes().applyMap(createEdgeAttributes(tool));

        return edge;
    }


    /**
     * Creates attributes for a epc graph edges
     *
     * @param tool selected epc tool
     *
     * @return the map with relevant attributes for the edge
     */
    private static  Map createEdgeAttributes(final ToolChooserModel.Tool tool) {
		final Map map = new Hashtable();

        if(!ToolChooserModel.Tool.ADD_ORGANIZATION_FLOW_LINE.equals(tool)){
            GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        }
        
        GraphConstants.setEndFill(map, true);
        GraphConstants.setLineStyle(map, GraphConstants.STYLE_ORTHOGONAL);
		GraphConstants.setLabelAlongEdge(map, false);
        GraphConstants.setEditable(map, true);
        GraphConstants.setMoveable(map, true);
        GraphConstants.setDisconnectable(map, false);

        if(ToolChooserModel.Tool.ADD_INFORMATION_SERVICE_FLOW_LINE.equals(tool)){
            // make dashed line
            GraphConstants.setDashPattern(map, new float[] {DASH_LINE_SEGMENT_LENGTH, DASH_SPACE_SEGMENT_LENGTH});

        } else if(ToolChooserModel.Tool.ADD_INFORMATION_FLOW_LINE.equals(tool)){
            // make dotted line
            GraphConstants.setDashPattern(map, new float[] {DOTTED_LINE_SEGMENT_LENGTH, DOTTED_SPACE_SEGMENT_LENGTH});

        } else if(ToolChooserModel.Tool.ADD_MATERIAL_OUTPUT_FLOW_LINE.equals(tool)){
            // make dot-and-dash line
            GraphConstants.setDashPattern(map, new float[] {
                    DOT_AND_DASH_SEGMENT_1, DOT_AND_DASH_SEGMENT_2,
                    DOT_AND_DASH_SEGMENT_3, DOT_AND_DASH_SEGMENT_4});
        }

		return map;
	}
}
