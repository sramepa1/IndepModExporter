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
 * Date: 19:14:14, 7.12.2009
 *
 * Represents the Organization role element of the EPCNotation plugin.
 */
public class OrganizationRoleModel extends EPCEditableVertex{

    private static String DEFAULT_LABEL = Resources.getResources().getString("epc.vertex.org.role");

    public static final int DEFAULT_INSET = 4;

    private final UUID uuid;


    public OrganizationRoleModel(final UUID uuid){
        this.uuid = uuid;
        setName(DEFAULT_LABEL);
    }

    public OrganizationRoleModel(final OrganizationRoleModel organizationRoleModel, final String name){
        setName(name);
        uuid = organizationRoleModel.getUuid();
        setNote(organizationRoleModel.getNote());
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

        map.put(CellConstants.VERTEXSHAPE, MultiLineVertexRenderer.SHAPE_RECTANGLE);

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