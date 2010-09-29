/*
 * @(#)SimpleScrollPaneDemo.java 12/13/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.SimpleScrollPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.SimpleScrollPane} <br> Required jar files:
 * jide-common.jar <br> Required L&F: any L&F
 */
public class SimpleScrollPaneDemo extends AbstractDemo {
    private SimpleScrollPane _pane;

    public SimpleScrollPaneDemo() {
    }

    public String getName() {
        return "SimpleScrollPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "SimpleScrollPaneDemo is a simplied version of JScrollPane. It simply adds four buttons on each side to do the scrolling." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.SimpleScrollPane";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(700, 400));
        panel.setLayout(new BorderLayout());
        panel.add(createTabbedPane(), BorderLayout.CENTER);
        return panel;
    }

    private Component createTabbedPane() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.addTab("SimpleScrollPane", createFlatScrollPane());
        return tabbedPane;
    }


    @Override
    public Component getOptionsPanel() {
        final JComboBox h = new JComboBox(new Object[]{"Show as needed", "Never shown", "Always show"});
        h.setSelectedIndex(_pane.getHorizontalScrollBarPolicy() - SimpleScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        h.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pane.setHorizontalScrollBarPolicy(SimpleScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED + h.getSelectedIndex());
            }
        });

        final JComboBox v = new JComboBox(new Object[]{"Show as needed", "Never shown", "Always show"});
        v.setSelectedIndex(_pane.getVerticalScrollBarPolicy() - SimpleScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        v.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pane.setVerticalScrollBarPolicy(SimpleScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED + v.getSelectedIndex());
            }
        });

        final JCheckBox rollover = new JCheckBox("Scroll on Rollover");
        rollover.setSelected(_pane.isScrollOnRollover());
        rollover.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pane.setScrollOnRollover(rollover.isSelected());
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        panel.add(new JLabel("Horizontal Scrolling:"));
        panel.add(h);
        panel.add(new JLabel("Vertical Scrolling:"));
        panel.add(v);
        panel.add(rollover);
        return panel;
    }

    class DummyTableModel extends AbstractTableModel {
        public int getRowCount() {
            return 30;
        }

        public int getColumnCount() {
            return 12;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "" + rowIndex + "," + columnIndex;
        }
    }

    private SimpleScrollPane createFlatScrollPane() {
        _pane = new SimpleScrollPane();
        _pane.setHorizontalScrollBarPolicy(SimpleScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        _pane.setVerticalScrollBarPolicy(SimpleScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
// you can add a border around viewport.
//        _pane.setViewportBorder(BorderFactory.createLineBorder(Color.RED, 6));

        JTable table = new JTable(new DummyTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _pane.setViewportView(table);

        return _pane;
    }

    @Override
    public String getDemoFolder() {
        return "B8.JideScrollPane";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SimpleScrollPaneDemo());
    }
}
