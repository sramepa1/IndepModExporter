package cz.cvut.promod.plugin.notationSpecificPlugIn.notation;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:26:04, 12.10.2009
 */
public interface NotationWorkspaceData {

    /**
     * Method for obtaining a WorkspaceComponent. Any invocation of this method on the same instance of
     * NotationWorkspaceData class has to return the same instance of JComponent (usually an instance of
     * JComponent that implements UpdatableWorkspaceComponent interface).
     *
     * @return an instance of WorkspaceComponent class (every method invocation returns the same object).
     */
    public JComponent getWorkspaceComponentSingleton();

}
