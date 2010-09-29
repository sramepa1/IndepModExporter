package cz.cvut.promod.services.projectService;

import cz.cvut.promod.IndependentModelerMain;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.notationService.NotationServiceImpl;

import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:27:05, 5.2.2010
 */
public class TestProjectControlServiceImplAddProject {

    private static final String PROJECT_NAME = "Project 1";

    private ProjectControlService projectService;


    @Before
    public void setUp(){
        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            fail();
        }
        
        projectService = new ProjectControlServiceImpl();

        ModelerSession.setNotationService(new NotationServiceImpl(
                new LinkedList<NotationSpecificPlugins>(),
                new LinkedList<PluginLoadErrors>()
        ));
    }

    @After
    public void tearDown(){
        try { // null services
            final Method nullServicesMethod = ModelerSession.class.getDeclaredMethod(ModelerSession.NULL_METHOD_NAME);
            nullServicesMethod.setAccessible(true);
            nullServicesMethod.invoke(null);
            nullServicesMethod.setAccessible(false);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * Nullary project name.
     */
    @Test
    public void testAddProject1(){
        final ProjectRoot projectRoot = new ProjectRoot(null, null);
        AddProjectItemResult result = projectService.addProject(projectRoot, false);
        assertEquals(result.getStatus(), AddProjectItemStatus.INVALID_NAME);

        assertTrue(projectService.getProjects().isEmpty());
    }

    /**
     * Nullary project location, but the project is not supposed to be stored on the file system.
     */
    @Test
    public void testAddProject2(){
        final ProjectRoot projectRoot = new ProjectRoot(PROJECT_NAME, null);
        AddProjectItemResult result = projectService.addProject(projectRoot, false);
        assertEquals(result.getStatus(), AddProjectItemStatus.SUCCESS);

        assertTrue(projectService.getProjects().size() == 1);
    }

    /**
     * Adding new 2 projects, but the second has the same name is the first one.
     */
    @Test
    public void testAddProject4(){
        ProjectRoot projectRoot = new ProjectRoot(PROJECT_NAME, null);
        AddProjectItemResult result = projectService.addProject(projectRoot, false);
        assertEquals(result.getStatus(), AddProjectItemStatus.SUCCESS);

        assertTrue(projectService.getProjects().size() == 1);
        final ProjectRoot pr1 = projectService.getProjects().get(0);


        projectRoot = new ProjectRoot(PROJECT_NAME, null);
        result = projectService.addProject(projectRoot, false);
        assertEquals(result.getStatus(), AddProjectItemStatus.NAME_DUPLICITY);

        assertTrue(projectService.getProjects().size() == 1);
        final ProjectRoot pr2 = projectService.getProjects().get(0);

        assertEquals(pr1, pr2); // no second project has been added
    }

    /**
     * Adding project with project name containing any disallowed symbols.
     */
    @Test
    public void testAddProject5(){
        // project name with a disallowed symbol
        ProjectRoot projectRoot = new ProjectRoot(PROJECT_NAME + ProjectServiceUtils.DISALLOWES_FILE_NAME_SYMBOLS[0], null);

        AddProjectItemResult result = projectService.addProject(projectRoot, false);
        assertEquals(result.getStatus(), AddProjectItemStatus.INVALID_NAME);

        assertTrue(projectService.getProjects().isEmpty());
    }

}
