/*
 * @(#)SpanTableDemo.java 6/9/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class DefaultSpanTableDemo extends AbstractDemo {
    public JideTable _table;
    public JLabel _message;
    protected DefaultSpanTableModel _tableModel;

    public DefaultSpanTableDemo() {
    }

    public String getName() {
        return "CellSpanTable Demo (Custom)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellSpanTable. CellSpanTable is a JTable that supports cell spans. \n" +
                "\nTo see cell span, select an area and click on \"Merge selected cells\" to make merge selected cells into one cell. This is what we called CellSpan.\n" +
                "\nTo split an existing cell span, select the CellSpan and click on \"Split selected cell\".\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.DefaultSpanTableModel\n" +
                "com.jidesoft.grid.CellSpanTable";
    }

    class SpanTableTableModel extends DefaultSpanTableModel {
        @Override
        public int getRowCount() {
            return 100;
        }

        @Override
        public int getColumnCount() {
            return 9;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return "" + rowIndex + "," + columnIndex;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _tableModel = new SpanTableTableModel();
        _table = new SortableTable(_tableModel);
        _table.getTableHeader().setReorderingAllowed(false);
        _table.setRowSelectionAllowed(true);
        _table.setColumnSelectionAllowed(true);
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        // add example spans
        _tableModel.addCellSpan(new CellSpan(3, 3, 5, 5));
        _tableModel.addCellSpan(new CellSpan(11, 1, 2, 2));

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Merge selected cells") {
            public void actionPerformed(ActionEvent e) {
                int[] columns = _table.getSelectedColumns();
                int[] rows = _table.getSelectedRows();
                if (rows.length <= 1 && columns.length <= 1) {
                    return;
                }
                CellSpan cellSpan = new CellSpan(rows[0], columns[0], rows.length, columns.length);
                _tableModel.addCellSpan(((CellSpanTable) _table).convertViewCellSpanToModel(cellSpan));
                _table.clearSelection();
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Split selected cell") {
            public void actionPerformed(ActionEvent e) {
                int[] columns = _table.getSelectedColumns();
                int[] rows = _table.getSelectedRows();
                if (rows.length >= 1 && columns.length >= 1) {
                    for (int row : rows) {
                        for (int column : columns) {
                            _tableModel.removeCellSpan(row, column);
                        }
                    }
                    _table.clearSelection();
                }
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Turn on/off cell span") {
            public void actionPerformed(ActionEvent e) {
                _tableModel.setCellSpanOn(!_tableModel.isCellSpanOn());
            }
        }));
        return buttonPanel;
    }

    @Override
    public String getDemoFolder() {
        return "G16.SpanTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DefaultSpanTableDemo());
    }
}

