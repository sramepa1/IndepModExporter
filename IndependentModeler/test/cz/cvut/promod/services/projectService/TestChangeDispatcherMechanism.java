package cz.cvut.promod.services.projectService;

import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagramChange;
import cz.cvut.promod.services.projectService.treeProjectNode.listener.ProjectDiagramListener;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import javax.swing.tree.TreePath;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:47:38, 18.4.2010
 */

/**
 * Test the Change Dispatcher Mechanisms of the ProjectControlService.
 */
public class TestChangeDispatcherMechanism {

    private static final String SUB_NAME_1 = "Sub1";
    private static final String SUB_NAME_2 = "Sub2";


    private static final String SEP = System.getProperty("file.separator");

    private final static String PROJECT_1_PATH =
            "."+SEP+"test"+SEP+"cz"+SEP+"cvut"+SEP+"promod"+SEP+"services"+SEP+"projectService"+SEP+"files"+SEP+"project1";

    private ProjectControlService projectService;


    private ProjectDiagram projectDiagram1;
    private ProjectDiagram projectDiagram2;
    private ProjectDiagram projectDiagram3;
    private ProjectDiagram projectDiagram4;
    private ProjectDiagram projectDiagram5;

    private TreePath projectTreePath;
    private TreePath projectDiagram1TreePath;
    private TreePath sub1TreePath;

    private ProjectControlService service;


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

        final ProcessHierarchyNotation notation = new ProcessHierarchyNotation();

        final NotationSpecificPlugins hierarchyNotationPlugins = new NotationSpecificPlugins(notation, Collections.unmodifiableList(new LinkedList<Module>()));
        final List<NotationSpecificPlugins> plugins = new LinkedList<NotationSpecificPlugins>();
        plugins.add(hierarchyNotationPlugins);

        ModelerSession.setNotationService(new NotationServiceImpl(
                plugins,
                new LinkedList<PluginLoadErrors>()
        ));

        ModelerSession.setProjectControlService(projectService);

        // build a testing project navigation tree structure

        try{
            final ProjectRoot projectRoot1 = new ProjectRoot("Project", PROJECT_1_PATH);
            projectDiagram1 = new ProjectDiagram(
                    "Diag1", notation.getIdentifier(), UUID.randomUUID(),
                    notation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram2 = new ProjectDiagram(
                    "Diag2", notation.getIdentifier(), UUID.randomUUID(),
                    notation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram3 = new ProjectDiagram(
                    "Diag3", notation.getIdentifier(), UUID.randomUUID(),
                    notation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram4 = new ProjectDiagram(
                    "Diag4", notation.getIdentifier(), UUID.randomUUID(),
                    notation.getDiagramModelFactory().createEmptyDiagramModel());
            projectDiagram5 = new ProjectDiagram(
                    "Diag5", notation.getIdentifier(), UUID.randomUUID(),
                    notation.getDiagramModelFactory().createEmptyDiagramModel());

            projectService.addProject(projectRoot1, true);

            assertEquals(projectRoot1, projectService.getSelectedProject());

            projectTreePath = projectService.getSelectedProjectPath(); 

            projectService.addDiagram(projectDiagram1, true);

            assertEquals(projectDiagram1, projectService.getSelectedDiagram());

            projectDiagram1TreePath = projectService.getSelectedDiagramPath();           

            projectService.addDiagram(projectDiagram2, false);
            projectService.addSubFolder(SUB_NAME_1, true);

            sub1TreePath = projectService.getSelectedTreePath();
            assertNotNull(sub1TreePath);

            projectService.addDiagram(projectDiagram3,  false);
            projectService.addDiagram(projectDiagram4,  false);
            projectService.addSubFolder(SUB_NAME_2, true);
            projectService.addDiagram(projectDiagram5, false);
        } catch (Exception e){
            fail();
        }

        service = ModelerSession.getProjectControlService();

        assertNotNull(service);
    }

    @After
    public void tearDown(){
        try { // null services
            final Method nullServicesMethod = ModelerSession.class.getDeclaredMethod(ModelerSession.NULL_METHOD_NAME);
            nullServicesMethod.setAccessible(true);
            nullServicesMethod.invoke(null);
            nullServicesMethod.setAccessible(false);
        } catch (Exception e) {
            fail();
        }
    }    

    /**
     * No diagram specified.
     */
    @Test
    public void testRegisterDiagramListener1(){
        service.registerDiagramListener(null, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                fail(); // shouldn't be invoked
            }
        });

        projectDiagram1.publishChange(null, null);
        projectDiagram2.publishChange(null, null);
        projectDiagram3.publishChange(null, null);
        projectDiagram4.publishChange(null, null);
        projectDiagram5.publishChange(null, null);
    }

