package cz.cvut.promod.epc.frames.vertexInfo;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.*;
import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;

import javax.swing.*;
import java.util.*;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.event.GraphSelectionEvent;
import org.apache.log4j.Logger;
import com.jidesoft.grid.Property;
import com.jidesoft.grid.PropertyTableModel;
import com.jidesoft.introspector.BeanProperty;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:20:45, 8.12.2009
 *
 * The Info dockable frame.
 */
public class VertexInfo extends VertexInfoView implements DockableFrameData {

    private static final Logger LOG = Logger.getLogger(VertexInfo.class);

    public static final String FRAME_ID = "epc.info";

    private static final String TITLE_LABEL = Resources.getResources().getString("epc.info.title");
    private static final String CATEGORY_LOGICS_LABEL = Resources.getResources().getString("epc.info.cat.logics");
    private static final String CATEGORY_GENERALS_LABEL = Resources.getResources().getString("epc.info.cat.general");
    private static final String LOGICS_CONDITION_LABEL = Resources.getResources().getString("epc.info.condition");
    private static final String LOGICS_CONDITION_TOP_LABEL = Resources.getResources().getString("epc.info.condition.top");
    private static final String LOGICS_CONDITION_BOTTOM_LABEL = Resources.getResources().getString("epc.info.condition.bottom");
    private static final String CATEGORY_NOTES_LABEL = Resources.getResources().getString("epc.info.cat.notes");
    private static final String NOTE_LABEL = Resources.getResources().getString("epc.info.note");
    private static final String CATEGORY_TYPE_LABEL = Resources.getResources().getString("epc.info.cat.type");
    private static final String TYPE_LABEL = Resources.getResources().getString("epc.info.type");
    private static final String NAME_LABEL = Resources.getResources().getString("epc.info.name");
    private static final String UUID_LABEL = Resources.getResources().getString("epc.info.uuid");    
    private static final String APP_SW_LABEL = Resources.getResources().getString("epc.vertex.app.sw");
    private static final String HW_LABEL = Resources.getResources().getString("epc.vertex.hw");
    private static final String DELIVERABLE_LABEL = Resources.getResources().getString("epc.vertex.deliverable");
    private static final String FLOW_LABEL = Resources.getResources().getString("epc.vertex.flow");
    private static final String EVENT_LABEL = Resources.getResources().getString("epc.vertex.event");
    private static final String FUNCTION_LABEL = Resources.getResources().getString("epc.vertex.function");
    private static final String GOAL_LABEL = Resources.getResources().getString("epc.vertex.goal");
    private static final String INFO_OBJ_LABEL = Resources.getResources().getString("epc.vertex.info.object");
    private static final String LOGIC_OP_LABEL = Resources.getResources().getString("epc.vertex.logic.op");
    private static final String MACHINE_LABEL = Resources.getResources().getString("epc.vertex.machine");
    private static final String MESSAGE_LABEL = Resources.getResources().getString("epc.vertex.message");
    private static final String ORG_ROLE_LABEL = Resources.getResources().getString("epc.vertex.org.role");
    private static final String ORG_UNIT_LABEL = Resources.getResources().getString("epc.vertex.org.unit");
    private static final String CTR_FLOW_LABEL = Resources.getResources().getString("epc.frame.tools.line.control.flow");
    private static final String INF_SERVICES_FLOW_LABEL = Resources.getResources().getString("epc.frame.tools.line.info.services");
    private static final String ORG_FLOW_LABEL = Resources.getResources().getString("epc.frame.tools.line.org.flow");
    private static final String INFO_FLOW_LABEL = Resources.getResources().getString("epc.frame.tools.line.info.flow");
    private static final String MATERIAL_FLOW_LABEL = Resources.getResources().getString("epc.frame.tools.line.material");


    public String getDockableFrameName() {
        return FRAME_ID;
    }

    public JComponent getDockableFrameComponent() {
        return this;
    }

    public NotationGuiHolder.Position getInitialPosition() {
        return NotationGuiHolder.Position.BOTTOM;
    }

    public boolean isMaximizable() {
        return false;
    }

    public Set<NotationGuiHolder.Position> getAllowedDockableFramePositions() {
        final Set<NotationGuiHolder.Position> allowedPositions = new HashSet<NotationGuiHolder.Position>();
        allowedPositions.add(NotationGuiHolder.Position.BOTTOM);
        allowedPositions.add(NotationGuiHolder.Position.TOP);

        return allowedPositions;
    }

    public InitialState getInitialState() {
        return InitialState.HIDDEN;
    }

    public String getDockableFrameTitle() {
        return TITLE_LABEL;
    }

    public Icon getButtonIcon() {
        return null;
    }

