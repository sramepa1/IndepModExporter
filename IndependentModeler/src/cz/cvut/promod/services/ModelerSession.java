package cz.cvut.promod.services;

import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.services.userService.UserService;
import cz.cvut.promod.services.actionService.ActionControlService;
import cz.cvut.promod.services.actionService.ActionService;
import cz.cvut.promod.services.projectService.ProjectControlService;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.notationService.NotationService;
import cz.cvut.promod.services.toolBarService.ToolBarService;
import cz.cvut.promod.services.toolBarService.ToolBarControlService;
import cz.cvut.promod.services.statusBarService.StatusBarControlService;
import cz.cvut.promod.services.statusBarService.StatusBarService;
import cz.cvut.promod.services.extensionService.ExtensionService;
import cz.cvut.promod.gui.ModelerModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:10:56, 10.10.2009
 */

/**
 * ModelerSession allows access to all services and shared resources.
 *
 * It is supposed to be an entry point for plugins when need to use services.
 *
 * IMPORTANT NOTE: One is always supposed to use all services and other resources only in Event Dispatcher Thread
 * or Main Thread (during application start).
 *
 * Usage of services and their resources form another thread than Event Dispatcher Thread or Main Thread can cause a
 * unpredictable inconsistency of application. One is supposed to always avoid using services and their
 * resources out of the Event Dispatcher Thread (when the GUI is shown)!
 *
 * @see cz.cvut.promod.services.Service
 * @see javax.swing.SwingUtilities
 */
public final class ModelerSession {

    private static final Logger LOG = Logger.getLogger(ModelerSession.class);

    /** testing purposes */
    public static final String NULL_METHOD_NAME = "nullServices";

    private static final ModelerSession MODELER_SESSION_INSTANCE = new ModelerSession();

    private static ResourceBundle commonResourceBundle = null;

    private static JFrame frame = null;

    private ComponentFactoryService componentFactoryService = null;
    private UserService userService = null;
    private ActionControlService actionControlService = null;
    private ProjectControlService projectControlService = null;
    private MenuService menuService = null;
    private NotationService notationService = null;
    private ToolBarControlService toolBarControlService = null;
    private StatusBarControlService statusBarControlService = null;
    private ExtensionService extensionsService = null;

    /**
     * @return every-time the same instance of ModelerSession (singleton)
     */
    public static ModelerSession getModelerSession() {
        return MODELER_SESSION_INSTANCE;
    }

    /**
     * Returns the common resource bundle - common translations.
     *
     * @return the common resource bundle - common translations
     */
    public static ResourceBundle getCommonResourceBundle() {
        return commonResourceBundle;
    }

    /**
     * Sets the resource bundle object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param commonResourceBundle the resource bundle holding translations
     */
    public static void setCommonResourceBundle(final ResourceBundle commonResourceBundle) {
        if(ModelerSession.commonResourceBundle == null){
            ModelerSession.commonResourceBundle = commonResourceBundle;
        }
    }

    /**
     * Usage of this method is a way how to get reference to the JFrame object of Modeler.
     * One should always check whether return value is not null before actual use.
     *
     * @return an instance of the modeler frame 
     */
    public static JFrame getFrame() {
        return frame;
    }

