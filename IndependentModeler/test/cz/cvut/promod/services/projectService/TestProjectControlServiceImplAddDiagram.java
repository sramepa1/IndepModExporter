package cz.cvut.promod.services.projectService;
import org.junit.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Method;

import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
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
public class TestProjectControlServiceImplAddDiagram {

    private static final String PROJECT_NAME = "Project 1";
    private static final String SUBFOLDER_1_NAME = "Sub 1";

    private static final String DIAGRAM_1_NAME = "Diag 1";
    private static final String DIAGRAM_1_NAME_DUP = "Diag 1";
    private static final String DIAGRAM_2_NAME = "Diag 2";

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private ProjectControlService projectService;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

    private TreePath projectTreePath;

    @Before
    public void setUp(){
        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            fail();
        }

        projectService = new ProjectControlServiceImpl();

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

        final ProjectRoot projectRoot = new ProjectRoot(PROJECT_NAME, null);
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
     * Nullary diagram name.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder1(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final ProjectDiagram projectDiagram = new ProjectDiagram(
                    null,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathBefore,
                false
        );

        assertTrue(AddProjectItemStatus.INVALID_NAME.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Empty diagram name.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder2(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final ProjectDiagram projectDiagram = new ProjectDiagram(
                    "",
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathBefore,
                false
        );

        assertTrue(AddProjectItemStatus.INVALID_NAME.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Diagram name containing some disallowed symbols.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder3(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME + ProjectServiceUtils.DISALLOWES_FILE_NAME_SYMBOLS[2],
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathBefore,
                false
        );

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
        ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                null,
                false
        );

        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

     /* Invalid (not existing) parent tree path.
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder5(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final ProjectDiagram projectDiagram = new ProjectDiagram(
                     DIAGRAM_1_NAME,
                     epcNotation.getIdentifier(),
                     UUID.randomUUID(),
                     epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

         AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                new TreePath(new Object[]{new Object(), new Object()}),
                false
        );

        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));

        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();
        assertTrue(projectNode.getChildCount() == 0);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathAfter, selectedTreePathBefore);
    }

    /**
     * Invalid (not project container) parent tree path.
     *
     * Selected project item is not affected (point to the diagram node that was inserted first).
     */
    @Test
    public void testAddSubfolder6(){
        TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        final ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathBefore,
                true
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        final DefaultMutableTreeNode diagramNode =
                (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();
        assertTrue(diagramNode.getChildCount() == 0);
        assertTrue(projectNode.getChildCount() == 1);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathAfter,
                true
        );
        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));
    }

    /**
     * Two same names under one parent (same notation).
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder7(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        final ProjectDiagram projectDiagram1 = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        final ProjectDiagram projectDiagram2 = new ProjectDiagram(
                    DIAGRAM_1_NAME_DUP,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram1,
                selectedTreePathBefore,
                false
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        result = projectService.addDiagram(
                projectDiagram2,
                selectedTreePathBefore,
                false
        );
        assertTrue(AddProjectItemStatus.NAME_DUPLICITY.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathBefore, selectedTreePathAfter);
    }

    /**
     * Two same names under one parent (different notation).   SUCCESS
     *
     * Selected project item is not affected.
     */
    @Test
    public void testAddSubfolder8(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        final ProjectDiagram projectDiagram1 = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        final ProjectDiagram projectDiagram2 = new ProjectDiagram(
                    DIAGRAM_1_NAME_DUP,
                    hierarchyNotation.getIdentifier(),
                    UUID.randomUUID(),
                    hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram1,
                selectedTreePathBefore,
                false
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        result = projectService.addDiagram(
                projectDiagram2,
                selectedTreePathBefore,
                false
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 2);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(selectedTreePathBefore, selectedTreePathAfter);
    }

    /**
     * Selected project item is changed on success if required.
     */
    @Test
    public void testAddSubfolder9(){
        TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        final ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
            );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram,
                selectedTreePathBefore,
                true
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));

        final DefaultMutableTreeNode diagramNode =
                (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();
        assertTrue(diagramNode.getChildCount() == 0);
        assertTrue(projectNode.getChildCount() == 1);

        final TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(
                selectedTreePathAfter,
                selectedTreePathBefore.pathByAddingChild(projectService.getSelectedTreePath().getLastPathComponent()));
    }

