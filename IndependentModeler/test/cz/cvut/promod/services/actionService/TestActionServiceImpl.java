package cz.cvut.promod.services.actionService;

import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.gui.ModelerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import static junit.framework.Assert.fail;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 20:48:42, 1.2.2010
 */
public class TestActionServiceImpl {

    private ActionControlServiceImpl actionControlService;

    private static final String NOTATION_IDENTIFIER = "Notation";

    private static final String MODULE_IDENTIFIER = "Module";

    private static final String ACTION_1_IDENTIFIER = "proModAction1ID";
    private static final String ACTION_2_IDENTIFIER = "proModAction2ID";
    private static final String ACTION_3_IDENTIFIER = "proModAction3ID";
    private static final String ACTION_4_IDENTIFIER = "proModAction4ID";
    private static final String ACTION_5_IDENTIFIER = "proModAction5ID";
    private static final String ACTION_6_IDENTIFIER = "proModAction6ID";
    private static final String ACTION_7_IDENTIFIER = "proModAction7ID";

    private ProModAction proModAction1;
    private ProModAction proModAction2;
    private ProModAction proModAction3;
    private ProModAction proModAction4;
    private ProModAction proModAction5;
    private ProModAction proModAction6;
    private ProModAction proModAction7;


    @Before
    public void setUp(){
        actionControlService = new ActionControlServiceImpl();

        proModAction1 = new ProModAction("My Action 1", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 1 do nothing.");
            }
        };

