/*
 * @(#)LargeSortableTableDemo.java
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JidePopupMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Demoed Component: {@link SortableTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F:
 * any L&F
 */
public class LargeSortableTableDemo extends AbstractDemo {
    public DefaultTableModel _model;
    public SortableTable _sortableTable;

    public LargeSortableTableDemo() {
    }

    public static class LargeTableModel extends DefaultTableModel {

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
                case 0:
                    return Integer.class;
                case 1:
                    return Double.class;
                case 2:
                    return Boolean.class;
                case 3:
                    return String.class;
            }
            return Object.class;
        }
    }

    public String getName() {
        return "LargeSortableTable Demo";
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Insert a row") {
            public void actionPerformed(ActionEvent e) {
                int row = _sortableTable.getSelectedRow();
                if (row == -1) {
                    row = _sortableTable.getRowCount();
                }
                _model.insertRow(row, new Object[]{100000, Math.random(), Boolean.FALSE, new String("new row")});
                int visualRow = _sortableTable.getSortedRowAt(row);
                _sortableTable.changeSelection(visualRow, 0, false, false);
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Delete selected rows") {
            public void actionPerformed(ActionEvent e) {
                // _sortableTable.getSelectedRows() returns the row indices after sorted,
                // so we need to convert them to the actual row indices as in the actual table model 
                int[] rows = TableModelWrapperUtils.getActualRowsAt(_sortableTable.getModel(), _sortableTable.getSelectedRows(), true);
                for (int i = rows.length - 1; i >= 0; i--) {
                    int row = rows[i];
                    _model.removeRow(row);
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Delete all rows") {
            public void actionPerformed(ActionEvent e) {
                int rowCount = _model.getRowCount();
                _model.getDataVector().clear();
                _model.fireTableRowsDeleted(0, rowCount - 1);
            }
        }));
        return buttonPanel;
    }

    @Override
    public String getDescription() {
        return "This is a demo of SortableTable with a large table model. \n" +
                "\nClick once on the header to sort ascending, click twice to sort descending, a third time to unsort. Hold CTRL key then click on several headers to see mulitple columns sorting.\n" +
                "\nTo see the dynamic update when table model changes, press the buttons in options panel.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.SortableTable";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        _model = new LargeTableModel();
        _model.addColumn("int");
        _model.addColumn("double");
        _model.addColumn("boolean");
        _model.addColumn("string");
        for (int i = 0; i < 10000; i++) {
            _model.addRow(new Object[]{i * 1024, Math.random(), i % 2 == 0, "row" + i});
        }

        _sortableTable = new SortableTable(_model);
        _sortableTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int column = ((JTableHeader) e.getSource()).getColumnModel().getColumnIndexAtX(e.getPoint().x);
                JMenuItem[] menuItems = ((SortableTableModel) _sortableTable.getModel()).getPopupMenuItems(column);
                JPopupMenu popupMenu = new JidePopupMenu();
                for (JMenuItem item : menuItems) {
                    popupMenu.add(item);
                }
                popupMenu.show((Component) e.getSource(), e.getPoint().x, e.getPoint().y);
            }
        });

        _sortableTable.setPreferredScrollableViewportSize(new Dimension(550, 400));
        return new JScrollPane(_sortableTable);
    }

    @Override
    public String getDemoFolder() {
        return "G3.LargeSortableTable";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new LargeSortableTableDemo());
    }
}
