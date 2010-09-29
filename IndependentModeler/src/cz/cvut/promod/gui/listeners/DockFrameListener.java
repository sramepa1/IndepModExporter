package cz.cvut.promod.gui.listeners;

import com.jidesoft.docking.event.DockableFrameAdapter;
import com.jidesoft.docking.event.DockableFrameEvent;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.swing.JideButton;
import cz.cvut.promod.gui.support.utils.NotationGuiHolder;
import cz.cvut.promod.gui.support.utils.DockableFrameWrapper;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:20:52, 19.10.2009
 *
 * Controls the dockable frame visibility on basis of their events.
 */
public class DockFrameListener extends DockableFrameAdapter {

    private final Logger LOG = Logger.getLogger(DockFrameListener.class);

    private final DefaultDockingManager dockingManager;
    private final NotationGuiHolder specification; 

    public DockFrameListener( final NotationGuiHolder specification,
                              final DefaultDockingManager dockingManager ){
        this.dockingManager = dockingManager;
        this.specification = specification;
    }

    /**
     * Hides the dockable frame when user clicks the cross button.
     *
     * @param dockableFrameEvent is the dockable frame event
     */
    @Override
    public void dockableFrameHidden(DockableFrameEvent dockableFrameEvent) {        
        final DockableFrame dockableFrame = (DockableFrame)dockableFrameEvent.getSource();
        final String dockableFrameName = dockableFrame.getName();
        final DockableFrameWrapper facade = specification.getDockableFrameFacade(dockableFrameName);
        
        dockingManager.setFrameAvailable(dockableFrame.getName());
        dockingManager.showFrame(dockableFrame.getName());
        dockingManager.setFrameUnavailable(dockableFrame.getName());
        facade.setWasAvailable(false);
        facade.getButton().setSelected(false);
    }

    /**
    * Main purpose of this is to hide other DockableFrames at the time the particular DockableFrame
    * is docked (set available).
    *
    * @param dockableFrameEvent source.
    */
    @Override
    public void dockableFrameDocked( final DockableFrameEvent dockableFrameEvent ){
        final DockableFrame dockableFrame = (DockableFrame)dockableFrameEvent.getSource();
        NotationGuiHolder.Position position = specification.getPosition(dockableFrame.getName());

        try{
            if( specification.isHideOtherFrames(dockableFrame.getName(), position) ){
                final List<DockableFrame> dockableFrameList = specification.getListOfFrames(position);

                if( !dockableFrame.isFloated() ){
                for( DockableFrame frame : dockableFrameList ){
                    if( (!frame.isFloated()) && (frame != dockableFrame) && (dockableFrame.isAvailable()) ){
                        dockingManager.setFrameUnavailable(frame.getName());
                        final JideButton but = specification.getButton(frame.getName(), position);
                        but.setSelected(false);
                    }
                }
            }
        }
        specification.setButtonsMovementOptions(dockableFrame);

        } catch(IllegalArgumentException exception){
            LOG.error("Illegal position or unknown frame during frame docking.", exception);
        }
    }

    /**
    * Hides the possibility to move button to the another side in the button's context menu when
    * the DockableFrame appears in the floating state.
    *
    * @param dockableFrameEvent source.
    */
    @Override
    public void dockableFrameFloating( DockableFrameEvent dockableFrameEvent ){
        final DockableFrame dockableFrame = (DockableFrame)dockableFrameEvent.getSource();
        specification.setButtonsMovementOptions(dockableFrame);

        specification.getDockableFrameFacade(dockableFrame.getName()).setWasAvailable(true);
    }
}

