package cz.cvut.promod.services.actionService;

import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:59:19, 10.10.2009
 */

/**
 * ActionControlService is the second service layer for the ActionService.
 */
public interface ActionControlService extends ActionService{

    /**
     * Updates visibility of actions registered with the ActionService. If a action has been registered with the
     * notation identifier specified as a argument of this method or with a identifier of ProMod Modeler, such
     * a action will be visible. Other actions will be invisible.
     *
     * @param notationIdentifier is the identifier that is supposed to be active
     */
    public void updateActionsVisibility(final String notationIdentifier);
    
}
