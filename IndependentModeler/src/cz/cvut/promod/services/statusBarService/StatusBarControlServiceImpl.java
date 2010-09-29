package cz.cvut.promod.services.statusBarService;

import com.jidesoft.status.StatusBar;
import com.jidesoft.status.StatusBarItem;
import com.jidesoft.swing.JideBoxLayout;

import java.util.Map;
import java.util.HashMap;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.statusBarService.utils.InsertStatusBarItemResult;
import cz.cvut.promod.gui.ModelerModel;
import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 14:54:23, 21.1.2010
 */

/**
 * StatusBarControlService implementation.
 */
public class StatusBarControlServiceImpl implements StatusBarControlService{

    private static final Logger LOG = Logger.getLogger(StatusBarControlServiceImpl.class);

    private final Map<String, StatusBar> statusBarMap;


    /**
     *  Constructs a new StatusBarControlServiceImpl instance.
     */
    public StatusBarControlServiceImpl() {
        statusBarMap = new HashMap<String, StatusBar>();
    }

     /** {@inheritDoc} */
    public StatusBar getStatusBar(final String notationIdentifier) {
         if(notationIdentifier == null){
             return null;
         }

        if(!ModelerSession.getNotationService().existNotation(notationIdentifier)
                && !notationIdentifier.equals(ModelerModel.MODELER_IDENTIFIER)){
            return null;
        }

        if(statusBarMap.containsKey(notationIdentifier)){
            return statusBarMap.get(notationIdentifier);
        }

        final StatusBar statusBar = ModelerSession.getComponentFactoryService().createStatusBar();
        statusBarMap.put(notationIdentifier, statusBar);

        return statusBar;        
    }

    /** {@inheritDoc} */
    public InsertStatusBarItemResult addStatusBarItem(final String notationIdentifier,
                                                      final StatusBarItem statusBarItem,
                                                      String itemLayout) {

        if(!ModelerSession.getNotationService().existNotation(notationIdentifier)
                && !ModelerModel.MODELER_IDENTIFIER.equals(notationIdentifier)){
            LOG.error("Not possible to insert action to the tool bar of not existing notation.");
            return InsertStatusBarItemResult.INVALID_NOTATION;
        }

        if(statusBarItem == null){
            LOG.error("Invalid status bar item to be inserted.");
            return InsertStatusBarItemResult.INVALID_STATUS_BAR_ITEM;            
        }

        if(itemLayout == null){
            LOG.info("Setting status bar layout to implicit value");
            itemLayout = JideBoxLayout.FIX;
        }

        final StatusBar statusBar = getStatusBar(notationIdentifier);

        if(!(itemLayout.equals(JideBoxLayout.FIX)
                || itemLayout.equals(JideBoxLayout.FLEXIBLE)
                || itemLayout.equals(JideBoxLayout.VARY))){

                itemLayout = JideBoxLayout.FIX;
        }

        statusBar.add(statusBarItem, itemLayout);

        return InsertStatusBarItemResult.SUCCESS;
    }

    /** {@inheritDoc} */
    public boolean check() {
        return true;
    }
    
}