    /**
     * Takes care of updating of vertex information whenever a selection is changed.  
     *
     * @param graph is the graph used by the notation
     */
    public void initCellSelectionListener(final JGraph graph) {
        graph.addGraphSelectionListener(new GraphSelectionListener(){
            public void valueChanged(GraphSelectionEvent e){
                List<Property> listProperties = new ArrayList<Property>();

                final Object newlySelectedVertex = getNewlySelectedVertex(e);
                if(newlySelectedVertex != null){
                    final Object cell = e.getCell();

                    if(cell instanceof DefaultGraphCell){
                       final Object userObject = ((DefaultGraphCell) cell).getUserObject();

                        recognizeItemType(userObject, listProperties);

                        if(userObject instanceof EPCEditableVertex){
                            updateInfo(listProperties, userObject);
                        }

                        if(userObject instanceof LogicFunctionModel){
                            getLogicOperatorCondition((LogicFunctionModel)userObject, listProperties);
                        }

                        getItemNote(userObject, listProperties);

                        table.setModel(new PropertyTableModel<Property>(listProperties));
                        table.expandFirstLevel();

                        return;
                    }
                }

                table.setModel(null);
            }
        });
    }

    /**
     * Adds the note.
     *
     * @param userObject is the owner of the note
     * @param listProperties is the list where is the info about note supposed to be added
     */
    private void getItemNote(final Object userObject, final List<Property> listProperties) {
        if(userObject instanceof EPCNoteItem){
            try {
                final BeanProperty conditionBean = new BeanProperty(
                        new PropertyDescriptor(EPCNoteItem.NOTE_PROPERTY, EPCNoteItem.class)){

                    @Override
                    public void setValue(Object o) {
                        super.setValue(o);

                        // let the project diagram know about the change
                        final ProjectDiagram projectDiagram = ModelerSession.getProjectService().getSelectedDiagram();
                        if(projectDiagram != null){
                            projectDiagram.changePerformed(null);
                        }
                    }
                };

                conditionBean.setInstance(userObject);
                conditionBean.setName(NOTE_LABEL);
                conditionBean.setCategory(CATEGORY_NOTES_LABEL);
                conditionBean.setEditable(true);

                listProperties.add(conditionBean);

            } catch (IntrospectionException e) {
                LOG.error("Couldn't introspect an instance of EPCNoteItem", e);
            }
        }
    }

    /**
     * Adds the logic condition to operator(s). Double-conditions-operators (i.g. AND_OR) have two
     * conditions (TOP & BOTTOM), Single-condition-operators (i.g. AND) have only one condition.
     *
     * @param logicFunctionModel is the logic operator
     * @param listProperties is the list where is the info about condition supposed to be added
     */
    private void getLogicOperatorCondition(final LogicFunctionModel logicFunctionModel,
                                               final List<Property> listProperties) {

        try {
            final BeanProperty condition1Bean = new BeanProperty(
                    new PropertyDescriptor(LogicFunctionModel.CONDITION_1_PROPERTY, LogicFunctionModel.class)){

                    public void setValue(Object o) {
                        super.setValue(o);

                        // let the project diagram know about the change
                        final ProjectDiagram projectDiagram = ModelerSession.getProjectService().getSelectedDiagram();
                        if(projectDiagram != null){
                            projectDiagram.changePerformed(null);
                        }
                    }
            };

            final boolean doubleConditionOperator = isDoubleOperator(logicFunctionModel.getOperator());

            condition1Bean.setInstance(logicFunctionModel);
            if(doubleConditionOperator){
                condition1Bean.setName(LOGICS_CONDITION_TOP_LABEL);
            } else {
                condition1Bean.setName(LOGICS_CONDITION_LABEL);
            }
            condition1Bean.setCategory(CATEGORY_LOGICS_LABEL);
            condition1Bean.setEditable(true);
            listProperties.add(condition1Bean);

            if(doubleConditionOperator){
                final BeanProperty condition2Bean = new BeanProperty(
                        new PropertyDescriptor(LogicFunctionModel.CONDITION_2_PROPERTY, LogicFunctionModel.class)){

                        public void setValue(Object o) {
                            super.setValue(o);

                            // let the project diagram know about the change
                            final ProjectDiagram projectDiagram = ModelerSession.getProjectService().getSelectedDiagram();
                            if(projectDiagram != null){
                                projectDiagram.changePerformed(null);
                            }
                        }
                };

                condition2Bean.setInstance(logicFunctionModel);
                condition2Bean.setName(LOGICS_CONDITION_BOTTOM_LABEL);
                condition2Bean.setCategory(CATEGORY_LOGICS_LABEL);
                condition2Bean.setEditable(true);
                listProperties.add(condition2Bean);
            }

        } catch (IntrospectionException e) {
            LOG.error("Couldn't introspect an instance of LogicFunctionModel", e);
        }
    }

