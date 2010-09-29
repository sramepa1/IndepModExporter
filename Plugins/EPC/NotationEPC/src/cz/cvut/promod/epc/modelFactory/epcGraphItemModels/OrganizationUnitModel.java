package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import com.jgraph.components.labels.CellConstants;
import com.jgraph.components.labels.MultiLineVertexRenderer;

import java.util.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.*;

import org.jgraph.graph.GraphConstants;

import cz.cvut.promod.epc.resources.Resources;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:22:05, 7.12.2009
 *
 * Represents the Organization Unit of the EPCNotation plugin.
 */
public class OrganizationUnitModel extends EPCEditableVertex{

    private static final String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.org.unit");

    public static final int DEFAULT_INSET = 6;

    private final UUID uuid;

    
    public OrganizationUnitModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public OrganizationUnitModel(final OrganizationUnitModel organizationUnitModel, final String name){
        setName(name);
        uuid = organizationUnitModel.getUuid();
        setNote(organizationUnitModel.getNote());

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

        map.put(CellConstants.VERTEXSHAPE, MultiLineVertexRenderer.SHAPE_CIRCLE);

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
