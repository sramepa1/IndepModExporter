package cz.cvut.promod.services.actionService;

import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:19:25, 2.11.2009
 */

/**
 * Notation specific actions holder.
 */
public class NotationSpecificActions {

    private final Logger LOG = Logger.getLogger(NotationSpecificActions.class);

    private final Map<String, ProModAction> notationActionsMap;

    private final Map<String, ModuleSpecificActions> moduleActionsMap;


    public NotationSpecificActions(){
        notationActionsMap = new HashMap<String, ProModAction>();
        moduleActionsMap = new HashMap<String, ModuleSpecificActions>();
    }

     /**
     * @param actionIdentifier is the action identifier
     * @return returns the action is such a action exists, null otherwise
     */
    public ProModAction getAction(final String actionIdentifier) {
        if(notationActionsMap.containsKey(actionIdentifier)){
            return notationActionsMap.get(actionIdentifier);
        }

        return null;
    }

    /**
     * @param moduleIdentifier is the module identifier
     * @param actionIdentifier is the action identifier
     * @return returns the action is such a action exists, null otherwise
     */
    public ProModAction getAction(final String moduleIdentifier, final String actionIdentifier) {
        if(moduleActionsMap.containsKey(moduleIdentifier)){
            return moduleActionsMap.get(moduleIdentifier).getAction(actionIdentifier);
        }

        return null;
    }

    /**
     * Sets the action.
     * @param moduleIdentifier module identifier
     * @param actionIdentifier action identifier
     * @param newProModAction the action itself
     */
    public void setAction(final String moduleIdentifier, final String actionIdentifier, final ProModAction newProModAction) {
        if(moduleIdentifier == null){
            if(notationActionsMap.containsKey(actionIdentifier)){
                LOG.error("An attempt to insert the same notation action to the notation actions map, action identifier: " + actionIdentifier + ".");
            } else {
                notationActionsMap.put(actionIdentifier, newProModAction);
            }

        } else {
            if(moduleActionsMap.containsKey(moduleIdentifier)){
                moduleActionsMap.get(moduleIdentifier).setAction(actionIdentifier, newProModAction);
            } else {
                final ModuleSpecificActions moduleSpecificActions = new ModuleSpecificActions();
                moduleSpecificActions.setAction(actionIdentifier, newProModAction);
                moduleActionsMap.put(moduleIdentifier, moduleSpecificActions);
            }
        }
    }

    /**
     * Updates the action visibility on the basis of active notation.
     * @param notationIdentifier is the identifier of active notation.
     */
    public void updateActionVisibility(final String notationIdentifier) {
        for(final ProModAction proModAction : notationActionsMap.values()){
            proModAction.updateActionVisibility(notationIdentifier);
        }

        for(final ModuleSpecificActions moduleSpecificActions : moduleActionsMap.values()){
            moduleSpecificActions.updateActionVisibility(notationIdentifier);
        }
    }

    /**
     * Tests whether there is no other already registered action with the same accelerator key definition
     * by this notation. Additionally, check all registered actions of this notation's modules.
     *
     * @param keyStroke is the accelerator key to be tested
     * @return true if there is such a action, false otherwise
     */
    public boolean existAcceleratorKey(final KeyStroke keyStroke){
        for(final ProModAction action : notationActionsMap.values()){
            final KeyStroke key = (KeyStroke) action.getValue(AbstractAction.ACCELERATOR_KEY);

            if(key != null && key.equals(keyStroke)){
                return true;
            }
        }

        for(final ModuleSpecificActions moduleSpecificActions : moduleActionsMap.values()){
            if(moduleSpecificActions.existAcceleratorKey(keyStroke)){
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether such a action has been already registered by a notation or any of it's modules.
     *
     * @param action is the action to be checked
     * @return true if such a actions has been already registered, false otherwise
     */
    public boolean existAction(final ProModAction action) {
        if(notationActionsMap.values().contains(action)){
            return true;
        }

        for(final ModuleSpecificActions moduleSpecificActions : moduleActionsMap.values()){
            if(moduleSpecificActions.exist(action)){
                return true;
            }
        }

        return false;
    }
}