    /**
     * Checks for 2 condition operators.
     *
     * @param operator is the operator tbe checked
     * @return true if operator has 2 conditions, false otherwise
     */
    private boolean isDoubleOperator(final LogicFunctionModel.LogicOperator operator) {
        if(LogicFunctionModel.LogicOperator.AND_OR.equals(operator)
                || LogicFunctionModel.LogicOperator.AND_XOR.equals(operator)
                || LogicFunctionModel.LogicOperator.OR_AND.equals(operator)
                || LogicFunctionModel.LogicOperator.OR_XOR.equals(operator)
                || LogicFunctionModel.LogicOperator.XOR_AND.equals(operator)
                || LogicFunctionModel.LogicOperator.XOR_OR.equals(operator)){

            return true;
        }

        return false;                                    
    }

    /**
     * Determinate the type of selected graph item.
     *
     * @param userObject is the user's object of selected cell
     * @param listProperties is the list to that will be added the record about the type of cell
     */
    private void recognizeItemType(final Object userObject, final List<Property> listProperties) {
        final Property typeBean = new Property(){
                public void setValue(Object o) {
                }

                public Object getValue() {
                    if(userObject instanceof ApplicationSoftwareModel){
                        return APP_SW_LABEL;
                    } else if(userObject instanceof ComputerHWModel){
                        return HW_LABEL;
                    } else if(userObject instanceof DeliverableModel){
                        return DELIVERABLE_LABEL;
                    } else if(userObject instanceof EdgeModel){
                        return FLOW_LABEL + " " + getFlowType((EdgeModel) userObject);
                    } else if(userObject instanceof EventModel){
                        return EVENT_LABEL;
                    } else if(userObject instanceof FunctionModel){
                        return FUNCTION_LABEL;
                    } else if(userObject instanceof GoalModel){
                        return GOAL_LABEL;
                    } else if(userObject instanceof InformationObjectModel){
                        return INFO_OBJ_LABEL;
                    } else if(userObject instanceof LogicFunctionModel){
                        return LOGIC_OP_LABEL;
                    } else if(userObject instanceof MachineModel){
                        return MACHINE_LABEL;
                    } else if(userObject instanceof MessageModel){
                        return MESSAGE_LABEL;
                    } else if(userObject instanceof OrganizationRoleModel){
                        return ORG_ROLE_LABEL;
                    } else if(userObject instanceof OrganizationUnitModel){
                        return ORG_UNIT_LABEL;
                    } else {
                        LOG.error("An unknown type of EPC item.");
                        return "";
                    }
                }
            };

            typeBean.setName(TYPE_LABEL);
            typeBean.setCategory(CATEGORY_TYPE_LABEL);
            typeBean.setEditable(false);
            listProperties.add(typeBean);
    }

    /**
     * Get description for flow (edge).
     *
     * @param edgeModel is an instance of flow
     * @return the description of flow
     */
    private String getFlowType(final EdgeModel edgeModel) {
        StringBuffer type = new StringBuffer();
        type.append("(");

        switch (edgeModel.getEdgeType()){
            case CONTROL_FLOW:
                type.append(CTR_FLOW_LABEL);
                break;
            case INFORMATION_FLOW:
                type.append(INFO_FLOW_LABEL);
                break;
            case INFORMATION_SERVICES_FLOW:
                type.append(INF_SERVICES_FLOW_LABEL);
                break;
            case MATERIAL_FLOW:
                type.append(MATERIAL_FLOW_LABEL);
                break;
            case ORGANIZATION_FLOW:
                type.append(ORG_FLOW_LABEL);
                break;
            default:
                type.append("-");
                LOG.error("Unknown type of flow.");
        }

        type.append(")");

        return type.toString();
    }    

    /**
     * Finds newly selected vertex in the graph.
     *
     * @param event represents actual graph selection event
     * @return newly selected vertex; or null if there are no newly selected vertexes or more than one vertex is selected
     */
    private Object getNewlySelectedVertex(final GraphSelectionEvent event) {
        Object newCell = null;

        for(Object cell : event.getCells()){
            if(event.isAddedCell(cell)){
                if(newCell != null) {
                    return null; // multiple vertex selection
                }

                newCell = cell;
            }
        }

        return newCell;
    }

    private void updateInfo(final List<Property> listProperties, final Object userObject) {
        try{
            final EPCEditableVertex editableVertex = (EPCEditableVertex) userObject;

            final Property nameBean = new Property(){
                public void setValue(Object object) {
                    // do nothing
                }

                public Object getValue() {
                    return editableVertex.getName();
                }
            };

            nameBean.setName(NAME_LABEL);
            nameBean.setCategory(CATEGORY_GENERALS_LABEL);
            nameBean.setEditable(false);
            listProperties.add(nameBean);

            final Property uuidBean = new Property(){
                public void setValue(Object o) {
                    //do nothing
                }

                public Object getValue() {
                    return editableVertex.getUuid();
                }
            };

            uuidBean.setName(UUID_LABEL);
            uuidBean.setCategory(CATEGORY_GENERALS_LABEL);
            uuidBean.setEditable(false);
            listProperties.add(uuidBean);

        } catch(Exception exception){
            LOG.error("Introspection failed.", exception);
        }
    }

}
