package cz.cvut.promod.services.notationService;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.epc.modelFactory.diagramModel.EPCDiagramModel;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import cz.cvut.promod.hierarchyNotation.modelFactory.diagramModel.ProcessHierarchyDiagramModel;
import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.ph.treeLayout.ProcessHierarchyTreeLayout;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;

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
 * Date: 22:26:59, 10.2.2010
 */
public class TestNotationServiceImpl {

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");

    private NotationService notationService;
    private NotationService notationService_empty;
    private NotationService notationService_reverse;

    private PluginLoadErrors error1;
    private PluginLoadErrors error2;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;

    private ProcessHierarchyTreeLayout processHierarchyTreeLayout;

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

        try {
            epcNotation = new EPCNotation(EPC_PROPERTY_FILE);
            hierarchyNotation = new ProcessHierarchyNotation();
        } catch (InstantiationException e) {
            fail();
        }

        List<PluginLoadErrors> errors = new LinkedList<PluginLoadErrors>();
        error1 = new PluginLoadErrors("error1");
        error2 = new PluginLoadErrors("error2");
        errors.add(error1);
        errors.add(error2);

        processHierarchyTreeLayout =
                new ProcessHierarchyTreeLayout(hierarchyNotation.getIdentifier());

        final NotationSpecificPlugins epcNotationPlugins = new NotationSpecificPlugins(epcNotation, new LinkedList<Module>());
        final List<Module> modules = new LinkedList<Module>();
        modules.add(processHierarchyTreeLayout);
        final NotationSpecificPlugins hierarchyNotationPlugins = new NotationSpecificPlugins(hierarchyNotation, Collections.unmodifiableList(modules));

        final List<NotationSpecificPlugins> plugins = new LinkedList<NotationSpecificPlugins>();
        plugins.add(epcNotationPlugins);
        plugins.add(hierarchyNotationPlugins);
        notationService = new NotationServiceImpl(plugins, errors);

        final List<NotationSpecificPlugins> plugins_reverse = new LinkedList<NotationSpecificPlugins>();
        plugins_reverse.add(hierarchyNotationPlugins);
        plugins_reverse.add(epcNotationPlugins);
        notationService_reverse = new NotationServiceImpl(plugins_reverse, new LinkedList<PluginLoadErrors>());

        notationService_empty = new NotationServiceImpl(
                new LinkedList<NotationSpecificPlugins>(),
                new LinkedList<PluginLoadErrors>()
        );
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
     * Simple testing of getNotations() method.
     */
    @Test
    public void testGetNotations(){
        List<Notation> notations = notationService.getNotations();
        assertTrue(notations.size() == 2);
        assertEquals(notations.get(0), epcNotation);
        assertEquals(notations.get(1), hierarchyNotation);

        // keeps order
        notations = notationService_reverse.getNotations();
        assertTrue(notations.size() == 2);
        assertEquals(notations.get(0), hierarchyNotation);
        assertEquals(notations.get(1), epcNotation);

        notations = notationService_empty.getNotations();
        assertTrue(notations.isEmpty());
    }


    /**
     * Simple testing of getNotationsIdentifiers() method.
     */
    @Test
    public void testGetNotationsIdentifiers(){
        List<String> identifiers = notationService.getNotationsIdentifiers();
        assertTrue(identifiers.size() == 2);
        assertEquals(identifiers.get(0), epcNotation.getIdentifier());
        assertEquals(identifiers.get(1), hierarchyNotation.getIdentifier());

        // keeps order
        identifiers = notationService_reverse.getNotationsIdentifiers();
        assertTrue(identifiers.size() == 2);
        assertEquals(identifiers.get(0), hierarchyNotation.getIdentifier());
        assertEquals(identifiers.get(1), epcNotation.getIdentifier());

        identifiers = notationService_empty.getNotationsIdentifiers();
        assertTrue(identifiers.isEmpty());
    }

