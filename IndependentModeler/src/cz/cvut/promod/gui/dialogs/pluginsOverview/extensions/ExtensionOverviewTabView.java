package cz.cvut.promod.gui.dialogs.pluginsOverview.extensions;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.gui.dialogs.pluginsOverview.PluginsOverviewDialogView;
import cz.cvut.promod.gui.dialogs.pluginsOverview.PluginsOverviewDialogDialog;

import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:50:08, 11.2.2010
 *
 * Tab for basic information demonstration about the extensions.
 */
public class ExtensionOverviewTabView extends JPanel {

    protected final JList extensionList = ModelerSession.getComponentFactoryService().createList();

    private final JLabel identifierLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.IDENTIFIER_LABEL);
    private final JLabel nameLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.NAME_LABEL);
    private final JLabel descriptionLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.DESCRIPTION_LABEL);
    private final JLabel extensionLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.EXTENSION_LABEL);

    protected final JTextField identifierTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextField nameTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextArea descriptionTextArea =
            ModelerSession.getComponentFactoryService().createTextArea();

    public ExtensionOverviewTabView(){
        identifierTextArea.setEditable(false);
        nameTextArea.setEditable(false);
        descriptionTextArea.setEditable(false);        
        
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        extensionList.setFixedCellWidth(PluginsOverviewDialogView.LIST_WIDTH);

        initLayout();
    }

    private void initLayout() {
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        final JPanel centerPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        centerPanel.setLayout(new FormLayout(
                "pref:grow",
                "pref, 3dlu, pref, 7dlu, pref, 3dlu, pref, 7dlu, pref, 3dlu, fill:pref:grow"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        centerPanel.add(identifierLabel, cellConstraints.xy(1,1));
        centerPanel.add(identifierTextArea, cellConstraints.xy(1,3));
        centerPanel.add(nameLabel, cellConstraints.xy(1,5));
        centerPanel.add(nameTextArea, cellConstraints.xy(1,7));
        centerPanel.add(descriptionLabel, cellConstraints.xy(1,9));
        centerPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(descriptionTextArea), cellConstraints.xy(1,11));

        JPanel listPanel = ModelerSession.getComponentFactoryService().createPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.add(extensionLabel, BorderLayout.NORTH);
        listPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(extensionList), BorderLayout.CENTER);

        setLayout(new BorderLayout(PluginsOverviewDialogView.HGAP, PluginsOverviewDialogView.HGAP));
        add(centerPanel, BorderLayout.CENTER);
        add(listPanel, BorderLayout.WEST);
    }

}