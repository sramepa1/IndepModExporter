package cz.cvut.promod.gui.support.utils;

import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockContext;
import com.jidesoft.swing.JideButton;

import java.util.*;
import java.util.List;

import org.apache.log4j.Logger;

import javax.swing.*;

import cz.cvut.promod.gui.Modeler;
import cz.cvut.promod.services.componentFactoryService.ComponentFactoryService;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:52:49, 12.10.2009
 */

/**
 * Notation specific data holder.
 */
public class NotationGuiHolder {

    private static final Logger LOG = Logger.getLogger(NotationGuiHolder.class);

    public static enum Position{
      LEFT, RIGHT, BOTTOM, TOP
    }

    private final String notationIdentifier;

    private Map<String, DockableFrameWrapper> rightItems;

    private Map<String, DockableFrameWrapper> leftItems;

    private Map<String, DockableFrameWrapper> bottomItems;

    private Map<String, DockableFrameWrapper> topItems;

    private NotationWorkspaceData notationWorkspace;

    private final DefaultDockingManager dockingManager;

    private final  Set<Position> takenPositions;

    public NotationGuiHolder( final String notationIdentifier, final DefaultDockingManager dockingManager){
        this.notationIdentifier = notationIdentifier;
        this.dockingManager = dockingManager;

        this.takenPositions = new HashSet<Position>();

        rightItems = new HashMap<String, DockableFrameWrapper>();
        leftItems = new HashMap<String, DockableFrameWrapper>();
        bottomItems = new HashMap<String, DockableFrameWrapper>();
        topItems = new HashMap<String, DockableFrameWrapper>();
    }

    public String getNotationIdentifier() {
        return notationIdentifier;
    }

    /**
     * Returns the required dockable frame position.
     * @param frameName the dockable frame name
     * @return the dockable frame or null if no such a dockable frame exists
     */
    public Position getPosition( final String frameName ){
      if( rightItems.containsKey(frameName) ){
        return Position.RIGHT;
      } else if( leftItems.containsKey(frameName) ){
        return Position.LEFT;
      } else if( bottomItems.containsKey(frameName) ){
        return Position.BOTTOM;
      } else if( topItems.containsKey(frameName) ){
          return Position.TOP;
      }

      return null;
    }

    /**
     * @return notation's workspace
     */
    public NotationWorkspaceData getNotationWorkspace(){
      return notationWorkspace;
    }

    /**
     * Sets the notation's workspace.
     * @param notationWorkspaceData the notation workspace
     */
    public void setNotationWorkspace( final NotationWorkspaceData notationWorkspaceData){
      notationWorkspace = notationWorkspaceData;
    }

    /**
     * Returns the dockable frame associated button.
     * @param dockableFrameName is the name of the dockable frame
     * @param position is the dockable frame position
     * @return the dockable frame associated button.
     */
    public JideButton getButton( final String dockableFrameName, final Position position ){
        return getItems(position).get(dockableFrameName).getButton();
    }

    /**
     * Returns all items from the specified position.
     * @param position is the specified position
     * @return map holding name and DockableFrameWrappers
     */
    public Map<String, DockableFrameWrapper> getItems(final Position position){
        final Map<String, DockableFrameWrapper> items;
        switch( position ){
            case RIGHT:
                items = rightItems;
                break;
            case LEFT:
                items = leftItems;
                break;
            case BOTTOM:
                items = bottomItems;
                break;
            case TOP:
                items = topItems;
                break;
            default:
                LOG.error("No such a dockable frame position.");
                throw new IllegalArgumentException("Wrong dockable frame position.");
        }

        return items;
    }

