package cz.cvut.promod.services.projectService.treeProjectNode;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 8:31:21, 18.1.2010
 */

/**
 * ProjectDiagramChange class contains detail information about change that occurred in an instance of
 * ProjectDiagram class.
 */
public class ProjectDiagramChange {

    /**
     * Standard project diagram change types.
     */
    public static enum ChangeType {
        /**
         * The displayed name has changes.
         */
        DISPLAY_NAME,

        /**
         * The project diagram change flag has changed.
         */
        CHANGE_FLAG,

        /**
         * Diagram model od project diagram has changed.
         */
        DIAGRAM_MODEL,

        /**
         * Project diagram has been removed from the project navigation tree.
         */
        REMOVED_FROM_NAVIGATION,

        /**
         * When the project diagram object in the project navigation tree has been replaced bt another
         * project diagram object. When is event is dispatched by the ProjectControlService, the project
         * diagram variable is the new object and the changeValue holds the old object of project diagram.
         */
        PROJECT_DIAGRAM_REPLACED,

        /**
         * Special purposes.
         */
        UNDEFINED
    }

    private final ChangeType changeType;

    /** Any object holding detail info about the particular change */
    private final Object changeValue;

    /** Any object holding detail info about the particular change */
    private final Object oldValue;

    /** source diagram */
    private final ProjectDiagram projectDiagram;

    /**
     * Instantiate new ProjectDiagramChange object.
     *
     * @param projectDiagram project diagram that has changed
     * @param changeType is the change type
     * @param changeValue change payload, detail information
     */
    public ProjectDiagramChange(final ProjectDiagram projectDiagram,
                                final ChangeType changeType,
                                final Object changeValue,
                                final Object oldValue){
        this.changeType = changeType;
        this.changeValue = changeValue;
        this.projectDiagram = projectDiagram;
        this.oldValue = oldValue;
    }

    /**
     * Instantiate new ProjectDiagramChange object.
     *
     * @param projectDiagram project diagram that has changed
     * @param changeType is the change type
     */
    public ProjectDiagramChange(final ProjectDiagram projectDiagram, final ChangeType changeType){
        this(projectDiagram, changeType, null, null);
    }

    /**
     * @return returns the changed project diagram
     */
    public ProjectDiagram getProjectDiagram() {
        return projectDiagram;
    }

    /**
     * @return the old value, the value before change
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * @return the change type
     */
    public ChangeType getChangeType() {
        return changeType;
    }

    /**
     * @return the change payload
     */
    public Object getChangeValue() {
        return changeValue;
    }
}
