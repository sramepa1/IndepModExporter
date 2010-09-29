package cz.cvut.promod.services.actionService;

import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:48:55, 10.10.2009
 */

/**
 * ActionControlService implementation.
 */
public class ActionControlServiceImpl implements ActionControlService{

    private final Logger LOG = Logger.getLogger(ActionControlServiceImpl.class);

    private final Map<String, NotationSpecificActions> notationSpecificActionsMap;

    public ActionControlServiceImpl(){
        notationSpecificActionsMap = new HashMap<String, NotationSpecificActions>();
    }

    /** {@inheritDoc} */
    public boolean check() {
        return true;
    }

    /** {@inheritDoc} */
    public ProModAction getAction(final String notationIdentifier,
                                  final String moduleIdentifier,
                                  final String actionIdentifier){

        if(notationIdentifier == null || actionIdentifier == null){
            return null;
        }

        if(notationSpecificActionsMap.containsKey(notationIdentifier)){
            final NotationSpecificActions notationSpecificActions = notationSpecificActionsMap.get(notationIdentifier);

            if(moduleIdentifier == null){
                return notationSpecificActions.getAction(actionIdentifier);
            } else {
                return notationSpecificActions.getAction(moduleIdentifier, actionIdentifier);
            }
        }

        return null;
    }

    public ProModAction getAction(final String notationIdentifier, final String actionIdentifier) {
        return getAction(notationIdentifier, null, actionIdentifier);
    }

    /** {@inheritDoc} */
    public ProModAction registerAction(final String notationIdentifier,
                                       final String moduleIdentifier,
                                       final String actionIdentifier,
                                       final ProModAction action){

        if(notationIdentifier == null || actionIdentifier == null){
            LOG.error("Action registration failed due to nullary info," +
                    " actionIdentifier: " + actionIdentifier +
                    ", notationIdentifier: " + notationIdentifier +
                    ", moduleIdentifier: " + moduleIdentifier + ".");
            
            return null;
        }

        final ProModAction proModAction = getAction(notationIdentifier, moduleIdentifier, actionIdentifier);

         if(proModAction != null){
             return proModAction;

        } else {
            if(action == null){
                LOG.error("Nullary action is not possible to register.");
                return null;
            }

             if(!isUniqueKeyAccelerator(notationIdentifier, (KeyStroke) action.getValue(AbstractAction.ACCELERATOR_KEY))){
                LOG.error("Duplicity in action's 'ACCELERATOR KEY'," +
                        " action: " + actionIdentifier +
                        " notation: " + notationIdentifier +
                        " module: " + moduleIdentifier);
                action.putValue(AbstractAction.ACCELERATOR_KEY, null);
             }

            action.initAction(
                    notationIdentifier,
                    moduleIdentifier,
                    actionIdentifier);

            setAction(notationIdentifier, moduleIdentifier, actionIdentifier, action);

            LOG.debug("New Action (" + actionIdentifier + ") has been added.");

            return action;
        }
    }

    /**
     * Tests whether there is no other already registered action with the same accelerator key definition. This
     * test is done only by the 'modeler' action and actions of the same notation.  
     *
     * @param notationIdentifier is the notation where to perform the test
     * @param keyStroke is the accelerator key to be tested  @return true if there is no such a action, false otherwise
     * @return true if the accelerator key is unique, false otherwise
     */
    private boolean isUniqueKeyAccelerator(final String notationIdentifier, final KeyStroke keyStroke) {

        NotationSpecificActions notationSpecificActions = notationSpecificActionsMap.get(notationIdentifier);
        if(notationSpecificActions != null){
            if(notationSpecificActions.existAcceleratorKey(keyStroke)){
                return false;
            }
        }

        notationSpecificActions = notationSpecificActionsMap.get(ModelerModel.MODELER_IDENTIFIER);
        if(notationSpecificActions != null){
            if(notationSpecificActions.existAcceleratorKey(keyStroke)){
                return false;
            }
        }


        return true;
    }

    /**
     * {@inheritDoc}
     */
    public ProModAction registerAction(final String notationIdentifier,
                                       final String actionIdentifier,
                                       final ProModAction action) {

        return registerAction(
                notationIdentifier,
                null,
                actionIdentifier,
                action
        );
    }

    /**
     * Sets the action.
     *
     * @param notationIdentifier is the notation identifier
     * @param moduleIdentifier os the module identifier
     * @param actionIdentifier is the action identifier
     * @param newProModAction is the action
     */
    private void setAction(final String notationIdentifier,
                           final String moduleIdentifier,
                           final String actionIdentifier,
                           final ProModAction newProModAction){
        if(notationIdentifier == null || actionIdentifier == null){
           return;
        }

        if(notationSpecificActionsMap.containsKey(notationIdentifier)){
            notationSpecificActionsMap.get(notationIdentifier).setAction(moduleIdentifier, actionIdentifier, newProModAction);
        } else {
            final NotationSpecificActions notationSpecificActions = new NotationSpecificActions();
            notationSpecificActions.setAction(moduleIdentifier, actionIdentifier, newProModAction);
            notationSpecificActionsMap.put(notationIdentifier, notationSpecificActions);
        }
    }


    /** {@inheritDoc} */
    public void updateActionsVisibility(final String newNotationIdentifier) {
        for(final NotationSpecificActions notationSpecificActions  : notationSpecificActionsMap.values()){
            notationSpecificActions.updateActionVisibility(newNotationIdentifier);
        }
    }

    /** {@inheritDoc} */
    public boolean isRegisteredAction(final ProModAction action) {
        for(final NotationSpecificActions notationSpecificActions : notationSpecificActionsMap.values()){
            if(notationSpecificActions.existAction(action)){
                return true;
            }
        }

        return false;
    }
}
