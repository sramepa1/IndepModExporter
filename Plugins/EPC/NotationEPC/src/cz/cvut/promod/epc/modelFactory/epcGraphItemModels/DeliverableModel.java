package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import com.jgraph.components.labels.CellConstants;

import java.util.*;
import java.awt.geom.Point2D;
import java.awt.*;

import cz.cvut.promod.epc.workspace.cell.EPCVertexRenderer;
import cz.cvut.promod.epc.workspace.cell.borders.DoubleLineBorder;
import cz.cvut.promod.epc.resources.Resources;
import org.jgraph.graph.GraphConstants;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:03:20, 7.12.2009
 *
 * Represents the Deliverable element of the EPCNotation plugin.
 */
public class DeliverableModel extends EPCEditableVertex {

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.deliverable");

    public static final int DEFAULT_INSET = 5;

    private final UUID uuid;


    public DeliverableModel(final UUID uuid){
        this.uuid = uuid;        
        setName(DEFAULT_LABEL);
    }

    public DeliverableModel(final DeliverableModel deliverableModel, final String name){
        uuid = deliverableModel.getUuid();
        setName(name);
        setNote(deliverableModel.getNote());
    }

    @Override
    public String toString(){
        return name;
    }

    /**
     * Initialize new vertex attributes.
     *
     * @param point is the point where new vertex is supposed to be inserted
     * @return a proper attribute map for new vertex
     */
    public static Map installAttributes(final Point2D point) {
        final Map map = new Hashtable();

        map.put(CellConstants.VERTEXSHAPE, EPCVertexRenderer.SHAPE_RECTANGLE);

        GraphConstants.setBounds(map, new Rectangle.Double(point.getX(), point.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setBorder(map, new DoubleLineBorder(Color.BLACK));
        GraphConstants.setSizeable(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);

        return map;
    }

    public UUID getUuid() {
        return uuid;
    }
    
}
