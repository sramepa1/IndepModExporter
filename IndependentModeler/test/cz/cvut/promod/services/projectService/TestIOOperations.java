package cz.cvut.promod.services.projectService;

import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectSubFolder;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.results.SaveProjectResult;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.extensionService.ExtensionServiceImpl;
import cz.cvut.promod.services.userService.UserServiceImpl;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.IndependentModelerMain;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:30:49, 8.2.2010
 */
public class TestIOOperations {

    private static final String PROJECT_NAME_1 = "Project 1";
    private static final String PROJECT_NAME_2 = "Project 2";
    private static final String PROJECT_NAME_3 = "Project 3";

    private static final String DIAG_NAME_2 = "Diag2";
    private static final String DIAG_NAME_8 = "Diag8";

    private static final String SUB_NAME_1 = "Sub1";
    private static final String SUB_NAME_2 = "Sub2";
    private static final String SUB_NAME_3 = "Sub3";
    private static final String SUB_NAME_4 = "Sub4";
    private static final String SUB_NAME_5 = "Sub5";

    private static final String SEP = System.getProperty("file.separator");

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private final static String PROJECT_1_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"files"+SEP+"project1";
    private final static String PROJECT_2_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"files"+SEP+"project2";

    private final static File PROJECT_1_FILE = new File(PROJECT_1_PATH);
    private final static File PROJECT_2_FILE = new File(PROJECT_2_PATH);

    private final static File PROJECT_FILE_1_FILE = new File(PROJECT_1_PATH + "/" + PROJECT_NAME_1 + ProjectService.PROJECT_FILE_EXTENSION);
    private final static File PROJECT_FILE_2_FILE = new File(PROJECT_2_PATH + "/" + PROJECT_NAME_2 + ProjectService.PROJECT_FILE_EXTENSION);    

    private ProjectControlService projectService;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

    private TreePath projectRoot1TreePath;
    private TreePath projectRoot2TreePath;
    private TreePath projectRoot3TreePath;

    private ProjectDiagram projectDiagram1;


    @Before
    public void setUp(){
        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
        ModelerSession.setUserService(new UserServiceImpl());
        ModelerSession.getUserService().setUser("Petr");
        ModelerSession.setExtensionService(new ExtensionServiceImpl(new LinkedList<Extension>()));
        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            fail();
        }

        try {
            epcNotation = new EPCNotation(EPC_PROPERTY_FILE);
            projectService = new ProjectControlServiceImpl();
        } catch (InstantiationException e) {
            fail();
        }

        hierarchyNotation = new ProcessHierarchyNotation();

        final NotationSpecificPlugins epcNotationPlugins = new NotationSpecificPlugins(epcNotation, Collections.unmodifiableList(new LinkedList<Module>()));
        final NotationSpecificPlugins hierarchyNotationPlugins = new NotationSpecificPlugins(hierarchyNotation, Collections.unmodifiableList(new LinkedList<Module>()));
        final List<NotationSpecificPlugins> plugins = new LinkedList<NotationSpecificPlugins>();
        plugins.add(epcNotationPlugins);
        plugins.add(hierarchyNotationPlugins);


        ModelerSession.setNotationService(new NotationServiceImpl(
                plugins,
                new LinkedList<PluginLoadErrors>()
        ));

        ModelerSession.setProjectControlService(projectService);

        // build a testing project navigation tree structure

