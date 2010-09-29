package cz.cvut.promod.services.statusBarService.utils;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:26:54, 4.2.2010
 */

/**
 * Possible results for inserting a new status bar item into the notation specific status bar.
 */
public enum InsertStatusBarItemResult {

    /**
     * The new status bar item has been appended successfully.
     */
    SUCCESS,

    /**
     * It is not possible to insert any status bar item into the tool bar of not existing notation.
     */
    INVALID_NOTATION,

    /**
     * Status bar item is not valid.
     */
    INVALID_STATUS_BAR_ITEM,
    
}
