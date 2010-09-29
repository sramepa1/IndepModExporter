package cz.cvut.promod.gui.projectNavigation.actions.executors;

import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialogExecutor;
import cz.cvut.promod.gui.projectNavigation.ProjectNavigation;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectItem;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectContainer;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.results.SaveProjectResult;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;
import java.io.File;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 3:45:36, 17.4.2010
 */

/**
 * Simple executor for the standard text dialog performing project item rename action.
 *
 * @see cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialogExecutor
 */
public class RenameExecutor implements SimpleTextFieldDialogExecutor {

    private static final Logger LOG = Logger.getLogger(RenameExecutor.class);

    private final  TreePath treePath;
    private final ProjectItem projectItem;
    private final JTree projectTree;


    public RenameExecutor(final TreePath treePath, final ProjectItem projectItem, final JTree projectTree) {
        this.treePath = treePath;
        this.projectItem = projectItem;
        this.projectTree = projectTree;
    }
    /**{@inheritDoc} performs the rename action*/
    public String execute(final String text) {
        if(ProjectServiceUtils.isSyntacticallyCorrectName(text)){
            if(isDuplicitName(treePath.getParentPath(), projectItem, text)){
                return ProjectNavigation.DUPLICATE_NAME_LABEL;
            }

            File file = new File(ProjectServiceUtils.getFileSystemPathToProjectItem(treePath));
            final String oldName = projectItem.getDisplayName();

            if(projectItem instanceof ProjectRoot){
                projectItem.setDisplayName(text);
                if(SaveProjectResult.SUCCESS.equals(ModelerSession.getProjectControlService().saveProject(treePath))){
                    file = new File(file, oldName + ProjectService.PROJECT_FILE_EXTENSION);
                    if(file.exists()){
                        ProjectServiceUtils.removeDirectory(file);
                    } else {
                        showError();
                    }
                } else {
                    showError();
                    projectItem.setDisplayName(oldName);
                }

            } else {
                projectItem.setDisplayName(text);
                if(ModelerSession.getProjectControlService().synchronize(treePath, true, false, false, false)){
                    if(file.exists()){
                        ProjectServiceUtils.removeDirectory(file);
                    } else {
                        showError();
                    }
                } else {
                    projectItem.setDisplayName(oldName);
                    showError();
                }
            }
            projectTree.updateUI();

            return null;
        }

        return ProjectNavigation.INVALID_NAME_LABEL + ProjectServiceUtils.getDisallowedNameSymbols(',');
    }

    private boolean isDuplicitName(final TreePath treePath, final ProjectItem projectItem, final String newText){
        if(treePath.getLastPathComponent() instanceof DefaultMutableTreeNode){
            final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            for(int i = 0; i < defaultMutableTreeNode.getChildCount(); i++){
                if(defaultMutableTreeNode.getChildAt(i) instanceof DefaultMutableTreeNode){
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) defaultMutableTreeNode.getChildAt(i);

                    if(node.getUserObject() instanceof ProjectItem){
                        final ProjectItem testedProjectItem = (ProjectItem) node.getUserObject();

                        if(testedProjectItem == projectItem){
                            continue;
                        }

                        if(testedProjectItem.getDisplayName() != null && testedProjectItem.getDisplayName().equals(newText)){
                            if((testedProjectItem instanceof ProjectContainer) && (projectItem instanceof ProjectContainer)){
                                return true;
                            }
                            else if((testedProjectItem instanceof ProjectDiagram) && (projectItem instanceof ProjectDiagram)){
                                final ProjectDiagram testedProjectDiagram = (ProjectDiagram) testedProjectItem;
                                final ProjectDiagram projectDiagram = (ProjectDiagram) projectItem;

                                if(testedProjectDiagram.getNotationIdentifier().equals(projectDiagram.getNotationIdentifier())){
                                    return true;
                                }
                            }
                        }
                    }

                }
            }
        } else {
            LOG.error("A node is not an instance od DefaultMutableTreeNode");
        }


        return false;
    }

    /**
     * Shows an error.
     */
    private void showError(){
        JOptionPane.showMessageDialog(
                ModelerSession.getFrame(),
                ProjectNavigation.RENAME_ERROR_LABEL,
                ProjectNavigation.RENAME_ERROR_TITLE_LABEL,
                JOptionPane.ERROR_MESSAGE);
    }


}
