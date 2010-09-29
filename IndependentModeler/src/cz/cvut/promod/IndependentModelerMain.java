package cz.cvut.promod;

import org.apache.log4j.*;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryServiceImpl;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.extensionService.ExtensionService;
import cz.cvut.promod.services.extensionService.ExtensionServiceImpl;
import cz.cvut.promod.services.statusBarService.StatusBarControlService;
import cz.cvut.promod.services.statusBarService.StatusBarControlServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.PluginLoaderService;
import cz.cvut.promod.services.pluginLoaderService.PluginLoaderServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.toolBarService.ToolBarControlServiceImpl;
import cz.cvut.promod.services.toolBarService.ToolBarControlService;
import cz.cvut.promod.services.projectService.ProjectControlService;
import cz.cvut.promod.services.projectService.ProjectControlServiceImpl;
import cz.cvut.promod.services.notationService.NotationService;
import cz.cvut.promod.services.notationService.NotationServiceImpl;
import cz.cvut.promod.services.menuService.MenuService;
import cz.cvut.promod.services.menuService.MenuControlServiceImpl;
import cz.cvut.promod.services.actionService.ActionControlService;
import cz.cvut.promod.services.actionService.ActionControlServiceImpl;
import cz.cvut.promod.services.userService.UserService;
import cz.cvut.promod.services.userService.UserServiceImpl;
import cz.cvut.promod.gui.Modeler;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.resources.TranslationCheck;

import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:08:53, 10.10.2009
 */

/**
 * Application Main Class.
 */
public class IndependentModelerMain {

    private static final Logger LOG = Logger.getLogger(IndependentModelerMain.class);

    /**
     * Path to the config directory
     */
    private static final String CONFIG_DIR = "." + System.getProperty("file.separator") + "config"
            + System.getProperty("file.separator");

    /**
     * Plugin configuration file
     */
    private static final String LOG_INIT_FILE = CONFIG_DIR + "log4j.config";

    /**
     * Common Resource Bundle file
     */
    public static final String COMMON_RESOURCES_FILE = CONFIG_DIR + "common.properties";

    public final static String PLUGINS_PROPERTY_FILE = "plugins.xml";

    /**
     * Defines constant for application fall, some service(s) not available
     */
    public static final int SERVICE_NULL_EXIT = 1;

    /**
     * Defines constant for application fall, some service(s) not valid
     */
    public static final int SERVICE_CHECK_FAIL_EXIT = 2;

    private static PluginLoaderService pluginLoaderService;


    public static void main(String[] args) {

        verifyJidesoftLicense();

        initLog();

        LOG.info("ProMod application is starting.");

        logPlatformInformation();

        initIndependentServices();

        initResources();

        loadPlugIns();

        initDependentServices(pluginLoaderService);

        checkServices();

        // init modeler
        final Modeler modeler = new Modeler();

        initPlugins();

        modeler.deployNotationSpecificPlugIns();

        modeler.initSettingsDialog();

        modeler.showFrame();
    }

    /**
     * Performs JideSoft license verification
     */
    private static void verifyJidesoftLicense() {

        // TODO: read license from some external resource
        com.jidesoft.utils.Lm.verifyLicense("Lukas Vyhlidka", "Independent Modeler",
                "IVvcwhRLbFZFiGm1aBSocn9LRYt:SDR2");

    }

    /**
     * Initialize logging mechanism.
     */
    private static void initLog() {
        PropertyConfigurator.configure(LOG_INIT_FILE);
    }

    /**
     * Logs basic platform information.
     * <p/>
     * Information logged includes:
     * <ul>
     * <li>user name of current system user</li>
     * <li>operating system name and version</li>
     * <li>java language version</li>
     * <li>java implementation vendor</li>
     * <li>java home variable</li>
     * <li>system default locale settings</li>
     * </ul>
     */
    private static void logPlatformInformation() {
        LOG.info("user name: " + System.getProperty("user.name"));
        LOG.info("os name, version: " + System.getProperty("os.name") + ", " + System.getProperty("os.version"));
        LOG.info("java version: " + System.getProperty("java.version"));
        LOG.info("java vendor: " + System.getProperty("java.vendor") + ", " + System.getProperty("java.vendor.url"));
        LOG.info("java home: " + System.getProperty("java.home"));
        LOG.info("default locale: " + System.getProperty("user.language"));
    }

    /**
     * Independent services are all services that can any plugin use during it's instantiation time. Usage of such a
     * service during plugin's instantiation time does NOT affect any run of application.
     */
    private static void initIndependentServices() {

        // init ComponentFactoryService
        final ComponentFactoryService componentFactoryService = new ComponentFactoryServiceImpl();
        ModelerSession.setComponentFactoryService(componentFactoryService);
    }

