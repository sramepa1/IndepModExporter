package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:45:34, 17.1.2010
 */

/**
 * All listeners of changes in a diagram model are supposed to implement this interface.
 */
public interface DiagramModelChangeListener {

    /**
     * Whenever any change in an instance of DiagramModel, then that instance is supposed to invoke
     * this method of all it's listeners.
     *
     * @param change holds any detail info about the change 
     */
    public void changePerformed(final Object change);

}
