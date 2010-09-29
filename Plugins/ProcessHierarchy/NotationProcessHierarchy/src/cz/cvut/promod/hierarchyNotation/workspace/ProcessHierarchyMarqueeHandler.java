package cz.cvut.promod.hierarchyNotation.workspace;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.PortView;
import org.jgraph.graph.Port;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.event.MouseEvent;
import java.awt.*;

import cz.cvut.promod.hierarchyNotation.frames.toolChooser.ToolChooserModel;
import com.jgoodies.binding.value.ValueModel;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:46:24, 25.1.2010
 *
 * Implementation of MarqueeHandler for Process Hierarchy Notation. 
 */
public class ProcessHierarchyMarqueeHandler extends BasicMarqueeHandler{

    private PortView currentPort = null;
    private PortView startingPort = null;

    private Point2D startingPoint;
    private Point2D point;

    private final ProcessHierarchyGraph graph;

    final ValueModel selectedToolModel;


    public ProcessHierarchyMarqueeHandler(final ProcessHierarchyGraph graph,
                                          final ValueModel selectedToolModel){

        this.graph = graph;
        this.selectedToolModel = selectedToolModel;
    }

    /** {@inheritDoc}
     *
     * Use this handler when adding a new vertex or edge. 
     */
    public boolean isForceMarqueeEvent(MouseEvent e) {
        if (addingVertex(e) || deletingVertex(e)){
            // when one click by the left mouse button and adds a new vertex, don't use any other handler
            return true;
        }

        currentPort = graph.getSourcePortAt(e.getPoint());

        return currentPort != null && graph.isPortsVisible() || super.isForceMarqueeEvent(e);

    }

    /** {@inheritDoc} */
    public void mousePressed(final MouseEvent e) {
        if(addingVertex(e)){
            graph.insert(e.getPoint());

        } else if(deletingVertex(e)){
            // delete selected vertexes
            final Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
            graph.getSelectionModel().setSelectionCell(cell);
            graph.getRemoveAction().actionPerformed(null);

        } else if((currentPort != null) && (graph.isPortsVisible())){
            // isPortsVisible() == true <-> selected tool is AddEdge
            
            startingPort = currentPort;
            startingPoint = graph.toScreen(currentPort.getLocation());

        } else {
            super.mousePressed(e);
        }
    }

    /** {@inheritDoc} */
    public void mouseDragged(MouseEvent e) {
        if (startingPoint != null) {
            final PortView newPort = graph.getTargetPortAt(e.getPoint());

            if (newPort == null || newPort != currentPort){
                paintConnector(Color.black, graph.getBackground());
                currentPort = newPort;

                if (currentPort != null){
                    point = graph.toScreen(currentPort.getLocation());

                } else {
                    point = graph.snap(e.getPoint());
                }

                paintConnector(graph.getBackground(), Color.black);
            }
        }
        
        super.mouseDragged(e);
    }


    protected void paintConnector(final Color foreground,
                                  final Color background){

        final Graphics graphics = graph.getGraphics();

        if (graph.isXorEnabled()) {
            graphics.setColor(foreground);
            graphics.setXORMode(background);

            drawTempLine(graphics);

        } else {
            final Rectangle rectangle = new Rectangle((int) startPoint.getX(), (int) startPoint.getY(), 1, 1);

            if (point != null) {
                rectangle.add(point);
            }

            rectangle.grow(1, 1);

            graph.repaint(rectangle);
        }
    }

    protected void drawTempLine(final Graphics g) {
        if (startingPort != null && startingPoint != null && point != null) {
            g.drawLine((int) startingPoint.getX(), (int) startingPoint.getY(), (int) point.getX(), (int) point.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e != null && currentPort != null && startingPort != null && startingPort != currentPort) {
            graph.connectProcesses((Port) startingPort.getCell(), (Port) currentPort.getCell());
            e.consume();
        }

        graph.repaint();

        startingPort = null;
        currentPort = null;
        startingPoint = null;
        point = null;

        /**
         * Consume event when deleting cells using Remove tool. This prevents before NullPointerException
         * occurring when graph repaints handles of already deleted edges.
         */
        if(e!= null && ToolChooserModel.Tool.REMOVE.equals(selectedToolModel.getValue())){
            e.consume();
        }        

        super.mouseReleased(e);
    }

    /**
     * Show CROSSHAIR_CURSOR cursor when the mouse is over any portview.
     * @param event is the MouseEvent that has occurred
     */
    public void mouseMoved(MouseEvent event) {

        // (isPortsVisible()== true) <-> ports are visible only when the AddEdge tool is selected
        if ((event != null) && (graph.isPortsVisible()) && (graph.getSourcePortAt(event.getPoint()) != null)){
            graph.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            event.consume(); // BasicGraphUI should stop process this

        } else {
            super.mouseMoved(event);
        }
    }


    /**
     * Checks whether a new vertex (process) is being added.
     *
     * @param e is the mouse event that initialized this process
     * @return true if a new vertex is being added
     */
    private boolean addingVertex(final MouseEvent e){
        return (SwingUtilities.isLeftMouseButton(e))
                &&
                (selectedToolModel.getValue() == ToolChooserModel.Tool.ADD_PROCESS);
    }

    /**
     * Decides whether the user is deleting an existing vertex or no.
     *
     * @param e is the current mouse event
     * @return true if user is removing vertex, false otherwise
     */
    private boolean deletingVertex(final MouseEvent e) {
        final ToolChooserModel.Tool tool;
        try{
            tool = (ToolChooserModel.Tool) selectedToolModel.getValue();
        } catch (ClassCastException exception){
            return false;
        }

        return (SwingUtilities.isLeftMouseButton(e) && ToolChooserModel.Tool.REMOVE.equals(tool));
    }
}
