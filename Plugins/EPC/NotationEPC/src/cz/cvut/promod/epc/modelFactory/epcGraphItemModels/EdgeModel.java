package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import java.util.UUID;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:34:37, 19.1.2010
 *
 * Represents the general edge element of the EPCNotation plugin.
 */
public class EdgeModel extends EPCEditableVertex {

    public static enum EdgeType {
        CONTROL_FLOW,
        ORGANIZATION_FLOW,
        INFORMATION_FLOW,
        INFORMATION_SERVICES_FLOW,
        MATERIAL_FLOW
    }

    private final EdgeType edgeType;

    public EdgeModel(final EdgeType edgeType){
        this.edgeType = edgeType;
    }

    public EdgeModel(final EdgeModel edgeModel, final String newName){
        this.edgeType = edgeModel.getEdgeType();
        setName(newName);
        setNote(edgeModel.getNote());
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    @Override
    public String toString() {
        return name;
    }

    public UUID getUuid() {
        return null; // edges do not have uuid
    }
}
