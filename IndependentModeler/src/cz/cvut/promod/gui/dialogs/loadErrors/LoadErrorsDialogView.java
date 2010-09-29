package cz.cvut.promod.gui.dialogs.loadErrors;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.factories.Borders;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:55:41, 12.12.2009
 *
 * The view component for LoadErrorsDialog.
 */
public class LoadErrorsDialogView extends JDialog {

    private static final Dimension DIMENSION = new Dimension(500, 350);

    private final JLabel errorLabel = ModelerSession.getComponentFactoryService().createLabel(
        ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.message")
    );

    private final JLabel checkLogLabel = ModelerSession.getComponentFactoryService().createLabel(
        ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.log")
    );

    protected final JTextArea errorTextArea = ModelerSession.getComponentFactoryService().createTextArea();

    protected final JButton hideButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.loadErrorsDialog.hideButton"), null
    );

    public LoadErrorsDialogView(){
        initLayout();

        errorTextArea.setLineWrap(true);
        errorTextArea.setWrapStyleWord(true);

        errorTextArea.setEditable(false);

        setSize(DIMENSION);
    }

    private void initLayout() {
        final JPanel panel = ModelerSession.getComponentFactoryService().createPanel();

        panel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        panel.setLayout(new BorderLayout());

        final JPanel northPanel = ModelerSession.getComponentFactoryService().createPanel();
        northPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        northPanel.setLayout(new BorderLayout());

        northPanel.add(errorLabel, BorderLayout.NORTH);
        northPanel.add(checkLogLabel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(ModelerSession.getComponentFactoryService().createScrollPane(errorTextArea), BorderLayout.CENTER);
        panel.add(hideButton, BorderLayout.SOUTH);
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

}
