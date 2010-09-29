package cz.cvut.promod.gui.dialogs.pluginsOverview.notations;

import org.apache.log4j.Logger;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:49:59, 11.2.2010
 *
 * Implementation of NotationOverviewTab.
 */
public class NotationOverviewTab extends NotationOverviewTabView {

    private static final Logger LOG = Logger.getLogger(NotationOverviewTab.class);

    private final NotationOverviewTabModel model;

    /** holds the selected notation list item */
    private int selectedNotation = -1;


    public NotationOverviewTab(){
        model = new NotationOverviewTabModel();

        initList();

        initEventHandling();
    }

    private void initEventHandling() {
        notationsList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if(notationsList.getSelectedIndex() > -1){                
                    final String notationIdentifier =
                            ((NotationOverviewTabModel.NotationLabelWrapper)model.getElementAt(notationsList.getSelectedIndex())).getIdentifier();

                    selectedNotation = notationsList.getSelectedIndex();

                    updateDialogView(notationIdentifier);

                    modulesList.setModel( ((NotationOverviewTabModel.NotationLabelWrapper)model.getElementAt(notationsList.getSelectedIndex())).getModuleListModel());
                    modulesList.setEnabled(true);

                    notationsList.setSelectedIndex(selectedNotation);
                }
            }
        });

        modulesList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                final NotationOverviewTabModel.NotationLabelWrapper notationWrapper =
                        (NotationOverviewTabModel.NotationLabelWrapper) model.getElementAt(selectedNotation);

                notationsList.clearSelection();

                final String notationIdentifier = notationWrapper.getIdentifier();

                final ModuleListModel.ModuleLabelWrapper moduleWrapper =
                        (ModuleListModel.ModuleLabelWrapper) notationWrapper.getModuleListModel().getElementAt(modulesList.getSelectedIndex());

                if(moduleWrapper != null){
                    updateDialogView(notationIdentifier, moduleWrapper.getIdentifier());    
                }
            }
        });
    }

    private void updateDialogView(final String notationIdentifier, final String moduleIdentifier) {
        Module module = null;
        try{
            module = ModelerSession.getNotationService().getNotationSpecificPlugins(notationIdentifier).getModule(moduleIdentifier);
        } catch (Exception e){
            LOG.error("Not valid identifier data");
            identifierTextArea.setText(null);
            nameTextArea.setText(null);
            descriptionTextArea.setText(null);
        }

        if(module != null){
            identifierTextArea.setText(module.getIdentifier());
            fullNameTextArea.setText(null);
            nameTextArea.setText(module.getName());
            abbreviationTextArea.setText(null);
            toolTipTextArea.setText(null);
            fileExtensionTextArea.setText(null);
            descriptionTextArea.setText(module.getDescription());

        } else {
            LOG.error("Missing module.");
        }
    }

    private void updateDialogView(final String notationIdentifier) {
        final Notation notation = ModelerSession.getNotationService().getNotation(notationIdentifier);

        if(notation != null){
            identifierTextArea.setText(notation.getIdentifier());
            fullNameTextArea.setText(notation.getFullName());
            nameTextArea.setText(notation.getName());
            abbreviationTextArea.setText(notation.getAbbreviation());
            toolTipTextArea.setText(notation.getNotationToolTip());
            fileExtensionTextArea.setText(notation.getLocalIOController().getNotationFileExtension());
            descriptionTextArea.setText(notation.getDescription());

        } else {
            LOG.error("Missing notation.");
        }
    }

    private void initList() {
        notationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notationsList.setModel(model);

        modulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }    
}
