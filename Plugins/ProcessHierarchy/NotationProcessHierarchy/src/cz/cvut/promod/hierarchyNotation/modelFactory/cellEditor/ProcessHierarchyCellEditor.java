package cz.cvut.promod.hierarchyNotation.modelFactory.cellEditor;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphCellEditor;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.ProcessVertex;
import cz.cvut.promod.services.ModelerSession;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:29:27, 16.4.2010
 *
 * An implementation of DefaultGraphCellEditor for the ProcessHierarchyNotation plugin.
 */
public class ProcessHierarchyCellEditor  extends DefaultGraphCellEditor {
    
    private static final Logger LOG = Logger.getLogger(ProcessHierarchyCellEditor.class);

    private static String errorString = " - ";

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

            if(oldUserObject instanceof ProcessVertex){
                newUserObject = new ProcessVertex((ProcessVertex)oldUserObject, newName);
                ModelerSession.getProjectControlService().switchChangeListener((ProcessVertex)oldUserObject, (ProcessVertex)newUserObject);

            } else {
                // should never happened, testing & debugging purposes
                LOG.error("Unknown PH vertex model");
                newUserObject = errorString;
            }

            return newUserObject;

        } else {
            // should never happened, testing & debugging purposes
            LOG.error("PH vertexes is not an instances of DefaultGraphCell class.");
        }

        return errorString;
    }

}
