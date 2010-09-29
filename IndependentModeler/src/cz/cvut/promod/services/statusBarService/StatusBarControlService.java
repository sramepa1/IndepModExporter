package cz.cvut.promod.services.statusBarService;

import com.jidesoft.status.StatusBar;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:53:46, 21.1.2010
 */

/**
 * StatusBarControlService allows developers to obtain the notation specific status bar.
 */
public interface StatusBarControlService extends StatusBarService{

    /**
     * Returns the notation specific status bar.
     *
     * @param notationIdentifier is the notation identifier of the associated status bar
     *
     * @return required notation specific status bar
     */
    public StatusBar getStatusBar(final String notationIdentifier);    

}
