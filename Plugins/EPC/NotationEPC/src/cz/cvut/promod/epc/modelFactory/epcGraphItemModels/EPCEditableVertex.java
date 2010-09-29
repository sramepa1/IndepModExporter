package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:57:04, 9.12.2009
 *
 * Represents the editable element of the EPCNotation plugin notation.
 */
public abstract class EPCEditableVertex extends EPCNoteItem implements EPCIdentifiableVertex{

    public static final String PROPERTY_NAME = "name";
    protected String name;        

    public String getName(){
        return name;
    }

    public void setName(final String name){
        this.name = name;      
    }

}
