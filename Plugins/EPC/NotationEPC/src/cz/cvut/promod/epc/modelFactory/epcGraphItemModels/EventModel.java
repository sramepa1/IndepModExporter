package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import com.jgraph.components.labels.CellConstants;
import cz.cvut.promod.epc.workspace.cell.EPCVertexRenderer;
import cz.cvut.promod.epc.resources.Resources;
import org.jgraph.graph.GraphConstants;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:02:18, 7.12.2009
 *
 * Represents the Event element of the EPCNotation plugin.
 */
public class EventModel extends EPCEditableVertex {

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.event");

    public static final int DEFAULT_INSET = 5;

    private final UUID uuid;


    public EventModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public EventModel(final EventModel eventModel, final String name){
        uuid = eventModel.getUuid();
        setName(name);
        setNote(eventModel.getNote());
    }

    @Override
    public String toString(){
        return name;
    }

    /**
    /**
     * Initialize new vertex attributes.
     *
     * @param point is the point where new vertex is supposed to be inserted
     * @return a proper attribute map for new vertex
     */
    public static Map installAttributes(final Point2D point) {
        final Map map = new Hashtable();

        map.put(CellConstants.VERTEXSHAPE, EPCVertexRenderer.SHAPE_EPC_EVENT);
        GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(), point.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);

        return map;
    }

    public UUID getUuid() {
        return uuid;
    }

}
