package cz.cvut.promod.services.toolBarService;

import com.jidesoft.action.CommandBar;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:54:34, 20.12.2009
 */

/**
 * ToolBarControlService allows developer to obtain and operate with notation specific tool bar.   
 */
public interface ToolBarControlService extends ToolBarService{

    /**
     * Returns notation specific instance of CommandBar class.
     *
     * @param notationIdentifier is the identifier of a notation
     * 
     * @return the command bar for the notation, or null if there is no such a notation
     */
    public CommandBar getCommandBar(final String notationIdentifier);

}
