package cz.cvut.promod.services.projectService;

import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.projectService.results.LoadProjectResult;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectSubFolder;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.services.extensionService.ExtensionServiceImpl;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.userService.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static junit.framework.Assert.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:27:44, 8.2.2010
 */
public class TestSyncFromFS {

    private static final String SEP = System.getProperty("file.separator");

    private static final String PROJECT_NAME = "test";

    private final static String PROJECT_FS_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"sync"+SEP+"TestFromFSProject";

    private final File PROJECT_FILE = new File(PROJECT_FS_PATH);

    private final static String PROJECT_TMP_FS_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"sync"+SEP+"TestFromFSProjectTmp";

    private final File PROJECT_TMP_FILE = new File(PROJECT_TMP_FS_PATH);

    private final static File PROJECT_FILE_FS_PATH =
            new File("."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+
                    "projectService"+SEP+"sync"+SEP+"TestFromFSProject", PROJECT_NAME + ProjectService.PROJECT_FILE_EXTENSION);

    private final static File PROJECT_TMP_FILE_FS_PATH =
            new File(PROJECT_TMP_FS_PATH, PROJECT_NAME + ProjectService.PROJECT_FILE_EXTENSION);    

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private static final String DIAG_NAME_1 = "Diag1";
    private static final String DIAG_NAME_2 = "Diag2";
    private static final String DIAG_NAME_3 = "Diag3";
    private static final String DIAG_NAME_4 = "Diag4";
    private static final String DIAG_NAME_5 = "Diag5";

    private static final String SUB_NAME_1 = "Sub1";
    private static final String SUB_NAME_2 = "Sub2";

    private ProjectControlServiceImpl projectService;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

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