    /**
     * Simple testing of getNotation(String) method.
     */
    @Test
    public void testGetNotation(){
        Notation notation = notationService.getNotation(null);
        assertNull(notation);

        // not existing notation identifier
        notation = notationService.getNotation(epcNotation.getIdentifier() + "_");
        assertNull(notation);

        assertEquals(notationService.getNotation(epcNotation.getIdentifier()), epcNotation);
        assertEquals(notationService.getNotation(hierarchyNotation.getIdentifier()), hierarchyNotation);

        // order does NOT matter
        assertEquals(notationService_reverse.getNotation(epcNotation.getIdentifier()), epcNotation);
        assertEquals(notationService_reverse.getNotation(hierarchyNotation.getIdentifier()), hierarchyNotation);

        assertNull(notationService_empty.getNotation(epcNotation.getIdentifier()));
        assertNull(notationService_empty.getNotation(hierarchyNotation.getIdentifier()));
    }

    /**
     * Simple testing of getNotationSpecificPlugins(String) method.
     */
    @Test
    public void testGetNotationSpecificPlugins(){
        NotationSpecificPlugins notationSpecificPlugins = notationService.getNotationSpecificPlugins(null);
        assertNull(notationSpecificPlugins);

        // not existing notation identifier
        notationSpecificPlugins = notationService.getNotationSpecificPlugins(epcNotation.getIdentifier() + "_");
        assertNull(notationSpecificPlugins);

        notationSpecificPlugins = notationService.getNotationSpecificPlugins(epcNotation.getIdentifier());
        assertTrue(notationSpecificPlugins != null);
        assertNotNull(notationSpecificPlugins);
        assertEquals(notationSpecificPlugins.getNotation(), epcNotation);
        assertTrue(notationSpecificPlugins.getModules().size() == 0);

        notationSpecificPlugins = notationService.getNotationSpecificPlugins(hierarchyNotation.getIdentifier());
        assertTrue(notationSpecificPlugins != null);
        assertNotNull(notationSpecificPlugins);
        assertEquals(notationSpecificPlugins.getNotation(), hierarchyNotation);
        assertTrue(notationSpecificPlugins.getModules().size() == 1);
        assertEquals(notationSpecificPlugins.getModules().get(0), processHierarchyTreeLayout);
        assertEquals(notationSpecificPlugins.getModule(processHierarchyTreeLayout.getIdentifier()), processHierarchyTreeLayout);

        // order does NOT matter
        notationSpecificPlugins = notationService_reverse.getNotationSpecificPlugins(epcNotation.getIdentifier());
        assertNotNull(notationSpecificPlugins);
        assertEquals(notationSpecificPlugins.getNotation(), epcNotation);
        assertTrue(notationSpecificPlugins.getModules().size() == 0);

        notationSpecificPlugins = notationService_reverse.getNotationSpecificPlugins(hierarchyNotation.getIdentifier());
        assertTrue(notationSpecificPlugins != null);
        assertNotNull(notationSpecificPlugins);
        assertEquals(notationSpecificPlugins.getNotation(), hierarchyNotation);
        assertTrue(notationSpecificPlugins.getModules().size() == 1);
        assertEquals(notationSpecificPlugins.getModules().get(0), processHierarchyTreeLayout);
        assertEquals(notationSpecificPlugins.getModule(processHierarchyTreeLayout.getIdentifier()), processHierarchyTreeLayout);


        // no notations
        notationSpecificPlugins = notationService_empty.getNotationSpecificPlugins(epcNotation.getIdentifier());
        assertNull(notationSpecificPlugins);

        notationSpecificPlugins = notationService_empty.getNotationSpecificPlugins(hierarchyNotation.getIdentifier());
        assertNull(notationSpecificPlugins);
    }

    /**
     * Simple testing of createEmptyNotationModel(String) method.
     */
    @Test
    public void testCreateEmptyNotationModel(){
        DiagramModel diagramModel = notationService.createEmptyNotationModel(null);
        assertNull(diagramModel);

        // not existing notation
        diagramModel = notationService.createEmptyNotationModel(epcNotation.getIdentifier() + "_");
        assertNull(diagramModel);

        diagramModel = notationService.createEmptyNotationModel(epcNotation.getIdentifier());
        assertNotNull(diagramModel);
        assertTrue(diagramModel instanceof EPCDiagramModel);

        diagramModel = notationService.createEmptyNotationModel(hierarchyNotation.getIdentifier());
        assertNotNull(diagramModel);
        assertTrue(diagramModel instanceof ProcessHierarchyDiagramModel);

        // order does NOT matter
        diagramModel = notationService_reverse.createEmptyNotationModel(epcNotation.getIdentifier());
        assertNotNull(diagramModel);
        assertTrue(diagramModel instanceof EPCDiagramModel);

        diagramModel = notationService_reverse.createEmptyNotationModel(hierarchyNotation.getIdentifier());
        assertNotNull(diagramModel);
        assertTrue(diagramModel instanceof ProcessHierarchyDiagramModel);

        // empty notation service
        diagramModel = notationService_empty.createEmptyNotationModel(epcNotation.getIdentifier());
        assertNull(diagramModel);

        diagramModel = notationService_empty.createEmptyNotationModel(hierarchyNotation.getIdentifier());
        assertNull(diagramModel);        
    }

