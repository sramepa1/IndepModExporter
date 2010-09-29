package cz.cvut.promod.gui.dialogs.simpleTextFieldDialog;

import com.jgoodies.binding.beans.Model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:49:58, 23.10.2009
 *
 * A Model component for SimpleTextFieldDialog.
 */
public class SimpleTextFieldDialogModel extends Model {

    public static final String PROPERTY_SUBFOLDER_NAME = "inputText";
    private String inputText;

    public String getInputText() {
        return inputText;
    }

    public void setInputText(final String inputText) {
        final String oldSubFolderName = this.inputText;
        this.inputText = inputText;
        firePropertyChange(PROPERTY_SUBFOLDER_NAME, oldSubFolderName, inputText);
    }
}
