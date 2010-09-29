/*
 * @(#)AbstractSpanTableDemo.java 6/27/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.CellSpanTable;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableStyleProvider;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class AutoCellMergeTableDemo extends AbstractDemo {
    public CellSpanTable _table1;
    public CellSpanTable _table2;
    public JLabel _message;
    protected DefaultTableModel _tableModel1;
    protected DefaultTableModel _tableModel2;
    protected int _pattern;

    public AutoCellMergeTableDemo() {
    }

    public String getName() {
        return "CellSpanTable Demo (Auto Cell Merge)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellSpanTable wiht autoCellMerge feature. The autoCellMerge feature is to merge cell automatically if the cells have the value." +
                "In this demo, we have option to set the autoCellMerge to merge different rows or to merge different columns.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CellSpanTable";
    }

    static CellStyle cellStyle = new CellStyle();

    static {
        cellStyle.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _tableModel1 = new DefaultTableModel(0, 5);
        _tableModel1.addRow(new Integer[]{1, 1, 1, 1, 1});
        _tableModel1.addRow(new Integer[]{1, 2, 3, 4, 5});
        _tableModel1.addRow(new Integer[]{3, 3, 3, 3, 3});
        _tableModel1.addRow(new Integer[]{1, 2, 3, 4, 5});
        _tableModel1.addRow(new Integer[]{5, 5, 5, 5, 5});

        _tableModel2 = new DefaultTableModel(0, 5);
        _tableModel2.addRow(new String[]{"abc", "abc", "abc", "abc", "abc"});
        _tableModel2.addRow(new String[]{"abc", "abc", "abc", "abc", "ghi"});
        _tableModel2.addRow(new String[]{"abc", "abc", "abc", "def", "ghi"});
        _tableModel2.addRow(new String[]{"abc", "abc", "def", "ghi", "ghi"});
        _tableModel2.addRow(new String[]{"abc", "def", "def", "ghi", "ghi"});

        _table1 = new SortableTable(_tableModel1);
        _table1.getTableHeader().setReorderingAllowed(false);
        _table1.setRowSelectionAllowed(true);
        _table1.setColumnSelectionAllowed(true);
        _table1.setTableStyleProvider(new TableStyleProvider() {
            public CellStyle getCellStyleAt(JTable table, int rowIndex, int columnIndex) {
                return cellStyle;
            }
        });

        _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS);

        _table1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table1.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        _table2 = new SortableTable(_tableModel2);
        _table2.getTableHeader().setReorderingAllowed(false);
        _table2.setRowSelectionAllowed(true);
        _table2.setColumnSelectionAllowed(true);
        _table2.setTableStyleProvider(new TableStyleProvider() {
            public CellStyle getCellStyleAt(JTable table, int rowIndex, int columnIndex) {
                return cellStyle;
            }
        });

        _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS);

        _table2.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table2.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JPanel tablePanel = new JPanel(new GridLayout(2, 1, 6, 6));
        tablePanel.add(new JScrollPane(_table1), BorderLayout.CENTER);
        tablePanel.add(new JScrollPane(_table2), BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JRadioButton[] buttons = new JRadioButton[5];
        buttons[0] = new JRadioButton(new AbstractAction("Auto Merge Different Rows") {
            private static final long serialVersionUID = -2558770459399653699L;

            public void actionPerformed(ActionEvent e) {
                _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS);
                _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS);
            }
        });
        buttons[0].setToolTipText("Auto-merge cells from different rows if the values are the cells");

        buttons[1] = new JRadioButton(new AbstractAction("Auto Merge Different Columns") {
            private static final long serialVersionUID = -1356909969843635810L;

            public void actionPerformed(ActionEvent e) {
                _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_COLUMNS);
                _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_COLUMNS);
            }
        });
        buttons[1].setToolTipText("Auto-merge cells from different columns if the values are the cells");

        buttons[2] = new JRadioButton(new AbstractAction("Auto Merge Different Rows (Limited by the cell span in the previous column)") {
            private static final long serialVersionUID = 297898692656519741L;

            public void actionPerformed(ActionEvent e) {
                _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
                _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
            }
        });
        buttons[2].setToolTipText("Auto-merge cells from different rows if the values are the cells");

        buttons[3] = new JRadioButton(new AbstractAction("Auto Merge Different Columns (Limited by the cell span in the previous row") {
            private static final long serialVersionUID = -8412209959797799286L;

            public void actionPerformed(ActionEvent e) {
                _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_COLUMNS_LIMITED);
                _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_COLUMNS_LIMITED);
            }
        });
        buttons[3].setToolTipText("Auto-merge cells from different columns if the values are the cells");

        buttons[4] = new JRadioButton(new AbstractAction("None") {
            private static final long serialVersionUID = 297898692656519741L;

            public void actionPerformed(ActionEvent e) {
                _table1.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_OFF);
                _table2.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_OFF);
            }
        });
        buttons[4].setToolTipText("Do not auto-merge cells");

        buttons[0].setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(_pattern == i);
            panel.add(buttons[i]);
            buttonGroup.add(buttons[i]);
        }
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G16.SpanTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AutoCellMergeTableDemo());
    }
}