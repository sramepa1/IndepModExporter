/*
 * @(#)ScrollPaneIconsFactory.java 6/5/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products. Those icons are copyrighted by JIDE Software, Inc.
 */
public class ScrollPaneIconsFactory {

    public static class View {
        public static final String NORMAL = "icons/view_normal.png";
        public static final String OUTLINE = "icons/view_outline.png";
        public static final String PRINT_LAYOUT = "icons/view_print_layout.png";
        public static final String READING_LAYOUT = "icons/view_reading_layout.png";
        public static final String WEB_LAYOUT = "icons/view_web_layout.png";
    }

    public static class Scroll {
        public static final String UP = "icons/scroll_up.png";
        public static final String DOWN = "icons/scroll_down.png";
        public static final String SELECT = "icons/scroll_select.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(ScrollPaneIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(ScrollPaneIconsFactory.class);
    }


}
