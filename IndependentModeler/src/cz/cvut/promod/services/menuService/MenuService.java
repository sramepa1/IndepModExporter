package cz.cvut.promod.services.menuService;

import cz.cvut.promod.services.Service;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import cz.cvut.promod.services.menuService.utils.MenuItemPosition;
import cz.cvut.promod.services.menuService.utils.InsertMenuItemResult;


import javax.swing.*;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:45:27, 10.10.2009
 */

/**
 * Menu Service allows developer to insert items into the main menu, project tree navigation popup menu
 *  or notation specific menus. 
 */
public interface MenuService extends Service{

    // Separator for hierarchical placement of menu items, e.g. "File;Export;Image"  
    public static final String PLACEMENT_DELIMITER = ";";


    // Determines whether a menu separator is supposed to be inserted (before or after a menu item) or not.
    public static enum MenuSeparator {
        AFTER, // insert menu separator before the item
        BEFORE, // insert menu separator after the item
        NONE // do not insert menu separator
    }

    /**
     * Inserts a new action to the main menu structure. 
     *
     * @param proModAction is the action that will be triggered by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. Hierarchical position cannot be null or an empty string for main menu.
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @param menuSeparator determines whether a menu separator is supposed to be inserted after or before the menu
     * item or not
     *
     * @param checkable if true, than the menu item is designated to be selectable (has a small checkbox)
     * 
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertMainMenuItem(final ProModAction proModAction,
                                                   final MenuItemPosition menuItemPosition,
                                                   final MenuSeparator menuSeparator,
                                                   final boolean checkable);

    /**
     * Inserts a new action to the main menu structure. This menu item won't be checkable (does not have the little
     * checkbox on it).
     *
     * @param proModAction is the action that will be triggered by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. Hierarchical position cannot be null or an empty string for main menu.
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @param menuSeparator determines whether a menu separator is supposed to be inserted after or before the menu
     * item or not
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */ 
    public InsertMenuItemResult insertMainMenuItem(final ProModAction proModAction,
                                                   final MenuItemPosition menuItemPosition,
                                                   final MenuSeparator menuSeparator);

    /**
     * Inserts a new action to the main menu structure without any menu separator.
     *
     * @param proModAction is the action that will be triggered by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. Hierarchical position cannot be null or an empty string for main menu.
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @param checkable if true, than the menu item is designated to be selectable (has a small checkbox)
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertMainMenuItem(final ProModAction proModAction,
                                                   final MenuItemPosition menuItemPosition,
                                                   final boolean checkable);

    /**
     * Inserts a new action to the main menu structure without any menu separator.
     * This menu item won't be checkable (does not have the little checkbox on it).
     *
     * @param proModAction is the action that will be triggered by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. Hierarchical position cannot be null or an empty string for main menu.
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertMainMenuItem(final ProModAction proModAction,
                                                   final MenuItemPosition menuItemPosition);

    
    /**
     * Inserts new action to the project navigation tree popup menu.
     * This menu is invoked anytime the right mouse button is clicked on any items in the project navigation tree.
     *
     * @param proModAction that will be triggered when by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. An empty string in hierarchical position means that the menu item will be inserted
     * into the root of the popup menu (menu item that are immediately visible when the popup menu is shown).
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @param menuSeparator insert any possible value (see InsertMenuSeparator enum) or null
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertProjectNavigationPopupMenuItem(final ProModAction proModAction,
                                                                     final MenuItemPosition menuItemPosition,
                                                                     final MenuSeparator menuSeparator);

    /**
     * Inserts new action to the project navigation tree popup menu without any menu separator.
     * This menu is invoked anytime the right mouse button is clicked on any items in the project navigation tree.
     *
     * @param proModAction that will be triggered when by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. An empty string in hierarchical position means that the menu item will be inserted
     * into the root of the popup menu (menu item that are immediately visible when the popup menu is shown).
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertProjectNavigationPopupMenuItem(final ProModAction proModAction,
                                                                     final MenuItemPosition menuItemPosition);


    /**
     * Inserts new action to the notation specific popup menu.
     * Not all notations support the popup menu feature.
     *
     * @param notationIdentifier is the identifier of notation to which popup menu is the menu item (action)
     * supposed to be inserted. When one specifies the identifier of Modeler, the menu item will be the inserted
     * into the project navigation tree popup menu.
     *
     * @param proModAction that will be triggered when by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. An empty string in hierarchical position means that the menu item will be inserted
     * into the root of the popup menu (menu item that are immediately visible when the popup menu is shown).
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @param menuSeparator insert any possible value (see InsertMenuSeparator enum) or null
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertPopupMenuItem(final String notationIdentifier,
                                                    final ProModAction proModAction,
                                                    final MenuItemPosition menuItemPosition,
                                                    final MenuSeparator menuSeparator);

    /**
     * Inserts new action to the notation specific popup menu without any menu separator.
     * Not all notations support the popup menu feature.
     *
     * @param notationIdentifier is the identifier of notation to which popup menu is the menu item (action)
     * supposed to be inserted. When one specifies the identifier of Modeler, the menu item will be the inserted
     * into the project navigation tree popup menu.
     *
     * @param proModAction that will be triggered when by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. An empty string in hierarchical position means that the menu item will be inserted
     * into the root of the popup menu (menu item that are immediately visible when the popup menu is shown).
     *
     * @see cz.cvut.promod.services.menuService.utils.MenuItemPosition
     *
     * @return an value from InsertMenuItemResults enumeration
     *
     * @see cz.cvut.promod.services.menuService.utils.InsertMenuItemResult for more details about return values
     * and their meanings
     */
    public InsertMenuItemResult insertPopupMenuItem(final String notationIdentifier,
                                                    final ProModAction proModAction,
                                                    final MenuItemPosition menuItemPosition);
    /*
     * Inserts a new action into given popup menu. This function can be used by notation using JIDE to handle their
     *  popup menus.
     *
     * @param parentMenu  is the parent menu item of added action
     *
     * @param parentPopupMenu the popup menu to be extended by defined action
     *
     * @param proModAction that will be triggered when by the menu item, the action has to be registered by the
     * ActionService before, promod action cannot have an empty string as it's name (text).
     *
     * @param menuItemPosition is an instance of MenuItemPosition specifying hierarchical, absolute or relative position
     * of the menu item in menu. An empty string in hierarchical position means that the menu item will be inserted
     * into the root of the popup menu (menu item that are immediately visible when the popup menu is shown).
     *
     * @param checkable  determines whether the menu item will be checkable or not
     *
     * @return an information about inserting success
     */
    public InsertMenuItemResult insertAction(final JMenu parentMenu,
                                               final JPopupMenu parentPopupMenu,
                                               final ProModAction proModAction,
                                               final MenuSeparator menuSeparator,
                                               final MenuItemPosition menuItemPosition,
                                               final boolean checkable);


}
