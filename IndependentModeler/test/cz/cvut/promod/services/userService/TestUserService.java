package cz.cvut.promod.services.userService;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import cz.cvut.promod.services.ModelerSession;

import java.lang.reflect.Method;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import static junit.framework.Assert.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 10:29:09, 2.5.2010
 */

/**
 * Tests the user service.
 */
public class TestUserService {

    private static String user1 = "user";
    private static String user2 = "John Malina";
    private static String user3 = "Jiří Březina";

    @Before
    public void setUp(){
        ModelerSession.setUserService(new UserServiceImpl());
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
    public void testSetUser(){
        ModelerSession.getUserService().setUser(null);
        assertNull(ModelerSession.getUserService().getUser());
        assertNull(ModelerSession.getUserService().getUserValueModel().getValue());

        ModelerSession.getUserService().setUser("");
        assertTrue("".equals(ModelerSession.getUserService().getUser()));
        assertTrue(ModelerSession.getUserService().getUserValueModel().getValue().equals(""));

        ModelerSession.getUserService().setUser(user1);
        assertTrue(user1.equals(ModelerSession.getUserService().getUser()));
        assertTrue(ModelerSession.getUserService().getUserValueModel().getValue().equals(user1));

        ModelerSession.getUserService().setUser(null);
        assertNull(ModelerSession.getUserService().getUser());
        assertNull(ModelerSession.getUserService().getUserValueModel().getValue());

        ModelerSession.getUserService().setUser("");
        assertTrue("".equals(ModelerSession.getUserService().getUser()));
        assertTrue(ModelerSession.getUserService().getUserValueModel().getValue().equals(""));

        final CallCounter callCounter = new CallCounter();

        ModelerSession.getUserService().getUserValueModel().addValueChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getNewValue().equals(user2)){
                    callCounter.i = 1;
                } else if(evt.getNewValue().equals(user3)){
                    callCounter.i = 2;
                } else {
                    fail();
                }
            }
        });

        ModelerSession.getUserService().setUser(user2);
        assertTrue(user2.equals(ModelerSession.getUserService().getUser()));

        if(callCounter.i != 1){
            fail();
        }

        ModelerSession.getUserService().setUser(user3);
        assertTrue(user3.equals(ModelerSession.getUserService().getUser()));

        if(callCounter.i != 2){
            fail();
        }
    }

    private static class CallCounter{
        public int i = 0;
    }

}
