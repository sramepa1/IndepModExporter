package cz.cvut.promod.gui.dialogs.loadErrors;

import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:55:32, 12.12.2009
 *
 * Dialog displaying errors and error reasons that have occurred at load time.
 */
public class LoadErrorsDialog extends LoadErrorsDialogView{

    public final String NEW_LINE = "\n";

    private final LoadErrorsDialogModel model;

    public LoadErrorsDialog(final List<PluginLoadErrors> errors){
        setTitle(
                ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.title")
        );

        model = new LoadErrorsDialogModel(errors);

        initEventHandling();

        initErrorReport();

        setModal(true);

        getRootPane().setDefaultButton(hideButton);

        setVisible(true);
    }

    /**
     * Initialize event handling.
     */
    private void initEventHandling() {
        hideButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                disposeDialog();
            }
        });

        getRootPane().registerKeyboardAction(new ActionListener(){
                    public void actionPerformed(ActionEvent actionEvent) {
                        disposeDialog();
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);        
    }

    /**
     * Hides the dialog.
     */
    private void disposeDialog(){
        setVisible(false);
        dispose();
    }

    /**
     * Initialize error report.
     */
    private void initErrorReport() {
        for(final PluginLoadErrors pluginLoadErrors : model.getErrors()){
            
            errorTextArea.append(LoadErrorsDialogModel.PROPERTY_NAME + " " + pluginLoadErrors.getAlias());
            errorTextArea.append(NEW_LINE);

            if(pluginLoadErrors.getFullClassName() != null){
                errorTextArea.append(LoadErrorsDialogModel.FULL_CLASS_NAME + " " + pluginLoadErrors.getFullClassName());
                errorTextArea.append(NEW_LINE);
            }

            errorTextArea. append(LoadErrorsDialogModel.ERROR + " " + pluginLoadErrors.getTranslatedError());
            errorTextArea.append(NEW_LINE);

            if(pluginLoadErrors.getMessage() != null){
                errorTextArea.append(LoadErrorsDialogModel.MESSAGE + " " + pluginLoadErrors.getMessage());
                errorTextArea.append(NEW_LINE);
            }

            errorTextArea.append(NEW_LINE);
        }
    }

}
