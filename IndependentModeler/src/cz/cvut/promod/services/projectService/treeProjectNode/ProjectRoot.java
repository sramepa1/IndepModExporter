package cz.cvut.promod.services.projectService.treeProjectNode;

import cz.cvut.promod.services.projectService.ProjectService;

import java.io.File;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:02:45, 13.10.2009
 */

/**
 * Represents the project root object.
 */
public final class ProjectRoot implements ProjectContainer {

    private String displayName;

    /* location of the project file (having ProjectService.PROJECT_FILE_EXTENSION_NAME extension) is not taken into
        account during saving the project root */ 
    transient private String projectLocation;


    /**
     * Constructs a new instance of ProjectRoot class.
     *
     * @param displayName is the project name (will be trimmed)
     * @param projectLocation is the project location on the file system (will be trimmed)
     */
    public ProjectRoot(final String displayName, final String projectLocation){
        if(displayName != null){
            this.displayName = displayName.trim();
        }
        
        if(projectLocation != null){
            this.projectLocation = projectLocation.trim();
        }
    }

    /** {@inheritDoc} */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName.trim();
    }

    /** {@inheritDoc} */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the project file system location
     */
    public String getProjectLocation() {
        return projectLocation;
    }

    /**
     * Sets the file system location
     *
     * @param projectLocation is the new project file system location
     */
    public void setProjectLocation(final String projectLocation) {
        this.projectLocation = projectLocation.trim();
    }

    /**
     * Returns the project file.
     *
     * @return the project file
     */
    public File getProjectFile(){
        return new File(getProjectLocation(), getDisplayName() + ProjectService.PROJECT_FILE_EXTENSION);
    }

    @Override
    public String toString(){
        return getDisplayName();
    }

}