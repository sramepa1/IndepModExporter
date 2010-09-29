package cz.cvut.promod.services.actionService.actionUtils;

import cz.cvut.promod.gui.ModelerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:51:16, 13.10.2009
 */

/**
 * Represents basic ProMod action. This kind of actions is supposed to be
 * used with the ActionService.
 *
 * @see cz.cvut.promod.services.actionService.ActionService 
 */
public abstract class ProModAction extends AbstractAction{

    private String actionIdentifier = null;

    private String notationIdentifier = null;

    private String moduleIdentifier = null;

    public ProModAction(final String displayName,
                        final Icon icon,
                        final KeyStroke keyStroke){

        super(displayName, icon);

        putValue(ACCELERATOR_KEY, keyStroke);       

        /**
         * All instances of ProModAction class are initially disabled. Action is enabled when the
         * notation identifier is set during action registration by ActionService. Only ActionService is supposed
         * to use <i>initAction</i> method.
         *
         * For example, if anyone don't register action using ActionService and only inserts this action to the menu,
         * then visibility of this action is never updated.
         * @see cz.cvut.promod.gui.ModelerModel an it's <b>update</b> method
         * That's way this action is initially disabled.
         */
        setEnabled(false);
    }

    public void updateActionVisibility(final String notationIdentifier){
        if(ModelerModel.MODELER_IDENTIFIER.equals(this.notationIdentifier)){
            return;
        }

        if(this.notationIdentifier.equals(notationIdentifier)){
            setEnabled(true);
        } else {
            setEnabled(false);            
        }
    }

    /**
     * Once the notation identifier has been set, no one can change it. This method should NEVER EVER be used
     * by the programmer. Only ActionService uses this method to initialize this service.
     *
     * @param notationIdentifier required notation identifier
     * @param moduleIdentifier is the identifier of a module
     * @param actionIdentifier is the identifier of a action
     *
     * @return true if notation identifier has been set, false otherwise
     */
    public boolean initAction(final String notationIdentifier,
                              final String moduleIdentifier,
                              final String actionIdentifier){

        if(this.notationIdentifier == null && this.moduleIdentifier == null && this.actionIdentifier == null){
            this.notationIdentifier = notationIdentifier;
            this.moduleIdentifier = moduleIdentifier;
            this.actionIdentifier = actionIdentifier;

            setEnabled(true);

            return true;
        }

        return false;
    }


    public String getActionIdentifier() {
        return actionIdentifier;
    }

    public String getNotationIdentifier() {
        return notationIdentifier;
    }

    public String getModuleIdentifier() {
        return moduleIdentifier;
    }

    public abstract void actionPerformed(ActionEvent event);
}
