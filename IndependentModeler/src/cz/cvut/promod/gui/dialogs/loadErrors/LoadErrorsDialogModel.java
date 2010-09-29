package cz.cvut.promod.gui.dialogs.loadErrors;

import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.ModelerSession;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:55:46, 12.12.2009
 *
 * The model component for LoadErrorsDialog.
 */
public class LoadErrorsDialogModel {

    private final List<PluginLoadErrors> errors;

    public static final String PROPERTY_NAME =
            ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.propertyName");

    public static final String FULL_CLASS_NAME =
            ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.fullClassName");

    public static final String ERROR =
            ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.error");

    public static final String MESSAGE =
        ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.error.message");


    public LoadErrorsDialogModel(final List<PluginLoadErrors> errors) {
        this.errors = errors;
    }

    public List<PluginLoadErrors> getErrors() {
        return errors;
    }
}
