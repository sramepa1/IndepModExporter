/*
 * @(#)DemoIconsFactory.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

class DemoIconsFactory {

    public static class Attribute {
        public static final String NEW = "icons/new.png";
        public static final String BETA = "icons/beta.png";
        public static final String UPDATED = "icons/updated.png";
    }

    public static class Tree {
        public static final String FOLDER_OPENED = "icons/folder-opened.png";
        public static final String FOLDER_CLOSED = "icons/folder-closed.png";

        public static final String SORT = "icons/sort.png";
        public static final String TREE = "icons/tree.png";

        public static final String NODE = "icons/node.png";
        public static final String BASE = "icons/base.png";
        public static final String DOCK = "icons/dock.png";
        public static final String ACTION = "icons/action.png";
        public static final String COMPOENTS = "icons/components.png";
        public static final String GRIDS = "icons/grids.png";
        public static final String DIALOGS = "icons/dialogs.png";
        public static final String SHORTCUT = "icons/shortcut.png";
        public static final String PIVOT = "icons/pivot.png";
        public static final String CODE_EDITOR = "icons/code.png";
        public static final String FEED_READER = "icons/rss.png";
        public static final String DASHBOARD = "icons/dashboard.png";
        public static final String DATA = "icons/data.png";
        public static final String CHARTS = "icons/charts.png";
        public static final String GANTT = "icons/gantt.png";
    }

    public static class Frame {
        public static final String DEMO = "icons/demo.png";
        public static final String OPTIONS = "icons/options.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(DemoIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(DemoIconsFactory.class);
    }


}
