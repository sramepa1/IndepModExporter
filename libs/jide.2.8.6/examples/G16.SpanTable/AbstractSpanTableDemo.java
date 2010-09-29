/*
 * @(#)AbstractSpanTableDemo.java 6/27/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class AbstractSpanTableDemo extends AbstractDemo {
    public JideTable _table;
    public JLabel _message;
    protected AbstractTableModel _tableModel;
    protected int _pattern;

    public AbstractSpanTableDemo() {
    }

    public String getName() {
        return "CellSpanTable Demo (Predefined Span)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellSpanTable. CellSpanTable is a JTable that supports cell spans. Different from the DefaultSpanTableDemo, in this demo, " +
                "we programmatically control the cell spans.\n" +
                "\nYou can define the cell spans whatever you want by returning CellSpan value in getCellSpanAt() method of AbstractSpanTableModel. We defined several cell span styles just to show you what it can do.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractSpanTableModel\n" +
                "com.jidesoft.grid.CellSpanTable";
    }

    static CellStyle cellStyle = new CellStyle();

    static {
        cellStyle.setHorizontalAlignment(SwingConstants.CENTER);
    }

    class SpanTableTableModel extends AbstractSpanTableModel implements StyleModel {
        public int getRowCount() {
            return 100;
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

        @Override
        public CellSpan getCellSpanAt(int rowIndex, int columnIndex) {
            if (_pattern == 0) {
                if ((rowIndex % 3) != 2 && (columnIndex % 3) != 2) {
                    return new CellSpan(rowIndex - rowIndex % 3, columnIndex - columnIndex % 3, 2, 2);
                }
            }
            else if (_pattern == 1) {
                if (rowIndex % 3 == 0) {
                    return new CellSpan(rowIndex, 0, 1, getColumnCount());
                }
            }
            else if (_pattern == 2) {
                if (columnIndex % 3 == 0) {
                    return new CellSpan(0, columnIndex, getRowCount(), 1);
                }
            }
            else if (_pattern == 3) {
                if (rowIndex % 7 == 0 || rowIndex % 10 == 7) {
                    return new CellSpan(rowIndex, 0, 1, getColumnCount());
                }
            }
            return super.getCellSpanAt(rowIndex, columnIndex);
        }

        @Override
        public boolean isCellSpanOn() {
            return true;
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            return cellStyle;
        }

        public boolean isCellStyleOn() {
            return true;
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

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JRadioButton[] buttons = new JRadioButton[5];
        buttons[0] = new JRadioButton(new AbstractAction("Two of every three cells") {
            public void actionPerformed(ActionEvent e) {
                changePattern(0);
            }
        });
        buttons[0].setToolTipText("Span two rows and columns for every three of them");
        buttons[1] = new JRadioButton(new AbstractAction("Every third row") {
            public void actionPerformed(ActionEvent e) {
                changePattern(1);
            }
        });
        buttons[1].setToolTipText("Span the whole row if row index can be divided by 3");
        buttons[2] = new JRadioButton(new AbstractAction("Every third columns") {
            public void actionPerformed(ActionEvent e) {
                changePattern(2);
            }
        });
        buttons[2].setToolTipText("Span the whole column if column index can be divided by 3");
        buttons[3] = new JRadioButton(new AbstractAction("Magic 7 (row index can be divided by 7 or ends with 7)") {
            public void actionPerformed(ActionEvent e) {
                changePattern(3);
            }
        });
        buttons[3].setToolTipText("Span the whole row if row index can be divided by 7 or ends with 7)");
        buttons[4] = new JRadioButton(new AbstractAction("None") {
            public void actionPerformed(ActionEvent e) {
                changePattern(4);
            }
        });
        buttons[4].setToolTipText("No span");

        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(_pattern == i);
            panel.add(buttons[i]);
            buttonGroup.add(buttons[i]);
        }
        return panel;
    }

    private void changePattern(int pattern) {
        _pattern = pattern;
        ((CellSpanTable) _table).invalidateCellSpanCache();
        _table.repaint();
    }

    @Override
    public String getDemoFolder() {
        return "G16.SpanTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AbstractSpanTableDemo());
    }
}