        final ProjectRoot projectRoot1 = new ProjectRoot(PROJECT_NAME_1, PROJECT_1_PATH);
        projectDiagram1 = new ProjectDiagram(
                "Diag1", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram2 = new ProjectDiagram(
                DIAG_NAME_2, epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram3 = new ProjectDiagram(
                "Diag3", hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram4 = new ProjectDiagram(
                "Diag4", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram5 = new ProjectDiagram(
                "Diag5", hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());

        AddProjectItemResult result = projectService.addProject(projectRoot1, true);
        projectRoot1TreePath = result.getTreePath();

        projectService.addDiagram(projectDiagram1, false);
        projectService.addDiagram(projectDiagram2, false);
        projectService.addSubFolder(SUB_NAME_1, true);
        projectService.addDiagram(projectDiagram3,  false);
        projectService.addDiagram(projectDiagram4,  false);
        projectService.addSubFolder(SUB_NAME_2, true);
        projectService.addDiagram(projectDiagram5, false);

        final ProjectRoot projectRoot2 = new ProjectRoot(PROJECT_NAME_2, PROJECT_2_PATH);
        final ProjectDiagram projectDiagram6 = new ProjectDiagram(
                "Diag6", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram7 = new ProjectDiagram(
                "Diag7", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram8 = new ProjectDiagram(
                DIAG_NAME_8, hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram9 = new ProjectDiagram(
                "Diag9", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram10 = new ProjectDiagram(
                "Diag10", hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());

        result = projectService.addProject(projectRoot2, true);
        projectRoot2TreePath = result.getTreePath();

        projectService.addSubFolder(SUB_NAME_3, true);
        projectService.addDiagram(projectDiagram6, false);
        projectService.addDiagram(projectDiagram7, false);
        projectService.addSubFolder(SUB_NAME_4, true);
        projectService.addDiagram(projectDiagram8, false);
        projectService.addDiagram(projectDiagram9, result.getTreePath(), false);
        projectService.addSubFolder(SUB_NAME_5, result.getTreePath(), true);
        projectService.addDiagram(projectDiagram10, true);

        final ProjectRoot projectRoot3 = new ProjectRoot(PROJECT_NAME_3, null);

        result = projectService.addProject(projectRoot3, true);
        projectRoot3TreePath = result.getTreePath();

        assertEquals(result.getStatus(), AddProjectItemStatus.SUCCESS);
        assertTrue(projectService.getProjects().size() == 3);
    }

    @After
    public void tearDown(){
        if(PROJECT_1_FILE.exists()){
            ProjectServiceUtils.removeDirectory(PROJECT_1_FILE);
        }

        if(PROJECT_2_FILE.exists()){
            ProjectServiceUtils.removeDirectory(PROJECT_2_FILE);
        }

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
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Nullary project root tree path.
     */
    @Test
    public void testSaveProject1(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        SaveProjectResult result = projectService.saveProject(null);
        assertEquals(result, SaveProjectResult.INVALID_TREE_PATH);

        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Noe existing project root tree path
     */
    @Test
    public void testSaveProject2(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        SaveProjectResult result = projectService.saveProject(new TreePath(new Object[] {new Object(), new Object()}));
        assertEquals(result, SaveProjectResult.INVALID_TREE_PATH);

        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Project tree path to a diagram.
     */
    @Test
    public void testSaveProject3(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        TreePath diagramTreePath = projectRoot1TreePath.pathByAddingChild(
                ((DefaultMutableTreeNode)projectRoot1TreePath.getLastPathComponent()).getFirstChild());

        assertEquals(((DefaultMutableTreeNode)diagramTreePath.getLastPathComponent()).getUserObject(), projectDiagram1);

        SaveProjectResult result = projectService.saveProject(diagramTreePath);
        assertEquals(result, SaveProjectResult.INVALID_TREE_PATH);

        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Project tree path to a subfolder.
     */
    @Test
    public void testSaveProject4(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        // tree path to the subfolder 3
        TreePath subfolderTreePath = projectRoot2TreePath.pathByAddingChild(
                ((DefaultMutableTreeNode)projectRoot2TreePath.getLastPathComponent()).getFirstChild());

        assertTrue(((DefaultMutableTreeNode)subfolderTreePath.getLastPathComponent()).getUserObject() instanceof ProjectSubFolder);

        SaveProjectResult result = projectService.saveProject(subfolderTreePath);
        assertEquals(result, SaveProjectResult.INVALID_TREE_PATH);

        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Tree path to an instance of ProjectRoot but invalid project navigation tree path.
     */
    @Test
    public void testSaveProject5(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        ProjectRoot projectRoot = new ProjectRoot(
                PROJECT_NAME_1, PROJECT_1_PATH
        );

        SaveProjectResult result = projectService.saveProject(
                new TreePath(new DefaultMutableTreeNode[]{
                        new DefaultMutableTreeNode("node"), new DefaultMutableTreeNode(projectRoot)
                }));
        assertEquals(result, SaveProjectResult.INVALID_TREE_PATH);

        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Saving project file. Success.
     */
    @Test
    public void testSaveProject6(){
        assertFalse(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        assertFalse(PROJECT_FILE_1_FILE.exists());
        assertFalse(PROJECT_FILE_2_FILE.exists());

        SaveProjectResult result = projectService.saveProject(projectRoot1TreePath);
        assertEquals(result, SaveProjectResult.SUCCESS);

        assertTrue(PROJECT_1_FILE.exists());
        assertFalse(PROJECT_2_FILE.exists());

        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertFalse(PROJECT_FILE_2_FILE.exists());
    }

    /**
     * Simple testing of saveProject(DefaultMutableTreeNode) method
     *
     * Nullary project file location.
     */
    @Test
    public void testSaveProject7(){
        SaveProjectResult result = projectService.saveProject(projectRoot3TreePath);
        assertEquals(result, SaveProjectResult.IOERROR);
    }

}
