package cz.cvut.promod.services.statusBarService;

import com.jidesoft.status.StatusBarItem;
import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.statusBarService.utils.InsertStatusBarItemResult;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:53:26, 21.1.2010
 */

/**
 * StatusBarService allows developers to insert new status bar items into the notation specific status bar.
 */
public interface StatusBarService extends Service {

    /**
     * Appends new status bar item to the notation specific status bar.
     *
     * @param notationIdentifier is the identifier of notation into which status bar is the new item supposed to be
     * appended
     *
     * @param statusBarItem is the status bar item that is supposed to be appended
     *
     * @param itemLayout is the layout information, see JideBoxLayout.FIX, JideBoxLayout.FLEXIBLE, JideBoxLayout.VARY,
     * JideBoxLayout.FIX is the initial value in case of nullary info or invalid value
     *
     * @return any value from InsertStatusBarItemResult enumeration
     *
     * @see cz.cvut.promod.services.statusBarService.utils.InsertStatusBarItemResult for more detail info about return
     * values
     */
    public InsertStatusBarItemResult addStatusBarItem(final String notationIdentifier,
                                                      final StatusBarItem statusBarItem,
                                                      String itemLayout);

}
