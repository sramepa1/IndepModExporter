package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import com.jgraph.components.labels.CellConstants;
import com.jgraph.components.labels.MultiLineVertexRenderer;
import org.jgraph.graph.GraphConstants;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import cz.cvut.promod.epc.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:15:53, 6.12.2009
 *
 * Represents the Function element of the EPCNotation plugin.
 */
public class FunctionModel extends EPCEditableVertex {

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.function");

    public static final int DEFAULT_INSET = 4;

    private final UUID uuid;


    public FunctionModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public FunctionModel(final FunctionModel functionModel, final String newName){
        this.uuid = functionModel.getUuid();
        setName(newName);
        setNote(functionModel.getNote());
    }

    public UUID getUuid() {
        return uuid;
    }


    @Override
    public String toString(){
        return name;
    }

    /**
     * Creates attributes for an epc function.
     *
     * @param point is the position where to insert a new epc function
     * @return the map holding all attributes for a new epc function
     */
    public static Map installAttributes(final Point2D point) {
        final Map map = new Hashtable();

        map.put(CellConstants.VERTEXSHAPE, MultiLineVertexRenderer.SHAPE_ROUNDED);

        GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(), point.getY(), 0, 0));
        GraphConstants.setResize(map, true);
        GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setOpaque(map, true);
        GraphConstants.setInset(map, DEFAULT_INSET);

        return map;
    }
    
}
