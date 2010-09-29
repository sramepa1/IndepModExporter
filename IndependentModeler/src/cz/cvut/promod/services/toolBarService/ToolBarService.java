package cz.cvut.promod.services.toolBarService;

import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.toolBarService.utils.InsertToolBarItemResult;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:46:23, 10.10.2009
 */

/**
 * ToolBarService allows developer to insert (append) simple action to the notation specific tool bar.  
 */
public interface ToolBarService extends Service{

    /**
     * Adds new action to the tool bar of specified notation.
     *
     * @param notationIdentifier is the identifier of the notation to which is tool bar is the action supposed to be added.
     * @param action the action that is supposed to be added
     * @return the result in form of InsertToolBarItemResult
     *
     * @see cz.cvut.promod.services.toolBarService.utils.InsertToolBarItemResult
     */
    public InsertToolBarItemResult addAction(final String notationIdentifier, final ProModAction action);   

}
