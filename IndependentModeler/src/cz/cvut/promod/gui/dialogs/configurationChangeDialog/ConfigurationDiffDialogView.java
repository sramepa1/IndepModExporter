package cz.cvut.promod.gui.dialogs.configurationChangeDialog;

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
 * Represents the view component for ConfigurationDiffDialog.
 */
public class ConfigurationDiffDialogView extends JDialog {

    private static final Dimension DIMENSION = new Dimension(500, 450);

    private final JLabel titleLabel = ModelerSession.getComponentFactoryService().createLabel(
        ModelerSession.getCommonResourceBundle().getString("modeler.configuration.message")
    );
    private final JLabel messageLabel = ModelerSession.getComponentFactoryService().createLabel(
        ModelerSession.getCommonResourceBundle().getString("modeler.configuration.message.detail")
    );

    protected final JTextArea diffTextArea = ModelerSession.getComponentFactoryService().createTextArea();

    protected final JButton hideButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.hideButton"), null
    );

    public ConfigurationDiffDialogView(){
        initLayout();

        diffTextArea.setLineWrap(true);
        diffTextArea.setWrapStyleWord(true);

        diffTextArea.setEditable(false);

        setSize(DIMENSION);
    }

    private void initLayout() {
        final JPanel panel = ModelerSession.getComponentFactoryService().createPanel();

        panel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        panel.setLayout(new BorderLayout());

        final JPanel northPanel = ModelerSession.getComponentFactoryService().createPanel();
        northPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));
        northPanel.setLayout(new BorderLayout());

        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(messageLabel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(ModelerSession.getComponentFactoryService().createScrollPane(diffTextArea), BorderLayout.CENTER);
        panel.add(hideButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

}