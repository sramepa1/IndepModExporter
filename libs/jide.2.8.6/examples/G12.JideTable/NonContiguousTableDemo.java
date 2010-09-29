/*
 * @(#)NonContiguousTableDemo.java 9/7/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableColumnChooserPopupMenuCustomizer;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class NonContiguousTableDemo extends AbstractDemo {
    public JideTable _table;
    protected AbstractTableModel _tableModel;

    public NonContiguousTableDemo() {
    }

    public String getName() {
        return "Non-contiguous Selection Table Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JideTable with setNonContiguousCellSelection(true).\n" +
                "\nYou can hold CTRL key and click a cell to add it to existing selection. " +
                "Or you can hold SHIFT key to add a range of cells to selection. You can also use mouse to drag a rectangle to select all cells in the rectangle.\n" +
                "\nSince JideTable is the base of all other table components in JIDE Grids, all of them will have this feature.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.JideTable";
    }

    class JideTableModel extends AbstractTableModel {
        public int getRowCount() {
            return 10000;
        }

        public int getColumnCount() {
            return 9;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "" + rowIndex + "," + columnIndex;
        }
    }

    @Override
    public Component getOptionsPanel() {
        final JCheckBox nonContiguous = new JCheckBox("Enable NonContiguousCellSelection");
        JPanel panel = new JPanel(new GridLayout(0, 1, 3, 3));
        panel.add(nonContiguous);
        nonContiguous.setSelected(_table.isNonContiguousCellSelection());
        nonContiguous.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setNonContiguousCellSelection(nonContiguous.isSelected());
            }
        });
        return panel;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _tableModel = new JideTableModel();
        _table = new JideTable(_tableModel);
        _table.setNonContiguousCellSelection(true);
        _table.setRowSelectionAllowed(true);
        _table.setColumnSelectionAllowed(true);
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_table);
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G12.JideTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new NonContiguousTableDemo());
    }
}