        assertTrue(projectService.getProjects().size() == 0);
    }

    @After
    public void tearDown(){
        if(PROJECT_TMP_FILE.exists()){
            assertTrue(ProjectServiceUtils.removeDirectory(PROJECT_TMP_FILE));
        }

        try { // null services
            final Method nullServicesMethod = ModelerSession.class.getDeclaredMethod(ModelerSession.NULL_METHOD_NAME);
            nullServicesMethod.setAccessible(true);
            nullServicesMethod.invoke(null);
            nullServicesMethod.setAccessible(false);
        } catch (Exception e) {
            fail();
        }
    }

    private void runTestOnEDT(final Runnable runnable){
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
    }

    /**
     * Not in EventDispatcherThread.
     */
    @Test
    public void testSynchronize1(){
        assertFalse(ModelerSession.getProjectControlService().synchronize(null, null, false, false, false, false));

        assertTrue(projectService.getProjects().size() == 0);
    }

    /**
     * No add, overwrite or delete - nothing to do.
     */
    @Test
    public void testSynchronize2(){
        runTestOnEDT(new Runnable(){
            public void run() {
                assertTrue(ModelerSession.getProjectControlService().synchronize(null, null, false, false, false, false));
            }
        });

        assertTrue(projectService.getProjects().size() == 0);
    }

    /**
     * Nullary project file.
     */
    @Test
    public void testSynchronize3(){
        runTestOnEDT(new Runnable(){
            public void run() {
                assertFalse(ModelerSession.getProjectControlService().synchronize(null, null, true, false, false, false));
            }
        });

        assertTrue(projectService.getProjects().size() == 0);
    }

   /**
     * Invalid project file.
     */
    @Test
    public void testSynchronize4(){
        runTestOnEDT(new Runnable(){
            public void run() {
                assertFalse(ModelerSession.getProjectControlService().synchronize(new File(""), null, true, false, false, false));
            }
        });

        assertTrue(projectService.getProjects().size() == 0);
    }

   /**
     * Invalid sync offset - not existing file system path
     */
    @Test
    public void testSynchronize5(){
       final List<String> offset = new LinkedList<String>();
       offset.add("a");
       offset.add("b");
       offset.add("c");

        runTestOnEDT(new Runnable(){
            public void run() {
                assertFalse(ModelerSession.getProjectControlService().synchronize(PROJECT_FILE_FS_PATH, offset, true, false, false, false));
            }
        });

        assertTrue(projectService.getProjects().size() == 0);
    }

   /**
     * Valid sync but not loaded project firstly.
     */
    @Test
    public void testSynchronize6(){
       final List<String> offset = new LinkedList<String>();

        runTestOnEDT(new Runnable(){
            public void run() {
                assertFalse(ModelerSession.getProjectControlService().synchronize(PROJECT_FILE_FS_PATH, offset, true, false, false, false));
            }
        });

        assertTrue(projectService.getProjects().size() == 0);
    }

    /**
      * OK, complete sync.
      */
     @Test
     public void testSynchronize7(){
        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

         assertTrue(projectService.getProjects().size() == 1);

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));
     }
    
    /**
      * OK, only one diagram sync.
      */
     @Test
     public void testSynchronize8(){
        final List<String> offset = new LinkedList<String>();
        offset.add(DIAG_NAME_1 + "." + epcNotation.getLocalIOController().getNotationFileExtension());

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));

        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));

        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));
     }

    /**
      * OK, only one subfolder sync.
      */
     @Test
     public void testSynchronize9(){
        final List<String> offset = new LinkedList<String>();
        offset.add(SUB_NAME_1);

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));
     }
   
    /**
     * Delete a project diagram
     */
     @Test
     public void testSynchronize10(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertNotNull(projectService.getProjects().size() == 1);
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        ProjectServiceUtils.removeDirectory(new File(PROJECT_TMP_FS_PATH, DIAG_NAME_1 + "." + epcNotation.getLocalIOController().getNotationFileExtension()));

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, false, false, true, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        assertTrue(PROJECT_TMP_FILE.exists());
     }

    /**
     * Delete a subfolder.
     */
     @Test
     public void testSynchronize11(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        ProjectServiceUtils.removeDirectory(new File(PROJECT_TMP_FS_PATH, SUB_NAME_1));

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, false, false, true, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        assertTrue(PROJECT_TMP_FILE.exists());
     }

    /**
     * Delete a project file.
     */
     @Test
     public void testSynchronize12(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        ProjectServiceUtils.removeDirectory(PROJECT_TMP_FILE_FS_PATH);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertFalse(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, false, false, true, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false));
        assertNotNull(getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false));

        assertTrue(PROJECT_TMP_FILE.exists());
     }

   /**
     * Overwrite, entire project.
     */
     @Test
     public void testSynchronize13(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        final DefaultMutableTreeNode node1 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node1);
        ProjectDiagram pd = (ProjectDiagram) node1.getUserObject();
        node1.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o1 = node1.getUserObject();

        final DefaultMutableTreeNode node2 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node2);
        pd = (ProjectDiagram) node2.getUserObject();
        node2.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o2 = node2.getUserObject();

        final DefaultMutableTreeNode node3 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node3);
        pd = (ProjectDiagram) node3.getUserObject();
        node3.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o3 = node3.getUserObject();

        final DefaultMutableTreeNode node4 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node4);
        pd = (ProjectDiagram) node4.getUserObject();
        node4.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o4 = node4.getUserObject();

        final DefaultMutableTreeNode node5 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node5);
        pd = (ProjectDiagram) node5.getUserObject();
        node5.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o5 = node5.getUserObject();

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, false, true, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        final DefaultMutableTreeNode node6 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node6);
        assertNotSame(o1, node6.getUserObject());

        final DefaultMutableTreeNode node7 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node7);
        assertNotSame(o2, node7.getUserObject());

        final DefaultMutableTreeNode node8 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node8);
        assertNotSame(o3, node8.getUserObject());

        final DefaultMutableTreeNode node9 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node9);
        assertNotSame(o4, node9.getUserObject());

        final DefaultMutableTreeNode node10 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node10);
        assertNotSame(o5, node10.getUserObject());

        assertTrue(PROJECT_TMP_FILE.exists());
     }

   /**
     * Do NOT overwrite, entire project.
     */
     @Test
     public void testSynchronize14(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        final List<String> offset = new LinkedList<String>();

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        final DefaultMutableTreeNode node1 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node1);
        ProjectDiagram pd = (ProjectDiagram) node1.getUserObject();
        node1.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o1 = node1.getUserObject();

        final DefaultMutableTreeNode node2 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node2);
        pd = (ProjectDiagram) node2.getUserObject();
        node2.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o2 = node2.getUserObject();

        final DefaultMutableTreeNode node3 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node3);
        pd = (ProjectDiagram) node3.getUserObject();
        node3.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o3 = node3.getUserObject();

        final DefaultMutableTreeNode node4 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node4);
        pd = (ProjectDiagram) node4.getUserObject();
        node4.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o4 = node4.getUserObject();

        final DefaultMutableTreeNode node5 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node5);
        pd = (ProjectDiagram) node5.getUserObject();
        node5.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o5 = node5.getUserObject();

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, false, true, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        final DefaultMutableTreeNode node6 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node6);
        assertSame(o1, node6.getUserObject());

        final DefaultMutableTreeNode node7 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node7);
        assertSame(o2, node7.getUserObject());

        final DefaultMutableTreeNode node8 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node8);
        assertSame(o3, node8.getUserObject());

        final DefaultMutableTreeNode node9 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node9);
        assertSame(o4, node9.getUserObject());

        final DefaultMutableTreeNode node10 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node10);
        assertSame(o5, node10.getUserObject());

        assertTrue(PROJECT_TMP_FILE.exists());
     }

   /**
     * Overwrite, only one project.
     */
     @Test
     public void testSynchronize15(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, null, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        final DefaultMutableTreeNode node1 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node1);
        ProjectDiagram pd = (ProjectDiagram) node1.getUserObject();
        node1.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o1 = node1.getUserObject();

        final DefaultMutableTreeNode node2 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node2);
        final Object o2 = node2.getUserObject();

        final DefaultMutableTreeNode node3 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node3);
        final Object o3 = node3.getUserObject();

        final DefaultMutableTreeNode node4 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node4);
        final Object o4 = node4.getUserObject();

        final DefaultMutableTreeNode node5 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node5);
        final Object o5 = node5.getUserObject();

        final List<String> offset = new LinkedList<String>();
        offset.add(DIAG_NAME_1 + "." + epcNotation.getLocalIOController().getNotationFileExtension());

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, false, true, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        final DefaultMutableTreeNode node6 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node6);
        assertNotSame(o1, node6.getUserObject());

        final DefaultMutableTreeNode node7 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node7);
        assertSame(o2, node7.getUserObject());

        final DefaultMutableTreeNode node8 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node8);
        assertSame(o3, node8.getUserObject());

        final DefaultMutableTreeNode node9 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node9);
        assertSame(o4, node9.getUserObject());

        final DefaultMutableTreeNode node10 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node10);
        assertSame(o5, node10.getUserObject());

        assertTrue(PROJECT_TMP_FILE.exists());
     }

   /**
     * Overwrite, one subfolder.
     */
     @Test
     public void testSynchronize16(){
        try {
            copyDirectory(PROJECT_FILE, PROJECT_TMP_FILE);
        } catch (IOException e) {
            fail();
        }

        assertTrue(projectService.getProjects().size() == 0);

        final LoadProjectResult result = ModelerSession.getProjectControlService().loadProject(PROJECT_TMP_FILE_FS_PATH);

        assertNotNull(result.getProjectRoot());

        ModelerSession.getProjectControlService().addProject(result.getProjectRoot(), true);

        assertTrue(projectService.getProjects().size() == 1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, null, true, false, false, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);

        final DefaultMutableTreeNode node1 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node1);
        final Object o1 = node1.getUserObject();

        final DefaultMutableTreeNode node2 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node2);
        final Object o2 = node2.getUserObject();

        final DefaultMutableTreeNode node3 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node3);
        ProjectDiagram pd = (ProjectDiagram) node3.getUserObject();
        node3.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o3 = node3.getUserObject();

        final DefaultMutableTreeNode node4 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node4);
        pd = (ProjectDiagram) node4.getUserObject();
        node4.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o4 = node4.getUserObject();

        final DefaultMutableTreeNode node5 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node5);
        pd = (ProjectDiagram) node5.getUserObject();
        node5.setUserObject(new ProjectDiagram(pd.getDisplayName(), pd.getNotationIdentifier(), pd.getUuid(), pd.getDiagramModel()));
        final Object o5 = node5.getUserObject();

        final List<String> offset = new LinkedList<String>();
        offset.add(SUB_NAME_1);

         runTestOnEDT(new Runnable(){
             public void run() {
                 assertTrue(ModelerSession.getProjectControlService().synchronize(PROJECT_TMP_FILE_FS_PATH, offset, true, true, true, false));
             }
         });

        assertTrue(projectService.getProjects().size() == 1);
        final DefaultMutableTreeNode node6 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_1, null, false);
        assertNotNull(node6);
        assertSame(o1, node6.getUserObject());

        final DefaultMutableTreeNode node7 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_2, null, false);
        assertNotNull(node7);
        assertSame(o2, node7.getUserObject());

        final DefaultMutableTreeNode node8 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_3, SUB_NAME_1, false);
        assertNotNull(node8);
        assertNotSame(o3, node8.getUserObject());

        final DefaultMutableTreeNode node9 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_4, SUB_NAME_1, false);
        assertNotNull(node9);
        assertNotSame(o4, node9.getUserObject());

        final DefaultMutableTreeNode node10 = getLoadedNode(ModelerSession.getProjectService().getSelectedProjectPath(), DIAG_NAME_5, SUB_NAME_1+"/"+SUB_NAME_2, false);
        assertNotNull(node10);
        assertNotSame(o5, node10.getUserObject());

        assertTrue(PROJECT_TMP_FILE.exists());
     }


    /**
     * Copies file/directory.
     *
     * @param sourceLocation the source file/directory.
     * @param targetLocation the source file/directory.
     * @throws IOException when anything wrong happens.
     */
    public void copyDirectory(final File sourceLocation , final File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            final String[] children = sourceLocation.list();
            for(String aChildren : children) {
                copyDirectory(new File(sourceLocation, aChildren),
                        new File(targetLocation, aChildren));
            }
        } else {
            final InputStream in = new FileInputStream(sourceLocation);
            final OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from in-stream to out-stream
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * Tests whether there is a project item loaded.
     *
     * @param treePath is the tree path where to start the search
     * @param name is the name of required project item
     * @param offsetString is the subfolder offset
     * @param setObject if true, set the node user's object to an object
     * @return a tree node if there is such a item, null otherwise
     */
    private DefaultMutableTreeNode getLoadedNode(TreePath treePath, final String name, final String offsetString, final boolean setObject){
        if(offsetString != null){
            final String[] offset = offsetString.split("/");
            int offsetPointer = 0;

            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            for(int i = 0; i < parentNode.getChildCount(); i++){
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);

                if(node.getUserObject() instanceof ProjectSubFolder){
                    final ProjectSubFolder subFolder = (ProjectSubFolder) node.getUserObject();

                    if(subFolder.getDisplayName().equals(offset[offsetPointer])){
                        offsetPointer++;

                        treePath = treePath.pathByAddingChild(node);

                        if(offsetPointer >= offset.length){
                            break;
                        }
                        
                        parentNode = node;
                        i = -1;
                    }
                }
            }

            if(offsetPointer != offset.length){
                return null;
            }
        }

        final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        for(int i = 0; i < parentNode.getChildCount(); i++){
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);

            if(node.getUserObject() instanceof ProjectDiagram){
                final ProjectDiagram projectDiagram = (ProjectDiagram) node.getUserObject();

                if(projectDiagram.getDisplayName().equals(name)){
                    if(setObject){
                        node.setUserObject(new Object());
                    }

                    return node;
                }
            }
        }

        return null;
    }

}
