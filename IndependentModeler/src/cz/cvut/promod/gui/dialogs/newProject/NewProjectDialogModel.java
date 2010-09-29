package cz.cvut.promod.gui.dialogs.newProject;

import com.jgoodies.binding.beans.Model;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:40:17, 20.10.2009
 *
 * The model component for NewProjectDialog.
 */
public class NewProjectDialogModel extends Model{

    public static final String PROPERTY_PROJECT_LOCATION = "projectLocation";
    private String projectLocation;

    public static final String PROPERTY_PROJECT_NAME = "projectName";
    private String projectName;

    private static final String DEFAULT_PROJECT_NAME = "Project";
    private int projectNumber = 1;

    private static final String DEFAULT_PROJECT_DIRECTORY = "ProMod";


    /**
     * Generates the initial name for new project.
     */
    public void generateInitialProjectName() {
        String projectName = DEFAULT_PROJECT_NAME + projectNumber;

        // test whether the is no open project with the same name as the implicit one
        boolean testName;
        do{
            testName = false;
            for(final ProjectRoot projectRoot : ModelerSession.getProjectService().getProjects()){
                if(projectRoot.getDisplayName().equals(projectName)){
                    testName = true;
                    ++projectNumber;
                    projectName = DEFAULT_PROJECT_NAME + projectNumber;
                }
            }
        } while (testName);

        setProjectName(DEFAULT_PROJECT_NAME + projectNumber);
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(final String projectLocation) {
        final String oldProjectLocation = this.projectLocation;
        this.projectLocation = projectLocation;
        firePropertyChange(PROPERTY_PROJECT_LOCATION, oldProjectLocation, projectLocation);
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String projectName) {
        final String oldProjectName = this.projectName;
        this.projectName = projectName;
        firePropertyChange(PROPERTY_PROJECT_NAME, oldProjectName, projectName);
    }

    public void generateInitialProjectLocation() {
        setProjectLocation(System.getProperty("user.home") +
                                                System.getProperty("file.separator") +
                                                DEFAULT_PROJECT_DIRECTORY +
                                                System.getProperty("file.separator") +
                                                getProjectName());
    }
}
