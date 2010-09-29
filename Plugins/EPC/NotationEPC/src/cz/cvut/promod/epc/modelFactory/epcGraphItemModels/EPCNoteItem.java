package cz.cvut.promod.epc.modelFactory.epcGraphItemModels;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:34:42, 20.1.2010
 *
 * Base class for all EPC items, that can hold a note.
 */
public abstract class EPCNoteItem {

    public static final String NOTE_PROPERTY = "note";
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }
}