    /**
     * Simple testing of existNotation(String) method.
     */
    @Test
    public void testExistNotation(){
        assertFalse(notationService.existNotation(null));

        // not existing notation
        assertFalse(notationService.existNotation(epcNotation.getIdentifier() + "_"));

        assertTrue(notationService.existNotation(epcNotation.getIdentifier()));
        assertTrue(notationService.existNotation(hierarchyNotation.getIdentifier()));

        // order doesn't matter
        assertTrue(notationService_reverse.existNotation(epcNotation.getIdentifier()));
        assertTrue(notationService_reverse.existNotation(hierarchyNotation.getIdentifier()));

        // empty notation model
        assertFalse(notationService_empty.existNotation(epcNotation.getIdentifier()));
        assertFalse(notationService_empty.existNotation(hierarchyNotation.getIdentifier()));        
    }

    /**
     * Simple testing of getNotationFileExtension(String) method.
     */
    @Test
    public void testGetNotationFileExtension(){
        assertNull(notationService.getNotationFileExtension(null));

        // not existing notation
        assertNull(notationService.getNotationFileExtension(epcNotation.getIdentifier() + "_"));

        assertEquals(
                notationService.getNotationFileExtension(epcNotation.getIdentifier()),
                epcNotation.getLocalIOController().getNotationFileExtension()
        );

        assertEquals(
                notationService.getNotationFileExtension(hierarchyNotation.getIdentifier()),
                hierarchyNotation.getLocalIOController().getNotationFileExtension()
        );

        // order doesn't matter
        assertEquals(
                notationService_reverse.getNotationFileExtension(epcNotation.getIdentifier()),
                epcNotation.getLocalIOController().getNotationFileExtension()
        );

        assertEquals(
                notationService_reverse.getNotationFileExtension(hierarchyNotation.getIdentifier()),
                hierarchyNotation.getLocalIOController().getNotationFileExtension()
        );

        // empty
        assertNull(notationService_empty.getNotationFileExtension(epcNotation.getIdentifier()));

        assertNull(notationService_empty.getNotationFileExtension(hierarchyNotation.getIdentifier()));
    }

    /**
     * Simple testing of getNotationIdentifier(String) method.
     */
    @Test
    public void testGetNotationIdentifier(){
        final String epcExtension = epcNotation.getLocalIOController().getNotationFileExtension();
        final String hierarchyExtension = hierarchyNotation.getLocalIOController().getNotationFileExtension();

        assertNull(notationService.getNotationIdentifier(null));

        // not existing extension
        assertNull(notationService.getNotationIdentifier(epcExtension + "_"));

        assertEquals(notationService.getNotationIdentifier(epcExtension), epcNotation.getIdentifier());
        assertEquals(notationService.getNotationIdentifier(hierarchyExtension), hierarchyNotation.getIdentifier());

        // order does not matter
        assertEquals(notationService_reverse.getNotationIdentifier(epcExtension), epcNotation.getIdentifier());
        assertEquals(notationService_reverse.getNotationIdentifier(hierarchyExtension), hierarchyNotation.getIdentifier());

        // empty
        assertNull(notationService_empty.getNotationIdentifier(epcExtension));
        assertNull(notationService_empty.getNotationIdentifier(hierarchyExtension));        
    }

    /**
     * Simple testing of getErrors() method.
     */
    @Test
    public void testGetErrors(){
        assertTrue(notationService.getErrors().size() == 2);
        assertEquals(notationService.getErrors().get(0), error1);
        assertEquals(notationService.getErrors().get(1), error2);

        assertTrue(notationService_reverse.getErrors().size() == 0);

        assertTrue(notationService_empty.getErrors().size() == 0);
    }

}