    /**
     * Diagram1 is registered and dispatches nullary change.
     */
    @Test
    public void testRegisterDiagramListener2(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                fail(); // shouldn't be invoked
            }
        });

        projectDiagram1.publishChange(null, null);
        projectDiagram2.publishChange(null, null);
        projectDiagram3.publishChange(null, null);
        projectDiagram4.publishChange(null, null);
        projectDiagram5.publishChange(null, null);
    }

    /**
     * Diagram1 is registered and dispatches nullary project diagram, change type.
     */
    @Test
    public void testRegisterDiagramListener3(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                fail(); // shouldn't be invoked
            }
        });

        projectDiagram1.publishChange(null, null);
        projectDiagram1.publishChange(null, new Object());
        projectDiagram2.publishChange(null, null);
        projectDiagram3.publishChange(null, null);
        projectDiagram4.publishChange(null, null);
        projectDiagram5.publishChange(null, null);
    }

    /**
     * Diagram1 is registered and dispatches DIAGRAM MODEL CHANGE.
     */
    @Test
    public void testRegisterDiagramListener4(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {

                assertEquals(payload.getProjectDiagram(), projectDiagram1);
                
                if(!(payload.getChangeType().equals(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL))){
                    fail(); // shouldn't be invoked
                }

            }
        });

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Diagram1 is registered and dispatches DIAGRAM MODEL change.
     * Diagram2 is registered and dispatches DISPLAY NAME change.
     */
    @Test
    public void testRegisterDiagramListener5(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                assertEquals(payload.getProjectDiagram(), projectDiagram1);
            }
        });

        service.registerDiagramListener(projectDiagram2, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                assertEquals(payload.getProjectDiagram(), projectDiagram2);
            }
        });

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Diagram1 is registered and dispatches DIAGRAM MODEL CHANGE + two listeners
     */
    @Test
    public void testRegisterDiagramListener6(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                assertEquals(payload.getProjectDiagram(), projectDiagram1);
            }
        });

        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                assertEquals(payload.getProjectDiagram(), projectDiagram1);
            }
        });

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.UNDEFINED, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION, new Object());
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());        

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Diagram1 and Diagram 2 have the same listener.
     * Firstly dispatches the projectDiagram1 2x and then projectDiagram2  2x
     */
    @Test
    public void testRegisterDiagramListener7(){
        final ProjectDiagramListener listener = new ProjectDiagramListener(){
            int i = 0;

            public void changePerformed(ProjectDiagramChange change) {
                if(change.getProjectDiagram() != projectDiagram1 && change.getProjectDiagram() != projectDiagram2){
                    fail();
                }

                 if(i++ < 2){
                     if(change.getProjectDiagram() != projectDiagram1){
                         fail();
                     }
                 }
                else if(i++ > 2){
                     if(change.getProjectDiagram() != projectDiagram2){
                         fail();
                     }
                 }
            }
        };

        service.registerDiagramListener(projectDiagram1, listener);

        service.registerDiagramListener(projectDiagram2, listener);

        /* 1. */projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        /* 2. */projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        /* 1. */projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        /* 2. */projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Diagram1 is registered and removed.
     */
    @Test
    public void testRegisterDiagramListener8(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram1 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        projectService.removeProjectItem(projectDiagram1TreePath);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null); // ok

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object()); // won't be delivered

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Diagram3,4,5 is registered and the containing subfolder is removed.
     */
    @Test
    public void testRegisterDiagramListener9(){
        service.registerDiagramListener(projectDiagram3, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram3 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram4, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram4 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram5, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram5 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        projectService.removeProjectItem(sub1TreePath);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null); // ok
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());// won't be delivered

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null); // ok
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());// won't be delivered

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);// ok
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());// won't be delivered
    }

    /**
     * Diagram3,4,5 is registered and the containing project is removed.
     */
    @Test
    public void testRegisterDiagramListener10(){
        service.registerDiagramListener(projectDiagram1, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram1 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram2, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram2 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram3, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram3 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram4, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram4 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        service.registerDiagramListener(projectDiagram5, new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram5 && payload.getChangeType().equals(ProjectDiagramChange.ChangeType.REMOVED_FROM_NAVIGATION)){
                    if(payload.getChangeValue() != null){
                        fail();
                    }
                }
            }
        });

        projectService.removeProjectItem(projectTreePath);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null); // ok
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());// won't be delivered

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);// ok
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());// won't be delivered

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null); // ok
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());// won't be delivered

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null); // ok
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());// won't be delivered

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);// ok
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());// won't be delivered
    }

    /**
     * Un-registering listeners from the project diagram, but nullary project diagram or listener
     */
    @Test
    public void testUnregisterDiagramListener1(){
        final ProjectDiagramListener listener = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram1) {
                    fail(); // shouldn't be invoked
                }
            }
        };

        service.registerDiagramListener(null, listener);  // have no effect
        service.registerDiagramListener(null, null);  // have no effect

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
    }


    /**
     * Un-registering listeners from the project diagram.
     */
    @Test
    public void testUnregisterDiagramListener2(){
        final ProjectDiagramListener listener = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram3){ // diagram3 is not un-registered
                    fail(); // shouldn't be invoked
                }
            }
        };

        service.registerDiagramListener(projectDiagram1, listener);
        service.registerDiagramListener(projectDiagram2, listener);
        service.registerDiagramListener(projectDiagram3, listener);

        projectService.unRegisterDiagramListener(projectDiagram1, listener);
        projectService.unRegisterDiagramListener(projectDiagram2, listener);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
    }

   /**
     * Un-registering listener, but nullary listener.
     */
    @Test
    public void testUnregisterDiagramListener3(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                final ProjectDiagram diagram = payload.getProjectDiagram();

                if(diagram != projectDiagram1 && diagram != projectDiagram2 && diagram != projectDiagram3){
                    fail(); // shouldn't be invoked
                }
            }
        };

        service.registerDiagramListener(projectDiagram1, listener1);
        service.registerDiagramListener(projectDiagram2, listener1);
        service.registerDiagramListener(projectDiagram3, listener1);

        projectService.unRegisterListener(null);  // has no effect

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
    }

    /**
     * Un-registering listener.
     */
    @Test
    public void testUnregisterDiagramListener4(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                fail(); // shouldn't be invoked
            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange payload) {
                if(payload.getProjectDiagram() != projectDiagram3){
                    fail(); // shouldn't be invoked
                }
            }
        };

        service.registerDiagramListener(projectDiagram1, listener1);
        service.registerDiagramListener(projectDiagram2, listener1);
        service.registerDiagramListener(projectDiagram3, listener1);

        service.registerDiagramListener(projectDiagram3, listener2);

        projectService.unRegisterListener(listener1);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
    }

    /**
     * Listener switch - nullary listener 1.
     */
    @Test
    public void testSwitchChangeListener4(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {

            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                fail();
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);
        projectService.registerDiagramListener(projectDiagram3, listener1);
        projectService.registerDiagramListener(projectDiagram4, listener1);
        projectService.registerDiagramListener(projectDiagram5, listener1);

        service.switchChangeListener(null, listener2);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Listener switch - nullary listener 1 and 2.
     */
    @Test
    public void testSwitchChangeListener3(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {

            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                fail();
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);
        projectService.registerDiagramListener(projectDiagram3, listener1);
        projectService.registerDiagramListener(projectDiagram4, listener1);
        projectService.registerDiagramListener(projectDiagram5, listener1);

        service.switchChangeListener(null, null);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * Listener switch - nullary listener 1.
     */
    @Test
    public void testSwitchChangeListener2(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {

            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                fail();
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);
        projectService.registerDiagramListener(projectDiagram3, listener1);
        projectService.registerDiagramListener(projectDiagram4, listener1);
        projectService.registerDiagramListener(projectDiagram5, listener1);

        service.switchChangeListener(null, listener2);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }


    /**
     * Listener switch - ok.
     */
    @Test
    public void testSwitchChangeListener1(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                    fail();
            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {

            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);
        projectService.registerDiagramListener(projectDiagram3, listener1);
        projectService.registerDiagramListener(projectDiagram4, listener1);
        projectService.registerDiagramListener(projectDiagram5, listener1);

        service.switchChangeListener(listener1, listener2);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * On listener is unregistered from all project diagrams
     */
    @Test
    public void testUnregisterDiagramListener5(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                    fail();
            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram1 && diagram != projectDiagram2){
                    fail();
                }
            }
        };

        final ProjectDiagramListener listener3 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram3 && diagram != projectDiagram4 && diagram != projectDiagram5){
                    fail();
                }
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);
        projectService.registerDiagramListener(projectDiagram3, listener1);
        projectService.registerDiagramListener(projectDiagram4, listener1);
        projectService.registerDiagramListener(projectDiagram5, listener1);

        projectService.registerDiagramListener(projectDiagram1, listener2);
        projectService.registerDiagramListener(projectDiagram2, listener2);
        projectService.registerDiagramListener(projectDiagram3, listener3);
        projectService.registerDiagramListener(projectDiagram4, listener3);
        projectService.registerDiagramListener(projectDiagram5, listener3);

        projectService.unRegisterListener(listener1);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null); // ok
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());// won't be delivered

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);// ok
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());// won't be delivered

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null); // ok
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());// won't be delivered

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null); // ok
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());// won't be delivered

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);// ok
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());// won't be delivered
    }

    /**
     * switching project diagrams 
     */
    @Test
    public void testSwitchChangePublisher1(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram3 && diagram != projectDiagram2){
                    fail();
                }
            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram3 && diagram != projectDiagram2){
                    fail();
                }
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);

        projectService.registerDiagramListener(projectDiagram1, listener2);
        projectService.registerDiagramListener(projectDiagram2, listener2);


        projectService.switchChangePublisher(projectDiagram1, projectDiagram3);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }

    /**
     * switching project diagrams
     */
    @Test
    public void testSwitchChangePublisher2(){
        final ProjectDiagramListener listener1 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram1 && diagram != projectDiagram2){
                    fail();
                }
            }
        };

        final ProjectDiagramListener listener2 = new ProjectDiagramListener(){
            public void changePerformed(ProjectDiagramChange change) {
                final ProjectDiagram diagram = change.getProjectDiagram();
                if(diagram != projectDiagram1 && diagram != projectDiagram2){
                    fail();
                }
            }
        };

        projectService.registerDiagramListener(projectDiagram1, listener1);
        projectService.registerDiagramListener(projectDiagram2, listener1);

        projectService.registerDiagramListener(projectDiagram1, listener2);
        projectService.registerDiagramListener(projectDiagram2, listener2);

        projectService.switchChangePublisher(null, projectDiagram3);
        projectService.switchChangePublisher(projectDiagram1, null);
        projectService.switchChangePublisher(null, null);

        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, null);
        projectDiagram1.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());

        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram2.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, new Object());

        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.PROJECT_DIAGRAM_REPLACED, null);
        projectDiagram3.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, new Object());

        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram4.publishChange(ProjectDiagramChange.ChangeType.CHANGE_FLAG, new Object());

        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DISPLAY_NAME, null);
        projectDiagram5.publishChange(ProjectDiagramChange.ChangeType.DIAGRAM_MODEL, new Object());
    }


}

