package cz.cvut.promod.services.menuService;

import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ondra
 * Date: 22.8.2010
 * Time: 12:39:09
 * To change this template use File | Settings | File Templates.
 */
public class MenuValidator {
   private final Logger LOG = Logger.getLogger(MenuControlServiceImpl.class);

   protected boolean hasCorrectParentMenu(JMenu parentMenu, JMenuBar parentMenuBar, JPopupMenu parentPopupMenu) {
    if((parentMenu != null && parentMenuBar != null)
            || (parentMenu != null && parentPopupMenu != null)
            || (parentMenuBar != null && parentPopupMenu != null)){
        LOG.error("Obscurity in parent of menu item");
        return false;

    }
    return true;
    }

   protected boolean isItemLocationValid(String[] placementParts) {
        if(placementParts.length < 1){
            LOG.error("Illegal menu item location.");
            return false;
        }
        return true;
    }

   protected boolean isPopupSupported(String notationIdentifier, JPopupMenu popupMenu) {
        if(popupMenu == null){
            LOG.error("Notation (" + notationIdentifier +") doesn't support popup menu.");
            return false;
        }
        return true;
    }

   protected boolean itemHasOnlyOneParent(JMenu parentMenu, JPopupMenu parentPopupMenu) {
        if(parentMenu != null && parentPopupMenu != null){
            LOG.error("Obscurity in menu item's parent. - There can be just one parent menu item for new menu item insertion.");
            return false;
        }
        return true;
    }

   protected boolean isActionDefined(ProModAction proModAction) {
       if(proModAction == null){
            LOG.error("Nullary action to be inserted.");
            return false;
       }
       if(proModAction.getValue(Action.NAME) == null || ((String)proModAction.getValue(Action.NAME)).isEmpty()){
           LOG.error("Action with no name to be inserted");
           return false;
       }
       return true;
    }

   protected boolean isMenuItemPositionSet(MenuItemPosition menuItemPosition) {
        if(menuItemPosition == null || menuItemPosition.getHierarchicalPlacement() == null){
            LOG.error("Nullary MenuItemPosition info.");
            return false;
        }
        return true;
    }

    /**
     * Checks the menu item placement parts not to be null or an empty strings.
     *
     * @param placementParts is an array representing hierarchical menu item placement
     * @return true if all the placement parts are correct, false otherwise
     */
   protected boolean areValidPlacementParts(final String[] placementParts) {
        for(final String placement : placementParts){
            if(placement == null || placement.isEmpty()){
               LOG.error("Invalid any/all part(s) of MenuItemPosition info.");
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether no the same action has been already inserted under the same menu parent.
     *
     * @param parentMenu is to menu parent
     * @param parentPopupMenu is the popup menu parent
     * @param proModAction is the action supposed to be inserted
     * @return true if there is no the same action under the parent, false otherwise
     */
   protected boolean isUniqueAction(final JMenu parentMenu,
                                   final JPopupMenu parentPopupMenu,
                                   final ProModAction proModAction) {

        final Component[] menuComponets;
        if(parentMenu != null){
            menuComponets = parentMenu.getMenuComponents();
        } else if(parentPopupMenu != null){
            menuComponets = parentPopupMenu.getComponents();
        } else {
            LOG.error("Parent of menu item cannot be determined.");
            return false;
        }

        for(final Component component : menuComponets){
            if(component instanceof JMenuItem){
                final JMenuItem menuItem = (JMenuItem) component;
                final Action action = menuItem.getAction();

                if(action != null && action == proModAction){
                    LOG.error("Inserting the same action to the one menu more than once.");
                    return false;
                }
            }
        }


        return true;
    }

    /**
        * This function protect to add  MenuSeparator next to an other MenuSeparator
        *
        * @param menuComponents menu items
        * @param insertionPosition required menu item position
        * @param menuSeparator menu separator info if one is supposed to be insert
        * @return true if there could be the separator added
        */
   protected boolean isSeparatorAlreadyDefinedAtPosition(final Component[] menuComponents,
                                                         final int insertionPosition,
                                                         final MenuService.MenuSeparator menuSeparator) {
           if(menuSeparator != null){
              if (isSeparatorAtPosition(menuComponents, insertionPosition)) return true;
              switch(menuSeparator){
                   case AFTER:
                       if (isSeparatorAtPosition(menuComponents, insertionPosition + 1)) return true;
                       break;
                   case BEFORE:
                       if (isSeparatorAtPosition(menuComponents, insertionPosition - 1)) return true;
                       break;
                   default:
                       return false;
               }
           }

           return false;
       }

   private boolean isSeparatorAtPosition(Component[] menuComponents, int insertionPosition) {
           if(isValidMenuPosition(insertionPosition, menuComponents.length)){
             if(menuComponents[insertionPosition] instanceof JPopupMenu.Separator){
                 return true;
             }
         }
           return false;
       }

    /**
    * @param position menu position
    * @param menuItems menu items
    * @return true if the given position is valid
    */
    protected boolean isValidMenuPosition(final int position, final int menuItems){
           return !(0 > position || position > (menuItems - 1));
    }
}
