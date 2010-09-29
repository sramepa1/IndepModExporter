package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController;

import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:52:56, 9.11.2009
 */

/**
 * NotationLocalIOController is an interface that has to be implemented by all notations.
 *
 * The functionality defined by methods of this interface are supposed to realize the project diagram FS
 * serialization and deserialization. 
 */
public interface NotationLocalIOController {

    public static enum SaveResult{
        /**
         * The diagram was saved successfully.
         */
        SUCCESS,

        /**
         * The diagram was NOT saved successfully. An error has occurred.
         */
        ERROR
    }

    /**
     * Saves the project diagram.
     *
     * @param projectDiagram is the project diagram to be saved
     * @param parentLocation is the FS location where the diagram is supposed to be saved
     * @param makeDirs true if the method is supposed to create necessary FS structure if missing
     * @return a value from SaveResult
     */
    public SaveResult saveProjectDiagram(final ProjectDiagram projectDiagram,
                                         final String parentLocation,
                                         final boolean makeDirs);

    /**
     * Loads a diagram saved in a notation specific protocol (xml, binary, etc.) form the file on the file system.
     *
     * Note:
     * DISPLAY NAME OF THE LOADED PROJECT DIAGRAM HAS TO MATCH WITH THE FILE NAME WITHOUT THE FILE EXTENSION.
     *
     * @param diagramLocation  is the location of the file representing notation specific dialog on file system
     *
     * @return an instance of ProjectDiagram class
     *
     * @throws Exception when something unexpected happens, one of possible reasons is notation mismatch (e.g. there
     * is a file on the file system that has extension of a notation, but this file isn't really the diagram file
     * of this notation, an exception is then usually thrown). This is why the responsibility for exception handling
     * takes programmer and not the service.
     */
    public ProjectDiagram loadProjectDiagram(final String diagramLocation) throws Exception;

    /**
     * @return the notation's file extension
     */
    public String getNotationFileExtension();
}
