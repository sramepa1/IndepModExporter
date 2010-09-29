/*
 * @(#)JideSplitPaneDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.JideTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.JideSplitPane} <br> Required jar files: jide-common.jar <br> Required
 * L&F: Jide L&F extension required
 */
public class JideSplitPaneDemo extends AbstractDemo {
    private static final long serialVersionUID = 353898442975692071L;

    private static JideSplitPane _jideSplitPane;
    private static JSplitPane _jSplitPane;

    public JideSplitPaneDemo() {
        super();
    }

    public String getName() {
        return "JideSplitPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "JideSplitPane is a split pane that supports multiple splits. As you can see on your right, there is a JideSplitPane that splits into three panes. In fact, you can split it as many panes as you want.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideSplitPane\n";
    }

    public Component getDemoPanel() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.addTab("JideSplitPane", createSplitPane());
        tabbedPane.addTab("JSplitPane (for comparison)", createJSplitPane());
        return tabbedPane;
    }

    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));

        final JCheckBox option1 = new JCheckBox("Show collapse/expand buttons");
        switchPanel.add(option1);
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _jideSplitPane.setOneTouchExpandable(option1.isSelected());
                _jSplitPane.setOneTouchExpandable(option1.isSelected());
            }
        });

        final JCheckBox option2 = new JCheckBox("Set continuous layout");
        switchPanel.add(option2);
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _jideSplitPane.setContinuousLayout(option2.isSelected());
                _jSplitPane.setContinuousLayout(option2.isSelected());
            }
        });

        final JCheckBox option3 = new JCheckBox("Show gripper");
        switchPanel.add(option3);
        option3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _jideSplitPane.setShowGripper(option3.isSelected());
            }
        });

        final JCheckBox option4 = new JCheckBox("Drag resizable");
        switchPanel.add(option4);
        option4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _jideSplitPane.setDragResizable(option4.isSelected());
            }
        });
        option4.setSelected(_jideSplitPane.isDragResizable());

        return switchPanel;
    }

    @Override
    public String getDemoFolder() {
        return "B5.JideSplitPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideSplitPaneDemo());
    }

    private static JideSplitPane createSplitPane() {
        JTree tree = new JTree();
        JTable table = new JideTable(new DefaultTableModel(10, 3));
        JList list = new JList(new Object[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",}) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                Dimension size = super.getPreferredScrollableViewportSize();
                size.width = 100;
                return size;
            }
        };

        _jideSplitPane = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
        _jideSplitPane.setProportionalLayout(true);
        _jideSplitPane.add(new JScrollPane(tree), JideBoxLayout.FLEXIBLE);
        _jideSplitPane.add(new JScrollPane(table), JideBoxLayout.VARY);
        _jideSplitPane.add(new JScrollPane(list), JideBoxLayout.FLEXIBLE);
        return _jideSplitPane;
    }

    private static JSplitPane createJSplitPane() {
        JTree tree = new JTree();
        JTable table = new JTable(new DefaultTableModel(10, 3));

        _jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        _jSplitPane.setLeftComponent(new JScrollPane(tree));
        _jSplitPane.setRightComponent(new JScrollPane(table));
        return _jSplitPane;
    }
}