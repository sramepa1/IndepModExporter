package cz.cvut.promod.gui.support.utils;

import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.ModelerSession;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:18:58, 11.11.2009
 */

/**
 * Class for file selection in the load project dialog.
 */
public class LoadProjectDialogFileFilter extends FileFilter {

    public boolean accept(final File f) {
        if(f.isDirectory()){
            return true;
        }

        if(f.isFile() && f.getName().endsWith(ProjectService.PROJECT_FILE_EXTENSION)){
            return true;
        }

        return false;
    }

    public String getDescription() {
        return ModelerSession.getCommonResourceBundle().getString("modeler.action.project.load.description");
    }

}
