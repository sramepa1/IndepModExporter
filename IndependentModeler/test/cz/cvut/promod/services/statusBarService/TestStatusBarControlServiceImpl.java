package cz.cvut.promod.services.statusBarService;

import cz.cvut.promod.IndependentModelerMain;
import cz.cvut.promod.services.actionService.ActionControlServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.services.statusBarService.utils.InsertStatusBarItemResult;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.lang.reflect.Method;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import com.jidesoft.status.*;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.editor.status.EditableStatusBarItem;
import static junit.framework.Assert.assertEquals;
import junit.framework.Assert;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:41:12, 4.2.2010
 */
public class TestStatusBarControlServiceImpl {

    private StatusBarControlService statusBarService;

    private ProcessHierarchyNotation notation;

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

        statusBarService = new StatusBarControlServiceImpl();

        notation = new ProcessHierarchyNotation();

        final NotationSpecificPlugins notationSpecificPlugins = new NotationSpecificPlugins(notation, Collections.unmodifiableList(new LinkedList<Module>()));
        final List<NotationSpecificPlugins> notationSpecificPluginsList =
                new LinkedList<NotationSpecificPlugins>();

        notationSpecificPluginsList.add(notationSpecificPlugins);

        ModelerSession.setNotationService(new NotationServiceImpl(notationSpecificPluginsList, null));
        ModelerSession.setActionControlService(new ActionControlServiceImpl());
        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
    }

    @Test
    public void testAddStatusBarItem(){
        //nullary notation identifier
        InsertStatusBarItemResult result = statusBarService.addStatusBarItem(null, null, null);
        assertTrue(InsertStatusBarItemResult.INVALID_NOTATION.equals(result));

        //not existing notation
        result = statusBarService.addStatusBarItem(notation.getIdentifier() + "_", null, null);
        assertTrue(InsertStatusBarItemResult.INVALID_NOTATION.equals(result));

        // nullary status bar item
        result = statusBarService.addStatusBarItem(notation.getIdentifier(), null, null);
        assertTrue(InsertStatusBarItemResult.INVALID_STATUS_BAR_ITEM.equals(result));

        Component[] components = statusBarService.getStatusBar(notation.getIdentifier()).getComponents();
        assertTrue(components.length == 0);        

        // success, null layout -> set to implicit value (FIX)
        LabelStatusBarItem labelStatusBarItem = new LabelStatusBarItem("ABC");
        result = statusBarService.addStatusBarItem(notation.getIdentifier(), labelStatusBarItem, null);
        assertTrue(InsertStatusBarItemResult.SUCCESS.equals(result));

        components = statusBarService.getStatusBar(notation.getIdentifier()).getComponents();
        assertEquals(components[0], labelStatusBarItem);
        assertTrue(components.length == 1);

        LayoutManager layoutManager = statusBarService.getStatusBar(notation.getIdentifier()).getLayout();
        if(layoutManager instanceof JideBoxLayout){
            final JideBoxLayout jideBoxLayout = (JideBoxLayout) layoutManager;

            assertEquals(jideBoxLayout.getConstraintMap().get(labelStatusBarItem), JideBoxLayout.FIX);
        } else {
            fail("Not valid layout for this test");
        }

        // success,layout -> set FIX
        ButtonStatusBarItem buttonStatusBarItem = new ButtonStatusBarItem();
        result = statusBarService.addStatusBarItem(
                notation.getIdentifier(), buttonStatusBarItem, JideBoxLayout.FIX);
        assertTrue(InsertStatusBarItemResult.SUCCESS.equals(result));

        components = statusBarService.getStatusBar(notation.getIdentifier()).getComponents();
        assertEquals(components[0], labelStatusBarItem);
        assertTrue(components[1] instanceof StatusBarSeparator);
        assertEquals(components[2], buttonStatusBarItem);
        assertTrue(components.length == 3);

        layoutManager = statusBarService.getStatusBar(notation.getIdentifier()).getLayout();
        if(layoutManager instanceof JideBoxLayout){
            final JideBoxLayout jideBoxLayout = (JideBoxLayout) layoutManager;

            assertEquals(jideBoxLayout.getConstraintMap().get(labelStatusBarItem), JideBoxLayout.FIX);
            assertEquals(jideBoxLayout.getConstraintMap().get(buttonStatusBarItem), JideBoxLayout.FIX);
        } else {
            fail("Not valid layout for this test");
        }

        // success,layout -> set VARY
        EditableStatusBarItem editableStatusBarItem = new EditableStatusBarItem();
        result = statusBarService.addStatusBarItem(
                notation.getIdentifier(), editableStatusBarItem, JideBoxLayout.VARY);
        assertTrue(InsertStatusBarItemResult.SUCCESS.equals(result));

        components = statusBarService.getStatusBar(notation.getIdentifier()).getComponents();
        assertEquals(components[0], labelStatusBarItem);
        assertTrue(components[1] instanceof StatusBarSeparator);
        assertEquals(components[2], buttonStatusBarItem);
        assertTrue(components[3] instanceof StatusBarSeparator);
        assertEquals(components[4], editableStatusBarItem);
        assertTrue(components.length == 5);

        layoutManager = statusBarService.getStatusBar(notation.getIdentifier()).getLayout();
        if(layoutManager instanceof JideBoxLayout){
            final JideBoxLayout jideBoxLayout = (JideBoxLayout) layoutManager;

            assertEquals(jideBoxLayout.getConstraintMap().get(labelStatusBarItem), JideBoxLayout.FIX);
            assertEquals(jideBoxLayout.getConstraintMap().get(buttonStatusBarItem), JideBoxLayout.FIX);
            assertEquals(jideBoxLayout.getConstraintMap().get(editableStatusBarItem), JideBoxLayout.VARY);
        } else {
            fail("Not valid layout for this test");
        }

        // success,layout -> set FLEXIBLE
        MemoryStatusBarItem memoryStatusBarItem = new MemoryStatusBarItem();
        result = statusBarService.addStatusBarItem(
                notation.getIdentifier(), memoryStatusBarItem, JideBoxLayout.FLEXIBLE);
        assertTrue(InsertStatusBarItemResult.SUCCESS.equals(result));

        components = statusBarService.getStatusBar(notation.getIdentifier()).getComponents();
        assertEquals(components[0], labelStatusBarItem);
        assertTrue(components[1] instanceof StatusBarSeparator);
        assertEquals(components[2], buttonStatusBarItem);
        assertTrue(components[3] instanceof StatusBarSeparator);
        assertEquals(components[4], editableStatusBarItem);
        assertTrue(components[5] instanceof StatusBarSeparator);
        assertEquals(components[6], memoryStatusBarItem);
        assertTrue(components.length == 7);

        layoutManager = statusBarService.getStatusBar(notation.getIdentifier()).getLayout();
        if(layoutManager instanceof JideBoxLayout){
            final JideBoxLayout jideBoxLayout = (JideBoxLayout) layoutManager;

            assertEquals(jideBoxLayout.getConstraintMap().get(labelStatusBarItem), JideBoxLayout.FIX);
            assertEquals(jideBoxLayout.getConstraintMap().get(buttonStatusBarItem), JideBoxLayout.FIX);
            assertEquals(jideBoxLayout.getConstraintMap().get(editableStatusBarItem), JideBoxLayout.VARY);
            assertEquals(jideBoxLayout.getConstraintMap().get(memoryStatusBarItem), JideBoxLayout.FLEXIBLE);
        } else {
            fail("Not valid layout for this test");
        }

        // success, inserting item to the Modeler's status bar
        EmptyStatusBarItem emptyStatusBarItem = new EmptyStatusBarItem();
        result = statusBarService.addStatusBarItem(
                ModelerModel.MODELER_IDENTIFIER, emptyStatusBarItem, JideBoxLayout.VARY);
        assertTrue(InsertStatusBarItemResult.SUCCESS.equals(result));
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

    @Test
    public void testGetStatusBar(){
        // nullary notation identifier
        StatusBar statusBar = statusBarService.getStatusBar(null);
        assertTrue(statusBar == null);

        // not existing notation
        statusBar = statusBarService.getStatusBar(notation.getIdentifier() + "_");
        assertTrue(statusBar == null);

        // existing notation
        statusBar = statusBarService.getStatusBar(notation.getIdentifier());
        assertTrue(statusBar != null);

        // there has to be a status bar for Modeler itself
        statusBar = statusBarService.getStatusBar(ModelerModel.MODELER_IDENTIFIER);
        assertNotNull(statusBar);         
    }

}
