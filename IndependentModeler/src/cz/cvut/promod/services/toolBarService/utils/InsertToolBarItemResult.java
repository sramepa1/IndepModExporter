package cz.cvut.promod.services.toolBarService.utils;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:53:01, 4.2.2010
 */

/**
 * Possible results for inserting a new action into the notation specific tool bar.
 */
public enum InsertToolBarItemResult {

    /**
     * The action has been successfully added into the tool bar.
     */
    SUCCESS,

    /**
     * The action is either null or does not have name or has an empty string as it's name.
     */
    UNDEFINED_ACTION,

    /**
     * It is not possible to insert any action into the tool bar of not existing notation.
     */
    INVALID_NOTATION,

    /**
     * It is not possible to insert action that is not registered by the ActionService.
     */
    UNREGISTERED_ACTION,

    /**
     * It is not possible to insert the same action twice into the same tool bar.
     */
    DUPLICATE_ACTION
}
