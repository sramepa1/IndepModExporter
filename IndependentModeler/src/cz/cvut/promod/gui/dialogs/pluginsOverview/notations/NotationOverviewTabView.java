package cz.cvut.promod.gui.dialogs.pluginsOverview.notations;

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
 * A View component of NotationOverviewTab.
 */

public class NotationOverviewTabView extends JPanel {


    protected final JList notationsList = ModelerSession.getComponentFactoryService().createList();
    protected final JList modulesList = ModelerSession.getComponentFactoryService().createList();

    private final JLabel identifierLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.IDENTIFIER_LABEL);
    private final JLabel nameLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.NAME_LABEL);
    private final JLabel descriptionLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.DESCRIPTION_LABEL);
    private final JLabel abbreviationLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.ABBREVIATION_LABEL);
    private final JLabel fullNameLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.FULL_NAME_LABEL);
    private final JLabel toolTipLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.TOOL_TIP_LABEL);
    private final JLabel fileExtensionLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.FILE_EXTENSION_LABEL);
    private final JLabel notationLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.NOTATION_LABEL);
    private final JLabel moduleLabel =
            ModelerSession.getComponentFactoryService().createLabel(PluginsOverviewDialogDialog.MODULE_LABEL);

    protected final JTextField identifierTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextField nameTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextField fullNameTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextArea descriptionTextArea =
            ModelerSession.getComponentFactoryService().createTextArea();
    protected final JTextField abbreviationTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextField toolTipTextArea =
            ModelerSession.getComponentFactoryService().createTextField();
    protected final JTextField fileExtensionTextArea =
            ModelerSession.getComponentFactoryService().createTextField();


    public NotationOverviewTabView(){
        identifierTextArea.setEditable(false);
        fullNameTextArea.setEditable(false);
        nameTextArea.setEditable(false);
        abbreviationTextArea.setEditable(false);
        toolTipTextArea.setEditable(false);
        fileExtensionTextArea.setEditable(false);
        descriptionTextArea.setEditable(false);

        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        notationsList.setFixedCellWidth(PluginsOverviewDialogView.LIST_WIDTH);
        modulesList.setFixedCellWidth(PluginsOverviewDialogView.LIST_WIDTH);

        initLayout();
    }

    private void initLayout() {
        setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        final JPanel topCenterPanel = ModelerSession.getComponentFactoryService().createPanel();
        topCenterPanel.setBorder(Borders.createEmptyBorder(ComponentFactoryService.DEFAULT_FORM_BORDER));

        topCenterPanel.setLayout(new FormLayout(
                "pref, 3dlu, pref:grow",
                "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 10dlu, pref"
        ));
        final CellConstraints cellConstraints = new CellConstraints();

        int row = 1;
        topCenterPanel.add(identifierLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(identifierTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(nameLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(nameTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(fullNameLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(fullNameTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(abbreviationLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(abbreviationTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(toolTipLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(toolTipTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(fileExtensionLabel, cellConstraints.xy(1,row));
        topCenterPanel.add(fileExtensionTextArea, cellConstraints.xy(3,row));

        row += 2;
        topCenterPanel.add(descriptionLabel, cellConstraints.xy(1,row));

        final JPanel centerPanel = ModelerSession.getComponentFactoryService().createPanel();
        centerPanel.setLayout(new BorderLayout());

        centerPanel.add(topCenterPanel, BorderLayout.NORTH);
        centerPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(descriptionTextArea));

        setLayout(new BorderLayout(PluginsOverviewDialogView.HGAP, PluginsOverviewDialogView.HGAP));

        final JPanel listsNotationPanel = ModelerSession.getComponentFactoryService().createPanel();
        listsNotationPanel.setLayout(new BorderLayout());
        listsNotationPanel.add(notationLabel, BorderLayout.NORTH);
        listsNotationPanel.add(ModelerSession.getComponentFactoryService().createScrollPane(notationsList), BorderLayout.CENTER);

        final JPanel listsModulePanel = ModelerSession.getComponentFactoryService().createPanel();
        listsModulePanel.setLayout(new BorderLayout());
        listsModulePanel.add(moduleLabel, BorderLayout.NORTH);
        listsModulePanel.add(ModelerSession.getComponentFactoryService().createScrollPane(modulesList), BorderLayout.CENTER);

        final JPanel listsPanel = ModelerSession.getComponentFactoryService().createPanel();
        listsPanel.setLayout(new BorderLayout(PluginsOverviewDialogView.HGAP, PluginsOverviewDialogView.HGAP));
        listsPanel.add(listsNotationPanel, BorderLayout.WEST);
        listsPanel.add(listsModulePanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);
        add(listsPanel, BorderLayout.WEST);
    }


}
