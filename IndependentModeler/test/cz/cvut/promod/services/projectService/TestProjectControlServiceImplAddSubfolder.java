package cz.cvut.promod.services.projectService;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Method;

import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectSubFolder;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import static junit.framework.Assert.assertEquals;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:27:05, 5.2.2010
 */
public class TestProjectControlServiceImplAddSubfolder {

    private static final String PROJECT_NAME = "Project 1";

    private static final String SUBFOLDER_1_NAME = "Sub 1";
    private static final String SUBFOLDER_1_NAME_DUP = "Sub 1";
    private static final String SUBFOLDER_2_NAME = "Sub 2";

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private ProjectControlService projectService;

    private EPCNotation epcNotation;

    private TreePath projectTreePath;

    private ProjectRoot projectRoot;


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
        } catch (InstantiationException e) {
            fail();
        }

        final NotationSpecificPlugins epcNotationPlugins = new NotationSpecificPlugins(epcNotation, Collections.unmodifiableList(new LinkedList<Module>()));
        final List<NotationSpecificPlugins> plugins = new LinkedList<NotationSpecificPlugins>();
        plugins.add(epcNotationPlugins);

        ModelerSession.setNotationService(new NotationServiceImpl(
                plugins,
                new LinkedList<PluginLoadErrors>()
        ));

        ModelerSession.setProjectControlService(projectService);

        projectRoot = new ProjectRoot(PROJECT_NAME, null);
        AddProjectItemResult result = projectService.addProject(projectRoot, true);
        assertEquals(result.getStatus(), AddProjectItemStatus.SUCCESS);

        assertTrue(projectService.getProjects().size() == 1);
        assertEquals(projectService.getProjects().get(0), projectService.getProjects().get(0));

        projectTreePath = result.getTreePath();

        final List<TreePath> treePaths =  projectService.getProjectPaths();
        assertTrue(treePaths.size() == 1);

        assertEquals(treePaths.get(0), projectTreePath);
    }

    @After
    public void tearDown(){
        try { // null services
            final Method nullServicesMethod = ModelerSession.class.getDeclaredMethod(ModelerSession.NULL_METHOD_NAME);
            nullServicesMethod.setAccessible(true);
            nullServicesMethod.invoke(null);
            nullServicesMethod.setAccessible(false);
        } catch (Exception e) {
            junit.framework.Assert.fail();
        }
    }    

    /**
     * Nullary subfolder name.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder1(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(null, projectTreePath, true);
        assertTrue(AddProjectItemStatus.INVALID_NAME.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Empty subfolder name.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder2(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder("", projectTreePath, true);
        assertTrue(AddProjectItemStatus.INVALID_NAME.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Subfolder name containing some disallowed symbols.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder3(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(
                SUBFOLDER_1_NAME + ProjectServiceUtils.DISALLOWES_FILE_NAME_SYMBOLS[0],
                projectTreePath, true);
        assertTrue(AddProjectItemStatus.INVALID_NAME.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Nullary parent tree path.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder4(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, null, true);
        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Invalid (not existing) parent tree path.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder5(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        TreePath treePath = new TreePath(new Object[]{
                new DefaultMutableTreeNode(),
                new DefaultMutableTreeNode()});

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, treePath, true);
        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Invalid (not project container) parent tree path.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder6(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addDiagram(
                new ProjectDiagram("Diagram", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel()),
                projectTreePath,
                false
        );

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        result = projectService.addSubFolder(SUBFOLDER_1_NAME, result.getTreePath(), true);
        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Two same names under one parent.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder7(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, projectTreePath, false);
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        final Object subfolderObjectBefore = projectNode.getChildAt(0);

        result = projectService.addSubFolder(SUBFOLDER_1_NAME_DUP, projectTreePath, false);
        assertTrue(AddProjectItemStatus.NAME_DUPLICITY.equals(result.getStatus()));

        projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        final Object subfolderObjectAfter = projectNode.getChildAt(0);

        assertEquals(subfolderObjectBefore, subfolderObjectAfter);        

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }


    /**
     * Success. 2 subfolders with different names.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder8(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, projectTreePath, false);
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);

        result = projectService.addSubFolder(SUBFOLDER_2_NAME, projectTreePath, false);
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        assertTrue(projectNode.getChildCount() == 2);

        selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Selected project item is changed on success if required.
     */
    @Test
    public void testAddSubfolder9(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, projectTreePath, true); // true
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore.pathByAddingChild(projectNode.getFirstChild()));
    }

    /**
     * Inserting a subfolder under another subfolder, but all subfolders have the same name.
     * SUCCESS
     */
    @Test
    public void testAddSubfolder10(){
        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, projectTreePath, true); // true
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        result = projectService.addSubFolder(SUBFOLDER_1_NAME_DUP, selectedTreePathAfter, true); // true
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        assertTrue(projectNode.getChildCount() == 1);

        projectNode = (DefaultMutableTreeNode) selectedTreePathAfter.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 1);

        // is selected tge lately added subfolder
        assertEquals(result.getTreePath(), selectedTreePathAfter.pathByAddingChild(projectNode.getFirstChild()));
    }

    /**
     * Using 2 parameter version of addSubfolder method.
     * Adding subfolder if a diagram is currently selected.
     */
    @Test
    public void testAddSubfolder11(){
        final TreePath projectTreePath = projectService.getSelectedTreePath();

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode)projectTreePath.getLastPathComponent();

        assertTrue(projectNode.getUserObject() == projectRoot);
        assertTrue(projectNode.getChildCount() == 0);

        AddProjectItemResult result = projectService.addDiagram(
                new ProjectDiagram("Diagram", epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel()),
                projectTreePath,
                true
        );

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        assertTrue(projectService.getSelectedDiagramPath() != null);
        assertEquals(
                projectService.getSelectedDiagramPath(),
                projectTreePath.pathByAddingChild(projectService.getSelectedDiagramPath().getLastPathComponent()));

        // the diagram is selected
        result = projectService.addSubFolder(SUBFOLDER_1_NAME, true);

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 2);
        assertTrue(projectService.getSelectedTreePath() != null);

        DefaultMutableTreeNode subfolderNode =
                (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();

        assertTrue(subfolderNode.getUserObject() instanceof ProjectSubFolder);
    }

    /**
     * Using 2 parameter version of addSubfolder method.
     * 1) Adding subfolder if project node is currently selected.
     * 2) Adding subfolder if a another subfolder is currently selected (subfolder under another subfolder).
     *
     * Diagrams have the same name.
     */
    @Test
    public void testAddSubfolder12(){
        final TreePath projectTreePath = projectService.getSelectedTreePath();

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode)projectTreePath.getLastPathComponent();

        assertTrue(projectNode.getUserObject() == projectRoot);
        assertTrue(projectNode.getChildCount() == 0);

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, true);

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        DefaultMutableTreeNode subfolderNode =
                (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();

        assertTrue(subfolderNode.getUserObject() instanceof ProjectSubFolder);

        assertTrue(projectService.getSelectedTreePath() != null);
        assertEquals(
                projectService.getSelectedTreePath(),
                projectTreePath.pathByAddingChild(projectService.getSelectedTreePath().getLastPathComponent()));

        assertTrue(subfolderNode.getChildCount() == 0);

        // the subfolder is selected
        result = projectService.addSubFolder(SUBFOLDER_1_NAME_DUP, true);

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1); // project has still just one child
        assertTrue(subfolderNode.getChildCount() == 1); // first subfolder has one child now

        assertTrue(projectService.getSelectedTreePath() != null);

        subfolderNode =
                (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();

        assertTrue(subfolderNode.getUserObject() instanceof ProjectSubFolder);
    }

    /**
     * Using 2 parameter version of addSubfolder method.
     * A diagram and a subfolder both having the same name (SUCCESS).
     */
    @Test
    public void testAddSubfolder13(){
        final TreePath projectTreePath = projectService.getSelectedTreePath();

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode)projectTreePath.getLastPathComponent();

        assertTrue(projectNode.getUserObject() == projectRoot);
        assertTrue(projectNode.getChildCount() == 0);

        AddProjectItemResult result = projectService.addDiagram(
                new ProjectDiagram(SUBFOLDER_1_NAME, epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel()),
                projectTreePath,
                false /* !false! do not select the just added diagram */
        );

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);
        assertNull(projectService.getSelectedDiagramPath()); // no diagram is selected
        assertEquals(projectTreePath, projectService.getSelectedProjectPath());

        // the diagram is selected
        result = projectService.addSubFolder(SUBFOLDER_1_NAME_DUP,
                false /* !false! do not select the just added subfolder */);

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 2);
        assertEquals(projectService.getSelectedTreePath(), projectTreePath);
    }

}