    /**
     * Initialize common resources.
     */
    private static void initResources() {

        try {
            final ResourceBundle commonResources = new PropertyResourceBundle(
                    new FileReader(new File(COMMON_RESOURCES_FILE)));
            
            ModelerSession.setCommonResourceBundle(commonResources);
        } catch (IOException e) {
            LOG.error("Common resource bundle have NOT been found.", e);
            System.exit(-1);
        }

        if (!TranslationCheck.validateTranslations()) {
            System.exit(-2);
        }
    }

    /**
     * Loads plugins.
     */
    private static void loadPlugIns() {
        try {
            pluginLoaderService = new PluginLoaderServiceImpl();

            if (pluginLoaderService.loadPlugInsDefinition(PLUGINS_PROPERTY_FILE)) {
                pluginLoaderService.instantiatePlugins();
            }

        } catch (ClassNotFoundException exception) {
            LOG.error("Plugin interface(-s) couldn't be properly loaded.", exception);
            System.exit(2);
        }
    }

    /**
     * Depending services are instantiated after all plugins have been loaded. This is done so, because no plugin can
     * use dependent service in it's constructor (generally during the whole instantiation time). ProMod asks the
     * plugin for it's identification after this plugin has been instantiated (there is no way to do this before),
     * and then, when the plugin is instantiated, can ProMod determines about the plugin's identification
     * uniqueness.
     * <p/>
     * So all dependent services are nullary before and during plugin's instantiation time to prevent this kind of
     * plugin's instantiation programming style.
     * <p/>
     * Plugin's are supposed to use services in it's init method.
     *
     * @param pluginLoaderService is loading services
     * @see cz.cvut.promod.plugin.Plugin
     */
    private static void initDependentServices(final PluginLoaderService pluginLoaderService) {

        // init UserService
        final UserService userService = new UserServiceImpl();
        ModelerSession.setUserService(userService);

        // init ActionControlService
        final ActionControlService actionControlService = new ActionControlServiceImpl();
        ModelerSession.setActionControlService(actionControlService);

        // init MenuService
        final MenuService menuService = new MenuControlServiceImpl();
        ModelerSession.setMainMenuService(menuService);

        // init NotationService
        final NotationService notationService = new NotationServiceImpl(
                pluginLoaderService.getNotationSpecificPlugins(),
                pluginLoaderService.getErrors());
        ModelerSession.setNotationService(notationService);

        // init ExtensionService
        final ExtensionService extensionService = new ExtensionServiceImpl(pluginLoaderService.getExtensions());
        ModelerSession.setExtensionService(extensionService);

        // init ProjectControlService
        final ProjectControlService projectControlService = new ProjectControlServiceImpl();
        ModelerSession.setProjectControlService(projectControlService);

        // init ToolBarService
        final ToolBarControlService toolBaControlService = new ToolBarControlServiceImpl();
        ModelerSession.setToolBarControlService(toolBaControlService);

        // init StatusBarService
        final StatusBarControlService statusBarControlService = new StatusBarControlServiceImpl();
        ModelerSession.setStatusBarControlService(statusBarControlService);
    }

    /**
     * Check all provided services before initialization of plugins is invoked.
     */
    private static void checkServices() {
        boolean result = true;

        try {
            result &= ModelerSession.getActionService().check();
            result &= ModelerSession.getActionControlService().check();

            result &= ModelerSession.getUserService().check();

            result &= ModelerSession.getMenuService().check();

            result &= ModelerSession.getComponentFactoryService().check();

            result &= ModelerSession.getNotationService().check();

            result &= ModelerSession.getExtensionService().check();

            result &= ModelerSession.getProjectService().check();
            result &= ModelerSession.getProjectControlService().check();

            result &= ModelerSession.getToolBarService().check();
            result &= ModelerSession.getToolBarControlService().check();

        } catch (NullPointerException exception) {
            LOG.error("Shutting down. Some service(s) is/are not available.", exception);
            System.exit(SERVICE_NULL_EXIT);
        }

        if (!result) {
            LOG.error("Shutting down. Check() method in some service(s) has failed.");
            System.exit(SERVICE_CHECK_FAIL_EXIT);
        }
    }

    /**
     * Performs plugin initialization.
     */
    private static void initPlugins() {

        // notations & modules initializations
        for (final String notationIdentifier : ModelerSession.getNotationService().getNotationsIdentifiers()) {

            final NotationSpecificPlugins notationSpecificPlugins = ModelerSession.getNotationService()
                    .getNotationSpecificPlugins(notationIdentifier);

            notationSpecificPlugins.getNotation().init();

            for (final Module module : notationSpecificPlugins.getModules()) {
                module.init();
            }
        }

        // extensions initializations
        for (final Extension extension : ModelerSession.getExtensionService().getExtensions()) {
            extension.init();
        }
    }

}
