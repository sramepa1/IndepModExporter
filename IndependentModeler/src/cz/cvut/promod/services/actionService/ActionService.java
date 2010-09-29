package cz.cvut.promod.services.actionService;

import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:44:48, 10.10.2009
 */

/**
 * ActionService interface is a base point for ProModAction handling. One is supposed to use methods defined in this
 * interface for registering and obtaining actions.
 *
 * Using ModelerSession.getActionService() is possible to get proper implementation of this interface ready for use.
 *
 * All instances of ProModAction class (this class is defined as abstract, so all class that extend ProMod class and
 * implement method actionPerformed(ActionEvent event)) are initially defined as disabled. This forces user to use this
 * mechanism for action registration. But on the other hand, this is not very strong protection, so be careful with
 * dealing with not registered actions. 
 */
public interface ActionService extends Service {

    /**
     * Register a new ProModAction.
     *
     * @param notationIdentifier is a identifier of notation to that the action belongs
     * @param moduleIdentifier is a identifier of module to that the action belongs
     * @param actionIdentifier is the action identifier
     * @param action is an instance of any class that extends ProModAction
     *
     * @return the reference to the action (so the same as in the action parameter), or a reference to the action
     * that has the same actionIdentifier, notationIdentifier and moduleIdentifier and that has been registered before.
     *
     * One can use for example:
     *      MyProModAction myAction = new MyProModAction();
     *      ProModAction action = ModelerSession.getActionService().registerAction(...);
     *
     *      if(myAction == action){ //my action has been registered }
     *      else {// my action was not registered because of action duplicity }
     */
    public ProModAction registerAction(final String notationIdentifier,
                                       final String moduleIdentifier,
                                       final String actionIdentifier,
                                       final ProModAction action
    );


    /**
     * Register a new ProModAction. Just syntactical sugar supplement of the more complex method.
     *
     * @param notationIdentifier  is a identifier of notation to that the action belongs
     * @param actionIdentifier is the action identifier
     * @param action is the action identifier
     * @return the reference to the action (so the same as in the action parameter), or a reference to the action
     * that has the same actionIdentifier, notationIdentifier and moduleIdentifier and that has been registered before.
     */
    public ProModAction registerAction(final String notationIdentifier,
                                       final String actionIdentifier,
                                       final ProModAction action
    );


    /**
     * Finds a returns an instance of ProModAction that has been registered by the ActionService with
     * the given notation identifier, given module identifier and given action identifier.
     *
     * @param notationIdentifier is the identifier of notation under that has been the action registered
     * @param moduleIdentifier is the identifier of module under that has been the action registered
     * @param actionIdentifier is the identifier of action
     *
     * @return the instance of action if such a action has been successfully registered, null otherwise
     */
    public ProModAction getAction(final String notationIdentifier,
                                  final String moduleIdentifier,
                                  final String actionIdentifier
    );


    /**
     * Finds a returns an instance of ProModAction that has been registered by the ActionService with
     * the given notation identifier and given action identifier.
     *
     * @param notationIdentifier is the identifier of notation under that has been the action registered
     * @param actionIdentifier is the identifier of action
     *
     * @return the instance of action if such a action has been successfully registered, null otherwise
     */
    public ProModAction getAction(final String notationIdentifier,
                                  final String actionIdentifier
    );

    /**
     * Checks whether the action given as a parameter is any already registered action by any plugin or
     * modeler itself.
     *
     * @param action is the action to be checked
     * @return true if the actions has already been registered, false otherwise
     */
    public boolean isRegisteredAction(final ProModAction action);    
    
}