    /**
     * Returns allowed dockable frame initial state
     * @param initialState is the required initial state
     * @param position is the required position
     * @param dockableFrameName is the dockable frame name
     * @return the allowed state, no two different dockable frames are supposed to be docked and visible in a moment
     */
    private DockableFrameData.InitialState getAllowedInitialState(final DockableFrameData.InitialState initialState,
                                                                  final Position position,
                                                                  final String dockableFrameName) {
        if(initialState == null){
            return DockableFrameData.InitialState.HIDDEN;
        }

        if(DockableFrameData.InitialState.OPENED.equals(initialState)){
            if(takenPositions.contains(position)){
                LOG.warn("DockableFrame " + dockableFrameName +" has been set as HIDDEN.");
                return DockableFrameData.InitialState.HIDDEN;
            }

            takenPositions.add(position);
        }

        return initialState;
    }

    /**
     * Adds a new frame and it's associated button.
     *
     * @param dockableFrame is the dockable frame
     * @param button is the button
     * @param popupMenu is the button's popup menu
     * @param moveToLeftItem is the menu's item
     * @param moveToRightItem is the menu's item
     * @param moveToBottomItem is the menu's item
     * @param moveToTopItem is the menu's item
     * @param position is the dockable frame position
     * @param initialState is the dockable frame initial state
     */
    public void addFrameWithButton( final DockableFrame dockableFrame,
                                    final JideButton button,
                                    final JPopupMenu popupMenu,
                                    final JMenuItem moveToLeftItem,
                                    final JMenuItem moveToRightItem,
                                    final JMenuItem moveToBottomItem,
                                    final JMenuItem moveToTopItem,
                                    final Position position,
                                    DockableFrameData.InitialState initialState ){
        initialState = getAllowedInitialState(initialState, position, dockableFrame.getName());

        final Map<String, DockableFrameWrapper> items = getItems(position);

        switch( position ){
            case RIGHT:
                moveToRightItem.setEnabled(false);
                break;
            case LEFT:
                moveToLeftItem.setEnabled(false);
                break;
            case BOTTOM:
                moveToBottomItem.setEnabled(false);
                break;
            case TOP:
                moveToTopItem.setEnabled(false);
                break;
            default:
                throw new IllegalArgumentException("Wrong dockable frame position");
        }

        dockingManager.addFrame(dockableFrame);
        
        items.put(dockableFrame.getName(), new DockableFrameWrapper(dockableFrame,
                                                                            button,
                                                                            popupMenu,
                                                                            moveToLeftItem,
                                                                            moveToRightItem,
                                                                            moveToBottomItem,
                                                                            moveToTopItem,
                                                                            initialState,
                                                                            dockingManager));
    }

    /**
     * Returns the list of dockable frame from the positions specified.
     * @param position is the required position
     * @return the list of required frames
     */
    public List<DockableFrame> getListOfFrames( final Position position ){
        final List<DockableFrame> frames = new LinkedList<DockableFrame>();

        final Map<String, DockableFrameWrapper> items = getItems(position);

        final Collection<DockableFrameWrapper> wrappers = items.values();

        for( final DockableFrameWrapper wrapper : wrappers){
            frames.add(wrapper.getDockableFrame());
        }

        return frames;
    }

    /**
     * Returns the list of buttons from the positions specified.
     * @param position is the required position
     * @return the list of required buttons
     */
    public List<JideButton> getListOfButtons( final Position position ){
        final List<JideButton> buttons = new LinkedList<JideButton>();
        final Collection<DockableFrameWrapper> wrappers = getItems(position).values();

        for( final DockableFrameWrapper wrapper : wrappers){
            buttons.add(wrapper.getButton());
        }

        return buttons;
    }