        proModAction2 = new ProModAction("My Action 2", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 2 do nothing.");
            }
        };

        proModAction3 = new ProModAction("My Action 3", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 3 do nothing.");
            }
        };

        proModAction4 = new ProModAction("My Action 4", null, null){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 4 do nothing.");
            }
        };
        proModAction5 = new ProModAction("My Action 5", null, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 4 do nothing.");
            }
        };
        proModAction6 = new ProModAction("My Action 6", null, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK)){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 4 do nothing.");
            }
        };
        proModAction7 = new ProModAction("My Action 7", null, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)){
            public void actionPerformed(ActionEvent event) {
                System.out.print("Action 4 do nothing.");
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
    public void testActionServicePublicMethods(){
        assertFalse(actionControlService.isRegisteredAction(proModAction1));
        assertFalse(actionControlService.isRegisteredAction(proModAction2));
        assertFalse(actionControlService.isRegisteredAction(proModAction3));
        assertFalse(actionControlService.isRegisteredAction(proModAction4));

        /*
            public ProModAction registerAction(final String notationIdentifier,
                                               final String moduleIdentifier,
                                               final String actionIdentifier,
                                               final ProModAction action
            );
         */
        ProModAction action = actionControlService.registerAction(null, MODULE_IDENTIFIER, ACTION_1_IDENTIFIER, proModAction1);
        assertNull(action);
        assertFalse(actionControlService.isRegisteredAction(proModAction1));

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, null, proModAction1);
        assertNull(action);
        assertFalse(actionControlService.isRegisteredAction(proModAction1));

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, ACTION_1_IDENTIFIER, null);
        assertNull(action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, null, ACTION_1_IDENTIFIER, proModAction1);
        assertNotNull(action);
        assertEquals(action,  proModAction1);
        assertTrue(actionControlService.isRegisteredAction(proModAction1));

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, null, ACTION_1_IDENTIFIER, proModAction2);
        assertNotNull(action);
        assertEquals(action,  proModAction1);
        assertTrue(actionControlService.isRegisteredAction(proModAction1));
        assertFalse(actionControlService.isRegisteredAction(proModAction2));


        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, ACTION_1_IDENTIFIER, proModAction2);
        assertNotNull(action);
        assertEquals(action,  proModAction2);
        assertEquals(action,  proModAction2);
        assertTrue(actionControlService.isRegisteredAction(proModAction1));
        assertTrue(actionControlService.isRegisteredAction(proModAction2));

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, ACTION_2_IDENTIFIER, proModAction1);
        assertNotNull(action);
        assertEquals(action,  proModAction1);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, ACTION_2_IDENTIFIER, proModAction2);
        assertNotNull(action);
        assertEquals(action,  proModAction1);

        /*
            public ProModAction registerAction(final String notationIdentifier,
                                               final String actionIdentifier,
                                               final ProModAction action
            );
         */
        action = actionControlService.registerAction(null, ACTION_1_IDENTIFIER, proModAction3);
        assertNull(action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, null, proModAction3);
        assertNull(action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, null);
        assertNull(action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, proModAction3);
        assertNotNull(action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, proModAction3);
        assertEquals(proModAction3, action);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_3_IDENTIFIER, proModAction4);
        assertNotNull(action);
        assertEquals(proModAction3, action);

        assertTrue(actionControlService.isRegisteredAction(proModAction3));

        assertFalse(actionControlService.isRegisteredAction(proModAction4));

        action = actionControlService.registerAction(ModelerModel.MODELER_IDENTIFIER, ACTION_4_IDENTIFIER, proModAction4);
        assertNotNull(action);
        assertEquals(action,  proModAction4);
        assertTrue(actionControlService.isRegisteredAction(proModAction4));
        /*
        public ProModAction getAction(final String notationIdentifier,
                                      final String moduleIdentifier,
                                      final String actionIdentifier
        );

        public ProModAction getAction(final String notationIdentifier,
                                      final String actionIdentifier
        );        
         */
        action = actionControlService.getAction(null, null, null);
        assertNull(action);

        action = actionControlService.getAction(null, null, ACTION_1_IDENTIFIER);
        assertNull(action);

        action = actionControlService.getAction(null, ACTION_1_IDENTIFIER);
        assertNull(action);

        action = actionControlService.getAction(NOTATION_IDENTIFIER, null, ACTION_2_IDENTIFIER);
        assertNull(action);

        action = actionControlService.getAction(NOTATION_IDENTIFIER, null, ACTION_1_IDENTIFIER);
        assertEquals(action, proModAction1);

        action = actionControlService.getAction(NOTATION_IDENTIFIER, ACTION_1_IDENTIFIER);
        assertEquals(action, proModAction1);

        action = actionControlService.registerAction(NOTATION_IDENTIFIER, MODULE_IDENTIFIER, ACTION_1_IDENTIFIER, proModAction2);
        assertEquals(action, proModAction2);

        /*
        public void updateActionsVisibility(final String notationIdentifier);
        */

        actionControlService.updateActionsVisibility("ABC");
        assertFalse(proModAction1.isEnabled());
        assertFalse(proModAction2.isEnabled());
        assertFalse(proModAction3.isEnabled());
        assertTrue(proModAction4.isEnabled()); // Modeler Identifier

        actionControlService.updateActionsVisibility(NOTATION_IDENTIFIER);
        assertTrue(proModAction1.isEnabled());
        assertTrue(proModAction2.isEnabled());
        assertTrue(proModAction3.isEnabled());
        assertTrue(proModAction4.isEnabled()); // Modeler Identifier

        actionControlService.updateActionsVisibility("DEF");
        assertFalse(proModAction1.isEnabled());
        assertFalse(proModAction2.isEnabled());
        assertFalse(proModAction3.isEnabled());
        assertTrue(proModAction4.isEnabled()); // Modeler Identifier
    }

    /**
     * All action, that are registered by one particular notation, have to have unique key accelerator. If there is
     * any duplicity in key accelerator the action, that wasn't registered first, is supposed to lose it's key
     * accelerator
     */
    @Test
    public void testKeyAcceleratorUniqueness(){
        ProModAction action =
                actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_5_IDENTIFIER, proModAction5);
        assertEquals(action,  proModAction5);
        assertNotNull(action.getValue(AbstractAction.ACCELERATOR_KEY));

        action =
                actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_6_IDENTIFIER, proModAction6);
        assertEquals(action,  proModAction6);
        assertNotNull(action.getValue(AbstractAction.ACCELERATOR_KEY));

        // action is supposed to lose it's key accelerator, because it has the same key accelerator as action 5
        action =
                actionControlService.registerAction(NOTATION_IDENTIFIER, ACTION_7_IDENTIFIER, proModAction7);
        assertEquals(action,  proModAction7);
        assertNull(action.getValue(AbstractAction.ACCELERATOR_KEY));
    }

}
