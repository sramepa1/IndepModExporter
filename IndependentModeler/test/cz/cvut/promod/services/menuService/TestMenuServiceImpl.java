package cz.cvut.promod.services.menuService;

import cz.cvut.promod.IndependentModelerMain;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.actionService.ActionControlServiceImpl;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.menuService.utils.InsertMenuItemResult;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import cz.cvut.promod.services.menuService.utils.CheckableMenuItem;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.epc.EPCNotation;
import cz.cvut.promod.hierarchyNotation.ProcessHierarchyNotation;

import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.*;
import java.lang.reflect.Method;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import javax.swing.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 10:39:41, 2.2.2010
 */
public class TestMenuServiceImpl {

    private final static File EPC_PROPERTY_FILE =
            new File("./test/cz/cvut/promod/services/projectService/files/epc.properties");    

    private static final String NOTATION_IDENTIFIER = "notation.id";
    private static final String ACTION_1_IDENTIFIER = "action.1.id";
    private static final String ACTION_2_IDENTIFIER = "action.2.id";
    private static final String ACTION_3_IDENTIFIER = "action.3.id";
    private static final String ACTION_4_IDENTIFIER = "action.4.id";
    private static final String ACTION_5_IDENTIFIER = "action.5.id";
    private static final String ACTION_6_IDENTIFIER = "action.6.id";
    private static final String ACTION_7_IDENTIFIER = "action.7.id";
    private static final String ACTION_8_IDENTIFIER = "action.8.id";
    private static final String ACTION_9_IDENTIFIER = "action.9.id";
    private static final String ACTION_10_IDENTIFIER = "action.10.id";
    private static final String ACTION_11_IDENTIFIER = "action.11.id";
    private static final String ACTION_12_IDENTIFIER = "action.12.id";
    private static final String ACTION_13_IDENTIFIER = "action.13.id";
    private static final String ACTION_14_IDENTIFIER = "action.14.id";
    private static final String ACTION_15_IDENTIFIER = "action.15.id";
    private static final String ACTION_16_IDENTIFIER = "action.16.id";

    private MenuControlService menuService;

    private ProModAction action1;
    private ProModAction action2;
    private ProModAction action3;
    private ProModAction action4;
    private ProModAction action5;
    private ProModAction action6;
    private ProModAction action7;
    private ProModAction action8;
    private ProModAction action9;
    private ProModAction action10;
    private ProModAction action11;
    private ProModAction action12;
    private ProModAction action13;
    private ProModAction action14;
    private ProModAction action15;
    private ProModAction action16;

    private EPCNotation epcNotation;
    private ProcessHierarchyNotation hierarchyNotation;


