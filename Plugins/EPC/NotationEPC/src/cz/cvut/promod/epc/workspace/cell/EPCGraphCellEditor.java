package cz.cvut.promod.epc.workspace.cell;

import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.DefaultGraphCell;
import org.apache.log4j.Logger;
import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:48:33, 9.12.2009
 *
 * Special implementation of the EPCGraphCellEditor for the EPCNotation plugin.
 */
public class EPCGraphCellEditor extends DefaultGraphCellEditor {

    private static final Logger LOG = Logger.getLogger(EPCGraphCellEditor.class);

    private static String errorString = "ERROR";

    /**
     * When one edits the graph vertex, then an instance of DefaultGraphCellEditor is used. When editing is finished,
     * one of methods of CellEditorListener interface is invoked. Finally completeEditing(boolean, boolean, boolean)
     * method in the BasicGraphUI class is used and this class sets new UserObject which gets by getCellEditorValue() 
     * method invocation.
     *
     * So, this method overrides super class implementation of getCellEditorValue() method and return appropriate
     * object of appropriate class. 
     *
     * @return the cell editor value
     */
    @Override
    public Object getCellEditorValue() {
        if(lastCell instanceof DefaultGraphCell){
            final DefaultGraphCell defaultGraphCell = (DefaultGraphCell) lastCell;
            final Object oldUserObject = defaultGraphCell.getUserObject();

            final Object newUserObject;
            final String newName = (String) realEditor.getCellEditorValue();

            if(oldUserObject instanceof EdgeModel){
                newUserObject = new EdgeModel((EdgeModel)oldUserObject, newName);

            } else if(oldUserObject instanceof FunctionModel){
                newUserObject = new FunctionModel((FunctionModel)oldUserObject, newName);

            } else if(oldUserObject instanceof EventModel){
                newUserObject = new EventModel(((EventModel)oldUserObject), newName);

            } else if(oldUserObject instanceof DeliverableModel){
                newUserObject = new DeliverableModel(((DeliverableModel)oldUserObject), newName);

            } else if(oldUserObject instanceof InformationObjectModel){
                newUserObject = new InformationObjectModel(((InformationObjectModel)oldUserObject), newName);

            } else if(oldUserObject instanceof OrganizationRoleModel){
                newUserObject = new OrganizationRoleModel(((OrganizationRoleModel)oldUserObject), newName);

            } else if(oldUserObject instanceof OrganizationUnitModel){
                newUserObject = new OrganizationUnitModel(((OrganizationUnitModel)oldUserObject), newName);

            } else if(oldUserObject instanceof ApplicationSoftwareModel){
                    newUserObject = new ApplicationSoftwareModel((ApplicationSoftwareModel) oldUserObject, newName);

            } else if(oldUserObject instanceof ComputerHWModel){
                    newUserObject = new ComputerHWModel((ComputerHWModel) oldUserObject, newName);

            } else if(oldUserObject instanceof GoalModel){
                    newUserObject = new GoalModel((GoalModel) oldUserObject, newName);

            } else if(oldUserObject instanceof MachineModel){
                    newUserObject = new MachineModel((MachineModel) oldUserObject, newName);

            } else if(oldUserObject instanceof LogicFunctionModel){
                // should never happened, testing & debugging purposes
                LOG.error("EPC logical function vertexes are not supposed to be editable.");
                newUserObject = errorString;

            } else if(oldUserObject instanceof MessageModel){
                // should never happened, testing & debugging purposes
                LOG.error("EPC message vertexes are not supposed to be editable.");
                newUserObject = "";

            } else {
                // should never happened, testing & debugging purposes
                LOG.error("Unknown EPC vertex model");
                newUserObject = errorString;
            }

            return newUserObject;

        } else {
            // should never happened, testing & debugging purposes
            LOG.error("EPC vertexes is not an instances of DefaultGraphCell class.");
        }

        return errorString;
    }

}
