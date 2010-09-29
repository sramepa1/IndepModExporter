package cz.cvut.promod.gui.dialogs.pluginsOverview.extensions;

import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:49:59, 11.2.2010
 *
 * Implementation of ExtensionOverviewTab for PluginsOverviewDialog.
 */
public class ExtensionOverviewTab extends ExtensionOverviewTabView {

    private static final Logger LOG = Logger.getLogger(ExtensionOverviewTab.class);

    private final ExtensionOverviewTabModel model;

    public ExtensionOverviewTab(){
        model = new ExtensionOverviewTabModel();

        initList();

        initEventHandling();
    }

    private void initEventHandling() {
        extensionList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                final String extensionIdentifier =
                        ((ExtensionOverviewTabModel.ExtensionLabelWrapper)model.getElementAt(extensionList.getSelectedIndex())).getIdentifier();

                updateDialogView(extensionIdentifier);
            }
        });
    }

    private void updateDialogView(final String extensionIdentifier) {
        final Extension extension = ModelerSession.getExtensionService().getExtension(extensionIdentifier);

        if(extension != null){
            identifierTextArea.setText(extension.getIdentifier());
            nameTextArea.setText(extension.getName());
            descriptionTextArea.setText(extension.getDescription());
        } else {
            LOG.error("Missing notation.");
        }
    }

    private void initList() {
        extensionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        extensionList.setModel(model);
    }

}