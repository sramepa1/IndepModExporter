package cz.cvut.promod.actions;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.results.LoadProjectResult;
import cz.cvut.promod.services.projectService.results.ConfigurationDifference;
import cz.cvut.promod.gui.support.utils.LoadProjectDialogFileFilter;
import cz.cvut.promod.gui.projectNavigation.ProjectNavigation;
import cz.cvut.promod.gui.projectNavigation.events.ProjectTreeExpandEvent;
import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.gui.dialogs.configurationChangeDialog.ConfigurationDiffDialog;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.io.File;
import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:47:03, 11.11.2009
 *
 * Project load actions.
 */
public class LoadDialogAction extends ProModAction {

    private static final Logger LOG = Logger.getLogger(LoadDialogAction.class);

    public LoadDialogAction(final String displayName,
                            final Icon icon,
                            final KeyStroke keyStroke) {
        
        super(displayName, icon, keyStroke);
    }

    public void actionPerformed(ActionEvent event) {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new LoadProjectDialogFileFilter());

        final int returnVal = fileChooser.showOpenDialog(ModelerSession.getFrame());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
           final File selectedFile = fileChooser.getSelectedFile();

            final LoadProjectResult loadResult = ModelerSession.getProjectControlService().loadProject(selectedFile);

            if(loadResult.getProjectRoot() == null){
                JOptionPane.showMessageDialog(
                        ModelerSession.getFrame(),
                        ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile"),
                        ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile.title"),
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            // show the configuration differences dialog
            final List<ConfigurationDifference> differences = loadResult.getMessages();
            if(differences != null && !differences.isEmpty()){
                new ConfigurationDiffDialog(differences);                
            }

            final AddProjectItemResult result = ModelerSession.getProjectControlService().addProject(
                    new ProjectRoot(loadResult.getProjectRoot().getDisplayName(), loadResult.getProjectRoot().getProjectLocation()), true
            );

            switch (result.getStatus()){
                case SUCCESS:
                    //sync project structure with file system = load project data
                    ModelerSession.getProjectControlService().synchronize(
                            loadResult.getProjectRoot().getProjectFile(), null, true, false, false, true);

                    final ProModAction action = ModelerSession.getActionService().getAction(
                            ModelerModel.MODELER_IDENTIFIER, ProjectNavigation.ACTION_EXPAND);

                    final TreePath projectTreePath = result.getTreePath();
                    ModelerSession.getProjectControlService().setSelectedItem(projectTreePath);

                    if(action != null && projectTreePath != null){
                        action.actionPerformed(new ProjectTreeExpandEvent(this, projectTreePath, 1, true));
                    } else {
                        LOG.error("Expand action and/or project node is/are not available.");
                    }

                    break;
                case NAME_DUPLICITY:
                    alertNameDuplicity();
                    break;
                case INVALID_NAME:
                    alertIllegalName();
                    break;
                default:
                    alertGeneralError();
            }

        }
    }

    private void alertGeneralError() {
        JOptionPane.showMessageDialog(
                ModelerSession.getFrame(),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile"),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile.title"),
                JOptionPane.ERROR_MESSAGE);
    }

    private void alertIllegalName() {
        JOptionPane.showMessageDialog(
                ModelerSession.getFrame(),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile"),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile.title"),
                JOptionPane.ERROR_MESSAGE);
    }

    private void alertNameDuplicity() {
        JOptionPane.showMessageDialog(
                ModelerSession.getFrame(),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile"),
                ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.error.loadfile.title"),
                JOptionPane.ERROR_MESSAGE);
    }

}
