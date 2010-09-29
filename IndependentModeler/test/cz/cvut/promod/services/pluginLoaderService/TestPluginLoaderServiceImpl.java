package cz.cvut.promod.services.pluginLoaderService;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Ignore;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import junit.framework.Assert;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginDetails;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.IndependentModelerMain;

import java.util.List;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:21:41, 5.2.2010
 */
public class TestPluginLoaderServiceImpl {

    private final static String PLUGINS_PROPERTY_FILE_PATH = "./test/cz/cvut/promod/services/pluginLoaderService/files/";

    private final static String PLUGINS_PROPERTY_FILE_0 = PLUGINS_PROPERTY_FILE_PATH + "_0errorPlugins.xml"; // valid

    private final static String PLUGINS_PROPERTY_FILE_1 = PLUGINS_PROPERTY_FILE_PATH + "_1errorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_2 = PLUGINS_PROPERTY_FILE_PATH + "_2errorPlugins.xml";

    private final static String PLUGINS_PROPERTY_FILE_3 = PLUGINS_PROPERTY_FILE_PATH + "_3XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_4 = PLUGINS_PROPERTY_FILE_PATH + "_4XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_5 = PLUGINS_PROPERTY_FILE_PATH + "_5XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_6 = PLUGINS_PROPERTY_FILE_PATH + "_6XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_7 = PLUGINS_PROPERTY_FILE_PATH + "_7XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_8 = PLUGINS_PROPERTY_FILE_PATH + "_8XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_9 = PLUGINS_PROPERTY_FILE_PATH + "_9XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_10 = PLUGINS_PROPERTY_FILE_PATH + "_10XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_11 = PLUGINS_PROPERTY_FILE_PATH + "_11XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_12 = PLUGINS_PROPERTY_FILE_PATH + "_12XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_13 = PLUGINS_PROPERTY_FILE_PATH + "_13XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_14 = PLUGINS_PROPERTY_FILE_PATH + "_14XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_15 = PLUGINS_PROPERTY_FILE_PATH + "_15XSDerrorPlugins.xml";
    private final static String PLUGINS_PROPERTY_FILE_16 = PLUGINS_PROPERTY_FILE_PATH + "_16XSDerrorPlugins.xml";

    private PluginLoaderService pluginLoaderService;


    @Before
    public void setUp() throws ClassNotFoundException {
        pluginLoaderService = new PluginLoaderServiceImpl();

        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
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
     * Not existing file.
     */
    @Test
    public void testLoadPlugInsDefinitionNotExistingFile(){
        boolean returnValue = pluginLoaderService.loadPlugInsDefinition("non_existing_file.file");
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     *  Invalid file 1
     *
     */
    @Test
    public void testLoadPlugInsDefinitionInvalidRootElement(){
        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_1);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 2
     *
     * Missing root element.
     */
    @Test
    public void testLoadPlugInsDefinitionMissingRootElement(){
        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_2);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }


    /**
     * Invalid 3
     *
     * Unknown xml element, ABC
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError1(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_3);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 4
     *
     * Notation tag in another notation tag.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError2(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_4);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 5
     *
     * Not empty module tag
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError3(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_5);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 6
     *
     * Extension tag in notation tag.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError4(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_6);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 7
     *
     * Not empty extension tag.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError5(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_7);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 8
     *
     * Empty tag not in notation tag.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError6(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_8);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 9
     *
     * Notation tag with missing alias attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError7(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_9);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 10
     *
     * Notation tag with missing class attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError8(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_10);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 11
     *
     * Module tag with missing alias attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError9(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_11);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 12
     *
     * Module tag with missing class attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError10(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_12);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 13
     *
     * Extension tag with missing alias attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError11(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_13);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 14
     *
     * Module tag with missing class attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError12(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_14);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 15
     *
     * Extension tag is not after all notation tags.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError13(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_15);
        assertFalse(returnValue);

        assertTrue(pluginLoaderService.getNotationSpecificPlugins().isEmpty());
        assertTrue(pluginLoaderService.getExtensions().isEmpty());
        assertTrue(pluginLoaderService.getErrors().size() == 1);
        assertEquals(pluginLoaderService.getErrors().get(0).getError(), PluginLoadErrors.ERRORS.xmlParsing);

        assertTrue(pluginLoaderService.getNotationModulesInstInfo().isEmpty());
        assertTrue(pluginLoaderService.getExtensionInstInfo().isEmpty());
    }

    /**
     * Invalid 16
     *
     * Extension without config attribute.
     */
    @Test
    public void testLoadPlugInsDefinitionXSDError15(){

        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_16);
        assertTrue(returnValue);

        assertTrue(pluginLoaderService.getExtensionInstInfo().size() == 1);

        PluginDetails pluginDetails = pluginLoaderService.getExtensionInstInfo().get(0);
        assertEquals(pluginDetails.getAlias(), "MemoryIndicatorExtension");
        assertEquals(pluginDetails.getClazz(), "cz.cvut.promod.memory.MemoryIndicatorExtension");
        assertTrue(pluginDetails.getConfig() == null);
    }

    /**
     * Valid file, all other files are derived from this file.
     */
    @Test
    public void testLoadPlugInsDefinitionValidFile(){
        boolean returnValue = pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE_0);
        assertTrue(returnValue);

        final List<PluginDetails> notationModulesInstInfos = pluginLoaderService.getNotationModulesInstInfo();
        assertTrue(notationModulesInstInfos.size() == 2);

        PluginDetails pluginDetails = notationModulesInstInfos.get(0);
        assertEquals(pluginDetails.getAlias(), "epc");
        assertEquals(pluginDetails.getClazz(), "cz.cvut.promod.epc.EPCNotation");
        assertEquals(pluginDetails.getConfig(), "epcNotation.properties");

        assertTrue(pluginDetails.getModules().size() == 1);

        pluginDetails = pluginDetails.getModules().get(0);
        assertEquals(pluginDetails.getAlias(), "epc image export");
        assertEquals(pluginDetails.getClazz(), "cz.cvut.promod.epcImageExport.EPCImageExportModule");
        assertEquals(pluginDetails.getConfig(), "epcImageExportModule.properties");

        pluginDetails = notationModulesInstInfos.get(1);
        assertEquals(pluginDetails.getAlias(), "hierarchy notation");
        assertEquals(pluginDetails.getClazz(), "cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation");
        assertNull(pluginDetails.getConfig());

        assertTrue(pluginDetails.getModules().size() == 2);

        final PluginDetails mod1  = pluginDetails.getModules().get(0);
        final PluginDetails mod2  = pluginDetails.getModules().get(1);

        assertEquals(mod1.getAlias(), "ProcessHierarchyTreeLayout");
        assertEquals(mod1.getClazz(), "cz.cvut.promod.ph.treeLayout.ProcessHierarchyTreeLayout");
        assertTrue(mod1.getConfig() == null);

        assertEquals(mod2.getAlias(), "DiagramReferenceModule");
        assertEquals(mod2.getClazz(), "cz.cvut.promod.diagramReferenceModule.DiagramReferenceModule");
        assertEquals(mod2.getConfig(), "phDiagramReference.properties");

        assertTrue(pluginLoaderService.getExtensionInstInfo().size() == 1);

        pluginDetails = pluginLoaderService.getExtensionInstInfo().get(0);
        assertEquals(pluginDetails.getAlias(), "MemoryIndicatorExtension");
        assertEquals(pluginDetails.getClazz(), "cz.cvut.promod.memory.MemoryIndicatorExtension");
        assertEquals(pluginDetails.getConfig(), "memoryIndicator.properties");
    }
    
}
