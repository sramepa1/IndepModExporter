package cz.cvut.promod.epcImageExport.frames.imageExport;

import com.jgoodies.binding.beans.Model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:48:51, 13.12.2009
 *
 * The model component of the Image Export dockable frame.
 */
public class ImageExportModel extends Model {

    public static String EXPORT_AS_LABEL_RES = "epc.image.export.as.label";
    public static String EXPORT_BUTTON_RES = "epc.image.export.button";
    public static String PNG_OPTIONS_INSET_RES = "epc.image.export.png.inset";

    public static String FRAME_TITLE_RES = "epc.image.export.frame.label";

    public static String PNG_EXTENSION = ".png";
    public static String PNG_FORMAT_NAME = "png";

    public static final int MIN_INSET = 0;
    public static final int MAX_INSET = 100;
    public static final int EXTENT = 0;
    public static final int INIT_INSET = 5;
    public static final int INIT_SPINNER_STEP = 1;
    public static final String PROPERTY_INSET = "inset";
    private int inset = 20;

    public int getInset() {
        return inset;
    }

    public void setInset(final int inset) {
        final int oldValue = this.inset;
        this.inset = inset;
        firePropertyChange(PROPERTY_INSET, oldValue, inset);
    }

}
