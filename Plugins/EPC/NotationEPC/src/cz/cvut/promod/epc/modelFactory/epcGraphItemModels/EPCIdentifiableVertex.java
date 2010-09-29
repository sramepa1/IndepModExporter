package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

import java.util.UUID;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:13:18, 9.12.2009
 *
 * Represents the identifiable element of the EPCNotation plugin notation.
 */
public interface EPCIdentifiableVertex {

    public static final String PROPERTY_UUID = "uuid";

    /**
     * All objects of classes implementing this interface are identifiable.
     *
     * @return uuid
     */
    public UUID getUuid();

}
