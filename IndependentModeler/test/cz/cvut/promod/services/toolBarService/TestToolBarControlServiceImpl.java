package cz.cvut.promod.services.toolBarService;

import cz.cvut.promod.IndependentModelerMain;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.actionService.ActionControlServiceImpl;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.toolBarService.utils.InsertToolBarItemResult;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;

import java.awt.event.ActionEvent;
import java.util.*;
import java.lang.reflect.Method;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import com.jidesoft.action.CommandBar;
import static junit.framework.Assert.fail;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 11:39:17, 4.2.2010
 */
public class TestToolBarControlServiceImpl {

    private ToolBarControlService toolBarService;

    private ProcessHierarchyNotation notation;

    private static final String ACTION_2_IDENTIFIER = "action.2.id";

    private ProModAction action1;
    private ProModAction action2;

    @Before
    public void setUp(){
        toolBarService = new ToolBarControlServiceImpl();

        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            Assert.fail();
        }

        notation = new ProcessHierarchyNotation();

        final NotationSpecificPlugins notationSpecificPlugins = new NotationSpecificPlugins(notation, Collections.unmodifiableList(new LinkedList<Module>()));
        final List<NotationSpecificPlugins> notationSpecificPluginsList =
                new LinkedList<NotationSpecificPlugins>();

        notationSpecificPluginsList.add(notationSpecificPlugins);

        ModelerSession.setNotationService(new NotationServiceImpl(notationSpecificPluginsList, null));
        ModelerSession.setActionControlService(new ActionControlServiceImpl());        

        action1 = new ProModAction(null, null, null){

            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 1");
            }
        };
        action2 = new ProModAction("Action 2", null, null){

            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 2");
            }
        };
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

    @Test
    public void addAction(){
        InsertToolBarItemResult result = toolBarService.addAction(null, null);
        assertTrue(InsertToolBarItemResult.UNDEFINED_ACTION.equals(result));

        result = toolBarService.addAction(notation.getIdentifier(), null);
        assertTrue(InsertToolBarItemResult.UNDEFINED_ACTION.equals(result));

        // action has an empty string as it's name
        result = toolBarService.addAction(notation.getIdentifier(), action1);
        assertTrue(InsertToolBarItemResult.UNDEFINED_ACTION.equals(result));

        // unregistered action
        result = toolBarService.addAction(notation.getIdentifier(), action2);
        assertTrue(InsertToolBarItemResult.UNREGISTERED_ACTION.equals(result));

        // nullary notation identifier
        ModelerSession.getActionService().registerAction(notation.getIdentifier(), ACTION_2_IDENTIFIER, action2);
        result = toolBarService.addAction(null, action2);
        assertTrue(InsertToolBarItemResult.INVALID_NOTATION.equals(result));

        // not existing notation
        result = toolBarService.addAction(notation.getIdentifier() + "_", action2);
        assertTrue(InsertToolBarItemResult.INVALID_NOTATION.equals(result));

        assertTrue(toolBarService.getCommandBar(notation.getIdentifier()).getComponents().length == 3);

        // inserting successfully
        result = toolBarService.addAction(notation.getIdentifier(), action2);
        assertTrue(InsertToolBarItemResult.SUCCESS.equals(result));

        assertTrue(toolBarService.getCommandBar(notation.getIdentifier()).getComponents().length == 4);

        // inserting duplicate action
        result = toolBarService.addAction(notation.getIdentifier(), action2);
        assertTrue(InsertToolBarItemResult.DUPLICATE_ACTION.equals(result));

        assertTrue(toolBarService.getCommandBar(notation.getIdentifier()).getComponents().length == 4);

    }

    @Test
    public void textGetCommandBar(){
        // getting command bar for not specified notation
        CommandBar commandBar = toolBarService.getCommandBar(null);
        assertNull(commandBar);

        // getting command bar for NOT existing notation identifier
        commandBar = toolBarService.getCommandBar(notation.getIdentifier() + "_");
        assertNull(commandBar);

        // getting command bar for existing notation identifier
        commandBar = toolBarService.getCommandBar(notation.getIdentifier());
        assertNotNull(commandBar);

        // there has to be a command bar for modeler itself 
        commandBar = toolBarService.getCommandBar(ModelerModel.MODELER_IDENTIFIER);
        assertNotNull(commandBar);
    }

}
