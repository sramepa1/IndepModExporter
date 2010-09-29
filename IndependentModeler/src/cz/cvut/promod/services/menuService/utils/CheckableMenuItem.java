package cz.cvut.promod.services.menuService.utils;

import javax.swing.*;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:25:00, 30.10.2009
 */

/**
 * Is the implementation of checkable menu item (has a small checkbox) of menu item in ProMod.
 */
public class CheckableMenuItem extends JCheckBoxMenuItem implements ModelerMenuItem {

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
