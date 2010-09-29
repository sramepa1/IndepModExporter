package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.workspace;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:36:37, 7.11.2009
 */

/**
 * This interface is intended to be implemented by the workspace components. Every-time the actual model
 * is being changes, the host application (modeler) invokes these methods.
 *
 * It is up to notation plugin developers whether they use this interface when implementing the workspace
 * component or not. It is not required.
 *
 * This order of method invocation is used when switching working models (diagrams):
 * 1. finish()
 * 2. over()
 */
public interface UpdatableWorkspaceComponent {

    /**
     * This method is invoked on the workspace component when the actual model is being changed and the workspace
     * component is going to be active.
     */
    public void update();

    /**
     * This method is invoked on the workspace component when the actual model is being changed. This method is invoked
     * event if the the workspace component is  going to be inactivated.
     */
    public void over();
    
}
