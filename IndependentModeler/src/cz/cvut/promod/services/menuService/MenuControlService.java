package cz.cvut.promod.services.menuService;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:20:39, 14.10.2009
 */

/**
 *  MenuControlService contains advanced method for menu managing.  
 */
public interface MenuControlService extends MenuService {

    /**
     * Returns the main menu bar.
     *
     * @return the main menu bar
     */
    public JMenuBar getMenuBar();

    /**
     * Returns project navigation tree popup menu.
     *
     * @return project navigation tree popup menu
     */
    public JPopupMenu getProjectTreePopupMenu();
    
}
