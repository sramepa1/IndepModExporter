package cz.cvut.promod;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import cz.cvut.promod.services.actionService.TestActionServiceImpl;
import cz.cvut.promod.services.toolBarService.TestToolBarControlServiceImpl;
import cz.cvut.promod.services.statusBarService.TestStatusBarControlServiceImpl;
import cz.cvut.promod.services.pluginLoaderService.TestPluginLoaderServiceImpl;
import cz.cvut.promod.services.projectService.*;
import cz.cvut.promod.services.notationService.TestNotationServiceImpl;
import cz.cvut.promod.services.extensionService.TestExtensionServiceImpl;
import cz.cvut.promod.services.menuService.TestMenuServiceImpl;
import cz.cvut.promod.services.userService.TestUserService;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:03:47, 18.4.2010
 *
 * Runs all JUnit tests.
 *
 * never run test from an medium, where the test is not allowed to write data (e.g. CD).
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestActionServiceImpl.class,
        TestToolBarControlServiceImpl.class,
        TestStatusBarControlServiceImpl.class,
        TestPluginLoaderServiceImpl.class,
        TestProjectControlServiceImplAddDiagram.class,
        TestProjectControlServiceImplAddSubfolder.class,
        TestProjectControlServiceImplAddProject.class,
        TestProjectService.class,
        TestIOOperations.class,
        TestSyncFromPN.class,
        TestSyncFromFS.class,
        TestNotationServiceImpl.class,
        TestExtensionServiceImpl.class,
        TestChangeDispatcherMechanism.class,
        TestMenuServiceImpl.class,
        TestUserService.class
})
public class AllTests {}
