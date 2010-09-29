package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:27:56, 31.10.2009
 */

/**
 * DiagramModel interface is base for all class holding actual diagram. Every diagram is represented by an instance
 * of ProjectDiagram, which basically represents an item in project tree navigation.
 *
 * DiagramModel is supposed to be a holder for actual diagram data like vertexes, connections (edges), etc.
 */
public interface DiagramModel {

    /**
     * Adds a listener of any change that has occurred in diagram.
     * ProjectDiagram registers a listener during initialization time to be informed about all changes
     * in diagram and then change state of it's 'changed' variable.
     *
     * @see cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram for more info 
     *
     * @param listener is an instance of DiagramModelChangeListener class that will be notified when any change in
     * an instance of DiagramModel occurs
     */
    public void addChangeListener(final DiagramModelChangeListener listener);

    /**
     * This method is invoked on the diagram model when the actual model is being changed and this diagram
     * is going to be active.
     *
     * Can be used e.g. for initial settings and initialization of event handling mechanism when
     * the diagram is opened for the first time - e.g. after load.
     */
    public void update();

    /**
     * This method is invoked on the diagram when the actual model is being changed and this diagram is going
     * to be inactive.
     */
    public void over();

}
