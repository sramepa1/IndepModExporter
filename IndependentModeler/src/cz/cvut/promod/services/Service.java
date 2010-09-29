package cz.cvut.promod.services;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:20:15, 10.10.2009
 */

/**
 * Common interface for all services.
 *
 * IMPORTANT NOTE! All services and their methods are supposed NOT to be used in (or by) a different thread
 * that Event Dispatcher Thread or Main Thread (during application start - init() method of any plugin).
 * Even all resources provided by services (generally ModelerSession), e.g. all instances of a ValueModel
 * interface etc., are supposed to be used only in Event Dispatcher Thread or Main Thread (during application start).
 *
 * One can use following code fragment if is not convinced about the thread identity:
 *
 * if(SwingUtilities.isEventDispatchThread()){
 *      usage of a service or a it's resource
 * }
 *
 * One can always delegate any program sequence to the Event Dispatcher thread too. Use:
 * 1) SwingUtilities.invokeAndWait(Runnable doRun) or
 * 2) SwingUtilities.invokeLater(Runnable doRun) - generally preferable solution.
 *
 * Usage of services and their resources form another thread than Event Dispatcher Thread can cause a
 * unpredictable inconsistency of application. One is supposed to always avoid using services and their
 * resources out of the Event Dispatcher Thread!
 *
 * @see cz.cvut.promod.services.ModelerSession
 * @see javax.swing.SwingUtilities 
 */
public interface Service {

    /**
     * Is the common method for all services and is invoked by the IndependentModelerMain class
     * @see cz.cvut.promod.IndependentModelerMain
     *
     * This method is invoked during ProMod start to check availability and integrity of services.

     * @return true if no error has occurred during check, false otherwise
     */
    public boolean check();

}
