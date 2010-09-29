package cz.cvut.promod.hierarchyNotation.workspace;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.apache.log4j.Logger;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.hierarchyNotation.resources.Resources;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotationModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.ProcessVertex;

import java.util.Map;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.*;

import com.jgoodies.binding.value.ValueModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:25:04, 25.1.2010
 *
 * Represents the actual drawing canvas of a ProcessHierarchyNotation plugin.
 */
public class ProcessHierarchyGraph extends JGraph {

    private static final Logger LOG = Logger.getLogger(ProcessHierarchyGraph.class);

    private static final String REFRESH_ACTIN_LABEL =
            Resources.getResources().getString(ProcessHierarchyNotationModel.REFRESH_ACTION_KEY);
    private static final String DELETE_ACTIN_LABEL =
            Resources.getResources().getString(ProcessHierarchyNotationModel.DELETE_ACTION_KEY);

    private final int GET_PORTVIEW_TOLERANCE = 15;

    private ProModAction removeAction;

            
    public ProcessHierarchyGraph(final Map<String, ProModAction> actions,
                                 final ValueModel selectedToolModel){

        initActions(actions);

        setMarqueeHandler(new ProcessHierarchyMarqueeHandler(this, selectedToolModel));
    }

    private void initActions(final Map<String, ProModAction> actions) {
        final ProModAction refreshAction = new ProModAction(
                REFRESH_ACTIN_LABEL,
                Resources.getIcon(Resources.ICONS + Resources.REFRESH), null){
            public void actionPerformed(ActionEvent event) {
                refresh();
            }
        };

        actions.put(ProcessHierarchyNotationModel.REFRESH_ACTION_KEY, refreshAction);

        removeAction = new ProModAction(
                DELETE_ACTIN_LABEL, null, null){
            public void actionPerformed(ActionEvent event) {
                if (!isSelectionEmpty()) {
                    final Object[] selectedCells = getSelectionCells();
                    unregisterListeners(selectedCells);
                    logDeleteInfo(selectedCells);
                    getGraphLayoutCache().remove(selectedCells, true, true);
                }
            }
        };

        actions.put(ProcessHierarchyNotationModel.DELETE_ACTION_KEY, removeAction);
    }

    /**
     * Un-register all vertex as listeners by the PCS.
     *
     * @param selectedCells to be unregistered
     */
    private void unregisterListeners(final Object[] selectedCells) {
        for(final Object cell : selectedCells){
            if(cell instanceof DefaultGraphCell){
                final DefaultGraphCell defaultGraphCell = (DefaultGraphCell) cell;
                if(defaultGraphCell.getUserObject() instanceof ProcessVertex){
                    final ProcessVertex processVertex = (ProcessVertex) defaultGraphCell.getUserObject();

                    ModelerSession.getProjectControlService().unRegisterListener(processVertex);
                }
            }
        }
    }

    /**
     * Log information about cells tha have been deleted.
     *
     * @param selectedCells that have been deleted
     */
    private void logDeleteInfo(final Object[] selectedCells){
        for(final Object cell : selectedCells){
            if(cell instanceof DefaultEdge){
                final DefaultEdge edge = (DefaultEdge) cell;
                LOG.info("Edge has been deleted, source: " + edge.getSource() + ", target: " + edge.getTarget() + ".");

            } else if(cell instanceof DefaultGraphCell){
                final DefaultGraphCell defaultGraphCell = (DefaultGraphCell) cell;
                final Object object = defaultGraphCell.getUserObject();

                LOG.info("Vertex has been deleted, detail info: " + cell + ", text: " + object + ".");

            } else {
                LOG.info("Uknown item has been deleted, object: " + cell + ".");
            }
        }
    }

    public PortView getSourcePortAt(final Point2D point) {
        setJumpToDefaultPort(false); // do not use default currentPort if no another currentPort is on the point

        PortView result;
        try{
            result = getPortViewAt(point.getX(), point.getY(), GET_PORTVIEW_TOLERANCE);
        }
        catch (Exception exception){
            LOG.error("Couldn't locate the portview.");
            result = null;
        }

        setJumpToDefaultPort(true);

        return result;
    }

    public PortView getTargetPortAt(final Point2D point) {
        return getPortViewAt(point.getX(), point.getY());
    }

    /**
     * Inserts a new vertex to the graph.
     *
     * @param point is the point where the new vertex is supposed to be added
     */
    public void insert(Point2D point) {
        final DefaultGraphCell vertex = new DefaultGraphCell(new ProcessVertex());

        vertex.getAttributes().applyMap(createCellAttributes(point));

        final DefaultPort defaultPort = new DefaultPort();
        vertex.add(defaultPort);

        getGraphLayoutCache().insert(vertex);
    }

    /**
     * Realizes the connection from one process to another.
     *
     * @param source is the port of source process
     * @param target is the port of target process
     */
	public void connectProcesses(final Port source, final Port target) {
		final DefaultEdge edge = new DefaultEdge();
        edge.setSource(source);
        edge.setTarget(target);

		if ((getModel().acceptsSource(edge, source)) && (getModel().acceptsTarget(edge, target))) {
			edge.getAttributes().applyMap(createEdgeAttributes());
			getGraphLayoutCache().insertEdge(edge, source, target);
		}
	}

    /**
     * Generates new attribute map for a new edge.
     *
     * @return new attribute map holding attributes for standard process hierarchy edge
     */
	private Map createEdgeAttributes() {
		final Map map = new Hashtable();
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_SIMPLE);
        GraphConstants.setLineStyle(map, GraphConstants.STYLE_ORTHOGONAL);
		GraphConstants.setLabelAlongEdge(map, false);
        GraphConstants.setEditable(map, false);
        GraphConstants.setMoveable(map, false);
        GraphConstants.setDisconnectable(map, false);

		return map;
	}

    /**
     * Generates new attribute map for a new vertex.
     *
     * @param point is the point where is the new vertex supposed to be inserted
     * @return new complete attribute map for new vertex
     */
    private Map createCellAttributes(final Point2D point) {
        final Map map = new Hashtable();

        final Point2D snappedpoint = snap((Point2D) point.clone());

        GraphConstants.setBounds(map, new Rectangle2D.Double(snappedpoint.getX(), snappedpoint.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setBackground(map, Color.white);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setInset(map, 5);

        return map;
    }

    /**
     * @return the remove-action.
     */
    public ProModAction getRemoveAction() {
        return removeAction;
    }
}
