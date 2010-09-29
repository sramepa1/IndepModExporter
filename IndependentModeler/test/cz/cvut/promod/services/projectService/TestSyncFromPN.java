package cz.cvut.promod.services.projectService;

import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.results.AddProjectItemResult;
import cz.cvut.promod.services.projectService.results.AddProjectItemStatus;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.extensionService.ExtensionServiceImpl;
import cz.cvut.promod.services.userService.UserServiceImpl;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.IndependentModelerMain;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static org.junit.Assert.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:01:37, 9.2.2010
 */
public class TestSyncFromPN {

    private static final String PROJECT_NAME_1 = "Project 1";
    private static final String PROJECT_NAME_3 = "Project 3";

    private static final String DIAG_NAME_1 = "Diag1";
    private static final String DIAG_NAME_2 = "Diag2";
    private static final String DIAG_NAME_3 = "Diag3";
    private static final String DIAG_NAME_4 = "Diag4";
    private static final String DIAG_NAME_5 = "Diag5";

    private static final String SUB_NAME_1 = "Sub1";
    private static final String SUB_NAME_2 = "Sub2";

    private static final String SEP = System.getProperty("file.separator");

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private final static String PROJECT_1_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"sync"+SEP+"project1";

    private final static File PROJECT_1_SUBFOLDER = new File(PROJECT_1_PATH);

    private File pd1_file;
    private File pd2_file;
    private File pd3_file;
    private File pd4_file;
    private File pd5_file;

    private File sub1_file;
    private File sub2_file;


    private final static File PROJECT_FILE_1_FILE = new File(PROJECT_1_PATH + "/" + PROJECT_NAME_1 + ProjectService.PROJECT_FILE_EXTENSION);

    private ProjectControlService projectService;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

    private TreePath projectRoot1TreePath;
    private TreePath projectRoot2TreePath;

    private TreePath sub1TreePath;

    private TreePath projectDiagram5TreePath;

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

        // build a testing project navigation tree structure

        ProjectRoot projectRoot1 = new ProjectRoot(PROJECT_NAME_1, PROJECT_1_PATH);
        ProjectDiagram projectDiagram1 = new ProjectDiagram(
                DIAG_NAME_1, epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        ProjectDiagram projectDiagram2 = new ProjectDiagram(
                DIAG_NAME_2, epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram3 = new ProjectDiagram(
                DIAG_NAME_3, hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());
        ProjectDiagram projectDiagram4 = new ProjectDiagram(
                DIAG_NAME_4, epcNotation.getIdentifier(), UUID.randomUUID(),
                epcNotation.getDiagramModelFactory().createEmptyDiagramModel());
        final ProjectDiagram projectDiagram5 = new ProjectDiagram(
                DIAG_NAME_5, hierarchyNotation.getIdentifier(), UUID.randomUUID(),
                hierarchyNotation.getDiagramModelFactory().createEmptyDiagramModel());

        AddProjectItemResult result = projectService.addProject(projectRoot1, true);
        projectRoot1TreePath = result.getTreePath();

        projectService.addDiagram(projectDiagram1, false);
        pd1_file = new File(PROJECT_1_SUBFOLDER, DIAG_NAME_1 + "." + epcNotation.getLocalIOController().getNotationFileExtension());

        projectService.addDiagram(projectDiagram2, false);
        pd2_file = new File(PROJECT_1_SUBFOLDER, DIAG_NAME_2 + "." + epcNotation.getLocalIOController().getNotationFileExtension());

        final AddProjectItemResult sub1Result = projectService.addSubFolder(SUB_NAME_1, true);
        sub1TreePath = sub1Result.getTreePath();
        sub1_file = new File(PROJECT_1_SUBFOLDER, SUB_NAME_1);

        projectService.addDiagram(projectDiagram3,  false);
        pd3_file = new File(sub1_file, DIAG_NAME_3 + "." + hierarchyNotation.getLocalIOController().getNotationFileExtension());

        projectService.addDiagram(projectDiagram4,  false);
        pd4_file = new File(sub1_file, DIAG_NAME_4 + "." + epcNotation.getLocalIOController().getNotationFileExtension());

        projectService.addSubFolder(SUB_NAME_2, true);
        sub2_file = new File(sub1_file, SUB_NAME_2);

        final AddProjectItemResult addProjectItemResult5 = projectService.addDiagram(projectDiagram5, false);
        projectDiagram5TreePath = addProjectItemResult5.getTreePath();

        pd5_file = new File(sub2_file, DIAG_NAME_5 + "." + hierarchyNotation.getLocalIOController().getNotationFileExtension());


        ProjectRoot projectRoot2 = new ProjectRoot(PROJECT_NAME_3, null);

        result = projectService.addProject(projectRoot2, true);
        projectRoot2TreePath = result.getTreePath();

        assertEquals(result.getStatus(), AddProjectItemStatus.SUCCESS);
        assertTrue(projectService.getProjects().size() == 2);
    }

    @After
    public void tearDown(){
        if(PROJECT_1_SUBFOLDER.exists()){
            ProjectServiceUtils.removeDirectory(PROJECT_1_SUBFOLDER);
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
     * Synchronization from PN.
     *
     *  Running out of Event Dispatcher Thread.
     */
    @Test
    public void testSynchronize0(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        assertFalse(projectService.synchronize(projectRoot1TreePath, true, true, true, true));

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

    /**
     * Synchronization from PN.
     *
     * Nullary tree path.
     */
    @Test
    public void testSynchronize1(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(null, true, true, true, true));
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

        /**
     * Synchronization from PN.
     *
     * All flags are set to false - nothing to do.
     */
    @Test
    public void testSynchronize2(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, false, false, false));
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

    /**
     * Synchronization from PN.
     *
     * Not existing/invalid project tree path
     */
    @Test
    public void testSynchronize3(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(
                            new TreePath(new Object[]{new Object(), new Object()}),
                            true, true, true, true)
                    );
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

    /**
     * Synchronization from PN.
     *
     * Synchronization of project having nullary location.
     */
    @Test
    public void testSynchronize4(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(
                            projectRoot2TreePath,
                        true, true, true, true));
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

    /**
     * Synchronization from PN.
     *
     * Correct sync but not right to write - add items.
     */
    @Test
    public void testSynchronize5(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(projectRoot1TreePath, false, true, true, true));
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  OK.
     */
    @Test
    public void testSynchronize6(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Serialization of only one diagram, but the project file is not saved -> ERROR.
     */
    @Test
    public void testSynchronize7(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(projectDiagram5TreePath, true, false, false, false));
                }
            }
        });