    /**
     * Inserting a diagram under another diagram.
     * Both have the same name, but it does NOT matter.
     */
    @Test
    public void testAddSubfolder10(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        ProjectDiagram projectDiagram1 = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        ProjectDiagram projectDiagram2 = new ProjectDiagram(
                    DIAGRAM_1_NAME_DUP,
                    hierarchyNotation.getIdentifier(),
                    UUID.randomUUID(),
                    hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram1,
                selectedTreePathBefore,
                true /* select the newly added diagram*/
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        result = projectService.addDiagram(
                projectDiagram2,
                projectService.getSelectedTreePath(), // path to the previously added diagram node
                false
        );
        assertTrue(AddProjectItemStatus.ILLEGAL_PARENT.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        TreePath selectedTreePathAfter = projectService.getSelectedTreePath();

        assertEquals(
                selectedTreePathBefore.pathByAddingChild(projectService.getSelectedTreePath().getLastPathComponent()), 
                selectedTreePathAfter);
    }

    /**
     * Adding diagram under subfolder.
     */
    @Test
    public void testAddSubfolder11(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addSubFolder(SUBFOLDER_1_NAME, true);
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        final DefaultMutableTreeNode subfolderNode = (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();
        final TreePath subfolderTreePath = projectService.getSelectedTreePath();
        assertEquals(
                selectedTreePathBefore.pathByAddingChild(subfolderNode),
                projectService.getSelectedTreePath());

        result = projectService.addDiagram(
                projectDiagram,
                projectService.getSelectedTreePath(),
                true
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);
        assertTrue(subfolderNode.getChildCount() == 1);

        assertEquals(
                subfolderTreePath.pathByAddingChild(projectService.getSelectedTreePath().getLastPathComponent()),
                projectService.getSelectedTreePath()
        );
    }

    /**
     * Using 2 parameter version of addDiagram method.
     * Adding diagram if another diagram is currently selected.
     */
    @Test
    public void testAddSubfolder12(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        ProjectDiagram projectDiagram1 = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        ProjectDiagram projectDiagram2 = new ProjectDiagram(
                    DIAGRAM_2_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addDiagram(
                projectDiagram1,
                selectedTreePathBefore,
                true
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        assertEquals(projectDiagram1, projectService.getSelectedDiagram());

        result = projectService.addDiagram(
                projectDiagram2,
                selectedTreePathBefore,
                true
        );
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 2);

        assertEquals(projectDiagram2, projectService.getSelectedDiagram());
    }

    /**
     * Using 2 parameter version of addDiagram method.
     * A diagram and a subfolder both having the same name (SUCCESS).
     */
    @Test
    public void testAddSubfolder13(){
        final TreePath selectedTreePathBefore = projectService.getSelectedTreePath();
        final DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode) selectedTreePathBefore.getLastPathComponent();

        assertTrue(projectNode.getChildCount() == 0);

        ProjectDiagram projectDiagram = new ProjectDiagram(
                    DIAGRAM_1_NAME,
                    epcNotation.getIdentifier(),
                    UUID.randomUUID(),
                    epcNotation.getDiagramModelFactory().createEmptyDiagramModel()
        );

        AddProjectItemResult result = projectService.addSubFolder(
                SUBFOLDER_1_NAME,
                true);
        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);

        final DefaultMutableTreeNode subfolderNode = (DefaultMutableTreeNode) projectService.getSelectedTreePath().getLastPathComponent();
        final TreePath subfolderTreePath = selectedTreePathBefore.pathByAddingChild(subfolderNode);

        assertEquals(subfolderTreePath, projectService.getSelectedTreePath());

        result = projectService.addDiagram( // 2 parameters version
                projectDiagram,
                false
        );

        assertTrue(AddProjectItemStatus.SUCCESS.equals(result.getStatus()));
        assertTrue(projectNode.getChildCount() == 1);
        assertTrue(subfolderNode.getChildCount() == 1);

        assertEquals(subfolderTreePath, projectService.getSelectedTreePath()
        );
    }

}