    /**
     * Sets the dockable frame's button's movement possibilities.
     * @param frame is the dockable frame
     */
    public void setButtonsMovementOptions( final DockableFrame frame ){
        final Position position = getPosition(frame.getName());
        final DockableFrameWrapper wrapper = getItems(position).get(frame.getName());

        try{
            switch( position ){
                case LEFT:
                    setButtonsMovementOptions(wrapper, false, true, true, true);
                    break;
                case RIGHT:
                    setButtonsMovementOptions(wrapper, true, false, true, true);
                    break;
                case BOTTOM:
                    setButtonsMovementOptions(wrapper, true, true, false, true);
                    break;
                case TOP:
                    setButtonsMovementOptions(wrapper, true, true, true, false);
                    break;
                default:
                    LOG.error("No such a dockable frame position");
                    throw new IllegalArgumentException();
            }
        } catch(NullPointerException exception){
            LOG.error("Non existing DockableFrameWrapper in setButtonsMovementOptions( DockableFrame frame )", exception);
        } catch(IllegalArgumentException exception){
            LOG.error("Illegal position of DockableFrameWrapper in setButtonsMovementOptions( DockableFrame frame )", exception);
        }
    }

    /**
     * Update the dockable frame's button's movement possibilities.
     * @param wrapper is the wrapper of the dockable frame and related button
     * @param toLeft required state
     * @param toRight required state
     * @param toBottom required state
     * @param toTop required state
     * @throws NullPointerException when an item is missing
     */
    private void setButtonsMovementOptions( final DockableFrameWrapper wrapper,
                                            final boolean toLeft,
                                            final boolean toRight,
                                            final boolean toBottom,
                                            final boolean toTop) throws NullPointerException{
        if( wrapper.getDockableFrame().isFloated() ){
            wrapper.getMoveToLeftItem().setEnabled(false);
            wrapper.getMoveToRightItem().setEnabled(false);
            wrapper.getMoveToBottomItem().setEnabled(false);
            wrapper.getMoveToTopItem().setEnabled(false);
        } else{
            wrapper.getMoveToLeftItem().setEnabled(toLeft);
            wrapper.getMoveToRightItem().setEnabled(toRight);
            wrapper.getMoveToBottomItem().setEnabled(toBottom);
            wrapper.getMoveToTopItem().setEnabled(toTop);
        }
    }

    /**
     * Moves the dockable frame.
     * @param name is the name of the dockable frame
     * @param directionOfMovement is the direction of the movement
     * @throws IllegalArgumentException when the move is not possible
     */
    public void moveButtonAndFrame(final String name, final NotationGuiHolder.Position directionOfMovement) throws IllegalArgumentException{
        final Position position = getPosition(name);

        if( position == null ){
            throw new IllegalArgumentException();
        }

        // find the target map
        final Map<String, DockableFrameWrapper> items = getItems(directionOfMovement);

        //remove from current map
        final DockableFrameWrapper movingWrapper = getItems(position).remove(name);

        items.put(name, movingWrapper);

        moveFrameToAnotherSide(movingWrapper.getDockableFrame(), directionOfMovement);

        updateButtonOrientation(directionOfMovement, movingWrapper.getButton());
    }

    /**
     * Updates the button orientation.
     * @param directionOfMovement is the movement direction
     * @param button is the button to be updated
     */
    private void updateButtonOrientation(final Position directionOfMovement, final JideButton button) {
        switch(directionOfMovement){
            case LEFT:
            case RIGHT:
                button.setOrientation(ComponentFactoryService.JIDE_BUTTON_VERTICAL_ORIENTATION);
                break;
            case BOTTOM:
            case TOP: 
                button.setOrientation(ComponentFactoryService.JIDE_BUTTON_HORIZONTAL_ORIENTATION);
        }
    }

