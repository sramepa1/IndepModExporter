package cz.cvut.promod.services.projectService.results;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:37:47, 8.2.2010
 */

/**
 * Possible result of project root saving file system operation.
 */
public enum SaveProjectResult {

    /**
     * No error has occurred. The project root has been successfully saved.
     */
    SUCCESS,

    /**
     * Used for signaling run-time failure of writing file system operations.
     */
    IOERROR,

    /**
     * Represents a xml error during creating of a structure of xml project file. This error should practically NEVER
     * happened - represents inner error of saving algorithm. 
     */
    XML_ERROR,

    /**
     * This error occurs when one tries to save project root that represents by an invalid tree path, e.g. not valid
     * tree path, or no project root specified by the tree path.
     */
    INVALID_TREE_PATH

}