        assertFalse(PROJECT_1_SUBFOLDER.exists());
        assertFalse(PROJECT_FILE_1_FILE.exists());
        assertFalse(pd1_file.exists());
        assertFalse(pd2_file.exists());
        assertFalse(pd3_file.exists());
        assertFalse(pd4_file.exists());
        assertFalse(pd5_file.exists());

        assertFalse(sub1_file.exists());
        assertFalse(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Serialization of only one diagram and the project file is saved -> OK.
     */
    @Test
    public void testSynchronize8(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        ModelerSession.getProjectControlService().saveProject(projectRoot1TreePath);
        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectDiagram5TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertFalse(pd1_file.exists());
        assertFalse(pd2_file.exists());
        assertFalse(pd3_file.exists());
        assertFalse(pd4_file.exists());
        assertTrue(pd5_file.exists());

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Serialization of a subfolder and the project file is saved -> OK.
     */
    @Test
    public void testSynchronize9(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        ModelerSession.getProjectControlService().saveProject(projectRoot1TreePath);
        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(sub1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertFalse(pd1_file.exists());
        assertFalse(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Delete diagram.
     */
    @Test
    public void testSynchronize10(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());               
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ModelerSession.getProjectControlService().removeProjectItem(projectDiagram5TreePath);

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, false, true, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertFalse(pd5_file.exists()); // !
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Delete subfolder.
     */
    @Test
    public void testSynchronize11(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ModelerSession.getProjectControlService().removeProjectItem(sub1TreePath);

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, false, true, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertFalse(pd3_file.exists());
        assertFalse(pd4_file.exists());
        assertFalse(pd5_file.exists()); // !
        assertFalse(sub1_file.exists());
        assertFalse(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Delete project -> not possible because of invalid project tree path after removal.
     */
    @Test
    public void testSynchronize12(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ModelerSession.getProjectControlService().removeProjectItem(projectRoot1TreePath);

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertFalse(projectService.synchronize(projectRoot1TreePath, false, false, true, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(pd1_file.exists());
        assertTrue(pd2_file.exists());
        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test overwrite - all.
     */
    @Test
    public void testSynchronize13(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        final Long pd3Date =  pd3_file.lastModified();

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate == PROJECT_FILE_1_FILE.lastModified()); // no change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() == pd1Date);  // no change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() == pd2Date);  // no change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() == pd3Date);  // no change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() == pd4Date);  // no change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() == pd5Date);  // no change       

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, true, false, false)); // ! overwrite
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate < PROJECT_FILE_1_FILE.lastModified()); // change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() > pd1Date);  // change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() > pd2Date);  // change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() > pd3Date);  // change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() > pd4Date);  // change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() > pd5Date);  // change       

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test overwrite - only one diagram.
     */
    @Test
    public void testSynchronize14(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        final Long pd3Date =  pd3_file.lastModified();

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectDiagram5TreePath, false, true, false, false)); // ! overwrite
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate == PROJECT_FILE_1_FILE.lastModified()); // change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() == pd1Date);  // change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() == pd2Date);  // change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() == pd3Date);  // change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() == pd4Date);  // change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() > pd5Date);  // change

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test overwrite - one subfolder.
     */
    @Test
    public void testSynchronize15(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        final Long pd3Date =  pd3_file.lastModified();

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(sub1TreePath, false, true, false, false)); // ! overwrite
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate == PROJECT_FILE_1_FILE.lastModified()); // change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() == pd1Date);  // change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() == pd2Date);  // change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() > pd3Date);  // change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() > pd4Date);  // change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() > pd5Date);  // change

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test add and  not overwrite overwrite - one diagram.
     */
    @Test
    public void testSynchronize16(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        final Long pd3Date =  pd3_file.lastModified();

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ProjectServiceUtils.deleteFSItem(sub1TreePath, DIAG_NAME_3 + "." + hierarchyNotation.getLocalIOController().getNotationFileExtension());

        assertFalse(pd3_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

   assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate == PROJECT_FILE_1_FILE.lastModified()); // no change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() == pd1Date);  // no change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() == pd2Date);  // no change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() > pd3Date);  // change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() == pd4Date);  // no change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() == pd5Date);  // no change

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test overwrite and  not add overwrite - one diagram.
     */
    @Test
    public void testSynchronize17(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ProjectServiceUtils.deleteFSItem(sub1TreePath, DIAG_NAME_3 + "." + hierarchyNotation.getLocalIOController().getNotationFileExtension());

        assertFalse(pd3_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, true, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate < PROJECT_FILE_1_FILE.lastModified()); // no change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() > pd1Date);  // no change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() > pd2Date);  // no change

        assertFalse(pd3_file.exists());   // !!

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() > pd4Date);  // no change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() > pd5Date);  // no change

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

   /**
     * Synchronization from PN.
     *
     *  Test add and  not overwrite overwrite - one subfolder.
     */
    @Test
    public void testSynchronize18(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        final Long pd3Date =  pd3_file.lastModified();

        assertTrue(pd4_file.exists());
        final Long pd4Date =  pd4_file.lastModified();

        assertTrue(pd5_file.exists());
        final Long pd5Date =  pd5_file.lastModified();

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ProjectServiceUtils.removeDirectory(sub1_file);

        assertFalse(sub1_file.exists());
        assertFalse(pd3_file.exists());   // !!
        assertFalse(pd4_file.exists());
        assertFalse(pd5_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate == PROJECT_FILE_1_FILE.lastModified()); // no change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() == pd1Date);  // no change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() == pd2Date);  // no change

        assertTrue(pd3_file.exists());
        assertTrue(pd3_file.lastModified() > pd3Date);  // change

        assertTrue(pd4_file.exists());
        assertTrue(pd4_file.lastModified() > pd4Date);  // no change

        assertTrue(pd5_file.exists());
        assertTrue(pd5_file.lastModified() > pd5Date);  // no change

        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());
    }

    /**
     * Synchronization from PN.
     *
     *  Test overwrite and  not add overwrite - one subfolder.
     */
    @Test
    public void testSynchronize19(){
        assertFalse(PROJECT_1_SUBFOLDER.exists());

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, true, false, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        final Long projectFileDate =  PROJECT_FILE_1_FILE.lastModified();

        assertTrue(pd1_file.exists());
        final Long pd1Date =  pd1_file.lastModified();

        assertTrue(pd2_file.exists());
        final Long pd2Date =  pd2_file.lastModified();

        assertTrue(pd3_file.exists());
        assertTrue(pd4_file.exists());
        assertTrue(pd5_file.exists());
        assertTrue(sub1_file.exists());
        assertTrue(sub2_file.exists());

        ProjectServiceUtils.removeDirectory(sub1_file);
        assertFalse(sub1_file.exists());
        assertFalse(sub2_file.exists());
        assertFalse(pd3_file.exists());
        assertFalse(pd4_file.exists());
        assertFalse(pd5_file.exists());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }        

        runTestOnEDT(new Runnable(){
            public void run() {
                if(SwingUtilities.isEventDispatchThread()){
                    assertTrue(projectService.synchronize(projectRoot1TreePath, false, true, false, false));
                }
            }
        });

        assertTrue(PROJECT_1_SUBFOLDER.exists());
        assertTrue(PROJECT_FILE_1_FILE.exists());
        assertTrue(projectFileDate < PROJECT_FILE_1_FILE.lastModified()); // no change

        assertTrue(pd1_file.exists());
        assertTrue(pd1_file.lastModified() > pd1Date);  // no change

        assertTrue(pd2_file.exists());
        assertTrue(pd2_file.lastModified() > pd2Date);  // no change

        assertFalse(pd3_file.exists());   // !!
        assertFalse(pd4_file.exists());
        assertFalse(pd5_file.exists());
        assertFalse(sub1_file.exists());
        assertFalse(sub2_file.exists());
    }

}