    /**
     * Allows to set a title for the Modeler frame.
     *
     * @param text is the text to be shown
     */
    public static void setFrameTitleText(final String text){
        if(frame != null){
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ModelerModel.APPLICATION_NAME);

            if(text != null && !text.isEmpty()){
                stringBuffer.append(" - [");
                stringBuffer.append(text);
                stringBuffer.append("]");
            }

            frame.setTitle(stringBuffer.toString());
        }
    }

    /**
     * Sets the title of Modeler frame to initial text.
     */
    public static void clearFrameTitleText(){
        if(frame != null){
            frame.setTitle(ModelerModel.APPLICATION_NAME);
        }
    }

    /**
     * Sets the application frame object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param frame is the application frame
     */
    public static void setFrame(final JFrame frame) {
        if(ModelerSession.frame == null){
            ModelerSession.frame = frame;
        }
    }

    /**
     * Returns the ComponentFactoryService object.
     *
     * @return every-time the same ComponentFactoryService object
     */
    public static ComponentFactoryService getComponentFactoryService() {
        final ComponentFactoryService componentFactoryService = MODELER_SESSION_INSTANCE.componentFactoryService;
        
        if(componentFactoryService == null){
            LOG.error("ComponentFactoryService has not been set.");
            throw new NullPointerException("ComponentFactoryService is not available.");
        }

        return componentFactoryService;
    }

    /**
     * Sets the ComponentFactoryService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param componentFactoryService is the ComponentFactoryService object
     */
    public static void setComponentFactoryService(final ComponentFactoryService componentFactoryService) {
        MODELER_SESSION_INSTANCE.componentFactoryService = componentFactoryService;
    }

    /**
     * Returns the UserService object.
     *
     * @return every-time the same UserService object
     */
    public static UserService getUserService() {
        return MODELER_SESSION_INSTANCE.userService;
    }

    /**
     * Sets the UserService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param userService is the UserService object
     */
    public static void setUserService(final UserService userService) {
        MODELER_SESSION_INSTANCE.userService = userService;
    }

    public static ActionControlService getActionControlService() {
        final ActionControlService actionControlService = MODELER_SESSION_INSTANCE.actionControlService;

        if(actionControlService == null){
            LOG.error("ActionControlService has not been set.");
            throw new NullPointerException("ActionControlService is not available.");
        }

        return actionControlService;
    }

    public static ActionService getActionService() {
        final ActionService actionService = MODELER_SESSION_INSTANCE.actionControlService;

        if(actionService == null){
            LOG.error("ActionService has not been set.");
            throw new NullPointerException("ActionService is not available.");
        }

        return actionService;
    }

    /**
     * Sets the ActionControlService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param actionControlService is the ActionControlService object
     */
    public static void setActionControlService(final ActionControlService actionControlService) {
        if(MODELER_SESSION_INSTANCE.actionControlService == null){
            MODELER_SESSION_INSTANCE.actionControlService = actionControlService;
        }
    }

    /**
     * Returns the ProjectControlService object.
     *
     * @return every-time the same ProjectControlService object
     */
    public static ProjectControlService getProjectControlService() {
        final ProjectControlService projectControlService = MODELER_SESSION_INSTANCE.projectControlService;

        if(projectControlService == null){
            LOG.error("ProjectControlService has not been set.");
            throw new NullPointerException("ProjectControlService is not available.");
        }

        return projectControlService;
    }

    /**
     * Returns the ProjectService object.
     *
     * @return every-time the same ProjectService object
     */
    public static ProjectService getProjectService() {
        final ProjectService projectService = MODELER_SESSION_INSTANCE.projectControlService;

        if(projectService == null){
            LOG.error("ProjectService has not been set.");
            throw new NullPointerException("ProjectService is not available.");
        }

        return projectService;
    }

    /**
     * Sets the ProjectControlService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param projectControlService is the ProjectControlService object
     */
    public static void setProjectControlService(final ProjectControlService projectControlService) {
        if(MODELER_SESSION_INSTANCE.projectControlService == null){
            MODELER_SESSION_INSTANCE.projectControlService = projectControlService;
        }
    }

    /**
     * Returns the MenuService object.
     *
     * @return every-time the same MenuService object
     */
    public static MenuService getMenuService() {
        final MenuService menuService = MODELER_SESSION_INSTANCE.menuService;

        if(menuService == null){
            LOG.error("MenuService has not been set.");
            throw new NullPointerException("MenuService is not available.");
        }

        return menuService;
    }

    /**
     * Sets the MenuService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param menuService is the MenuService object
     */
    public static void setMainMenuService(final MenuService menuService) {
        if(MODELER_SESSION_INSTANCE.menuService == null){
            MODELER_SESSION_INSTANCE.menuService = menuService;
        }
    }

    /**
     * Returns the NotationService object.
     *
     * @return every-time the same NotationService object
     */
    public static NotationService getNotationService() {
        final NotationService notationService = MODELER_SESSION_INSTANCE.notationService;

        if(notationService == null){
            LOG.error("NotationService has not been set.");
            throw new NullPointerException("NotationService is not available.");
        }

        return notationService;
    }

    /**
     * Sets the NotationService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param notationService is the NotationService object
     */
    public static void setNotationService(final NotationService notationService) {
        if(MODELER_SESSION_INSTANCE.notationService == null){
            MODELER_SESSION_INSTANCE.notationService = notationService;
        }
    }

    /**
     * Returns the ToolBarService object.
     *
     * @return every-time the same ToolBarService object
     */
    public static ToolBarService getToolBarService() {
       final ToolBarService toolBarService = MODELER_SESSION_INSTANCE.toolBarControlService;

        if(toolBarService == null){
            LOG.error("ToolBarService has not been set.");
            throw new NullPointerException("ToolBarService is not available.");
        }

       return toolBarService;
    }

    /**
     * Returns the ToolBarControlService object.
     *
     * @return every-time the same ToolBarControlService object
     */
    public static ToolBarControlService getToolBarControlService() {
       final ToolBarControlService toolBarControlService = MODELER_SESSION_INSTANCE.toolBarControlService;

        if(toolBarControlService == null){
            LOG.error("ToolBarControlService has not been set.");
            throw new NullPointerException("ToolBarControlService is not available.");
        }

       return toolBarControlService;
    }

    /**
     * Sets the ToolBarControlService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param toolBarService is the ToolBarControlService object
     */
    public static void setToolBarControlService(final ToolBarControlService toolBarService) {
        if(MODELER_SESSION_INSTANCE.toolBarControlService == null){
            MODELER_SESSION_INSTANCE.toolBarControlService = toolBarService;
        }
    }

    /**
     * Returns the StatusBarControlService object.
     *
     * @return every-time the same StatusBarControlService object
     */
    public static StatusBarControlService getStatusBarControlService() {
       final StatusBarControlService statusBarControlService = MODELER_SESSION_INSTANCE.statusBarControlService;

        if(statusBarControlService == null){
            LOG.error("statusBarControlService has not been set.");
            throw new NullPointerException("statusBarControlService is not available.");
        }

       return statusBarControlService;
    }

    /**
     * Returns the StatusBarService object.
     *
     * @return every-time the same StatusBarService object
     */
    public static StatusBarService getStatusBarService() {
       final StatusBarService statusBarService = MODELER_SESSION_INSTANCE.statusBarControlService;

        if(statusBarService == null){
            LOG.error("statusBarService has not been set.");
            throw new NullPointerException("statusBarService is not available.");
        }

       return statusBarService;
    }

    /**
     * Sets the StatusBarControlService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param statusBarControlService is the StatusBarControlService object
     */
    public static void setStatusBarControlService(final StatusBarControlService statusBarControlService) {
        if(MODELER_SESSION_INSTANCE.statusBarControlService == null){
            MODELER_SESSION_INSTANCE.statusBarControlService = statusBarControlService;
        }
    }

    /**
     * Returns the ExtensionService object.
     *
     * @return every-time the same ExtensionService object
     */
    public static ExtensionService getExtensionService() {
       final ExtensionService extensionService = MODELER_SESSION_INSTANCE.extensionsService;

        if(extensionService == null){
            LOG.error("extension service has not been set.");
            throw new NullPointerException("extension service is not available.");
        }

       return extensionService;
    }

    /**
     * Sets the ExtensionService object. It can be set only once. All next attempts to set it again will
     * have no effect.
     *
     * @param extensionService is the ExtensionService object
     */
    public static void setExtensionService(final ExtensionService extensionService) {
        if(MODELER_SESSION_INSTANCE.extensionsService == null){
            MODELER_SESSION_INSTANCE.extensionsService = extensionService;
        }
    }

    /**
     * Private static method to null all services. Used in automatic tests - JUnit tests to null services.
     * None is supposed to use this method for non-testing purposes.
     *
     * Tests use this method through reflection.
     */
    private static void nullServices(){
        MODELER_SESSION_INSTANCE.componentFactoryService = null;
        MODELER_SESSION_INSTANCE.userService = null;
        MODELER_SESSION_INSTANCE.actionControlService = null;
        MODELER_SESSION_INSTANCE.projectControlService = null;
        MODELER_SESSION_INSTANCE.menuService = null;
        MODELER_SESSION_INSTANCE.notationService = null;
        MODELER_SESSION_INSTANCE.toolBarControlService = null;
        MODELER_SESSION_INSTANCE.statusBarControlService = null;
        MODELER_SESSION_INSTANCE.extensionsService = null;
    }
}