    /**
     * Performed the auxiliary movement operations.
     * @param frame is the frame supposed to be moved
     * @param items related dockable frame in form of their wrappers
     * @param direction is the direction of movement
     * @throws IllegalArgumentException when an error occurs
     */
    private void performFrameMovement( final DockableFrame frame,
                                       final Map<String, DockableFrameWrapper> items,
                                       final Position direction ) throws IllegalArgumentException{
        final Collection<DockableFrameWrapper> wrappers;
        final int side;

        switch (direction) {
            case RIGHT:
                side = DockContext.DOCK_SIDE_EAST;
                break;
            case LEFT:
                side = DockContext.DOCK_SIDE_WEST;
                break;
            case BOTTOM:
                side = DockContext.DOCK_SIDE_SOUTH;
                break;
            case TOP:
                side = DockContext.DOCK_SIDE_NORTH;
                break;
            default:
                throw new IllegalArgumentException();
        }

        if( frame.isAvailable() ){
            wrappers = items.values();
            for( final DockableFrameWrapper wrapper : wrappers){
              boolean floatedAndAvailable = frame.isAvailable();
                if( (wrapper.getDockableFrame() != frame) && !floatedAndAvailable ){
                    dockingManager.setFrameUnavailable(wrapper.getDockableFrame().getName());
                }
            }
            dockingManager.dockFrame(frame.getName(), side,
                                     Modeler.dockingFrameCounter++);
            dockingManager.setFrameAvailable(frame.getName());

        } else{
            final DockableFrameWrapper wrapper = items.get(frame.getName());

            wrapper.setHideOtherFrames(false);
            dockingManager.setFrameAvailable(frame.getName());
            wrapper.setHideOtherFrames(false);
            dockingManager.dockFrame(frame.getName(), side, Modeler.dockingFrameCounter++);
            dockingManager.setFrameUnavailable(frame.getName());
        }
    }

    /**
     * Starts the dockable frame movement
     * @param frame is the dockable frame to be moved
     * @param direction is the direction of movement
     */
    private void moveFrameToAnotherSide( final DockableFrame frame, final Position direction ){
        performFrameMovement(frame, getItems(direction), direction);
    }

    /**
     * Enables the frames and their buttons.
     */
    public void enable(){
      final Collection<DockableFrameWrapper> wrappers = getAllDockableFrameFacades();

      for( DockableFrameWrapper wrapper : wrappers){
        if(wrapper.isWasAvailable()){
            wrapper.setWasAvailable(false);
            wrapper.getButton().doClick();
        }
      }
    }

    /**
     * Unables the frames and their buttons.
     */
    public void unable(){
      final Collection<DockableFrameWrapper> wrappers = getAllDockableFrameFacades();

      for( final DockableFrameWrapper wrapper : wrappers){
        if( wrapper.isWasAvailable() && !wrapper.getDockableFrame().isAvailable() ){
          wrapper.setWasAvailable(false);
        }

        if( wrapper.getDockableFrame().isAvailable() ){
          wrapper.getButton().doClick();
          wrapper.setWasAvailable(true);
        }
      }
    }

    /**
     * @param frameName is the frame name
     * @param position is the frame position
     * @return true if all other frames are supposed to be hidden
     * @throws IllegalArgumentException when not such a item exists 
     */
    public boolean isHideOtherFrames( final String frameName, final Position position ) throws IllegalArgumentException{
        return getItems(position).get(frameName).isHideOtherFrames();
    }

    /**
     * @return all wrappers representing dockable frames of a notations and it's modules
     */
    public Set<DockableFrameWrapper> getAllDockableFrameFacades(){
        final Set<DockableFrameWrapper> wrappers = new HashSet<DockableFrameWrapper>();
        wrappers.addAll(rightItems.values());
        wrappers.addAll(leftItems.values());
        wrappers.addAll((bottomItems.values()));
        wrappers.addAll(topItems.values());

        return wrappers;
    }

    /**
     * @param frameName is the name of the dockable frame
     * @return the required facade representing the dockable frame
     */
    public DockableFrameWrapper getDockableFrameFacade( final String frameName ){
        if( leftItems.containsKey(frameName) ){
            return leftItems.get(frameName);
        } else if (rightItems.containsKey(frameName)){
            return rightItems.get(frameName);
        } else if(bottomItems.containsKey(frameName)){
            return bottomItems.get(frameName);
        } else if(topItems.containsKey(frameName)){
            return topItems.get(frameName);
        }

      return null;
    }

}