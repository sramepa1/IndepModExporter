package cz.cvut.promod.gui.dialogs.configurationChangeDialog;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.results.ConfigurationDifference;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;

import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:55:32, 12.12.2009
 *
 * Dialog displaying differences between actual ProMod configuration and the ProMod configuration that
 * was actual at the time, when the project, that is now being loaded, was saved.
 */
public class ConfigurationDiffDialog extends ConfigurationDiffDialogView{

    private static final Logger LOG = Logger.getLogger(ConfigurationDiffDialog.class);

    private static final String TITLE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.title");
    private static final String MISSING_NOTATION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.missing.notation");
    private static final String LOADED_VALUE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.loaded.value");
    private static final String ACTUAL_VALUE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.actual.value");
    private static final String MISSING_MODULE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.missing.module");
    private static final String RELATED_IDENTIFIER_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.related.notation");
    private static final String MISSING_EXTENSION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.missing.extension");
    private static final String DIFF_EXTENSION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.different.extension");
    private static final String DIFF_ABBREVIATION_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.different.abbreviation");
    private static final String DIFF_NAME_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.configuration.different.name");

    public final String NEW_LINE = "\n";


    /**
     * Constructs a new ConfigurationDiffDialog class.
     *
     * @param differences is the list containing all configuration differences
     */
    public ConfigurationDiffDialog(final List<ConfigurationDifference> differences){
        setTitle(TITLE_LABEL);

        initEventHandling();

        initReport(differences);

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
     * Initialize messages about configuration differences.
     *
     * @param differences all configuration differences
     */
    private void initReport(final List<ConfigurationDifference> differences) {
        for(final ConfigurationDifference difference : differences){
            switch (difference.getChangeType()){
                case MISSING_NOTATION:
                    publishMissingNotationDiff(difference);
                    break;
                case MISSING_MODULE:
                    publishMissingModuleDiff(difference);
                    break;
                case MISSING_EXTENSION:
                    publishMissingExtensionDiff(difference);
                    break;
                case DIFFERENT_FULL_NAME:
                    publishDiffName(difference);
                    break;
                case DIFFERENT_ABBREVIATION:
                    publishDiffAbbreviation(difference);
                    break;
                case DIFFERENT_EXTENSION:
                    publishDiffExtension(difference);
                    break;
                default:
                    LOG.error("No such a configuration change option.");
            }

            diffTextArea.append(NEW_LINE);
            diffTextArea.append(NEW_LINE);
        }
    }

    /**
     * Publishes different full name.
     *
     * @param difference is the difference detail
     */
    private void publishDiffName(final ConfigurationDifference difference) {
        diffTextArea.append(DIFF_NAME_LABEL);
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(LOADED_VALUE_LABEL + " " + difference.getMessage());
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(ACTUAL_VALUE_LABEL + " " + ModelerSession.getNotationService().getNotation(difference.getIdentifier()).getFullName());
    }

    /**
     * Publishes different abbreviation.
     *
     * @param difference is the difference detail
     */
    private void publishDiffAbbreviation(final ConfigurationDifference difference) {
        diffTextArea.append(DIFF_ABBREVIATION_LABEL);
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(LOADED_VALUE_LABEL + " " + difference.getMessage());
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(ACTUAL_VALUE_LABEL + " " + ModelerSession.getNotationService().getNotation(difference.getIdentifier()).getAbbreviation());
    }

    /**
     * Publishes different extension.
     *
     * @param difference is the difference detail
     */
    private void publishDiffExtension(final ConfigurationDifference difference) {
        diffTextArea.append(DIFF_EXTENSION_LABEL);
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(LOADED_VALUE_LABEL +" " + difference.getMessage());
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(
                ACTUAL_VALUE_LABEL + " " + ModelerSession.getNotationService().getNotation(
                        difference.getIdentifier()
                ).getLocalIOController().getNotationFileExtension());
    }

    /**
     * Publishes missing extension difference.
     *
     * @param difference is the difference detail
     */
    private void publishMissingExtensionDiff(final ConfigurationDifference difference) {
        diffTextArea.append(MISSING_EXTENSION_LABEL);
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(LOADED_VALUE_LABEL + " " + difference.getMessage());
    }

    /**
     * Publishes missing module difference.
     *
     * @param difference is the difference detail
     */
    private void publishMissingModuleDiff(final ConfigurationDifference difference) {
        diffTextArea.append(MISSING_MODULE_LABEL);
        diffTextArea.append(NEW_LINE);

        final Notation notation = ModelerSession.getNotationService().getNotation(difference.getIdentifier());
        if(notation != null){
            diffTextArea.append(RELATED_IDENTIFIER_LABEL + "" + notation.getFullName() + " (" + notation.getIdentifier() + ").");
            diffTextArea.append(NEW_LINE);
        } else {
            LOG.error("Unavailable notation for given identifier: " + difference.getIdentifier());
        }
        
        diffTextArea.append(LOADED_VALUE_LABEL + " " + difference.getMessage());
    }

    /**
     * Publishes missing notation difference.
     *
     * @param difference is the difference detail
     */
    private void publishMissingNotationDiff(final ConfigurationDifference difference) {
        diffTextArea.append(MISSING_NOTATION_LABEL);
        diffTextArea.append(NEW_LINE);
        diffTextArea.append(LOADED_VALUE_LABEL + " " + difference.getMessage());
    }

}