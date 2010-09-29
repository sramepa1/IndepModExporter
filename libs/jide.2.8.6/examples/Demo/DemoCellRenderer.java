/*
 * @(#)DemoCellRenderer.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.tree.StyledTreeCellRenderer;
import com.jidesoft.utils.ProductNames;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 */
class DemoCellRenderer extends StyledTreeCellRenderer {
    public static final Icon ICON_FOLDER_CLOSED = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.FOLDER_CLOSED);
    public static final Icon ICON_FOLDER_OPENED = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.FOLDER_OPENED);

    public static final Icon ICON_NODE = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.NODE);
    public static final Icon ICON_BASE = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.BASE);
    public static final Icon ICON_DOCK = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.DOCK);
    public static final Icon ICON_ACTION = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.ACTION);
    public static final Icon ICON_COMPOENTS = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.COMPOENTS);
    public static final Icon ICON_GRIDS = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.GRIDS);
    public static final Icon ICON_DIALOGS = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.DIALOGS);
    public static final Icon ICON_SHORTCUT = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.SHORTCUT);
    public static final Icon ICON_PIVOT = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.PIVOT);
    public static final Icon ICON_CODE_EDITOR = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.CODE_EDITOR);
    public static final Icon ICON_FEED_READER = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.FEED_READER);
    public static final Icon ICON_DASHBOARD = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.DASHBOARD);
    public static final Icon ICON_DATA = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.DATA);
    public static final Icon ICON_CHARTS = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.CHARTS);
    public static final Icon ICON_GANTT = DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.GANTT);

    public static final Icon ICON_NEW = DemoIconsFactory.getImageIcon(DemoIconsFactory.Attribute.NEW);
    public static final Icon ICON_BETA = DemoIconsFactory.getImageIcon(DemoIconsFactory.Attribute.BETA);
    public static final Icon ICON_UPDATED = DemoIconsFactory.getImageIcon(DemoIconsFactory.Attribute.UPDATED);

    private static JLabel NEW = new JLabel(ICON_NEW);
    private static JLabel BETA = new JLabel(ICON_BETA);
    private static JLabel UPDATED = new JLabel(ICON_UPDATED);

    private final Color COLOR_NEW = new Color(10, 119, 0);
    private final Color COLOR_UPDATED = new Color(0, 50, 160);
    private final Color COLOR_BETA = new Color(150, 0, 10);

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DemoCellRendererPanel renderer = new DemoCellRendererPanel();

        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode && component instanceof JLabel) {
            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            JLabel label = ((JLabel) component);
            label.setOpaque(false);
            label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            renderer.removeAll();
            renderer.setLabel(label);
            if (obj instanceof Demo) {
                Demo demo = (Demo) obj;
                label.setText(demo.getName());

                if (label instanceof StyledLabel) {
                    if ((demo.getAttributes() & Demo.ATTRIBUTE_NEW) != 0) {
                        ((StyledLabel) label).addStyleRange(new StyleRange(COLOR_NEW));
                    }
                    if ((demo.getAttributes() & Demo.ATTRIBUTE_UPDATED) != 0) {
                        ((StyledLabel) label).addStyleRange(new StyleRange(COLOR_UPDATED));
                    }
                    if ((demo.getAttributes() & Demo.ATTRIBUTE_BETA) != 0) {
                        ((StyledLabel) label).addStyleRange(new StyleRange(COLOR_BETA));
                    }
                }

                label.setIcon(getProductIcon(demo.getProduct()));

                if ((demo.getAttributes() & Demo.ATTRIBUTE_NEW) != 0) {
                    renderer.add(NEW);
                }
                if ((demo.getAttributes() & Demo.ATTRIBUTE_UPDATED) != 0) {
                    renderer.add(UPDATED);
                }
                if ((demo.getAttributes() & Demo.ATTRIBUTE_BETA) != 0) {
                    renderer.add(BETA);
                }
            }
            else if (obj instanceof String) {
                label.setText(((String) obj));
                if (expanded) {
                    label.setIcon(ICON_FOLDER_OPENED);
                }
                else {
                    label.setIcon(ICON_FOLDER_CLOSED);
                }
            }
        }

        return renderer;
    }

    public static Icon getProductIcon(String product) {
        if (ProductNames.PRODUCT_NAME_COMMON.equals(product)) {
            return ICON_BASE;
        }
        else if (ProductNames.PRODUCT_NAME_DOCK.equals(product)) {
            return ICON_DOCK;
        }
        else if (ProductNames.PRODUCT_NAME_ACTION.equals(product)) {
            return ICON_ACTION;
        }
        else if (ProductNames.PRODUCT_NAME_COMPONENTS.equals(product)) {
            return ICON_COMPOENTS;
        }
        else if (ProductNames.PRODUCT_NAME_GRIDS.equals(product)) {
            return ICON_GRIDS;
        }
        else if (ProductNames.PRODUCT_NAME_DIALOGS.equals(product)) {
            return ICON_DIALOGS;
        }
        else if (ProductNames.PRODUCT_NAME_SHORTCUT.equals(product)) {
            return ICON_SHORTCUT;
        }
        else if (ProductNames.PRODUCT_NAME_PIVOT.equals(product)) {
            return ICON_PIVOT;
        }
        else if (ProductNames.PRODUCT_NAME_CODE_EDITOR.equals(product)) {
            return ICON_CODE_EDITOR;
        }
        else if (ProductNames.PRODUCT_NAME_FEEDREADER.equals(product)) {
            return ICON_FEED_READER;
        }
        else if (ProductNames.PRODUCT_NAME_DASHBOARD.equals(product)) {
            return ICON_DASHBOARD;
        }
        else if (ProductNames.PRODUCT_NAME_DATAGRIDS.equals(product)) {
            return ICON_DATA;
        }
        else if (ProductNames.PRODUCT_NAME_CHARTS.equals(product)) {
            return ICON_CHARTS;
        }
        else if (ProductNames.PRODUCT_NAME_GANTT_CHART.equals(product)) {
            return ICON_GANTT;
        }
        return ICON_NODE;
    }
}
