package cz.cvut.promod.gui.support.utils;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.swing.JideButton;

import javax.swing.*;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 2:17:17, 20.10.2009
 */

/**
 * Data holder for a single dockable frame.
 */
public class DockableFrameWrapper {

    private final DockingManager dockingManager;

    private final DockableFrame dockableFrame;
    private final JideButton button;

    private JPopupMenu popupMenu = null;
    private JMenuItem moveToLeftItem = null;
    private JMenuItem moveToRightItem = null;
    private JMenuItem moveToBottomItem = null;
    private JMenuItem moveToTopItem = null;
    
    private boolean hideOtherFrames = true;
    private boolean wasAvailable;

   private DockableFrameData.InitialState initialState;

    public DockableFrameWrapper( final DockableFrame dockingFrame,
                              final JideButton button,
                              final JPopupMenu popupMenu,
                              final JMenuItem moveToLeftItem,
                              final JMenuItem moveToRightItem,
                              final JMenuItem moveToBottomItem,
                              final JMenuItem moveToTopItem,
                              final DockableFrameData.InitialState initialState,
                              final DockingManager dockingManager){

        this.initialState = initialState;

        this.dockingManager = dockingManager;

        this.dockableFrame = dockingFrame;
        this.button = button;
        wasAvailable = false;
        
        this.popupMenu = popupMenu;
        this.moveToLeftItem = moveToLeftItem;
        this.moveToRightItem = moveToRightItem;
        this.moveToBottomItem = moveToBottomItem;
        this.moveToTopItem = moveToTopItem;

        if( initialState == DockableFrameData.InitialState.OPENED){
            setWasAvailable(true);
        }
  }

    /**
     * @return the frame initial state
     */
    public DockableFrameData.InitialState getInitialState() {
        DockableFrameData.InitialState a = initialState;
        initialState = null;
        return a;
    }

    /**
     * @return left menu item
     */
    public JMenuItem getMoveToLeftItem(){
        return moveToLeftItem;
    }

    /**
     * @return right menu item
     */
    public JMenuItem getMoveToRightItem(){
        return moveToRightItem;
    }

    public JMenuItem getMoveToBottomItem() {
        return moveToBottomItem;
    }

    /**
     * @return top menu item
     */
    public JMenuItem getMoveToTopItem() {
        return moveToTopItem;
    }

    /**
     * @return the dockable frame itself
     */
    public DockableFrame getDockableFrame(){
    return dockableFrame;
  }

    /**
    * @return the associated button for visibility controlling
    */
    public JideButton getButton(){
        return button;
    }

    /**
     * @return associated button popup menu
     */
    public JPopupMenu getPopupMenu(){
        return popupMenu;
    }

    /**
     * @return true if the dockable frame was visible before context switch, false otherwise
     */
    public boolean isWasAvailable(){
        return wasAvailable;
    }

    /**
     * Sets the wasAvailable flag
     *
     * @param wasAvailable true if the is supposed to be true, false otherwise
     */
    public void setWasAvailable( boolean wasAvailable ){
        this.wasAvailable = wasAvailable;
    }

    /**
     * @return true if all other frames are supposed to be hidden
     */
     public boolean isHideOtherFrames(){
        boolean returnValue = hideOtherFrames;
        hideOtherFrames = true;
        return returnValue;
    }

    /**
     * Sets the hideOtherFrames flag.
     * @param notHideOtherFrames new value for hideOtherFrames flag
     */
    public void setHideOtherFrames( boolean notHideOtherFrames ){
        this.hideOtherFrames = notHideOtherFrames;
    }
}

