package cz.cvut.promod.gui.listeners;

import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.swing.JideButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import cz.cvut.promod.gui.support.utils.NotationGuiHolder;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:38:43, 19.10.2009
 *
 * Implementation of an ActionListener that reacts on side panel buttons clicks. It set the
 * corresponding DockableFrame available.
 */
public class SideButtonListener implements ActionListener {

    private final DefaultDockingManager dockingManager;
    private final NotationGuiHolder specification;
    private final DockableFrame dockableFrame;

    public SideButtonListener( final NotationGuiHolder specification,
                               final DefaultDockingManager dockingManager,
                               final DockableFrame frame ){

        this.dockingManager = dockingManager;
        this.dockableFrame = frame;
        this.specification = specification;
    }

    public void actionPerformed( ActionEvent e ){
        final List<DockableFrame> dockableFrameList;
        final NotationGuiHolder.Position position = specification.getPosition(dockableFrame.getName());

        dockableFrameList = specification.getListOfFrames(position);

        if( dockableFrame.isHidden()){
            dockingManager.setFrameAvailable(dockableFrame.getName());
            dockingManager.showFrame(dockableFrame.getName());
            dockingManager.setFrameUnavailable(dockableFrame.getName());
        }

        if( !dockableFrame.isFloated() ){
            for( DockableFrame frame : dockableFrameList ){
                if( (!frame.isFloated()) && (frame != dockableFrame) ){
                    dockingManager.setFrameUnavailable(frame.getName());
                    final JideButton but = specification.getButton(frame.getName(), position);
                    but.setSelected(false);
                }
            }
        }

        if( dockableFrame.isAvailable() ){
            dockingManager.setFrameUnavailable(dockableFrame.getName());
            specification.getDockableFrameFacade(dockableFrame.getName()).setWasAvailable(false);
        } else{
            dockingManager.setFrameAvailable(dockableFrame.getName());
        }
    }
    
}