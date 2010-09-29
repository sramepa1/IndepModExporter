package cz.cvut.promod.services.projectService.dialogs;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.factories.Borders;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:06:00, 13.11.2009
 */

/**
 * SyncDialog view.
 */
public class SyncDialogView extends JDialog {

    private final JLabel errorsLabel = ModelerSession.getComponentFactoryService().createLabel(
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.errorlist"));

    private final JLabel searchLocationLabel = ModelerSession.getComponentFactoryService().createLabel(
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.searching"));

    protected final JLabel currentSearchLocationLabel = ModelerSession.getComponentFactoryService().createLabel("");

    protected final JTextArea errorsTextArea = ModelerSession.getComponentFactoryService().createTextArea();

    protected final JButton cancelButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("project.service.sync.dialog.cancel"), null);

    protected final JButton okButton = ModelerSession.getComponentFactoryService().createButton(
            ModelerSession.getCommonResourceBundle().getString("modeler.ok"), null);

    public SyncDialogView(final String title){
        super(ModelerSession.getFrame(), title);

        setLocationRelativeTo(ModelerSession.getFrame());

        setSize(600, 350);

        errorsTextArea.setEditable(false);

        initLayout();
    }

    private void initLayout() {
        final JPanel panel = ModelerSession.getComponentFactoryService().createPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        final CellConstraints cellConstraints = new CellConstraints();

        final JPanel northPanel = ModelerSession.getComponentFactoryService().createPanel();
        northPanel.setLayout(new FormLayout(
                "pref:grow, 10dlu, pref",
                "pref, 3dlu, pref, 7dlu"
        ));

        northPanel.add(searchLocationLabel, cellConstraints.xy(1,1));
        northPanel.add(cancelButton, cellConstraints.xy(3,1));

        northPanel.add(currentSearchLocationLabel, cellConstraints.xyw(1, 3, 3));

        final JPanel centerPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerPanel.setLayout(new BorderLayout());

        centerPanel.add(errorsLabel, BorderLayout.NORTH);
        centerPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(errorsTextArea), BorderLayout.CENTER);

        final JPanel southPanel = ModelerSession.getComponentFactoryService().createPanel();
        southPanel.setLayout(new FormLayout(
                "right:pref:grow",
                "7dlu, pref"
        ));
        southPanel.add(okButton, cellConstraints.xy(1,2));

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);
        add(panel);
    }
}
