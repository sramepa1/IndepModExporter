package cz.cvut.promod.services.projectService.treeProjectNode;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModelChangeListener;
import cz.cvut.promod.services.projectService.treeProjectNode.listener.ProjectDiagramListener;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:03:10, 13.10.2009
 */

/**
 * Represents an item in the project tree navigation.
 * This class is final. So no plugin is allowed to implement any subclasses of this class. In case of need, plugins
 * can extend ProjectModel class and store it's specific data there. 
 */
public final class ProjectDiagram implements IdentifiableProjectItem, DiagramModelChangeListener{

    private final UUID uuid;

    /** display name is supposed to match with file name (without extension) */
    private transient String displayName;

    // serialize, but just for pertinent error handling 
    final String notationIdentifier;

    /** Indicates whether the model has been changes since last save */
    private transient boolean changed = false;

    private transient final Set<ProjectDiagramListener> listeners;

    final DiagramModel diagramModel;
    

    public ProjectDiagram(final String displayName,
                          final String notationIdentifier,
                          final UUID uuid,
                          final DiagramModel diagramModel) {

        this.uuid = uuid;
        this.notationIdentifier = notationIdentifier;
        this.diagramModel = diagramModel;

        /** register itself to listen changes in it's diagram model */
        if(diagramModel != null){
            diagramModel.addChangeListener(this);
        }

        changed = false;

        listeners = new HashSet<ProjectDiagramListener>();

        setDisplayName(displayName);
    }

    /**
     * @return the diagrams notation identifier
     */
    public String getNotationIdentifier() {
        return notationIdentifier;
    }

    /** {@inheritDoc} */
    public void setDisplayName(final String displayName) {
        final String oldName = this.displayName;
        this.displayName = displayName;

        publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, displayName, oldName);
    }

    /** {@inheritDoc} */
    public String getDisplayName() {
        return displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return diagrams model
     */
    public DiagramModel getDiagramModel() {
        return diagramModel;
    }

    @Override
    public String toString(){
        return getDisplayName();
    }

    /**
     * @return the value of change flag
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * set the change flag to false = unchanged.
     */
    public void reset() {
        final boolean oldChanged = changed;
        changed = false;

        publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, false, oldChanged);
    }

    public void changePerformed(final Object change) {
        final boolean oldChanged = changed;

        changed = true;

        publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, change, oldChanged);
    }

    /**
     * Registers a new change listener.
     *
     * @param listener is a listener that listens changes in ProjectDiagram.
     */
    public void addChangeListener(final ProjectDiagramListener listener){
        listeners.add(listener);
    }

    /**
     * Removes the change listener.
     *
     * @param listener is the listener to be removed
     */
    public void removeChangeListener(final ProjectDiagramListener listener){
        listeners.remove(listener);
    }

    /**
     * Publishes the change that has occurred.
     *
     * @param changeType is the type of the change
     * @param changeValue is optional change info about the change
     */
    public void publishChange(final ProjectDiagramChange.ChangeType changeType, final Object changeValue){
        final ProjectDiagramChange change = new ProjectDiagramChange(this, changeType, changeValue, null);

        for(final ProjectDiagramListener listener : listeners){
            listener.changePerformed(change);
        }
    }

    /**
     * Publishes the change that has occurred.
     *
     * @param changeType is the type of the change
     * @param changeValue is optional change info about the change
     * @param oldValue is the old value before the change
     */
    public void publishChange(final ProjectDiagramChange.ChangeType changeType, final Object changeValue, final Object oldValue){
        final ProjectDiagramChange change = new ProjectDiagramChange(this, changeType, changeValue, oldValue);

        for(final ProjectDiagramListener listener : listeners){
            listener.changePerformed(change);
        }
    }
}
