package cz.cvut.promod.services.projectService.results;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 13:05:20, 6.2.2010
 */

/**
 * Represents the result of adding project item.
 */
public enum AddProjectItemStatus {

    /**
     * No error has occurred.
     */
    SUCCESS,

    /**
     * There is already name colliding project item.
     */
    NAME_DUPLICITY,

    /**
     * The project item is invalid, e.g. nullary.
     */
    INVALID_NAME,

    /**
     * Not valid parent for the project item.
     */
    ILLEGAL_PARENT,

    /**
     * Not valid notation for the project item.
     */
    ILLEGAL_NOTATION,

    /**
     * Invalid project item data, e.g. nullary.
     */
    INVALID_ITEM_DATA,

}
