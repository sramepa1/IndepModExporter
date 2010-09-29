package cz.cvut.promod.services.menuService.utils;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:22:36, 30.10.2009
 */

/**
 * Class that is a base class for all items in menus.
 */
public interface ModelerMenuItem {

    /**
     * Returns the list of relative items. Relives are all items in menu that have the same name (text).
     * All relatives return empty list (but not nullary), but one returns list containing references to all
     * other relative items (to make it possible to set them not visible).
     *
     * @return If the item is supposed to be hidden when not related notation is selected, than this methods
     * return an empty list, if it has no relatives then returns null, and if this is the item that is supposed
     * to be shown (event if it is disabled anyway) that it returns the non empty list of relatives.
     */
    public List<ModelerMenuItem> getListOfRelatives();

    /**
     * Sets the list of relative items. Relives are all items in menu that have the same name (text).
     * All relatives return empty list (but not nullary), but one returns list containing references to all
     * other relative items (to make it possible to set them not visible).
     *
     * @param relatives is the list of relatives
     */
    public void setListOfRelatives(List<ModelerMenuItem> relatives);

}
