package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import cz.cvut.promod.epc.workspace.cell.EPCVertexRenderer;

import java.util.UUID;
import java.util.Map;
import java.util.Hashtable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.*;

import com.jgraph.components.labels.CellConstants;
import org.jgraph.graph.GraphConstants;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 10:32:01, 19.1.2010
 *
 * Represents the Message element of the EPCNotation plugin.
 */
public class MessageModel extends EPCNoteItem implements EPCIdentifiableVertex {

    public static final int INIT_WIDTH = 50;
    public static final int INIT_HEIGHT = 25;

    public static final int DEFAULT_INSET = 5;

    private final UUID uuid;


    public MessageModel(final UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "    ";
    }

    /**
     * Initialize new vertex attributes.
     *
     * @param point is the point where new vertex is supposed to be inserted
     * @return a proper attribute map for new vertex
     */
    public static Map installAttributes(final Point2D point) {
        final Map map = new Hashtable();

        map.put(CellConstants.VERTEXSHAPE, EPCVertexRenderer.SHAPE_EPC_ENVELOPE);
        GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(), point.getY(), INIT_WIDTH, INIT_HEIGHT));
        GraphConstants.setEditable(map, false);
        GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);
        GraphConstants.setSizeable(map, false);

        return map;
    }

    public UUID getUuid() {
        return uuid;
    }

}
