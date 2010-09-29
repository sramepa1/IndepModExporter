package cz.cvut.promod.services.menuService.utils;

import javax.swing.*;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:11:38, 12.10.2009
 */

/**
 * Is the implementation of uncheckable menu item (does not have a small checkbox) of menu item in ProMod.
 */
public class DefaultMenuItem extends JMenuItem implements ModelerMenuItem {

    private List<ModelerMenuItem> relatives = null;


    /** {@inheritDoc} */
    public List<ModelerMenuItem> getListOfRelatives() {
        return relatives;
    }

    /** {@inheritDoc} */
    public void setListOfRelatives(final List<ModelerMenuItem> relatives) {
        this.relatives = relatives;
    }
}
