package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import cz.cvut.promod.epc.resources.Resources;
import cz.cvut.promod.epc.workspace.cell.EPCVertexRenderer;
import cz.cvut.promod.epc.workspace.cell.borders.ComputerHWBorder;

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
 * Date: 0:20:20, 19.1.2010
 *
 * Represents the Computer HW element of the EPCNotation plugin.
 */
public class ComputerHWModel extends EPCEditableVertex{

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.hw");

    public static final int DEFAULT_INSET = 5;

    private final UUID uuid;


    public ComputerHWModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public ComputerHWModel(final ComputerHWModel computerHWModel, final String name){
        uuid = computerHWModel.getUuid();
        setName(name);
        setNote(computerHWModel.getNote());
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
        GraphConstants.setBorder(map, new ComputerHWBorder(Color.BLACK));
        GraphConstants.setSizeable(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);

        return map;
    }

    public UUID getUuid() {
        return uuid;
    }

}
