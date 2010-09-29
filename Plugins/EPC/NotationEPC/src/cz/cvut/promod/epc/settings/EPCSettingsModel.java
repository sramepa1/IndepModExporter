package cz.cvut.promod.epc.settings;

import com.jgoodies.binding.beans.Model;

import java.util.Properties;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:50:04, 27.1.2010
 *
 *  The model component for the EPCSettings. 
 */
public class EPCSettingsModel extends Model {

    private static final String UNDO_LIMIT_FILE_PROPERTY = "undo.limit";
    private static final String DEFAULT_UNDO_LIMIT = "10";    

    public static final int MIN_UNDO = 1;
    public static final int MAX_UNDO = 100;
    public static final int INIT_UNDO_STEP = 1;    

    private final Properties properties;

    public static final String UNDO_LIMIT_PROPERTY = "undoLimit";
    private int undoLimit;


    public EPCSettingsModel(final Properties properties) {
        this.properties = properties;

        initValues();
    }

    private void initValues() {
        int undoLimit;
        try{
            final String undoLimitString = properties.getProperty(UNDO_LIMIT_FILE_PROPERTY, DEFAULT_UNDO_LIMIT);
            undoLimit = Integer.parseInt(undoLimitString);

        } catch (Exception exception){
            undoLimit = Integer.parseInt(DEFAULT_UNDO_LIMIT);
        }

        setUndoLimit(undoLimit);
    }

    public void setUndoLimit(int undoLimit) {
        final int oldValue = this.undoLimit;
        this.undoLimit = undoLimit;
        firePropertyChange(UNDO_LIMIT_PROPERTY, oldValue, undoLimit);
    }

    public int getUndoLimit() {
        return undoLimit;
    }
}
