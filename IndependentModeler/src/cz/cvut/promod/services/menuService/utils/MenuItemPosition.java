package cz.cvut.promod.services.menuService.utils;

import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 1:46:34, 2.2.2010
 */

/**
 * Specifies the menu item hierarchical and relative or absolute position in menu.
 */
public final class MenuItemPosition{

    private static final Logger LOG = Logger.getLogger(MenuItemPosition.class);

    /**
     * Specifies relative menu item position
     */
    public static enum PlacementStyle {
        /** First item in menu. */
        FIRST,

        /** Last item in menu. */
        LAST,

        /** Group menu item having the same text. Last if there is no name duplicity. */
        GROUPED
    }

    private final PositionInfo positionInfo;
    private final String hierarchicalPlacement;

    /**
     * Constructs new MenuItemPosition holding hierarchical placement information.
     *
     * @param hierarchicalPlacement is the info about hierarchical position for new menu item
     */
    public MenuItemPosition(final String hierarchicalPlacement){
        positionInfo = new PositionInfo();
        this.hierarchicalPlacement = hierarchicalPlacement;
    }

    /**
     * Constructs new MenuItemPosition holding hierarchical placement information and relative position.
     *
     * @param hierarchicalPlacement is the info about hierarchical position for new menu item
     *
     * @param placementStyle defines relative menu item position in the menu
     */
    public MenuItemPosition(final String hierarchicalPlacement, final PlacementStyle placementStyle){
        this(hierarchicalPlacement);

        positionInfo.setRelativePosition(placementStyle);
    }

    /**
     * Constructs new MenuItemPosition holding hierarchical placement information and absolute position.
     *
     * @param hierarchicalPlacement is the info about hierarchical position for new menu item
     *
     * @param position defines absolute menu item position in the menu
     */
    public MenuItemPosition(final String hierarchicalPlacement, final int position){
        this(hierarchicalPlacement);

        if(position < 0){
            LOG.error("Wrong menu position for menu item. Position has been set to zero.");
            positionInfo.setPosition(0);
        } else {
            positionInfo.setPosition(position);
        }
    }

    /**
     * Returns required hierarchical placement information.
     *
     * @return hierarchical placement information
     */
    public String getHierarchicalPlacement() {
        return hierarchicalPlacement;
    }

    /**
     * Returns absolute position for menu item.
     *
     * @return absolute position
     */
    public int getPosition() {
        return positionInfo.getAbsolutePosition();
    }

    /**
     * Returns relative position for menu item.
     *
     * @return relative position
     */
    public PlacementStyle getPlacementStyle() {
        return positionInfo.getRelativePosition();
    }

    /**
     * Returns information about relative and/or absolute position.
     *
     * @return information about relative and/or absolute position
     */
    public PositionInfo getPositionInfo() {
        return positionInfo;
    }

    /**
     * Holds the absolute and/or relative position information.
     */
    public static final class PositionInfo{

        /**
         * If position > 0 -> then use position (absolute positioning), else use placementStyle (relative positioning)
         */
        private int position = -1;

        /**
         * Relative positioning.
         */
        private PlacementStyle placementStyle = PlacementStyle.LAST;

        /**
         * Returns absolute positioning information.
         *
         * @return absolute positioning information
         */
        public int getAbsolutePosition() {
            return position;
        }

        /**
         * Returns relative positioning information.
         *
         * @return relative positioning information
         */
        public PlacementStyle getRelativePosition() {
            return placementStyle;
        }

        /**
         * Sets absolute positioning information.
         *
         * @param position is the new absolute position
         */
        public void setPosition(final int position) {
            this.position = position;
        }

        /**
         * Sets relative positioning information.
         *
         * @param placementStyle is the new relative position
         */
        public void setRelativePosition(final PlacementStyle placementStyle) {
            this.placementStyle = placementStyle;
        }
    }
}
