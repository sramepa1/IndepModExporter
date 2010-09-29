package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.epc.workspace.cell.EPCVertexRenderer;

import java.util.UUID;
import java.util.Map;
import java.util.Hashtable;
import java.awt.geom.Point2D;
import java.awt.*;

import com.jgraph.components.labels.CellConstants;
import org.jgraph.graph.GraphConstants;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 10:04:35, 19.1.2010
 *
 * Represents the Goal element of the EPCNotation plugin.
 */
public class GoalModel extends EPCEditableVertex{

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.goal");

    public static final int DEFAULT_INSET = 5;

    private final UUID uuid;


    public GoalModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public GoalModel(final GoalModel goalModel, final String name){
        uuid = goalModel.getUuid();
        setName(name);
        setNote(goalModel.getNote());
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

        map.put(CellConstants.VERTEXSHAPE, EPCVertexRenderer.SHAPE_EPC_GOAL);

        GraphConstants.setBounds(map, new Rectangle.Double(point.getX(), point.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setSizeable(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);

        return map;
    }

    public UUID getUuid() {
        return uuid;
    }


}