    @Before
    public void setUp(){
        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(IndependentModelerMain.COMMON_RESOURCES_FILE)));
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            Assert.fail();
        }                

        try {
            epcNotation = new EPCNotation(EPC_PROPERTY_FILE);
        } catch (InstantiationException e) {
            Assert.fail();
        }

        hierarchyNotation = new ProcessHierarchyNotation();

        final NotationSpecificPlugins notationSpecificPlugins1 = new NotationSpecificPlugins(epcNotation, Collections.unmodifiableList(new LinkedList<Module>()));
        final NotationSpecificPlugins notationSpecificPlugins2 = new NotationSpecificPlugins(hierarchyNotation, Collections.unmodifiableList(new LinkedList<Module>()));
        final java.util.List<NotationSpecificPlugins> notationSpecificPluginsList;
        notationSpecificPluginsList = new LinkedList<NotationSpecificPlugins>();

        notationSpecificPluginsList.add(notationSpecificPlugins1);
        notationSpecificPluginsList.add(notationSpecificPlugins2);

        ModelerSession.setComponentFactoryService(new ComponentFactoryServiceImpl());
        ModelerSession.setActionControlService(new ActionControlServiceImpl());
        ModelerSession.setNotationService(new NotationServiceImpl(notationSpecificPluginsList, null));

        menuService = new MenuControlServiceImpl();

        action1 = new ProModAction("Action 1", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 1");
            }
        };
        action2 = new ProModAction("Action 2", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 2");
            }
        };
        action3 = new ProModAction("Action 3", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 3");
            }
        };
        action4 = new ProModAction("Action 4", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 4");
            }
        };
        action5 = new ProModAction("Action 5", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 5");
            }
        };
        action6 = new ProModAction("Action 6", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 6");
            }
        };
        action7 = new ProModAction("Action 3", null, null){ //same name as action 3
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 7");
            }
        };
        action8 = new ProModAction("Action 3", null, null){ //same name as action 3
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 8");
            }
        };
        action9 = new ProModAction("Action 9", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 9");
            }
        };
        action10 = new ProModAction("Action 10", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 10");
            }
        };
        action11 = new ProModAction("Action 11", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 11");
            }
        };
        action12 = new ProModAction("Action 12", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 12");
            }
        };
        action13 = new ProModAction("E", null, null){ // has the same name as menu
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 13");
            }
        };
        action14 = new ProModAction("E", null, null){ // has the same name as menu
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 14");
            }
        };
        action15 = new ProModAction("", null, null){ // empty name
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 15");
            }
        };
        action16 = new ProModAction("Action 16", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.println("Performing action 16");
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
    public void testInsertMainMenuItem(){
        InsertMenuItemResult result = menuService.insertMainMenuItem(null, null, null, true);
        assertEquals(result, InsertMenuItemResult.WRONG_PLACEMENT);

        result = menuService.insertMainMenuItem(null, new MenuItemPosition("A"), null, true);
        assertEquals(result, InsertMenuItemResult.UNDEFINED_ACTION);

        assertTrue(menuService.getMenuBar().getMenuCount() == 0);

        // testing illegal position info
        result = menuService.insertMainMenuItem(
                action1,
                new MenuItemPosition("")
        );
        assertEquals(result, InsertMenuItemResult.WRONG_PLACEMENT);

        result = menuService.insertMainMenuItem(
                action1,
                new MenuItemPosition(null)
        );
        assertEquals(result, InsertMenuItemResult.WRONG_PLACEMENT);

        // only actions registered with ActionServices are allowed to be inserted to the menus
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("A"));
        assertEquals(result, InsertMenuItemResult.NOT_REGISTERED_ACTION);

        assertTrue(menuService.getMenuBar().getMenuCount() == 0);

        // inserting under no parent menu
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_1_IDENTIFIER, action1);
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition(""));
        assertEquals(result, InsertMenuItemResult.WRONG_PLACEMENT);

        // registering action without module
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("A"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertTrue(menuService.getMenuBar().getMenu(0).getText().equals("A"));

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action1);

        // inserting action with empty name
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_15_IDENTIFIER, action15);
        result = menuService.insertMainMenuItem(action15, new MenuItemPosition("A"));
        assertEquals(result, InsertMenuItemResult.UNDEFINED_ACTION);

        Component[] menuComponents = menuService.getMenuBar().getMenu(0).getMenuComponents();

        assertTrue(menuService.getMenuBar().getMenu(0).getText().equals("A"));
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertTrue(menuComponents.length == 1);

        // inserting separator BEFORE
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_2_IDENTIFIER, action2);
        result = menuService.insertMainMenuItem(action2, new MenuItemPosition("A"), MenuService.MenuSeparator.BEFORE);
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        menuComponents = menuService.getMenuBar().getMenu(0).getMenuComponents();

        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertTrue(menuComponents[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action2);
        assertTrue(menuComponents.length == 3);

        // inserting separator AFTER
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, action3);
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_4_IDENTIFIER, action4);
        result = menuService.insertMainMenuItem(action3, new MenuItemPosition("A"), MenuService.MenuSeparator.AFTER);
        assertEquals(result, InsertMenuItemResult.SUCCESS);
        result = menuService.insertMainMenuItem(action4, new MenuItemPosition("A"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[2]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[4] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action4);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 6);

        // insert to relative position, FIRST
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_5_IDENTIFIER, action5);
        result = menuService.insertMainMenuItem(action5, new MenuItemPosition("A", MenuItemPosition.PlacementStyle.FIRST));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[2] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[4]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[5] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action4);

        // insert to relative position, LAST
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_6_IDENTIFIER, action6);
        result = menuService.insertMainMenuItem(action6, new MenuItemPosition("A", MenuItemPosition.PlacementStyle.LAST));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[2] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[4]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[5] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 8);

        // grouping, inserting action with duplicate text label
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_7_IDENTIFIER, action7);
        result = menuService.insertMainMenuItem(action7, new MenuItemPosition("A"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[2] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[4]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[6] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[8]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 9);

        // grouping, inserting action with duplicate text label, ignoring explicitly specified placement
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_8_IDENTIFIER, action8);
        result = menuService.insertMainMenuItem(action8, new MenuItemPosition("A", MenuItemPosition.PlacementStyle.FIRST));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[2] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[4]).getAction(), action8);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[7] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[8]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[9]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 10);

        // inserting to the explicitly specified position, 0
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_9_IDENTIFIER, action9);
        result = menuService.insertMainMenuItem(action9, new MenuItemPosition("A", 0));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action9);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[2]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[3] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[4]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action8);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[8] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[9]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[10]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 11);

        // inserting to the explicitly specified position, 2
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_10_IDENTIFIER, action10);
        result = menuService.insertMainMenuItem(action10, new MenuItemPosition("A", 2));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action9);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[2]).getAction(), action10);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[4] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action8);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[8]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[9] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[10]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[11]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 12);

        // inserting to the explicitly specified position, 100 -> last position
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_11_IDENTIFIER, action11);
        result = menuService.insertMainMenuItem(action11, new MenuItemPosition("A", 100));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action9);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[2]).getAction(), action10);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[4] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action8);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[8]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[9] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[10]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[11]).getAction(), action6);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[12]).getAction(), action11);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 13);

        // inserting to the explicitly specified position (last but one), with separator and the item is supposed to be checkable
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_12_IDENTIFIER, action12);
        result = menuService.insertMainMenuItem(action12,
                new MenuItemPosition("A", menuService.getMenuBar().getMenu(0).getItemCount() - 1),
                MenuService.MenuSeparator.BEFORE,
                true);
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[0]).getAction(), action9);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[1]).getAction(), action5);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[2]).getAction(), action10);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[3]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[4] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[5]).getAction(), action2);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[6]).getAction(), action8);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[7]).getAction(), action7);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[8]).getAction(), action3);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[9] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[10]).getAction(), action4);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[11]).getAction(), action6);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents()[12] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[13]).getAction(), action12);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[14]).getAction(), action11);
        assertTrue(menuService.getMenuBar().getMenu(0).getMenuComponents().length == 15);

        assertTrue((JMenuItem)menuService.getMenuBar().getMenu(0).getMenuComponents()[13] instanceof CheckableMenuItem);

        //**** inserting to menu "B" ****//

        // Insertion to the empty menu with separator at the menu start
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("B"), MenuService.MenuSeparator.BEFORE);
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents()[0] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(1).getMenuComponents()[1]).getAction(), action1);

        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents().length == 2);


       // do insert 2 separators next to each other
        result = menuService.insertMainMenuItem(action2,
                new MenuItemPosition("B", MenuItemPosition.PlacementStyle.FIRST),
                MenuService.MenuSeparator.AFTER);
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(1).getMenuComponents()[0]).getAction(), action2);
        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents()[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(1).getMenuComponents()[2]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents().length == 3);


        //**** inserting to menu "C" ****//

        // Insertion to the empty menu with separator at the menu end
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("C"), MenuService.MenuSeparator.AFTER);
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(2).getMenuComponents()[0]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(2).getMenuComponents()[1] instanceof JPopupMenu.Separator);
        assertTrue(menuService.getMenuBar().getMenu(2).getMenuComponents().length == 2);

        //**** inserting to menu "D" ****//

        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("D;E;F"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        Object item = menuService.getMenuBar().getMenu(3).getMenuComponents()[0];
        assertTrue(item instanceof JMenu);
        final JMenu menu1 = (JMenu) item;
        assertTrue(menu1.getText().equals("E"));
        assertTrue(menuService.getMenuBar().getMenu(3).getText().equals("D"));
        assertTrue(menuService.getMenuBar().getMenu(3).getMenuComponents().length == 1);
        assertTrue(menu1.getMenuComponentCount() == 1);

        item = menu1.getMenuComponent(0);
        assertTrue(item instanceof JMenu);
        final JMenu menu2 = (JMenu) item;
        assertTrue(menu2.getText().equals("F"));
        assertTrue(menu2.getMenuComponentCount() == 1);

        assertEquals(((JMenuItem)menu2.getMenuComponents()[0]).getAction(), action1);

        result = menuService.insertMainMenuItem(action2, new MenuItemPosition("D;E;F"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals(((JMenuItem)menu2.getMenuComponents()[1]).getAction(), action2);
        assertTrue(menu2.getMenuComponentCount() == 2);

        // append new menu to the structure D -> E -> F -> "G" -> action3
        result = menuService.insertMainMenuItem(action3, new MenuItemPosition("D;E;F;G"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        item = menu2.getMenuComponent(2);
        assertTrue(item instanceof JMenu);
        final JMenu menu3 = (JMenu) item;
        assertTrue(menu3.getText().equals("G"));
        assertTrue(menu3.getMenuComponentCount() == 1);

        assertEquals(((JMenuItem)menu3.getMenuComponents()[0]).getAction(), action3);

        // append new action under u menu
        result = menuService.insertMainMenuItem(action4, new MenuItemPosition("D"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        assertEquals((menuService.getMenuBar().getMenu(3).getMenuComponents()[0]), menu1);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(3).getMenuComponents()[1]).getAction(), action4);
        assertTrue(menuService.getMenuBar().getMenu(3).getMenuComponents().length == 2);

        // do not group items if one is JMenuItem and the other is JMenu
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_13_IDENTIFIER, action13);
        result = menuService.insertMainMenuItem(action13, new MenuItemPosition("D"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        menuComponents = menuService.getMenuBar().getMenu(3).getMenuComponents();
        assertEquals(menuComponents[0], menu1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action13);
        assertTrue(menuComponents.length == 3);

        // group items
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_14_IDENTIFIER, action14);
        result = menuService.insertMainMenuItem(action14, new MenuItemPosition("D", MenuItemPosition.PlacementStyle.FIRST));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        menuComponents = menuService.getMenuBar().getMenu(3).getMenuComponents();
        assertEquals(menuComponents[0], menu1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action14);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action13);
        assertTrue(menuComponents.length == 4);

        // append new menu and action in it
        result = menuService.insertMainMenuItem(action13, new MenuItemPosition("D;D1"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        menuComponents = menuService.getMenuBar().getMenu(3).getMenuComponents();
        assertEquals(menuComponents[0], menu1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action14);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action13);
        assertTrue(menuComponents[4] instanceof JMenu);
        assertTrue(((JMenu)menuComponents[4]).getText().equals("D1"));
        assertTrue(menuComponents.length == 5);

        menuComponents = ((JMenu) menuComponents[4]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action13);
        assertTrue(menuComponents.length == 1);

        // inserting of the same action under the same parent
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("B"));
        assertEquals(result, InsertMenuItemResult.DUPLICIT_ACTION);

        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(1).getMenuComponents()[0]).getAction(), action2);
        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents()[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuService.getMenuBar().getMenu(1).getMenuComponents()[2]).getAction(), action1);
        assertTrue(menuService.getMenuBar().getMenu(1).getMenuComponents().length == 3);

        // inserting of the same action but not under the same menu parent
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("B;B1"));
        assertEquals(result, InsertMenuItemResult.SUCCESS);

        menuComponents = menuService.getMenuBar().getMenu(1).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action2);
        assertTrue(menuComponents[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action1);
        assertTrue(menuComponents[3] instanceof JMenu);
        assertTrue(menuComponents.length == 4);

        // inserting separators when grouping is active, ALL BEFORE
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("E"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(action2, new MenuItemPosition("E"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(
                action3, new MenuItemPosition("E"), MenuService.MenuSeparator.BEFORE);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(action4, new MenuItemPosition("E"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        result = menuService.insertMainMenuItem(
                action7, new MenuItemPosition("E"), MenuService.MenuSeparator.BEFORE); //action3.text == action7.text == action8.text
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(
                action8, new MenuItemPosition("E"), MenuService.MenuSeparator.BEFORE); //action3.text == action7.text == action8.text
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = menuService.getMenuBar().getMenu(4).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertTrue(menuComponents[2] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[5]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action4);

        assertTrue(menuComponents.length == 7);

        // inserting separators when grouping is active, ALL AFTER
        result = menuService.insertMainMenuItem(action1, new MenuItemPosition("F"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(action2, new MenuItemPosition("F"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(
                action3, new MenuItemPosition("F"), MenuService.MenuSeparator.AFTER);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(action4, new MenuItemPosition("F"));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        result = menuService.insertMainMenuItem(
                action7, new MenuItemPosition("F"), MenuService.MenuSeparator.AFTER); //action3.text == action7.text == action8.text
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));
        result = menuService.insertMainMenuItem(
                action8, new MenuItemPosition("F"), MenuService.MenuSeparator.AFTER); //action3.text == action7.text == action8.text
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = menuService.getMenuBar().getMenu(5).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action3);
        assertTrue(menuComponents[5] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action4);

        assertTrue(menuComponents.length == 7);

        // grouping if there is no name duplicity
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_16_IDENTIFIER, action16);
        result = menuService.insertMainMenuItem(action16, new MenuItemPosition("F", MenuItemPosition.PlacementStyle.GROUPED));
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = menuService.getMenuBar().getMenu(5).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action3);
        assertTrue(menuComponents[5] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[7]).getAction(), action16);

        assertTrue(menuComponents.length == 8);
    }

    @Test
    public void TestInsertProjectNavigationPopupMenuItem(){
        InsertMenuItemResult result = menuService.insertProjectNavigationPopupMenuItem(null, null, null);
        assertTrue(InsertMenuItemResult.WRONG_PLACEMENT.equals(result));

        result = menuService.insertProjectNavigationPopupMenuItem(null, new MenuItemPosition(""), null);
        assertTrue(InsertMenuItemResult.UNDEFINED_ACTION.equals(result));

        // inserting of unregistered action
        result = menuService.insertProjectNavigationPopupMenuItem(action1, new MenuItemPosition(""), null);
        assertTrue(InsertMenuItemResult.NOT_REGISTERED_ACTION.equals(result));

        final JPopupMenu popupMenu = menuService.getProjectTreePopupMenu();
        Component[] menuComponents = popupMenu.getComponents();

        assertTrue(menuComponents.length == 0);

        // inserting of an action
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_1_IDENTIFIER, action1);
        result = menuService.insertProjectNavigationPopupMenuItem(action1, new MenuItemPosition(""), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();        
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertTrue(menuComponents.length == 1);

        // inserting of an action to the FIRST position
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_2_IDENTIFIER, action2);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action2, new MenuItemPosition("", MenuItemPosition.PlacementStyle.FIRST), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action1);
        assertTrue(menuComponents.length == 2);

        // inserting the same action under the same parent
        result = menuService.insertProjectNavigationPopupMenuItem(
                action2, new MenuItemPosition(""), null);
        assertTrue(InsertMenuItemResult.DUPLICIT_ACTION.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action1);
        assertTrue(menuComponents.length == 2);

        // inserting of an action to the position 0
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, action3);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action3, new MenuItemPosition("", 0), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action1);
        assertTrue(menuComponents.length == 3);

        // inserting of an action to the position > 0 (2)
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_4_IDENTIFIER, action4);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action4, new MenuItemPosition("", 2), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action1);
        assertTrue(menuComponents.length == 4);

        // inserting of an action to the position -> inf (1000)
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_5_IDENTIFIER, action5);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action5, new MenuItemPosition("", 1000), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action5);
        assertTrue(menuComponents.length == 5);

        // inserting of an action to the LAST position
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_6_IDENTIFIER, action6);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action6, new MenuItemPosition("", MenuItemPosition.PlacementStyle.LAST), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action5);
        assertEquals(((JMenuItem)menuComponents[5]).getAction(), action6);        
        assertTrue(menuComponents.length == 6);

        // grouping
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_7_IDENTIFIER, action7);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action7, new MenuItemPosition(""), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[5]).getAction(), action5);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action6);
        assertTrue(menuComponents.length == 7);

        // inserting submenus
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_9_IDENTIFIER, action9);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action9, new MenuItemPosition("A"), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[5]).getAction(), action5);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action6);

        assertTrue(menuComponents[7] instanceof JMenu);
        assertTrue(((JMenu)menuComponents[7]).getText().equals("A"));

        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action9);

        assertTrue(menuComponents.length == 1);

        // inserting second action to the submenu to the FIRST position
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_10_IDENTIFIER, action10);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action10, new MenuItemPosition("A", MenuItemPosition.PlacementStyle.FIRST), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action2);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action4);
        assertEquals(((JMenuItem)menuComponents[4]).getAction(), action1);
        assertEquals(((JMenuItem)menuComponents[5]).getAction(), action5);
        assertEquals(((JMenuItem)menuComponents[6]).getAction(), action6);

        assertTrue(menuComponents[7] instanceof JMenu);
        assertTrue(((JMenu)menuComponents[7]).getText().equals("A"));

        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action10);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action9);

        assertTrue(menuComponents.length == 2);

        // inserting second action to the submenu to the position 1
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_8_IDENTIFIER, action8);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action8, new MenuItemPosition("A", 1), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action10);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action9);

        assertTrue(menuComponents.length == 3);

        // inserting second action to the submenu to the LAST position, but GROUPING
        ModelerSession.getActionService().registerAction(NOTATION_IDENTIFIER, ACTION_7_IDENTIFIER, action7);
        result = menuService.insertProjectNavigationPopupMenuItem(
                action7, new MenuItemPosition("A", MenuItemPosition.PlacementStyle.LAST), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action10);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action9);

        assertTrue(menuComponents.length == 4);

        // appending new menu into the submenu
        result = menuService.insertProjectNavigationPopupMenuItem(
                action1, new MenuItemPosition("A;A1"), null);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action10);
        assertEquals(((JMenuItem)menuComponents[1]).getAction(), action7);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action8);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action9);
        assertTrue(menuComponents[4] instanceof JMenu);
        assertTrue(((JMenu)menuComponents[4]).getText().equals("A1"));

        assertTrue(menuComponents.length == 5);

        menuComponents = ((JMenu) menuComponents[4]).getMenuComponents();
        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action1);
        assertTrue(menuComponents.length == 1);

        // insert separator
        result = menuService.insertProjectNavigationPopupMenuItem(
                action2, new MenuItemPosition("A;A1", 0), MenuService.MenuSeparator.AFTER);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertTrue(menuComponents.length == 5);

        menuComponents = ((JMenu) menuComponents[4]).getMenuComponents();

        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action2);
        assertTrue(menuComponents[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action1);
        assertTrue(menuComponents.length == 3);

        // insert item to the specified position, insert separator, don't insert 2 separators each to other
        result = menuService.insertProjectNavigationPopupMenuItem(
                action3, new MenuItemPosition("A;A1", 2), MenuService.MenuSeparator.BEFORE);
        assertTrue(InsertMenuItemResult.SUCCESS.equals(result));

        menuComponents = popupMenu.getComponents();
        assertTrue(menuComponents.length == 8);

        menuComponents = ((JMenu) menuComponents[7]).getMenuComponents();
        assertTrue(menuComponents.length == 5);

        menuComponents = ((JMenu) menuComponents[4]).getMenuComponents();

        assertEquals(((JMenuItem)menuComponents[0]).getAction(), action2);
        assertTrue(menuComponents[1] instanceof JPopupMenu.Separator);
        assertEquals(((JMenuItem)menuComponents[2]).getAction(), action3);
        assertEquals(((JMenuItem)menuComponents[3]).getAction(), action1);
        assertTrue(menuComponents.length == 4);
    }

    @Test
    public void testInsertPopupMenuItem(){
        // mock notation doesn't support popup menu
        ModelerSession.getActionService().registerAction(
                hierarchyNotation.getIdentifier(), ACTION_1_IDENTIFIER, action1
        );
        InsertMenuItemResult result = menuService.insertPopupMenuItem(
                hierarchyNotation.getIdentifier(),
                action1,
                new MenuItemPosition(""),
                MenuService.MenuSeparator.AFTER);

        assertEquals(result, InsertMenuItemResult.POPUP_NOT_SUPPORTED);
    }


}
