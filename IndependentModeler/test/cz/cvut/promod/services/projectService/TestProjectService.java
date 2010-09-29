package cz.cvut.promod.services.projectService;

import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;

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
import static org.junit.Assert.assertFalse;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertNotNull;
import junit.framework.Assert;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:23:34, 8.2.2010
 */
public class TestProjectService {

    private static final String PROJECT_NAME_1 = "Project 1";
    private static final String PROJECT_NAME_2 = "Project 2";

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

    private ProjectControlService projectService;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

    private ProjectRoot projectRoot1;
    private ProjectRoot projectRoot2;

    private TreePath projectRoot1TreePath;
    private TreePath projectRoot2TreePath;

    private ProjectDiagram projectDiagram1;
    private ProjectDiagram projectDiagram2;
    private ProjectDiagram projectDiagram4;
    private ProjectDiagram projectDiagram6;


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

        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());

        try {
            epcNotation = new EPCNotation(EPC_PROPERTY_FILE);
            hierarchyNotation = new ProcessHierarchyNotation();
        } catch (InstantiationException e) {
            fail();
        }

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

        try{
            projectRoot1 = new ProjectRoot(PROJECT_NAME_1, PROJECT_1_PATH);
            projectDiagram1 = new ProjectDiagram(
                    "Diag1", epcNotation.getIdentifier(), UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram2 = new ProjectDiagram(
                    DIAG_NAME_2, epcNotation.getIdentifier(), UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
            final ProjectDiagram projectDiagram3 = new ProjectDiagram(
                    "Diag3", hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                    hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram4 = new ProjectDiagram(
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
        } catch (Exception e){
            fail();
        }

        try{
            projectRoot2 = new ProjectRoot(PROJECT_NAME_2, PROJECT_2_PATH);
            projectDiagram6 = new ProjectDiagram(
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

            AddProjectItemResult result = projectService.addProject(projectRoot2, true);
            projectRoot2TreePath = result.getTreePath();

            projectService.addSubFolder(SUB_NAME_3, true);
            projectService.addDiagram(projectDiagram6, false);
            projectService.addDiagram(projectDiagram7, false);
            projectService.addSubFolder(SUB_NAME_4, true);
            projectService.addDiagram(projectDiagram8, false);
            projectService.addDiagram(projectDiagram9, result.getTreePath(), false);
            projectService.addSubFolder(SUB_NAME_5, result.getTreePath(), true);
            projectService.addDiagram(projectDiagram10, true);
        } catch (Exception e){
            fail();
        }
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
     * Simple testing of getProjects() method.
     */
    @Test
    public void testGetProjects(){
        final List<ProjectRoot> projectRoots = projectService.getProjects();

        assertTrue(projectRoots.size() == 2);
        assertEquals(projectRoots.get(0), projectRoot1);
        assertEquals(projectRoots.get(1), projectRoot2);

        String path = projectService.removeProjectItem(projectService.getProjectPaths().get(0));
        assertEquals(path, PROJECT_1_PATH);

        assertTrue(projectService.getProjects().size() == 1);
        assertEquals(projectService.getProjects().get(0), projectRoot2);

        path = projectService.removeProjectItem(projectService.getProjectPaths().get(0));
        assertEquals(path, PROJECT_2_PATH);
        
        assertTrue(projectService.getProjects().size() == 0);        
    }

    /**
     * Simple testing of getProjectPaths() method.
     */
    @Test
    public void testGetProjectPaths(){
        final List<TreePath> treePaths = projectService.getProjectPaths();

        assertTrue(treePaths.size() == 2);
        assertEquals(((DefaultMutableTreeNode)treePaths.get(0).getLastPathComponent()).getUserObject(), projectRoot1);
        assertEquals(((DefaultMutableTreeNode)treePaths.get(1).getLastPathComponent()).getUserObject(), projectRoot2);

        String path = projectService.removeProjectItem(treePaths.get(0));
        assertEquals(path, PROJECT_1_PATH);

        assertTrue(projectService.getProjectPaths().size() == 1);
        assertEquals(((DefaultMutableTreeNode)treePaths.get(1).getLastPathComponent()).getUserObject(), projectRoot2);

        path = projectService.removeProjectItem(treePaths.get(1));
        assertEquals(path, PROJECT_2_PATH);
        
        assertTrue(projectService.getProjectPaths().size() == 0);
    }

     /**
     * Simple testing of getProject(string) method.
     */
    @Test
    public void testGetProject(){
        ProjectRoot projectRoot = projectService.getProject(null);
        assertNull(projectRoot);

        projectRoot = projectService.getProject("");
        assertNull(projectRoot);

        projectRoot = projectService.getProject(PROJECT_NAME_1 + "_");
        assertNull(projectRoot);

        projectRoot = projectService.getProject(PROJECT_NAME_1);
        assertEquals(projectRoot, projectRoot1);

        projectRoot = projectService.getProject(PROJECT_NAME_2);
        assertEquals(projectRoot, projectRoot2);

        TreePath treePath = projectService.getProjectPath(PROJECT_NAME_1);

        String path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_1_PATH);

        projectRoot = projectService.getProject(PROJECT_NAME_1);
        assertNull(projectRoot);

        treePath = projectService.getProjectPath(PROJECT_NAME_2);

        path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_2_PATH);

        projectRoot = projectService.getProject(PROJECT_NAME_2);
        assertNull(projectRoot);
    }

     /**
     * Simple testing of getProjectTreeNode(string) method.
     */
    @Test
    public void testGetProjectTreeNode(){
        DefaultMutableTreeNode node = projectService.getProjectTreeNode(null); 
        assertNull(node);

        node = projectService.getProjectTreeNode("");
        assertNull(node);

        node = projectService.getProjectTreeNode(PROJECT_NAME_1 + "_");
        assertNull(node);

        node = projectService.getProjectTreeNode(PROJECT_NAME_1);
        assertEquals(node.getUserObject(), projectRoot1);

        node = projectService.getProjectTreeNode(PROJECT_NAME_2);
        assertEquals(node.getUserObject(), projectRoot2);

        TreePath treePath = projectService.getProjectPath(PROJECT_NAME_1);

        String path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_1_PATH);

        node = projectService.getProjectTreeNode(PROJECT_NAME_1);
        assertNull(node);

        treePath = projectService.getProjectPath(PROJECT_NAME_2);

        path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_2_PATH);

        node = projectService.getProjectTreeNode(PROJECT_NAME_2);
        assertNull(node);
    }

     /**
     * Simple testing of getProjectPath(string) method.
     */
    @Test
    public void testGetProjectPath(){
        TreePath treePath = projectService.getProjectPath(null);
        assertNull(treePath);

        treePath = projectService.getProjectPath("");
        assertNull(treePath);

        treePath = projectService.getProjectPath(PROJECT_NAME_1 + "_");
        assertNull(treePath);

        treePath = projectService.getProjectPath(PROJECT_NAME_1);
        assertEquals(((DefaultMutableTreeNode)treePath.getLastPathComponent()).getUserObject(), projectRoot1);

        treePath = projectService.getProjectPath(PROJECT_NAME_2);
        assertEquals(((DefaultMutableTreeNode)treePath.getLastPathComponent()).getUserObject(), projectRoot2);

        treePath = projectService.getProjectPath(PROJECT_NAME_1);

        String path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_1_PATH);

        treePath = projectService.getProjectPath(PROJECT_NAME_1);
        assertNull(treePath);

        treePath = projectService.getProjectPath(PROJECT_NAME_2);

         path = projectService.removeProjectItem(treePath);
         assertEquals(path, PROJECT_2_PATH);

        treePath = projectService.getProjectPath(PROJECT_NAME_2);
        assertNull(treePath);
    }

     /**
     * Simple testing of isOpenedProject(string) method.
     */
    @Test
    public void testIsOpenedProject(){
        assertFalse(projectService.isOpenedProject(null));

        assertFalse(projectService.isOpenedProject(""));

        assertFalse(projectService.isOpenedProject(PROJECT_NAME_1 + "_"));

        assertTrue(projectService.isOpenedProject(PROJECT_NAME_1));

        assertTrue(projectService.isOpenedProject(PROJECT_NAME_2));

        TreePath treePath = projectService.getProjectPath(PROJECT_NAME_1);

        String path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_1_PATH);

        assertFalse(projectService.isOpenedProject(PROJECT_NAME_1));

        treePath = projectService.getProjectPath(PROJECT_NAME_2);

        path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_2_PATH);

        assertFalse(projectService.isOpenedProject(PROJECT_NAME_2));
    }

    /**
     *  Simple testing of all getSelectedXyz methods.
     */
    @Test
    public void testGetSelectedXyz(){
        projectService.setSelectedItem(projectRoot1TreePath);
        assertEquals(projectService.getSelectedTreePath(), projectRoot1TreePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot1TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot1);
        assertNull(projectService.getSelectedDiagramPath());
        assertNull(projectService.getSelectedDiagram());

        projectService.setSelectedItem(projectRoot2TreePath);
        assertEquals(projectService.getSelectedTreePath(), projectRoot2TreePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot2TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot2);
        assertNull(projectService.getSelectedDiagramPath());
        assertNull(projectService.getSelectedDiagram());

        //select subfolder 3
        TreePath treePath =
                projectRoot2TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot2TreePath.getLastPathComponent()).getFirstChild());
        projectService.setSelectedItem(treePath);
        assertEquals(projectService.getSelectedTreePath(), treePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot2TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot2);
        assertNull(projectService.getSelectedDiagramPath());
        assertNull(projectService.getSelectedDiagram());

        //select diagram 6, project 2
        treePath =
                projectRoot2TreePath.pathByAddingChild(((DefaultMutableTreeNode)treePath.getLastPathComponent()).getFirstChild());
        projectService.setSelectedItem(treePath);
        assertEquals(projectService.getSelectedTreePath(), treePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot2TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot2);
        assertNotNull(projectService.getSelectedDiagramPath());
        assertEquals(
                ((DefaultMutableTreeNode)projectService.getSelectedDiagramPath().getLastPathComponent()).getUserObject(),
                projectDiagram6);
        assertEquals(projectService.getSelectedDiagram(), projectDiagram6);

        //select diagram 2, project 1
        treePath =
                projectRoot1TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot1TreePath.getLastPathComponent()).getChildAt(1));
        projectService.setSelectedItem(treePath);
        assertEquals(projectService.getSelectedTreePath(), treePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot1TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot1);
        assertNotNull(projectService.getSelectedDiagramPath());
        assertEquals(
                ((DefaultMutableTreeNode)projectService.getSelectedDiagramPath().getLastPathComponent()).getUserObject(),
                projectDiagram2);
        assertEquals(projectService.getSelectedDiagram(), projectDiagram2);

        //select subfolder 1, project 1
        treePath =
                projectRoot1TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot1TreePath.getLastPathComponent()).getChildAt(2));
        projectService.setSelectedItem(treePath);
        assertEquals(projectService.getSelectedTreePath(), treePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot1TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot1);
        assertNull(projectService.getSelectedDiagramPath());
        assertNull(projectService.getSelectedDiagram());

        //select subfolder 4, project 1
        treePath =
                projectRoot1TreePath.pathByAddingChild(((DefaultMutableTreeNode)treePath.getLastPathComponent()).getChildAt(1));
        projectService.setSelectedItem(treePath);
        assertEquals(projectService.getSelectedTreePath(), treePath);
        assertEquals(projectService.getSelectedProjectPath(), projectRoot1TreePath);
        assertEquals(projectService.getSelectedProject(), projectRoot1);
        assertNotNull(projectService.getSelectedDiagramPath());
        assertEquals(
                ((DefaultMutableTreeNode)projectService.getSelectedDiagramPath().getLastPathComponent()).getUserObject(),
                projectDiagram4);
        assertEquals(projectService.getSelectedDiagram(), projectDiagram4);
    }

    /**
     * Simple testing of removeProjectItem(TreePath) method.
     */
    @Test
    public void testRemoveProjectItem(){
        // this method is sufficiently tested in other tests

        String path = projectService.removeProjectItem(null);
        assertNull(path);

        path = projectService.removeProjectItem(new TreePath(new Object[]{
                new Object(), new Object()
        }));
        assertNull(path);

        //remove diagram 8
        TreePath treePath = // sub 3
                projectRoot2TreePath.pathByAddingChild(
                        ((DefaultMutableTreeNode)projectRoot2TreePath.getLastPathComponent()).getChildAt(0)
                );
        TreePath treePathSub4 = // sub 4
                treePath.pathByAddingChild(
                        ((DefaultMutableTreeNode)treePath.getLastPathComponent()).getChildAt(2)
                );
        treePath = // diag 8
                treePathSub4.pathByAddingChild(
                        ((DefaultMutableTreeNode)treePathSub4.getLastPathComponent()).getChildAt(0)
                );

        projectService.setSelectedItem(treePath);

        path = projectService.removeProjectItem(treePath);
        assertEquals(
                path,
                PROJECT_2_PATH + SEP + SUB_NAME_3 + SEP + SUB_NAME_4 + SEP + DIAG_NAME_8 + ProjectService.FILE_EXTENSION_DELIMITER + hierarchyNotation.getLocalIOController().getNotationFileExtension());

        assertEquals(projectService.getSelectedTreePath(), treePathSub4);

        //remove subfolder 3
        treePath =
                projectRoot2TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot2TreePath.getLastPathComponent()).getFirstChild());

        // but select a child of subfolder 3. diag 6
        projectService.setSelectedItem(treePath.pathByAddingChild(((DefaultMutableTreeNode)treePath.getLastPathComponent()).getFirstChild()));
        assertEquals(projectService.getSelectedDiagram(), projectDiagram6);

        path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_2_PATH + SEP + SUB_NAME_3);

        // project 2 is supposed to be selected, because it's parent of subfolder 3
        assertEquals(projectService.getSelectedTreePath(), projectRoot2TreePath);

        //remove diagram 2, project 1
        treePath =
                projectRoot1TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot1TreePath.getLastPathComponent()).getChildAt(1));

        // but select diagram 1, the selection shouldn't be changed
        final TreePath diag1TreePath =
                projectRoot1TreePath.pathByAddingChild(((DefaultMutableTreeNode)projectRoot1TreePath.getLastPathComponent()).getFirstChild());
        projectService.setSelectedItem(diag1TreePath);

        assertEquals(projectService.getSelectedDiagram(), projectDiagram1); // assert that the diag1 is really selected

        path = projectService.removeProjectItem(treePath);
        assertEquals(path, PROJECT_1_PATH + SEP +DIAG_NAME_2 + ProjectService.FILE_EXTENSION_DELIMITER 
                + epcNotation.getLocalIOController().getNotationFileExtension());

        assertEquals(projectService.getSelectedTreePath(), diag1TreePath);
    }
}
