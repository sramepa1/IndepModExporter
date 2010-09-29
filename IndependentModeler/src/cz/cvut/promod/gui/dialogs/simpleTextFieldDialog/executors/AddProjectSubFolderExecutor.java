package cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.executors;

import cz.cvut.promod.gui.dialogs.simpleTextFieldDialog.SimpleTextFieldDialogExecutor;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import org.apache.log4j.Logger;

import javax.swing.tree.TreePath;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:38:55, 24.10.2009
 */
public class AddProjectSubFolderExecutor implements SimpleTextFieldDialogExecutor {

    private final Logger LOG = Logger.getLogger(AddProjectSubFolderExecutor.class);

    private static final String ILLEGAL_NAME_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.error.disallowed");
    private static final String NAME_DUPLICATE_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.error.nameduplicity");
    private static final String ERROR_LABEL =
            ModelerSession.getCommonResourceBundle().getString("modeler.add.new.subfolder.dialog.error");

    public String execute(final String text) {
        final AddProjectItemResult result = ModelerSession.getProjectControlService().addSubFolder(text, true);

        switch (result.getStatus()){
            case SUCCESS:
                mkdir(result.getTreePath());
                return null;
            case INVALID_NAME:
                return ILLEGAL_NAME_LABEL + ProjectServiceUtils.getDisallowedNameSymbols(',');
            case NAME_DUPLICITY:
                return NAME_DUPLICATE_LABEL;
            default:
                LOG.error("No such a result expected during project subfolder addition.");
                return ERROR_LABEL;
        }
    }

    /**
     * Make the dir structure.
     *
     * @param treePath is the tree path to the node to that is the dir structure supposed to make
     */
    private void mkdir(final TreePath treePath) {
        ModelerSession.getProjectControlService().synchronize(
                treePath,
                true, true, false, false
        );

    }

}
